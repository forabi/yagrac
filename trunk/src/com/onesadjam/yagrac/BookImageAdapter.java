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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class BookImageAdapter extends BaseAdapter
{
	private static final int BOOK_IMAGE_HEIGHT = 80;
	private static final int BOOK_IMAGE_WIDTH = 60;

	private List<Review> _Reviews = new ArrayList<Review>();
	private Context _Context;
	
	public BookImageAdapter(Context c)
	{
		_Context = c;
	}
	
	public void AddBook(Review book)
	{
		_Reviews.add(book);
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
		ImageView bookImage = new ImageView(_Context);
		bookImage.setScaleType(ScaleType.FIT_CENTER);
		bookImage.setMinimumHeight((int)(BOOK_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
		bookImage.setMinimumWidth((int)(BOOK_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));
		try
		{
			bookImage = LazyImageLoader.LazyLoadImageView(
					_Context, 
					new URL(_Reviews.get(position).get_Book().get_SmallImageUrl()), 
					R.drawable.nocover,
					bookImage);
		}
		catch (MalformedURLException e)
		{
			Toast.makeText(_Context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return bookImage;
	}
}

