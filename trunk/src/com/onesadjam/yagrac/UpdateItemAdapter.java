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

import com.onesadjam.yagrac.xml.Update;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UpdateItemAdapter extends BaseAdapter
{
	private List<Update> _Updates = new ArrayList<Update>();
	private Context _Context;
	
	public UpdateItemAdapter(Context context)
	{
		_Context = context;
	}
	
	public void add(Update update)
	{
		_Updates.add(update);
	}
	
	public void clear()
	{
		_Updates.clear();
	}
	
	@Override
	public int getCount()
	{
		return _Updates.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _Updates.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout updateItemView = new LinearLayout(_Context);
		
		Update update = _Updates.get(position);
		
		if (update.get_ImageUrl() != null && update.get_ImageUrl() != "")
		{
			try
			{
				updateItemView.addView(
					LazyImageLoader.LazyLoadImageView(
						_Context, 
						new URL(update.get_ImageUrl()), 
						R.drawable.nophoto_unisex,
						null));
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		
		LinearLayout body = new LinearLayout(_Context);
		body.setOrientation(LinearLayout.VERTICAL);
		
		updateItemView.addView(body);
		
		TextView actionText = new TextView(_Context);
		String actorLinkString = "<a href=\"" + update.get_Actor().get_Link() + "\">" + update.get_Actor().get_Name() + "</a> ";
		actionText.setText(Html.fromHtml(actorLinkString + update.get_ActionText()));
		actionText.setMovementMethod(LinkMovementMethod.getInstance());
		body.addView(actionText);
		
		TextView timestampText = new TextView(_Context);
		timestampText.setText(update.get_UpdatedAt());
		
		body.addView(timestampText);
		
		return updateItemView;
	}

}
