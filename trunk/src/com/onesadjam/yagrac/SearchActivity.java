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

import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.onesadjam.yagrac.xml.BestBook;
import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.Search;
import com.onesadjam.yagrac.xml.Work;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
	private List<String> _BulkScanShelves;
	private ProgressDialog _BusySpinner;
	private List<Work> _MatchingWorks;
	
	private static final int SCAN_BARCODE_REQUEST = 0;
	private static final int BULK_SCAN_REQUEST = 1;
	
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
		
		AdView adView = (AdView)this.findViewById(R.id._SearchAd);
	    adView.loadAd(new AdRequest());
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
		CharSequence searchString = searchTextView.getText();
		
		_BusySpinner = ProgressDialog.show(this, "", "Searching...");
		
		new SearchTask().execute(searchString.toString());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id._Search_Scan:
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				try
				{
					startActivityForResult(intent, SCAN_BARCODE_REQUEST);
				}
				catch (Exception e)
				{
					Toast.makeText(this, "Please install Barcode Scanner (ZXing) to scan barcodes.", Toast.LENGTH_LONG).show();
				}
				return true;	
			case R.id._Search_BulkScan:
				bulkScan();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
		switch (requestCode)
		{
			case SCAN_BARCODE_REQUEST:
		    	if (resultCode == RESULT_OK) 
		        {
					String contents = intent.getStringExtra("SCAN_RESULT");
					
					EditText searchText = (EditText)findViewById(R.id._SearchText);
					searchText.setText(contents);
					performSearch();
		        }
		    	break;
			case BULK_SCAN_REQUEST:
		    	if (resultCode == RESULT_OK) 
		        {
					String contents = intent.getStringExtra("SCAN_RESULT");
				
					_BusySpinner = ProgressDialog.show(this, "", "Searching for matching book...");
					new SearchForBarcodeTask().execute(contents);
		        }
		    	break;
	    }
	}
	
	private void bulkScan()
	{
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setMessage("Bulk scan mode allows you to quickly scan a number of books to a set of shelves."
				+ "\n\nWould you like to start scanning?");
		alertBuilder.setCancelable(true);
		alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				final PickShelvesDialog shelfPicker = new PickShelvesDialog(_Context);
				shelfPicker.set_UserId(_AuthenticatedUserId);
				shelfPicker.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						if (shelfPicker.is_Accepted())
						{
							_BulkScanShelves = shelfPicker.get_SelectedShelves();
							
							Intent intent = new Intent("com.google.zxing.client.android.SCAN");
							startActivityForResult(intent, BULK_SCAN_REQUEST);
						}
					}
				});
				shelfPicker.show();
			}
		});
		alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertBuilder.create();
		alertDialog.show();
	}
	
	private class SearchForBarcodeTask extends AsyncTask<String, Void, Search>
	{
		@Override
		protected Search doInBackground(String... params)
		{
			try
			{
				return ResponseParser.Search(params[0]);
			}
			catch (Exception e) {}
			return null;
		}

		@Override
		protected void onPostExecute(Search result)
		{
			try
			{
				_BusySpinner.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(_Context);
				if (result == null || result.get_TotalResults() == 0)
				{
					promptToScanNext("No books match that scan.");
				}
				else
				{
					_MatchingWorks = result.get_Results();
					List<CharSequence> books = new ArrayList<CharSequence>();
					for ( Work work : _MatchingWorks )
					{
						books.add(work.get_BestBook().get_Title() + " by " + work.get_BestBook().get_Author().get_Name());
						}
						books.add("I did not find a match, skip this book");

						builder.setItems(books.toArray(new CharSequence[]{}), new DialogInterface.OnClickListener()
						{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							if (which == _MatchingWorks.size())
							{
								promptToScanNext("Continue scanning.");
							}
							else
							{
								_BusySpinner = ProgressDialog.show(_Context, "", "Adding book to shelves...");
								Work selectedWork = _MatchingWorks.get(which);
								BestBook book = selectedWork.get_BestBook();
								String bookId = Integer.toString(book.get_Id());
								new AddBookToShelvesTask().execute(bookId);
							}
						}
					});
				
					builder.create().show();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private class AddBookToShelvesTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			String successes = "";
			String fails = "";
			for ( String shelfName : _BulkScanShelves )
			{
				try
				{
					ResponseParser.AddBookToShelf(params[0], shelfName);
					successes += "[" + shelfName + "]";
				}
				catch (Exception err)
				{
					fails += "[" + shelfName + "]";
				}
			}
			String result = "";
			if (successes.length() != 0)
			{
				result += "Added book to " + successes;
			}
			if (fails.length() != 0)
			{
				result += "\nFailed to add to " + fails;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result)
		{
			try
			{
				_BusySpinner.dismiss();
				Toast.makeText(_Context, result, Toast.LENGTH_SHORT).show();
				promptToScanNext("Continue scanning.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void promptToScanNext(String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(_Context);
		builder.setMessage(message);
		builder.setPositiveButton("Scan Next", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1)
			{
				dialog.dismiss();
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				startActivityForResult(intent, BULK_SCAN_REQUEST);
			}
		});
		builder.setNeutralButton("Change Shelves", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1)
			{
				dialog.dismiss();
				final PickShelvesDialog shelfPicker = new PickShelvesDialog(_Context);
				shelfPicker.set_UserId(_AuthenticatedUserId);
				shelfPicker.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						if (shelfPicker.is_Accepted())
						{
							_BulkScanShelves = shelfPicker.get_SelectedShelves();
							
							Intent intent = new Intent("com.google.zxing.client.android.SCAN");
							startActivityForResult(intent, BULK_SCAN_REQUEST);
						}
					}
				});
				shelfPicker.show();
			}
		});
		builder.setNegativeButton("Done", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int arg1)
			{
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	/**
	 * Background task for book search.
	 * @author ajones
	 *
	 */
	private class SearchTask extends AsyncTask<String, Void, List<Work>>
	{
		@Override
		protected List<Work> doInBackground(String... params)
		{
			List<Work> searchResults = null;
			Search search;
			if (params != null && params.length > 0)
			{
				try
				{
					search = ResponseParser.Search(params[0]);
					searchResults = search.get_Results();
				}
				catch (Exception e){}
			}
			return searchResults;
		}

		@Override
		protected void onPostExecute(List<Work> result)
		{
			try
			{
				if (result != null)
				{
					if (result.size() == 0)
					{
						Toast toast = Toast.makeText(_Context, "no matches", Toast.LENGTH_SHORT);
						toast.show();
					}
					else
					{
						ListView listView = (ListView)findViewById(R.id._SearchResultsList);
						SearchResultAdapter searchAdapter = new SearchResultAdapter(_Context);
						for ( int i = 0; i < result.size(); i++ )
						{
							searchAdapter.addResult(result.get(i));
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
				}
			}
			finally
			{
				_BusySpinner.dismiss();
			}
		}
	}
}
