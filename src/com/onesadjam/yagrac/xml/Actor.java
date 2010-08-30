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

public class Actor
{
	private int _Id;
	private String _Name;
	private String _ImageUrl;
	private String _Link;
	
	public void clear()
	{
		this.set_Id(0);
		this.set_Name("");
		this.set_ImageUrl("");
		this.set_Link("");
	}
	
	public Actor copy()
	{
		Actor actorCopy = new Actor();
		
		actorCopy.set_Id(this.get_Id());
		actorCopy.set_Name(this.get_Name());
		actorCopy.set_ImageUrl(this.get_ImageUrl());
		actorCopy.set_Link(this.get_Link());
		
		return actorCopy;
	}
	
	public static Actor appendSingletonListener(Element parentElement)
	{
		final Actor actor = new Actor();
		final Element actorElement = parentElement.getChild("actor");
		
		actorElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					actor.set_Id(Integer.parseInt(body));
				}
			}
		});
		
		actorElement.getChild("name").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				actor.set_Name(body);
			}
		});
		
		actorElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				actor.set_ImageUrl(body);
			}
		});
		
		actorElement.getChild("link").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				actor.set_Link(body);
			}
		});
		
		return actor;
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
	public String get_Link()
	{
		return _Link;
	}
	public void set_Link(String _Link)
	{
		this._Link = _Link;
	}
}
