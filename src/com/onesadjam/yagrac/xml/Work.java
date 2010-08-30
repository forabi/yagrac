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

public class Work
{
	private int _BooksCount;
	private int _Id;
	private int _OriginalPublicationDay;
	private int _OriginalPublicationMonth;
	private int _OriginalPublicationYear;
	private int _RatingsCount;
	private int _ReviewsCount;
	private float _AverageRating;
	private BestBook _BestBook = new BestBook();
	
	public void clear()
	{
		this.set_BooksCount(0);
		this.set_Id(0);
		this.set_OriginalPublicationDay(0);
		this.set_OriginalPublicationMonth(0);
		this.set_OriginalPublicationYear(0);
		this.set_RatingsCount(0);
		this.set_ReviewsCount(0);
		this.set_AverageRating(0);
		this._BestBook.clear();
	}
	
	public Work copy()
	{
		Work workCopy = new Work();

		workCopy.set_BooksCount(this.get_BooksCount());
		workCopy.set_Id(this.get_Id());
		workCopy.set_OriginalPublicationDay(this.get_OriginalPublicationDay());
		workCopy.set_OriginalPublicationMonth(this.get_OriginalPublicationMonth());
		workCopy.set_OriginalPublicationYear(this.get_OriginalPublicationYear());
		workCopy.set_RatingsCount(this.get_RatingsCount());
		workCopy.set_ReviewsCount(this.get_ReviewsCount());
		workCopy.set_AverageRating(this.get_AverageRating());
		workCopy.set_BestBook(this.get_BestBook().copy());

		return workCopy;
	}
	
	public static Work appendSingletonListener(final Element parentElement)
	{
		final Work work = new Work();
		final Element workElement = parentElement.getChild("work");
		
		appendCommonListeners(workElement, work);
		
		return work;
	}
	
	public static List<Work> appendArrayListener(final Element parentElement)
	{
		final List<Work> works = new ArrayList<Work>();
		final Work work = new Work();
		final Element workElement = parentElement.getChild("work");
		
		appendCommonListeners(workElement, work);
		
		workElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				works.add(work.copy());
				work.clear();
			}
		});
		return works;
	}
	
	private static void appendCommonListeners(final Element workElement, final Work work)
	{
		work.set_BestBook(BestBook.appendSingletonListener(workElement));

		workElement.getChild("books_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_BooksCount(Integer.parseInt(body));
				}
			}
		});

		workElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_Id(Integer.parseInt(body));
				}
			}
		});

		workElement.getChild("original_publication_day").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_OriginalPublicationDay(Integer.parseInt(body));
				}
			}
		});

		workElement.getChild("original_publication_month").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_OriginalPublicationMonth(Integer.parseInt(body));
				}
			}
		});

		workElement.getChild("original_publication_year").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_OriginalPublicationYear(Integer.parseInt(body));
				}
			}
		});

		workElement.getChild("ratings_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_RatingsCount(Integer.parseInt(body));
				}
			}
		});

		workElement.getChild("text_reviews_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_ReviewsCount(Integer.parseInt(body));
				}
			}
		});

		workElement.getChild("average_rating").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					work.set_AverageRating(Float.parseFloat(body));
				}
			}
		});
	}

	public int get_BooksCount()
	{
		return _BooksCount;
	}

	public void set_BooksCount(int _BooksCount)
	{
		this._BooksCount = _BooksCount;
	}

	public int get_Id()
	{
		return _Id;
	}

	public void set_Id(int _Id)
	{
		this._Id = _Id;
	}

	public int get_OriginalPublicationDay()
	{
		return _OriginalPublicationDay;
	}

	public void set_OriginalPublicationDay(int _OriginalPublicationDay)
	{
		this._OriginalPublicationDay = _OriginalPublicationDay;
	}

	public int get_OriginalPublicationMonth()
	{
		return _OriginalPublicationMonth;
	}

	public void set_OriginalPublicationMonth(int _OriginalPublicationMonth)
	{
		this._OriginalPublicationMonth = _OriginalPublicationMonth;
	}

	public int get_OriginalPublicationYear()
	{
		return _OriginalPublicationYear;
	}

	public void set_OriginalPublicationYear(int _OriginalPublicationYear)
	{
		this._OriginalPublicationYear = _OriginalPublicationYear;
	}

	public int get_RatingsCount()
	{
		return _RatingsCount;
	}

	public void set_RatingsCount(int _RatingsCount)
	{
		this._RatingsCount = _RatingsCount;
	}

	public int get_ReviewsCount()
	{
		return _ReviewsCount;
	}

	public void set_ReviewsCount(int _ReviewsCount)
	{
		this._ReviewsCount = _ReviewsCount;
	}

	public float get_AverageRating()
	{
		return _AverageRating;
	}

	public void set_AverageRating(float _AverageRating)
	{
		this._AverageRating = _AverageRating;
	}

	public void set_BestBook(BestBook _BestBook)
	{
		this._BestBook = _BestBook;
	}

	public BestBook get_BestBook()
	{
		return _BestBook;
	}
}
