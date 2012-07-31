package de.bundestag.android;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Bundestag Shared Preferences.
 * 
 * Ads globally shared state handling.
 */
public abstract class BundestagSharedPreferences
{
    private static final String SHARED_PREFERENCES_FILENAME = "BundestagSharedPreferences";

    private static final String SHOW_OPTIONS_MENU_KEY = "SHOW_OPTIONS_MENU_KEY";

    private static final String PLENUM_NEEDS_NEWS_KEY = "PLENUM_NEEDS_NEWS_KEY";

    private static final String CURRENT_MEMBER_KEY = "CURRENT_MEMBER_KEY";

    public static boolean getCheckedSynchronization(Activity activity)
    {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_FILENAME, 0);

        return settings.getBoolean(SHOW_OPTIONS_MENU_KEY, false);
    }

    public static void setCheckedSynchronization(Activity activity, boolean checkedSynchronization)
    {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SHOW_OPTIONS_MENU_KEY, checkedSynchronization);
        editor.commit();
    }

    public static boolean getPlenumNeedsNews(Activity activity)
    {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_FILENAME, 0);

        return settings.getBoolean(PLENUM_NEEDS_NEWS_KEY, false);
    }

    public static void setPlenumNeedsNews(Activity activity, boolean plenumNeedsNews)
    {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PLENUM_NEEDS_NEWS_KEY, plenumNeedsNews);
        editor.commit();
    }

    public static int getCurrentMember(Activity activity)
    {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_FILENAME, 0);

        return settings.getInt(CURRENT_MEMBER_KEY, -1);
    }

    public static void setCurrentMember(Activity activity, int memberId)
    {
        SharedPreferences settings = activity.getSharedPreferences(SHARED_PREFERENCES_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(CURRENT_MEMBER_KEY, memberId);
        editor.commit();
    }

    /**
     * Cleanup the session settings at startup.
     */
    public static void cleanupStartSettings(Activity activity)
    {
        setCheckedSynchronization(activity, false);
        setPlenumNeedsNews(activity, true);
        setCurrentMember(activity, -1);
    }
}
