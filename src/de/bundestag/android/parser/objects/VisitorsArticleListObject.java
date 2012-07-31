package de.bundestag.android.parser.objects;

import java.util.List;

/**
 * Visitors article list object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsArticleListObject implements Comparable<VisitorsArticleListObject>
{
    private String title;

    private String parentArticleId;

    private List<VisitorsArticleListItemObject> items;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getParentArticleId()
    {
        return parentArticleId;
    }

    public void setParentArticleId(String parentArticleId)
    {
        this.parentArticleId = parentArticleId;
    }

    public List<VisitorsArticleListItemObject> getItems()
    {
        return items;
    }

    public void setItems(List<VisitorsArticleListItemObject> items)
    {
        this.items = items;
    }

    public VisitorsArticleListObject copy()
	{
		VisitorsArticleListObject copy = new VisitorsArticleListObject();

		copy.title = title;
		copy.parentArticleId = parentArticleId;
		copy.items = items;

		return copy;
	}

	@Override
	public int compareTo(VisitorsArticleListObject another)
	{
		if (another == null) return 1;

        return another.title.compareTo(title);
    }
}