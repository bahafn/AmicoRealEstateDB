package com.github.lamico.gui.controllers;

import com.github.lamico.db.managers.PropertyRegistrationManager;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;

public class LandRegisterController {

	@FXML
	private Button btRegister;

	@FXML
	private BorderPane root;

	@FXML
	private TextField txtBlock;

	@FXML
	private TextField txtPlot;

	@FXML
	void register(ActionEvent event) {
		int blockNum = Integer.parseInt(txtBlock.getText().strip());
		int plotNum = Integer.parseInt(txtPlot.getText().strip());
		try {
			PropertyRegistrationManager.registerLand(plotNum, blockNum);

			PropertyRegistrationManager.commitTransaction();

			((LandController) MainController.getTabManager().getController(TabManager.LAND)).show();
			MainController.getTabManager().switchTo(TabManager.LAND);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				PropertyRegistrationManager.rollbackTransaction();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@FXML
	void initialize() {
		root.setBackground(ResourceManager.getBackground("land.png"));
		restrictFields();
	}

	private void restrictFields() {
		txtBlock.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
		txtPlot.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
	}
}
