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

import com.onesadjam.yagrac.xml.Comment;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class CommentListItemAdapter extends BaseAdapter
{
	private static final int BOOK_IMAGE_HEIGHT = 80;
	private static final int BOOK_IMAGE_WIDTH = 60;

	private List<Comment> _Comments = new ArrayList<Comment>();
	private Context _Context;
	
	private ILastItemRequestedListener _LastItemRequestedListener = null;
	
	public CommentListItemAdapter(Context context)
	{
		_Context = context;
	}
	
	public void add(Comment comment)
	{
		_Comments.add(comment);
	}
	
	public void clear()
	{
		_Comments.clear();
	}
	
	@Override
	public int getCount()
	{
		return _Comments.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _Comments.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout commentItemView;

		if (convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) _Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			commentItemView = (LinearLayout)layoutInflater.inflate(R.layout.commentlistitem, parent, false);
		}
		else
		{
			commentItemView = (LinearLayout)convertView;
		}
		
		ImageView userImage = (ImageView)commentItemView.findViewById(R.id._CommentListItem_UserImageView);
		userImage.setScaleType(ScaleType.FIT_CENTER);
		userImage.setMinimumHeight((int)(BOOK_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
		userImage.setMinimumWidth((int)(BOOK_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));

		Comment comment = _Comments.get(position);
		
		if (comment.get_User().get_ImageUrl() != null && comment.get_User().get_ImageUrl() != "")
		{
			try
			{
				LazyImageLoader.LazyLoadImageView(
						_Context, 
						new URL(comment.get_User().get_ImageUrl()), 
						R.drawable.nocover,
						userImage);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		
		TextView usernameText = (TextView)commentItemView.findViewById(R.id._CommentListItem_UsernameTextView);
		usernameText.setText(comment.get_User().get_Name());
		
		TextView bodyText = (TextView)commentItemView.findViewById(R.id._CommentListItem_Body);
		bodyText.setText(Html.fromHtml(comment.get_Body()));
		
		TextView timestampText = (TextView)commentItemView.findViewById(R.id._CommentListItem_Timestamp);
		timestampText.setText(comment.get_UpdatedAt());
		
		if (position == _Comments.size() - 1 && _LastItemRequestedListener != null)
		{
			_LastItemRequestedListener.onLastItemRequest(this, position);
		}
		
		return commentItemView;
	}
	
	public void setOnLastItemRequestListener(ILastItemRequestedListener lastItemRequestedListener)
	{
		_LastItemRequestedListener = lastItemRequestedListener;
	}
}
