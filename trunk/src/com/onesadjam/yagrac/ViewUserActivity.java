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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewUserActivity extends Activity
{
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
		_UserId = launchingIntent.getExtras().getString("com.onesadjam.GoodReads.UserId");
		_AuthenticatedUserId = launchingIntent.getExtras().getString("com.onesadjam.GoodReads.AuthenticatedUserId");
		
		try
		{
			_UserDetails = ResponseParser.GetUserDetails(_UserId);
			ImageView userImage = (ImageView)findViewById(R.id._ViewUserImage);
			LazyImageLoader.LazyLoadImageView(this, new URL(_UserDetails.get_ImageUrl()), R.drawable.nophoto_unisex, userImage);
			
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
				viewShelfIntent.putExtra("com.onesadjam.GoodReads.UserId", _UserId);
				viewShelfIntent.putExtra("com.onesadjam.GoodReads.AuthenticatedUserId", _AuthenticatedUserId);
				_Context.startActivity(viewShelfIntent);				
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
