package de.bundestag.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import de.bundestag.android.parser.objects.PlenumTvObject;

/**
 * Plenum XML parser.
 * 
 * This class knows how to parse plenum (Plenum) from Bundestag.
 */
public class PlenumTvXMLParser extends BaseXMLParser {
	public static final String MAIN_PLENUM_URL = "http://www.bundestag.de/includes/datasources/tv.xml";

	public PlenumTvXMLParser() {

		super(MAIN_PLENUM_URL);

	}

	/**
	 * Parse a plenum (Plenum) object.
	 */
	public PlenumTvObject parseTv() {
		final PlenumTvObject currentPlenumTvObject = new PlenumTvObject();
		final List<PlenumTvObject> tv = new ArrayList<PlenumTvObject>();

		RootElement root = new RootElement("tvprogramm");

		Element groupsElement = root.getChild("sendung");
		groupsElement.setEndElementListener(new EndElementListener() {
			public void end() {
				tv.add(currentPlenumTvObject);
			}
		});

		root.getChild("sendedatum").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumTvObject.setDispatchDate(body);
			}
		});
		root.getChild("titel").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumTvObject.setTitle(body);
			}
		});
		root.getChild("langtitel").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumTvObject.setLongTitle(body);
			}
		});
		root.getChild("infos").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumTvObject.setInfo(body);
			}
		});
		root.getChild("aufzeichnungsdatum").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumTvObject.setRecordingDate(body);
			}
		});
		root.getChild("link").setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentPlenumTvObject.setLink(body);
			}
		});
		
		currentPlenumTvObject.setTv(tv);

		return currentPlenumTvObject;
	}

}