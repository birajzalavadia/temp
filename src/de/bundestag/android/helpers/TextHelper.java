package de.bundestag.android.helpers;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.VisitorsArticleObject;

/**
 * Text helper.
 * 
 * Contains static methods to format text.
 */
public abstract class TextHelper {
	public static String checkNull(String text) {
		if (text == null) {
			return "";
		}

		return text;
	}

	/**
	 * Replaces the strongs with b in the HTML
	 * 
	 * @param text
	 *            the HTML
	 * 
	 * @return a Spanned with the handling of the HTML
	 */
	public static Spanned customizedFromHtml(String text) {

		if (text == null)
			return null;

		String temp = text.replaceAll("<strong>", "<b>");
		String reformatedText = temp.replaceAll("</strong>", "</b>");

		reformatedText = reformatedText.replaceAll("<h3>", "<h3><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h3>", "</font></h3>");

		reformatedText = reformatedText.replaceAll("<h2>", "<h2><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h2>", "</font></h2>");

		reformatedText = reformatedText.replaceAll("<h1>", "<h1><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h1>", "</font></h1>");

		reformatedText = reformatedText.replaceAll("<ul>", "");
		reformatedText = reformatedText.replaceAll("</ul>", "");

		reformatedText = reformatedText.replaceAll("<li>", "&#8226; ");
		reformatedText = reformatedText.replaceAll("</li>", "<br/>");

		return Html.fromHtml(reformatedText);

	}

	/**
	 * Replaces the strongs with b in the HTML
	 * 
	 * @param text
	 *            the HTML
	 * 
	 * @return a Spanned with the handling of the HTML
	 */
	public static Spanned customizedFromHtmlTab(String text) {

		if (text == null)
			return null;

		String temp = text.replaceAll("<strong>", "<b>");

		String reformatedText = text.replaceAll("<h3>", "<h3 style=\"font-weight: normal; \"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h3>", "</font></h3>");

		reformatedText = reformatedText.replaceAll("<h2>", "<h2 style=\"font-weight: normal;\"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h2>", "</font></h2>");

		reformatedText = reformatedText.replaceAll("<h1>", "<h1><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h1>", "</font></h1>");

		reformatedText = reformatedText.replaceAll("<ul>", "");
		reformatedText = reformatedText.replaceAll("</ul>", "");

		reformatedText = reformatedText.replaceAll("<li>", "&#8226; ");
		reformatedText = reformatedText.replaceAll("</li>", "<br/>");

		return Html.fromHtml(reformatedText);

	}

	/**
	 * Replaces the strongs with b in the HTML
	 * 
	 * @param text
	 *            the HTML
	 * 
	 * @return a Spanned with the handling of the HTML
	 */
	public static String customizedFromHtmlforWebView(String text) {

		if (text == null)
			return null;

		text = "<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>" + text + "</body></HTML>";
		String reformatedText = text.replaceAll("<h3>", "<h3><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h3>", "</font></h3>");
		reformatedText = reformatedText.replaceAll("<h2>", "<h2 style=\"font-weight: normal;\"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h2>", "</font></h2>");

		reformatedText = reformatedText.replaceAll("<h1>", "<h1><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h1>", "</font></h1>");

		reformatedText = reformatedText.replaceAll("<a href=", "<a style=\"color:#EF8608\" href=");
		// Log.w("reformatedText$$", reformatedText);
		return reformatedText;

	}

	/**
	 * Replaces the strongs with b in the HTML FOR TABLET
	 * 
	 * @param text
	 *            the HTML
	 * 
	 * @return a Spanned with the handling of the HTML
	 */
	public static String customizedFromHtmlforWebViewTab(String text) {

		if (text == null)
			return null;

		text = "<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body style=color:#333333>" + text + "</body></HTML>";
		String reformatedText = text.replaceAll("<h3>", "<h3 style=\"font-weight: normal;font-size: 16; \"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h3>", "</font></h3>");

		reformatedText = reformatedText.replaceAll("<strong>", "<b>");
		reformatedText = reformatedText.replaceAll("</strong>", "</b>");

		reformatedText = reformatedText.replaceAll("<p>", "<p><font face='sans'>");
		reformatedText = reformatedText.replaceAll("</p>", "</font></p>");

		reformatedText = reformatedText.replaceAll("<h2>", "<h2 style=\"font-weight: normal;\"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h2>", "</font></h2>");
//		reformatedText = text.replaceAll("<h3>", "<h3 style=\"font-weight: normal;font-size=160px;\"><font face='serif'>");

		reformatedText = reformatedText.replaceAll("<h1>", "<h1><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h1>", "</font></h1>");

		reformatedText = reformatedText.replaceAll("<a href=", "<a style=\"color:#EF8608\" href=");
		// Log.w("reformatedText$$", reformatedText);
		return reformatedText;

	}
	public static String customizedFromHtmlforWebViewTabInfo(String text) {

		if (text == null)
			return null;

		text = "<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body style=color:#333333>" + text + "</body></HTML>";
		String reformatedText = text.replaceAll("<h3>", "<h3 style=\"font-weight: normal;font-size: 16; \"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h3>", "</font></h3>");
		
		reformatedText = reformatedText.replaceAll("<strong>", "<b>");
		reformatedText = reformatedText.replaceAll("</strong>", "</b>");

		reformatedText = reformatedText.replaceAll("<p class=\"voa_tab1 voa_abstand\">", "<p style=\"padding-left:18px \"><font face='sans'>");
		reformatedText = reformatedText.replaceAll("<p class=\"voa_tab1\">", "<p style=\"padding-left:18px \"><font face='sans'>");
		reformatedText = reformatedText.replaceAll("<p>", "<p style=\"padding-left:18px \"><font face='sans'>");
		reformatedText = reformatedText.replaceAll("</p>", "</font></p>");

		reformatedText = reformatedText.replaceAll("<h2>", "<h2 style=\"font-weight: normal;\"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h2>", "</font></h2>");
//		reformatedText = text.replaceAll("<h3>", "<h3 style=\"font-weight: normal;font-size=160px;\"><font face='serif'>");

		reformatedText = reformatedText.replaceAll("<h1>", "<h1><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h1>", "</font></h1>");

		reformatedText = reformatedText.replaceAll("<a href=", "<a style=\"color:#EF8608\" href=");
		// Log.w("reformatedText$$", reformatedText);
		return reformatedText;

	}
	public static String customizedFromHtmlforTextViewTab(String text) {

		if (text == null)
			return null;

		text = "<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body style=color:#333333>" + text + "</body></HTML>";

		String reformatedText = text.replaceAll("<h3>", "<h3 style=\"font-weight: normal;\"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h3>", "</font></h3>");

		reformatedText = reformatedText.replaceAll("<strong>", "<b>");
		reformatedText = reformatedText.replaceAll("</strong>", "</b>");

		reformatedText = reformatedText.replaceAll("<p>", "<p><font face='sans'>");
		reformatedText = reformatedText.replaceAll("</p>", "</font></p>");

		reformatedText = reformatedText.replaceAll("<h2>", "<h2 style=\"font-weight: normal;\"><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h2>", "</font></h2>");

		reformatedText = reformatedText.replaceAll("<h1>", "<h1><font face='serif'>");
		reformatedText = reformatedText.replaceAll("</h1>", "</font></h1>");

		reformatedText = reformatedText.replaceAll("<a href=", "<a style=\"color:#EF8608\" href=");
		// Log.w("reformatedText$$", reformatedText);
		return reformatedText;

	}

	/**
	 * Deals with the presence of tables in the news text
	 * 
	 * @param detailsObject
	 * @param activity
	 */
	public static void textViewHTMLTest(final Object detailsObject, final FragmentActivity activity) {

		// The original text with the html tags
		String text = "";

		if (detailsObject instanceof VisitorsArticleObject) {
			text = ((VisitorsArticleObject) detailsObject).getText();
		} else if (detailsObject instanceof NewsDetailsObject) {
			text = ((NewsDetailsObject) detailsObject).getText();
		} else if (detailsObject instanceof CommitteesDetailsNewsDetailsObject) {
			text = ((CommitteesDetailsNewsDetailsObject) detailsObject).getText();
		} else if (detailsObject instanceof PlenumObject) {
			text = ((PlenumObject) detailsObject).getText();
		}

		if (text.contains("<table")) {
			// when there is a table present
			String[] textBeforeTable = text.split("<table");

			// Text before table
			TextView tvTextBeforeTable = (TextView) activity.findViewById(R.id.text);
			tvTextBeforeTable.setText(TextHelper.customizedFromHtml(textBeforeTable[0]));

			for (int i = 1; i < textBeforeTable.length; i++) {
				// creates tables
				createTables(textBeforeTable[i], activity);
				// check if there is text after the table
				String[] textAfterTable = textBeforeTable[i].split("/table>");
				if (textAfterTable.length > 1) {
					LinearLayout layout = (LinearLayout) activity.findViewById(R.id.layout);
					TextView tvTextAfterTable = new TextView(activity.getApplicationContext());
					tvTextAfterTable.setTextColor(Color.BLACK);
					tvTextAfterTable.setText(TextHelper.customizedFromHtml(textAfterTable[1]));
					tvTextAfterTable.setTextSize(12);
					tvTextAfterTable.setPadding(10, 5, 10, 5);
					layout.addView(tvTextAfterTable);
				}
			}
		} else {
			// If no table is present proceed has usual
			TextView tvTestAnzeige = (TextView) activity.findViewById(R.id.text);
			tvTestAnzeige.setText(TextHelper.customizedFromHtml(text));
		}
	}

	public static void createTables(String textBeforeTable, final FragmentActivity activity) {

		LinearLayout layout = (LinearLayout) activity.findViewById(R.id.layout);

		// create table dynamically
		TableLayout tlMain = new TableLayout(activity.getApplicationContext());
		tlMain.setPadding(10, 0, 10, 0);
		layout.addView(tlMain);

		String[] trs = textBeforeTable.split("</tr>\n<tr>");

		// Check TH Element
		String firstTag = "th";

		if (!trs[0].contains("</" + firstTag + ">")) {
			firstTag = "td";
		}

		// Split row to see how many columns there are
		String[] ths = trs[0].split("</" + firstTag + "><" + firstTag + ">");

		TableRow trTh = createHeaderRow(ths, firstTag, activity);

		/* Add row to TableLayout. */
		tlMain.addView(trTh, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		// for each body row
		for (int i = 1; i < trs.length; i++) {

			String[] tds = trs[i].split("</td>\n<td>");

			TableRow trTd = createRow(tds, activity);

			/* Add row to TableLayout. */
			tlMain.addView(trTd, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}

	}

	public static TableRow createHeaderRow(String[] ths, String firstTag, final FragmentActivity activity) {

		/* Create a new row to be added. */
		TableRow trTh = new TableRow(activity.getApplicationContext());
		trTh.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		String[] th1 = ths[0].split("<" + firstTag + ">");
		String firstThColumn = "<b>" + th1[1].trim() + "</b>";

		TextView tvFirstThCol = new TextView(activity.getApplicationContext());
		tvFirstThCol.setTextColor(Color.BLACK);
		tvFirstThCol.setText(TextHelper.customizedFromHtml(firstThColumn));
		tvFirstThCol.setPadding(1, 1, 10, 1);
		trTh.addView(tvFirstThCol);

		for (int i = 1; i < ths.length; i++) {
			String[] th = ths[i].split("</" + firstTag + ">");
			String secondThColumn = "<b>" + th[0].trim() + "</b>";
			TextView tvThCol = new TextView(activity.getApplicationContext());
			tvThCol.setTextColor(Color.BLACK);
			tvThCol.setText(TextHelper.customizedFromHtml(secondThColumn));
			tvThCol.setPadding(10, 1, 1, 1);
			trTh.addView(tvThCol);
		}

		return trTh;
	}

	public static TableRow createRow(String[] tds, final FragmentActivity activity) {

		/* Create a new row to be added. */
		TableRow trTd = new TableRow(activity.getApplicationContext());
		trTd.setPadding(0, 0, 0, 2);
		trTd.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		String[] td1 = tds[0].split("<td>");
		String firstTdColumn = td1[1].trim();
		TextView tvFirstTdCol = new TextView(activity.getApplicationContext());
		tvFirstTdCol.setTextColor(Color.BLACK);
		tvFirstTdCol.setTextSize(12);
		tvFirstTdCol.setText(TextHelper.customizedFromHtml(firstTdColumn));
		tvFirstTdCol.setPadding(1, 1, 10, 1);
		trTd.addView(tvFirstTdCol);

		for (int i = 1; i < tds.length; i++) {
			String[] td = tds[i].split("</td>");
			String tdColumn = td[0].trim();
			TextView tvTdCol = new TextView(activity.getApplicationContext());
			tvTdCol.setTextColor(Color.BLACK);
			tvTdCol.setText(TextHelper.customizedFromHtml(tdColumn));
			tvTdCol.setTextSize(12);
			tvTdCol.setPadding(10, 1, 1, 1);
			trTd.addView(tvTdCol);
		}

		return trTd;
	}

}
