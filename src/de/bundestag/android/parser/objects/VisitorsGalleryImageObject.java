package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;

/**
 * Visitors article object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsGalleryImageObject implements Comparable<VisitorsGalleryImageObject>
{
    private String imageId;

    private String imageString;

    private Date imageDate;

    private String imageText;

    private String imageCopyright;

    public String getImageId()
    {
        return imageId;
    }

    public void setImageId(String imageId)
    {
        this.imageId = imageId;
    }

    public String getImageString()
    {
        return imageString;
    }

    public void setImageString(String imageString)
    {
        this.imageString = imageString;
    }

    public String getImageDate()
    {
        return DateHelper.getArticleDateAsString(imageDate);
    }

    public void setImageDate(String imageDate)
    {
        this.imageDate = DateHelper.parseArticleDate(imageDate);
    }

    public String getImageText()
    {
        return imageText;
    }

    public void setImageText(String imageText)
    {
        this.imageText = imageText;
    }

    public String getImageCopyright()
    {
        return imageCopyright;
    }

    public void setImageCopyright(String imageCopyright)
    {
        this.imageCopyright = imageCopyright;
    }

    public VisitorsGalleryImageObject copy()
	{
		VisitorsGalleryImageObject copy = new VisitorsGalleryImageObject();

        copy.imageId = imageId;
        copy.imageString = imageString;
        copy.imageDate = imageDate;
        copy.imageText = imageText;
		copy.imageCopyright = imageCopyright;

		return copy;
	}

	@Override
	public int compareTo(VisitorsGalleryImageObject another)
	{
		if (another == null) return 1;

        return another.imageDate.compareTo(imageDate);
    }
}