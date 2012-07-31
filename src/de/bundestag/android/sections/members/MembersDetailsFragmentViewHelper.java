package de.bundestag.android.sections.members;

//import android.graphics.Typeface;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.bundestag.android.R;
import de.bundestag.android.constant.DataHolder;
import de.bundestag.android.helpers.ImageHelper;
import de.bundestag.android.helpers.TextHelper;
import de.bundestag.android.parser.objects.MembersDetailsObject;

public class MembersDetailsFragmentViewHelper {
	public static void createDetailsView(MembersDetailsObject membersDetailsObject, FragmentActivity activity, int groupsCount) {

		String imageString = membersDetailsObject.getMediaPhotoImageString();
		ImageView image = (ImageView) activity.findViewById(R.id.image_tab);
		if (imageString != null) {
			image.setImageBitmap(ImageHelper.convertStringToBitmap(imageString));

		} else {
			image.setImageBitmap(null);
		}

		TextView name = (TextView) activity.findViewById(R.id.name_tab);
		name.setText(TextHelper.customizedFromHtml(membersDetailsObject.getCourse() + " " + membersDetailsObject.getFirstName() + " " + membersDetailsObject.getTitle() + " "
				+ membersDetailsObject.getLastName()));// + ",<br>" +
														// membersDetailsObject.getFraction()));

		TextView committeeName = (TextView) activity.findViewById(R.id.committee_name_tab);
		committeeName.setText(TextHelper.customizedFromHtml(membersDetailsObject.getFraction() + " | " + membersDetailsObject.getCity()));

		TextView fractionAndCity = (TextView) activity.findViewById(R.id.status_tab);
		fractionAndCity.setText(TextHelper.customizedFromHtml(membersDetailsObject.getFraction() + "|" + membersDetailsObject.getCity()));
		TextView status = (TextView) activity.findViewById(R.id.status_tab);

		String statusText = "";
		if (membersDetailsObject.getStatus().equals("Verstorben")) {
			statusText = "Verstorben am " + membersDetailsObject.getExitDate();
		} else if (membersDetailsObject.getStatus().equals("Ausgeschieden")) {
			statusText = "Ausgeschieden am " + membersDetailsObject.getExitDate();
		} else if (membersDetailsObject.getStatus().equals("Aktiv")) {
			if (membersDetailsObject.getElected().equals("Landesliste")) {
				statusText = "Gewählt über die Landesliste " + membersDetailsObject.getCity();
			} else if (membersDetailsObject.getElected().equals("direkt")) {
				statusText = "Direkt gewählt im Wahlkreis " + membersDetailsObject.getElectionName();
			}

		}
		status.setText(TextHelper.customizedFromHtml(statusText));
		TextView occupation = (TextView) activity.findViewById(R.id.occupation_tab);
		occupation.setText(TextHelper.customizedFromHtml(membersDetailsObject.getProfession()));
		System.out.println("details---->" + TextHelper.customizedFromHtml(membersDetailsObject.getProfession()));
		LinearLayout sub_tab = (LinearLayout) activity.findViewById(R.id.sub_tab_members);
		sub_tab.setWeightSum(1.0f);
		defaultSet(activity);
		if (membersDetailsObject.getStatus().equals("Verstorben") || membersDetailsObject.getStatus().equals("Ausgeschieden")) {
			DataHolder.noOfSubTab = 1;

			sub_tab.setWeightSum(0.5f);
			LinearLayout groupsHolder3 = (LinearLayout) activity.findViewById(R.id.subMenuHolder3Tab);
			groupsHolder3.setVisibility(View.GONE);
			LinearLayout groupsHolder4 = (LinearLayout) activity.findViewById(R.id.subMenuHolder4Tab);
			groupsHolder4.setVisibility(View.GONE);

			ImageView groupsHolderDivider3 = (ImageView) activity.findViewById(R.id.seperator_subMenuHolder3);
			groupsHolderDivider3.setVisibility(View.GONE);
			ImageView groupsHolderDivider4 = (ImageView) activity.findViewById(R.id.seperator_subMenuHolder4);
			groupsHolderDivider4.setVisibility(View.GONE);
		} else if (groupsCount <= 0) {
			DataHolder.noOfSubTab = 2;

			if (DataHolder.isLandscape) {
				sub_tab.setWeightSum(0.75f);
			} else {
				sub_tab.setWeightSum(1.0f);
				LinearLayout groupsHolder4 = (LinearLayout) activity.findViewById(R.id.subMenuHolder4Tab);
				groupsHolder4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.33f));
				LinearLayout groupsHolder1 = (LinearLayout) activity.findViewById(R.id.subMenuHolder1Tab);
				groupsHolder1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.33f));
				LinearLayout groupsHolder2 = (LinearLayout) activity.findViewById(R.id.subMenuHolder2Tab);
				groupsHolder2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.33f));
			}

			LinearLayout groupsHolder3 = (LinearLayout) activity.findViewById(R.id.subMenuHolder3Tab);
			groupsHolder3.setVisibility(View.GONE);
			ImageView groupsHolderDivider3 = (ImageView) activity.findViewById(R.id.seperator_subMenuHolder3);
			groupsHolderDivider3.setVisibility(View.GONE);

		}
	}

	private static void defaultSet(Activity activity) {

		LinearLayout groupsHolder1 = (LinearLayout) activity.findViewById(R.id.subMenuHolder1Tab);
		groupsHolder1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
		LinearLayout groupsHolder2 = (LinearLayout) activity.findViewById(R.id.subMenuHolder2Tab);
		groupsHolder2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
		LinearLayout groupsHolder3 = (LinearLayout) activity.findViewById(R.id.subMenuHolder3Tab);
		if (DataHolder.isLandscape) {
			groupsHolder3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
		} else {
			groupsHolder3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.23f));
		}
		groupsHolder3.setVisibility(View.VISIBLE);
		LinearLayout groupsHolder4 = (LinearLayout) activity.findViewById(R.id.subMenuHolder4Tab);
		if (DataHolder.isLandscape) {
			groupsHolder4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
		} else {
			groupsHolder4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.27f));
		}
		groupsHolder4.setVisibility(View.VISIBLE);
		ImageView groupsHolderDivider3 = (ImageView) activity.findViewById(R.id.seperator_subMenuHolder3);
		groupsHolderDivider3.setVisibility(View.VISIBLE);
		ImageView groupsHolderDivider4 = (ImageView) activity.findViewById(R.id.seperator_subMenuHolder4);
		groupsHolderDivider4.setVisibility(View.VISIBLE);
		DataHolder.noOfSubTab = 3;
	}
}
