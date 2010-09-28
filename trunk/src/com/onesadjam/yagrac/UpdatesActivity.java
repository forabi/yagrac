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
import com.onesadjam.yagrac.xml.Update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class UpdatesActivity extends Activity
{
	private String _UserId;
	private ProgressDialog _BusySpinner;
	private Context _Context;
	private ListView _UpdatesList;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.updates);
		
		_Context = this;

		_UpdatesList = (ListView)findViewById(R.id._UpdatesListView);
		
		if (!ResponseParser.get_IsAuthenticated())
		{
			Toast.makeText(this, "Not authenticated", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		_UserId = getIntent().getExtras().getString("com.onesadjam.yagrac.UserId");

		_BusySpinner = ProgressDialog.show(this, "", "Loading updates...");
		
		new LoadUpdatesTask().execute();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		final Context context = this;
		MenuItem menuItem = menu.add("Update Status");
		menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				Intent updateStatusIntent = new Intent(context, UpdateStatusActivity.class);
				updateStatusIntent.putExtra("com.onesadjam.yagrac.UserId", _UserId);
				startActivity(updateStatusIntent);
				return true;
			}
		});
		return true;
	}
	
	private class LoadUpdatesTask extends AsyncTask<Void, Void, List<Update>>
	{
		@Override
		protected List<Update> doInBackground(Void... params)
		{
			List<Update> updates = null;
			try
			{
				updates = ResponseParser.GetFriendsUpdates();
			}
			catch (Exception e){}
			return updates;
		}

		@Override
		protected void onPostExecute(List<Update> result)
		{
			if (result == null)
			{
				Toast.makeText(_Context, "Unable to retrieve updates.", Toast.LENGTH_LONG);
			}
			else
			{
				UpdateItemAdapter updateItemAdapter = new UpdateItemAdapter(_Context);
				for (int i = 0; i < result.size(); i++ )
				{
					updateItemAdapter.add(result.get(i));
				}
				_UpdatesList.setAdapter(updateItemAdapter);
			}
			_BusySpinner.dismiss();
		}
	}
}
