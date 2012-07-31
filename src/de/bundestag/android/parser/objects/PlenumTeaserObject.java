package de.bundestag.android.parser.objects;

import de.bundestag.android.helpers.TextHelper;

/**
 * Plenum teaser object holder.
 * 
 * Contains the plenum teaser (Plenum) data.
 */
public class PlenumTeaserObject
{
    private String title;
    private String link;
    private String linkXML;
    private String teaser;
    private String image;
    private String text;

    public String getTitle()
    {
        return TextHelper.checkNull(title);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getLinkXML()
    {
        return linkXML;
    }

    public void setLinkXML(String linkXML)
    {
        this.linkXML = linkXML;
    }

    public String getTeaser()
    {
        return TextHelper.checkNull(teaser);
    }

    public void setTeaser(String teaser)
    {
        this.teaser = teaser;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public PlenumTeaserObject copy()
	{
        PlenumTeaserObject copy = new PlenumTeaserObject();

        copy.title = title;
        copy.link = link;
        copy.linkXML = linkXML;
        copy.teaser = teaser;
        copy.image = image;
        copy.text = text;

		return copy;
	}
}