package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;

/**
 * Committees object holder.
 * 
 * Contains the committees (Ausschusse) data.
 */
public class CommitteesObject implements Comparable<CommitteesObject>
{
    private String id;
    
    private String name;
    
    private String course;
    
    private String teaser;
    
    private Date lastChanged;
    
    private Date changedDateTime;

    private Date imageLastChanged;
    
    private Date imageChangedDateTime;

    private String detailsXML;

    private CommitteesDetailsObject committeeDetails;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCourse()
    {
        return course;
    }

    public void setCourse(String course)
    {
        this.course = course;
    }

    public String getTeaser()
    {
        return teaser;
    }

    public void setTeaser(String teaser)
    {
        this.teaser = teaser;
    }

    public String getLastChanged()
    {
        return DateHelper.getDateAsString(lastChanged);
    }

    public void setLastChanged(String lastChanged)
    {
        this.lastChanged = DateHelper.parseDate(lastChanged);
    }

    public String getChangedDateTime()
    {
        return DateHelper.getDateTimeAsString(changedDateTime);
    }

    public Date getChangedDateTimeDate()
    {
        return changedDateTime;
    }

    public void setChangedDateTime(String changedDateTime)
    {
        this.changedDateTime = DateHelper.parseDate(changedDateTime);
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

    public Date getImageChangedDateTimeDate()
    {
        return imageChangedDateTime;
    }

    public void setImageChangedDateTime(String imageChangedDateTime)
    {
        this.imageChangedDateTime = DateHelper.parseDate(imageChangedDateTime);
    }

    public String getDetailsXML()
    {
        return detailsXML;
    }

    public void setDetailsXML(String detailsXML)
    {
        this.detailsXML = detailsXML;
    }

	public CommitteesDetailsObject getCommitteeDetails()
    {
        return committeeDetails;
    }

    public void setCommitteeDetails(CommitteesDetailsObject committeeDetails)
    {
        this.committeeDetails = committeeDetails;
    }

    public CommitteesObject copy()
	{
		CommitteesObject copy = new CommitteesObject();

        copy.id = id;
        copy.name = name;
        copy.course = course;
        copy.teaser = teaser;
		copy.detailsXML = detailsXML;
        copy.lastChanged = lastChanged;
        copy.changedDateTime = changedDateTime;
        copy.imageLastChanged = imageLastChanged;
        copy.imageChangedDateTime = imageChangedDateTime;

		return copy;
	}

	@Override
	public int compareTo(CommitteesObject another)
	{
		if (another == null) return 1;

        return another.name.compareTo(name);
    }
}