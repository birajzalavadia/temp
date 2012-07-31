package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;

/**
 * Members object holder.
 * 
 * Contains the members (MdB) data.
 */
public class MembersObject implements Comparable<MembersObject>
{
    private String id;
    private String name;
    private String bioURL;
    private String infoURL;
    private String infoURLMore;
    private String land;
    private String photo;
    private String photoString;
    private Date photoLastChanged;
    private Date photoChangedDateTime;
    private Date lastChanged;
    private Date changedDateTime;

    private MembersDetailsObject membersDetails;

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

    public String getBioURL()
    {
        return bioURL;
    }

    public void setBioURL(String bioURL)
    {
        this.bioURL = bioURL;
    }

    public String getInfoURL()
    {
        return infoURL;
    }

    public void setInfoURL(String infoURL)
    {
        this.infoURL = infoURL;
    }

    public String getInfoURLMore()
    {
        return infoURLMore;
    }

    public void setInfoURLMore(String infoURLMore)
    {
        this.infoURLMore = infoURLMore;
    }

    public String getLand()
    {
        return land;
    }

    public void setLand(String land)
    {
        this.land = land;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public String getPhotoString()
    {
        return photoString;
    }

    public void setPhotoString(String photoString)
    {
        this.photoString = photoString;
    }

    public String getPhotoLastChanged()
    {
        return DateHelper.getDateAsString(photoLastChanged);
    }

    public void setPhotoLastChanged(String photoLastChanged)
    {
        this.photoLastChanged = DateHelper.parseDate(photoLastChanged);
    }

    public String getPhotoChangedDateTime()
    {
        return DateHelper.getDateTimeAsString(photoChangedDateTime);
    }

    public Date getPhotoChangedDateTimeDate()
    {
        return photoChangedDateTime;
    }

    public void setPhotoChangedDateTime(String photoChangedDateTime)
    {
        this.photoChangedDateTime = DateHelper.parseDate(photoChangedDateTime);
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

	public MembersDetailsObject getMembersDetails()
    {
        return membersDetails;
    }

    public void setMembersDetails(MembersDetailsObject membersDetails)
    {
        this.membersDetails = membersDetails;
    }

    public MembersObject copy()
	{
		MembersObject copy = new MembersObject();

        copy.id = id;
        copy.name = name;
        copy.bioURL = bioURL;
        copy.infoURL = infoURL;
        copy.infoURLMore = infoURLMore;
        copy.land = land;
        copy.photo = photo;
        copy.photoString = photoString;
        copy.photoLastChanged = photoLastChanged;
        copy.photoChangedDateTime = photoChangedDateTime;
		copy.lastChanged = lastChanged;
		copy.changedDateTime = changedDateTime;
		copy.membersDetails = membersDetails;

		return copy;
	}

	@Override
	public int compareTo(MembersObject another)
	{
		if (another == null) return 1;

        return another.name.compareTo(name);
    }
}