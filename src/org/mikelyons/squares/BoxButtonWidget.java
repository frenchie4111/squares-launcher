package org.mikelyons.squares;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.view.View;

public class BoxButtonWidget extends BoxButton {

	BoxWidgetModel model;
	
	public BoxButtonWidget(BoxWidgetModel box, Context context, AppWidgetHost host) {
		super(box, context);
		model = box;
		
		setup(host);
	}
	
	public void setup(AppWidgetHost host) {
		int widgetId = model.getId();
		AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(getContext());
		AppWidgetProviderInfo appWidgetInfo =  mAppWidgetManager.getAppWidgetInfo(model.getId());
		AppWidgetHostView hostView = host.createView(getContext(), model.getId(), appWidgetInfo);
	    hostView.setAppWidget(widgetId, appWidgetInfo);
	    addWidgetView(hostView);
	}
	
	public void addWidgetView( View view ) {
		addView(view);
	}

}
