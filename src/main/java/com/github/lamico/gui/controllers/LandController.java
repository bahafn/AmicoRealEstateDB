package com.github.lamico.gui.controllers;

import com.github.lamico.managers.TabManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class LandController {

    @FXML
    private Button btRegisterNew;

    @FXML
    private TableView<?> tbvTable;

    @FXML
    private TableColumn<?, ?> tvBlock;

    @FXML
    private TableColumn<?, ?> tvPlot;

    @FXML
    private TableColumn<?, ?> tvPrNum;

    @FXML
    void handleRowSelection(MouseEvent event) {

    }
	@FXML
	void registerNewProperty(ActionEvent event) {
		getPropertyController().showRegisterScreen(TabManager.LAND);
	}

	public PropertyController getPropertyController() {
		return (PropertyController) MainController.getTabManager().getController(TabManager.PROPERTY);
	}
}
