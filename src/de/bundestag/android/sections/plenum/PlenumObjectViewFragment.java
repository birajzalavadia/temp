package de.bundestag.android.sections.plenum;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.bundestag.android.BaseFragment;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.customviews.WebActivityClient;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.storage.PlenumDatabaseAdapter;
import de.bundestag.android.synchronization.PlenumSynchronization;

public class PlenumObjectViewFragment extends BaseFragment {
	String plenumObjectType = "";

	WebView text;

	public void setPlenumObjectType(String plenumObjectType) {
		this.plenumObjectType = plenumObjectType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.plenum_detail_item, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void createPlenumObject() {
		final PlenumDatabaseAdapter pdb = new PlenumDatabaseAdapter(this.getActivity());
		pdb.open();
		final Cursor obj = pdb.fetchPlenumByType(Integer.parseInt(plenumObjectType));
		obj.moveToFirst();
		TextView title = (TextView) getActivity().findViewById(R.id.title);
		// title.setTypeface(faceGeorgia);
		title.setText(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TITLE)));
		ImageView img = (ImageView) getActivity().findViewById(R.id.image);
		TextView copyright = ((TextView) getActivity().findViewById(R.id.copyright));
		if (!AppConstant.isFragmentSupported) {
			copyright.setVisibility(View.VISIBLE);
			String imageString = obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGESTRING));

			if (imageString != null) {

				img.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));
				img.setVisibility(View.VISIBLE);
				String copy = obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGECOPYRIGHT));
				if (copy != null && !copy.equals("")) {
					copyright.setText("\u00A9 " + copy);
				}
			}
		} else {
			img.setVisibility(View.GONE);
			copyright.setVisibility(View.GONE);
		}

		final ScrollView scrView = (ScrollView) getView().findViewById(R.id.scrView);
		scrView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		scrView.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrView.fullScroll(ScrollView.FOCUS_UP);
				text = (WebView) getActivity().findViewById(R.id.textWebView);
//				text.setOnTouchListener(new OnTouchListener() {
//					
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						// TODO Auto-generated method stub
//						text.setWebViewClient(new WebActivityClient(getActivity()));
//						return false;
//					}
//				});
			
				String reformatedText = TextHelper.customizedFromHtmlforWebViewTab(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TEXT)));
				text.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);
				text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				text.loadDataWithBaseURL("file:///android_asset/", reformatedText, "text/html", "UTF-8", null);
				text.setBackgroundColor(Color.TRANSPARENT);
				obj.close();
				pdb.close();
				DataHolder.releaseScreenLock(PlenumObjectViewFragment.this.getActivity());
			}
		}, 1000);

	}

	public PlenumObject getPlenumTaskObject() {
		final PlenumObject plenumObject = new PlenumObject();

		PlenumDatabaseAdapter pdb = new PlenumDatabaseAdapter(getActivity());
		pdb.open();

		Cursor obj = pdb.fetchPlenumByType(PlenumSynchronization.PLENUM_TYPE_TASK);

		if ((obj != null) && (obj.getCount() > 0)) {
			obj.moveToFirst();

			plenumObject.setTitle(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TITLE)));
			plenumObject.setImageString(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGESTRING)));
			plenumObject.setImageCopyright(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_IMAGECOPYRIGHT)));

			plenumObject.setText(obj.getString(obj.getColumnIndex(PlenumDatabaseAdapter.KEY_TEXT)));
		}
		obj.close();
		pdb.close();
		PlenumAufgabeViewHelper.createViewTab(plenumObject, getActivity());
		// DataHolder.releaseScreenLock(this.getActivity());
		return plenumObject;
	}

}
