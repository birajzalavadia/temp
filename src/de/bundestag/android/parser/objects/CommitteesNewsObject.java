package de.bundestag.android.parser.objects;

import java.util.Date;

/**
 * Committees object holder.
 * 
 * Contains the committees (Ausschusse) data.
 */
public class CommitteesNewsObject implements Comparable<CommitteesNewsObject>
{
    private String id;
    
    private String startTeaser;
    
    private String teaser;
    
    private Date lastChanged;

    private String titleTxt;
    
    private Date changedDate;

    private String imageUrl;
    
    private String detailsXML;
    
    private Date lasteChagned;
    
    

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTeaser() {
		return startTeaser;
	}

	public void setStartTeaser(String startTeaser) {
		this.startTeaser = startTeaser;
	}

	public String getTeaser() {
		return teaser;
	}

	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	public String getTitleTxt() {
		return titleTxt;
	}

	public void setTitleTxt(String titleTxt) {
		this.titleTxt = titleTxt;
	}

	public Date getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDetailsXML() {
		return detailsXML;
	}

	public void setDetailsXML(String detailsXML) {
		this.detailsXML = detailsXML;
	}

	public Date getLasteChagned() {
		return lasteChagned;
	}

	public void setLasteChagned(Date lasteChagned) {
		this.lasteChagned = lasteChagned;
	}

	public CommitteesNewsObject copy()
	{
		CommitteesNewsObject copy = new CommitteesNewsObject();

        copy.id = id;
        copy.imageUrl = imageUrl;
        copy.changedDate = changedDate;
        copy.detailsXML = detailsXML;
		copy.lastChanged = lastChanged;
        copy.startTeaser = startTeaser;
        copy.teaser  = teaser;
        copy.titleTxt = titleTxt;
		return copy;
	}

	@Override
	public int compareTo(CommitteesNewsObject another)
	{
		if (another == null) return 1;

        return another.titleTxt.compareTo(titleTxt);
    }
}