package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Vlog {

	private List<VlogEntry> entries = new ArrayList<>();

	private transient ObservableList<VlogEntry> entriesProperty;

	private static final String VLOG_JSON_NAME = ".vlog";

	private static final File VLOG_JSON = FileUtil.relativeFile(VLOG_JSON_NAME);

	public void save() {
		String json = Persitence.getGson().toJson(this);
		FileUtil.write(VLOG_JSON, json);
	}

	public static Vlog load() {
		if (VLOG_JSON.exists()) {
			try {
				return Persitence.getGson().fromJson(FileUtil.read(VLOG_JSON), Vlog.class);
			} catch (JsonSyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new Vlog();
	}

	public VlogEntry createEntry() {
		VlogEntry entry = new VlogEntry();
		entriesProperty.add(entry);
		return entry;
	}

	public void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::save));
	}

	public ObservableList<VlogEntry> entriesProperty() {
		if (entriesProperty == null) {
			entriesProperty = FXCollections.observableArrayList(entries);
			entriesProperty.addListener(new ListChangeListener<VlogEntry>() {

				@Override
				public void onChanged(Change<? extends VlogEntry> c) {
					entries = entriesProperty.subList(0, entriesProperty.size());
				}
			});
		}
		return entriesProperty;
	}

	public static File file(String name) {
		File files = FileUtil.relativeFile("files");
		if (!files.exists()) {
			files.mkdir();
		}
		return new File(files, name);
	}

}
