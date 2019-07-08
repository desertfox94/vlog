package app;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringPropertyAdapter implements JsonSerializer<StringProperty>, JsonDeserializer<StringProperty> {

	@Override
	public JsonElement serialize(StringProperty src, Type typeOfSrc, JsonSerializationContext context) {
		String s = src.get();
		if (s == null) {
			s = "";
		}
		return new JsonPrimitive(s);
	}

	@Override
	public StringProperty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new SimpleStringProperty(json.getAsJsonPrimitive().getAsString());
	}

}
