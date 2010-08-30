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

import android.sax.Element;
import android.sax.EndTextElementListener;

public class Search
{
	private String _Query;
	private int _ResultsStart;
	private int _ResultsEnd;
	private int _TotalResults;
	private String _Source;
	private float _QueryTime;
	private List<Work> _Results = new ArrayList<Work>();
	
	public void clear()
	{
		this.set_Query("");
		this.set_ResultsStart(0);
		this.set_ResultsEnd(0);
		this.set_TotalResults(0);
		this.set_QueryTime(0);
		this.set_Source("");
		this._Results.clear();
	}
	
	public Search copy()
	{
		Search searchCopy = new Search();

		searchCopy.set_Query(this.get_Query());
		searchCopy.set_Results(this.get_Results());
		searchCopy.set_ResultsStart(this.get_ResultsStart());
		searchCopy.set_ResultsEnd(this.get_ResultsEnd());
		searchCopy.set_TotalResults(this.get_TotalResults());
		searchCopy.set_QueryTime(this.get_QueryTime());
		searchCopy.set_Source(this.get_Source());

		return searchCopy;
	}
	
	public static Search appendSingletonListener(final Element parentElement)
	{
		final Search search = new Search();
		
		Element searchElement = parentElement.getChild("search");

		appendCommonListeners(searchElement, search);
		
		return search;
	}
	
//	public static List<Search> appendArrayListener(final Element parentElement)
//	{
//		final List<Search> searches = new ArrayList<Search>();
//		
//		return searches;
//	}
	
	private static void appendCommonListeners(final Element searchElement, final Search search)
	{
		Element resultsElement = searchElement.getChild("results");
		search.set_Results(Work.appendArrayListener(resultsElement));

		searchElement.getChild("query").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				search.set_Query(body);
			}
		});

		searchElement.getChild("results-start").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					search.set_ResultsStart(Integer.parseInt(body));
				}
			}
		});

		searchElement.getChild("results-end").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					search.set_ResultsEnd(Integer.parseInt(body));
				}
			}
		});

		searchElement.getChild("total-results").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					search.set_TotalResults(Integer.parseInt(body));
				}
			}
		});

		searchElement.getChild("source").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				search.set_Source(body);
			}
		});

		searchElement.getChild("query-time-seconds").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				if (body != null && body != "")
				{
					search.set_QueryTime(Float.parseFloat(body));
				}
			}
		});
	}

	public String get_Query()
	{
		return _Query;
	}

	public void set_Query(String _Query)
	{
		this._Query = _Query;
	}

	public int get_ResultsStart()
	{
		return _ResultsStart;
	}

	public void set_ResultsStart(int _ResultsStart)
	{
		this._ResultsStart = _ResultsStart;
	}

	public int get_ResultsEnd()
	{
		return _ResultsEnd;
	}

	public void set_ResultsEnd(int _ResultsEnd)
	{
		this._ResultsEnd = _ResultsEnd;
	}

	public int get_TotalResults()
	{
		return _TotalResults;
	}

	public void set_TotalResults(int _TotalResults)
	{
		this._TotalResults = _TotalResults;
	}

	public String get_Source()
	{
		return _Source;
	}

	public void set_Source(String _Source)
	{
		this._Source = _Source;
	}

	public float get_QueryTime()
	{
		return _QueryTime;
	}

	public void set_QueryTime(float _QueryTime)
	{
		this._QueryTime = _QueryTime;
	}

	public List<Work> get_Results()
	{
		return _Results;
	}

	public void set_Results(List<Work> _Results)
	{
		this._Results = _Results;
	}
}
