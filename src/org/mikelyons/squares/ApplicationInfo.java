package org.mikelyons.squares;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ApplicationInfo {
	ResolveInfo info;
	
	ApplicationInfo( ResolveInfo info ) {
		this.info = info;
	}
	
	public void start(Context c) {
		Log.v("Application Info", "Starting Application");
		Intent new_intent = new Intent(Intent.ACTION_MAIN);
		Log.v("Application Info", info.activityInfo.packageName);
		Log.v("Application Info", info.activityInfo.name);
		new_intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name ));
		new_intent.addCategory(Intent.CATEGORY_LAUNCHER);
		new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		c.startActivity(new_intent);
	}
	
	public Drawable getIcon(PackageManager pkg) {
		return info.activityInfo.loadIcon(pkg);
	}
	
	public String getName(PackageManager pkg) {
		return (String) info.activityInfo.loadLabel(pkg);
	}
	
	public ResolveInfo getInfo() {
		return info;
	}
}