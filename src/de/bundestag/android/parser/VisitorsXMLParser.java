package de.bundestag.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.helpers.ParseEncodingDetector;
import de.bundestag.android.parser.objects.VisitorsArticleListItemGalleryObject;
import de.bundestag.android.parser.objects.VisitorsArticleListItemObject;
import de.bundestag.android.parser.objects.VisitorsArticleListObject;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.parser.objects.VisitorsGalleryImageObject;
import de.bundestag.android.parser.objects.VisitorsGalleryObject;
import de.bundestag.android.parser.objects.VisitorsItemObject;
import de.bundestag.android.parser.objects.VisitorsObject;

/**
 * Visitors XML parser.
 * 
 * This class knows how to parse visitors (Besuche) from Bundestag.
 */
public class VisitorsXMLParser extends BaseXMLParser {
	private static final String VISITORS_URL = "xml/besuche/index.xml";

	public static final String KONTAKT_URL = "http://www.bundestag.de/xml/besuche/kontakt/index.xml";

	public static final String OFFERS_URL = "http://www.bundestag.de/xml/besuche/angebote/index.xml";

	public static final String LOCATIONS_URL = "http://www.bundestag.de/xml/besuche/orte/index.xml";

	// Visitors article types
	public static final String VISITOR_TYPE_ARTICLE = "article";
	public static final String VISITOR_TYPE_ARTICLE_LIST = "article-list";
	public static final String VISITOR_TYPE_IMAGE = "image";
	public static final String VISITOR_TYPE_GALLERY = "gallery";

	public static final String KEY_NEWS_LIST_TITLE = "newslistTitle";
	// Visitors xml tags
	private static final String TAG_VISITORS_ROOT_ELEMENT = "content-list";
	private static final String TAG_VISITORS_ITEM = "content-item";
	private static final String ATTRIBUTE_VISITORS_DATE = "date";
	private static final String ATTRIBUTE_VISITORS_TYPE = "type";
	private static final String ATTRIBUTE_VISITORS_ID = "id";
	private static final String ATTRIBUTE_VISITORS_URL = "url";

	// Visitors article xml tags
	private static final String TAG_VISITORS_ARTICLE_ROOT_ELEMENT = "content-item";
	private static final String TAG_VISITORS_ARTICLE_TITLE = "title";
	private static final String TAG_VISITORS_ARTICLE_TEASER = "teaser";
	private static final String TAG_VISITORS_ARTICLE_TEXT = "text";
	private static final String TAG_VISITORS_ARTICLE_IMAGE = "image";
	private static final String ATTRIBUTE_VISITORS_ARTICLE_IMAGEID = "id";
	private static final String TAG_VISITORS_ARTICLE_IMAGECOPYRIGHT = "copyright";

	// Visitors article list xml tags
	private static final String TAG_VISITORS_ARTICLE_LIST_ROOT_ELEMENT = "content-item";
	private static final String TAG_VISITORS_ARTICLE_LIST_TITLE = "title";
	private static final String TAG_VISITORS_ARTICLE_LIST_ITEM = "list-item";
	private static final String ATTRIBUTE_VISITORS_ARTICLE_LIST_TYPE = "type";
	private static final String ATTRIBUTE_VISITORS_ARTICLE_LIST_ID = "id";
	private static final String TAG_VISITORS_ARTICLE_LIST_ITEM_TITLE = "title";
	private static final String TAG_VISITORS_ARTICLE_LIST_ITEM_TEASER = "teaser";
	private static final String TAG_VISITORS_ARTICLE_LIST_ITEM_IMAGE = "image";
	private static final String TAG_VISITORS_ARTICLE_LIST_ITEM_GALLERY_REF = "gallery-ref";
	private static final String ATTRIBUTE_VISITORS_ARTICLE_LIST_ITEM_IMAGEID = "id";
	private static final String TAG_VISITORS_ARTICLE_LIST_ITEM_COPYRIGHT = "copyright";
	private static final String ATTRIBUTE_VISITORS_ARTICLE_LIST_GALLERY_REF_ID = "id";

	// Visitors gallery xml tags
	private static final String TAG_VISITORS_GALLERY_ROOT_ELEMENT = "content-item";
	private static final String TAG_VISITORS_GALLERY_TITLE = "title";
	private static final String TAG_VISITORS_GALLERY_IMAGE_ITEM = "image";
	private static final String ATTRIBUTE_VISITORS_GALLERY_ID = "id";
	private static final String ATTRIBUTE_VISITORS_GALLERY_IMAGE_ITEM_ID = "id";
	private static final String ATTRIBUTE_VISITORS_GALLERY_IMAGE_ITEM_DATE = "date";
	private static final String TAG_VISITORS_GALLERY_IMAGE_ITEM_TEXT = "text";
	private static final String TAG_VISITORS_GALLERY_IMAGE_ITEM_COPYRIGHT = "copyright";

	public VisitorsXMLParser() {
		super(VISITORS_URL);
	}

	/**
	 * Parse all visitors (Besuche) objects.
	 */
	public VisitorsObject parse() {
		VisitorsObject visitorsObject = new VisitorsObject();

		final List<VisitorsItemObject> visitors = new ArrayList<VisitorsItemObject>();

		VisitorsArticleListObject offers = new VisitorsArticleListObject();

		VisitorsArticleListObject locations = new VisitorsArticleListObject();

		List<VisitorsArticleListObject> articleLists = new ArrayList<VisitorsArticleListObject>();

		VisitorsArticleObject contact = new VisitorsArticleObject();

		List<VisitorsArticleObject> news = new ArrayList<VisitorsArticleObject>();

		List<VisitorsGalleryObject> galleries = new ArrayList<VisitorsGalleryObject>();

		List<VisitorsItemObject> images = new ArrayList<VisitorsItemObject>();

		final VisitorsItemObject currentVisitorsObject = new VisitorsItemObject();
		RootElement root = new RootElement(TAG_VISITORS_ROOT_ELEMENT);
		Element item = root.getChild(TAG_VISITORS_ITEM);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				visitors.add(currentVisitorsObject.copy());
			}
		});

		item.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				currentVisitorsObject.setDate(attributes.getValue(ATTRIBUTE_VISITORS_DATE));
				currentVisitorsObject.setType(attributes.getValue(ATTRIBUTE_VISITORS_TYPE));
				currentVisitorsObject.setId(attributes.getValue(ATTRIBUTE_VISITORS_ID));
				currentVisitorsObject.setUrl(attributes.getValue(ATTRIBUTE_VISITORS_URL));
			}
		});

		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getXMLURL());
			Xml.parse(this.getInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > visitors.size()) ? visitors.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER)
				: visitors.size();
		for (int i = 0; i < insertsSize; i++) {
			VisitorsItemObject visitorsItemObject = (VisitorsItemObject) visitors.get(i);

			if (visitorsItemObject.getType().equals(VISITOR_TYPE_ARTICLE)) {
				if (visitorsItemObject.getUrl().equals(KONTAKT_URL)) {
					contact = parseVisitorArticle(visitorsItemObject);
				} else {
					news.add(parseVisitorArticle(visitorsItemObject));
				}
			} else if (visitorsItemObject.getType().equals(VISITOR_TYPE_IMAGE)) {
				images.add(visitorsItemObject);
			}

			else if (visitorsItemObject.getType().equals(VISITOR_TYPE_GALLERY)) {
				galleries.add(parseVisitorGallery(visitorsItemObject));
			} else if (visitorsItemObject.getType().equals(VISITOR_TYPE_ARTICLE_LIST)) {
				if (visitorsItemObject.getUrl().equals(OFFERS_URL)) {
					offers = parseVisitorArticleList(visitorsItemObject);
				} else if (visitorsItemObject.getUrl().equals(LOCATIONS_URL)) {
					locations = parseVisitorArticleList(visitorsItemObject);
				} else {
					VisitorsArticleListObject articleList = parseVisitorArticleList(visitorsItemObject);
					articleList.setParentArticleId(visitorsItemObject.getId());
					articleLists.add(articleList);
				}
			}
		}

		visitorsObject.setItems(visitors);
		visitorsObject.setNewsArticles(news);
		visitorsObject.setImages(images);
		visitorsObject.setGalleries(galleries);
		visitorsObject.setOffers(offers);
		visitorsObject.setLocations(locations);
		visitorsObject.setArticleLists(articleLists);
		visitorsObject.setContact(contact);

		return visitorsObject;
	}

	public VisitorsArticleObject parseVisitorArticle(VisitorsItemObject visitorsItemObject) {
		final VisitorsArticleObject visitorsArticleObject = new VisitorsArticleObject();

		visitorsArticleObject.setType(visitorsItemObject.getType());
		visitorsArticleObject.setDate(visitorsItemObject.getDate());
		visitorsArticleObject.setId(visitorsItemObject.getId());

		RootElement root = new RootElement(TAG_VISITORS_ARTICLE_ROOT_ELEMENT);

		root.getChild(TAG_VISITORS_ARTICLE_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				visitorsArticleObject.setTitle(body);
			}
		});
		root.getChild(TAG_VISITORS_ARTICLE_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				visitorsArticleObject.setTeaser(body);
			}
		});
		root.getChild(TAG_VISITORS_ARTICLE_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				visitorsArticleObject.setText(body);
			}
		});

		Element imageElement = root.getChild(TAG_VISITORS_ARTICLE_IMAGE);

		imageElement.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				visitorsArticleObject.setImageId(attributes.getValue(ATTRIBUTE_VISITORS_ARTICLE_IMAGEID));
			}
		});

		imageElement.getChild(TAG_VISITORS_ARTICLE_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				visitorsArticleObject.setImageCopyright(body);
			}
		});

		this.setSpecificParserURL(visitorsItemObject.getUrl());
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return visitorsArticleObject;
	}

	public VisitorsArticleListObject parseVisitorArticleList(VisitorsItemObject visitorsItemObject) {
		final VisitorsArticleListObject visitorsArticleListObject = new VisitorsArticleListObject();

		final List<VisitorsArticleListItemObject> items = new ArrayList<VisitorsArticleListItemObject>();

		final VisitorsArticleListItemObject currentVisitorsArticleListItemObject = new VisitorsArticleListItemObject();

		final List<VisitorsArticleListItemGalleryObject> galleryItems = new ArrayList<VisitorsArticleListItemGalleryObject>();

		final VisitorsArticleListItemGalleryObject currentArticleListArticleGallery = new VisitorsArticleListItemGalleryObject();

		final String listId = visitorsItemObject.getId();

		RootElement root = new RootElement(TAG_VISITORS_ARTICLE_LIST_ROOT_ELEMENT);
		root.getChild(TAG_VISITORS_ARTICLE_LIST_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				visitorsArticleListObject.setTitle(body);
			}
		});

		Element item = root.getChild(TAG_VISITORS_ARTICLE_LIST_ITEM);

		item.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				currentVisitorsArticleListItemObject.setType(attributes.getValue(ATTRIBUTE_VISITORS_ARTICLE_LIST_TYPE));
				currentVisitorsArticleListItemObject.setId(attributes.getValue(ATTRIBUTE_VISITORS_ARTICLE_LIST_ID));

			}
		});

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				VisitorsArticleListItemObject copyCurrent = currentVisitorsArticleListItemObject.copy();

				if (galleryItems.size() > 0) {
					ArrayList<VisitorsArticleListItemGalleryObject> articleGalleries = new ArrayList<VisitorsArticleListItemGalleryObject>();
					for (VisitorsArticleListItemGalleryObject galItem : galleryItems) {
						articleGalleries.add(galItem);
					}
					copyCurrent.setGalleryImages(articleGalleries);
					galleryItems.clear();
				}

				items.add(copyCurrent);

				currentVisitorsArticleListItemObject.setImageId(null);
			}
		});

		item.getChild(TAG_VISITORS_ARTICLE_LIST_ITEM_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentVisitorsArticleListItemObject.setTitle(body);
			}
		});
		item.getChild(TAG_VISITORS_ARTICLE_LIST_ITEM_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentVisitorsArticleListItemObject.setTeaser(body);
			}
		});

		Element imageElement = item.getChild(TAG_VISITORS_ARTICLE_LIST_ITEM_IMAGE);
		imageElement.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				currentVisitorsArticleListItemObject.setImageId(attributes.getValue(ATTRIBUTE_VISITORS_ARTICLE_LIST_ITEM_IMAGEID));
			}
		});
		imageElement.getChild(TAG_VISITORS_ARTICLE_LIST_ITEM_COPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentVisitorsArticleListItemObject.setImageCopyright(body);
			}
		});

		// TODO - add article-list item gallery parsing

		Element galleryItem = item.getChild(TAG_VISITORS_ARTICLE_LIST_ITEM_GALLERY_REF);
		galleryItem.setStartElementListener(new StartElementListener() {

			@Override
			public void start(Attributes attributes) {

				currentArticleListArticleGallery.setGalleryId(attributes.getValue(ATTRIBUTE_VISITORS_ARTICLE_LIST_GALLERY_REF_ID));

			}
		});
		galleryItem.setEndElementListener(new EndElementListener() {

			@Override
			public void end() {
				galleryItems.add(currentArticleListArticleGallery.copy());
			}
		});

		Element galleryItemImage = galleryItem.getChild(TAG_VISITORS_ARTICLE_LIST_ITEM_IMAGE);
		galleryItemImage.setStartElementListener(new StartElementListener() {

			@Override
			public void start(Attributes attributes) {
				currentArticleListArticleGallery.setImageId(attributes.getValue(ATTRIBUTE_VISITORS_ARTICLE_LIST_ITEM_IMAGEID));
				currentArticleListArticleGallery.setListId(listId);
			}
		});

		galleryItemImage.getChild(TAG_VISITORS_ARTICLE_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String body) {
				currentArticleListArticleGallery.setImageCopyright(body);
			}
		});

		this.setSpecificParserURL(visitorsItemObject.getUrl());
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		visitorsArticleListObject.setItems(items);

		return visitorsArticleListObject;
	}

	public VisitorsGalleryObject parseVisitorGallery(VisitorsItemObject visitorsItemObject) {
		final VisitorsGalleryObject visitorsGalleryObject = new VisitorsGalleryObject();

		final List<VisitorsGalleryImageObject> images = new ArrayList<VisitorsGalleryImageObject>();

		final VisitorsGalleryImageObject currentVisitorsGalleryImageObject = new VisitorsGalleryImageObject();

		RootElement root = new RootElement(TAG_VISITORS_GALLERY_ROOT_ELEMENT);

		root.setStartElementListener(new StartElementListener() {

			@Override
			public void start(Attributes attributes) {
				visitorsGalleryObject.setGalleryId(attributes.getValue(ATTRIBUTE_VISITORS_GALLERY_ID));
			}
		});

		root.getChild(TAG_VISITORS_GALLERY_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				visitorsGalleryObject.setTitle(body);
			}
		});

		Element imageItem = root.getChild(TAG_VISITORS_GALLERY_IMAGE_ITEM);
		imageItem.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				currentVisitorsGalleryImageObject.setImageId(attributes.getValue(ATTRIBUTE_VISITORS_GALLERY_IMAGE_ITEM_ID));
				currentVisitorsGalleryImageObject.setImageDate(attributes.getValue(ATTRIBUTE_VISITORS_GALLERY_IMAGE_ITEM_DATE));
			}
		});

		root.getChild(TAG_VISITORS_GALLERY_IMAGE_ITEM).setEndElementListener(new EndElementListener() {
			public void end() {
				images.add(currentVisitorsGalleryImageObject.copy());
			}
		});

		imageItem.getChild(TAG_VISITORS_GALLERY_IMAGE_ITEM_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentVisitorsGalleryImageObject.setImageText(body);
			}
		});

		imageItem.getChild(TAG_VISITORS_GALLERY_IMAGE_ITEM_COPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentVisitorsGalleryImageObject.setImageCopyright(body);
			}
		});

		this.setSpecificParserURL(visitorsItemObject.getUrl());
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		visitorsGalleryObject.setImages(images);

		return visitorsGalleryObject;
	}
}