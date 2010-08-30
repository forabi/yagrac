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

public class Request
{
	private boolean _Authentication;
	private String _Key;
	private String _Method;
	
	public void clear()
	{
		this.set_Authentication(false);
		this.set_Key("");
		this.set_Method("");
	}
	
	public Request copy()
	{
		Request requestCopy = new Request();
		
		requestCopy.set_Authentication(this.get_Authentication());
		requestCopy.set_Key(this.get_Key());
		requestCopy.set_Method(this.get_Method());
		
		return requestCopy;
	}
	
	public static Request appendSingletonListener(Element parentElement)
	{
		final Request request = new Request();
		final Element requestElement = parentElement.getChild("request");
		
		requestElement.getChild("authentication").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				request.set_Authentication(Boolean.parseBoolean(body));
			}
		});
		
		requestElement.getChild("key").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				request.set_Key(body);
			}
		});
		
		requestElement.getChild("request").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				request.set_Method(body);
			}
		});

		return request;
	}
	
	public boolean get_Authentication()
	{
		return _Authentication;
	}
	
	public void set_Authentication(boolean authentication)
	{
		_Authentication = authentication;
	}

	public String get_Key()
	{
		return _Key;
	}

	public void set_Key(String key)
	{
		_Key = key;
	}

	public String get_Method()
	{
		return _Method;
	}

	public void set_Method(String method)
	{
		this._Method = method;
	}
}
