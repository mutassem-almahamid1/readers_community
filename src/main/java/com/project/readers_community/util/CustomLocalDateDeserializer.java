package com.project.readers_community.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Custom deserializer to handle different date formats from Google Books API
 * Supports ISO date format (yyyy-MM-dd), year-month format (yyyy-MM), and year-only format (yyyy)
 */
public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final Pattern YEAR_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern YEAR_MONTH_PATTERN = Pattern.compile("^\\d{4}-\\d{1,2}$");
    private static final Pattern FULL_DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$");

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateStr = parser.getText();
        
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        
        dateStr = dateStr.trim();
        
        try {
            // Case 1: Full date (YYYY-MM-DD)
            if (FULL_DATE_PATTERN.matcher(dateStr).matches()) {
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
            }
            
            // Case 2: Year and month (YYYY-MM)
            if (YEAR_MONTH_PATTERN.matcher(dateStr).matches()) {
                return LocalDate.parse(dateStr + "-01", DateTimeFormatter.ISO_DATE);
            }
            
            // Case 3: Year only (YYYY)
            if (YEAR_PATTERN.matcher(dateStr).matches()) {
                int year = Integer.parseInt(dateStr);
                return LocalDate.of(year, 1, 1);
            }
            
            // Case 4: Try to extract just the year (for formats like "1998-01-01T00:00:00Z")
            if (dateStr.length() > 4) {
                String yearStr = dateStr.substring(0, 4);
                if (YEAR_PATTERN.matcher(yearStr).matches()) {
                    int year = Integer.parseInt(yearStr);
                    return LocalDate.of(year, 1, 1);
                }
            }
            
            // Last attempt: try with ISO date parser
            return LocalDate.parse(dateStr);
            
        } catch (DateTimeParseException | NumberFormatException e) {
            // Log the error and provide a default date or null
            System.err.println("Could not parse date: " + dateStr + " - " + e.getMessage());
            return null;
        }
    }
}
