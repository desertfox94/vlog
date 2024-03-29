package app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import app.core.LocaleDateConverter;
import app.model.VlogEntry;
import app.model.VlogFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

public class DetailsController extends Controller {

	private static final int DEFAULT_WIDTH = 365;

	private static final int DEFAULT_HEIGHT = 200;

	@FXML
	TextField name;

	@FXML
	TextArea description;

	@FXML
	MediaView mediaView;

	boolean newEntry = false;

	private VlogEntry vlogEntry;

	private LocaleDateConverter dateConverter = new LocaleDateConverter();

	private ChangeListener<? super LocalDate> dateChangeListener = (p, o, n) -> {
		this.vlogEntry.date().set(dateConverter.toString(n));
	};

	@FXML
	DatePicker datePicker;

	@FXML
	TableView<VlogFile> filesTableView;

	@FXML
	TableColumn<VlogFile, String> fileNameColumn;

	@FXML
	TableColumn<VlogFile, String> fileMimeTypeColumn;

	@FXML
	TableColumn<VlogFile, String> fileSizeColumn;

	@FXML
	TableColumn<VlogFile, String> fileCommentColumn;

	@Override
	public void show() {
		Locale.setDefault(Locale.GERMANY);
		fileNameColumn.setCellValueFactory(cell -> cell.getValue().getName());
		fileMimeTypeColumn.setCellValueFactory(cell -> {
			try {
				return new SimpleStringProperty(Files.probeContentType(cell.getValue().getFile().toPath()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});
		fileSizeColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getFile().length())));
		fileCommentColumn.setCellValueFactory(cell -> null);

		filesTableView.setContextMenu(createContextMenu());

		filesTableView.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> {
			if (n == null) {
				return;
			}
			Media media = new Media(n.getFile().toURI().toString());
			final MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);
			mediaPlayer.setAutoPlay(true);
			System.out.println();
		});
	}

	public void showEntry(VlogEntry vlogEntry) {
		if (this.vlogEntry != null) {
			reset();
		}
		this.vlogEntry = vlogEntry;
		if (this.vlogEntry == null) {
			return;
		}
		filesTableView.setItems(vlogEntry.filesProperty());
		name.textProperty().bindBidirectional(vlogEntry.name());
		datePicker.valueProperty().addListener(dateChangeListener);
		if (vlogEntry.date().get() == null) {
			datePicker.valueProperty().set(LocalDate.now());
		} else {
			datePicker.valueProperty().set(dateConverter.fromString(vlogEntry.date().get()));
		}
		description.textProperty().bindBidirectional(vlogEntry.description());
	}

	private ContextMenu createContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem item = new MenuItem("Entfernen");
		item.setOnAction(e -> vlogEntry.delete(filesTableView.getSelectionModel().getSelectedItem()));
		contextMenu.getItems().add(item);
		return contextMenu;
	}

	private void reset() {
		name.textProperty().unbindBidirectional(vlogEntry.name());
		name.textProperty().set(null);
		datePicker.valueProperty().removeListener(dateChangeListener);
		datePicker.setValue(null);
		description.textProperty().unbindBidirectional(vlogEntry.description());
		description.textProperty().set(null);
	}

	@FXML
	public void appendFiles() {
		FileChooser fileChooser = new FileChooser();
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		files.forEach(vlogEntry::addFile);
	}

	@FXML
	public void rotate() {
		double rotation = mediaView.rotateProperty().get();
		mediaView.rotateProperty().set(rotation == 0 ? 90.0 : 0);
	}

}
