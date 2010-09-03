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

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.StartElementListener;

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
	private int _BooksStart;
	private int _BooksEnd;
	private int _BooksTotal;
	private int _FansCount;
	private String _About;
	private String _Influences;
	private int _WorksCount;
	private String _Gender;
	private String _Hometown;
	private String _BornAt;
	private String _DiedAt;
	private String _UserId;
	private List<Book> _Books = new ArrayList<Book>();
	
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
		authorCopy.set_BooksStart(this.get_BooksStart());
		authorCopy.set_BooksEnd(this.get_BooksEnd());
		authorCopy.set_BooksTotal(this.get_BooksTotal());
		authorCopy.set_FansCount(this.get_FansCount());
		authorCopy.set_About(this.get_About());
		authorCopy.set_Influences(this.get_Influences());
		authorCopy.set_WorksCount(this.get_WorksCount());
		authorCopy.set_Gender(this.get_Gender());
		authorCopy.set_Hometown(this.get_Hometown());
		authorCopy.set_BornAt(this.get_BornAt());
		authorCopy.set_DiedAt(this.get_DiedAt());
		authorCopy.set_UserId(this.get_UserId());
		
		List<Book> booksCopy = new ArrayList<Book>();
		for (int i = 0; i < _Books.size(); i++)
		{
			booksCopy.add(_Books.get(i).copy());
		}
		authorCopy.set_Books(booksCopy);

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
		this.set_BooksEnd(0);
		this.set_BooksStart(0);
		this.set_BooksTotal(0);
		this.set_FansCount(0);
		this.set_About("");
		this.set_Influences("");
		this.set_WorksCount(0);
		this.set_Gender("");
		this.set_Hometown("");
		this.set_BornAt("");
		this.set_DiedAt("");
		this.set_UserId("");
		_Books.clear();
	}
	
	public static Author appendSingletonListener(final Element parentElement, int depth)
	{
		final Author author = new Author();
		
		Element authorElement = parentElement.getChild("author");
		
		appendCommonListeners(authorElement, author, depth);
		
		return author;
	}
	
	public static List<Author> appendArrayListener(final Element parentElement, int depth)
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
		
		appendCommonListeners(authorElement, author, depth);
		
		return authors;
	}
	
	private static void appendCommonListeners(final Element authorElement, final Author author, int depth)
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
		
		authorElement.getChild("fans_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					author.set_FansCount(Integer.parseInt(body));
				}
			}
		});

		authorElement.getChild("about").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_About(body);
			}
		});

		authorElement.getChild("influences").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_Influences(body);
			}
		});

		authorElement.getChild("works_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					author.set_WorksCount(Integer.parseInt(body));
				}
			}
		});

		authorElement.getChild("gender").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_Gender(body);
			}
		});

		authorElement.getChild("hometown").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_Hometown(body);
			}
		});

		authorElement.getChild("born_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_BornAt(body);
			}
		});

		authorElement.getChild("died_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_DiedAt(body);
			}
		});

		authorElement.getChild("user").getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				author.set_UserId(body);
			}
		});
		
		if (depth < 2 )
		{
			Element booksElement = authorElement.getChild("books");
			booksElement.setStartElementListener(new StartElementListener()
			{
				@Override
				public void start(Attributes attributes)
				{
					String value = attributes.getValue("start");
					if (value != null && value.length() != 0)
					{
						author.set_BooksStart(Integer.parseInt(value));
					}
					value = attributes.getValue("end");
					if (value != null && value.length() != 0)
					{
						author.set_BooksEnd(Integer.parseInt(value));
					}
					value = attributes.getValue("total");
					if (value != null && value.length() != 0)
					{
						author.set_BooksTotal(Integer.parseInt(value));
					}
				}
			});
			author.set_Books(Book.appendArrayListener(booksElement, depth + 1));
		}
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

	public int get_BooksStart()
	{
		return _BooksStart;
	}

	public void set_BooksStart(int _BooksStart)
	{
		this._BooksStart = _BooksStart;
	}

	public int get_BooksEnd()
	{
		return _BooksEnd;
	}

	public void set_BooksEnd(int _BooksEnd)
	{
		this._BooksEnd = _BooksEnd;
	}

	public int get_BooksTotal()
	{
		return _BooksTotal;
	}

	public void set_BooksTotal(int _BooksTotal)
	{
		this._BooksTotal = _BooksTotal;
	}

	public List<Book> get_Books()
	{
		return _Books;
	}

	public void set_Books(List<Book> _Books)
	{
		this._Books = _Books;
	}

	public int get_FansCount()
	{
		return _FansCount;
	}

	public void set_FansCount(int _FansCount)
	{
		this._FansCount = _FansCount;
	}

	public String get_About()
	{
		return _About;
	}

	public void set_About(String _About)
	{
		this._About = _About;
	}

	public String get_Influences()
	{
		return _Influences;
	}

	public void set_Influences(String _Influences)
	{
		this._Influences = _Influences;
	}

	public int get_WorksCount()
	{
		return _WorksCount;
	}

	public void set_WorksCount(int _WorksCount)
	{
		this._WorksCount = _WorksCount;
	}

	public String get_Gender()
	{
		return _Gender;
	}

	public void set_Gender(String _Gender)
	{
		this._Gender = _Gender;
	}

	public String get_Hometown()
	{
		return _Hometown;
	}

	public void set_Hometown(String _Hometown)
	{
		this._Hometown = _Hometown;
	}

	public String get_BornAt()
	{
		return _BornAt;
	}

	public void set_BornAt(String _BornAt)
	{
		this._BornAt = _BornAt;
	}

	public String get_DiedAt()
	{
		return _DiedAt;
	}

	public void set_DiedAt(String _DiedAt)
	{
		this._DiedAt = _DiedAt;
	}

	public String get_UserId()
	{
		return _UserId;
	}

	public void set_UserId(String _UserId)
	{
		this._UserId = _UserId;
	}
}
