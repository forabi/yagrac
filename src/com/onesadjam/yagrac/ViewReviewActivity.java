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

public class ViewReviewActivity extends TabActivity
{
	private String _ReviewId;
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
		_ReviewId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.ReviewId");
		_AuthenticatedUserId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

	    Intent intent = new Intent().setClass(this, ViewReviewDetailsActivity.class);
	    intent.putExtra("com.onesadjam.yagrac.ReviewId", _ReviewId);
	    intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
	    spec = tabs.newTabSpec("Review").setIndicator("Review").setContent(intent);
	    tabs.addTab(spec);

	    intent = new Intent().setClass(this, ViewReviewCommentsActivity.class);
	    intent.putExtra("com.onesadjam.yagrac.ReviewId", _ReviewId);
	    intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
	    spec = tabs.newTabSpec("Comments").setIndicator("Comments").setContent(intent);
	    tabs.addTab(spec);

	    tabs.setCurrentTab(0);

	    final TabHost tabHost = getTabHost();
	    tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = (int)(TAB_HEIGHT * HomeActivity.get_ScalingFactor()); 
	    tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = (int)(TAB_HEIGHT * HomeActivity.get_ScalingFactor()); 
	}
}
