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

import com.onesadjam.yagrac.xml.User;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class SocialAdapter extends BaseAdapter
{
	private static final int CONTACT_IMAGE_HEIGHT = 80;
	private static final int CONTACT_IMAGE_WIDTH = 60;

	private List<User> _Users = new ArrayList<User>();
	private Context _Context;
	private ILastItemRequestedListener _LastItemRequestedListener = null;

	public SocialAdapter(Context c)
	{
		_Context = c;
	}
	
	public void AddContact(User user)
	{
		_Users.add(user);
	}
	
	public void clear()
	{
		_Users.clear();
	}
	
	@Override
	public int getCount()
	{
		return _Users.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _Users.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout layout = new LinearLayout(_Context);
		ImageView contactImage = new ImageView(_Context);
		contactImage.setScaleType(ScaleType.FIT_CENTER);
		contactImage.setMinimumHeight((int)(CONTACT_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
		contactImage.setMinimumWidth((int)(CONTACT_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));
		try
		{
			LazyImageLoader.LazyLoadImageView(
					_Context, 
					new URL(_Users.get(position).get_SmallImageUrl()), 
					R.drawable.nophoto_unisex,
					contactImage);
			layout.addView(contactImage);
			TextView contactText = new TextView(_Context);
			contactText.setText(_Users.get(position).get_Name());
			layout.addView(contactText);
			
			if (position == _Users.size() - 1 && _LastItemRequestedListener != null)
			{
				_LastItemRequestedListener.onLastItemRequest(this, position);
			}
		}
		catch (MalformedURLException e)
		{
			Toast.makeText(_Context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return layout;
	}

	public void setLastItemRequestedListener(ILastItemRequestedListener lastItemRequestedListener)
	{
		_LastItemRequestedListener = lastItemRequestedListener;
	}
}
