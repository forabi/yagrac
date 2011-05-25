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

import com.onesadjam.yagrac.xml.Book;
import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.Review;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ViewBookReviewsActivity extends Activity
{
	private String _AuthenticatedUserId;
	private Context _Context = this;
	private ProgressDialog _BusySpinner;
	private String _BookId;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent launchingIntent = this.getIntent();
		_BookId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.BookId");
		_AuthenticatedUserId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

		_BusySpinner = ProgressDialog.show(this, "", "Retrieving book reviews...");
		
		new GetBookReviewsTask().execute(_BookId);
	}
	
	private class GetBookReviewsTask extends AsyncTask<String, Void, Book>
	{
		@Override
		protected Book doInBackground(String... params)
		{
			Book bookDetails = null;
			if (params != null && params.length > 0)
			{
				try
				{
					bookDetails = ResponseParser.GetReviewsForBook(params[0]);
				}
				catch (Exception e){}
			}
			return bookDetails;
		}

		@Override
		protected void onPostExecute(Book reviewsForBook)
		{
			try
			{
				ListView listview = new ListView(_Context);
				ReviewAdapter reviewAdapter = new ReviewAdapter(_Context);
				setContentView(listview);
				
				for ( int i = 0; i < reviewsForBook.get_Reviews().get_Reviews().size(); i++ )
				{
					reviewAdapter.add(reviewsForBook.get_Reviews().get_Reviews().get(i));
				}
				
				listview.setAdapter(reviewAdapter);
				
				listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						Review clickedReview = (Review)arg0.getAdapter().getItem(arg2);
						Intent viewReviewIntent = new Intent(arg1.getContext(), ViewReviewActivity.class);
						viewReviewIntent.putExtra("com.onesadjam.yagrac.ReviewId", clickedReview.get_Id());
						viewReviewIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
						arg1.getContext().startActivity(viewReviewIntent);				
					}
				});
			}
			catch (Exception e)
			{
				Toast.makeText(_Context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			finally
			{
				try
				{
					_BusySpinner.dismiss();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
