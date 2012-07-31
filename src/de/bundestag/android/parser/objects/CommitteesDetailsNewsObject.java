package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;

/**
 * Committee details news object holder.
 * 
 * Contains the committee news details (Ausschuse) data.
 */
public class CommitteesDetailsNewsObject implements Comparable<CommitteesDetailsNewsObject>
{
    private String committeeId;
    
    private Date date;

    private String title;
    
    private String teaser;
    
    private String imageURL;
    
    private String imageString;

    private String imageCopyright;
    
    private String detailsXML;
    
    private Date lastChanged;

    private CommitteesDetailsNewsDetailsObject newsDetails;

    public String getCommitteeId()
    {
        return committeeId;
    }

    public void setCommitteeId(String committeeId)
    {
        this.committeeId = committeeId;
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

    public String getTeaser()
    {
        return teaser;
    }

    public void setTeaser(String teaser)
    {
        this.teaser = teaser;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
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

    public String getDetailsXML()
    {
        return detailsXML;
    }

    public void setDetailsXML(String detailsXML)
    {
        this.detailsXML = detailsXML;
    }

    public String getLastChanged()
    {
        return DateHelper.getDateAsString(lastChanged);
    }

    public Date getLastChangedDate()
    {
        return lastChanged;
    }

    public void setLastChanged(String lastChanged)
    {
        this.lastChanged = DateHelper.parseDate(lastChanged);
    }

    public CommitteesDetailsNewsDetailsObject getNewsDetails()
    {
        return newsDetails;
    }

    public void setNewsDetails(CommitteesDetailsNewsDetailsObject newsDetails)
    {
        this.newsDetails = newsDetails;
    }

    public CommitteesDetailsNewsObject copy()
	{
		CommitteesDetailsNewsObject copy = new CommitteesDetailsNewsObject();

		copy.committeeId = committeeId;
		copy.date = date;
        copy.title = title;
        copy.teaser = teaser;
        copy.imageURL = imageURL;
        copy.imageString = imageString;
        copy.imageCopyright = imageCopyright;
        copy.detailsXML = detailsXML;
        copy.lastChanged = lastChanged;
        copy.newsDetails = newsDetails;

		return copy;
	}

	@Override
	public int compareTo(CommitteesDetailsNewsObject another)
	{
		if (another == null) return 1;

        return another.date.compareTo(date);
    }
}