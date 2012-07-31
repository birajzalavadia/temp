package de.bundestag.android.parser.objects;


/**
 * Members detail groups object holder.
 * 
 * Contains the members (MdB) group data.
 */
public class MembersDetailsGroupsObject implements Comparable<MembersDetailsGroupsObject>
{
    private String groupId;

    private String groupTitle;

    private String groupName;

    private String groupURL;

    private String groupRSS;

	public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public String getGroupTitle()
    {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle)
    {
        this.groupTitle = groupTitle;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupURL()
    {
        return groupURL;
    }

    public void setGroupURL(String groupURL)
    {
        this.groupURL = groupURL;
    }

    public String getGroupRSS()
    {
        return groupRSS;
    }

    public void setGroupRSS(String groupRSS)
    {
        this.groupRSS = groupRSS;
    }

    public MembersDetailsGroupsObject copy()
	{
		MembersDetailsGroupsObject copy = new MembersDetailsGroupsObject();

        copy.groupId = groupId;
        copy.groupTitle = groupTitle;
        copy.groupName = groupName;
        copy.groupURL = groupURL;
        copy.groupRSS = groupRSS;

		return copy;
	}

	@Override
	public int compareTo(MembersDetailsGroupsObject another)
	{
		if (another == null) return 1;

        return another.groupName.compareTo(groupName);
    }
}