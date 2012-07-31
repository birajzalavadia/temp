package de.bundestag.android.parser.objects;

import java.util.List;

/**
 * Visitors article list item object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsArticleListItemObject implements Comparable<VisitorsArticleListItemObject>
{
    private String type;
    
    private String id;
    
    private String title;

    private String teaser;

    private String imageId;

    private String imageString;

    private String imageCopyright;
    
    

    private List<VisitorsArticleListItemGalleryObject> galleryImages;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTeaser()
    {
        return teaser;
    }

    public void setTeaser(String teaser)
    {
        this.teaser = teaser;
    }

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

    public String getImageCopyright()
    {
        return imageCopyright;
    }

    public void setImageCopyright(String imageCopyright)
    {
        this.imageCopyright = imageCopyright;
    }

    public List<VisitorsArticleListItemGalleryObject> getGalleryImages()
    {
        return galleryImages;
    }

    public void setGalleryImages(List<VisitorsArticleListItemGalleryObject> galleryImages)
    {
        this.galleryImages = galleryImages;
    }

    public VisitorsArticleListItemObject copy()
	{
		VisitorsArticleListItemObject copy = new VisitorsArticleListItemObject();

        copy.type = type;
        copy.id = id;
        copy.title = title;
		copy.teaser = teaser;
        copy.imageId = imageId;
        copy.imageString = imageString;
        copy.imageCopyright = imageCopyright;
        copy.galleryImages = galleryImages;

		return copy;
	}

	@Override
	public int compareTo(VisitorsArticleListItemObject another)
	{
		if (another == null) return 1;

        return another.title.compareTo(title);
    }
}