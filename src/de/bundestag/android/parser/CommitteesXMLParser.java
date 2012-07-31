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
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.parser.objects.CommitteesObject;

/**
 * Committees XML parser.
 * 
 * This class knows how to parse Committees (Ausschusse) from Bundestag.
 */
public class CommitteesXMLParser extends BaseXMLParser {
	private static final String COMMITTEE_URL = "xml/ausschuesse/index.xml";

	// Committee xml tags
	private static final String TAG_COMMITTEE_ROOT_ELEMENT = "ausschuesseUebersicht";
	private static final String TAG_COMMITTEE_LIST = "ausschuesse";
	private static final String TAG_COMMITTEE_ITEM = "ausschuss";
	private static final String ATTRIBUTE_COMMITTEE_ID = "id";
	private static final String TAG_COMMITTEE_NAME = "ausschussName";
	private static final String TAG_COMMITTEE_COURSE = "ausschussKurzName";
	private static final String TAG_COMMITTEE_TEASER = "ausschussTeaser";
	private static final String TAG_COMMITTEE_LASTCHANGED = "lastChanged";
	private static final String TAG_COMMITTEE_CHANGEDDATETIME = "changedDateTime";
	private static final String TAG_COMMITTEE_DETAILSXML = "ausschussDetailXML";
	private static final String TAG_COMMITTEE_IMAGELASTCHANGED = "imageLastChanged";
	private static final String TAG_COMMITTEE_IMAGECHANGEDDATETIME = "imageChangedDateTime";
	private static final String TAG_COMMITTEE_DETAILS_TAG = "ausschuessestartartikel";

	// Committee details xml tags
	private static final String TAG_COMMITTEE_DETAILS_ROOT_ELEMENT = "ausschussDetail";

	private static final String TAG_COMMITTEE_DETAILS_NAME = "ausschussName";
	private static final String TAG_COMMITTEE_DETAILS_PHOTOURL = "ausschussBildURL";
	private static final String TAG_COMMITTEE_DETAILS_PHOTOCOPYRIGHT = "ausschussBildCopyright";
	private static final String TAG_COMMITTEE_DETAILS_DESCRIPTION = "ausschussAufgabe";

	private static final String TAG_COMMITTEE_DETAILS_NEWS_LIST = "newslist";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_ITEM = "news";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DATE = "date";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_TITLE = "title";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_TEASER = "teaser";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_IMAGEURL = "imageGrossURL";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_IMAGECOPYRIGHT = "imageCopyright";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILSXML = "detailsXML";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_LASTCHANGED = "lastChanged";

	// Committee details news details xml tags
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_ROOT_ELEMENT = "newsdetails";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_DATE = "date";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_TITLE = "title";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGEURL = "imageGrossURL";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGECOPYRIGHT = "imageCopyright";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGELASTCHANGED = "imageLastChanged";
	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGECHANGEDDATETIME = "imageChangedDateTime";

	private static final String TAG_COMMITTEE_DETAILS_NEWS_DETAILS_TEXT = "text";

	public CommitteesXMLParser() {
		super(COMMITTEE_URL);
	}

	/**
	 * Parse a committees (Ausschusse) object.
	 */
	public List<CommitteesObject> parse(boolean insertCommitteeDetails) {
		final CommitteesObject currentCommitteesObject = new CommitteesObject();

		RootElement root = new RootElement(TAG_COMMITTEE_ROOT_ELEMENT);

		final List<CommitteesObject> committees = new ArrayList<CommitteesObject>();

		Element committeesList = root.getChild(TAG_COMMITTEE_LIST);

		Element item = committeesList.getChild(TAG_COMMITTEE_ITEM);

		item.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				currentCommitteesObject.setId(attributes.getValue(ATTRIBUTE_COMMITTEE_ID));
			}
		});

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				committees.add(currentCommitteesObject.copy());
			}
		});

		item.getChild(TAG_COMMITTEE_NAME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setName(body);
			}
		});
		item.getChild(TAG_COMMITTEE_COURSE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setCourse(body);
			}
		});
		item.getChild(TAG_COMMITTEE_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setTeaser(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILSXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setDetailsXML(body);
			}
		});
		item.getChild(TAG_COMMITTEE_LASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setLastChanged(body);
			}
		});
		item.getChild(TAG_COMMITTEE_CHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setChangedDateTime(body);
			}
		});
		item.getChild(TAG_COMMITTEE_IMAGELASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setImageLastChanged(body);
			}
		});
		item.getChild(TAG_COMMITTEE_IMAGECHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesObject.setImageChangedDateTime(body);
			}
		});

		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getXMLURL());
			Xml.parse(this.getInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (insertCommitteeDetails) {
			// Fill in the news and the news details
			int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > committees.size()) ? committees.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER)
					: committees.size();
			for (int i = 0; i < insertsSize; i++) {
				CommitteesObject committeesObject = (CommitteesObject) committees.get(i);
				if ((committeesObject.getDetailsXML() != null) && (!committeesObject.getDetailsXML().equals(""))) {
					CommitteesDetailsObject committeesDetailsObject = parseDetails(committeesObject.getDetailsXML(), committeesObject.getId());
					committeesObject.setCommitteeDetails(committeesDetailsObject);
				}
			}
		}

		return committees;
	}

	/**
	 * Parse a committee details (Ausschuse) object.
	 */
	public CommitteesDetailsObject parseDetails(String detailsURL, String committeeId) {
		final CommitteesDetailsObject currentCommitteesDetailsObject = new CommitteesDetailsObject();

		RootElement root = new RootElement(TAG_COMMITTEE_DETAILS_ROOT_ELEMENT);
		root.getChild(TAG_COMMITTEE_DETAILS_NAME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setName(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_PHOTOURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setPhotoURL(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_PHOTOCOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setPhotoCopyright(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setDescription(body);
			}
		});

		this.setSpecificParserURL(detailsURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentCommitteesDetailsObject;
	}

	/**
	 * Parse a committee details (Ausschuse) object.
	 */
	public CommitteesDetailsObject parseNewsDetails(String detailsURL, String committeeId) {
		final CommitteesDetailsObject currentCommitteesDetailsObject = new CommitteesDetailsObject();

		RootElement root = new RootElement(TAG_COMMITTEE_DETAILS_ROOT_ELEMENT);
		root.getChild(TAG_COMMITTEE_DETAILS_NAME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setName(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_PHOTOURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setPhotoURL(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_PHOTOCOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setPhotoCopyright(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsObject.setDescription(body);
			}
		});

		final CommitteesDetailsNewsObject currentCommitteesDetailsNewsObject = new CommitteesDetailsNewsObject();
		final List<CommitteesDetailsNewsObject> committeesDetailsNews = new ArrayList<CommitteesDetailsNewsObject>();

		Element committeesList = root.getChild(TAG_COMMITTEE_DETAILS_NEWS_LIST);
		Element item = committeesList.getChild(TAG_COMMITTEE_DETAILS_NEWS_ITEM);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				committeesDetailsNews.add(currentCommitteesDetailsNewsObject.copy());
			}
		});

		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setDate(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setTitle(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setTeaser(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setImageURL(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setImageCopyright(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILSXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setDetailsXML(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_LASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setLastChanged(body);
			}
		});

		this.setSpecificParserURL(detailsURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Fill in the news and the news details
		// int insertsSize = BaseActivity.isDebugOn() ?
		// ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER >
		// committeesDetailsNews.size()) ? committeesDetailsNews.size() :
		// BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) :
		// committeesDetailsNews.size();
		// for (int i = 0; i < insertsSize; i++)
		// {
		// CommitteesDetailsNewsObject committeesDetailsNewsObject =
		// (CommitteesDetailsNewsObject) committeesDetailsNews.get(i);
		// if ((committeesDetailsNewsObject.getDetailsXML() != null) &&
		// (!committeesDetailsNewsObject.getDetailsXML().equals("")))
		// {
		// CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject
		// = parseDetailsNews(committeesDetailsNewsObject.getDetailsXML());
		// committeesDetailsNewsObject.setNewsDetails(committeesDetailsNewsDetailsObject);
		// committeesDetailsNewsObject.setCommitteeId(committeeId);
		// }
		// }

		// add committees details news
		currentCommitteesDetailsObject.setDetailNews(committeesDetailsNews);

		return currentCommitteesDetailsObject;
	}

	/**
	 * Parse a committee details (Ausschuse) object.
	 */
	public CommitteesDetailsObject parseNewsDetailsDates(String detailsURL) {
		final CommitteesDetailsObject currentCommitteesDetailsObject = new CommitteesDetailsObject();

		RootElement root = new RootElement(TAG_COMMITTEE_DETAILS_ROOT_ELEMENT);

		final CommitteesDetailsNewsObject currentCommitteesDetailsNewsObject = new CommitteesDetailsNewsObject();
		final List<CommitteesDetailsNewsObject> committeesDetailsNews = new ArrayList<CommitteesDetailsNewsObject>();

		Element committeesList = root.getChild(TAG_COMMITTEE_DETAILS_NEWS_LIST);
		Element item = committeesList.getChild(TAG_COMMITTEE_DETAILS_NEWS_ITEM);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				committeesDetailsNews.add(currentCommitteesDetailsNewsObject.copy());
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILSXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setDetailsXML(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_LASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsObject.setLastChanged(body);
			}
		});

		this.setSpecificParserURL(detailsURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// add committees details news
		currentCommitteesDetailsObject.setDetailNews(committeesDetailsNews);

		return currentCommitteesDetailsObject;
	}

	/**
	 * Parse a committee details (Ausschuse) object.
	 */
	public CommitteesDetailsNewsDetailsObject parseDetailsNews(String detailsURL) {
		final CommitteesDetailsNewsDetailsObject currentCommitteesDetailsNewsDetailsObject = new CommitteesDetailsNewsDetailsObject();

		RootElement root = new RootElement(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_ROOT_ELEMENT);
		root.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setDate(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setTitle(body);
			}
		});

		root.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageURL(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageCopyright(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGELASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageLastChanged(body);
			}
		});
		root.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGECHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageChangedDateTime(body);
			}
		});

		root.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setText(body);
			}
		});

		this.setSpecificParserURL(detailsURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentCommitteesDetailsNewsDetailsObject;
	}

	public CommitteesDetailsNewsDetailsObject parseDetailsNewsFirstItem() {
		final CommitteesDetailsNewsDetailsObject currentCommitteesDetailsNewsDetailsObject = new CommitteesDetailsNewsDetailsObject();

		RootElement root = new RootElement(TAG_COMMITTEE_ROOT_ELEMENT);
		Element item = root.getChild(TAG_COMMITTEE_DETAILS_TAG);

		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setDate(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setTitle(body);
			}
		});

		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageURL(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageCopyright(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGELASTCHANGED).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageLastChanged(body);
			}
		});
		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_IMAGECHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setImageChangedDateTime(body);
			}
		});

		item.getChild(TAG_COMMITTEE_DETAILS_NEWS_DETAILS_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentCommitteesDetailsNewsDetailsObject.setText(body);
			}
		});
		this.setSpecificParserURL(getXMLURL());
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getXMLURL());
			Xml.parse(this.getInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentCommitteesDetailsNewsDetailsObject;
	}

}