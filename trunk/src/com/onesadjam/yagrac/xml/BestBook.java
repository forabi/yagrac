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

import android.sax.Element;
import android.sax.EndTextElementListener;

public class BestBook
{
	private int _Id;
	private String _Title;
	private Author _Author = new Author();
	private String _ImageUrl;
	private String _SmallImageUrl;
	
	public void clear()
	{
		this.set_Id(0);
		this.set_Title("");
		this.set_ImageUrl("");
		this.set_SmallImageUrl("");
		this._Author.clear();
	}
	
	public BestBook copy()
	{
		BestBook bestBookCopy = new BestBook();

		bestBookCopy.set_Id(this.get_Id());
		bestBookCopy.set_Title(this.get_Title());
		bestBookCopy.set_Author(this.get_Author().copy());
		bestBookCopy.set_ImageUrl(this.get_ImageUrl());
		bestBookCopy.set_SmallImageUrl(this.get_SmallImageUrl());

		return bestBookCopy;
	}
	
	public static BestBook appendSingletonListener(final Element parentElement, int depth)
	{
		final BestBook bestBook = new BestBook();
		
		Element bestBookElement = parentElement.getChild("best_book");
		
		bestBook.set_Author(Author.appendSingletonListener(bestBookElement, depth + 1));
		appendCommonListeners(bestBookElement, bestBook);
		
		return bestBook;
	}
	
//	public static List<BestBook> appendArrayListener(final Element parentElement)
//	{
//		final List<BestBook> bestBooks = new ArrayList<BestBook>();
//		
//		return bestBooks;
//	}
	
	private static void appendCommonListeners(final Element bestBookElement, final BestBook bestBook)
	{
		bestBookElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					bestBook.set_Id(Integer.parseInt(body));
				}
			}
		});

		bestBookElement.getChild("title").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				bestBook.set_Title(body);
			}
		});

		bestBookElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				bestBook.set_ImageUrl(body);
			}
		});

		bestBookElement.getChild("small_image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				bestBook.set_SmallImageUrl(body);
			}
		});
	}

	public int get_Id()
	{
		return _Id;
	}

	public void set_Id(int _Id)
	{
		this._Id = _Id;
	}

	public String get_Title()
	{
		return _Title;
	}

	public void set_Title(String _Title)
	{
		this._Title = _Title;
	}

	public Author get_Author()
	{
		return _Author;
	}

	public void set_Author(Author _Author)
	{
		this._Author = _Author;
	}

	public String get_ImageUrl()
	{
		return _ImageUrl;
	}

	public void set_ImageUrl(String _ImageUrl)
	{
		this._ImageUrl = _ImageUrl;
	}

	public String get_SmallImageUrl()
	{
		return _SmallImageUrl;
	}

	public void set_SmallImageUrl(String _SmallImageUrl)
	{
		this._SmallImageUrl = _SmallImageUrl;
	}
}
