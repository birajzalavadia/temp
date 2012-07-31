package de.bundestag.android.parser.objects;


/**
 * Plenum stream source object holder.
 * 
 * Contains the plenum stream source (Plenum) data.
 */
public class PlenumStreamSourceObject
{
   
	private String bandwidth;
	private String href;
	private String type;
	
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public PlenumStreamSourceObject copy(){
		PlenumStreamSourceObject p = new PlenumStreamSourceObject();
		p.setBandwidth(this.bandwidth);
		p.setHref(this.href);
		p.setType(this.type);
		return p;
	}
	
}