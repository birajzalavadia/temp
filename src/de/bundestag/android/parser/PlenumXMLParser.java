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
import de.bundestag.android.helpers.ParseEncodingDetector;
import de.bundestag.android.parser.objects.PlenumAgendaObject;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumSimpleObject;
import de.bundestag.android.parser.objects.PlenumSpeakerItemObject;
import de.bundestag.android.parser.objects.PlenumSpeakerObject;
import de.bundestag.android.parser.objects.PlenumStreamObject;
import de.bundestag.android.parser.objects.PlenumStreamSourceObject;
import de.bundestag.android.parser.objects.PlenumTeaserObject;
import de.bundestag.android.parser.objects.PlenumTvObject;

/**
 * Plenum XML parser.
 * 
 * This class knows how to parse plenum (Plenum) from Bundestag.
 */
public class PlenumXMLParser extends BaseXMLParser {
	 public static final String MAIN_PLENUM_URL =
	 "http://www.bundestag.de/xml/plenum/index.xml";
//	public static final String MAIN_PLENUM_URL = "http://202.131.103.44:7233/babiel_xml/index.xml";

	public static final String SIMPLE1_PLENUM_URL = "http://www.bundestag.de/xml/plenum/1.xml";
	public static final String SIMPLE2_PLENUM_URL = "http://www.bundestag.de/xml/plenum/2.xml";
	public static final String SIMPLE3_PLENUM_URL = "http://www.bundestag.de/xml/plenum/3.xml";
	public static final String SIMPLE4_PLENUM_URL = "http://www.bundestag.de/xml/plenum/4.xml";

	private static final String STREAM_PLENUM_URL = "http://www.bundestag.de/xml/plenum/stream/index.xml";

	// private static final String PLENUM_TEASER_URL =
	// "includes/bausteine/homepage_deutsch/startTeaserPlenarsitzung.xml";
	private static final String PLENUM_TEASER_URL = "http://www.bundestag.de/includes/bausteine/homepage_deutsch/startTeaserPlenarsitzung.xml";

	// private static final String PLENUM_SPEAKER_URL =
	// "includes/staticdatasources/plenum/speaker.xml";

	 private static final String PLENUM_SPEAKER_URL =
	 "http://www.bundestag.de/includes/staticdatasources/plenum/speaker.xml";

//	private static final String PLENUM_SPEAKER_URL = "http://202.131.103.44:7233/babiel_xml/speaker.xml";
	private static final String PLENUM_TV_URL = "http://www.bundestag.de/includes/datasources/tv.xml";
	// private static final String PLENUM_SPEAKER_URL =
	// "http://eae.mine.nu/bundestag/plenum_redner.xml";

	// plenum_akuell

	// private static final String PLENUM_AGENDA_URL =
	// "includes/datasources/plenum/tagesordnungen.xml";

	 private static final String PLENUM_AGENDA_URL =
	 "http://www.bundestag.de/includes/datasources/plenum/tagesordnungen.xml";
//	private static final String PLENUM_AGENDA_URL = "http://202.131.103.44:7233/babiel_xml/tagesordnungen.xml";

	// private static final String PLENUM_AGENDA_URL =
	// "http://eae.mine.nu/bundestag/plenum_speakers.xml";

	// public static final String PLENUM_TASK_URL =
	// "http://www.bundestag.de/xml/plenum/aufgaben/index.xml";

	// new
	public static final String PLENUM_TASK_URL = "http://www.bundestag.de/xml/plenum/aufgabe/index.xml";

	// main
	private static final String TAG_PLENUM_ROOT_ELEMENT = "newsdetails";
	private static final String TAG_PLENUM_STATUS = "status";
	private static final String TAG_PLENUM_DATE = "date";
	private static final String TAG_PLENUM_TITLE = "title";
	private static final String TAG_PLENUM_IMAGEURL = "imageURL";
	private static final String TAG_PLENUM_IMAGECOPYRIGHT = "imageCopyright";
	private static final String TAG_PLENUM_TEASER = "teaser";
	private static final String TAG_PLENUM_TEXT = "text";
	private static final String TAG_PLENUM_DETAILS_XML = "detailsXML";

	// simple
	private static final String TAG_PLENUM_SIMPLE_ROOT_ELEMENT = "newsdetails";
	private static final String TAG_PLENUM_SIMPLE_DATE = "date";
	private static final String TAG_PLENUM_SIMPLE_TITLE = "title";
	private static final String TAG_PLENUM_SIMPLE_TEXT = "text";

	// stream
	private static final String TAG_PLENUM_STREAM_ROOT_ELEMENT = "stream-config";
	private static final String TAG_PLENUM_STREAM_STREAM = "stream";
	private static final String TAG_PLENUM_STREAM_URL = "url";
	private static final String TAG_PLENUM_STREAM_VIDEOSTREAM = "video-stream";
	private static final String TAG_PLENUM_STREAM_VIDEOSTREAM_URL = "url";
	private static final String TAG_PLENUM_STREAM_VIDEOSTREAM_SOURCE = "streamsource";
	private static final String TAG_PLENUM_STREAM_VIDEOSTREAM_STARTIMG = "start-img-url";
	private static final String TAG_PLENUM_STREAM_VIDEOSTREAM_TITLE = "title";
	private static final String TAG_PLENUM_STREAM_VIDEOSTREAM_DESCRIPTION = "description";

	// stream source
	private static final String TAG_PLENUM_STREAM_SOURCE = "mobilefeed";
	private static final String TAG_PLENUM_STREAM_SOURCE_GROUP = "group";
	private static final String TAG_PLENUM_STREAM_SOURCE_STREAM = "stream";
	private static final String ATT_PLENUM_STREAM_SOURCE_GROUP_TYPE = "type";
	private static final String ATT_PLENUM_STREAM_SOURCE_STREAM_BANDWIDTH = "bandwidth";
	private static final String ATT_PLENUM_STREAM_SOURCE_STREAM_HREF = "href";

	// teaser
	private static final String TAG_PLENUM_TEASER_ROOT_ELEMENT = "plenarsitzung";
	private static final String TAG_PLENUM_TEASER_TITLE = "plenarsitzungtitle";
	private static final String TAG_PLENUM_TEASER_LINK = "plenarsitzunglink";
	private static final String TAG_PLENUM_TEASER_XMLLINK = "plenarsitzungxmllink";
	private static final String TAG_PLENUM_TEASER_TEASER = "plenarsitzungteaser";
	private static final String TAG_PLENUM_TEASER_IMAGE = "plenarsitzungimage";

	// speaker
	private static final String TAG_PLENUM_SPEAKER_ROOT_ELEMENT = "plenum";
	private static final String TAG_PLENUM_SPEAKER_TOPICNUMBER = "topicNumber";
	private static final String TAG_PLENUM_SPEAKER_LIVE = "live";
	private static final String TAG_PLENUM_SPEAKER_SPEAKERS = "speakers";
	private static final String TAG_PLENUM_SPEAKER_SPEAKER_ITEM = "speaker";
	private static final String TAG_PLENUM_SPEAKER_SPEAKER_ITEM_TOPIC = "topic";
	private static final String TAG_PLENUM_SPEAKER_SPEAKER_ITEM_STARTTIME = "startTime";
	private static final String TAG_PLENUM_SPEAKER_SPEAKER_ITEM_STATE = "state";
	private static final String TAG_PLENUM_SPEAKER_SPEAKER_ITEM_FUNCTION = "function";
	private static final String TAG_PLENUM_SPEAKER_SPEAKER_ITEM_NAME = "name";

	// agenda
	private static final String TAG_PLENUM_AGENDA_ROOT_ELEMENT = "tagesordnungen";
	private static final String TAG_PLENUM_AGENDA_ITEM = "tagesordnung";
	private static final String TAG_PLENUM_AGENDA_ITEM_STARTTIME = "startzeit";
	private static final String TAG_PLENUM_AGENDA_ITEM_ENDTIME = "endzeit";
	private static final String TAG_PLENUM_AGENDA_ITEM_STATUS = "status";
	private static final String TAG_PLENUM_AGENDA_ITEM_TITLE = "titel";
	private static final String TAG_PLENUM_AGENDA_ITEM_TOP = "top";
	private static final String TAG_PLENUM_AGENDA_ITEM_PROTOCOL = "protokoll";
	private static final String TAG_PLENUM_AGENDA_ITEM_LINK = "link";
	private static final String TAG_PLENUM_AGENDA_ITEM_LINKXML = "xmlLink";

	public PlenumXMLParser() {

		super(MAIN_PLENUM_URL);

	}

	/**
	 * Parse a plenum (Plenum) object.
	 */
	public PlenumObject parseMain(String url) {
		final PlenumObject currentPlenumObject = new PlenumObject();

		RootElement root = new RootElement(TAG_PLENUM_ROOT_ELEMENT);

		root.getChild(TAG_PLENUM_STATUS).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setStatus(body);
			}
		});
		root.getChild(TAG_PLENUM_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setDate(body);
			}
		});
		root.getChild(TAG_PLENUM_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setTitle(body);
			}
		});
		root.getChild(TAG_PLENUM_IMAGEURL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setImageURL(body);
			}
		});
		root.getChild(TAG_PLENUM_IMAGECOPYRIGHT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setImageCopyright(body);
			}
		});
		root.getChild(TAG_PLENUM_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setTeaser(body);
			}
		});
		root.getChild(TAG_PLENUM_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setText(body);
			}
		});

		root.getChild(TAG_PLENUM_DETAILS_XML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setDetailsXML(body);
			}
		});

		this.setSpecificParserURL(url);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentPlenumObject;
	}

	/**
	 * Parse a plenum stream (Plenum) object.
	 */
	public PlenumStreamObject parseStream() {
		final PlenumStreamObject currentPlenumObject = new PlenumStreamObject();

		RootElement root = new RootElement(TAG_PLENUM_STREAM_ROOT_ELEMENT);
		Element streamElement = root.getChild(TAG_PLENUM_STREAM_STREAM);
		streamElement.getChild(TAG_PLENUM_STREAM_URL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setStreamURL(body);
			}
		});

		Element videoStreamElement = root.getChild(TAG_PLENUM_STREAM_VIDEOSTREAM);
		videoStreamElement.getChild(TAG_PLENUM_STREAM_VIDEOSTREAM_URL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setVideoStreamURL(body);
			}
		});
		videoStreamElement.getChild(TAG_PLENUM_STREAM_VIDEOSTREAM_SOURCE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setVideoStreamSource(body);
			}
		});
		videoStreamElement.getChild(TAG_PLENUM_STREAM_VIDEOSTREAM_STARTIMG).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setVideoStreamImage(body);
			}
		});
		videoStreamElement.getChild(TAG_PLENUM_STREAM_VIDEOSTREAM_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setVideoStreamTitle(body);
			}
		});
		videoStreamElement.getChild(TAG_PLENUM_STREAM_VIDEOSTREAM_DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setVideoStreamDescription(body);
			}
		});

		this.setSpecificParserURL(STREAM_PLENUM_URL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentPlenumObject;
	}

	/**
	 * Parse Video Stream source.
	 */
	public ArrayList<PlenumStreamSourceObject> parseStreamSource(String simpleURL) {
		final PlenumStreamSourceObject currentStreamSource = new PlenumStreamSourceObject();
		final ArrayList<PlenumStreamSourceObject> streamSources = new ArrayList<PlenumStreamSourceObject>();
		final String streamSourceGroup;

		RootElement root = new RootElement(TAG_PLENUM_STREAM_SOURCE);
		Element group = root.getChild(TAG_PLENUM_STREAM_SOURCE_GROUP);

		group.setStartElementListener(new StartElementListener() {

			public void start(Attributes attributes) {
				currentStreamSource.setType(attributes.getValue(ATT_PLENUM_STREAM_SOURCE_GROUP_TYPE));
			}
		});

		group.getChild(TAG_PLENUM_STREAM_SOURCE_STREAM).setStartElementListener(new StartElementListener() {

			public void start(Attributes attributes) {
				currentStreamSource.setBandwidth(attributes.getValue(ATT_PLENUM_STREAM_SOURCE_STREAM_BANDWIDTH));
				currentStreamSource.setHref(attributes.getValue(ATT_PLENUM_STREAM_SOURCE_STREAM_HREF));

				streamSources.add(currentStreamSource.copy());
			}
		});

		this.setSpecificParserURL(simpleURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return streamSources;
	}

	/**
	 * Parse a plenum simple (Plenum) object.
	 */
	public PlenumSimpleObject parseSimpleObject(String simpleURL) {
		final PlenumSimpleObject currentPlenumObject = new PlenumSimpleObject();

		RootElement root = new RootElement(TAG_PLENUM_SIMPLE_ROOT_ELEMENT);
		root.getChild(TAG_PLENUM_SIMPLE_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setDate(body);
			}
		});
		root.getChild(TAG_PLENUM_SIMPLE_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setTitle(body);
			}
		});
		root.getChild(TAG_PLENUM_SIMPLE_TEXT).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setText(body);
			}
		});

		this.setSpecificParserURL(simpleURL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentPlenumObject;
	}

	/**
	 * Parse a plenum teaser (Plenum) object.
	 */
	public PlenumTeaserObject parseTeaser() {
		final PlenumTeaserObject currentPlenumObject = new PlenumTeaserObject();

		RootElement root = new RootElement(TAG_PLENUM_TEASER_ROOT_ELEMENT);
		root.getChild(TAG_PLENUM_TEASER_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setTitle(body);
			}
		});
		root.getChild(TAG_PLENUM_TEASER_LINK).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setLink(body);
			}
		});
		root.getChild(TAG_PLENUM_TEASER_XMLLINK).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setLinkXML(body);
			}
		});
		root.getChild(TAG_PLENUM_TEASER_TEASER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setTeaser(body);
			}
		});
		root.getChild(TAG_PLENUM_TEASER_IMAGE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setImage(body);
			}
		});

		this.setSpecificParserURL(PLENUM_TEASER_URL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentPlenumObject;
	}

	/**
	 * Parse plenum Tv (Plenum) object.
	 */
	public PlenumTvObject parseTv() {
		final PlenumTvObject currentPlenumObject = new PlenumTvObject();

		RootElement root = new RootElement("tvprogramm");

		// speakers
		final List<PlenumTvObject> speakers = new ArrayList<PlenumTvObject>();

		Element groupsElement = root.getChild("sendung");
		groupsElement.setEndElementListener(new EndElementListener() {
			public void end() {
				speakers.add(currentPlenumObject.copy());
			}
		});

		groupsElement.getChild("sendedatum").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setDispatchDate(body);
			}
		});
		groupsElement.getChild("titel").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setTitle(body);
			}
		});
		groupsElement.getChild("langtitel").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setLongTitle(body);
			}
		});
		groupsElement.getChild("infos").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setInfo(body);
			}
		});
		groupsElement.getChild("aufzeichnungsdatum").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setRecordingDate(body);
			}
		});
		groupsElement.getChild("link").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setLink(body);
			}
		});
		currentPlenumObject.setTv(speakers);

		this.setSpecificParserURL(PLENUM_TV_URL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(),encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentPlenumObject;
	}

	/**
	 * Parse plenum speakers (Plenum) object.
	 */
	public PlenumSpeakerObject parseSpeakers() {
		final PlenumSpeakerObject currentPlenumObject = new PlenumSpeakerObject();

		RootElement root = new RootElement(TAG_PLENUM_SPEAKER_ROOT_ELEMENT);
		root.getChild(TAG_PLENUM_SPEAKER_TOPICNUMBER).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setTopicNumber(body);
			}
		});
		root.getChild(TAG_PLENUM_SPEAKER_LIVE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumObject.setLive(body);
			}
		});

		Element speakersElement = root.getChild(TAG_PLENUM_SPEAKER_SPEAKERS);
		// speakers
		final PlenumSpeakerItemObject plenumSpeakerItemObject = new PlenumSpeakerItemObject();
		final List<PlenumSpeakerItemObject> speakers = new ArrayList<PlenumSpeakerItemObject>();

		Element groupsElement = speakersElement.getChild(TAG_PLENUM_SPEAKER_SPEAKER_ITEM);
		groupsElement.setEndElementListener(new EndElementListener() {
			public void end() {
				speakers.add(plenumSpeakerItemObject.copy());
			}
		});

		groupsElement.getChild(TAG_PLENUM_SPEAKER_SPEAKER_ITEM_TOPIC).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumSpeakerItemObject.setTopic(body);
			}
		});
		groupsElement.getChild(TAG_PLENUM_SPEAKER_SPEAKER_ITEM_STARTTIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumSpeakerItemObject.setStartTime(body);
			}
		});
		groupsElement.getChild(TAG_PLENUM_SPEAKER_SPEAKER_ITEM_STATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumSpeakerItemObject.setState(body);
			}
		});
		groupsElement.getChild(TAG_PLENUM_SPEAKER_SPEAKER_ITEM_FUNCTION).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumSpeakerItemObject.setFunction(body);
			}
		});
		groupsElement.getChild(TAG_PLENUM_SPEAKER_SPEAKER_ITEM_NAME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumSpeakerItemObject.setName(body);
			}
		});
		currentPlenumObject.setSpeakers(speakers);

		this.setSpecificParserURL(PLENUM_SPEAKER_URL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return currentPlenumObject;
	}

	/**
	 * Parse plenum agendas (Plenum) object.
	 */
	public List<PlenumAgendaObject> parseAgenda() {
		final List<PlenumAgendaObject> agendas = new ArrayList<PlenumAgendaObject>();

		RootElement root = new RootElement(TAG_PLENUM_AGENDA_ROOT_ELEMENT);

		// speakers
		final PlenumAgendaObject plenumAgendaObject = new PlenumAgendaObject();

		Element agendasElement = root.getChild(TAG_PLENUM_AGENDA_ITEM);
		agendasElement.setEndElementListener(new EndElementListener() {
			public void end() {
				agendas.add(plenumAgendaObject.copy());
			}
		});

		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_STARTTIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setStartTime(body);
			}
		});
		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_ENDTIME).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setEndTime(body);
			}
		});
		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_STATUS).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setStatus(body);
			}
		});
		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setTitle(body);
			}
		});
		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_TOP).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setTop(body);
			}
		});
		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_PROTOCOL).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setProtocol(body);
			}
		});
		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_LINK).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setLink(body);
			}
		});
		agendasElement.getChild(TAG_PLENUM_AGENDA_ITEM_LINKXML).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				plenumAgendaObject.setLinkXml(body);
			}
		});

		this.setSpecificParserURL(PLENUM_AGENDA_URL);
		try {
			Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getDetailsXMLURL());
			Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return agendas;
	}
}