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

public class UpdateObject
{
	private Book _Book = new Book();
	
	public void clear()
	{
		_Book.clear();
	}
	
	public UpdateObject copy()
	{
		UpdateObject updateObjectCopy = new UpdateObject();
		
		updateObjectCopy.set_Book(_Book.copy());
		
		return updateObjectCopy;
	}
	
	public static UpdateObject appendSingletonListener(Element parentElement, int depth)
	{
		final Element updateObjectElement = parentElement.getChild("object");
		final UpdateObject updateObject = new UpdateObject();
		
		updateObject.set_Book(Book.appendSingletonListener(updateObjectElement, depth + 1));
		
		return updateObject;
	}

	public void set_Book(Book _Book)
	{
		this._Book = _Book;
	}

	public Book get_Book()
	{
		return _Book;
	}
}
