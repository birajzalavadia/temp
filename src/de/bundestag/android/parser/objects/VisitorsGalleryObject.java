package de.bundestag.android.parser.objects;

import java.util.List;

/**
 * Visitors gallery object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsGalleryObject implements Comparable<VisitorsGalleryObject>
{
    private String title;

    private List<VisitorsGalleryImageObject> images;
    
    private String galleryId;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public List<VisitorsGalleryImageObject> getImages()
    {
        return images;
    }

    public void setImages(List<VisitorsGalleryImageObject> images)
    {
        this.images = images;
    }

    public VisitorsGalleryObject copy()
	{
		VisitorsGalleryObject copy = new VisitorsGalleryObject();

		copy.title = title;
		copy.images = images;

		return copy;
	}

	@Override
	public int compareTo(VisitorsGalleryObject another)
	{
		if (another == null) return 1;

        return another.title.compareTo(title);
    }

	public String getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(String galleryId) {
		this.galleryId = galleryId;
	}
}