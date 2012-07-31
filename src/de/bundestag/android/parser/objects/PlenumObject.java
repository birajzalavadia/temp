package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;

/**
 * Plenum object holder.
 * 
 * Contains the plenum (Plenum) data.
 */
public class PlenumObject
{    
    private Integer type;
    private Integer status;
    private Date date;
    private String title;
    private String imageURL;
    private String imageCopyright;
    private String imageString;
    private String teaser;
    private String text;
    private String detailsXML;

	public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = Integer.parseInt(status);
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

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getImageCopyright()
    {
        return imageCopyright;
    }

    public void setImageCopyright(String imageCopyright)
    {
        this.imageCopyright = imageCopyright;
    }

    public String getImageString()
    {
        return imageString;
    }

    public void setImageString(String imageString)
    {
        this.imageString = imageString;
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
        return text;
    }

    public String getDetailsXML() {
		return detailsXML;
	}

	public void setDetailsXML(String detailsXML) {
		this.detailsXML = detailsXML;
	}

	public void setText(String text)
    {
        this.text = text;
    }

    public PlenumObject copy()
	{
		PlenumObject copy = new PlenumObject();

		copy.type = type;
        copy.status = status;
        copy.date = date;
		copy.title = title;
        copy.imageURL = imageURL;
        copy.imageCopyright = imageCopyright;
        copy.imageString = imageString;
		copy.teaser = teaser;
		copy.text = text;

		return copy;
	}
}