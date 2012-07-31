package de.bundestag.android.parser.objects;


/**
 * Plenum speaker item object holder.
 * 
 * Contains the plenum speaker item (Plenum) data.
 */
public class PlenumSpeakerItemObject
{
    private String topic;
    private String startTime;
    private String state;
    private String function;
    private String name;

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getFunction()
    {
        return function;
    }

    public void setFunction(String function)
    {
        this.function = function;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public PlenumSpeakerItemObject copy()
	{
        PlenumSpeakerItemObject copy = new PlenumSpeakerItemObject();

        copy.topic = topic;
		copy.startTime = startTime;
        copy.state = state;
        copy.function = function;
        copy.name = name;

		return copy;
	}
}