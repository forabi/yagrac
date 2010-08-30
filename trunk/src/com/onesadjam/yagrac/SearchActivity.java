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

package com.onesadjam.yagrac;

import java.util.List;

import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.Search;
import com.onesadjam.yagrac.xml.Work;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search);
		
		final Context context = this;
		
		final String authenticatedUserId = getIntent().getExtras().getString("com.onesadjam.GoodReads.AuthenticatedUserId");

		Button searchButton = (Button)findViewById(R.id._SearchButton);
		searchButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EditText searchTextView = (EditText) findViewById(R.id._SearchText);
				ListView listView = (ListView)findViewById(R.id._SearchResultsList);
				CharSequence searchString = searchTextView.getText();
				try
				{
					SearchResultAdapter searchAdapter = new SearchResultAdapter(context);
					
					Search search = ResponseParser.Search(searchString.toString());
					if (search != null)
					{
						List<Work> works = search.get_Results();
						if ( works != null )
						{
							if (works.size() == 0)
							{
								Toast toast = Toast.makeText(context, "no matches", Toast.LENGTH_SHORT);
								toast.show();
							}
							for ( int i = 0; i < works.size(); i++ )
							{
								searchAdapter.addResult(works.get(i));
							}
						}
					}
					
					listView.setAdapter(searchAdapter);
					
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
					{
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
						{
							Work clickedBook = (Work)arg0.getAdapter().getItem(arg2);
							Intent viewBookIntent = new Intent(arg1.getContext(), ViewBookActivity.class);
							viewBookIntent.putExtra("com.onesadjam.GoodReads.BookId", Integer.toString(clickedBook.get_BestBook().get_Id()));
							viewBookIntent.putExtra("com.onesadjam.GoodReads.AuthenticatedUserId", authenticatedUserId);
							arg1.getContext().startActivity(viewBookIntent);				

						}
					});

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
