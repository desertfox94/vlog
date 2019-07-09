package app.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VlogFile {

	private StringProperty name = new SimpleStringProperty();

	private StringProperty date = new SimpleStringProperty();

	private transient File file;

	public VlogFile() {
	}

	public VlogFile(String name, File file) {
		this.name.set(name);
		;
		try {
			FileTime creationTime = Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime();
			this.date.set(creationTime.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.file = file;
	}

	public StringProperty getName() {
		return name;
	}

	public StringProperty getDate() {
		return date;
	}

	public File getFile() {
		if (file == null) {
			file = Vlog.file(name.get());
		}
		return file;
	}

	public boolean delete() {
		return file.delete();
	}

}
