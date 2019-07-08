package app;

import javafx.fxml.FXML;
import javafx.scene.Parent;

public abstract class Controller {

	@FXML
	protected Parent root;

	public void show() {

	}

	public Parent getRoot() {
		return root;
	}

	public void setRoot(Parent root) {
		this.root = root;
	}

}
