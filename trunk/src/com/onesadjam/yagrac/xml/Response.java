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

public class Response
{
	private Request _Request;
	private User _User;
	private Shelves _Shelves;
	private Reviews _Reviews;
	private Review _Review;
	private Friends _Friends;
	private Followers _Followers;
	private Following _Following;
	private Search _Search;
	private Book _Book;
	private List<Update> _Updates = new ArrayList<Update>();
	
	public void clear()
	{
		this.set_Request(new Request());
		this.get_User().clear();
		this.set_Shelves(new Shelves());
		this.set_Reviews(new Reviews());
		this.set_Friends(new Friends());
		this.set_Followers(new Followers());
		this.set_Following(new Following());
		this.get_Search().clear();
		this.get_Review().clear();
		this.get_Updates().clear();
		this.get_Book().clear();
	}
	
	public void copy()
	{
		Response responseCopy = new Response();
		
		responseCopy.set_Request(this.get_Request().copy());
		responseCopy.set_User(this.get_User().copy());
		responseCopy.set_Shelves(this.get_Shelves());
		responseCopy.set_Reviews(this.get_Reviews());
		responseCopy.set_Review(this.get_Review().copy());
		responseCopy.set_Friends(this.get_Friends());
		responseCopy.set_Followers(this.get_Followers());
		responseCopy.set_Following(this.get_Following());
		responseCopy.set_Search(get_Search().copy());
		responseCopy.set_Book(get_Book().copy());
		
		List<Update> updates = new ArrayList<Update>();
		for (int i = 0; i < this.get_Updates().size(); i++)
		{
			updates.add(this.get_Updates().get(i));
		}
		responseCopy.set_Updates(updates);
	}
	
	public Request get_Request()
	{
		return _Request;
	}
	
	public void set_Request(Request request)
	{
		_Request = request;
	}
	
	public User get_User()
	{
		return _User;
	}
	
	public void set_User(User user)
	{
		_User = user;
	}

	public Shelves get_Shelves()
	{
		return _Shelves;
	}

	public void set_Shelves(Shelves _Shelves)
	{
		this._Shelves = _Shelves;
	}

	public Reviews get_Reviews()
	{
		return _Reviews;
	}

	public void set_Reviews(Reviews _Reviews)
	{
		this._Reviews = _Reviews;
	}

	public Friends get_Friends()
	{
		return _Friends;
	}

	public void set_Friends(Friends _Friends)
	{
		this._Friends = _Friends;
	}

	public Followers get_Followers()
	{
		return _Followers;
	}

	public void set_Followers(Followers _Followers)
	{
		this._Followers = _Followers;
	}

	public Following get_Following()
	{
		return _Following;
	}

	public void set_Following(Following _Following)
	{
		this._Following = _Following;
	}

	public void set_Search(Search _Search)
	{
		this._Search = _Search;
	}

	public Search get_Search()
	{
		return _Search;
	}

	public void set_Updates(List<Update> _Updates)
	{
		this._Updates = _Updates;
	}

	public List<Update> get_Updates()
	{
		return _Updates;
	}

	public void set_Review(Review _Review)
	{
		this._Review = _Review;
	}

	public Review get_Review()
	{
		return _Review;
	}

	public Book get_Book()
	{
		return _Book;
	}

	public void set_Book(Book _Book)
	{
		this._Book = _Book;
	}
}