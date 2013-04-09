package jp.co.ayuta.gs_sample.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import jp.co.ayuta.gs_sample.model.ImageFile;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;

public class ShowController extends Controller {

	@Override
	public Navigation run() throws Exception {

		Validators v = new Validators(request);
		v.add("id", v.required(), v.longType());
		if (!v.validate()) {
			response.setStatus(404);
			return null;
		}

		Key key = Datastore.createKey(ImageFile.class, asLong("id"));
		ImageFile image = Datastore.getOrNull(ImageFile.class, key);
		if (image == null) {
			response.setStatus(404);
			return null;
		}

		response.setContentType(image.getContentType());
		OutputStream out = response.getOutputStream();

		FileService fs = FileServiceFactory.getFileService();
		AppEngineFile file = new AppEngineFile("/gs/ayuta-dev-default/"
				+ image.getId());
		FileReadChannel readChannel = fs.openReadChannel(file, false);
		InputStream in = Channels.newInputStream(readChannel);
		byte[] buf = new byte[8192];
		int readBytes = 0;

		while ((readBytes = in.read(buf)) != -1) {
			out.write(buf, 0, readBytes);
		}
		out.flush();
		readChannel.close();

		return null;
	}
}
