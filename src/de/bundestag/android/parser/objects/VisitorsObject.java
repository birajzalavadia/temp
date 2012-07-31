package de.bundestag.android.parser.objects;

import java.util.List;

/**
 * Visitors object holder.
 * 
 * Contains the visitors (Besuche) data.
 */
public class VisitorsObject
{
    private List<VisitorsItemObject> items;

    private List<VisitorsArticleObject> newsArticles;   // Aktuell

    private VisitorsArticleListObject offers;           // Angebote

    private VisitorsArticleListObject locations;        // Orte

    private List<VisitorsArticleListObject> articleLists;   // article-lists

    private VisitorsArticleObject contact;              // Kontakt

    private List<VisitorsGalleryObject> galleries;

    private List<VisitorsItemObject> images;

    public List<VisitorsItemObject> getItems()
    {
        return items;
    }

    public void setItems(List<VisitorsItemObject> items)
    {
        this.items = items;
    }

    public List<VisitorsArticleObject> getNewsArticles()
    {
        return newsArticles;
    }

    public void setNewsArticles(List<VisitorsArticleObject> newsArticles)
    {
        this.newsArticles = newsArticles;
    }

    public VisitorsArticleListObject getOffers()
    {
        return offers;
    }

    public void setOffers(VisitorsArticleListObject offers)
    {
        this.offers = offers;
    }

    public VisitorsArticleListObject getLocations()
    {
        return locations;
    }

    public void setLocations(VisitorsArticleListObject locations)
    {
        this.locations = locations;
    }

    public List<VisitorsArticleListObject> getArticleLists()
    {
        return articleLists;
    }

    public void setArticleLists(List<VisitorsArticleListObject> articleLists)
    {
        this.articleLists = articleLists;
    }

    public VisitorsArticleObject getContact()
    {
        return contact;
    }

    public void setContact(VisitorsArticleObject contact)
    {
        this.contact = contact;
    }

    public List<VisitorsGalleryObject> getGalleries()
    {
        return galleries;
    }

    public void setGalleries(List<VisitorsGalleryObject> galleries)
    {
        this.galleries = galleries;
    }

    public List<VisitorsItemObject> getImages()
    {
        return images;
    }

    public void setImages(List<VisitorsItemObject> images)
    {
        this.images = images;
    }
}