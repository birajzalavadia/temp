package de.bundestag.android.synchronization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.parser.VisitorsXMLParser;
import de.bundestag.android.parser.objects.VisitorsArticleListItemGalleryObject;
import de.bundestag.android.parser.objects.VisitorsArticleListItemObject;
import de.bundestag.android.parser.objects.VisitorsArticleListObject;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.parser.objects.VisitorsGalleryImageObject;
import de.bundestag.android.parser.objects.VisitorsGalleryObject;
import de.bundestag.android.parser.objects.VisitorsItemObject;
import de.bundestag.android.parser.objects.VisitorsObject;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;

/**
 * Visitors synchronization class.
 * 
 * Knows how to synchronize the visitors.
 */
public class VisitorsSynchronization
{
    public static final int ARTICLE_SPECIFIC_TYPE_NORMAL = 1;

    public static final int ARTICLE_SPECIFIC_TYPE_CONTACT = 2;

    public static final int ARTICLE_SPECIFIC_TYPE_OFFERS = 3;

    public static final int ARTICLE_SPECIFIC_TYPE_LOCATIONS = 4;

    public static final int ARTICLE_SPECIFIC_TYPE_ARTICLELIST = 5;

    private Context context;

    private VisitorsDatabaseAdapter visitorsDatabaseAdapter;

    /**
     * Setup visitors.
     */
    public void setup(Context context)
    {
        this.context = context;
    }

    /**
     * Call the visitors parser and parse the latest visitors.
     */
    public VisitorsObject parseMainVisitors()
    {
        VisitorsXMLParser visitorsParser = new VisitorsXMLParser();
        VisitorsObject visitorsObject = visitorsParser.parse();
        
        return visitorsObject;
    }

    /**
     * Call the visitors parser and parse the latest visitors.
     */
    public VisitorsObject parseVisitorsNews()
    {
        VisitorsXMLParser visitorsParser = new VisitorsXMLParser();
        VisitorsObject visitorsObject = visitorsParser.parse();
        
        return visitorsObject;
    }

    /**
     * Call the visitors parser and parse an article list.
     */
    public VisitorsArticleListObject parseArticleList(VisitorsItemObject visitorArticleObject)
    {
        VisitorsXMLParser visitorsParser = new VisitorsXMLParser();
        VisitorsArticleListObject visitorsArticleListObject = visitorsParser.parseVisitorArticleList(visitorArticleObject);
        
        return visitorsArticleListObject;
    }

    /**
     * Call the visitors parser and parse an article.
     */
    public VisitorsArticleObject parseArticle(VisitorsItemObject visitorArticleObject)
    {
        VisitorsXMLParser visitorsParser = new VisitorsXMLParser();
        VisitorsArticleObject visitorsArticleListObject = visitorsParser.parseVisitorArticle(visitorArticleObject);
        
        return visitorsArticleListObject;
    }

    /**
     * Call the visitors parser and parse an gallery.
     */
    public VisitorsGalleryObject parseGallery(VisitorsItemObject visitorsItemObject)
    {
        VisitorsXMLParser visitorsParser = new VisitorsXMLParser();
        VisitorsGalleryObject visitorsGalleryObject = visitorsParser.parseVisitorGallery(visitorsItemObject);
        
        return visitorsGalleryObject;
    }
    
    /**
     * Open the database and remove all visitors.
     */
    public void deleteAllVisitors()
    {
        visitorsDatabaseAdapter.deleteAllVisitors();
    }

    /**
     * Get all visitors.
     */
    public Cursor getAllVisitors()
    {
        return visitorsDatabaseAdapter.fetchAllVisitors();
    }

    /**
     * Insert a visitor object in the database.
     */
    public void insertAVisitor(VisitorsItemObject visitorsItemObject)
    {
        visitorsDatabaseAdapter.createVisitor(visitorsItemObject);
    }

    /**
     * Open the database.
     */
    public void openDatabase()
    {
        if (visitorsDatabaseAdapter == null)
        {
            visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(context);
        }

        visitorsDatabaseAdapter.open();
    }

    /**
     * Load and insert the pictures.
     * 
     * DEPRECATED
     */
    public void insertPictures(VisitorsObject visitorsObject)
    {
        Map<String, String> imageMap = new HashMap<String, String>();
        List<VisitorsItemObject> images = visitorsObject.getImages();
        for (int i = 0; i < images.size(); i++)
        {
            VisitorsItemObject visitorsItemObject = (VisitorsItemObject) images.get(i);
            Bitmap bitmap = ImageHelper.loadBitmapFromUrl(visitorsItemObject.getUrl());
            if (bitmap != null)
            {
                String imageString = ImageHelper.convertBitmapToString(bitmap);
                visitorsItemObject.setImageString(imageString);
                
                imageMap.put(visitorsItemObject.getId(), imageString);
            }
        }
        
        if ((imageMap != null) && (imageMap.size() > 0))
        {
            String imageString;
            
            // galleries
            List<VisitorsGalleryObject> galleries = visitorsObject.getGalleries();
            if (galleries != null)
            {
                for (int i = 0; i < galleries.size(); i++)
                {
                    VisitorsGalleryObject gallery = galleries.get(i);
                    
                    if (gallery != null)
                    {
                        List<VisitorsGalleryImageObject> galleriesImages = gallery.getImages();
                        
                        if (galleriesImages != null)
                        {
                            for (int j = 0; j < galleriesImages.size(); j++)
                            {
                                VisitorsGalleryImageObject galImage = galleriesImages.get(j);

                                if (galImage != null)
                                {
                                    imageString = imageMap.get(galImage.getImageId());
                                    galImage.setImageString(imageString);
                                }
                            }
                        }
                    }
                }
            }

            // sub lists
            List<VisitorsArticleListObject> articleLists = visitorsObject.getArticleLists();
            if (articleLists != null)
            {
                for (int k = 0; k < articleLists.size(); k++)
                {
                    VisitorsArticleListObject visitorsArticleList = articleLists.get(k);

                    if (visitorsArticleList != null)
                    {
                        List<VisitorsArticleListItemObject> items = visitorsArticleList.getItems();
                        if (items != null)
                        {
                            for (int i = 0; i < items.size(); i++)
                            {
                                VisitorsArticleListItemObject article = items.get(i);
                                
                                if (article.getImageId() != null)
                                {
                                    imageString = imageMap.get(article.getImageId());
                                    article.setImageString(imageString);
                                }
                                
                                List<VisitorsArticleListItemGalleryObject> galleryImages = article.getGalleryImages();
                                if (galleryImages != null)
                                {
                                    for (int j = 0; j < galleryImages.size(); j++)
                                    {
                                        VisitorsArticleListItemGalleryObject galleryImage = galleryImages.get(j);

                                        if (galleryImage.getImageId() != null)
                                        {
                                            imageString = imageMap.get(galleryImage.getImageId());
                                            galleryImage.setImageString(imageString);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // article details
            List<VisitorsArticleObject> articles = visitorsObject.getNewsArticles();
            if (articles != null)
            {
                for (int i = 0; i < articles.size(); i++)
                {
                    VisitorsArticleObject article = articles.get(i);
                    
                    if (article.getImageId() != null)
                    {
                        imageString = imageMap.get(article.getImageId());
                        article.setImageString(imageString);
                    }
                }
            }        

            // offers
            VisitorsArticleListObject offers = visitorsObject.getOffers();
            if (offers != null)
            {
                List<VisitorsArticleListItemObject> items = offers.getItems();
                if (items != null)
                {
                    for (int i = 0; i < items.size(); i++)
                    {
                        VisitorsArticleListItemObject article = items.get(i);
                        
                        if (article.getImageId() != null)
                        {
                            imageString = imageMap.get(article.getImageId());
                            article.setImageString(imageString);
                        }
                        
                        List<VisitorsArticleListItemGalleryObject> galleryImages = article.getGalleryImages();
                        if (galleryImages != null)
                        {
                            for (int j = 0; j < galleryImages.size(); j++)
                            {
                                VisitorsArticleListItemGalleryObject galleryImage = galleryImages.get(j);

                                if (galleryImage.getImageId() != null)
                                {
                                    imageString = imageMap.get(galleryImage.getImageId());
                                    galleryImage.setImageString(imageString);
                                }
                            }
                        }
                    }
                }
            }

            // locations
            VisitorsArticleListObject locations = visitorsObject.getLocations();
            if (locations != null)
            {
                List<VisitorsArticleListItemObject> items = locations.getItems();
                if (items != null)
                {
                    for (int i = 0; i < items.size(); i++)
                    {
                        VisitorsArticleListItemObject article = items.get(i);
                        
                        if (article.getImageId() != null)
                        {
                            imageString = imageMap.get(article.getImageId());
                            article.setImageString(imageString);
                        }
                        
                        List<VisitorsArticleListItemGalleryObject> galleryImages = article.getGalleryImages();
                        if (galleryImages != null)
                        {
                            for (int j = 0; j < galleryImages.size(); j++)
                            {
                                VisitorsArticleListItemGalleryObject galleryImage = galleryImages.get(j);

                                if (galleryImage.getImageId() != null)
                                {
                                    imageString = imageMap.get(galleryImage.getImageId());
                                    galleryImage.setImageString(imageString);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Load and insert the visitor news pictures.
     */
    public void insertNewsPictures(Map<String, VisitorsItemObject> allPicturesMap, List<VisitorsItemObject> images, VisitorsArticleObject contact, List<VisitorsArticleObject> news)
    {
        Map<String, String> imageMap = new HashMap<String, String>();
        for (int i = 0; i < images.size(); i++)
        {
            VisitorsItemObject visitorsItemObject = (VisitorsItemObject) images.get(i);
            Bitmap bitmap = ImageHelper.loadBitmapFromUrl(visitorsItemObject.getUrl());
            if (bitmap != null)
            {
                String imageString = ImageHelper.convertBitmapToString(bitmap);
                visitorsItemObject.setImageString(imageString);

                imageMap.put(visitorsItemObject.getId(), imageString);
            }
        }
        
        String imageString;

        // contact
        if ((contact != null) && (contact.getImageId() != null))
        {
            imageString = imageMap.get(contact.getImageId());
            contact.setImageString(imageString);
        }

        // news articles
        if (news != null)
        {
            for (int i = 0; i < news.size(); i++)
            {
                VisitorsArticleObject newsArticle = news.get(i);
                
                if (newsArticle.getImageId() != null)
                {
                    imageString = imageMap.get(newsArticle.getImageId());
                    newsArticle.setImageString(imageString);
                }
            }
        }
    }

    /**
     * Close the database.
     */
    public void closeDatabase()
    {
        visitorsDatabaseAdapter.close();
    }

    /**
     * Insert the visitor contact article.
     */
    public void insertVisitorArticle(VisitorsArticleObject contact, int type)
    {
        visitorsDatabaseAdapter.createVisitorArticle(contact, type, null);
    }

    /**
     * Insert a visitor gallery.
     */
    public long insertGallery(VisitorsGalleryObject gallery)
    {
        return visitorsDatabaseAdapter.createVisitorGallery(gallery);
    }

    /**
     * Insert a visitor gallery image.
     */
    public void insertVisitorGalleryImage(VisitorsGalleryImageObject galleryImage, long galleryId)
    {
       visitorsDatabaseAdapter.createVisitorGalleryImage(galleryImage, galleryId);
    }

    /**
     * Insert a visitor article list.
     */
    public long insertVisitorArticleList(VisitorsArticleListObject articleListObject, int type)
    {
        return visitorsDatabaseAdapter.createVisitorArticleList(articleListObject, type);
    }

    /**
     * Insert a visitor article list article.
     */
    public long insertVisitorArticleListArticle(VisitorsArticleListItemObject locationArticle, int type, Long articleListId)
    {
        return visitorsDatabaseAdapter.createVisitorArticleListArticle(locationArticle, type, articleListId);
    }

    /**
     * Insert a visitor article gallery image.
     */
    public void insertVisitorArticleGalleryImage(VisitorsArticleListItemGalleryObject articleGalleryImage, long articleListId)
    {
        visitorsDatabaseAdapter.createVisitorArticleGalleryImage(articleGalleryImage, articleListId);
    }

    public void deleteAllVisitorNews()
    {
        visitorsDatabaseAdapter.deleteAllVisitorsNews();
    }

    public Cursor getVisitorArticleList(int articleSpecificTypeOffers)
    {
        return visitorsDatabaseAdapter.getVisitorArticleList(articleSpecificTypeOffers);
    }

    public Cursor getVisitor(String visitorId)
    {
        return visitorsDatabaseAdapter.getVisitor(visitorId);
    }

    public Cursor getVisitorArticle(String articleId)
    {
        return visitorsDatabaseAdapter.getVisitorArticle(articleId);
    }
}
