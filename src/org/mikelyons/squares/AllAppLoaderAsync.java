package org.mikelyons.squares;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.util.Log;

public class AllAppLoaderAsync extends AsyncTask<AllAppLoader, Void, HashMap<String, ApplicationInfo>> {

	AllAppLoader loader;
	
	@Override
	protected void onPostExecute(HashMap<String, ApplicationInfo> results) {
		
	}

	@Override
	protected HashMap<String, ApplicationInfo> doInBackground(
			AllAppLoader... params) {
		loader = params[0];
		List<ResolveInfo> apps = null;
		HashMap<String, ApplicationInfo> results = new HashMap<String, ApplicationInfo>();
		
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		apps = loader.getPkg().queryIntentActivities(mainIntent, 0);
		
		for( ResolveInfo r : apps ){
			String name = r.activityInfo.loadLabel(loader.getPkg()).toString();
			results.put(name, new ApplicationInfo(r));
		}
		loader.setApps(results);
		loader.populateList();
		Log.v("All Apps Async", "Done");
		return results;
	}
}
