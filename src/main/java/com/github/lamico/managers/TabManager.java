package com.github.lamico.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class TabManager {
	public static final String BUILDINGS = "buildings";
	public static final String TRANSACTIONS = "transactions";
	public static final String CONTRACTS = "contracts";
	public static final String LAND = "land";
	public static final String APARTMENTS = "apartments";
	public static final String BROKERS = "brokers";
	public static final String CLIENTS = "clients";
	public static final String EMPLOYEES = "employees";
	public static final String DASHBOARD = "dashboard";
	private Map<String, Node> nodeMap = new HashMap<>();
	private BorderPane root;

	public TabManager(BorderPane pane) throws IOException {
		this.root = pane;
		initialise();
	}

	private void initialise() throws IOException {
		nodeMap.put(BUILDINGS, ResourceManager.getFXMLLoader(BUILDINGS).load());
	}

	public void switchTo(String tab) {
		Node node = nodeMap.get(tab);
		root.setCenter(node);
	}

}
