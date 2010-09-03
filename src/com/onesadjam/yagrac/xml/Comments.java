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

public class Comments
{
	private int _Start;
	private int _End;
	private int _Total;
	private List<Comment> _Comments = new ArrayList<Comment>();
	
	public void clear()
	{
		this.set_Start(0);
		this.set_End(0);
		this.set_Total(0);
		_Comments.clear();
	}
	
	public Comments copy()
	{
		Comments commentsCopy = new Comments();
		
		commentsCopy.set_Start(this.get_Start());
		commentsCopy.set_End(this.get_End());
		commentsCopy.set_Total(this.get_Total());
		
		List<Comment> commentsListCopy = new ArrayList<Comment>();
		for (int i = 0; i < _Comments.size(); i++)
		{
			commentsListCopy.add(_Comments.get(i).copy());
		}
		commentsCopy.set_Comments(commentsListCopy);
		
		return commentsCopy;
	}
	
	public static Comments appendSingletonListener(Element parentElement, int depth)
	{
		final Comments comments = new Comments();
		final Element commentsElement = parentElement.getChild("comments");
		
		commentsElement.setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				String value = attributes.getValue("start");
				if (value != null && value.length() != 0)
				{
					comments.set_Start(Integer.parseInt(value));
				}
				value = attributes.getValue("end");
				if (value != null && value.length() != 0)
				{
					comments.set_End(Integer.parseInt(value));
				}
				value = attributes.getValue("total");
				if (value != null && value.length() != 0)
				{
					comments.set_Total(Integer.parseInt(value));
				}
			}
		});
		
		comments.set_Comments(Comment.appendArrayListener(commentsElement, depth + 1));
		
		return comments;
	}
	
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

	public List<Comment> get_Comments()
	{
		return _Comments;
	}

	public void set_Comments(List<Comment> _Comments)
	{
		this._Comments = _Comments;
	}
}
