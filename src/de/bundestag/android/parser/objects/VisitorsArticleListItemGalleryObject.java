package de.bundestag.android.parser.objects;

/**
 * Visitors article list item gallery object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsArticleListItemGalleryObject implements Comparable<VisitorsArticleListItemGalleryObject>
{
    private String galleryId;

    private String teaser;

    private String imageId;

    private String imageString;

    private String imageCopyright;
    
    private String listId;

    public String getGalleryId()
    {
        return galleryId;
    }

    public void setGalleryId(String galleryId)
    {
        this.galleryId = galleryId;
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

    public VisitorsArticleListItemGalleryObject copy()
	{
		VisitorsArticleListItemGalleryObject copy = new VisitorsArticleListItemGalleryObject();

        copy.galleryId = galleryId;
        copy.teaser = teaser;
        copy.imageId = imageId;
        copy.imageString = imageString;
		copy.imageCopyright = imageCopyright;

		return copy;
	}

	@Override
	public int compareTo(VisitorsArticleListItemGalleryObject another)
	{
		if (another == null) return 1;

        return another.galleryId.compareTo(galleryId);
    }

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}
}