package de.bundestag.android.parser.objects;

import java.util.Date;
import java.util.List;

import de.bundestag.android.helpers.DateHelper;
import de.bundestag.android.helpers.TextHelper;

/**
 * Members object holder.
 * 
 * Contains the members (MdB) data.
 */
public class MembersDetailsObject implements Comparable<MembersDetailsObject>
{
    private String id;
    private String status;
    private Date exitDate;
    private String lastName;
    private String firstName;
    private String title;
    private String course;
    private String local;
    private Date birthdate;
    private String religion;
    private String degree;
    private String higherEducation;
    private String profession;
    private String professionField;
    private String sex;
    private String maritalStatus;
    private Integer children;
    private String fraction;
    private String party;
    private String city;
    private String elected;
    private String bioURL;
    private String information;
    private String more;
    private String homepage;
    private String phone;
    private String disclosureInformation;

    // media
    private String mediaPhoto;
    private String mediaPhotoImageString;
    private String mediaPhotoCopyright;
    private String speakerURL;
    private String speakerRSS;

    // election place
    private String electionNumber;
    private String electionName;
    private String electionURL;
    private String electionCity;
    
    private List<MembersDetailsWebsitesObject> websites;

    private List<MembersDetailsGroupsObject> presidentGroups;
    private List<MembersDetailsGroupsObject> subPresidentGroups;
    private List<MembersDetailsGroupsObject> coordinateGroups;
    private List<MembersDetailsGroupsObject> fullMemberGroups;
    private List<MembersDetailsGroupsObject> deputyMemberGroups;
    private List<MembersDetailsGroupsObject> substituteMemberGroups;

    private List<MembersDetailsGroupsObject> otherPresidentGroups;
    private List<MembersDetailsGroupsObject> otherSubPresidentGroups;
    private List<MembersDetailsGroupsObject> otherCoordinateGroups;
    private List<MembersDetailsGroupsObject> otherFullMemberGroups;
    private List<MembersDetailsGroupsObject> otherDeputyMemberGroups;
    private List<MembersDetailsGroupsObject> otherSubstituteMemberGroups;

	public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getExitDate()
    {
        return DateHelper.getDateAsString(exitDate);
    }

    public void setExitDate(String exitDate)
    {
        this.exitDate = DateHelper.parseDate(exitDate);
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getTitle()
    {
        return TextHelper.checkNull(title);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCourse()
    {
        return TextHelper.checkNull(course);
    }

    public void setCourse(String course)
    {
        this.course = course;
    }

    public String getLocal()
    {
        return local;
    }

    public void setLocal(String local)
    {
        this.local = local;
    }

    public String getBirthdate()
    {
        return DateHelper.getDateAsString(birthdate);
    }

    public void setBirthdate(String birthdate)
    {
        this.birthdate = DateHelper.parseDate(birthdate);
    }

    public String getReligion()
    {
        return religion;
    }

    public void setReligion(String religion)
    {
        this.religion = religion;
    }

    public String getDegree()
    {
        return degree;
    }

    public void setDegree(String degree)
    {
        this.degree = degree;
    }

    public String getHigherEducation()
    {
        return higherEducation;
    }

    public void setHigherEducation(String higherEducation)
    {
        this.higherEducation = higherEducation;
    }

    public String getProfession()
    {
        return TextHelper.checkNull(profession);
    }

    public void setProfession(String profession)
    {
        this.profession = profession;
    }

    public String getProfessionField()
    {
        return professionField;
    }

    public void setProfessionField(String professionField)
    {
        this.professionField = professionField;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getMaritalStatus()
    {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus)
    {
        this.maritalStatus = maritalStatus;
    }

    public Integer getChildren()
    {
        return children;
    }

    public void setChildren(String children)
    {
        if ((children != null) && (!children.equals("")))
        {
            this.children = Integer.parseInt(children);
        }
    }

    public String getFraction()
    {
        return TextHelper.checkNull(fraction);
    }

    public void setFraction(String fraction)
    {
        this.fraction = fraction;
    }

    public String getParty()
    {
        return party;
    }

    public void setParty(String party)
    {
        this.party = party;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getElected()
    {
        return elected;
    }

    public void setElected(String elected)
    {
        this.elected = elected;
    }

    public String getBioURL()
    {
        return bioURL;
    }

    public void setBioURL(String bioURL)
    {
        this.bioURL = bioURL;
    }

    public String getInformation()
    {
        return TextHelper.checkNull(information);
    }

    public void setInformation(String information)
    {
        this.information = information;
    }

    public String getMore()
    {
        return more;
    }

    public void setMore(String more)
    {
        this.more = more;
    }

    public String getHomepage()
    {
        return homepage;
    }

    public void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getDisclosureInformation()
    {
        return TextHelper.checkNull(disclosureInformation);
    }

    public void setDisclosureInformation(String disclosureInformation)
    {
        this.disclosureInformation = disclosureInformation;
    }

    public String getMediaPhoto()
    {
        return mediaPhoto;
    }

    public void setMediaPhoto(String mediaPhoto)
    {
        this.mediaPhoto = mediaPhoto;
    }

    public String getMediaPhotoImageString()
    {
        return mediaPhotoImageString;
    }

    public void setMediaPhotoImageString(String mediaPhotoImageString)
    {
        this.mediaPhotoImageString = mediaPhotoImageString;
    }

    public String getMediaPhotoCopyright()
    {
        return TextHelper.checkNull(mediaPhotoCopyright);
    }

    public void setMediaPhotoCopyright(String mediaPhotoCopyright)
    {
        this.mediaPhotoCopyright = mediaPhotoCopyright;
    }

    public String getSpeakerURL()
    {
        return speakerURL;
    }

    public void setSpeakerURL(String speakerURL)
    {
        this.speakerURL = speakerURL;
    }

    public String getSpeakerRSS()
    {
        return speakerRSS;
    }

    public void setSpeakerRSS(String speakerRSS)
    {
        this.speakerRSS = speakerRSS;
    }

    public String getElectionNumber()
    {
        return electionNumber;
    }

    public void setElectionNumber(String electionNumber)
    {
        this.electionNumber = electionNumber;
    }

    public String getElectionName()
    {
        return electionName;
    }

    public void setElectionName(String electionName)
    {
        this.electionName = electionName;
    }

    public String getElectionURL()
    {
        return electionURL;
    }

    public void setElectionURL(String electionURL)
    {
        this.electionURL = electionURL;
    }

    public String getElectionCity()
    {
        return electionCity;
    }

    public void setElectionCity(String electionCity)
    {
        this.electionCity = electionCity;
    }

    public List<MembersDetailsWebsitesObject> getWebsites()
    {
        return websites;
    }

    public void setWebsites(List<MembersDetailsWebsitesObject> websites)
    {
        this.websites = websites;
    }

    public List<MembersDetailsGroupsObject> getPresidentGroups()
    {
        return presidentGroups;
    }

    public void setPresidentGroups(List<MembersDetailsGroupsObject> presidentGroups)
    {
        this.presidentGroups = presidentGroups;
    }

    public List<MembersDetailsGroupsObject> getSubPresidentGroups()
    {
        return subPresidentGroups;
    }

    public void setSubPresidentGroups(List<MembersDetailsGroupsObject> subPresidentGroups)
    {
        this.subPresidentGroups = subPresidentGroups;
    }

    public List<MembersDetailsGroupsObject> getCoordinateGroups()
    {
        return coordinateGroups;
    }

    public void setCoordinateGroups(List<MembersDetailsGroupsObject> coordinateGroups)
    {
        this.coordinateGroups = coordinateGroups;
    }

    public List<MembersDetailsGroupsObject> getFullMemberGroups()
    {
        return fullMemberGroups;
    }

    public void setFullMemberGroups(List<MembersDetailsGroupsObject> fullMemberGroups)
    {
        this.fullMemberGroups = fullMemberGroups;
    }

    public List<MembersDetailsGroupsObject> getDeputyMemberGroups()
    {
        return deputyMemberGroups;
    }

    public void setDeputyMemberGroups(List<MembersDetailsGroupsObject> deputyMemberGroups)
    {
        this.deputyMemberGroups = deputyMemberGroups;
    }

    public List<MembersDetailsGroupsObject> getSubstituteMemberGroups()
    {
        return substituteMemberGroups;
    }

    public void setSubstituteMemberGroups(List<MembersDetailsGroupsObject> substituteMemberGroups)
    {
        this.substituteMemberGroups = substituteMemberGroups;
    }

    public List<MembersDetailsGroupsObject> getOtherPresidentGroups()
    {
        return otherPresidentGroups;
    }

    public void setOtherPresidentGroups(List<MembersDetailsGroupsObject> otherPresidentGroups)
    {
        this.otherPresidentGroups = otherPresidentGroups;
    }

    public List<MembersDetailsGroupsObject> getOtherSubPresidentGroups()
    {
        return otherSubPresidentGroups;
    }

    public void setOtherSubPresidentGroups(List<MembersDetailsGroupsObject> otherSubPresidentGroups)
    {
        this.otherSubPresidentGroups = otherSubPresidentGroups;
    }

    public List<MembersDetailsGroupsObject> getOtherCoordinateGroups()
    {
        return otherCoordinateGroups;
    }

    public void setOtherCoordinateGroups(List<MembersDetailsGroupsObject> otherCoordinateGroups)
    {
        this.otherCoordinateGroups = otherCoordinateGroups;
    }

    public List<MembersDetailsGroupsObject> getOtherFullMemberGroups()
    {
        return otherFullMemberGroups;
    }

    public void setOtherFullMemberGroups(List<MembersDetailsGroupsObject> otherFullMemberGroups)
    {
        this.otherFullMemberGroups = otherFullMemberGroups;
    }

    public List<MembersDetailsGroupsObject> getOtherDeputyMemberGroups()
    {
        return otherDeputyMemberGroups;
    }

    public void setOtherDeputyMemberGroups(List<MembersDetailsGroupsObject> otherDeputyMemberGroups)
    {
        this.otherDeputyMemberGroups = otherDeputyMemberGroups;
    }

    public List<MembersDetailsGroupsObject> getOtherSubstituteMemberGroups()
    {
        return otherSubstituteMemberGroups;
    }

    public void setOtherSubstituteMemberGroups(List<MembersDetailsGroupsObject> otherSubstituteMemberGroups)
    {
        this.otherSubstituteMemberGroups = otherSubstituteMemberGroups;
    }

    public MembersDetailsObject copy()
	{
		MembersDetailsObject copy = new MembersDetailsObject();

        copy.id = id;
        copy.status = status;
        copy.exitDate = exitDate;
        copy.lastName = lastName;
        copy.firstName = firstName;
        copy.title = title;
        copy.course = course;
        copy.local = local;
        copy.birthdate = birthdate;
        copy.religion = religion;
        copy.degree = degree;
        copy.higherEducation = higherEducation;
        copy.profession = profession;
        copy.professionField = professionField;
        copy.sex = sex;
        copy.maritalStatus = maritalStatus;
        copy.children = children;
        copy.fraction = fraction;
        copy.party = party;
        copy.city = city;
        copy.elected = elected;
        copy.bioURL = bioURL;
        copy.information = information;
        copy.more = more;
        copy.homepage = homepage;
        copy.phone = phone;
        copy.disclosureInformation = disclosureInformation;

        copy.mediaPhoto = mediaPhoto;
        copy.mediaPhotoImageString = mediaPhotoImageString;
        copy.mediaPhotoCopyright = mediaPhotoCopyright;
        copy.speakerURL = speakerURL;
        copy.speakerRSS = speakerRSS;

        copy.electionNumber = electionNumber;
        copy.electionName = electionName;
        copy.electionURL = electionURL;
        copy.electionCity = electionCity;

        copy.websites = websites;

        copy.presidentGroups = presidentGroups;
        copy.subPresidentGroups = subPresidentGroups;
        copy.coordinateGroups = coordinateGroups;
        copy.fullMemberGroups = fullMemberGroups;
        copy.deputyMemberGroups = deputyMemberGroups;
        copy.substituteMemberGroups = substituteMemberGroups;

        copy.otherPresidentGroups = otherPresidentGroups;
        copy.otherSubPresidentGroups = otherSubPresidentGroups;
        copy.otherCoordinateGroups = otherCoordinateGroups;
        copy.otherFullMemberGroups = otherFullMemberGroups;
        copy.otherDeputyMemberGroups = otherDeputyMemberGroups;
        copy.otherSubstituteMemberGroups = otherSubstituteMemberGroups;

        return copy;
	}

	@Override
	public int compareTo(MembersDetailsObject another)
	{
		if (another == null) return 1;

        return another.lastName.compareTo(lastName);
    }
}