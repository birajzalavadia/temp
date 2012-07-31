package de.bundestag.android.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * Base XML parser class.
 * 
 * Contains the general methods to open a connection to an XML feed.
 * 
 * This class is the base class for our parser.
 * 
 * It implements the XMLParser interface that makes sure that any class
 * extending this class implements the parse() method.
 */
public abstract class BaseXMLParser {
	private final String BASE_XML_URL = "http://www.bundestag.de/";

	private String xmlURLString;

	private String xmlDetailsURLString;

	private final URL xmlURL;

	private URL detailsXMLURL;

	public String getDetailsXMLURL() {
		return xmlDetailsURLString;
	}

	public String getXMLURL() {
		return xmlURLString;
	}

	/**
	 * Constructor takes an XML URL from the implementing child class.
	 */
	protected BaseXMLParser(String xmlURL) {
		String fullXMLURL = BASE_XML_URL + xmlURL;

		// Test News
//		if (xmlURL.equals("xml/aktuell/index.xml"))
//			fullXMLURL = "file:///android_asset/aktuell_index_1.3.xml";

		this.xmlURLString = fullXMLURL;

		try {
			this.xmlURL = new URL(fullXMLURL);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Set a specific parser URL. Used for example for detail links that are
	 * included in the parsed XML files.
	 */
	protected void setSpecificParserURL(String xmlURL) {
		this.xmlDetailsURLString = xmlURL;

		try {
			this.detailsXMLURL = new URL(xmlURL);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the XML feed URL.
	 */
	protected InputStream getInputStream() {
		try {
			// AssetManager as = DataHolder.context.getAssets();
			// return as.open("aktuell_index_1.3.xml");

			URLConnection conn = null;
			for (int i = 0; i < 5; i++) {
				conn = xmlURL.openConnection();
				if (conn.getContentLength() > 0)
					break;
			}

			return conn.getInputStream();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the XML feed URL.
	 */
	protected InputStream getSpecificInputStream() {
		try {

			URLConnection conn = null;
			for (int i = 0; i < 5; i++) {
				conn = detailsXMLURL.openConnection();
				
				
				if (conn.getContentLength() > 0)
					break;
				/*
				 * try{ this.wait(200); }catch(Exception e){
				 * e.printStackTrace(); }
				 */
				// Log.e("LOAD XML","RETRY " + (i+1));
			}
			return conn.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}