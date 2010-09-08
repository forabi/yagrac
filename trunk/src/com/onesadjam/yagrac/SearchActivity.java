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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity
{
	private Context _Context = this;
	private String _AuthenticatedUserId;
	
	private static final int SCAN_BARCODE_REQUEST = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search);
		
		_AuthenticatedUserId = getIntent().getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

		EditText searchText = (EditText)findViewById(R.id._SearchText);
		searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() 
		{
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
			{
				Button searchButton = (Button)findViewById(R.id._SearchButton);
				searchButton.requestFocus();
				performSearch();
				return true;
			}
		});
		
		Button searchButton = (Button)findViewById(R.id._SearchButton);
		searchButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				performSearch();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.searchmenu, menu);
		return true;
	}

	private void performSearch()
	{
		EditText searchTextView = (EditText) findViewById(R.id._SearchText);
		ListView listView = (ListView)findViewById(R.id._SearchResultsList);
		CharSequence searchString = searchTextView.getText();
		try
		{
			SearchResultAdapter searchAdapter = new SearchResultAdapter(_Context);
			
			Search search = ResponseParser.Search(searchString.toString());
			if (search != null)
			{
				List<Work> works = search.get_Results();
				if ( works != null )
				{
					if (works.size() == 0)
					{
						Toast toast = Toast.makeText(_Context, "no matches", Toast.LENGTH_SHORT);
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
					viewBookIntent.putExtra("com.onesadjam.yagrac.BookId", Integer.toString(clickedBook.get_BestBook().get_Id()));
					viewBookIntent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
					arg1.getContext().startActivity(viewBookIntent);				

				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id._Search_Scan:
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//		        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				try
				{
					startActivityForResult(intent, SCAN_BARCODE_REQUEST);
				}
				catch (Exception e)
				{
					Toast.makeText(this, "Please install Barcode Scanner (ZXing) to scan barcodes.", Toast.LENGTH_LONG).show();
				}
				return true;				
		}

		return super.onOptionsItemSelected(item);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
	    if (requestCode == SCAN_BARCODE_REQUEST) 
	    {
	    	if (resultCode == RESULT_OK) 
	        {
				String contents = intent.getStringExtra("SCAN_RESULT");
				
				EditText searchText = (EditText)findViewById(R.id._SearchText);
				searchText.setText(contents);
				performSearch();
//				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
	        }
	    }
	}
}
