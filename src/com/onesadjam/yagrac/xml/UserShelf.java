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

package com.onesadjam.yagrac.xml;

import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;

public class UserShelf
{
	private int _BookCount;
	private String _Description;
	private String _Name;
	
	public UserShelf copy()
	{
		UserShelf shelfCopy = new UserShelf();
		shelfCopy.set_BookCount(this.get_BookCount());
		shelfCopy.set_Description(this.get_Description());
		shelfCopy.set_Name(this.get_Name());
		return shelfCopy;
	}
	
	public void clear()
	{
		this.set_BookCount(0);
		this.set_Description("");
		this.set_Name("");
	}
	
	public static UserShelf appendSingletonListener(Element parentElement, int depth)
	{
		final UserShelf userShelf = new UserShelf();
		final Element userShelfElement = parentElement.getChild("user_shelf");
		
		appendCommonListeners(userShelfElement, userShelf, depth);
		
		return userShelf;
	}
	
	public static List<UserShelf> appendArrayListener(Element parentElement, int depth)
	{
		final List<UserShelf> userShelfList = new ArrayList<UserShelf>();
		final UserShelf userShelf = new UserShelf();
		final Element userShelfElement = parentElement.getChild("user_shelf");

		appendCommonListeners(userShelfElement, userShelf, depth);

		userShelfElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				userShelfList.add(userShelf.copy());
				userShelf.clear();
			}
		});

		return userShelfList;
	}
	
	private static void appendCommonListeners(final Element userShelfElement, final UserShelf userShelf, int depth)
	{
		userShelfElement.getChild("book_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				userShelf.set_BookCount(Integer.parseInt(body));
			}
		});
		
		userShelfElement.getChild("name").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				userShelf.set_Name(body);
			}
		});
		
		userShelfElement.getChild("description").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				userShelf.set_Description(body);
			}
		});
	}

	public int get_BookCount()
	{
		return _BookCount;
	}
	public void set_BookCount(int _BookCount)
	{
		this._BookCount = _BookCount;
	}
	public String get_Description()
	{
		return _Description;
	}
	public void set_Description(String _Description)
	{
		this._Description = _Description;
	}
	public String get_Name()
	{
		return _Name;
	}
	public void set_Name(String _Name)
	{
		this._Name = _Name;
	}
}
