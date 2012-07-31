package de.bundestag.android.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.AppConstant;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.parser.objects.CommitteesObject;
import de.bundestag.android.parser.objects.MembersDetailsObject;
import de.bundestag.android.sections.committees.CommitteesActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMemberDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersByFractionActivity;
import de.bundestag.android.sections.committees.ComDetailsMemByFractionListAdapter;
import de.bundestag.android.sections.committees.CommitteesDetailsMembersViewHelper;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsDetailsActivity;
import de.bundestag.android.sections.committees.CommitteesDetailsNewsListAdapter;
import de.bundestag.android.sections.committees.CommitteesDetailsTasksActivity;
import de.bundestag.android.sections.committees.CommitteesListAdapter;
import de.bundestag.android.sections.members.MembersCityActivity;
import de.bundestag.android.sections.members.MembersCityListAdapter;
import de.bundestag.android.sections.members.MembersDetailsActivity;
import de.bundestag.android.sections.members.MembersElectionActivity;
import de.bundestag.android.sections.members.MembersElectionListAdapter;
import de.bundestag.android.sections.members.MembersFractionActivity;
import de.bundestag.android.sections.members.MembersFractionCommitteeAdapter;
import de.bundestag.android.sections.members.MembersFractionListAdapter;
import de.bundestag.android.sections.members.MembersListActivity;
import de.bundestag.android.sections.members.MembersListAdapter;
import de.bundestag.android.sections.members.MembersSubListDetailsActivity;
import de.bundestag.android.sections.members.MembersSubpageMembersActivity;
import de.bundestag.android.sections.members.MembersSubpageViewHelper;
import de.bundestag.android.sections.news.NewsActivity;
import de.bundestag.android.sections.news.NewsDetailsActivity;
import de.bundestag.android.sections.news.NewsListActivity;
import de.bundestag.android.sections.news.NewsListAdapter;
import de.bundestag.android.sections.plenum.DebateNewsActivity;
import de.bundestag.android.sections.plenum.DebateNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumAudioActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesListAdapter;
import de.bundestag.android.sections.plenum.PlenumDebatesNewsDetailsActivity;
import de.bundestag.android.sections.plenum.PlenumDebatesViewHelper;
import de.bundestag.android.sections.plenum.PlenumNewsActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersActivity;
import de.bundestag.android.sections.plenum.PlenumSpeakersListAdapter;
import de.bundestag.android.sections.plenum.PlenumSpeakersViewHelper;
import de.bundestag.android.sections.plenum.PlenumVideoActivity;
import de.bundestag.android.sections.visitors.VisitorsListAdapter;
import de.bundestag.android.sections.visitors.VisitorsLocationsActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsLocationsListActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsActivity;
import de.bundestag.android.sections.visitors.VisitorsNewsDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersDetailsActivity;
import de.bundestag.android.sections.visitors.VisitorsOffersListActivity;
import de.bundestag.android.storage.CommitteesDatabaseAdapter;
import de.bundestag.android.storage.MembersDatabaseAdapter;
import de.bundestag.android.storage.NewsDatabaseAdapter;
import de.bundestag.android.storage.VisitorsDatabaseAdapter;
import de.bundestag.android.synchronization.MembersSynchronization;
import de.bundestag.android.synchronization.NewsSynchronization;

/**
 * General list fragment class that holds the list. This is a fragment so it can
 * be used together with another fragment inside the same activity.
 * 
 * See http://developer.android.com/guide/topics/fundamentals/fragments.html
 */
public class GeneralListFragment extends ListFragment
{
    int mCurCheckPosition = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getListView().setFastScrollEnabled(true);
        ColorDrawable line_color = new ColorDrawable(0xFF8fa3a7);
        getListView().setDivider(line_color);
        getListView().setDividerHeight(1);
        getListView().setCacheColorHint(0x00000000);
       // getListView().setSelectionFromTop(1, 0);
        // setEmptyText("No card match specified filter");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity instanceof NewsActivity)
        {
            handleNewsList();
        }
        if (activity instanceof NewsListActivity)
        {
            handleNewsSubList();
        }

        // Plenum
        if (activity instanceof PlenumSpeakersActivity)
        {
            handlePlenumSpeakersList();
        }
        if (activity instanceof PlenumDebatesActivity)
        {
            handlePlenumDebatesList();
        }
        if (activity instanceof PlenumVideoActivity)
        {
            handlePlenumList();
        }
        if (activity instanceof PlenumAudioActivity)
        {
            handlePlenumList();
        }
        if (activity instanceof DebateNewsActivity)
        {
            handleDebateNewsList();
        }

        // Members
        if (activity instanceof MembersListActivity)
        {
            handleMembersList();
        }
        if (activity instanceof MembersFractionActivity)
        {
            handleMembersFractionList();
        }
        if (activity instanceof MembersCityActivity)
        {
            handleMembersCityList();
        }
        if (activity instanceof MembersElectionActivity)
        {
            handleMembersElectionList();
        }
        if (activity instanceof MembersSubpageMembersActivity)
        {
            MembersSubpageMembersActivity membersSubpageMembersActivity = (MembersSubpageMembersActivity) activity;

            handleMembersSubpage(membersSubpageMembersActivity.subPageId, membersSubpageMembersActivity.index);
        }

        // Committee
        if (activity instanceof CommitteesActivity)
        {
            handleCommitteesList();
        }
        if (activity instanceof CommitteesDetailsNewsActivity)
        {
            CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) activity;
            handleCommitteesDetailNewsList(committeesDetailsNewsActivity.committeesName);
        }
        if (activity instanceof CommitteesDetailsMembersActivity)
        {
            CommitteesDetailsMembersActivity committeesDetailsMembersActivity = (CommitteesDetailsMembersActivity) activity;

            handleCommitteesDetailMembersList(committeesDetailsMembersActivity.committeesId);
        }
        if (activity instanceof CommitteesDetailsMembersByFractionActivity)
        {
            CommitteesDetailsMembersByFractionActivity committeesDetailsMembersByFractionActivity = (CommitteesDetailsMembersByFractionActivity) activity;

            handleCommitteesDetailMembersByFractionList(committeesDetailsMembersByFractionActivity.committeesId,
                    committeesDetailsMembersByFractionActivity.committeesIdString, committeesDetailsMembersByFractionActivity.fractionName);
        }

        // Visitors
        if (activity instanceof VisitorsOffersActivity)
        {
            handleVisitorsOffersList();
        }
        if (activity instanceof VisitorsOffersListActivity)
        {
            handleVisitorsOffersSubList();
        }
        if (activity instanceof VisitorsLocationsActivity)
        {
            handleVisitorsLocationsList();
        }
        if (activity instanceof VisitorsLocationsListActivity)
        {
            handleVisitorsLocationsSubList();
        }
        if (activity instanceof VisitorsNewsActivity)
        {
            handleVisitorsNewsList();
        }
    }

    // News
    private void handleNewsList()
    {
        NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
        newsDatabaseAdapter.open();

        Cursor newsCursor = newsDatabaseAdapter.fetchAllNews();
        getActivity().startManagingCursor(newsCursor);
        setListAdapter(new NewsListAdapter(getActivity(), newsCursor));
    }

    private void handleNewsSubList()
    {
        NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
        newsDatabaseAdapter.open();

        NewsListActivity newsListActivity = (NewsListActivity) getActivity();
        int listId = newsListActivity.listId;

        Cursor newsCursor = newsDatabaseAdapter.fetchSubListNews(listId);
        getActivity().startManagingCursor(newsCursor);
        setListAdapter(new NewsListAdapter(getActivity(), newsCursor));
    }

    
    // Plenum
    private void handlePlenumList()
    {
    }

    // Plenum Speakers
    private void handlePlenumSpeakersList()
    {
    	ArrayList<HashMap<String, Object>> speakers = PlenumSpeakersViewHelper.createSpeakers();
    	if(speakers!=null){
    		setListAdapter(new PlenumSpeakersListAdapter(getActivity(), speakers));
    		for (int i = 0; i < speakers.size(); i++) {
    			String state = (String) speakers.get(i).get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
    			if(state!=null){
	    			if(state.equals("live") || state.equals("läuft")){
	    				getListView().setSelectionFromTop(i, 0);    				
	    			}
    			}
    		}
    		
    		//getListView().smoothScrollToPosition(5);
    	}
    }

    // Plenum Debates
    private void handlePlenumDebatesList()
    {
    	ArrayList<HashMap<String, Object>> debates = PlenumDebatesViewHelper.createDebates();
    	PlenumDebatesActivity.debates = debates;
    	if(debates!=null){
    		setListAdapter(new PlenumDebatesListAdapter(getActivity(), debates));
    		for (int i = 0; i < debates.size(); i++) {
    			String state = (String) debates.get(i).get(PlenumSpeakersViewHelper.KEY_SPEAKER_STATE);
    			if(state!=null){
	    			if(state.equals("live") || state.equals("läuft")){
	    				getListView().setSelectionFromTop(i, 0);    				
	    			}
    			}
    		}
    	}
    }

    // Members
    private void handleMembersList()
    {
        MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
        membersDatabaseAdapter.open();

//        Cursor newsCursor = membersDatabaseAdapter.fetchAllMembers();
        Cursor newsCursor = membersDatabaseAdapter.fetchAllMembersOptimized();
        getActivity().startManagingCursor(newsCursor);
        setListAdapter(new MembersListAdapter(getActivity(), newsCursor));

    }

    private void handleMembersFractionList()
    {
        ArrayList<HashMap<String, Object>> fractions = MembersSubpageViewHelper.createMembersFractions(getActivity());

        setListAdapter(new MembersFractionListAdapter(getActivity(), fractions));
    }

    private void handleMembersCityList()
    {
        ArrayList<HashMap<String, Object>> cities = MembersSubpageViewHelper.createMembersCities(getActivity());

        setListAdapter(new MembersCityListAdapter(getActivity(), cities));
    }

    private void handleMembersElectionList()
    {
        ArrayList<HashMap<String, Object>> elections = MembersSubpageViewHelper.createMembersElections(getActivity());

        setListAdapter(new MembersElectionListAdapter(getActivity(), elections));
    }

    /**
     * Gets the members that belong to either a fraction, city or an election.
     */
    private void handleMembersSubpage(int subPageId, int index)
    {
        ArrayList values = MembersSubpageViewHelper.createMembersSubpage(getActivity(), subPageId, index);
        Cursor membersCursor = (Cursor) values.get(0);
        String selected = (String) values.get(1);
        getActivity().startManagingCursor(membersCursor);
        
        //Add textView with corresponding title - fraction, city or election
        TextView valueTV = new TextView(getActivity().getApplicationContext());
        valueTV.setText(selected);
        valueTV.setPadding(10, 10, 10, 10);
        valueTV.setBackgroundResource(R.color.members_title__background_color);
        valueTV.setTextColor(Color.WHITE);
        valueTV.setTextSize(17);
        valueTV.setTypeface(Typeface.SERIF);
        valueTV.setOnClickListener(null);
        
        this.getListView().addHeaderView(valueTV);
        setListAdapter(new MembersListAdapter(getActivity(), membersCursor));
    }

    // Committees
    private void handleCommitteesList()
    {
        CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
        committeesDatabaseAdapter.open();

        Cursor committeesCursor = committeesDatabaseAdapter.fetchAllCommittees();
        getActivity().startManagingCursor(committeesCursor);
        setListAdapter(new CommitteesListAdapter(getActivity(), committeesCursor));
    }

    private void handleCommitteesDetailNewsList(String committeeName)
    {
        CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) getActivity();

        CommitteesDatabaseAdapter committeesDatabaseAdapter = new CommitteesDatabaseAdapter(getActivity());
        committeesDatabaseAdapter.open();

        Cursor committeesNewsCursor = null;
        if (committeesDetailsNewsActivity.committeesIdString != null)
        {
            committeesNewsCursor = committeesDatabaseAdapter.fetchCommitteesNews(committeesDetailsNewsActivity.committeesIdString);
        }

        getActivity().startManagingCursor(committeesNewsCursor);  
        
        //If no news are present in a committee go to the Aufgaben
        if(committeesNewsCursor.getCount() == 0) {
        	
        	committeesDetailsNewsActivity.finish();
        	committeesNewsCursor.close();

        	// go to aufgaben        	
            Intent intent = new Intent();
            intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
            intent.putExtra(CommitteesDetailsNewsDetailsActivity.KEY_COMMITTEE_ID, committeesDetailsNewsActivity.committeesIdString);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);        	
        } else {
        	setListAdapter(new CommitteesDetailsNewsListAdapter(getActivity(), committeesNewsCursor, committeeName));
        }
    }

    private void handleCommitteesDetailMembersList(int committeesId)
    {
        CommitteesObject committeesObject = CommitteesDetailsMembersViewHelper.getCommittee(getActivity(), committeesId);

        // get members belonging to this committee

        int committeeMembersCount =  CommitteesDetailsMembersViewHelper.getCommitteeMembersCount(getActivity(), committeesObject.getId());
      // Log.e("COMITEE COUNT","" + committeeMembersCount);
        
        // get the president
        List<MembersDetailsObject> presidents = CommitteesDetailsMembersViewHelper.getPresident(getActivity(), committeesObject.getId());

        // get the sub president
        List<MembersDetailsObject> subPresidents = CommitteesDetailsMembersViewHelper.getSubPresidents(getActivity(), committeesObject.getId());

        // create the fractions with members for each in this committee
        ArrayList<HashMap<String, Object>> fractions = CommitteesDetailsMembersViewHelper.createMembersFractions(getActivity(), committeesObject.getId());

               
        setListAdapter(new MembersFractionCommitteeAdapter(getActivity(), fractions, presidents, subPresidents, committeesObject, committeeMembersCount));
    }

    private void handleCommitteesDetailMembersByFractionList(int committeesId, String committeesIdString, String fractionName)
    {
        CommitteesObject committeesObject = CommitteesDetailsMembersViewHelper.getCommittee(getActivity(), committeesId);

        // get members belonging to this committee

        // get the president
        List<MembersDetailsObject> presidents = CommitteesDetailsMembersViewHelper.getPresident(getActivity(), committeesObject.getId());

        // get the sub president
        List<MembersDetailsObject> subPresidents = CommitteesDetailsMembersViewHelper.getSubPresidents(getActivity(), committeesObject.getId());

        // create the members for each fraction in this committee
        ArrayList<HashMap<String, Object>> membersByFraction =
                CommitteesDetailsMembersViewHelper.getMembersByFraction(getActivity(), committeesId, committeesIdString, fractionName);

        int committeeMembersCount =  CommitteesDetailsMembersViewHelper.getCommitteeMembersCount(getActivity(), committeesObject.getId());

        
        MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(getActivity());
        membersDatabaseAdapter.open();
        Cursor memberCursor = membersDatabaseAdapter.fetchAllMembersByFractionsCommittee(fractionName, committeesIdString, MembersSynchronization.GROUP_TYPE_FULLMEMBER);

        int membersByFractionPresidentCount = 0;
        if (memberCursor != null)
        {
            membersByFractionPresidentCount = memberCursor.getCount();        
        }
        memberCursor.close();
        membersDatabaseAdapter.close();
        
        setListAdapter(new ComDetailsMemByFractionListAdapter(getActivity(), membersByFraction, membersByFractionPresidentCount, presidents, subPresidents, committeesObject,
                fractionName, committeeMembersCount));
    }

    // Visitors
    private void handleVisitorsOffersList()
    {
        VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
        visitorsDatabaseAdapter.open();

        Cursor offersCursor = visitorsDatabaseAdapter.fetchOffers();
        getActivity().startManagingCursor(offersCursor);
        setListAdapter(new VisitorsListAdapter(getActivity(), offersCursor, visitorsDatabaseAdapter));
    }

    private void handleVisitorsLocationsList()
    {
        VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
        visitorsDatabaseAdapter.open();

        Cursor offersCursor = visitorsDatabaseAdapter.fetchLocations();
        getActivity().startManagingCursor(offersCursor);
        setListAdapter(new VisitorsListAdapter(getActivity(), offersCursor, visitorsDatabaseAdapter));
    }

    private void handleVisitorsLocationsSubList()
    {
        VisitorsLocationsListActivity visitorsLocationsListActivity = (VisitorsLocationsListActivity) getActivity();
        int listId = visitorsLocationsListActivity.listId;

        VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
        visitorsDatabaseAdapter.open();

        Cursor articlesCursor = visitorsDatabaseAdapter.fetchArticleList(listId);
        getActivity().startManagingCursor(articlesCursor);
        setListAdapter(new VisitorsListAdapter(getActivity(), articlesCursor, visitorsDatabaseAdapter));
    }

    private void handleVisitorsOffersSubList()
    {
        VisitorsOffersListActivity visitorsOffersListActivity = (VisitorsOffersListActivity) getActivity();
        int listId = visitorsOffersListActivity.listId;

        VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(getActivity());
        visitorsDatabaseAdapter.open();

        Cursor articlesCursor = visitorsDatabaseAdapter.fetchArticleList(listId);
        getActivity().startManagingCursor(articlesCursor);
        setListAdapter(new VisitorsListAdapter(getActivity(), articlesCursor, visitorsDatabaseAdapter));
    }

    private void handleVisitorsNewsList()
    {
        NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
        newsDatabaseAdapter.open();

        Cursor newsCursor = newsDatabaseAdapter.fetchAllVisitorNews();
        getActivity().startManagingCursor(newsCursor);
        setListAdapter(new NewsListAdapter(getActivity(), newsCursor));
    }
    
    private void handleDebateNewsList()
    {
        NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(getActivity());
        newsDatabaseAdapter.open();

        Cursor newsCursor = newsDatabaseAdapter.fetchAllDebateNews();
        getActivity().startManagingCursor(newsCursor);
        setListAdapter(new NewsListAdapter(getActivity(), newsCursor));
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    /**
     * List click handler.
     * 
     * Whenever a general list fragment is clicked it will call the method
     * showDetails with the position and the index of the clicked item.
     * 
     * Since we both use cursor adapters and base adapters, we need to check
     * according to the activity, which one we are using.
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        Activity activity = getActivity();
        if ((activity instanceof MembersFractionActivity) || (activity instanceof MembersCityActivity) || (activity instanceof MembersElectionActivity))
        {
            showDetails(position, position);
        }
        else if (activity instanceof PlenumDebatesActivity)
        {
        	String detailsXML = (String) PlenumDebatesActivity.debates.get(position).get(PlenumDebatesViewHelper.KEY_DETAIL_XML);
        	if ((detailsXML != null) && (!detailsXML.equals("")))
            {
        		showDetails(position, position);
            }
        }
        
        else if (activity instanceof PlenumSpeakersActivity )
        {
        	//do nothing
        }
        else if ((activity instanceof CommitteesDetailsMembersActivity))
        {
            CommitteesDetailsMembersActivity committeesDetailsMembersActivity = (CommitteesDetailsMembersActivity) activity;

            HashMap<String, Object> hashMap = (HashMap<String, Object>) getListView().getItemAtPosition(position);
            String fractionName = (String) hashMap.get(MembersFractionListAdapter.KEY_FRACTION_NAME);
            showDetailsByFraction(position, committeesDetailsMembersActivity.committeesId, fractionName);
        }
        else if ((activity instanceof CommitteesDetailsMembersByFractionActivity))
        {
            CommitteesDetailsMembersByFractionActivity committeesDetailsMembersByFractionActivity = (CommitteesDetailsMembersByFractionActivity) activity;
            HashMap<String, Object> hashMap = (HashMap<String, Object>) getListView().getItemAtPosition(position);
            String memberID = (String) hashMap.get(MembersDatabaseAdapter.KEY_ID);
            showDetailsMember(memberID);
        }
        else
        {
            Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
            getActivity().startManagingCursor(cursor);
            showDetails(position, cursor.getInt(cursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID)));
            cursor.close();
        }
    }

    /**
     * Special to see member by fraction and committee Invoked whenever the
     * general list fragment is clicked. It will call the activity to handle the
     * click, depending on the current
     * 
     */
    void showDetailsByFraction(int index, int committeeId, String fractionName)
    {
        mCurCheckPosition = index;

        Intent intent = new Intent();
        intent.putExtra("index", index);
        intent.putExtra("committeesId", committeeId);
        intent.putExtra("fractionName", fractionName);
        Activity activity = getActivity();
        if (activity instanceof CommitteesDetailsMembersActivity)
        {
            intent.setClass(getActivity(), CommitteesDetailsMembersByFractionActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    /**
     * Special to see member by fraction and committee Invoked whenever the
     * general list fragment is clicked. It will call the activity to handle the
     * click, depending on the current
     * 
     */
    public void showDetailsMember(String memberId)
    {
        Activity activity = getActivity();
        MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
        membersDatabaseAdapter.open();
        Cursor memberCursor = membersDatabaseAdapter.fetchMemberFromId(memberId);

        // MembersDetailsObject membersDetailsObject = new
        // MembersDetailsObject();

        int memberIndex = memberCursor.getInt(memberCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));

        memberCursor.close();
        membersDatabaseAdapter.close();

        Intent intent = new Intent();
        intent.putExtra("index", memberIndex);

        if (activity instanceof CommitteesDetailsMembersByFractionActivity)
        {
            intent.setClass(getActivity(), CommitteesDetailsMemberDetailsActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    /**
     * Invoked whenever the general list fragment is clicked.
     * 
     * It will call the activity to handle the click, depending on the current
     * activity.
     */
    void showDetails(int position, int index)
    {
        mCurCheckPosition = index;

        Intent intent = new Intent();
        intent.putExtra("index", index);

        Activity activity = getActivity();

        // News
        if (activity instanceof NewsActivity)
        {
            NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(activity);
            newsDatabaseAdapter.open();
            Cursor newsCursor = newsDatabaseAdapter.fetchNews(index);

            Integer newsType = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_TYPE));
            String newsDetails = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILSXML));

            if ((newsDetails != null) && (!newsDetails.equals("")))
            {
                if ((newsType != null) && (newsType.intValue() == NewsSynchronization.NEWS_TYPE_LIST))
                {
                    Integer newsId = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_ROWID));
                    newsCursor = newsDatabaseAdapter.getListIdFromNewsId(newsId);
                    
                    // UGLY HACK
                    if (newsCursor.getCount() > 0 && !AppConstant.isFragmentSupported) //if fragment doesn't support
                    {
                        Integer listId = newsCursor.getInt(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_NEWSLIST_ROWID));
                        
                        intent.putExtra(NewsListActivity.KEY_NEWS_LISTID, listId);
                        intent.setClass(getActivity(), NewsListActivity.class);
                    }else if(newsCursor.getCount() > 0 && AppConstant.isFragmentSupported){
                    	// if fragment support then call to newslistActivity ....
            		}
                    else
                    {
                        intent.setClass(getActivity(), NewsDetailsActivity.class);
                    }
                }
                else
                {
                    intent.setClass(getActivity(), NewsDetailsActivity.class);
                }
            }
            else
            {
                intent.setClass(getActivity(), NewsActivity.class);
            }

            newsCursor.close();
            newsDatabaseAdapter.close();
        }
        else if (activity instanceof NewsListActivity)
        {
            NewsListActivity newsListActivity = (NewsListActivity) getActivity();
            
            // Check if news has details
            NewsDatabaseAdapter newsDatabaseAdapter = new NewsDatabaseAdapter(activity);
            newsDatabaseAdapter.open();
            Cursor newsCursor = newsDatabaseAdapter.fetchNews(index);
            String detailsXML = newsCursor.getString(newsCursor.getColumnIndex(NewsDatabaseAdapter.KEY_DETAILSXML));
            newsCursor.close();
            newsDatabaseAdapter.close();

            // Only call news details if there is actually a news detail
            if ((detailsXML != null) && (!detailsXML.equals("")))
            {
                intent.putExtra(NewsListActivity.KEY_NEWS_LISTID, newsListActivity.listId);
                intent.setClass(getActivity(), NewsDetailsActivity.class);
            }
        }

        // Plenum
        else if (activity instanceof PlenumNewsActivity)
        {
            // intent.setClass(getActivity(), PlenumDetailsActivity.class);
        }
        else if (activity instanceof PlenumDebatesActivity)
        {       	
            	 intent.setClass(getActivity(), PlenumDebatesNewsDetailsActivity.class);         
        }

        // Members
        else if (activity instanceof MembersListActivity)
        {
            intent.setClass(getActivity(), MembersDetailsActivity.class);
        }
        else if (activity instanceof MembersFractionActivity)
        {
            intent.setClass(getActivity(), MembersSubpageMembersActivity.class);
            intent.putExtra(MembersSubpageMembersActivity.KEY_SUB_PAGE, MembersSubpageMembersActivity.SUB_PAGE_FRACTION);
        }
        else if (activity instanceof MembersCityActivity)
        {
            intent.setClass(getActivity(), MembersSubpageMembersActivity.class);
            intent.putExtra(MembersSubpageMembersActivity.KEY_SUB_PAGE, MembersSubpageMembersActivity.SUB_PAGE_CITY);
        }
        else if (activity instanceof MembersElectionActivity)
        {
            intent.setClass(getActivity(), MembersSubpageMembersActivity.class);
            intent.putExtra(MembersSubpageMembersActivity.KEY_SUB_PAGE, MembersSubpageMembersActivity.SUB_PAGE_ELECTION);
        }
        else if (activity instanceof MembersSubpageMembersActivity)
        {
            MembersSubpageMembersActivity membersSubpageMembersActivity = (MembersSubpageMembersActivity) activity;
            intent.setClass(getActivity(), MembersSubListDetailsActivity.class);
            intent.putExtra(MembersSubpageMembersActivity.KEY_SUB_PAGE, membersSubpageMembersActivity.subPageId);
            intent.putExtra(MembersSubpageMembersActivity.KEY_LIST_KEY, membersSubpageMembersActivity.index);
//            intent.setClass(getActivity(), MembersDetailsActivity.class);
        }

        // Committees
        else if (activity instanceof CommitteesActivity)
        {
            CommitteesActivity committeesActivity = (CommitteesActivity) activity;
            if (DataHolder.isOnline(committeesActivity))
            {
                intent.setClass(getActivity(), CommitteesDetailsNewsActivity.class);
            }
            else
            {
                intent.setClass(getActivity(), CommitteesDetailsTasksActivity.class);
            }
        }
        else if (activity instanceof CommitteesDetailsNewsActivity)
        {
            CommitteesDetailsNewsActivity committeesDetailsNewsActivity = (CommitteesDetailsNewsActivity) activity;
            intent.setClass(getActivity(), CommitteesDetailsNewsDetailsActivity.class);
            intent.putExtra(CommitteesDetailsNewsDetailsActivity.KEY_COMMITTEE_ID, committeesDetailsNewsActivity.committeesId);
        }

        else if (activity instanceof CommitteesDetailsMembersByFractionActivity)
        {
            intent.setClass(getActivity(), CommitteesDetailsMemberDetailsActivity.class);
        }

        // Visitors
        else if (activity instanceof VisitorsOffersActivity)
        {
            VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(activity);
            visitorsDatabaseAdapter.open();
            Cursor visitorCursor = visitorsDatabaseAdapter.articleDetails(index);

            String articleType = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));

            if (articleType.equals("article-list"))
            {
                String articleId = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_ID));

                visitorCursor = visitorsDatabaseAdapter.getArticleListId(articleId);
                int listId = visitorCursor.getInt(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLELIST_ROWID));

                intent.setClass(getActivity(), VisitorsOffersListActivity.class);
                intent.putExtra(VisitorsOffersListActivity.KEY_LIST_ID, listId);
            }
            else
            {
                intent.setClass(getActivity(), VisitorsOffersDetailsActivity.class);
            }

            visitorCursor.close();
            visitorsDatabaseAdapter.close();
        }
        else if (activity instanceof VisitorsOffersListActivity)
        {
            intent.setClass(getActivity(), VisitorsOffersDetailsActivity.class);

            VisitorsOffersListActivity visitorsOffersListActivity = (VisitorsOffersListActivity) activity;
            intent.putExtra(VisitorsOffersListActivity.KEY_LIST_ID, visitorsOffersListActivity.listId);
        }
        else if (activity instanceof VisitorsLocationsActivity)
        {
            VisitorsDatabaseAdapter visitorsDatabaseAdapter = new VisitorsDatabaseAdapter(activity);
            visitorsDatabaseAdapter.open();
            Cursor visitorCursor = visitorsDatabaseAdapter.articleDetails(index);

            String articleType = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_TYPE));

            if (articleType.equals("article-list"))
            {
                String articleId = visitorCursor.getString(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLE_ID));

                visitorCursor = visitorsDatabaseAdapter.getArticleListId(articleId);
                int listId = visitorCursor.getInt(visitorCursor.getColumnIndex(VisitorsDatabaseAdapter.KEY_ARTICLELIST_ROWID));

                intent.setClass(getActivity(), VisitorsLocationsListActivity.class);
                intent.putExtra(VisitorsLocationsListActivity.KEY_LIST_ID, listId);
            }
            else
            {
                intent.setClass(getActivity(), VisitorsLocationsDetailsActivity.class);
            }

            visitorCursor.close();
            visitorsDatabaseAdapter.close();
        }
        else if (activity instanceof VisitorsLocationsListActivity)
        {
            intent.setClass(getActivity(), VisitorsLocationsDetailsActivity.class);

            VisitorsLocationsListActivity visitorsLocationsListActivity = (VisitorsLocationsListActivity) activity;
            intent.putExtra(VisitorsLocationsListActivity.KEY_LIST_ID, visitorsLocationsListActivity.listId);
        }
        else if (activity instanceof VisitorsNewsActivity)
        {
            intent.setClass(getActivity(), VisitorsNewsDetailsActivity.class);
        }
        else if (activity instanceof DebateNewsActivity)
        {
            intent.setClass(getActivity(), DebateNewsDetailsActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }
}
