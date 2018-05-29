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

    private static final String YEAR_LABEL="year";

    private static final String MONTH_YEAR_LABEL="month_year";

    private static final String YEAR_FORMAT = "yyyy";

    private static final String MONTH_YEAR_FORMAT = "MMM yyyy";

    /**
     *
     * This method takes in a date as a String and returns based on the new Format required
     * @param date is a string
     * @param newFormatType expects either "year" or "month_year"
     * @return If valid input, date in the corresponding format is returned, or else input date is returned
     *
     */
    public static String getDateAsFormattedString(String date, String newFormatType) {
        DateFormat dateFormat = new SimpleDateFormat(MOVIE_DATE_FORMAT);
        try {
            Date dateObj = dateFormat.parse(date);

            SimpleDateFormat newDateFormat;

            switch(newFormatType) {
                case YEAR_LABEL :
                    newDateFormat = new SimpleDateFormat(YEAR_FORMAT);
                    break;

                case MONTH_YEAR_LABEL:
                    newDateFormat = new SimpleDateFormat(MONTH_YEAR_FORMAT);
                    break;

                default:
                    return date;
            }

            String newDateString = newDateFormat.format(dateObj);
            return newDateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";
        }

    }
}
