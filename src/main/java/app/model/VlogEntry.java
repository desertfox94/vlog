package app.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class VlogEntry {

	private StringProperty name = new SimpleStringProperty();

	private StringProperty tags = new SimpleStringProperty();

	private StringProperty description = new SimpleStringProperty();

	private List<VlogFile> files = new ArrayList<>();

	private transient ObservableList<VlogFile> filesProperty;

	private StringProperty date = new SimpleStringProperty();

	public StringProperty name() {
		return name;
	}

	public StringProperty tags() {
		return tags;
	}

	public StringProperty description() {
		return description;
	}

	public StringProperty date() {
		return date;
	}

	public ObservableList<VlogFile> filesProperty() {
		if (filesProperty == null) {
			filesProperty = FXCollections.observableArrayList(files);
			filesProperty.addListener(new ListChangeListener<VlogFile>() {

				@Override
				public void onChanged(Change<? extends VlogFile> c) {
					files = filesProperty.subList(0, filesProperty.size());
				}
			});
		}
		return filesProperty;
	}

	public void addFile(File file) {
		String name = file.getName();
		name = date.get() + "_" + (filesProperty().size() + 1) + name.substring(name.indexOf('.'));
		try {
			File dest = Vlog.file(name);
			Files.copy(file.toPath(), dest.toPath());
			filesProperty().add(new VlogFile(name, dest));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
