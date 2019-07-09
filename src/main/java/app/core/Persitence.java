package app.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.beans.property.StringProperty;

public class Persitence {

	public static Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(StringProperty.class, new StringPropertyAdapter());
		return gsonBuilder.create();
	}

}
