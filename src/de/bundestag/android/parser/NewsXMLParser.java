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
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ParseEncodingDetector;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.NewsListObject;
import de.bundestag.android.parser.objects.NewsObject;
import de.bundestag.android.parser.objects.PlenumStreamSourceObject;
import de.bundestag.android.sections.plenum.DebateNewsActivity;
import de.bundestag.android.synchronization.NewsSynchronization;

/**
 * News XML parser.
 * 
 * This class knows how to parse news (Aktuell) from Bundestag.
 */
public class NewsXMLParser extends BaseXMLParser {
	private static final String NEWS_URL = "xml/aktuell/index.xml";
	// private static final String NEWS_URL = "bundestag/news.xml";
	// private static final String NEWS_URL =
	// "http://api.mettigel-software.com/bt/aktuell_vid.xml";
	// private static final String NEWS_URL =
	// "http://werkstatt.bundestag.de/includes/staticdatasources/testdaten_android_stream.xml";

	public static final String VISITOR_NEWS_URL = "http://www.bundestag.de/xml/besuche/aktuell/index.xml";

	public static final String DEBATE_NEWS_URL = "http://www.bundestag.de/xml/aktuell/debatten/index.xml";

	// News xml tags
	private static final String TAG_NEWS_ROOT_ELEMENT = "new";
	private static final String TAG_NEWS_LIST = "newslist";
	private static final String TAG_NEWS_ITEM = "news";
	private static final String TAG_NEWS_LIST_ITEM = "news-list";
	private static final String TAG_NEWS_STARTTEASER = "startteaser";
	private static final String TAG_NEWS_STATUS = "status";
	private static final String TAG_NEWS_DATE = "date";
	private static final String TAG_NEWS_TITLE = "title";
	private static final String TAG_NEWS_TEASER = "teaser";
	private static final String TAG_NEWS_IMAGEURL = "imageURL";
	public static final String KEY_NEWS_LIST_TITLE = "newslistTitle";

	private static final String TAG_NEWS_IMAGECOPYRIGHT = "imageCopyright";
	private static final String TAG_NEWS_DETAILSXML = "detailsXML";
	private static final String TAG_NEWS_VIDEOSTREAM = "video-stream";
	private static final String TAG_NEWS_VIDEOSTREAM_URL = "url";
	private static final String TAG_NEWS_VIDEOSTREAM_STREAMSOURCE = "streamsource";
	private static final String TAG_NEWS_LASTCHANGED = "lastChanged";
	private static final String TAG_NEWS_CHANGEDDATETIME = "changedDateTime";

	// News details xml tags
	private static final String TAG_NEWS_DETAILS_ROOT_ELEMENT = "newsdetails";
	private static final String TAG_NEWS_DETAILS_DATE = "date";
	private static final String TAG_NEWS_DETAILS_TITLE = "title";
	private static final String TAG_NEWS_DETAILS_IMAGEURL = "imageURL";
	private static final String TAG_NEWS_DETAILS_IMAGE_GROSS_URL = "imageGrossURL";
	private static final String TAG_NEWS_DETAILS_IMAGECOPYRIGHT = "imageCopyright";
	private static final String TAG_NEWS_DETAILS_IMAGELASTCHANGED = "imageLastChanged";
	private static final String TAG_NEWS_DETAILS_IMAGECHANGEDDATETIME = "imageChangedDateTime";
	private static final String TAG_NEWS_DETAILS_TEXT = "text";

	// News details xml tags
	private static final String TAG_VISITOR_NEWS_DETAILS_ROOT_ELEMENT = "newsdetails";
	private static final String TAG_VISITOR_NEWS_DETAILS_TITLE = "title";
	private static final String TAG_VISITOR_NEWS_DETAILS_TEASER = "teaser";
	private static final String TAG_VISITOR_NEWS_DETAILS_TEXT = "text";
	private static final String TAG_VISITOR_NEWS_DETAILS_IMAGE = "image";
	private static final String ATTRIBUTE_VISITOR_NEWS_DETAILS_IMAGEID = "id";
	private static final String TAG_VISITOR_NEWS_DETAILS_IMAGECOPYRIGHT = "copyright";

	public NewsXMLParser() {
		super(NEWS_URL);
	}

	/**
	 * Parse a news (Aktuell) object.
	 */
	public List<NewsObject> parse(boolean insertNewsDetails, int newsType) {
		return parse(insertNewsDetails, newsType, null);
	}

	/**
	 * Parse a news (Aktuell) object.
	 */
	public List<NewsObject> parse(boolean insertNewsDetails, int newsType, String url) {
		final NewsObject currentNewsObject = new NewsObject();

		/*
		 * if (visitorNews) {
		 * currentNewsObject.setType(NewsSynchronization.NEWS_TYPE_VISITOR); }
		 * else {
		 * currentNewsObject.setType(NewsSynchronization.NEWS_TYPE_NORMAL); }
		 */

		currentNewsObject.setType(newsType);

		RootElement root = new RootElement(TAG_NEWS_ROOT_ELEMENT);

		final List<NewsObject> newsObjects = new ArrayList<NewsObject>();

		Element newsList = root.getChild(TAG_NEWS_LIST);

		newsList.getChild(KEY_NEWS_LIST_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				if (DataHolder.currentActiviry instanceof DebateNewsActivity) {
					DataHolder.newsListTitlePlenum = body;
				} else {
					DataHolder.newsListTitle = body;
				}
			}
		});

		Element item = newsList.getChild(TAG_NEWS_ITEM);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				currentNewsObject.setIstList(false);
				newsObjects.add(currentNewsObject.copy());
				currentNewsObject.setVideoStreamURL(null);
				currentNewsObject.setImageURL(null);
			}
		});
		if (newsType == NewsSynchronization.NEWS_TYPE_NORMAL) {
			item.getChild(TAG_NEWS_STARTTEASER).setEndTextElementListener(new EndTextElementListener() {
				public void end(String body) {
					currentNewsObject.setStartteaser(body);
				}
			});
			item.getChild(TAG_NEWS_STATUS).setEndTextElementListener(new EndTextElementListener() {
				public void end(String body) {
					currentNewsObject.setStatus(body);
				}
			});
		}
		item.getChild(TAG_NEWS_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setDate(body);
			}
		});
		item.getChild(TAG_NEWS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setTitle(body);
			}
		});
		item.getChild(TAG_NEWS_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setTeaser(body);
			}
		});
		item.getChild(TAG_NEWS_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setImageURL(body);
			}
		});
		item.getChild(TAG_NEWS_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setImageCopyright(body);
			}
		});
		item.getChild(TAG_NEWS_DETAILSXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setDetailsXMLURL(body);
			}
		});
		Element videoStream = item.getChild(TAG_NEWS_VIDEOSTREAM);
		/*
		 * videoStream.getChild(TAG_NEWS_VIDEOSTREAM_URL).setEndTextElementListener
		 * (new EndTextElementListener() { public void end(String body) {
		 * currentNewsObject.setVideoURL(body); } });
		 */
		videoStream.getChild(TAG_NEWS_VIDEOSTREAM_STREAMSOURCE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				if (body != null && !body.equals("")) {
					PlenumXMLParser streamSourceParser = new PlenumXMLParser();
					ArrayList<PlenumStreamSourceObject> ss = streamSourceParser.parseStreamSource(body);
					for (PlenumStreamSourceObject s : ss) {
						if (s.getType().equals("rtsp") && s.getBandwidth().equals("500")) {
							currentNewsObject.setVideoStreamURL(s.getHref());
						}
					}
				}
				// currentNewsObject.setVideoStreamURL(body);

			}
		});
		item.getChild(TAG_NEWS_LASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setLastChanged(body);
			}
		});
		item.getChild(TAG_NEWS_CHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setChangedDateTime(body);
			}
		});

		// list
		Element itemList = newsList.getChild(TAG_NEWS_LIST_ITEM);
		itemList.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				currentNewsObject.setIstList(true);
				currentNewsObject.setType(NewsSynchronization.NEWS_TYPE_LIST);
			}
		});

		itemList.setEndElementListener(new EndElementListener() {
			public void end() {
				newsObjects.add(currentNewsObject.copy());
				currentNewsObject.setVideoStreamURL(null);
			}
		});

		itemList.getChild(TAG_NEWS_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setDate(body);
			}
		});
		itemList.getChild(TAG_NEWS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setTitle(body);
			}
		});
		itemList.getChild(TAG_NEWS_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setTeaser(body);
			}
		});
		itemList.getChild(TAG_NEWS_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setImageURL(body);
			}
		});
		itemList.getChild(TAG_NEWS_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setImageCopyright(body);
			}
		});
		itemList.getChild(TAG_NEWS_DETAILSXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setDetailsXMLURL(body);
			}
		});
		Element videoStreamList = itemList.getChild(TAG_NEWS_VIDEOSTREAM);
		videoStreamList.getChild(TAG_NEWS_VIDEOSTREAM_URL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setVideoURL(body);
			}
		});
		videoStreamList.getChild(TAG_NEWS_VIDEOSTREAM_STREAMSOURCE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setVideoStreamURL(body);
			}
		});
		itemList.getChild(TAG_NEWS_LASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setLastChanged(body);
			}
		});
		itemList.getChild(TAG_NEWS_CHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setChangedDateTime(body);
			}
		});

		try {
			if (newsType == NewsSynchronization.NEWS_TYPE_VISITOR) {
				this.setSpecificParserURL(VISITOR_NEWS_URL);
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
				Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
			} else if (newsType == NewsSynchronization.NEWS_TYPE_DEBATE) {
				this.setSpecificParserURL(DEBATE_NEWS_URL);
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
				Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
			} else if (url != null) {
				this.setSpecificParserURL(url);
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
				Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
			} else {
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getXMLURL());
				Xml.parse(this.getInputStream(), encoding, root.getContentHandler());
			}
		} catch (java.io.EOFException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Fill in the news details
		// if (insertNewsDetails)
		// {
		// int insertsSize =
		// BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER >
		// newsObjects.size()) ? newsObjects.size()
		// : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : newsObjects.size();
		// for (int i = 0; i < insertsSize; i++)
		// {
		// NewsObject newsObject = (NewsObject) newsObjects.get(i);
		// if (!newsObject.getIsList())
		// {
		// if ((newsObject.getDetailsXMLURL() != null) &&
		// (!newsObject.getDetailsXMLURL().equals("")))
		// {
		// NewsDetailsObject newsDetailsObject =
		// parseDetails(newsObject.getDetailsXMLURL());
		// newsObject.setNewsDetails(newsDetailsObject);
		// }
		// }
		// }
		// }

		// parse any list inside the news list
		for (int i = 0; i < newsObjects.size(); i++) {
			NewsObject newsObject = (NewsObject) newsObjects.get(i);
			if (newsObject.getIsList()) {
				if ((newsObject.getDetailsXMLURL() != null) && (!newsObject.getDetailsXMLURL().equals(""))) {

					List<NewsObject> newsItems = parse(insertNewsDetails, newsType, newsObject.getDetailsXMLURL());

					NewsListObject newsListObject = new NewsListObject();
					newsListObject.setItems(newsItems);

					// TODO - add the news list title
					newsObject.setNewsList(newsListObject);
				}
			}
		}

		return newsObjects;
	}

	/**
	 * Parse a news (Aktuell) object.
	 */
	public List<NewsObject> parseDates(int newsType, String url) {
		final NewsObject currentNewsObject = new NewsObject();

		currentNewsObject.setType(newsType);

		RootElement root = new RootElement(TAG_NEWS_ROOT_ELEMENT);

		final List<NewsObject> newsObjects = new ArrayList<NewsObject>();

		Element newsList = root.getChild(TAG_NEWS_LIST);

		Element item = newsList.getChild(TAG_NEWS_ITEM);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				currentNewsObject.setIstList(false);
				newsObjects.add(currentNewsObject.copy());
			}
		});
		item.getChild(TAG_NEWS_DETAILSXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setDetailsXMLURL(body);
			}
		});
		item.getChild(TAG_NEWS_CHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setChangedDateTime(body);
			}
		});

		// list
		Element itemList = newsList.getChild(TAG_NEWS_LIST_ITEM);
		itemList.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				currentNewsObject.setIstList(true);
				currentNewsObject.setType(NewsSynchronization.NEWS_TYPE_LIST);
			}
		});

		itemList.setEndElementListener(new EndElementListener() {
			public void end() {
				newsObjects.add(currentNewsObject.copy());
				currentNewsObject.setVideoStreamURL(null);
			}
		});

		itemList.getChild(TAG_NEWS_DETAILSXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setDetailsXMLURL(body);
			}
		});

		itemList.getChild(TAG_NEWS_CHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsObject.setChangedDateTime(body);
			}
		});

		try {
			if (newsType == NewsSynchronization.NEWS_TYPE_VISITOR) {
				this.setSpecificParserURL(VISITOR_NEWS_URL);
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
				Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
			} else if (newsType == NewsSynchronization.NEWS_TYPE_DEBATE) {
				this.setSpecificParserURL(DEBATE_NEWS_URL);
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
				Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
			} else if (url != null) {
				this.setSpecificParserURL(url);
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
				Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
			} else {
				Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getXMLURL());
				Xml.parse(this.getInputStream(), encoding, root.getContentHandler());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return newsObjects;
	}

	/**
	 * Parse a news details (Aktuell) object.
	 */
	public NewsDetailsObject parseDetails(String detailsURL) {
		final NewsDetailsObject currentNewsDetailsObject = new NewsDetailsObject();

		RootElement root = new RootElement(TAG_NEWS_DETAILS_ROOT_ELEMENT);

		root.getChild(TAG_NEWS_DETAILS_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setDate(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setTitle(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageURL(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGE_GROSS_URL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageGrossURL(body);
			}
		});

		root.getChild(TAG_NEWS_DETAILS_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageCopyright(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGELASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageLastChanged(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGECHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageChangedDateTime(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setText(body);
			}
		});

		this.setSpecificParserURL(detailsURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentNewsDetailsObject;
	}

	/**
	 * Parse a news details (Aktuell) object.
	 */
	public NewsDetailsObject parseDetailsDates(String detailsURL) {
		final NewsDetailsObject currentNewsDetailsObject = new NewsDetailsObject();

		RootElement root = new RootElement(TAG_NEWS_DETAILS_ROOT_ELEMENT);

		root.getChild(TAG_NEWS_DETAILS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setTitle(body);
			}
		});

		root.getChild(TAG_NEWS_DETAILS_IMAGECHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageChangedDateTime(body);
			}
		});

		this.setSpecificParserURL(detailsURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentNewsDetailsObject;
	}

	/**
	 * Parse a visitor news details (Aktuell) object.
	 */
	public NewsDetailsObject parseVisitorDetails(String detailsURL) {
		final NewsDetailsObject currentNewsDetailsObject = new NewsDetailsObject();

		RootElement root = new RootElement(TAG_VISITOR_NEWS_DETAILS_ROOT_ELEMENT);

		root.getChild(TAG_VISITOR_NEWS_DETAILS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setTitle(body);
			}
		});
		root.getChild(TAG_VISITOR_NEWS_DETAILS_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				// NOTE, hack to reuse the element
				// currentNewsDetailsObject.setImageURL(body);
			}
		});
		root.getChild(TAG_VISITOR_NEWS_DETAILS_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setText(body);
			}
		});

		root.getChild(TAG_NEWS_DETAILS_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageURL(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGE_GROSS_URL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageGrossURL(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageCopyright(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGELASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageLastChanged(body);
			}
		});
		root.getChild(TAG_NEWS_DETAILS_IMAGECHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentNewsDetailsObject.setImageChangedDateTime(body);
			}
		});

		this.setSpecificParserURL(detailsURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentNewsDetailsObject;
	}
}