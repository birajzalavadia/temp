package de.bundestag.android.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.mozilla.universalchardet.UniversalDetector;

import android.util.Xml;

/**
 * Helper class to retrieve the XML encoding format.
 * 
 * Receives the XML URL input stream and returns an encoding format.
 */
public class ParseEncodingDetector
{
    public static Xml.Encoding detectEncoding(String detectStreamURL) throws IOException
    {
        URL streamURL = new URL(detectStreamURL);

        URLConnection conn = null;
        for(int i = 0; i < 5; i++){
     	   conn = streamURL.openConnection();
     	   if(conn.getContentLength()>0)
     		   break;
     	   //Log.i("DETECT ENCODING","RETRY " + (i+1));
        }
        if(conn.getContentLength()<0){
           System.out.println("Unable to detect encoding - Using default");
           return Xml.Encoding.ISO_8859_1;	
        }
        
        InputStream inputStream = conn.getInputStream();
       // InputStream inputStream = streamURL.openConnection().getInputStream();

        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        byte[] buf = new byte[4096];
        while ((nread = inputStream.read(buf)) > 0 && !detector.isDone())
        {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();

        inputStream.close();

        String encoding = detector.getDetectedCharset();
        if (encoding != null)
        {
            System.out.println("Detected encoding = " + encoding);
        }
        else
        {
            System.out.println("No encoding detected.");
        }
        detector.reset();

        Xml.Encoding encodingType = Xml.Encoding.ISO_8859_1;
        if ((encoding != null) && (encoding.equals("UTF-8")))
        {
            encodingType = Xml.Encoding.UTF_8;
        }

        return encodingType;
    }
}