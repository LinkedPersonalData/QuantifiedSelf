package org.linkedpersonaldata.quantifiedself;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class UserInfoTask extends AsyncTask<String, Void, Boolean>  {

	private static final String LOG_TAG = "UserInfoTask";
	
	private Context mContext;
	
	
	public UserInfoTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (params.length != 1) {
			throw new IllegalArgumentException("UserInfoTask requires token as a parameter.");
		}
		String token = params[0];
		RegistryServer registry = new RegistryServer(mContext);
		
		try {
			JSONObject responseJson = registry.getUserInfo(token);
			
			if (responseJson == null) {
				showToast("Registry server user info is broken. Please contact brian717@media.mit.edu");
				Log.e(LOG_TAG, "Unable to parse response from getUserInfo.");
			}
			
			if (responseJson.has("error")) {
				showToast("Error while getting user info.");
				Log.e(LOG_TAG, String.format("Error while getting user info: %s - %s", responseJson.getString("error"), responseJson.getString("error_description")));
			} else if (responseJson.has("id") && responseJson.has("pds_location")) {
				Editor prefsEditor = mContext.getSharedPreferences(mContext.getString(R.string.prefs_file), mContext.MODE_PRIVATE).edit();
				
				prefsEditor.putString("uuid", responseJson.getString("id"));
				prefsEditor.putString("pds_location", responseJson.getString("pds_location"));
				prefsEditor.commit();
				
				return true;
			}
			
		} catch (Exception e) {
			showToast("Failed contacting the server. Please try again later.");
			Log.e(LOG_TAG, "Error during login - " + e.getMessage());
		}
		
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// We're assuming that if this is called from an activity, it should close and show the main screen
		// This makes sense currently, since the only activity this is run from so far is Login
		
		if (result && mContext instanceof Activity) {
			Activity activity = (Activity)mContext;
			Intent mainActivityIntent = new Intent(activity, MainActivity.class);
			activity.startActivity(mainActivityIntent);
			activity.finish();				
		}
	}		
	
	private void showToast(final String message) {
		if (mContext instanceof Activity) {
			final Activity activity = (Activity)mContext;
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
