package com.github.lamico.gui;

import com.github.lamico.managers.ResourceManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;	
import javafx.stage.Stage;

public class GUIApplication extends Application {

	/* Run from a different class (Main.java) */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader mainLoader = ResourceManager.getFXMLLoader("demo");
		Parent mainRoot = mainLoader.load();
		// MainController mainController = mainLoader.getController();
		

		Scene scene = new Scene(mainRoot);
		
		scene.getStylesheets().add(ResourceManager.getStylesheetURL("root"));
		
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(563);
		primaryStage.setMinWidth(934);
		primaryStage.setTitle("Lamico Demo");
		primaryStage.getIcons().add(ResourceManager.getImage("lamico_icon.png"));

		primaryStage.show();
	}
}
