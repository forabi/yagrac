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

public class Author
{
	private int _Id;
	private String _Name;
	private String _ImageUrl;
	private String _SmallImageUrl;
	private String _Link;
	private float _AverageRating;
	private int _RatingsCount;
	private int _TextReviewsCount;
	
	public Author copy()
	{
		Author authorCopy = new Author();
		
		authorCopy.set_AverageRating(this.get_AverageRating());
		authorCopy.set_Id(this.get_Id());
		authorCopy.set_ImageUrl(this.get_ImageUrl());
		authorCopy.set_Link(this.get_Link());
		authorCopy.set_Name(this.get_Name());
		authorCopy.set_RatingsCount(this.get_RatingsCount());
		authorCopy.set_SmallImageUrl(this.get_SmallImageUrl());
		authorCopy.set_TextReviewsCount(this.get_TextReviewsCount());

		return authorCopy;
	}

	public void clear()
	{
		this.set_AverageRating(0);
		this.set_Id(0);
		this.set_ImageUrl("");
		this.set_Link("");
		this.set_Name("");
		this.set_RatingsCount(0);
		this.set_SmallImageUrl("");
		this.set_TextReviewsCount(0);
	}
	
	public static Author appendSingletonListener(final Element parentElement)
	{
		final Author author = new Author();
		
		Element authorElement = parentElement.getChild("author");
		
		appendCommonListeners(authorElement, author);
		
		return author;
	}
	
	public static List<Author> appendArrayListener(final Element parentElement)
	{
		final List<Author> authors = new ArrayList<Author>();
		final Author author = new Author();
		
		Element authorElement = parentElement.getChild("author");
		
		authorElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				authors.add(author.copy());
				author.clear();
			}
		});
		
		appendCommonListeners(authorElement, author);
		
		return authors;
	}
	
	private static void appendCommonListeners(final Element authorElement, final Author author)
	{
		authorElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					author.set_Id(Integer.parseInt(body));
				}
			}
		});
		
		authorElement.getChild("name").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_Name(body);
			}
		});
		
		authorElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_ImageUrl(body);
			}
		});
		
		authorElement.getChild("small_image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_SmallImageUrl(body);
			}
		});
		
		authorElement.getChild("link").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_Link(body);
			}
		});
		
		authorElement.getChild("average_rating").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					author.set_AverageRating(Float.parseFloat(body));
				}
			}
		});
		
		authorElement.getChild("ratings_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					author.set_RatingsCount(Integer.parseInt(body));
				}
			}
		});
		
		authorElement.getChild("text_reviews_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					author.set_TextReviewsCount(Integer.parseInt(body));
				}
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
	public String get_Name()
	{
		return _Name;
	}
	public void set_Name(String _Name)
	{
		this._Name = _Name;
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
	public String get_Link()
	{
		return _Link;
	}
	public void set_Link(String _Link)
	{
		this._Link = _Link;
	}
	public float get_AverageRating()
	{
		return _AverageRating;
	}
	public void set_AverageRating(float _AverageRating)
	{
		this._AverageRating = _AverageRating;
	}
	public int get_RatingsCount()
	{
		return _RatingsCount;
	}
	public void set_RatingsCount(int _RatingsCount)
	{
		this._RatingsCount = _RatingsCount;
	}
	public int get_TextReviewsCount()
	{
		return _TextReviewsCount;
	}
	public void set_TextReviewsCount(int _TextReviewsCount)
	{
		this._TextReviewsCount = _TextReviewsCount;
	}
}
