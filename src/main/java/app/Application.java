package app;

import java.io.IOException;
import java.net.URL;

import app.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

	private Stage stage;

	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		try {
			controller = show(getClass().getResource("Main.fxml"));
			primaryStage.setTitle("Vlog");
			//			primaryStage.getIcons().add(App.icon());
			Scene scene = new Scene(controller.getRoot(), 800, 490);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	public static Controller show(URL location) {
		try {
			FXMLLoader loader = new FXMLLoader(location);
			Parent root = loader.load();
			Controller controller = loader.getController();
			controller.setRoot(root);
			controller.show();
			return controller;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
