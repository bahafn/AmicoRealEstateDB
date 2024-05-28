package com.github.lamico.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * The TabManager class is responsible for managing the tabs in the application.
 * It loads the FXML files for each tab and maps them to a string.
 * The switchTo method is used to switch between the tabs.
 */
public class TabManager {
	// Set the value to the fxml name for ease
	public static final String BUILDINGS = "buildings";
	public static final String TRANSACTIONS = "transactions";
	public static final String CONTRACTS = "contracts";
	public static final String LAND = "land";
	public static final String APARTMENTS = "apartments";
	public static final String OWNERS = "owners";
	public static final String BROKERS = "brokers";
	public static final String CLIENTS = "clients";
	public static final String EMPLOYEES = "employees";
	public static final String DASHBOARD = "dashboard";

	private Map<String, Node> nodeMap = new HashMap<>();
	private BorderPane root;

	/**
	 * Constructs a new TabManager with the given root BorderPane.
	 *
	 * @param pane the root BorderPane
	 * @throws IOException if an error occurs while loading the FXML files
	 */
	public TabManager(BorderPane pane) throws IOException {
		this.root = pane;
		initialise();
	}

	/**
	 * Loads the FXML files for each tab and maps them to a string.
	 *
	 * @throws IOException if an error occurs while loading the FXML files, or if
	 *                     they aren't found
	 */
	private void initialise() throws IOException {
		nodeMap.put(BUILDINGS, ResourceManager.getFXMLLoader(BUILDINGS).load());
		nodeMap.put(APARTMENTS, ResourceManager.getFXMLLoader(APARTMENTS).load());
		nodeMap.put(LAND, ResourceManager.getFXMLLoader(LAND).load());
		nodeMap.put(DASHBOARD, ResourceManager.getFXMLLoader(DASHBOARD).load());
	}

	/**
	 * Switches to the given tab.
	 *
	 * @param tab the name of the tab to switch to
	 */
	public void switchTo(String tab) {
		Node node = nodeMap.get(tab);
		root.setCenter(node);
	}

	/**
	 * Gets the given tab.
	 *
	 * @param tab the name of the tab to get
	 */
	public Node getTab(String tab) {
		return nodeMap.get(tab);
	}

	/**
	 * Gets the root BorderPane.
	 *
	 * @return the root BorderPane
	 */
	public BorderPane getRoot() {
		return root;
	}

	/**
	 * Sets the root BorderPane.
	 *
	 * @param root the new root BorderPane
	 */
	public void setRoot(BorderPane root) {
		this.root = root;
	}

}
