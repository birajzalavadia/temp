package de.bundestag.android.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import de.bundestag.android.BaseActivity;
import de.bundestag.android.helpers.ParseEncodingDetector;
import de.bundestag.android.parser.objects.MembersDetailsGroupsObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.MembersDetailsWebsitesObject;
import de.bundestag.android.parser.objects.MembersObject;

/**
 * Members XML parser.
 * 
 * This class knows how to parse members (MdB) from Bundestag.
 */
public class MembersXMLParser extends BaseXMLParser
{
	private static final String MEMBERS_URL = "xml/mdb/index.xml";

	private static final String TAG_MEMBERS_ROOT_ELEMENT = "mdbUebersicht";
	private static final String TAG_MEMBERS_LIST = "mdbs";
    private static final String TAG_MEMBERS_ITEM = "mdb";
    private static final String TAG_MEMBERS_ID = "mdbID";
    private static final String TAG_MEMBERS_NAME = "mdbName";
    private static final String TAG_MEMBERS_BIOURL = "mdbBioURL";
    private static final String TAG_MEMBERS_INFOURL = "mdbInfoXMLURL";
    private static final String TAG_MEMBERS_INFOURLMORE = "mdbInfoXMLURLMitmischen";
    private static final String TAG_MEMBERS_LAND = "mdbLand";
    private static final String TAG_MEMBERS_PHOTO = "mdbFotoURL";
    private static final String TAG_MEMBERS_PHOTOLASTCHANGED = "mdbFotoLastChanged";
    private static final String TAG_MEMBERS_PHOTOCHANGEDDATETIME = "mdbFotoChangedDateTime";
    private static final String TAG_MEMBERS_LASTCHANGED = "lastChanged";
    private static final String TAG_MEMBERS_CHANGEDDATETIME = "changedDateTime";

    // details
    private static final String TAG_MEMBERS_DETAILS_ROOT_ELEMENT = "mdb";
    private static final String TAG_MEMBERS_DETAILS_INFO_ELEMENT = "mdbInfo";
    private static final String TAG_MEMBERS_DETAILS_ID = "mdbID";
    private static final String ATTRIBUTE_MEMBERS_DETAILS_STATUS = "status";
    private static final String TAG_MEMBERS_DETAILS_EXITDATE = "mdbAustrittsdatum";
    private static final String TAG_MEMBERS_DETAILS_LASTNAME = "mdbZuname";
    private static final String TAG_MEMBERS_DETAILS_FIRSTNAME = "mdbVorname";
    private static final String TAG_MEMBERS_DETAILS_TITLE = "mdbAdelstitel";
    private static final String TAG_MEMBERS_DETAILS_COURSE = "mdbAkademischerTitel";
    private static final String TAG_MEMBERS_DETAILS_LOCAL = "mdbOrtszusatz";
    private static final String TAG_MEMBERS_DETAILS_BIRTHDATE = "mdbGeburtsdatum";
    private static final String TAG_MEMBERS_DETAILS_RELIGION = "mdbReligionKonfession";
    private static final String TAG_MEMBERS_DETAILS_DEGREE = "mdbSchulOderBerufsabschluss";
    private static final String TAG_MEMBERS_DETAILS_HIGHEREDUCATION = "mdbHochschulbildung";
    private static final String TAG_MEMBERS_DETAILS_PROFESSION = "mdbBeruf";
    private static final String ATTRIBUTE_MEMBERS_DETAILS_FIELD = "berufsfeld";
    private static final String TAG_MEMBERS_DETAILS_SEX = "mdbGeschlecht";
    private static final String TAG_MEMBERS_DETAILS_MARITALSTATUS = "mdbFamilienstand";
    private static final String TAG_MEMBERS_DETAILS_CHILDREN = "mdbAnzahlKinder";
    private static final String TAG_MEMBERS_DETAILS_FRACTION = "mdbFraktion";
    private static final String TAG_MEMBERS_DETAILS_PARTY = "mdbPartei";
    private static final String TAG_MEMBERS_DETAILS_CITY = "mdbLand";

    private static final String TAG_MEMBERS_DETAILS_ELECTION = "mdbWahlkreis";
    private static final String TAG_MEMBERS_DETAILS_ELECTIONNUMBER = "mdbWahlkreisNummer";
    private static final String TAG_MEMBERS_DETAILS_ELECTIONNAME = "mdbWahlkreisName";
    private static final String TAG_MEMBERS_DETAILS_ELECTIONURL = "mdbWahlkreisURL";
    
    private static final String TAG_MEMBERS_DETAILS_ELECTED = "mdbGewaehlt";
    private static final String TAG_MEMBERS_DETAILS_BIOURL = "mdbBioURL";
    private static final String TAG_MEMBERS_DETAILS_INFORMATION = "mdbBiografischeInformationen";
    private static final String TAG_MEMBERS_DETAILS_MORE = "mdbWissenswertes";
    private static final String TAG_MEMBERS_DETAILS_HOMEPAGEURL = "mdbHomepageURL";
    private static final String TAG_MEMBERS_DETAILS_PHONE = "mdbTelefon";
    private static final String TAG_MEMBERS_DETAILS_DISCLOSUREINFORMATION = "mdbVeroeffentlichungspflichtigeAngaben";

    private static final String TAG_MEMBERS_DETAILS_MEDIA_ELEMENT = "mdbMedien";
    private static final String TAG_MEMBERS_DETAILS_MEDIA_PHOTO_ELEMENT = "mdbFoto";
    private static final String TAG_MEMBERS_DETAILS_MEDIA_PHOTOURL = "mdbFotoURL";
    private static final String TAG_MEMBERS_DETAILS_MEDIA_PHOTOCOPYRIGHT = "mdbFotoCopyright";
    private static final String TAG_MEMBERS_DETAILS_MEDIA_SPEAKERURL = "mdbRedenVorPlenumURL";
    private static final String TAG_MEMBERS_DETAILS_MEDIA_SPEAKERRSS = "mdbRedenVorPlenumRSS";

    // groups
    private static final String TAG_MEMBERS_GROUPS_ROOT_ELEMENT = "mdbMitgliedschaften";
    
    private static final String TAG_MEMBERS_GROUPS_PRESIDENT_ELEMENT = "mdbVorsitzGremien";
    private static final String TAG_MEMBERS_GROUPS_PRESIDENT_ITEM_ELEMENT = "mdbVorsitzGremium";
    private static final String TAG_MEMBERS_GROUPS_SUB_PRESIDENT_ELEMENT = "mdbStellvertretenderVorsitzGremien";
    private static final String TAG_MEMBERS_GROUPS_SUB_PRESIDENT_ITEM_ELEMENT = "mdbStellvertretenderVorsitzGremium";
    private static final String TAG_MEMBERS_GROUPS_COORDINATE_ELEMENT = "mdbObleuteGremien";
    private static final String TAG_MEMBERS_GROUPS_COORDINATE_ITEM_ELEMENT = "mdbObleuteGremium";
    private static final String TAG_MEMBERS_GROUPS_FULLMEMBER_ELEMENT = "mdbOrdentlichesMitgliedGremien";
    private static final String TAG_MEMBERS_GROUPS_FULLMEMBER_ITEM_ELEMENT = "mdbOrdentlichesMitgliedGremium";
    private static final String TAG_MEMBERS_GROUPS_DEPUTYMEMBER_ELEMENT = "mdbStellvertretendesMitgliedGremien";
    private static final String TAG_MEMBERS_GROUPS_DEPUTYMEMBER_ITEM_ELEMENT = "mdbStellvertretendesMitgliedGremium";
    private static final String TAG_MEMBERS_GROUPS_SUBSTITUTEMEMBER_ELEMENT = "mdbBeratendesMitgliedGremien";
    private static final String TAG_MEMBERS_GROUPS_SUBSTITUTEMEMBER_ITME_ELEMENT = "mdbBeratendesMitgliedGremium";

    
    private static final String ATTRIBUTE_MEMBERS_GROUPS_TITLE = "title";
    private static final String ATTRIBUTE_MEMBERS_GROUPS_ID = "id";
    private static final String TAG_MEMBERS_GROUPS_NAME = "gremiumName";
    private static final String TAG_MEMBERS_GROUPS_URL = "gremiumURL";
    
    
 // groups

    private static final String TAG_OTHER_GROUPS_PRESIDENT_ELEMENT = "mdbVorsitzSonstigeGremien";
    private static final String TAG_OTHER_GROUPS_PRESIDENT_ITEM_ELEMENT = "mdbVorsitzSonstigeGremium";
    private static final String TAG_OTHER_GROUPS_SUB_PRESIDENT_ELEMENT = "mdbStellvVorsitzSonstigeGremien";
    private static final String TAG_OTHER_GROUPS_SUB_PRESIDENT_ITEM_ELEMENT = "mdbStellvVorsitzSonstigesGremium";
    private static final String TAG_OTHER_GROUPS_FULLMEMBER_ELEMENT = "mdbOrdentlichesMitgliedSonstigeGremien";
    private static final String TAG_OTHER_GROUPS_FULLMEMBER_ITEM_ELEMENT = "mdbOrdentlichesMitgliedSonstigesGremium";
//    private static final String TAG_OTHER_GROUPS_DEPUTYMEMBER_ELEMENT = "mdbStellvertretendesMitgliedSonstigeGremien";
//    private static final String TAG_OTHER_GROUPS_DEPUTYMEMBER_ITEM_ELEMENT = "mdbStellvertretendesMitgliedSonstigeGremium";
    private static final String TAG_OTHER_GROUPS_SUBSTITUTEMEMBER_ELEMENT = "mdbStellvertretendesMitgliedSonstigeGremien";
    private static final String TAG_OTHER_GROUPS_SUBSTITUTEMEMBER_ITEM_ELEMENT = "mdbStellvertretendesMitgliedSonstigesGremium";

    

    // websites
    private static final String TAG_MEMBERS_WEBSITE_ROOT_ELEMENT = "mdbSonstigeWebsites";
    private static final String TAG_MEMBERS_WEBSITE_ITEM = "mdbSonstigeWebsite";
    private static final String TAG_MEMBERS_WEBSITE_TITLE = "mdbSonstigeWebsiteTitel";
    private static final String TAG_MEMBERS_WEBSITE_URL = "mdbSonstigeWebsiteURL";

    public MembersXMLParser()
    {
    	super(MEMBERS_URL);
    }

    /**
     * Parse a members (MdB) object.
     */
    public List<MembersObject> parse(boolean insertMembersDetails)
    {
        final MembersObject currentMembersObject = new MembersObject();

        RootElement root = new RootElement(TAG_MEMBERS_ROOT_ELEMENT);

        final List<MembersObject> members = new ArrayList<MembersObject>();

        Element membersList = root.getChild(TAG_MEMBERS_LIST);

        Element item = membersList.getChild(TAG_MEMBERS_ITEM);
        item.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                members.add(currentMembersObject.copy());
            }
        });

        item.getChild(TAG_MEMBERS_ID).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setId(body);
            }
        });
        item.getChild(TAG_MEMBERS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setName(body);
            }
        });
        item.getChild(TAG_MEMBERS_BIOURL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setBioURL(body);
            }
        });
        item.getChild(TAG_MEMBERS_INFOURL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setInfoURL(body);
            }
        });
        item.getChild(TAG_MEMBERS_INFOURLMORE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setInfoURLMore(body);
            }
        });
        item.getChild(TAG_MEMBERS_LAND).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setLand(body);
            }
        });
        item.getChild(TAG_MEMBERS_PHOTO).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setPhoto(body);
            }
        });
        item.getChild(TAG_MEMBERS_PHOTOLASTCHANGED).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setPhotoLastChanged(body);
            }
        });
        item.getChild(TAG_MEMBERS_PHOTOCHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setPhotoChangedDateTime(body);
            }
        });
        item.getChild(TAG_MEMBERS_LASTCHANGED).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setLastChanged(body);
            }
        });
        item.getChild(TAG_MEMBERS_CHANGEDDATETIME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersObject.setChangedDateTime(body);
            }
        });

        try
        {
            Xml.Encoding encoding = ParseEncodingDetector.detectEncoding(getXMLURL());
//            Xml.parse(this.getInputStream(), ParseEncodingDetector.detectEncoding(getXMLURL()), root.getContentHandler());
            Xml.parse(this.getInputStream(), encoding, root.getContentHandler());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        if (insertMembersDetails)
        {
            // Fill in the member details
            int insertsSize = BaseActivity.isDebugOn() ? ((BaseActivity.DEBUG_SYNCHRONIZE_NUMBER > members.size()) ? members.size() : BaseActivity.DEBUG_SYNCHRONIZE_NUMBER) : members.size();
            for (int i = 0; i < insertsSize; i++)
            {
                MembersObject membersObject = (MembersObject) members.get(i);
                if ((membersObject.getInfoURL() != null) && (!membersObject.getInfoURL().equals("")))
                {
                    MembersDetailsObject membersDetailsObject;
                    try
                    {
                        membersDetailsObject = parseDetails(membersObject.getInfoURL());
                        membersObject.setMembersDetails(membersDetailsObject);
                    } catch (SAXException e)
                    {
//                        e.printStackTrace();
                    }
                }
            }
        }

        return members;
    }

    /**
     * Parse a members details (MdB) object.
     * @throws SAXException 
     * @throws IOException 
     */
    public MembersDetailsObject parseDetails(String detailsURL) throws SAXException
    {
        final MembersDetailsObject currentMembersDetailsObject = new MembersDetailsObject();

        RootElement root = new RootElement(TAG_MEMBERS_DETAILS_ROOT_ELEMENT);
        Element item = root.getChild(TAG_MEMBERS_DETAILS_INFO_ELEMENT);
        Element idItem = item.getChild(TAG_MEMBERS_DETAILS_ID);
        idItem.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersDetailsObject.setStatus(attributes.getValue(ATTRIBUTE_MEMBERS_DETAILS_STATUS));
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_ID).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setId(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_EXITDATE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setExitDate(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_LASTNAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setLastName(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_FIRSTNAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setFirstName(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_TITLE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setTitle(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_COURSE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setCourse(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_LOCAL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setLocal(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_BIRTHDATE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setBirthdate(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_RELIGION).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setReligion(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_DEGREE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setDegree(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_HIGHEREDUCATION).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setHigherEducation(body);
            }
        });
        Element professionItem = root.getChild(TAG_MEMBERS_DETAILS_PROFESSION);
        professionItem.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersDetailsObject.setProfessionField(attributes.getValue(ATTRIBUTE_MEMBERS_DETAILS_FIELD));
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_PROFESSION).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setProfession(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_SEX).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setSex(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_MARITALSTATUS).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setMaritalStatus(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_CHILDREN).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setChildren(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_FRACTION).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setFraction(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_PARTY).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setParty(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_CITY).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setCity(body);
            }
        });
        Element electionElement = item.getChild(TAG_MEMBERS_DETAILS_ELECTION);
        electionElement.getChild(TAG_MEMBERS_DETAILS_ELECTIONNUMBER).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setElectionNumber(body);
            }
        });
        electionElement.getChild(TAG_MEMBERS_DETAILS_ELECTIONNAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setElectionName(body);
            }
        });
        electionElement.getChild(TAG_MEMBERS_DETAILS_ELECTIONURL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setElectionURL(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_ELECTED).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setElected(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_BIOURL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setBioURL(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_INFORMATION).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setInformation(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_MORE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setMore(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_HOMEPAGEURL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setHomepage(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_PHONE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setPhone(body);
            }
        });
        item.getChild(TAG_MEMBERS_DETAILS_DISCLOSUREINFORMATION).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setDisclosureInformation(body);
            }
        });

        // media
        Element mediaElement = root.getChild(TAG_MEMBERS_DETAILS_MEDIA_ELEMENT);
        Element mediaPhotoElement = mediaElement.getChild(TAG_MEMBERS_DETAILS_MEDIA_PHOTO_ELEMENT);
        mediaPhotoElement.getChild(TAG_MEMBERS_DETAILS_MEDIA_PHOTOURL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setMediaPhoto(body);
            }
        });
        mediaPhotoElement.getChild(TAG_MEMBERS_DETAILS_MEDIA_PHOTOCOPYRIGHT).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setMediaPhotoCopyright(body);
            }
        });
        mediaElement.getChild(TAG_MEMBERS_DETAILS_MEDIA_SPEAKERURL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setSpeakerURL(body);
            }
        });
        mediaElement.getChild(TAG_MEMBERS_DETAILS_MEDIA_SPEAKERRSS).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsObject.setSpeakerRSS(body);
            }
        });

        
        // member groups
        Element groupsElement = item.getChild(TAG_MEMBERS_GROUPS_ROOT_ELEMENT);

        
        /////////////////////////////////////////////////////////////////
        // parse president groups
        final MembersDetailsGroupsObject currentOtherPresidentGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> otherPresidentGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element presidentOtherGroupsElement = groupsElement.getChild(TAG_OTHER_GROUPS_PRESIDENT_ELEMENT);
        // NOTE - the group name is the same for each group item
        presidentOtherGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherPresidentGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element presidentOtherGroupsItemElement = presidentOtherGroupsElement.getChild(TAG_OTHER_GROUPS_PRESIDENT_ITEM_ELEMENT);
        presidentOtherGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherPresidentGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        presidentOtherGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
            	otherPresidentGroups.add(currentOtherPresidentGroupsObject.copy());
            }
        });
        presidentOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherPresidentGroupsObject.setGroupName(body);
            }
        });
        presidentOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherPresidentGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setOtherPresidentGroups(otherPresidentGroups);

        // parse sub presidents groups
        final MembersDetailsGroupsObject currentOtherSubPresidentGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> otherSubPresidentGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element subPresidentOtherSubGroupsElement = groupsElement.getChild(TAG_OTHER_GROUPS_SUB_PRESIDENT_ELEMENT);
        // NOTE - the group name is the same for each group item
        subPresidentOtherSubGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherSubPresidentGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element subPresidentOtherGroupsItemElement = subPresidentOtherSubGroupsElement.getChild(TAG_OTHER_GROUPS_SUB_PRESIDENT_ITEM_ELEMENT);
        subPresidentOtherGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherSubPresidentGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        subPresidentOtherGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
            	otherSubPresidentGroups.add(currentOtherSubPresidentGroupsObject.copy());
            }
        });
        subPresidentOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherSubPresidentGroupsObject.setGroupName(body);
            }
        });
        subPresidentOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherSubPresidentGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setOtherSubPresidentGroups(otherSubPresidentGroups);

       

        // parse full member groups
        final MembersDetailsGroupsObject currentOtherFullMemberGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> otherFullMemberGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element fullMemberOtherGroupsElement = groupsElement.getChild(TAG_OTHER_GROUPS_FULLMEMBER_ELEMENT);
        // NOTE - the group name is the same for each group item
        fullMemberOtherGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherFullMemberGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element fullMemberOtherGroupsItemElement = fullMemberOtherGroupsElement.getChild(TAG_OTHER_GROUPS_FULLMEMBER_ITEM_ELEMENT);
        fullMemberOtherGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherFullMemberGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        fullMemberOtherGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
            	otherFullMemberGroups.add(currentOtherFullMemberGroupsObject.copy());
            }
        });
        fullMemberOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherFullMemberGroupsObject.setGroupName(body);
            }
        });
        fullMemberOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherFullMemberGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setOtherFullMemberGroups(otherFullMemberGroups);


//        // parse deputy member groups
//        final MembersDetailsGroupsObject currentOtherDeputyGroupsObject = new MembersDetailsGroupsObject();
//        final List<MembersDetailsGroupsObject> otherDeputyGroups = new ArrayList<MembersDetailsGroupsObject>();
//
//        Element deputyOtherGroupsElement = groupsElement.getChild(TAG_OTHER_GROUPS_DEPUTYMEMBER_ELEMENT);
//        // NOTE - the group name is the same for each group item
//        deputyOtherGroupsElement.setStartElementListener(new StartElementListener()
//        {
//            @Override
//            public void start(Attributes attributes)
//            {
//            	currentOtherDeputyGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
//            }
//        });
//        Element deputyOtherGroupsItemElement = deputyOtherGroupsElement.getChild(TAG_OTHER_GROUPS_DEPUTYMEMBER_ITEM_ELEMENT);
//        deputyOtherGroupsItemElement.setStartElementListener(new StartElementListener()
//        {
//            @Override
//            public void start(Attributes attributes)
//            {
//            	currentOtherDeputyGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
//            }
//        });
//        deputyOtherGroupsItemElement.setEndElementListener(new EndElementListener()
//        {
//            public void end()
//            {
//            	otherDeputyGroups.add(currentOtherDeputyGroupsObject.copy());
//            }
//        });
//        deputyOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
//        {
//            public void end(String body)
//            {
//            	currentOtherDeputyGroupsObject.setGroupName(body);
//            }
//        });
//        deputyOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
//        {
//            public void end(String body)
//            {
//            	currentOtherDeputyGroupsObject.setGroupURL(body);
//            }
//        });
//        currentMembersDetailsObject.setOtherDeputyMemberGroups(otherDeputyGroups);


        // parse substitute member groups
        final MembersDetailsGroupsObject currentOtherSubstituteGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> otherSubstituteGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element substituteOtherGroupsElement = groupsElement.getChild(TAG_OTHER_GROUPS_SUBSTITUTEMEMBER_ELEMENT);
        // NOTE - the group name is the same for each group item
        substituteOtherGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherSubstituteGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element substituteOtherGroupsItemElement = substituteOtherGroupsElement.getChild(TAG_OTHER_GROUPS_SUBSTITUTEMEMBER_ITEM_ELEMENT);
        substituteOtherGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
            	currentOtherSubstituteGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        substituteOtherGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
            	otherSubstituteGroups.add(currentOtherSubstituteGroupsObject.copy());
            }
        });
        substituteOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherSubstituteGroupsObject.setGroupName(body);
            }
        });
        substituteOtherGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
            	currentOtherSubstituteGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setOtherSubstituteMemberGroups(otherSubstituteGroups);
        
        /////////////////////////////////////////////////////////////////
        
        
        
        // parse president groups
        final MembersDetailsGroupsObject currentMembersPresidentGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> memberPresidentGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element presidentGroupsElement = groupsElement.getChild(TAG_MEMBERS_GROUPS_PRESIDENT_ELEMENT);
        // NOTE - the group name is the same for each group item
        presidentGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersPresidentGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element presidentGroupsItemElement = presidentGroupsElement.getChild(TAG_MEMBERS_GROUPS_PRESIDENT_ITEM_ELEMENT);
        presidentGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersPresidentGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        presidentGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                memberPresidentGroups.add(currentMembersPresidentGroupsObject.copy());
            }
        });
        presidentGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersPresidentGroupsObject.setGroupName(body);
            }
        });
        presidentGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersPresidentGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setPresidentGroups(memberPresidentGroups);

        // parse sub presidents groups
        final MembersDetailsGroupsObject currentMembersSubPresidentGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> memberSubPresidentGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element subPresidentSubGroupsElement = groupsElement.getChild(TAG_MEMBERS_GROUPS_SUB_PRESIDENT_ELEMENT);
        // NOTE - the group name is the same for each group item
        subPresidentSubGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersSubPresidentGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element subPresidentGroupsItemElement = subPresidentSubGroupsElement.getChild(TAG_MEMBERS_GROUPS_SUB_PRESIDENT_ITEM_ELEMENT);
        subPresidentGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersSubPresidentGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        subPresidentGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                memberSubPresidentGroups.add(currentMembersSubPresidentGroupsObject.copy());
            }
        });
        subPresidentGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersSubPresidentGroupsObject.setGroupName(body);
            }
        });
        subPresidentGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersSubPresidentGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setSubPresidentGroups(memberSubPresidentGroups);

        // parse coordinate groups
        final MembersDetailsGroupsObject currentMembersCoordinateGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> memberCoordinateGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element coordinateGroupsElement = groupsElement.getChild(TAG_MEMBERS_GROUPS_COORDINATE_ELEMENT);
        // NOTE - the group name is the same for each group item
        coordinateGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersCoordinateGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element coordinateGroupsItemElement = coordinateGroupsElement.getChild(TAG_MEMBERS_GROUPS_COORDINATE_ITEM_ELEMENT);
        coordinateGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersCoordinateGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        coordinateGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                memberCoordinateGroups.add(currentMembersCoordinateGroupsObject.copy());
            }
        });
        coordinateGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersCoordinateGroupsObject.setGroupName(body);
            }
        });
        coordinateGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersCoordinateGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setCoordinateGroups(memberCoordinateGroups);

        // parse full member groups
        final MembersDetailsGroupsObject currentMembersFullMemberGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> memberFullMemberGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element fullMemberGroupsElement = groupsElement.getChild(TAG_MEMBERS_GROUPS_FULLMEMBER_ELEMENT);
        // NOTE - the group name is the same for each group item
        fullMemberGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersFullMemberGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element fullMemberGroupsItemElement = fullMemberGroupsElement.getChild(TAG_MEMBERS_GROUPS_FULLMEMBER_ITEM_ELEMENT);
        fullMemberGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersFullMemberGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        fullMemberGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                memberFullMemberGroups.add(currentMembersFullMemberGroupsObject.copy());
            }
        });
        fullMemberGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersFullMemberGroupsObject.setGroupName(body);
            }
        });
        fullMemberGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersFullMemberGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setFullMemberGroups(memberFullMemberGroups);


        // parse deputy member groups
        final MembersDetailsGroupsObject currentMembersDeputyGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> memberDeputyGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element deputyGroupsElement = groupsElement.getChild(TAG_MEMBERS_GROUPS_DEPUTYMEMBER_ELEMENT);
        // NOTE - the group name is the same for each group item
        deputyGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersDeputyGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element deputyGroupsItemElement = deputyGroupsElement.getChild(TAG_MEMBERS_GROUPS_DEPUTYMEMBER_ITEM_ELEMENT);
        deputyGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersDeputyGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        deputyGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                memberDeputyGroups.add(currentMembersDeputyGroupsObject.copy());
            }
        });
        deputyGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDeputyGroupsObject.setGroupName(body);
            }
        });
        deputyGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDeputyGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setDeputyMemberGroups(memberDeputyGroups);


        // parse substitute member groups
        final MembersDetailsGroupsObject currentMembersSubstituteGroupsObject = new MembersDetailsGroupsObject();
        final List<MembersDetailsGroupsObject> memberSubstituteGroups = new ArrayList<MembersDetailsGroupsObject>();

        Element substituteGroupsElement = groupsElement.getChild(TAG_MEMBERS_GROUPS_SUBSTITUTEMEMBER_ELEMENT);
        // NOTE - the group name is the same for each group item
        substituteGroupsElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersSubstituteGroupsObject.setGroupTitle(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_TITLE));
            }
        });
        Element substituteGroupsItemElement = substituteGroupsElement.getChild(TAG_MEMBERS_GROUPS_SUBSTITUTEMEMBER_ITME_ELEMENT);
        substituteGroupsItemElement.setStartElementListener(new StartElementListener()
        {
            @Override
            public void start(Attributes attributes)
            {
                currentMembersSubstituteGroupsObject.setGroupId(attributes.getValue(ATTRIBUTE_MEMBERS_GROUPS_ID));
            }
        });
        substituteGroupsItemElement.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                memberSubstituteGroups.add(currentMembersSubstituteGroupsObject.copy());
            }
        });
        substituteGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_NAME).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersSubstituteGroupsObject.setGroupName(body);
            }
        });
        substituteGroupsItemElement.getChild(TAG_MEMBERS_GROUPS_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersSubstituteGroupsObject.setGroupURL(body);
            }
        });
        currentMembersDetailsObject.setSubstituteMemberGroups(memberSubstituteGroups);
        

        
        
        // parse member websites
        final MembersDetailsWebsitesObject currentMembersDetailsWebsitesObject = new MembersDetailsWebsitesObject();
        final List<MembersDetailsWebsitesObject> memberWebsites = new ArrayList<MembersDetailsWebsitesObject>();

        Element websitesList = item.getChild(TAG_MEMBERS_WEBSITE_ROOT_ELEMENT);
        Element websiteItem = websitesList.getChild(TAG_MEMBERS_WEBSITE_ITEM);
        websiteItem.setEndElementListener(new EndElementListener()
        {
            public void end()
            {
                memberWebsites.add(currentMembersDetailsWebsitesObject.copy());
            }
        });
        websiteItem.getChild(TAG_MEMBERS_WEBSITE_TITLE).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsWebsitesObject.setWebsiteTitle(body);
            }
        });
        websiteItem.getChild(TAG_MEMBERS_WEBSITE_URL).setEndTextElementListener(new EndTextElementListener()
        {
            public void end(String body)
            {
                currentMembersDetailsWebsitesObject.setWebsiteURL(body);
            }
        });

        this.setSpecificParserURL(detailsURL);
//        try
//        {
            Xml.Encoding encoding;
            try
            {
                encoding = ParseEncodingDetector.detectEncoding(getXMLURL());
                Xml.parse(this.getSpecificInputStream(), encoding, root.getContentHandler());
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            }
//        }
//        catch (SAXParseException e)
//        {
//            
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
        
        currentMembersDetailsObject.setWebsites(memberWebsites);
        
        return currentMembersDetailsObject;
    }
}