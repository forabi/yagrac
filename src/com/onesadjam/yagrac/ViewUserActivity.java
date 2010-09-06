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

import java.net.URL;

import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ViewUserActivity extends Activity
{
	private static final int CONTACT_IMAGE_HEIGHT = 160;
	private static final int CONTACT_IMAGE_WIDTH = 120;

	private String _UserId;
	private String _AuthenticatedUserId;
	private User _UserDetails;
	private final Context _Context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.viewuser);
		
		Intent launchingIntent = this.getIntent();
		_UserId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.UserId");
		_AuthenticatedUserId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");
		
		try
		{
			_UserDetails = ResponseParser.GetUserDetails(_UserId);
			ImageView userImage = (ImageView)findViewById(R.id._ViewUserImage);
			userImage.setScaleType(ScaleType.FIT_CENTER);
			userImage.setMinimumHeight((int)(CONTACT_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
			userImage.setMinimumWidth((int)(CONTACT_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));
			LazyImageLoader.LazyLoadImageView(this, new URL(_UserDetails.get_ImageUrl()), R.drawable.nophoto_unisex, userImage);
			
			TextView userDetails = (TextView)findViewById(R.id._ViewUser_UserDetails);
			
			StringBuilder sb = new StringBuilder();
			sb.append(_UserDetails.get_Name() + "<br />");
			sb.append("Location: " + _UserDetails.get_Location() + "<br /><br />");
			sb.append("<b>About</b><br />");
			sb.append(_UserDetails.get_About());
			sb.append("<br /><b>Favorite Books</b><br />");
			sb.append(_UserDetails.get_FavoriteBooks());
			sb.append("<br /><b>Favorite Authors</b><br />");
			
			for (int i = 0; i < _UserDetails.get_FavoriteAuthors().size(); i++)
			{
				sb.append(_UserDetails.get_FavoriteAuthors().get(i).get_Name() + "<br />");
			}
			sb.append("<br /><b>Interests</b><br />");
			sb.append(_UserDetails.get_Interests());
			
			userDetails.setText(Html.fromHtml(sb.toString()));
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.viewusermenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id._ViewUserMenu_ViewShelves:
				Intent viewShelfIntent = new Intent(_Context, ViewShelfActivity.class);
				viewShelfIntent.putExtra("com.onesadjam.yagrac.UserId", _UserId);
				viewShelfIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
				_Context.startActivity(viewShelfIntent);				
				return true;
			case R.id._ViewUserMenu_AddFriend:
				try
				{
					ResponseParser.SendFriendRequest(_UserId);
					Toast.makeText(_Context, "Friend Request Sent", Toast.LENGTH_LONG).show();
				}
				catch (Exception e)
				{
					Toast.makeText(_Context, "Error on Friend Request:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id._ViewUserMenu_Follow:
				try
				{
					ResponseParser.FollowUser(_UserId);
					Toast.makeText(_Context, "User Followed", Toast.LENGTH_LONG).show();
				}
				catch (Exception e)
				{
					Toast.makeText(_Context, "Error on Follow Request:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
