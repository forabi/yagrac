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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class UpdatesActivity extends Activity
{
	private String _UserId;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.updates);

		ListView updatesList = (ListView)findViewById(R.id._UpdatesListView);
		
		if (!ResponseParser.get_IsAuthenticated())
		{
			Toast.makeText(this, "Not authenticated", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		_UserId = getIntent().getExtras().getString("com.onesadjam.yagrac.UserId");

		try
		{	
			// TODO: The retrieval and parsing of updates takes a while.
			// TODO: kick this off to another thread and put up some sort of busy
			// TODO: indicator to the user to let them know what is going on.
			UpdateItemAdapter updateItemAdapter = new UpdateItemAdapter(this);
			List<Update> updates = ResponseParser.GetFriendsUpdates();
			
			for (int i = 0; i < updates.size(); i++ )
			{
				updateItemAdapter.add(updates.get(i));
			}
			updatesList.setAdapter(updateItemAdapter);
			
//			InputStream responseContent = response.getEntity().getContent();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(responseContent));
//			StringBuilder sb = new StringBuilder();
//			String nextLine = null;
//			while ((nextLine = reader.readLine()) != null)
//			{
//				sb.append(nextLine + "\n");
//			}
//			String fullResponse = sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
}
