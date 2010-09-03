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

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.StartElementListener;

public class Action
{
	private String _ActionType;
	private String _ShelfName;
	private int _Rating;
	
	public void clear()
	{
		this.set_ActionType("");
		this.set_ShelfName("");
		this.set_Rating(0);
	}
	
	public Action copy()
	{
		Action actionCopy = new Action();
		
		actionCopy.set_ActionType(this.get_ActionType());
		actionCopy.set_ShelfName(this.get_ShelfName());
		actionCopy.set_Rating(this.get_Rating());
		
		return actionCopy;
	}
	
	public static Action appendSingletonListener(Element parentElement, int depth)
	{
		final Action action = new Action();
		final Element actionElement = parentElement.getChild("action");
		
		actionElement.setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				action.set_ActionType(attributes.getValue("type"));
			}
		});
		
		actionElement.getChild("rating").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					action.set_Rating(Integer.parseInt(body));
				}
			}
		});
		
		actionElement.getChild("shelf").setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				action.set_ShelfName(attributes.getValue("name"));
			}
		});
		
		return action;
	}
	
	public String get_ActionType()
	{
		return _ActionType;
	}
	public void set_ActionType(String _ActionType)
	{
		this._ActionType = _ActionType;
	}
	public String get_ShelfName()
	{
		return _ShelfName;
	}
	public void set_ShelfName(String _ShelfName)
	{
		this._ShelfName = _ShelfName;
	}

	public int get_Rating()
	{
		return _Rating;
	}

	public void set_Rating(int _Rating)
	{
		this._Rating = _Rating;
	}
}
