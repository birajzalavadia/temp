package de.bundestag.android.parser.objects;

import java.util.List;

import de.bundestag.android.helpers.TextHelper;

/**
 * Committee details object holder.
 * 
 * Contains the committee details (Ausschuse) data.
 */
public class CommitteesDetailsObject implements Comparable<CommitteesDetailsObject>
{
    private String name;
    
    private String photoURL;
    
    private String photoString;
    
    private String photoCopyright;
    
    private String description;

    private List<CommitteesDetailsNewsObject> detailNews;

	public String getName()
    {
        return TextHelper.checkNull(name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhotoURL()
    {
        return photoURL;
    }

    public void setPhotoURL(String photoURL)
    {
        this.photoURL = photoURL;
    }

    public String getPhotoString()
    {
        return photoString;
    }

    public void setPhotoString(String photoString)
    {
        this.photoString = photoString;
    }

    public String getPhotoCopyright()
    {
        return TextHelper.checkNull(photoCopyright);
    }

    public void setPhotoCopyright(String photoCopyright)
    {
        this.photoCopyright = photoCopyright;
    }

    public String getDescription()
    {
        return TextHelper.checkNull(description);
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<CommitteesDetailsNewsObject> getDetailNews()
    {
        return detailNews;
    }

    public void setDetailNews(List<CommitteesDetailsNewsObject> detailNews)
    {
        this.detailNews = detailNews;
    }

    public CommitteesDetailsObject copy()
	{
		CommitteesDetailsObject copy = new CommitteesDetailsObject();

		copy.name = name;
		copy.photoURL = photoURL;
		copy.photoString = photoString;
		copy.photoCopyright = photoCopyright;
		copy.description = description;
        copy.detailNews = detailNews;

		return copy;
	}

	@Override
	public int compareTo(CommitteesDetailsObject another)
	{
		if (another == null) return 1;

        return another.name.compareTo(name);
    }
}