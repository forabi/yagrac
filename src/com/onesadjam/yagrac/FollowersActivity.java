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

import com.onesadjam.yagrac.xml.Followers;
import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class FollowersActivity extends Activity implements ILastItemRequestedListener
{
	private SocialAdapter _ContactAdapter;
	private ListView _FollowersListView;
	private int _PageSize = 20;
	private int _TotalItems = 0;
	private int _ItemsLoaded = 0;
	private String _UserId = "";
	private String _AuthenticatedUserId = "";
	private boolean _LoadingNextPage = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		_ContactAdapter = new SocialAdapter(this);
		_FollowersListView = new ListView(this);
		
		setContentView(_FollowersListView);
		
		_UserId = getIntent().getExtras().getString("com.onesadjam.yagrac.UserId");
		_AuthenticatedUserId = getIntent().getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

		try
		{
			Followers followers = ResponseParser.GetFollowers(_UserId);
			_TotalItems = followers.get_Total();
			_ItemsLoaded = followers.get_End();
			_PageSize = _ItemsLoaded;

			if (followers != null)
			{
				for ( int i = 0; i < followers.get_Followers().size(); i++ )
				{
					_ContactAdapter.AddContact(followers.get_Followers().get(i));
				}
			}

			_FollowersListView.setAdapter(_ContactAdapter);

			_FollowersListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					User clickedUser = (User)arg0.getAdapter().getItem(arg2);
					Intent viewUserIntent = new Intent(arg1.getContext(), ViewUserActivity.class);
					viewUserIntent.putExtra("com.onesadjam.yagrac.UserId", clickedUser.get_Id());
					viewUserIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
					arg1.getContext().startActivity(viewUserIntent);
				}
			});

			_ContactAdapter.setLastItemRequestedListener(this);
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public synchronized void onLastItemRequest(Object source, int requestedIndex)
	{
		if (!_LoadingNextPage)
		{
			_LoadingNextPage = true;
			loadNextPage();
		}
	}
	
	private void loadNextPage()
	{
		if (_ItemsLoaded == _TotalItems)
		{
			_LoadingNextPage = false;
			return;
		}

		final Handler contactsLoadedHandler = new Handler() 
		{
    		@Override
    		public void handleMessage(Message message) 
    		{
    			Followers followers = (Followers)message.obj;
    			for (int i = 0; i < followers.get_Followers().size(); i++)
    			{
    				_ContactAdapter.AddContact(followers.get_Followers().get(i));
    			}
    			_ContactAdapter.notifyDataSetChanged();
    			_ItemsLoaded = followers.get_End();
    			_LoadingNextPage = false;
    		}
    	};
    	
    	Thread thread = new Thread()
    	{
    		@Override
    		public void run() 
    		{
				try
				{
					Followers followers = ResponseParser.GetFollowers(_UserId, (_ItemsLoaded / _PageSize) + 1);
					Message message = contactsLoadedHandler.obtainMessage(1, followers);
					contactsLoadedHandler.sendMessage(message);	
				}
				catch (Exception e)
				{
					_LoadingNextPage = false;
					e.printStackTrace();
				}
    		}
    	};
    	thread.start();
	}
}
