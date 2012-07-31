package de.bundestag.android.parser.objects;


/**
 * Plenum stream object holder.
 * 
 * Contains the plenum stream (Plenum) data.
 */
public class PlenumStreamObject
{
    private String streamURL;
    private String videoStreamURL;
    private String videoStreamSource;
    private String videoStreamImage;
    private String videoStreamTitle;
    private String videoStreamDescription;

    public String getStreamURL()
    {
        return streamURL;
    }

    public void setStreamURL(String streamURL)
    {
        this.streamURL = streamURL;
    }

    public String getVideoStreamURL()
    {
        return videoStreamURL;
    }

    public void setVideoStreamURL(String videoStreamURL)
    {
        this.videoStreamURL = videoStreamURL;
    }

    public String getVideoStreamSource()
    {
        return videoStreamSource;
    }

    public void setVideoStreamSource(String videoStreamSource)
    {
        this.videoStreamSource = videoStreamSource;
    }

    public String getVideoStreamImage()
    {
        return videoStreamImage;
    }

    public void setVideoStreamImage(String videoStreamImage)
    {
        this.videoStreamImage = videoStreamImage;
    }

    public String getVideoStreamTitle()
    {
        return videoStreamTitle;
    }

    public void setVideoStreamTitle(String videoStreamTitle)
    {
        this.videoStreamTitle = videoStreamTitle;
    }

    public String getVideoStreamDescription()
    {
        return videoStreamDescription;
    }

    public void setVideoStreamDescription(String videoStreamDescription)
    {
        this.videoStreamDescription = videoStreamDescription;
    }

    public PlenumStreamObject copy()
	{
        PlenumStreamObject copy = new PlenumStreamObject();

        copy.streamURL = streamURL;
        copy.videoStreamURL = videoStreamURL;
        copy.videoStreamSource = videoStreamSource;
        copy.videoStreamImage = videoStreamImage;
        copy.videoStreamTitle = videoStreamTitle;
        copy.videoStreamDescription = videoStreamDescription;

		return copy;
	}
}