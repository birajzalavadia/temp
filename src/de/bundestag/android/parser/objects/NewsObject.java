package de.bundestag.android.parser.objects;

import java.util.Date;

import android.graphics.Bitmap;
import de.bundestag.android.helpers.DateHelper;

/**
 * News object holder.
 * 
 * Contains the news (Aktuell) data.
 * 
 * Also contains the news details object inside.
 */
public class NewsObject implements Comparable<NewsObject> {
	private Long id;

	private boolean isList;

	private Long listId;

	private int type;

	private String startteaser;

	private String status;

	private Date date;

	private String title;

	private String teaser;

	private String imageURL;

	private Bitmap imageBitmap;

	private String imageCopyright;

	private String imageString;

	private String detailsXMLURL;

	private String videoURL;

	private String videoStreamURL;

	private Date lastChanged;

	private Date changedDateTime;

	private NewsDetailsObject newsDetails;

	private NewsListObject newsList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getIsList() {
		return isList;
	}

	public void setIstList(boolean isList) {
		this.isList = isList;
	}

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setStartteaser(String startteaser) {
		this.startteaser = startteaser;
	}

	public String getStartteaser() {
		return startteaser;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getDate() {
		return DateHelper.getDateAsString(date);
	}

	public void setDate(String date) {
		this.date = DateHelper.parseDate(date);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeaser() {
		return teaser;
	}

	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	public String getImageCopyright() {
		return imageCopyright;
	}

	public void setImageCopyright(String imageCopyright) {
		this.imageCopyright = imageCopyright;
	}

	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public String getDetailsXMLURL() {
		return detailsXMLURL;
	}

	public void setDetailsXMLURL(String detailsXMLURL) {
		this.detailsXMLURL = detailsXMLURL;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public String getVideoStreamURL() {
		return videoStreamURL;
	}

	public void setVideoStreamURL(String videoStreamURL) {
		this.videoStreamURL = videoStreamURL;
	}

	public String getLastChanged() {
		return DateHelper.getDateAsString(lastChanged);
	}

	public void setLastChanged(String lastChanged) {
		this.lastChanged = DateHelper.parseDate(lastChanged);
	}

	public String getChangedDateTime() {
		return DateHelper.getDateTimeAsString(changedDateTime);
	}

	public Date getChangedDateTimeDate() {
		return changedDateTime;
	}

	public void setChangedDateTime(String changedDateTime) {
		this.changedDateTime = DateHelper.parseDate(changedDateTime);
	}

	public NewsDetailsObject getNewsDetails() {
		return newsDetails;
	}

	public void setNewsDetails(NewsDetailsObject newsDetails) {
		this.newsDetails = newsDetails;
	}

	public NewsListObject getNewsList() {
		return newsList;
	}

	public void setNewsList(NewsListObject newsList) {
		this.newsList = newsList;
	}

	public NewsObject copy() {
		NewsObject copy = new NewsObject();

		copy.id = id;
		copy.newsList = newsList;
		copy.isList = isList;
		copy.listId = listId;
		copy.type = type;
		copy.startteaser = startteaser;
		copy.status = status;
		copy.date = date;
		copy.title = title;
		copy.teaser = teaser;
		copy.imageURL = imageURL;
		copy.imageBitmap = imageBitmap;
		copy.imageCopyright = imageCopyright;
		copy.imageString = imageString;
		copy.detailsXMLURL = detailsXMLURL;
		copy.videoURL = videoURL;
		copy.videoStreamURL = videoStreamURL;
		copy.lastChanged = lastChanged;
		copy.changedDateTime = changedDateTime;

		return copy;
	}

	@Override
	public int compareTo(NewsObject another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}