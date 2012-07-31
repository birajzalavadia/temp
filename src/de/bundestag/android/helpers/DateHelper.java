package de.bundestag.android.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date helper.
 * 
 * Contains static methods to parse date from string and back to date.
 */
public abstract class DateHelper
{
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private static SimpleDateFormat DATE_ARTICLE_FORMATTER = new SimpleDateFormat("yyyyMMddHHmmSS");

    private static SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        
    private static SimpleDateFormat DATE_TIME_PLENUM_FORMATTER = new SimpleDateFormat("HH:mm");

    public static Date parseDate(String date)
    {
        Date parsedDate = null;

        if ((date != null) && (date.length() == 10))
        {
            try
            {
                parsedDate = DATE_FORMATTER.parse(date.trim());
            } catch (ParseException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if ((date != null) && (date.length() == 16))
        {
            try
            {
                parsedDate = DATE_TIME_FORMATTER.parse(date.trim());
            } catch (ParseException e)
            {
                throw new RuntimeException(e);
            }
        }
        
        return parsedDate;
    }

    public static Date parseArticleDate(String date)
    {
        Date parsedDate = null;
        try
        {
        	if(date.length()>0){
        		
				parsedDate = DATE_ARTICLE_FORMATTER.parse(date.trim());
        	}
        } catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
        
        return parsedDate;
    }

    public static String getDateAsString(Date date)
    {
        return (date != null) ? DATE_FORMATTER.format(date) : "";
    }

    public static String getArticleDateAsString(Date date)
    {
        return (date != null) ? DATE_ARTICLE_FORMATTER.format(date) : "";
    }

    public static String getDateTimeAsString(Date date)
    {
        return (date != null) ? DATE_TIME_FORMATTER.format(date) : "";
    }

	public static String formatPlenumDate(Date currentDateTime) {
        return (currentDateTime != null) ? DATE_TIME_PLENUM_FORMATTER.format(currentDateTime) : "";
	}
}
