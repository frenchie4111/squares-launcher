package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class AllAppLoader {
	
	private HashMap<String, ResolveInfo> apps;
	
	public AllAppLoader() {
		setApps(new HashMap<String, ResolveInfo>());
	}
	
	public void loadApps(PackageManager pkg) {
		List<ResolveInfo> apps = null;
		//final PackageManager packageManager = c.getPackageManager();
		
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		apps = pkg.queryIntentActivities(mainIntent, 0);
		
		for( ResolveInfo r : apps ){
			this.getApps().put(r.activityInfo.name, r);
		}
	}
	
	public ArrayList<String> getAppsNameList(PackageManager pkg) {
		ArrayList<String> names = new ArrayList<String>();
		for( String key : getApps().keySet() ) {
			names.add( (String) getApps().get(key).activityInfo.loadLabel(pkg) );
		}
		return names;
	}
	
	public HashMap<String, ApplicationInfo> getMap(PackageManager pkg) {
		HashMap<String, ApplicationInfo> new_map = new HashMap<String, ApplicationInfo>();
		
		for( String key : getApps().keySet() ) {
			String name = (String) getApps().get(key).activityInfo.loadLabel(pkg);
			new_map.put(name, new ApplicationInfo( getApps().get(key) ));
		}
		
		return new_map;
	}

	public HashMap<String, ResolveInfo> getApps() {
		return apps;
	}

	public void setApps(HashMap<String, ResolveInfo> apps) {
		this.apps = apps;
	}
}
