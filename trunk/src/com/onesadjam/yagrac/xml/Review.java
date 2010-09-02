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

public class Review
{
	private String _Id;
	private Book _Book = new Book();
	private int _Rating;
	private int _Votes;
	private boolean _SpoilerFlag;
	private String _RecommendedFor;
	private String _RecommendedBy;
	private String _StartedAt;
	private String _ReadAt;
	private String _DateAdded;
	private String _DateUpdated;
	private String _Body;
	private String _Url;
	private String _Link;
	private List<String> _Shelves = new ArrayList<String>();
	
	public Review copy()
	{
		Review reviewCopy = new Review();
		
		reviewCopy.set_Body(this.get_Body());
		reviewCopy.set_Book(this.get_Book().copy());
		reviewCopy.set_DateAdded(this.get_DateAdded());
		reviewCopy.set_DateUpdated(this.get_DateUpdated());
		reviewCopy.set_Id(this.get_Id());
		reviewCopy.set_Link(this.get_Link());
		reviewCopy.set_Rating(this.get_Rating());
		reviewCopy.set_ReadAt(this.get_ReadAt());
		reviewCopy.set_RecommendedBy(this.get_RecommendedBy());
		reviewCopy.set_RecommendedFor(this.get_RecommendedFor());
		reviewCopy.set_SpoilerFlag(this.get_SpoilerFlag());
		reviewCopy.set_StartedAt(this.get_StartedAt());
		reviewCopy.set_Url(this.get_Url());
		reviewCopy.set_Votes(this.get_Votes());
		
		List<String> shelvesCopy = new ArrayList<String>();
		for (int i = 0; i < _Shelves.size(); i++ )
		{
			shelvesCopy.add(_Shelves.get(i));
		}
		reviewCopy.set_Shelves(shelvesCopy);
		
		return reviewCopy;
	}
	
	public void clear()
	{
		this.set_Body("");
		this.get_Book().clear();
		this.set_DateAdded("");
		this.set_DateUpdated("");
		this.set_Id("");
		this.set_Link("");
		this.set_Rating(0);
		this.set_ReadAt("");
		this.set_RecommendedBy("");
		this.set_RecommendedFor("");
		this.set_SpoilerFlag(false);
		this.set_StartedAt("");
		this.set_Url("");
		this.set_Votes(0);	
		this._Shelves.clear();
	}
	
	public static Review appendSingletonListener(final Element parentElement)
	{
		final Element reviewElement = parentElement.getChild("review");
		final Review review = new Review();
		
		appendCommonListeners(reviewElement, review);
		
		return review;
	}
	
	public static List<Review> appendArrayListener(final Element parentElement)
	{
		final Element reviewElement = parentElement.getChild("review");
		final List<Review> reviewList = new ArrayList<Review>();
		final Review review = new Review();
		
		appendCommonListeners(reviewElement, review);
		
		reviewElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				reviewList.add(review.copy());
				review.clear();
			}
		});
		
		return reviewList;
	}
	
	private static void appendCommonListeners(final Element reviewElement, final Review review)
	{
		reviewElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_Id(body);
			}
		});
		
		reviewElement.getChild("rating").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_Rating(Integer.parseInt(body));
			}
		});
		
		reviewElement.getChild("votes").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_Votes(Integer.parseInt(body));
			}
		});
		
		reviewElement.getChild("spoiler_flag").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_SpoilerFlag(Boolean.parseBoolean(body));
			}
		});
		
		reviewElement.getChild("recommended_for").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_RecommendedFor(body);
			}
		});
		
		reviewElement.getChild("recommended_by").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_RecommendedBy(body);
			}
		});
		
		reviewElement.getChild("started_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_StartedAt(body);
			}
		});
		
		reviewElement.getChild("read_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_ReadAt(body);
			}
		});
		
		reviewElement.getChild("date_added").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_DateAdded(body);
			}
		});
		
		reviewElement.getChild("date_updated").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_DateUpdated(body);
			}
		});
		
		reviewElement.getChild("body").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_Body(body);
			}
		});
		
		reviewElement.getChild("url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_Url(body);
			}
		});
		
		reviewElement.getChild("link").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				review.set_Link(body);
			}
		});
		
		review.set_Book(Book.appendSingletonListener(reviewElement));
		
		Element shelvesElement = reviewElement.getChild("shelves");
		shelvesElement.getChild("shelf").setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				review.get_Shelves().add(attributes.getValue("name"));
			}
		});
	}
	
	public String get_Id()
	{
		return _Id;
	}
	public void set_Id(String _Id)
	{
		this._Id = _Id;
	}
	public Book get_Book()
	{
		return _Book;
	}
	public void set_Book(Book _Book)
	{
		this._Book = _Book;
	}
	public int get_Rating()
	{
		return _Rating;
	}
	public void set_Rating(int _Rating)
	{
		this._Rating = _Rating;
	}
	public int get_Votes()
	{
		return _Votes;
	}
	public void set_Votes(int _Votes)
	{
		this._Votes = _Votes;
	}
	public boolean get_SpoilerFlag()
	{
		return _SpoilerFlag;
	}
	public void set_SpoilerFlag(boolean _SpoilerFlag)
	{
		this._SpoilerFlag = _SpoilerFlag;
	}
	public String get_RecommendedFor()
	{
		return _RecommendedFor;
	}
	public void set_RecommendedFor(String _RecommendedFor)
	{
		this._RecommendedFor = _RecommendedFor;
	}
	public String get_RecommendedBy()
	{
		return _RecommendedBy;
	}
	public void set_RecommendedBy(String _RecommendedBy)
	{
		this._RecommendedBy = _RecommendedBy;
	}
	public String get_StartedAt()
	{
		return _StartedAt;
	}
	public void set_StartedAt(String _StartedAt)
	{
		this._StartedAt = _StartedAt;
	}
	public String get_ReadAt()
	{
		return _ReadAt;
	}
	public void set_ReadAt(String _ReadAt)
	{
		this._ReadAt = _ReadAt;
	}
	public String get_DateAdded()
	{
		return _DateAdded;
	}
	public void set_DateAdded(String _DateAdded)
	{
		this._DateAdded = _DateAdded;
	}
	public String get_DateUpdated()
	{
		return _DateUpdated;
	}
	public void set_DateUpdated(String _DateUpdated)
	{
		this._DateUpdated = _DateUpdated;
	}
	public String get_Body()
	{
		return _Body;
	}
	public void set_Body(String _Body)
	{
		this._Body = _Body;
	}
	public String get_Url()
	{
		return _Url;
	}
	public void set_Url(String _Url)
	{
		this._Url = _Url;
	}
	public String get_Link()
	{
		return _Link;
	}
	public void set_Link(String _Link)
	{
		this._Link = _Link;
	}

	public void set_Shelves(List<String> _Shelves)
	{
		this._Shelves = _Shelves;
	}

	public List<String> get_Shelves()
	{
		return _Shelves;
	}	
}
