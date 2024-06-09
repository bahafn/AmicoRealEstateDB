package com.github.lamico.gui.controllers;

import com.github.lamico.managers.TabManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class BuildingsController {

	@FXML
    private TableView<?> tbvTable;

    @FXML
    private TableColumn<?, ?> tvFloors;

    @FXML
    private TableColumn<?, ?> tvLand;

    @FXML
    private TableColumn<?, ?> tvName;

    @FXML
    private TableColumn<?, ?> tvPrNum;

    @FXML
    private TableColumn<?, ?> tvYear;

    @FXML
    void handleRowSelection(MouseEvent event) {
    }


	@FXML
	void registerNewProperty(ActionEvent event) {
		getPropertyController()
				.showRegisterScreen(TabManager.BUILDINGS);
	}

	public PropertyController getPropertyController() {
		return (PropertyController) MainController.getTabManager().getController(TabManager.PROPERTY);
	}

	@FXML
	void initialize() {
	}
}
