package com.example.divya_user.popularmovies.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains the methods to manipulate the dates
 *
 */

public class DateUtils {

    private static final String MOVIE_DATE_FORMAT = "yyyy-MM-dd";

    /**
     *
     * This method takes in a date as a String and returns the year
     * @param date is a string
     * @return Year is returned as a String
     *
     */
    public static String getYearFromDateString(String date) {
        DateFormat dateFormat = new SimpleDateFormat(MOVIE_DATE_FORMAT);
        try {
            Date dateObj = dateFormat.parse(date);

            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String yearString = yearFormat.format(dateObj);
            return yearString;
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";
        }

    }
}
