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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.onesadjam.yagrac.xml.Response;
import com.onesadjam.yagrac.xml.ResponseParser;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity
{
	private final static String _ConsumerKey = "gSrbJMIPZcESXMUpYEtaQ";
	private final static String _ConsumerSecret = "7UptROsPTSejruDpGU59aOoy0oT321NigZLcmjDDy0";
	private final static String _CallbackUrl = "onesadjam://goodreads";
	private final static CommonsHttpOAuthConsumer _Consumer = new CommonsHttpOAuthConsumer(_ConsumerKey, _ConsumerSecret);

	private final static OAuthProvider _Provider = new DefaultOAuthProvider(
			"http://www.goodreads.com/oauth/request_token",
			"http://www.goodreads.com/oauth/access_token", 
			"http://www.goodreads.com/oauth/authorize");

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		
		Button loginButton = (Button)findViewById(R.id._LoginButton);
		loginButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String authUrl = "";
				try
				{
					authUrl = _Provider.retrieveRequestToken(_Consumer, _CallbackUrl);
					Context context = v.getContext();
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				}
				catch (Exception e)
				{
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}				
			}
		});
		
		Button noLoginButton = (Button) findViewById(R.id._NoLoginButton);
		noLoginButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent tokens = new Intent(getIntent());
				tokens.putExtra("token", "");
				tokens.putExtra("tokenSecret", "");
				setResult(RESULT_OK, tokens);
				finish();
			}
		});
	}

	/** Called when the activity is resumed. */
	@Override
	public void onResume()
	{
		super.onResume();

		// We might be resuming due to the web browser sending us our
		// access tokens.  If so, save them and finish.
		Uri uri = this.getIntent().getData();
		if (uri != null && uri.toString().startsWith(_CallbackUrl))
		{
			String oauthToken = uri.getQueryParameter(OAuth.OAUTH_TOKEN);
			
			// this will populate token and token_secret in consumer
			try
			{
				_Provider.retrieveAccessToken(_Consumer, oauthToken);
				String token = _Consumer.getToken();
				String tokenSecret = _Consumer.getTokenSecret();
				String userId = "";
				
				SharedPreferences sharedPreferences = getSharedPreferences("com.onesadjam.GoodReads", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("token", token);
				editor.putString("tokenSecret", tokenSecret);
				editor.commit();

				HttpGet getRequest = new HttpGet("http://www.goodreads.com/api/auth_user");
				try
				{
					_Consumer.sign(getRequest);
					HttpClient httpClient = new DefaultHttpClient();
					HttpResponse response = httpClient.execute(getRequest);
					
					Response responseData = ResponseParser.parse(response.getEntity().getContent());
					
					userId = responseData.get_User().get_Id();
					
					sharedPreferences = getSharedPreferences("com.onesadjam.GoodReads", MODE_PRIVATE);
					editor = sharedPreferences.edit();
					editor.putString("userId", userId);
					editor.commit();
				}
				catch (Exception e)
				{
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

				Intent tokens = new Intent(getIntent());
				tokens.putExtra("com.onesadjam.GoodReads.token", token);
				tokens.putExtra("com.onesadjam.GoodReads.tokenSecret", tokenSecret);
				tokens.putExtra("com.onesadjam.GoodReads.userId", userId);
				setResult(RESULT_OK, tokens);
				
				Toast.makeText(this, "Thanks for authenticating!\nPlease close the browser to continue", Toast.LENGTH_LONG).show();
				finish();
			}
			catch (Exception e)
			{
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		
		// we also might be resuming because the user backed out of the browser.
		else
		{
			SharedPreferences sharedPreferences = getSharedPreferences("com.onesadjam.GoodReads", MODE_PRIVATE);
			String token = sharedPreferences.getString("token", "");
			String tokenSecret = sharedPreferences.getString("tokenSecret", "");
			String userId = sharedPreferences.getString("userId", "");

			if ( token != "" )
			{

				Intent tokens = new Intent(getIntent());
				tokens.putExtra("com.onesadjam.GoodReads.token", token);
				tokens.putExtra("com.onesadjam.GoodReads.tokenSecret", tokenSecret);
				tokens.putExtra("com.onesadjam.GoodReads.userId", userId);
				setResult(RESULT_OK, tokens);
				finish();
			}
		}
	}
}
