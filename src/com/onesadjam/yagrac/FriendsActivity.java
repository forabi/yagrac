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

import com.onesadjam.yagrac.xml.Friends;
import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		ListView listview = new ListView(this);
		SocialAdapter contactAdapter = new SocialAdapter(this);
		setContentView(listview);
		
		Intent launchingIntent = this.getIntent();
		String userId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.UserId");
		final String authenticatedUserId = getIntent().getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

		try
		{
			Friends friendsXml = ResponseParser.GetFriends(userId);
			if (friendsXml != null)
			{
				List<User> friends = friendsXml.get_Friends();
				if (friends != null)
				{
					for ( int i = 0; i < friends.size(); i++ )
					{
						contactAdapter.AddContact(friends.get(i));
					}
				}
			}
			
			listview.setAdapter(contactAdapter);
			
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					User clickedUser = (User)arg0.getAdapter().getItem(arg2);
					Intent viewUserIntent = new Intent(arg1.getContext(), ViewUserActivity.class);
					viewUserIntent.putExtra("com.onesadjam.yagrac.UserId", clickedUser.get_Id());
					viewUserIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", authenticatedUserId);
					arg1.getContext().startActivity(viewUserIntent);				

				}
			});
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
