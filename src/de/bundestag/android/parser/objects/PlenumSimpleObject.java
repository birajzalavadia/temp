package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;

/**
 * Plenum simple object holder.
 * 
 * Contains the plenum simple (Plenum) data.
 */
public class PlenumSimpleObject
{
    private int type;
    private Date date;
    private String title;
    private String text;

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getDate()
    {
        return DateHelper.getDateAsString(date);
    }

    public void setDate(String date)
    {
        this.date = DateHelper.parseDate(date);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public PlenumSimpleObject copy()
	{
        PlenumSimpleObject copy = new PlenumSimpleObject();

        copy.type = type;
        copy.date = date;
		copy.title = title;
		copy.text = text;

		return copy;
	}
}