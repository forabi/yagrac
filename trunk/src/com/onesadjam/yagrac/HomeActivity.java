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

import com.onesadjam.yagrac.R;
import com.onesadjam.yagrac.xml.ResponseParser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity
{
	private final static int _LoginCode = 1;
	
	private String _UserId;
	private String _AccessToken;
	private String _AccessTokenSecret;
	
	private static float _ScalingFactor;  
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.homelayout);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		set_ScalingFactor(metrics.density);
		
		SharedPreferences sharedPreferences = getSharedPreferences("com.onesadjam.yagrac", MODE_PRIVATE);
		String token = sharedPreferences.getString("token", "");
		String tokenSecret = sharedPreferences.getString("tokenSecret", "");
		set_UserId(sharedPreferences.getString("userId", ""));
		
		Button myBooksButton = (Button)findViewById(R.id._MyBooksButton);
		myBooksButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent viewShelfIntent = new Intent(v.getContext(), ViewShelfActivity.class);
				viewShelfIntent.putExtra("com.onesadjam.yagrac.UserId", get_UserId());
				viewShelfIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", get_UserId());
				v.getContext().startActivity(viewShelfIntent);				
			}
		});

		Button findBooksButton = (Button)findViewById(R.id._FindBooksButton);
		findBooksButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent searchIntent = new Intent(v.getContext(), SearchActivity.class);
				searchIntent.putExtra("com.onesadjam.yagrac.UserId", get_UserId());
				searchIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", get_UserId());
				v.getContext().startActivity(searchIntent);
			}
		});

		Button friendsButton = (Button)findViewById(R.id._FriendsButton);
		friendsButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent friendsIntent = new Intent(v.getContext(), SocialActivity.class);
				friendsIntent.putExtra("com.onesadjam.yagrac.UserId", get_UserId());
				friendsIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", get_UserId());
				v.getContext().startActivity(friendsIntent);
			}
		});

		Button updatesButton = (Button)findViewById(R.id._UpdatesButton);
		updatesButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent updatesIntent = new Intent(v.getContext(), UpdatesActivity.class);
				updatesIntent.putExtra("com.onesadjam.yagrac.UserId", get_UserId());
				updatesIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", get_UserId());
				v.getContext().startActivity(updatesIntent);
			}
		});

		boolean isAuthenticated = false;
		
		if ( token == "" || token == null )
		{
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent, _LoginCode);
		}
		else
		{
			ResponseParser.SetTokenWithSecret(token, tokenSecret);
			isAuthenticated = true;
		}

		myBooksButton.setEnabled(isAuthenticated);
		updatesButton.setEnabled(isAuthenticated);
		friendsButton.setEnabled(isAuthenticated);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.homemenu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuItem logout = menu.findItem(R.id._LogoutMenuItem);
		
		if (ResponseParser.get_IsAuthenticated())
		{
			logout.setTitle("Logout");
		}
		else
		{
			logout.setTitle("Logon");
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id._LogoutMenuItem:
				if (ResponseParser.get_IsAuthenticated())
				{
					SharedPreferences sharedPreferences = getSharedPreferences("com.onesadjam.yagrac", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("token", "");
					editor.putString("tokenSecret", "");
					editor.commit();
					
					set_UserId("");

					Button button = (Button)findViewById(R.id._FriendsButton);
					button.setEnabled(false);
					button = (Button)findViewById(R.id._MyBooksButton);
					button.setEnabled(false);
					button = (Button)findViewById(R.id._UpdatesButton);
					button.setEnabled(false);
					
					ResponseParser.ClearAuthentication();
				}
				else
				{
					Intent loginIntent = new Intent(this, LoginActivity.class);
					startActivityForResult(loginIntent, _LoginCode);
				}
				return true;
				
			case R.id._Home_AboutMenuItem:
				Intent aboutIntent = new Intent(this, AboutActivity.class);
				startActivity(aboutIntent);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			String token = data.getStringExtra("com.onesadjam.yagrac.token");
			String tokenSecret = data.getStringExtra("com.onesadjam.yagrac.tokenSecret");
			set_UserId(data.getStringExtra("com.onesadjam.yagrac.userId"));
			
			if ( token != null && token != "" )
			{
				ResponseParser.SetTokenWithSecret(token, tokenSecret);
			
				Button button = (Button)findViewById(R.id._FindBooksButton);
				button.setEnabled(true);
				button = (Button)findViewById(R.id._FriendsButton);
				button.setEnabled(true);
				button = (Button)findViewById(R.id._MyBooksButton);
				button.setEnabled(true);
				button = (Button)findViewById(R.id._UpdatesButton);
				button.setEnabled(true);
			}
		}
	}

	public String get_UserId()
	{
		return _UserId;
	}

	public void set_UserId(String _UserId)
	{
		this._UserId = _UserId;
	}

	public String get_AccessToken()
	{
		return _AccessToken;
	}

	public void set_AccessToken(String _AccessToken)
	{
		this._AccessToken = _AccessToken;
	}

	public String get_AccessTokenSecret()
	{
		return _AccessTokenSecret;
	}

	public void set_AccessTokenSecret(String _AccessTokenSecret)
	{
		this._AccessTokenSecret = _AccessTokenSecret;
	}

	public static void set_ScalingFactor(float _ScalingFactor)
	{
		HomeActivity._ScalingFactor = _ScalingFactor;
	}

	public static float get_ScalingFactor()
	{
		return _ScalingFactor;
	}
}
