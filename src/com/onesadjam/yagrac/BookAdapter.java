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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onesadjam.yagrac.xml.Book;

public class BookAdapter extends BaseAdapter
{
	private Context _Context;
	private List<Book> _Books = new ArrayList<Book>();
	
	public BookAdapter(Context context)
	{
		_Context = context;
	}
	
	public void add(Book work)
	{
		_Books.add(work);
	}
	
	public void clear()
	{
		_Books.clear();
	}
	
	@Override
	public int getCount()
	{
		return _Books.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _Books.get(position);
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
			Book book = _Books.get(position);
			
			if (book.get_SmallImageUrl() != null && book.get_SmallImageUrl() != "")
			{
				bookImage = LazyImageLoader.LazyLoadImageView(
						_Context, 
						new URL(book.get_SmallImageUrl()), 
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
			if (book.get_Authors() != null && book.get_Authors().size() != 0)
			{
				text.setText(book.get_Title() + "\n by " + book.get_Authors().get(0).get_Name());
			}
			else
			{
				text.setText(book.get_Title());
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
