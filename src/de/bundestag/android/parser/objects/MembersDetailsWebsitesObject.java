package de.bundestag.android.parser.objects;

/**
 * Members detail website object holder.
 * 
 * Contains the members (MdB) website data.
 */
public class MembersDetailsWebsitesObject implements Comparable<MembersDetailsWebsitesObject>
{
    private String websiteTitle;

    private String websiteURL;

    public String getWebsiteTitle()
    {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle)
    {
        this.websiteTitle = websiteTitle;
    }

    public String getWebsiteURL()
    {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL)
    {
        this.websiteURL = websiteURL;
    }

    public MembersDetailsWebsitesObject copy()
	{
		MembersDetailsWebsitesObject copy = new MembersDetailsWebsitesObject();

        copy.websiteTitle = websiteTitle;
        copy.websiteURL = websiteURL;

		return copy;
	}

	@Override
	public int compareTo(MembersDetailsWebsitesObject another)
	{
		if (another == null) return 1;

        return another.websiteTitle.compareTo(websiteTitle);
    }
}