package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;

/**
 * Visitors object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsItemObject implements Comparable<VisitorsItemObject>
{
    private Date date;

    private String type;

    private String id;

    private String url;

    private String imageString;

    public String getDate()
    {
        return DateHelper.getArticleDateAsString(date);
    }

    public void setDate(String date)
    {
        this.date = DateHelper.parseArticleDate(date);
    }

	public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getImageString()
    {
        return imageString;
    }

    public void setImageString(String imageString)
    {
        this.imageString = imageString;
    }

    public VisitorsItemObject copy()
	{
		VisitorsItemObject copy = new VisitorsItemObject();

		copy.date = date;
		copy.type = type;
		copy.id = id;
		copy.url = url;
		copy.imageString = imageString;

		return copy;
	}

	@Override
	public int compareTo(VisitorsItemObject another)
	{
		if (another == null) return 1;

        return another.date.compareTo(date);
    }
}