package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class LocaleDateConverter extends StringConverter<LocalDate> {

	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public String toString(LocalDate date) {
		return date == null ? null : dateFormat.format(date);
	}

	@Override
	public LocalDate fromString(String string) {
		return string == null ? null : LocalDate.parse(string, dateFormat);
	}

}
