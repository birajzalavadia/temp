package de.bundestag.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.parser.objects.CommitteesDetailsNewsDetailsObject;
import de.bundestag.android.parser.objects.CommitteesDetailsObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.parser.objects.NewsDetailsObject;
import de.bundestag.android.parser.objects.PlenumObject;
import de.bundestag.android.parser.objects.PlenumTeaserObject;
import de.bundestag.android.parser.objects.VisitorsArticleObject;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsViewHelper;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsViewHelper;
import de.bundestag.android.sections.impressum.ImpressumActivity;
import de.bundestag.android.sections.impressum.ImpressumViewHelper;
import de.bundestag.android.sections.members.MembersDetailsActivity;
import de.bundestag.android.sections.members.MembersDetailsBiographyActivity;
import de.bundestag.android.sections.members.MembersDetailsBiographyViewHelper;
import de.bundestag.android.sections.members.MembersDetailsCommitteesActivity;
import de.bundestag.android.sections.members.MembersDetailsCommitteesViewHelper;
import de.bundestag.android.sections.members.MembersDetailsContactActivity;
import de.bundestag.android.sections.members.MembersDetailsContactViewHelper;
import de.bundestag.android.sections.members.MembersDetailsInfoActivity;
import de.bundestag.android.sections.members.MembersDetailsInfoViewHelper;
import de.bundestag.android.sections.members.MembersDetailsViewHelper;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsDetailsActivity;
import de.bundestag.android.sections.news.NewsDetailsViewHelper;
import de.bundestag.android.sections.plenum.DebateNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumAufgabeActivity;
import de.bundestag.android.sections.plenum.PlenumAufgabeViewHelper;
import de.bundestag.android.sections.plenum.PlenumDebatesNewsDetailViewHelper;
import de.bundestag.android.sections.plenum.PlenumDebatesNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumNewsViewHelper;
import de.bundestag.android.sections.plenum.PlenumVideoActivity;
import de.bundestag.android.sections.plenum.PlenumVideoViewHelper;
import de.bundestag.android.sections.plenum.PlenumViewHelper;
import de.bundestag.android.sections.visitors.VisitorsContactActivity;
import de.bundestag.android.sections.visitors.VisitorsDetailsViewHelper;
import de.bundestag.android.sections.visitors.VisitorsLocationsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersDetailsActivity;

/**
 * General details fragment. Shows the details for a section.
 * 
 * It is created as a fragment, so it can be used in different activities and even
 * together with other fragments, for example the list fragment.
 */
public class GeneralDetailsFragment extends Fragment
{
    private LinearLayout layout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Activity activity = getActivity();

        // News
        if (activity instanceof NewsDetailsActivity || activity instanceof NewsActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
    	}

        // Plenum
        else if (activity instanceof PlenumNewsActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.plenum_news_item, container, false);
        }
        else if (activity instanceof PlenumNewsDetailsActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
        }
        else if (activity instanceof DebateNewsDetailsActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
        }
        else if (activity instanceof PlenumDebatesNewsDetailsActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
        }
        
        // Plenum video
        else if (activity instanceof PlenumVideoActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.plenum_video_item, container, false);
        }

        // Plenum task
        else if (activity instanceof PlenumAufgabeActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.plenum_aufgabe_item, container, false);
        }

        // Members
        else if (activity instanceof MembersDetailsActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.members_detail_item, container, false);
        }
        else if (activity instanceof MembersDetailsBiographyActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.members_detail_biography, container, false);
        }
        else if (activity instanceof MembersDetailsInfoActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.members_detail_info, container, false);
        }
        else if (activity instanceof MembersDetailsCommitteesActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.members_detail_committees, container, false);
        }
        else if (activity instanceof MembersDetailsContactActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.members_detail_contact, container, false);
        }
       
        // Committees
        else if (activity instanceof CommitteesDetailsNewsActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.committees_detail_list, container, false);
        }
        else if (activity instanceof CommitteesDetailsNewsDetailsActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
        }
        else if (activity instanceof CommitteesDetailsMembersActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.committees_detail_list, container, false);
        }
        else if (activity instanceof CommitteesDetailsTasksActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.committees_detail_tasks, container, false);
        }

        // Visitors
        else if (activity instanceof VisitorsOffersDetailsActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
        }
        else if (activity instanceof VisitorsLocationsDetailsActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
        }
        else if (activity instanceof VisitorsNewsDetailsActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.news_detail_item, container, false);
        }
        else if (activity instanceof VisitorsContactActivity)
        {
            layout = (LinearLayout) inflater.inflate(R.layout.visitors_contact_item, container, false);
        }
        if (activity instanceof ImpressumActivity)
        {
        	layout = (LinearLayout) inflater.inflate(R.layout.impressum_item, container, false);
    	}
        return layout;
    }

    
    // News
    public void showNewsDetails(NewsDetailsObject newsDetailsObject, boolean linksOn)
    {
        NewsDetailsViewHelper.createDetailsView(newsDetailsObject, getActivity(), linksOn, false);
    }
    
    // Visitor news
    public void showVisitorNewsDetails(NewsDetailsObject newsDetailsObject, boolean linksOn)
    {
        NewsDetailsViewHelper.createDetailsView(newsDetailsObject, getActivity(), linksOn, true);
    }
    
    
    // Members
    public void showMembersDetails(MembersDetailsObject membersDetailsObject, int groupsCount)
    {
        MembersDetailsViewHelper.createDetailsView(membersDetailsObject, getActivity(), groupsCount);
    }
    
    public void showMembersDetailsBiography(MembersDetailsObject membersDetailsObject)
    {
        MembersDetailsBiographyViewHelper.createDetailsView(membersDetailsObject, getActivity());
    }
    public void showMembersDetailsInfo(MembersDetailsObject membersDetailsObject)
    {
        MembersDetailsInfoViewHelper.createDetailsView(membersDetailsObject, getActivity());
    }
    
    public void showMembersDetailsContact(MembersDetailsObject membersDetailsObject)
    {
        MembersDetailsContactViewHelper.createDetailsView(membersDetailsObject, getActivity());
    }
    
    public void showMembersDetailsCommittees(MembersDetailsObject membersDetailsObject, Activity activity, int memberRowId)
    {
        MembersDetailsCommitteesViewHelper.createDetailsView(membersDetailsObject, getActivity(), activity, memberRowId);
    }
    
    
    // Plenum
    public void showPlenum(PlenumObject plenumObject)
    {
        PlenumViewHelper.createView(plenumObject, getActivity());
    }
    
    public void showPlenumNews(PlenumTeaserObject plenumTeaserObject)
    {
        PlenumNewsViewHelper.createView(plenumTeaserObject, getActivity());
    }
    
    public void showPlenumNews(PlenumObject plenumObject)
    {
        PlenumNewsViewHelper.createView(plenumObject, getActivity());
    }
    
    public void showPlenumNewsDetails(PlenumObject plenumObject)
    {
        PlenumNewsViewHelper.createDetailsView(plenumObject, getActivity());
    }

    // Plenum news
    public void showPlenumDebatesNewsDetails(NewsDetailsObject newsDetailsObject)
    {
        PlenumDebatesNewsDetailViewHelper.createDetailsView(newsDetailsObject, getActivity());
    }
    
    // Plenum Video
    public void showPlenumVideo(PlenumObject plenumObject)
    {
        PlenumVideoViewHelper.createView(plenumObject, getActivity());
    }
    
    // Plenum tasks
    public void showPlenumTasks(PlenumObject plenumObject)
    {
        PlenumAufgabeViewHelper.createView(plenumObject, getActivity());
    }
    
    // Committees
    public void showCommitteesDetailsNews(CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject)
    {
    	CommitteesDetailsNewsDetailsViewHelper.createCommitteesDetailsNewsDetailsView(committeesDetailsNewsDetailsObject, getActivity());
    }
    
    public void showCommitteesDetailsNewsDetails(CommitteesDetailsNewsDetailsObject committeesDetailsNewsDetailsObject)
    {
        CommitteesDetailsNewsDetailsViewHelper.createCommitteesDetailsNewsDetailsView(committeesDetailsNewsDetailsObject, getActivity());
    }

    public void showCommitteesDetailsMembers(CommitteesDetailsObject committeesDetailsObject)
    {
        CommitteesDetailsViewHelper.createDetailsMembersView(committeesDetailsObject, getActivity());
    }

    public void showCommitteesDetailsTasks(CommitteesDetailsObject committeesDetailsObject)
    {
        CommitteesDetailsViewHelper.createDetailsTasksView(committeesDetailsObject, getActivity());
    }
    
    //visitor
    public void showVisitorsOffersDetails(VisitorsArticleObject visitorsDetailsObject)
    {
        VisitorsDetailsViewHelper.createDetailsView(visitorsDetailsObject, getActivity());
    }
    
    public void showVisitorsLocationsDetails(VisitorsArticleObject newsDetailsObject)
    {
        VisitorsDetailsViewHelper.createDetailsView(newsDetailsObject, getActivity());
    }
    
    public void showVisitorsNewsDetails(VisitorsArticleObject newsDetailsObject)
    {
        VisitorsDetailsViewHelper.createNewsDetailsView(newsDetailsObject, getActivity());
    }
    
    public void showVisitorsContactDetails(VisitorsArticleObject visitorContactDetailsObject)
    {
    	if (AppConstant.isFragmentSupported) {
    		VisitorsDetailsViewHelper.createContactDetailsViewTab(visitorContactDetailsObject, getActivity());
		} else {
			VisitorsDetailsViewHelper.createContactDetailsView(visitorContactDetailsObject, getActivity());
		}
        
    }
    
    public void showImpressum()
    {
        ImpressumViewHelper.createImpressumView( getActivity());
    }
}