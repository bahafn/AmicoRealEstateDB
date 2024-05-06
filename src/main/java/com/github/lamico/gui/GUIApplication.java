package com.github.lamico.gui;

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
		FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/demo.fxml"));
		Parent mainRoot = mainLoader.load();
		// MainController mainController = mainLoader.getController();
		

		Scene scene = new Scene(mainRoot);
		
		scene.getStylesheets().add(getClass().getResource("/css/root.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(563);
		primaryStage.setMinWidth(934);
		primaryStage.setTitle("Lamico Demo");

		primaryStage.show();
	}
}
