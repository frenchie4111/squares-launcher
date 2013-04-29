package org.mikelyons.squares;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class BoxModel {
	private ApplicationInfo application;
	
	public BoxModel(ResolveInfo info) {
		application = new ApplicationInfo(info);
	}
	
	public BoxModel() {
		
	}
	
	public void start(Context c) {
		application.start(c);
	}
	
	public String getLabel(PackageManager pkg) {
		return application.getName(pkg);
	}
	
	public Drawable getIcon(PackageManager pkg)	{
		return application.getIcon(pkg);
	}
}
