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

public class Reviews
{
	private int _Start;
	private int _End;
	private int _Total;
	private List<Review> _Reviews = new ArrayList<Review>();
	
	public void clear()
	{
		this.set_Start(0);
		this.set_End(0);
		this.set_Total(0);
		this._Reviews.clear();
	}
	
	public Reviews copy()
	{
		Reviews reviewsCopy = new Reviews();
		
		reviewsCopy.set_Start(this.get_Start());
		reviewsCopy.set_End(this.get_End());
		reviewsCopy.set_Total(this.get_Total());
		
		List<Review> reviewList = this.get_Reviews();
		List<Review> reviewListCopy = reviewsCopy.get_Reviews();
		if (reviewList != null)
		{
			for ( int i = 0; i < reviewList.size(); i++ )
			{
				reviewListCopy.add(reviewList.get(i).copy());
			}
		}
		
		return reviewsCopy;
	}
	
	public static Reviews appendSingletonListener(Element parentElement)
	{
		final Reviews reviews = new Reviews();
		Element reviewsElement = parentElement.getChild("reviews");
	
		reviewsElement.setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				reviews.set_Start(Integer.parseInt(attributes.getValue("start")));
				reviews.set_End(Integer.parseInt(attributes.getValue("end")));
				reviews.set_Total(Integer.parseInt(attributes.getValue("total")));
			}
		});

		reviews.set_Reviews(Review.appendArrayListener(reviewsElement));
		
		return reviews;
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
	public List<Review> get_Reviews()
	{
		return _Reviews;
	}
	public void set_Reviews(List<Review> _Reviews)
	{
		this._Reviews = _Reviews;
	}
}
