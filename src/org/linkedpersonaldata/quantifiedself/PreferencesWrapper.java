package org.linkedpersonaldata.quantifiedself;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesWrapper {

	private SharedPreferences mSharedPreferences;
	private Context mContext;
	
	public PreferencesWrapper(Context context) {
		mContext = context;
		mSharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs_file), context.MODE_PRIVATE);
	}
	
	public String getAccessToken() {
		return mSharedPreferences.getString(mContext.getString(R.string.access_token_prefs_key), null);
	}
	
	public String getPDSLocation() {
		return mSharedPreferences.getString("pds_location", null);
	}
	
	public String getUUID() {
		return mSharedPreferences.getString("uuid", null);
	}
	
	public Set<String> getPendingSurveys() {
		// NOTE: we're returning a copy here, rather than the original
		// This is so that modifications to the set will be saved properly if we store them in the same preferences key.
		return new HashSet<String>(mSharedPreferences.getStringSet("surveys", null));
	}
	
	public void addPendingSurvey(String survey) {
		Editor editor = mSharedPreferences.edit();
		Set<String> pendingSurveys = (getPendingSurveys() == null)? new HashSet<String>() : getPendingSurveys();
		
		pendingSurveys.remove(survey);
		pendingSurveys.add(survey);
	
		editor.putStringSet("surveys", pendingSurveys);
		
		editor.commit();
	}
	
	public void removePendingSurvey(String survey) {
		Editor editor = mSharedPreferences.edit();
		
		Set<String> pendingSurveys = getPendingSurveys();
		
		if (pendingSurveys == null) {
			return;
		}
		
		pendingSurveys.remove(survey);
		
		editor.putStringSet("surveys", pendingSurveys);
		editor.commit();
	}
}
