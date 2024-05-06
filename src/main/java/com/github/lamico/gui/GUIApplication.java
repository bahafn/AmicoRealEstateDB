package com.github.lamico.gui;

import com.github.lamico.gui.controllers.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIApplication extends Application {

	// Run from a different class (Main)
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/root.fxml"));
		Parent mainRoot = mainLoader.load();
		// MainController mainController = mainLoader.getController();

		Scene scene = new Scene(mainRoot, 300, 200);

		primaryStage.setScene(scene);

		primaryStage.setTitle("HelloWorld");

		primaryStage.show();
	}
}
