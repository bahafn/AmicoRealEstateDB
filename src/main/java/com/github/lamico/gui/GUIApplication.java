package com.github.lamico.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUIApplication extends Application {

	// Run from a different class (Main)
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label helloLabel = new Label("Hello, World!");

		StackPane root = new StackPane();
		root.getChildren().add(helloLabel);

		Scene scene = new Scene(root, 300, 200);

		primaryStage.setScene(scene);

		primaryStage.setTitle("HelloWorldApp");

		primaryStage.show();
	}
}
