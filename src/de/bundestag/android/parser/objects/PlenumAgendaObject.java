package de.bundestag.android.parser.objects;

/**
 * Plenum agenda object holder.
 * 
 * Contains the plenum agenda (Plenum) data.
 */
public class PlenumAgendaObject
{
    private String startTime;
    private String endTime;
    private String status;
    private String title;
    private String top;
    private String protocol;
    private String link;
    private String linkXml;

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTop()
    {
        return top;
    }

    public void setTop(String top)
    {
        this.top = top;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getLinkXml()
    {
        return linkXml;
    }

    public void setLinkXml(String linkXml)
    {
        this.linkXml = linkXml;
    }

    public PlenumAgendaObject copy()
	{
		PlenumAgendaObject copy = new PlenumAgendaObject();

        copy.startTime = startTime;
        copy.endTime = endTime;
        copy.status = status;
        copy.title = title;
        copy.top = top;
        copy.protocol = protocol;
        copy.link = link;
        copy.linkXml = linkXml;

		return copy;
	}
}