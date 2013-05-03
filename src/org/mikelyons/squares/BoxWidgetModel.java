package org.mikelyons.squares;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.view.View;

public class BoxWidgetModel extends BoxModel {
	
	private AppWidgetProviderInfo widget;
	private int widgetId;
	
	public BoxWidgetModel( AppWidgetProviderInfo info, int widgetId, int width, int height ) {
		this.widget = info;
		this.widgetId = widgetId;
	}
	
	public int getId() {
		return widgetId;
	}
}
