package org.linkedpersonaldata.quantifiedself;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.mit.media.funf.FunfManager;

import android.app.Notification;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private WebViewFragmentPagerAdapter mFragmentAdapter;	
	private FunfManager mFunfManager;
	private HashSet<String> mSurveysShown = new HashSet<String>();
	
	private ServiceConnection mPipelineConnection = new ServiceConnection() {
		public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {			
			mFunfManager = ((FunfManager.LocalBinder) service).getManager();
		};	
		
		public void onServiceDisconnected(android.content.ComponentName name) {
			mFunfManager = null;
		};		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferencesWrapper prefs = new PreferencesWrapper(this);
		
		String token = prefs.getAccessToken();
		String uuid = prefs.getUUID();
		String pdsLocation = prefs.getPDSLocation();
		
		if (token != null && uuid != null && pdsLocation != null) {
			setContentView(R.layout.activity_main);
			mViewPager = (ViewPager) findViewById(R.id.viewpager);
			
			if (mFragmentAdapter == null) {
				PDSWrapper pds = new PDSWrapper(this);
				mFragmentAdapter = new WebViewFragmentPagerAdapter(getSupportFragmentManager());
				addStandardViews(pds);			
				mViewPager.setAdapter(mFragmentAdapter);
			}
			
		} else {
			startLoginActivity();
			finish();
		}
	}
		
	private void addStandardViews(PDSWrapper pds) {		
		String radialUrl = pds.buildAbsoluteUrl(R.string.radial_relative_url);
		String activityUrl = pds.buildAbsoluteUrl(R.string.activity_relative_url);
		String socialUrl = pds.buildAbsoluteUrl(R.string.social_relative_url);
		String focusUrl = pds.buildAbsoluteUrl(R.string.focus_relative_url);
		String placesUrl = pds.buildAbsoluteUrl(R.string.places_relative_url);
		String adminUrl = pds.buildAbsoluteUrl("/admin/audit.html");
		
		mFragmentAdapter.addItem(WebViewFragment.Create(radialUrl, "My Social Health", this, mViewPager));
		mFragmentAdapter.addItem(WebViewFragment.Create(activityUrl, "Activity", this, mViewPager));
		mFragmentAdapter.addItem(WebViewFragment.Create(socialUrl, "Social", this, mViewPager));
		mFragmentAdapter.addItem(WebViewFragment.Create(focusUrl, "Focus", this, mViewPager));
		//mFragmentAdapter.addItem(WebViewFragment.Create(placesUrl, "Places", this, mViewPager));
		//mFragmentAdapter.addItem(WebViewFragment.Create(sharingUrl, "Settings", getApplicationContext()));
		//mFragmentAdapter.addItem(WebViewFragment.Create(adminUrl, "Audit Logs", this, mViewPager));
	}
	
	private void startLoginActivity() {
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivity(loginIntent);	
	}
}
