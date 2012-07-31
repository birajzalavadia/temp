package de.bundestag.android.parser.objects;

import java.util.List;

/**
 * Plenum speaker object holder.
 * 
 * Contains the plenum speaker (Plenum) data.
 */
public class PlenumSpeakerObject
{
    private String topicNumber;
    private String live;
    private List<PlenumSpeakerItemObject> speakers;

    public String getTopicNumber()
    {
        return topicNumber;
    }

    public void setTopicNumber(String topicNumber)
    {
        this.topicNumber = topicNumber;
    }

    public String getLive()
    {
        return live;
    }

    public void setLive(String live)
    {
        this.live = live;
    }

    public List<PlenumSpeakerItemObject> getSpeakers()
    {
        return speakers;
    }

    public void setSpeakers(List<PlenumSpeakerItemObject> speakers)
    {
        this.speakers = speakers;
    }

    public PlenumSpeakerObject copy()
	{
        PlenumSpeakerObject copy = new PlenumSpeakerObject();

        copy.topicNumber = topicNumber;
		copy.live = live;
		copy.speakers = speakers;

		return copy;
	}
}