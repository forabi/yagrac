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

import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.Review;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

public class ViewReviewCommentsActivity extends Activity implements ILastItemRequestedListener
{
	private String _ReviewId;
	private int _TotalComments;
	private int _ItemsLoaded;
	private int _PageSize;
	private boolean _LoadingNextPage;
	private CommentListItemAdapter _CommentsAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		ListView listview = new ListView(this);
		_CommentsAdapter = new CommentListItemAdapter(this);
		setContentView(listview);
		
		Intent launchingIntent = this.getIntent();
		_ReviewId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.ReviewId");

		try
		{
			Review review = ResponseParser.GetReview(_ReviewId);
			
			_TotalComments = review.get_Comments().get_Total();
			_ItemsLoaded = review.get_Comments().get_End();
			_PageSize = _ItemsLoaded;
			
			for ( int i = 0; i < review.get_Comments().get_Comments().size(); i++ )
			{
				_CommentsAdapter.add(review.get_Comments().get_Comments().get(i));
			}
			
			listview.setAdapter(_CommentsAdapter);
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
		if (_ItemsLoaded == _TotalComments)
		{
			_LoadingNextPage = false;
			return;
		}

		final Handler commentsLoadedHandler = new Handler() 
		{
    		@Override
    		public void handleMessage(Message message) 
    		{
    			Review review = (Review)message.obj;
    			for (int i = 0; i < review.get_Comments().get_Comments().size(); i++)
    			{
    				_CommentsAdapter.add(review.get_Comments().get_Comments().get(i));
    			}
    			_CommentsAdapter.notifyDataSetChanged();
    			_ItemsLoaded = review.get_Comments().get_End();
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
					Review review = ResponseParser.GetReview(_ReviewId, (_ItemsLoaded / _PageSize) + 1);
					Message message = commentsLoadedHandler.obtainMessage(1, review);
					commentsLoadedHandler.sendMessage(message);	
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
