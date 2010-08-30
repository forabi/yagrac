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

import com.onesadjam.yagrac.xml.BestBook;
import com.onesadjam.yagrac.xml.Work;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchResultAdapter extends BaseAdapter
{
	private Context _Context;
	private List<Work> _Results = new ArrayList<Work>();
	
	public SearchResultAdapter(Context context)
	{
		_Context = context;
	}
	
	public void addResult(Work work)
	{
		_Results.add(work);
	}
	
	public void clear()
	{
		_Results.clear();
	}
	
	@Override
	public int getCount()
	{
		return _Results.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _Results.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout listItem = new LinearLayout(_Context);
		
		ImageView bookImage;
		try
		{
			BestBook bestBook = _Results.get(position).get_BestBook();
			
			if (bestBook.get_SmallImageUrl() != null && bestBook.get_SmallImageUrl() != "")
			{
				bookImage = LazyImageLoader.LazyLoadImageView(
						_Context, 
						new URL(bestBook.get_SmallImageUrl()), 
						R.drawable.nocover,
						null);
			}
			else
			{
				bookImage = new ImageView(_Context);
				bookImage.setImageResource(R.drawable.nocover);
			}
			listItem.addView(bookImage);
			
			TextView text = new TextView(_Context);
			if (bestBook.get_Author() != null && bestBook.get_Author().get_Name() != null)
			{
				text.setText(bestBook.get_Title() + "\n by " + bestBook.get_Author().get_Name());
			}
			else
			{
				text.setText(bestBook.get_Title());
			}
						
			listItem.addView(text);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return listItem;
	}
}
