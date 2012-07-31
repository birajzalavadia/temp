package de.bundestag.android.customviews;

import de.bundestag.android.sections.plenum.PlenumSitzungenActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivityClient extends WebViewClient {
	Activity activity;

	public WebActivityClient(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Override to load every link within the page inside this webview instead
	 * of using android's default application #7 http://developer.android
	 * .com/resources/tutorials/views/hello-webview.html
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (!(activity instanceof PlenumSitzungenActivity)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			activity.startActivityForResult(intent,0);
			// Tell the WebView you took care of it.
		} else {
			Uri uriUrl = Uri.parse(url);
			if (uriUrl.toString().startsWith("http")) {
				Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
				activity.startActivity(launchBrowser);
			}
		}
		return true;

	}
}
