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

import android.net.Uri;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.StartElementListener;

public class User
{
	private String _Name;
	private Uri _Link;
	private String _Id;
	private String _ImageUrl;
	private String _SmallImageUrl;
	private int _FriendsCount;
	private int _ReviewsCount;
	private String _Username;
	private String _About;
	private String _Age;
	private String _Gender;
	private String _Location;
	private String _Website;
	private String _Joined;
	private String _LastActive;
	private String _Interests;
	private String _FavoriteBooks;
	private List<Author> _FavoriteAuthors = new ArrayList<Author>();
	private String _UpdatesRssUrl;
	private String _ReviewsRssUrl;
	private List<UserShelf> _Shelves;
	private List<Update> _Updates;
	
	public User copy()
	{
		User userCopy = new User();
		
		userCopy.set_FriendsCount(this.get_FriendsCount());
		userCopy.set_Id(this.get_Id());
		userCopy.set_ImageUrl(this.get_ImageUrl());
		userCopy.set_Link(this.get_Link());
		userCopy.set_Name(this.get_Name());
		userCopy.set_ReviewsCount(this.get_ReviewsCount());
		userCopy.set_SmallImageUrl(this.get_SmallImageUrl());
		userCopy.set_Username(this.get_Username());
		userCopy.set_About(this.get_About());
		userCopy.set_Age(this.get_Age());
		userCopy.set_Gender(this.get_Gender());
		userCopy.set_Location(this.get_Location());
		userCopy.set_Website(this.get_Website());
		userCopy.set_Joined(this.get_Joined());
		userCopy.set_LastActive(this.get_LastActive());
		userCopy.set_Interests(this.get_Interests());
		userCopy.set_FavoriteBooks(this.get_FavoriteBooks());
		userCopy.set_UpdatesRssUrl(this.get_UpdatesRssUrl());
		userCopy.set_ReviewsRssUrl(this.get_ReviewsRssUrl());
		
		List<Author> favoriteAuthorsCopy = new ArrayList<Author>();
		for (int i = 0; i < _FavoriteAuthors.size(); i++)
		{
			favoriteAuthorsCopy.add(_FavoriteAuthors.get(i));
		}
		userCopy.set_FavoriteAuthors(favoriteAuthorsCopy);
		
		List<UserShelf> userShelvesCopy = new ArrayList<UserShelf>();
		for (int i = 0; i < _Shelves.size(); i++)
		{
			userShelvesCopy.add(_Shelves.get(i));
		}
		userCopy.set_Shelves(userShelvesCopy);
		
		List<Update> udpatesCopy = new ArrayList<Update>();
		for (int i = 0; i < _FavoriteAuthors.size(); i++)
		{
			udpatesCopy.add(_Updates.get(i));
		}
		userCopy.set_Updates(udpatesCopy);
		
		return userCopy;
	}

	public void clear()
	{
		this.set_FriendsCount(0);
		this.set_Id("");
		this.set_ImageUrl("");
		this.set_Link(null);
		this.set_Name("");
		this.set_ReviewsCount(0);
		this.set_SmallImageUrl("");
		this.set_Username("");
		this.set_About("");
		this.set_Age("");
		this.set_Gender("");
		this.set_Location("");
		this.set_Website("");
		this.set_Joined("");
		this.set_LastActive("");
		this.set_Interests("");
		this.set_FavoriteBooks("");
		this.set_UpdatesRssUrl("");
		this.set_ReviewsRssUrl("");
		
		_FavoriteAuthors.clear();
		_Shelves.clear();
		_Updates.clear();
	}
	
	public static User appendSingletonListener(final Element parentElement)
	{
		final User user = new User();
		
		Element userElement = parentElement.getChild("user");

		appendCommonListeners(userElement, user);
		
		return user;
	}
	
	public static List<User> appendArrayListener(final Element parentElement)
	{
		final List<User> users = new ArrayList<User>();
		final User user = new User();
		
		Element userElement = parentElement.getChild("user");
		
		userElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				users.add(user.copy());
				user.clear();
			}
		});
		
		appendCommonListeners(userElement, user);
		
		return users;
	}
	
	private static void appendCommonListeners(final Element userElement, final User user)
	{
		userElement.setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				String idAsAttribute = attributes.getValue("id");
				if (idAsAttribute != null)
				{
					user.set_Id(idAsAttribute);
				}
			}
		});
		
		userElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Id(body);
			}
		});
		
		userElement.getChild("name").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Name(body);
			}
		});
		
		userElement.getChild("link").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Link(Uri.parse(body));
			}
		});

		userElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_ImageUrl(body);
			}
		});

		userElement.getChild("small_image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_SmallImageUrl(body);
			}
		});

		userElement.getChild("friends_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_FriendsCount(Integer.parseInt(body));
			}
		});

		userElement.getChild("reviews_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_ReviewsCount(Integer.parseInt(body));
			}
		});
	
		userElement.getChild("user_name").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Username(body);
			}
		});

		userElement.getChild("about").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_About(body);
			}
		});

		userElement.getChild("age").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Age(body);
			}
		});

		userElement.getChild("gender").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Gender(body);
			}
		});

		userElement.getChild("location").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Location(body);
			}
		});

		userElement.getChild("website").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Website(body);
			}
		});

		userElement.getChild("joined").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Joined(body);
			}
		});

		userElement.getChild("last_active").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_LastActive(body);
			}
		});

		userElement.getChild("interests").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_Interests(body);
			}
		});

		userElement.getChild("favorite_books").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_FavoriteBooks(body);
			}
		});

		userElement.getChild("udpates_rss_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_UpdatesRssUrl(body);
			}
		});

		userElement.getChild("reviews_rss_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				user.set_ReviewsRssUrl(body);
			}
		});
		
		Element userShelves = userElement.getChild("user_shelves");
		user.set_Shelves(UserShelf.appendArrayListener(userShelves));
		
		user.set_Updates(Update.appendArrayListener(userElement));
		
		Element favoriteAuthorsElement = userElement.getChild("favorite_authors");
		user.set_FavoriteAuthors(Author.appendArrayListener(favoriteAuthorsElement));
	}
	
	public String get_Name()
	{
		return _Name;
	}
	public void set_Name(String _Name)
	{
		this._Name = _Name;
	}
	public Uri get_Link()
	{
		return _Link;
	}
	public void set_Link(Uri _Link)
	{
		this._Link = _Link;
	}
	public String get_Id()
	{
		return _Id;
	}
	public void set_Id(String _Id)
	{
		this._Id = _Id;
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
	public int get_FriendsCount()
	{
		return _FriendsCount;
	}
	public void set_FriendsCount(int _FriendsCount)
	{
		this._FriendsCount = _FriendsCount;
	}
	public int get_ReviewsCount()
	{
		return _ReviewsCount;
	}
	public void set_ReviewsCount(int _ReviewsCount)
	{
		this._ReviewsCount = _ReviewsCount;
	}

	public String get_Username()
	{
		return _Username;
	}

	public void set_Username(String _Username)
	{
		this._Username = _Username;
	}

	public String get_About()
	{
		return _About;
	}

	public void set_About(String _About)
	{
		this._About = _About;
	}

	public String get_Age()
	{
		return _Age;
	}

	public void set_Age(String _Age)
	{
		this._Age = _Age;
	}

	public String get_Gender()
	{
		return _Gender;
	}

	public void set_Gender(String _Gender)
	{
		this._Gender = _Gender;
	}

	public String get_Location()
	{
		return _Location;
	}

	public void set_Location(String _Location)
	{
		this._Location = _Location;
	}

	public String get_Website()
	{
		return _Website;
	}

	public void set_Website(String _Website)
	{
		this._Website = _Website;
	}

	public String get_Joined()
	{
		return _Joined;
	}

	public void set_Joined(String _Joined)
	{
		this._Joined = _Joined;
	}

	public String get_LastActive()
	{
		return _LastActive;
	}

	public void set_LastActive(String _LastActive)
	{
		this._LastActive = _LastActive;
	}

	public String get_Interests()
	{
		return _Interests;
	}

	public void set_Interests(String _Interests)
	{
		this._Interests = _Interests;
	}

	public String get_FavoriteBooks()
	{
		return _FavoriteBooks;
	}

	public void set_FavoriteBooks(String _FavoriteBooks)
	{
		this._FavoriteBooks = _FavoriteBooks;
	}

	public List<Author> get_FavoriteAuthors()
	{
		return _FavoriteAuthors;
	}

	public void set_FavoriteAuthors(List<Author> _FavoriteAuthors)
	{
		this._FavoriteAuthors = _FavoriteAuthors;
	}

	public String get_UpdatesRssUrl()
	{
		return _UpdatesRssUrl;
	}

	public void set_UpdatesRssUrl(String _UpdatesRssUrl)
	{
		this._UpdatesRssUrl = _UpdatesRssUrl;
	}

	public String get_ReviewsRssUrl()
	{
		return _ReviewsRssUrl;
	}

	public void set_ReviewsRssUrl(String _ReviewsRssUrl)
	{
		this._ReviewsRssUrl = _ReviewsRssUrl;
	}

	public List<UserShelf> get_Shelves()
	{
		return _Shelves;
	}

	public void set_Shelves(List<UserShelf> _Shelves)
	{
		this._Shelves = _Shelves;
	}

	public List<Update> get_Updates()
	{
		return _Updates;
	}

	public void set_Updates(List<Update> _Updates)
	{
		this._Updates = _Updates;
	}	
}
