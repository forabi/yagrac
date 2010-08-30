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
import android.sax.EndTextElementListener;

public class Book
{
	private String _Id;
	private String _Isbn;
	private String _Isbn13;
	private int _TextReviewsCount;
	private String _Title;
	private String _ImageUrl;
	private String _SmallImageUrl;
	private String _Link;
	private int _Pages;
	private float _AverageRating;
	private int _RatingsCount;
	private String _Description;
	private List<Author> _Authors = new ArrayList<Author>();
	private int _YearPublished;
	
	public Book copy()
	{
		Book bookCopy = new Book();
		
		List<Author> authorsCopy = new ArrayList<Author>();
		for (int i = 0; i < this._Authors.size(); i++ )
		{
			authorsCopy.add(this._Authors.get(i).copy());
		}
		bookCopy.set_Authors(authorsCopy);
		
		bookCopy.set_AverageRating(this.get_AverageRating());
		bookCopy.set_Description(this.get_Description());
		bookCopy.set_Id(this.get_Id());
		bookCopy.set_ImageUrl(this.get_ImageUrl());
		bookCopy.set_Isbn(this.get_Isbn());
		bookCopy.set_Isbn13(this.get_Isbn13());
		bookCopy.set_Link(this.get_Link());
		bookCopy.set_Pages(this.get_Pages());
		bookCopy.set_RatingsCount(this.get_RatingsCount());
		bookCopy.set_SmallImageUrl(this.get_SmallImageUrl());
		bookCopy.set_TextReviewsCount(this.get_TextReviewsCount());
		bookCopy.set_Title(this.get_Title());
		bookCopy.set_YearPublished(this.get_YearPublished());
		
		return bookCopy;
	}
	
	public void clear()
	{
		this._Authors.clear();;
		this.set_AverageRating(0);
		this.set_Description("");
		this.set_Id("");
		this.set_ImageUrl("");
		this.set_Isbn("");
		this.set_Isbn13("");
		this.set_Link("");
		this.set_Pages(0);
		this.set_RatingsCount(0);
		this.set_SmallImageUrl("");
		this.set_TextReviewsCount(0);
		this.set_Title("");
		this.set_YearPublished(0);
	}

	public static Book appendSingletonListener(Element parentElement)
	{
		final Book book = new Book();
		final Element bookElement = parentElement.getChild("book");
		
		bookElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					book.set_Id(body);
				}
			}
		});
		
		bookElement.getChild("isbn").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				book.set_Isbn(body);
			}
		});
		
		bookElement.getChild("isbn13").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				book.set_Isbn13(body);
			}
		});
		
		bookElement.getChild("text_reviews_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					book.set_TextReviewsCount(Integer.parseInt(body));
				}
			}
		});
		
		bookElement.getChild("title").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				book.set_Title(body);
			}
		});
		
		bookElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				book.set_ImageUrl(body);
			}
		});
		
		bookElement.getChild("small_image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				book.set_SmallImageUrl(body);
			}
		});
		
		bookElement.getChild("link").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				book.set_Link(body);
			}
		});
		
		bookElement.getChild("num_pages").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					book.set_Pages(Integer.parseInt(body));
				}
			}
		});
		
		bookElement.getChild("average_rating").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					book.set_AverageRating(Float.parseFloat(body));
				}
			}
		});
		
		bookElement.getChild("ratings_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					book.set_RatingsCount(Integer.parseInt(body));
				}
			}
		});
		
		bookElement.getChild("description").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				book.set_Description(body);
			}
		});
		
		bookElement.getChild("published").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					book.set_YearPublished(Integer.parseInt(body));
				}
			}
		});
		
		Element authorsElement = bookElement.getChild("authors"); 
		book.set_Authors(Author.appendArrayListener(authorsElement));

		return book;
	}
	
	public String get_Id()
	{
		return _Id;
	}
	public void set_Id(String _Id)
	{
		this._Id = _Id;
	}
	public String get_Isbn()
	{
		return _Isbn;
	}
	public void set_Isbn(String _Isbn)
	{
		this._Isbn = _Isbn;
	}
	public String get_Isbn13()
	{
		return _Isbn13;
	}
	public void set_Isbn13(String _Isbn13)
	{
		this._Isbn13 = _Isbn13;
	}
	public int get_TextReviewsCount()
	{
		return _TextReviewsCount;
	}
	public void set_TextReviewsCount(int _TextReviewsCount)
	{
		this._TextReviewsCount = _TextReviewsCount;
	}
	public String get_Title()
	{
		return _Title;
	}
	public void set_Title(String _Title)
	{
		this._Title = _Title;
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
	public int get_Pages()
	{
		return _Pages;
	}
	public void set_Pages(int _Pages)
	{
		this._Pages = _Pages;
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
	public String get_Description()
	{
		return _Description;
	}
	public void set_Description(String _Description)
	{
		this._Description = _Description;
	}
	public List<Author> get_Authors()
	{
		return _Authors;
	}
	public void set_Authors(List<Author> _Authors)
	{
		this._Authors = _Authors;
	}
	public int get_YearPublished()
	{
		return _YearPublished;
	}
	public void set_YearPublished(int _YearPublished)
	{
		this._YearPublished = _YearPublished;
	}
}
