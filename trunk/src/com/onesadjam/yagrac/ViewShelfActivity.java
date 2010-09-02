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
import com.onesadjam.yagrac.xml.Review;
import com.onesadjam.yagrac.xml.UserShelf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class ViewShelfActivity extends Activity
{
	private String _UserId; 
	private String _AuthenticatedUserId;
	private BookImageAdapter _BookGalleryAdapter;
	private Review _SelectedReview;
	private Context _Context;
	
	private static final int PICK_SHELVES_DIALOG = 1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.viewshelflayout);
		
		Spinner shelfSpinner = (Spinner)findViewById(R.id._ShelfSpinner);
		_Context = this;
		
		Gallery bookGallery = (Gallery)findViewById(R.id._ViewShelfBookGallery);
		_BookGalleryAdapter = new BookImageAdapter(this);
		bookGallery.setAdapter(_BookGalleryAdapter);
		
		// Get the user ID
		try
		{
			// Request the list of shelves for the user.
			Intent launchingIntent = this.getIntent();
			
			_UserId = launchingIntent.getExtras().getString("com.onesadjam.yagrac.UserId");
			_AuthenticatedUserId = getIntent().getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");
			ArrayAdapter<String> shelfSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			shelfSpinner.setAdapter(shelfSpinnerAdapter);
			
			List<UserShelf> userShelves = ResponseParser.GetShelvesForUser(_UserId);
			
			for (int i = 0; i < userShelves.size(); i++)
			{
				shelfSpinnerAdapter.add(userShelves.get(i).get_Name());
			}
			
			shelfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id)
				{
					loadShelf(parentView.getItemAtPosition(position).toString());
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> parentview) {}
			});
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void loadShelf(String shelfName)
	{
		try
		{
			List<Review> reviews = ResponseParser.GetBooksOnShelf(shelfName, _UserId).get_Reviews();
			_BookGalleryAdapter.clear();
			for (int i = 0; i < reviews.size(); i++)
			{
				_BookGalleryAdapter.AddBook(reviews.get(i));
			}
			Gallery bookGallery = (Gallery)findViewById(R.id._ViewShelfBookGallery);
			bookGallery.setAdapter(_BookGalleryAdapter);
			
			bookGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					set_SelectedReview((Review)_BookGalleryAdapter.getItem(arg2));
					
					RatingBar bookRating = (RatingBar)findViewById(R.id._ViewShelfRatingBar);
					bookRating.setRating(_SelectedReview.get_Rating());
					bookRating.setVisibility(_SelectedReview.get_Rating() == 0 ? View.GONE : View.VISIBLE);

					TextView bookDetails = (TextView)findViewById(R.id._ViewShelfBookDetailsTextView);
					bookDetails.setVisibility(View.VISIBLE);
					
					StringBuilder sb = new StringBuilder();
					sb.append(_SelectedReview.get_Book().get_Title() + "\nBy:\n");
					for (int i = 0; i < _SelectedReview.get_Book().get_Authors().size(); i++)
					{
						sb.append("\t" + _SelectedReview.get_Book().get_Authors().get(i).get_Name() + "\n");
					}
					sb.append("\n" + Html.fromHtml(_SelectedReview.get_Book().get_Description()));
					
					bookDetails.setText(sb.toString());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) 
				{
					TextView bookDetails = (TextView)findViewById(R.id._ViewShelfBookDetailsTextView);
					bookDetails.setVisibility(View.INVISIBLE);
					RatingBar bookRating = (RatingBar)findViewById(R.id._ViewShelfRatingBar);
					bookRating.setVisibility(View.INVISIBLE);
				}
				
			});
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.viewshelfmenu, menu);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case PICK_SHELVES_DIALOG:
				final PickShelvesDialog pickShelvesDialog = new PickShelvesDialog(this);
				pickShelvesDialog.set_UserId(_AuthenticatedUserId);
				pickShelvesDialog.set_ReviewId(_SelectedReview.get_Id());
				pickShelvesDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						if (pickShelvesDialog.is_Accepted())
						{
							List<String> selectedShelves = pickShelvesDialog.get_SelectedShelves();
							for (int i = 0; i < selectedShelves.size(); i++)
							{
								try
								{
									ResponseParser.AddBookToShelf(_SelectedReview.get_Book().get_Id(), selectedShelves.get(i));
								}
								catch (Exception e)
								{
									Toast.makeText(_Context, e.getMessage(), Toast.LENGTH_LONG).show();
								}
							}
						}
						removeDialog(PICK_SHELVES_DIALOG);
					}
				});
				return pickShelvesDialog;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id._AddToShelf:
				showDialog(PICK_SHELVES_DIALOG);
				return true;
			case R.id._ViewShelfMenu_ReviewBook:
				Intent intent = new Intent(_Context, ReviewBookActivity.class);
				intent.putExtra("com.onesadjam.yagrac.AuthenticatedUserId", _AuthenticatedUserId);
				intent.putExtra("com.onesadjam.yagrac.BookId", _SelectedReview.get_Book().get_Id());
				if (_UserId.equalsIgnoreCase(_AuthenticatedUserId))
				{
					intent.putExtra("com.onesadjam.yagrac.ReviewId", _SelectedReview.get_Id());
				}
				_Context.startActivity(intent);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void set_SelectedReview(Review _SelectedReview)
	{
		this._SelectedReview = _SelectedReview;
	}

	public Review get_SelectedReview()
	{
		return _SelectedReview;
	}
	
	
}
