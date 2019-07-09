package app.controller;

import app.Application;
import app.model.Vlog;
import app.model.VlogEntry;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class MainController extends Controller {

	@FXML
	GridPane detailsStage;

	@FXML
	GridPane sidebar;

	private Vlog vlog = Vlog.load();

	@FXML
	TableView<VlogEntry> tableView;

	@FXML
	TableColumn<VlogEntry, String> dateColumn;

	@FXML
	TableColumn<VlogEntry, String> nameColumn;

	private DetailsController detailsController;

	public MainController() {
	}

	@Override
	public void show() {
		initTableView();
		vlog.addShutdownHook();
	}

	private void initTableView() {
		dateColumn.setCellValueFactory(cell -> cell.getValue().date());
		nameColumn.setCellValueFactory(cell -> cell.getValue().name());
		tableView.setItems(vlog.entriesProperty());
		tableView.setContextMenu(createContextMenu());
		tableView.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> {
			showEntryDetails(n);
		});
	}

	public void showEntryDetails(VlogEntry vlogEntry) {
		if (detailsController == null) {
			detailsController = (DetailsController)Application.show(getClass().getResource("../EntryDetails.fxml"));
			detailsStage.add(detailsController.getRoot(), 0, 0);
		}
		detailsController.showEntry(vlogEntry);
	}

	private ContextMenu createContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem item = new MenuItem("Löschen");
		item.setOnAction(e -> vlog.delete(tableView.getSelectionModel().getSelectedItem()));
		contextMenu.getItems().add(item);
		return contextMenu;
	}

	public void save() {
		vlog.save();
	}

	@FXML
	public void buttonPressed() {
	}

	@FXML
	public void createNewEntry() {
		VlogEntry entry = vlog.createEntry();
		tableView.getSelectionModel().select(entry);
	}

}
