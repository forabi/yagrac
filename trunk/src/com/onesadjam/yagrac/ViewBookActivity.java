//===============================================================================
// Copyright (c) 2010 Adam C Jones
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//===============================================================================

package com.onesadjam.yagrac;

import java.util.List;

import com.onesadjam.yagrac.xml.ResponseParser;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class ViewBookActivity extends TabActivity
{
	private static final int PICK_SHELVES_DIALOG = 1;
	private final Context _Context = this;

	private String _BookId;
	private String _AuthenticatedUserId;
	
	private static final int TAB_HEIGHT = 33;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.friends);
		
		TabHost tabs = getTabHost();
	    TabHost.TabSpec spec;
	    
		Intent launchingIntent = this.getIntent();
		_BookId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.BookId");
		_AuthenticatedUserId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

	    Intent intent = new Intent().setClass(this, ViewBookDetailsActivity.class);
	    intent.putExtra("com.onesadjam.yagrac.BookId", _BookId);
	    intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
	    spec = tabs.newTabSpec("Details").setIndicator("Details").setContent(intent);
	    tabs.addTab(spec);

	    intent = new Intent().setClass(this, ViewBookReviewsActivity.class);
	    intent.putExtra("com.onesadjam.yagrac.BookId", _BookId);
	    intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
	    spec = tabs.newTabSpec("Reviews").setIndicator("Reviews").setContent(intent);
	    tabs.addTab(spec);

	    tabs.setCurrentTab(0);
	    
	    final TabHost tabHost = getTabHost();
	    tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = (int)(TAB_HEIGHT * HomeActivity.get_ScalingFactor()); 
	    tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = (int)(TAB_HEIGHT * HomeActivity.get_ScalingFactor()); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.viewbookmenu, menu);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case PICK_SHELVES_DIALOG:
				final PickShelvesDialog pickShelvesDialog = new PickShelvesDialog(this);
				pickShelvesDialog.set_UserId(_AuthenticatedUserId);
				pickShelvesDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						if (pickShelvesDialog.is_Accepted())
						{
							List<String> selectedShelves = pickShelvesDialog.get_SelectedShelves();
							for (int i = 0; i < selectedShelves.size(); i++)
							{
								try
								{
									ResponseParser.AddBookToShelf(_BookId, selectedShelves.get(i));
									Toast.makeText(_Context, "added to " + selectedShelves.get(i), Toast.LENGTH_SHORT).show();
								}
								catch (Exception e)
								{
									Toast.makeText(_Context, e.getMessage(), Toast.LENGTH_LONG).show();
								}
							}
						}
						removeDialog(PICK_SHELVES_DIALOG);
					}
				});
				return pickShelvesDialog;
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id._ViewBookMenu_AddToShelf:
				if (_AuthenticatedUserId == null || _AuthenticatedUserId.length() == 0)
				{
					Toast.makeText(_Context, "You must be logged in to add books to your shelves.", Toast.LENGTH_LONG).show();
					return true;
				}
				showDialog(PICK_SHELVES_DIALOG);
				return true;
			case R.id._ViewBookMenu_Review:
				if (_AuthenticatedUserId == null || _AuthenticatedUserId.length() == 0)
				{
					Toast.makeText(_Context, "You must be logged in to review a book.", Toast.LENGTH_LONG).show();
					return true;
				}
				Intent intent = new Intent(_Context, ReviewBookActivity.class);
				intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
				intent.putExtra("com.onesadjam.yagrac.BookId", _BookId);
				_Context.startActivity(intent);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
