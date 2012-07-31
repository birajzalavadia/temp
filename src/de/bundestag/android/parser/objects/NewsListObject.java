package de.bundestag.android.parser.objects;

import java.util.List;

/**
 * News list object holder.
 * 
 * Contains the news (Aktuell) data.
 */
public class NewsListObject implements Comparable<NewsListObject>
{
    private String title;

    private String parentNewsId;

    private List<NewsObject> items;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getParentNewsId()
    {
        return parentNewsId;
    }

    public void setParentNewsId(String parentNewsId)
    {
        this.parentNewsId = parentNewsId;
    }

    public List<NewsObject> getItems()
    {
        return items;
    }

    public void setItems(List<NewsObject> items)
    {
        this.items = items;
    }

    public NewsListObject copy()
	{
        NewsListObject copy = new NewsListObject();

		copy.title = title;
		copy.parentNewsId = parentNewsId;
		copy.items = items;

		return copy;
	}

	@Override
	public int compareTo(NewsListObject another)
	{
		if (another == null) return 1;

        return another.title.compareTo(title);
    }
}