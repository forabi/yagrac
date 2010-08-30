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
import android.sax.StartElementListener;

public class Following
{
	private int _Start;
	private int _End;
	private int _Total;
	private List<User> _Following = new ArrayList<User>();
	
	public int get_Start()
	{
		return _Start;
	}
	public void set_Start(int _Start)
	{
		this._Start = _Start;
	}
	public int get_End()
	{
		return _End;
	}
	public void set_End(int _End)
	{
		this._End = _End;
	}
	public int get_Total()
	{
		return _Total;
	}
	public void set_Total(int _Total)
	{
		this._Total = _Total;
	}
	public List<User> get_Following()
	{
		return _Following;
	}
	public void set_Following(List<User> _Following)
	{
		this._Following = _Following;
	}

	public static Following appendSingletonListener(final Element parentElement)
	{
		final Following following = new Following();
		Element followingElement = parentElement.getChild("following");
		followingElement.setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				following.set_Start(Integer.parseInt(attributes.getValue("start")));
				following.set_End(Integer.parseInt(attributes.getValue("end")));
				following.set_Total(Integer.parseInt(attributes.getValue("total")));
			}
		});
		
		following.set_Following(User.appendArrayListener(followingElement));
		
		return following;
	}
}
