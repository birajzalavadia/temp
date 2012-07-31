package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.helpers.TextHelper;

/**
 * Visitors article object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsArticleObject implements Comparable<VisitorsArticleObject>
{
    private Date date;

    private String type;

    private int specificType;

    private String id;

    private String title;

    private String teaser;

    private String text;

    private String imageId;

    private String imageString;

    private String imageCopyright;

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

    public int getSpecificType()
    {
        return specificType;
    }

    public void setSpecificType(int specificType)
    {
        this.specificType = specificType;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTeaser()
    {
        return teaser;
    }

    public void setTeaser(String teaser)
    {
        this.teaser = teaser;
    }

    public String getText()
    {
        return TextHelper.checkNull(text);
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getImageId()
    {
        return imageId;
    }

    public void setImageId(String imageId)
    {
        this.imageId = imageId;
    }

    public String getImageString()
    {
        return imageString;
    }

    public void setImageString(String imageString)
    {
        this.imageString = imageString;
    }

    public String getImageCopyright()
    {
        return imageCopyright;
    }

    public void setImageCopyright(String imageCopyright)
    {
        this.imageCopyright = imageCopyright;
    }

    public VisitorsArticleObject copy()
	{
		VisitorsArticleObject copy = new VisitorsArticleObject();

        copy.date = date;
        copy.type = type;
        copy.specificType = specificType;
        copy.id = id;
		copy.title = title;
		copy.teaser = teaser;
		copy.text = text;
        copy.imageId = imageId;
        copy.imageString = imageString;
        copy.imageCopyright = imageCopyright;

		return copy;
	}

	@Override
	public int compareTo(VisitorsArticleObject another)
	{
		if (another == null) return 1;

        return another.date.compareTo(date);
    }
}