package de.bundestag.android.parser.objects;

import java.util.List;

public class PlenumTvObject {

	private String dispatchDate;
	private String title;
	private String longTitle;
	private String info;
	private String recordingDate;
	private String link;
	private List<PlenumTvObject> tv;
	
	public List<PlenumTvObject> getTv() {
		return tv;
	}

	public void setTv(List<PlenumTvObject> tv) {
		this.tv = tv;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getRecordingDate() {
		return recordingDate;
	}

	public void setRecordingDate(String recordingDate) {
		this.recordingDate = recordingDate;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getLongTitle() {
		return longTitle;
	}

	public void setLongTitle(String longTitle) {
		this.longTitle = longTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(String dispatchDate) {
		this.dispatchDate = dispatchDate;
	}
	
	 public PlenumTvObject copy()
		{
		 PlenumTvObject copy = new PlenumTvObject();

	        copy.dispatchDate = dispatchDate;
			copy.title = title;
	        copy.longTitle = longTitle;
	        copy.info = info;
	        copy.link = link;
	        copy.recordingDate=recordingDate;

			return copy;
		}
}
