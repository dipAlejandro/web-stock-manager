package com.dahl.webstockmanager.formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

	 private final DateTimeFormatter formatter;

	    public LocalDateTimeFormatter(String pattern) {
	        this.formatter = DateTimeFormatter.ofPattern(pattern);
	    }
	
	@Override
	public String print(LocalDateTime object, Locale locale) {
		return object.format(formatter);
	}

	@Override
	public LocalDateTime parse(String text, Locale locale) throws ParseException {
		
		return LocalDateTime.parse(text, formatter);
	}

}
