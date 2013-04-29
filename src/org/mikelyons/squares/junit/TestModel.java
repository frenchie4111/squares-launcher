package org.mikelyons.squares.junit;

import java.util.List;

import org.mikelyons.squares.BoxHandlerModel;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import junit.framework.TestCase;

public class TestModel extends TestCase {

	public TestModel(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests if adding a box to the first row works
	 */
	public void test_boxHandlerModelAddBoxSimple() {
		BoxHandlerModel model = new BoxHandlerModel();
		
		model.addBox(1);
		
		assertEquals( model.getBoxRows().size(), 1 );
		assertEquals( model.getBoxRows().get(0).getBoxes().size(), 1);
	}
	
	/**
	 * Tests if adding a box on a larger nonexistant row works
	 */
	public void test_boxHandlerModelAddBoxNewRow() {
		BoxHandlerModel model = new BoxHandlerModel();
		
		model.addBox(3);
		
		assertEquals( model.getBoxRows().size(), 3 );
		assertEquals( model.getBoxRows().get(2).getBoxes().size(), 1);
	}
	
	public void test_03boxHandlerModelWithInfo() {
		
	}

}
