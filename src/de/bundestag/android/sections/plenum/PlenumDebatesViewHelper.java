package de.bundestag.android.sections.plenum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumAgendaObject;

public class PlenumDebatesViewHelper
{
    public static final String KEY_SPEAKER_NAME = "SPEAKER_NAME";

    public static final String KEY_SPEAKER_TOPIC = "SPEAKER_TOPIC";

    public static final String KEY_SPEAKER_FUNCTION = "SPEAKER_FUNCTION";

    public static final String KEY_SPEAKER_START_TIME = "SPEAKER_START_TIME";

    public static final String KEY_SPEAKER_STATE = "SPEAKER_STATE";

    public static final String KEY_DETAIL_XML = "DETAIL_XML";

    /**
     * Creates hashmap list of elections.
     * 
     * Used by general list fragment.
     */
    public static ArrayList<HashMap<String, Object>> createDebates()
    {
        List<PlenumAgendaObject> plenumAgendaObject = parseAgenda();

        ArrayList<HashMap<String, Object>> speakers = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> hashMap;
        hashMap = new HashMap<String, Object>();
        speakers.add(hashMap);

        if (plenumAgendaObject != null)
        {
            for (int i = 0; i < plenumAgendaObject.size(); i++)
            {
                hashMap = new HashMap<String, Object>();
                hashMap.put(KEY_SPEAKER_NAME, plenumAgendaObject.get(i).getTitle());
                hashMap.put(KEY_SPEAKER_TOPIC, plenumAgendaObject.get(i).getTop());
                hashMap.put(KEY_SPEAKER_START_TIME, plenumAgendaObject.get(i).getStartTime());
                hashMap.put(KEY_SPEAKER_STATE, plenumAgendaObject.get(i).getStatus());
                hashMap.put(KEY_SPEAKER_FUNCTION, plenumAgendaObject.get(i).getProtocol());
                hashMap.put(KEY_DETAIL_XML, plenumAgendaObject.get(i).getLinkXml());
                speakers.add(hashMap);
            }
        }

        return speakers;
    }

    public static List<PlenumAgendaObject> parseAgenda()
    {
        PlenumXMLParser debatesParser = new PlenumXMLParser();
        List<PlenumAgendaObject> speakers = null;
        try
        {
            speakers = debatesParser.parseAgenda();
        } catch (Exception e)
        {
//            e.printStackTrace();
        }

        return speakers;
    }
}
