package de.bundestag.android.sections.plenum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.PlenumXMLParser;
import de.bundestag.android.parser.objects.PlenumObject;

public class PlenumNewsDetailsFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.plenum_news_item, container, false);
	}

	public PlenumObject createPlenumDetailsObject() {
		PlenumXMLParser plenumXMLParser = new PlenumXMLParser();

		PlenumObject news = null;
		try {
			news = plenumXMLParser.parseMain(plenumXMLParser.PLENUM_TASK_URL);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		PlenumNewsViewHelper.createDetailsViewTab(news, getActivity());
		DataHolder.releaseScreenLock(this.getActivity());
		return news;
	}

}
