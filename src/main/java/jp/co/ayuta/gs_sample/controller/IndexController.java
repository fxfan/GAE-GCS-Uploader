package jp.co.ayuta.gs_sample.controller;

import jp.co.ayuta.gs_sample.model.ImageFile;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class IndexController extends Controller {

	@Override
	protected Navigation run() throws Exception {
		requestScope("images", Datastore.query(ImageFile.class).asList());
		return forward("index.jsp");
	}

}
