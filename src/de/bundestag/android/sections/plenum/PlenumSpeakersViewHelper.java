package de.bundestag.android.sections.plenum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumSpeakerItemObject;
import de.bundestag.android.parser.objects.PlenumSpeakerObject;

public class PlenumSpeakersViewHelper
{
	 public static final String KEY_SPEAKER_NAME = "SPEAKER_NAME";
	 
	 public static final String KEY_SPEAKER_TOPIC = "SPEAKER_TOPIC";
	 
	 public static final String KEY_SPEAKER_FUNCTION = "SPEAKER_FUNCTION";
	 
	 public static final String KEY_SPEAKER_START_TIME = "SPEAKER_START_TIME";
	 
	 public static final String KEY_SPEAKER_STATE = "SPEAKER_STATE";

    /**
     * Creates hashmap list of elections.
     * 
     * Used by general list fragment.
     */
    public static ArrayList<HashMap<String, Object>> createSpeakers()
    {
		PlenumSpeakerObject plenumSpeakerObject = parseSpeakers();
		//PlenumXMLParser.parseSpeakers(); 
		ArrayList<HashMap<String, Object>> speakers = null;
		if (plenumSpeakerObject != null)
		{
	        List<PlenumSpeakerItemObject> speakersList = plenumSpeakerObject.getSpeakers();
	        speakers = new ArrayList<HashMap<String, Object>>();
	        HashMap<String, Object> hashMap;
	        //first item
	        hashMap = new HashMap<String, Object>();
	        speakers.add(hashMap);
	        
	        for (int i = 0; i < speakersList.size(); i++)
	        {
	            hashMap = new HashMap<String, Object>();
	            hashMap.put(KEY_SPEAKER_NAME, speakersList.get(i).getName());
	            hashMap.put(KEY_SPEAKER_TOPIC, speakersList.get(i).getTopic());
	            hashMap.put(KEY_SPEAKER_START_TIME, speakersList.get(i).getStartTime());
	            hashMap.put(KEY_SPEAKER_STATE, speakersList.get(i).getState());
	            hashMap.put(KEY_SPEAKER_FUNCTION, speakersList.get(i).getFunction());
	            speakers.add(hashMap);
	        }
		}

        return speakers;
    }

    public static PlenumSpeakerObject parseSpeakers()
    {
    	PlenumXMLParser speakersParser = new PlenumXMLParser();
    	PlenumSpeakerObject speakers = null;
        try
        {
            speakers = speakersParser.parseSpeakers();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return speakers;
    }
}
