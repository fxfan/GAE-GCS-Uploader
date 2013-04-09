package jp.co.ayuta.gs_sample.controller;

import java.io.OutputStream;
import java.nio.channels.Channels;

import jp.co.ayuta.gs_sample.model.ImageFile;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

public class UploadController extends Controller {

	@Override
	public Navigation run() throws Exception {

		Validators v = new Validators(request);
		v.add("title", v.required());
		v.add("file", v.required());
		if (!v.validate()) {
			requestScope("images", Datastore.query(ImageFile.class).asList());
			return forward("index.jsp");
		}

		Key key = Datastore.allocateId(ImageFile.class);
		String title = asString("title");
		FileItem item = requestScope("file");

		FileService fs = FileServiceFactory.getFileService();

		GSFileOptions options = new GSFileOptionsBuilder()
				.setBucket("ayuta-dev-default")
				.setKey(String.valueOf(key.getId()))
				.setMimeType(item.getContentType()).setAcl("public-read")
				.build();

		AppEngineFile file = fs.createNewGSFile(options);
		FileWriteChannel channel = fs.openWriteChannel(file, true);
		OutputStream out = Channels.newOutputStream(channel);
		out.write(item.getData());
		out.close();
		channel.closeFinally();

		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = bs.createGsBlobKey("/gs/ayuta-dev-default/"
				+ key.getId());

		ImagesService is = ImagesServiceFactory.getImagesService();
		String thumbnailUrl = is.getServingUrl(ServingUrlOptions.Builder
				.withBlobKey(blobKey));

		ImageFile imageFile = new ImageFile();
		imageFile.setKey(key);
		imageFile.setTitle(title);
		imageFile.setContentType(item.getContentType());
		imageFile.setBlobKey(blobKey);
		imageFile.setThumbnailUrl(thumbnailUrl);
		Datastore.put(imageFile);

		return redirect("index");
	}
}
