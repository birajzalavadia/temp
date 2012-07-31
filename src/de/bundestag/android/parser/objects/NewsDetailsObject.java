package de.bundestag.android.parser.objects;

import java.util.Date;

import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.helpers.TextHelper;

/**
 * News details object holder.
 * 
 * Contains the news details (Aktuell) data.
 */
public class NewsDetailsObject implements Comparable<NewsDetailsObject> {
	private Date date;

	private String title;

	private String imageURL;

	private String imageGrossURL;

	private String videoURL;

	private String statusTxt;

	private String startTeaser;

	private String imageCopyright;

	private Date imageLastChanged;

	private Date imageChangedDateTime;

	private String imageString;

	private String text;

	private String listId;

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getDate() {
		return DateHelper.getDateAsString(date);
	}

	public void setDate(String date) {
		this.date = DateHelper.parseDate(date);
	}

	public String getTitle() {
		return TextHelper.checkNull(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getImageGrossURL() {
		return imageGrossURL;
	}

	public void setImageGrossURL(String imageGrossURL) {
		this.imageGrossURL = imageGrossURL;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public String getStatusTxt() {
		return statusTxt;
	}

	public void setStatusTxt(String statusTxt) {
		this.statusTxt = statusTxt;
	}

	public String getStartTeaser() {
		return startTeaser;
	}

	public void setStartTeaser(String startTeaser) {
		this.startTeaser = startTeaser;
	}

	public String getImageCopyright() {
		return TextHelper.checkNull(imageCopyright);
	}

	public void setImageCopyright(String imageCopyright) {
		this.imageCopyright = imageCopyright;
	}

	public String getImageLastChanged() {
		return DateHelper.getDateAsString(imageLastChanged);
	}

	public void setImageLastChanged(String imageLastChanged) {
		this.imageLastChanged = DateHelper.parseDate(imageLastChanged);
	}

	public String getImageChangedDateTime() {
		return DateHelper.getDateTimeAsString(imageChangedDateTime);
	}

	public Date getImageChangedDateTimeDate() {
		return imageChangedDateTime;
	}

	public void setImageChangedDateTime(String imageChangedDateTime) {
		this.imageChangedDateTime = DateHelper.parseDate(imageChangedDateTime);
	}

	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public String getText() {
		return TextHelper.checkNull(text);
	}

	public void setText(String text) {
		this.text = text;
	}

	public NewsDetailsObject copy() {
		NewsDetailsObject copy = new NewsDetailsObject();

		copy.date = date;
		copy.title = title;
		copy.imageURL = imageURL;
		copy.imageGrossURL = imageGrossURL;
		copy.imageCopyright = imageCopyright;
		copy.imageLastChanged = imageLastChanged;
		copy.imageChangedDateTime = imageChangedDateTime;
		copy.imageString = imageString;
		copy.text = text;

		return copy;
	}

	@Override
	public int compareTo(NewsDetailsObject another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}
}