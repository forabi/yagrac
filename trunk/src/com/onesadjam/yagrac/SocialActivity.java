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

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class SocialActivity extends TabActivity
{
	private static final int TAB_HEIGHT = 33;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.friends);
		
		TabHost tabs = getTabHost();
	    TabHost.TabSpec spec;
	    
		Intent launchingIntent = this.getIntent();
		String userId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.UserId");
		String authenticatedUserId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

	    Intent intent = new Intent().setClass(this, FriendsActivity.class);
	    intent.putExtra("com.onesadjam.yagrac.UserId", userId);
	    intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", authenticatedUserId);
	    spec = tabs.newTabSpec("friends").setIndicator("Friends").setContent(intent);
	    tabs.addTab(spec);

	    intent = new Intent().setClass(this, FollowingActivity.class);
	    intent.putExtra("com.onesadjam.yagrac.UserId", userId);
	    intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", authenticatedUserId);
	    spec = tabs.newTabSpec("Following").setIndicator("Following").setContent(intent);
	    tabs.addTab(spec);

	    intent = new Intent().setClass(this, FollowersActivity.class);
	    intent.putExtra("com.onesadjam.yagrac.UserId", userId);
	    intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", authenticatedUserId);
	    spec = tabs.newTabSpec("followers").setIndicator("Followers").setContent(intent);
	    tabs.addTab(spec);

	    tabs.setCurrentTab(0);

	    final TabHost tabHost = getTabHost();
	    tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = (int)(TAB_HEIGHT * HomeActivity.get_ScalingFactor()); 
	    tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = (int)(TAB_HEIGHT * HomeActivity.get_ScalingFactor()); 
	    tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = (int)(TAB_HEIGHT * HomeActivity.get_ScalingFactor()); 
	}
}
