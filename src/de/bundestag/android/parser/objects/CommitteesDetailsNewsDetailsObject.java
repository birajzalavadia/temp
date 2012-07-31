package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.helpers.TextHelper;

/**
 * Committee details news object holder.
 * 
 * Contains the committee news details (Ausschuse) data.
 */
public class CommitteesDetailsNewsDetailsObject implements Comparable<CommitteesDetailsNewsDetailsObject>
{
    private Date date;
    
    private String title;
    
    private String text;

    private String imageURL;
    
    private String imageString;
    
    private String imageCopyright;
    
    private Date imageLastChanged;

    private Date imageChangedDateTime;

    public String getImageCopyright()
    {
        return imageCopyright;
    }

    public void setImageCopyright(String imageCopyright)
    {
        this.imageCopyright = imageCopyright;
    }

    public String getDate()
    {
        return DateHelper.getDateAsString(date);
    }

    public void setDate(String date)
    {
        this.date = DateHelper.parseDate(date);
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getImageLastChanged()
    {
        return DateHelper.getDateAsString(imageLastChanged);
    }

    public void setImageLastChanged(String imageLastChanged)
    {
        this.imageLastChanged = DateHelper.parseDate(imageLastChanged);
    }

    public String getImageChangedDateTime()
    {
        return DateHelper.getDateTimeAsString(imageChangedDateTime);
    }

    public void setImageChangedDateTime(String imageChangedDateTime)
    {
        this.imageChangedDateTime = DateHelper.parseDate(imageChangedDateTime);
    }

    public String getTitle()
    {
        return TextHelper.checkNull(title);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImageString()
    {
        return imageString;
    }

    public void setImageString(String imageString)
    {
        this.imageString = imageString;
    }

    public String getText()
    {
        return TextHelper.checkNull(text);
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public CommitteesDetailsNewsDetailsObject copy()
	{
		CommitteesDetailsNewsDetailsObject copy = new CommitteesDetailsNewsDetailsObject();

		copy.date = date;
        copy.title = title;
        copy.text = text;
        copy.imageURL = imageURL;
        copy.imageString = imageString;
        copy.imageCopyright = imageCopyright;
        copy.imageLastChanged = imageLastChanged;
        copy.imageChangedDateTime = imageChangedDateTime;
        copy.imageString = imageString;

		return copy;
	}

	@Override
	public int compareTo(CommitteesDetailsNewsDetailsObject another)
	{
		if (another == null) return 1;

        return another.date.compareTo(date);
    }
}