package org.linkedpersonaldata.quantifiedself;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;


public class WebViewFragmentJavascriptInterface {
	
	protected ViewPager mViewPager;
	protected Activity mActivity;
	
	public WebViewFragmentJavascriptInterface(ViewPager viewPager, Activity activity) {
		mViewPager = viewPager;
		mActivity = activity;
	}
	
	public boolean hideWebNavBar() {
		return true;
	}
	
	public boolean handleTabChange(final String dimension, final int tabNumber) {
		if (mActivity != null && mViewPager != null) {
			mActivity.runOnUiThread(new Runnable() { 
				public void run() {
					mViewPager.setCurrentItem(tabNumber + 1);
				}
			});
		}
		return true;
	}
	
	public boolean updateSharedPreference(final String key, final String value) {
		SharedPreferences sharedPrefs = mActivity.getSharedPreferences(mActivity.getString(R.string.prefs_file), Activity.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		
		editor.putString(key, value);
		
		return editor.commit();
	}
}