package org.mikelyons.squares;

import java.util.ArrayList;
import java.util.Observable;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;

public class BoxHandlerModel extends Observable {
	
	private DeferredHandler mainThread = new DeferredHandler();
	
	private static final HandlerThread workerThread = new HandlerThread("worker");
	static {
		workerThread.start();
	}
	private static final Handler worker = new Handler(workerThread.getLooper());
	
	private ArrayList<BoxRowModel> boxRows;
	private SQLSettingsManager ssm;
	
	public BoxHandlerModel() {
		boxRows = new ArrayList<BoxRowModel>();
	}

    /** Runs the specified runnable immediately if called from the main thread, otherwise it is
     * posted on the main thread handler. */
    private void runOnMainThread(Runnable r) {
        runOnMainThread(r, 0);
    }
    private void runOnMainThread(Runnable r, int type) {
        if (workerThread.getThreadId() == Process.myTid()) {
            // If we are on the worker thread, post onto the main handler
            mainThread.post(r);
        } else {
            r.run();
        }
    }

    /** Runs the specified runnable immediately if called from the worker thread, otherwise it is
     * posted on the worker thread handler. */
    private static void runOnWorkerThread(Runnable r) {
        if (workerThread.getThreadId() == Process.myTid()) {
            r.run();
        } else {
            // If we are not on the worker thread, then post to the worker handler
            worker.post(r);
        }
    }

	
	/**
	 * Adds a box with the given info to the given row
	 * 	The first row is referred to as row one
	 * @param row
	 * @param info
	 */
	public void addBox(ResolveInfo info, int row, int width, int height) {
		addBox( new ApplicationInfo( info ), row, 0, width, height, false );
	}
	
	/**
	 * Adds a box to the given index of the 
	 * @param info
	 * @param row
	 * @param index
	 */
	public void addBox(ResolveInfo info, int row, int index, int width, int height) {
		addBox( new ApplicationInfo(info), row, index, width, height, false);
	}
	
	public void addBox(ApplicationInfo info, int row, int index, int width, int height) {		
		addBox( info, row, index, width, height, false );
	}
	
	public void addBox(ApplicationInfo info, int row, int index, int width, int height, boolean save) {
		while( boxRows.size() < row ) {
			boxRows.add( new BoxRowModel() );
		}
		BoxModel new_box = boxRows.get(row - 1).addBox( info, index, width, height );
		
		if( new_box != null ) {
			if( save ) {
				ssm.updateBoxesAfterIncrement(row, index);
				ssm.addField(info.info.activityInfo.packageName, info.info.activityInfo.name , row, index, new_box.getWidth(), new_box.getHeight());
				ssm.printTable();
			}
			
			setChanged();
			notifyObservers();
		}
	}
	
	/**
	 * Adds a widget
	 * @param info
	 * @param row
	 * @param index
	 * @param widget
	 * @param height Whether or not to save it to the database
	 */
	public void addBoxWidget( AppWidgetProviderInfo info, int id, int row, int index, int width, int height, boolean save ) {
		while( boxRows.size() < row ) {
			boxRows.add( new BoxRowModel() );
		}
		// BoxModel new_box = boxRows.get(row - 1).addBox( info, index, width, height );
		BoxWidgetModel new_box = new BoxWidgetModel(info, id, width, height);
		boxRows.get(row - 1).addBox(new_box, index);
		
		if( save ) {
			ssm.updateBoxesAfterIncrement(row, index);
			ssm.addField( Integer.toString(id) , "n/a" , row, index, new_box.getWidth(), new_box.getHeight(), SQLSettingsHelper.TYPE_WIDGET);
			ssm.printTable();
		}
		
		setChanged();
		notifyObservers();
	}
	
	public void removeBox( int row, int index ) {
		if( this.boxRows.size() > row ) {
			Log.v("Removing Box", "" + Integer.toString(row));
			this.boxRows.get(row).removeBox(index);
			ssm.removeField(row+1, index);
			ssm.updateBoxesAfterDecrement(row+1, index);
			ssm.printTable();
		}
		
		setChanged();
		notifyObservers();
	}
	
	public void clickBox(int row, int index, Context c) {
		BoxModel b = boxRows.get(row).getBoxes().get(index);
		if( b instanceof BoxWidgetModel ) {
			// NOOP
		} else {
			b.start(c);
		}
	}
	
	public void load() {
		Runnable r = new Loader();
		runOnWorkerThread(r);
	}
	
	private class Loader implements Runnable {
		public Loader() {
			
		}

		@Override
		public void run() {
			android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND );
			
			BoxHandlerModel new_model = ssm.getModel();
			ArrayList<BoxRowModel> new_model_rows = new_model.getBoxRows();
			boxRows = new ArrayList<BoxRowModel>(new_model_rows);
			
			Runnable notify = new Runnable() {
				@Override
				public void run() {
					setChanged(); // Make this go piece by piece?
					notifyObservers();
				}
			};
			runOnMainThread(notify);
			Log.v("Loading thread", "Finished Loading");
		}
	}
	
	
	public void setSSM( SQLSettingsManager ssm ) {
		this.ssm = ssm;
	}
	
	public ArrayList<BoxRowModel> getBoxRows() {
		return boxRows;
	}
}
