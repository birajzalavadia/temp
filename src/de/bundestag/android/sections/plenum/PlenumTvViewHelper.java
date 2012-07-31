package de.bundestag.android.sections.plenum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumTvObject;

public class PlenumTvViewHelper {

	/**
	 * Creates hashmap list of elections.
	 * 
	 * Used by general list fragment.
	 */
	public static ArrayList<HashMap<String, Object>> createTv(String strDate) {
		PlenumTvObject plenumTvObject = null;
		if (DataHolder.tvSchedule != null) {
			plenumTvObject = DataHolder.tvSchedule;
		} else {
			plenumTvObject = parseTv();
		}
		ArrayList<HashMap<String, Object>> speakers = null;
		if (plenumTvObject != null) {
			List<PlenumTvObject> speakersList = plenumTvObject.getTv();
			speakers = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> hashMap;
			// first item
			hashMap = new HashMap<String, Object>();
			speakers.add(hashMap);

			for (int i = 0; i < speakersList.size(); i++) {

				String date = speakersList.get(i).getDispatchDate().toString();
				date = (date.substring(0, 4) + "-" + (date.substring(4, 6)) + "-" + (date.substring(6, 8)));

				if (date.trim().equals(strDate.trim())) {
					hashMap = new HashMap<String, Object>();
					hashMap.put("dispatchDate", speakersList.get(i).getDispatchDate());
					hashMap.put("title", speakersList.get(i).getTitle());

					hashMap.put("longTitle", speakersList.get(i).getLongTitle());
					hashMap.put("info", speakersList.get(i).getInfo());
					hashMap.put("recodingDate", speakersList.get(i).getRecordingDate());
					hashMap.put("link", speakersList.get(i).getLink());
					speakers.add(hashMap);
				}
			}
		}
		DataHolder.tvSchedule = plenumTvObject;
		return speakers;
	}

	public static PlenumTvObject parseTv() {
		PlenumXMLParser speakersParser = new PlenumXMLParser();
		PlenumTvObject speakers = null;
		try {
			speakers = speakersParser.parseTv();
		} catch (Exception e) {
//			e.printStackTrace();
		}

		return speakers;
	}
}
