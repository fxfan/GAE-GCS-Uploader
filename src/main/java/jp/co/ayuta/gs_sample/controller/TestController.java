package jp.co.ayuta.gs_sample.controller;

import java.io.OutputStream;
import java.nio.channels.Channels;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

public class TestController extends Controller {

	@Override
	public Navigation run() throws Exception {
		byte[] binary = "Hello".getBytes();
		FileService fs = FileServiceFactory.getFileService();
		String bucket = "ayuta-dev-default";
		String fileName = "hoge.jpg";

		GSFileOptions options = new GSFileOptionsBuilder().setBucket(bucket)
				.setKey(fileName).setMimeType("application/octet-stream")
				.setAcl("public-read").build();

		AppEngineFile file = fs.createNewGSFile(options);
		FileWriteChannel channel = fs.openWriteChannel(file, true);
		OutputStream out = Channels.newOutputStream(channel);
		out.write(binary);
		out.close();
		channel.closeFinally();

		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = bs.createGsBlobKey("/gs/" + bucket + "/" + fileName);

		ImagesService is = ImagesServiceFactory.getImagesService();
		String thumbnailUrl = is.getServingUrl(ServingUrlOptions.Builder
				.withBlobKey(blobKey));
		System.out.println(thumbnailUrl);

		return null;
	}
}
