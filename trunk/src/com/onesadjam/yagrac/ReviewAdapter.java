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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.onesadjam.yagrac.xml.Review;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class ReviewAdapter extends BaseAdapter
{
	private static final int BOOK_IMAGE_HEIGHT = 80;
	private static final int BOOK_IMAGE_WIDTH = 60;
	private List<Review> _Reviews = new ArrayList<Review>();
	private Context _Context;
	
	public ReviewAdapter(Context context)
	{
		_Context = context;
	}
	
	public void add(Review review)
	{
		_Reviews.add(review);
	}
	
	public void clear()
	{
		_Reviews.clear();
	}

	@Override
	public int getCount()
	{
		return _Reviews.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _Reviews.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Review review = _Reviews.get(position);
		
		LinearLayout listItem = new LinearLayout(_Context);
		LinearLayout reviewer = new LinearLayout(_Context);
		reviewer.setOrientation(LinearLayout.VERTICAL);
		listItem.addView(reviewer);
		ImageView reviewImage = new ImageView(_Context);
		reviewImage.setScaleType(ScaleType.FIT_CENTER);
		reviewImage.setMinimumHeight((int)(BOOK_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
		reviewImage.setMinimumWidth((int)(BOOK_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));

		try
		{
			if (review.get_User().get_SmallImageUrl() != null && review.get_User().get_SmallImageUrl() != "")
			{
				reviewImage = LazyImageLoader.LazyLoadImageView(
						_Context, 
						new URL(review.get_User().get_SmallImageUrl()), 
						R.drawable.nophoto_unisex,
						reviewImage);
			}
			else
			{
				reviewImage = new ImageView(_Context);
				reviewImage.setImageResource(R.drawable.nocover);
			}
			reviewer.addView(reviewImage);
			
			LinearLayout bodyLayout = new LinearLayout(_Context);
			
			bodyLayout.setOrientation(LinearLayout.VERTICAL);
			listItem.addView(bodyLayout);
			
			LayoutInflater layoutInflater = (LayoutInflater) _Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RatingBar rating = (RatingBar) layoutInflater.inflate(R.layout.viewbookreviewsrow, parent, false);
			rating.setIsIndicator(true);
			rating.setNumStars(5);
			rating.setStepSize(1);
			rating.setRating(review.get_Rating());
			reviewer.addView(rating);
			
			TextView text = new TextView(_Context);
			text.setText(Html.fromHtml(review.get_Body()));
			text.setMovementMethod(LinkMovementMethod.getInstance());
			bodyLayout.addView(text);
			
			text = new TextView(_Context);
			text.setText("Updated:\t" + review.get_DateUpdated());
			bodyLayout.addView(text);
			
			text = new TextView(_Context);
			text.setText("Comments:\t" + review.get_CommentsCount());
			bodyLayout.addView(text);
			
			text = new TextView(_Context);
			text.setText("Votes:\t" + review.get_Votes());
			bodyLayout.addView(text);
			
			text = new TextView(_Context);
			text.setText(review.get_User().get_Name());
			reviewer.addView(text);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return listItem;
	}
}
