package org.mikelyons.squares;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class BoxModel {
	private ApplicationInfo application;
	int w;
	int h;
	
	BoxModel( ApplicationInfo info, int width, int height ) {
		this();
		this.w = width;
		this.h = height;
		application = info;
	}
	
	public BoxModel(ResolveInfo info) {
		this();
		application = new ApplicationInfo(info);
	}
	
	public BoxModel(ApplicationInfo info) {
		this();
		application = info;
	}
	
	public BoxModel() {
		w = 200;
		h = 200;
	}
	
	public BoxModel(int width, int height) {
		w = width;
		h = height;
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
	
	public String toString() {
		return "BoxModel(" + application.info.toString() + ")";
	}
	
	public int getWidth() {
		return w;
	}
	public int getHeight() {
		return h;
	}
}
