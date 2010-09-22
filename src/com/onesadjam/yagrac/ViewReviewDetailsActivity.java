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
import com.onesadjam.yagrac.xml.Review;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ViewReviewDetailsActivity extends Activity
{
	private static final int BOOK_IMAGE_HEIGHT = 160;
	private static final int BOOK_IMAGE_WIDTH = 120;

	private String _ReviewId;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.viewreviewdetail);

		_ReviewId = getIntent().getExtras().getString("com.onesadjam.yagrac.ReviewId");
		
		try
		{
			Review reviewDetails = ResponseParser.GetReview(_ReviewId);
			
			ImageView userImage = (ImageView)findViewById(R.id._ViewReview_ReviewerImageView);
			userImage.setScaleType(ScaleType.FIT_CENTER);
			userImage.setMinimumHeight((int)(BOOK_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
			userImage.setMinimumWidth((int)(BOOK_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));
			LazyImageLoader.LazyLoadImageView(this, new URL(reviewDetails.get_User().get_ImageUrl()), R.drawable.nocover, userImage);

			ImageView bookImage = (ImageView)findViewById(R.id._ViewReview_BookImageView);
			bookImage.setScaleType(ScaleType.FIT_CENTER);
			bookImage.setMinimumHeight((int)(BOOK_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
			bookImage.setMinimumWidth((int)(BOOK_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));
			LazyImageLoader.LazyLoadImageView(this, new URL(reviewDetails.get_Book().get_ImageUrl()), R.drawable.nocover, bookImage);

			RatingBar ratingBar = (RatingBar)findViewById(R.id._ViewReview_Rating);
			ratingBar.setRating(reviewDetails.get_Rating());
			
			TextView reviewTitle = (TextView)findViewById(R.id._ViewReview_ReviewTitle);
			reviewTitle.setText(reviewDetails.get_User().get_Name() + " reviews " + reviewDetails.get_Book().get_Title());
			
			StringBuilder sb = new StringBuilder();
			sb.append(reviewDetails.get_Body());
			TextView textView = (TextView)findViewById(R.id._ViewReview_ReviewDetails);
			textView.setText(Html.fromHtml(sb.toString()));
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
