package com.github.lamico.managers;

import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class ResourceManager {

	private ResourceManager() {
	}

	public static FXMLLoader getFXMLLoader(String name) {
		String url = "/fxml/" + name;
		if(!url.contains(".fxml")) {
			url = url + ".fxml";
		}
		return new FXMLLoader(ResourceManager.class.getResource(url));
	}
	
	public static String getStylesheetURL(String name) {
		String url = "/css/" + name;
		if(!url.contains(".css")) {
			url = url + ".css";
		}
		return ResourceManager.class.getResource(url).toExternalForm();
	}
	
	public static Image getImage(String name) {
		String url = "/images/" + name;
		if(!url.contains(".")) {
			url = url + ".png";
		}
		InputStream is = ResourceManager.class.getResourceAsStream(url);
		if(is == null)
			return null;
		return new Image(is);
	}
}
