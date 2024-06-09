package com.github.lamico.gui.controllers;

import com.github.lamico.db.managers.PropertyRegistrationManager;
import com.github.lamico.gui.utils.ParseUtils;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.github.lamico.gui.utils.TimedError;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;

public class LandRegisterController {
	private TimedError timedError = new TimedError();

	@FXML
	private Label lbError;

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
		int blockNum = ParseUtils.parseIntOrDefault(txtBlock.getText());
		int plotNum = ParseUtils.parseIntOrDefault(txtPlot.getText());
		;

		if (blockNum < 0 || plotNum < 0) {
			timedError.displayErrorMessage(lbError, "Empty Fields", 2);
			return;
		}
		
		try {
			PropertyRegistrationManager.registerLand(plotNum, blockNum);

			PropertyRegistrationManager.commitTransaction();

			((LandController) MainController.getTabManager().getController(TabManager.LAND)).show();
			MainController.getTabManager().switchTo(TabManager.LAND);
		} catch (SQLException e) {
			timedError.displayErrorMessage(lbError, "Failed to register", 2);
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
