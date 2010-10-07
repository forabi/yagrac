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

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.onesadjam.yagrac.xml.Book;
import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.User;
import com.onesadjam.yagrac.xml.UserShelf;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewBookActivity extends Activity
{
	private static final int BOOK_IMAGE_HEIGHT = 160;
	private static final int BOOK_IMAGE_WIDTH = 120;
	private static final int DATE_DIALOG_ID = 0;
	private static final int PICK_SHELVES_DIALOG = 1;

	private String _BookId;
	private String _ReviewId;
	private String _AuthenticatedUserId;
	private List<String> _Shelves = new ArrayList<String>();
	private String _DateRead;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.reviewbook);
		
		_BookId = getIntent().getExtras().getString("com.onesadjam.yagrac.BookId");
		_ReviewId = getIntent().getExtras().getString("com.onesadjam.yagrac.ReviewId");
		_AuthenticatedUserId = getIntent().getExtras().getString("com.onesadjam.yagrac.AuthenticatedUserId");

		ImageView bookImage = (ImageView) findViewById(R.id._ReviewBook_BookImage);
		
		Calendar currentDate = Calendar.getInstance();
		TextView textView = (TextView)findViewById(R.id._ReviewBook_DateRead);
		_DateRead = String.format("%04d-%02d-%02d", 
				currentDate.get(Calendar.YEAR), 
				currentDate.get(Calendar.MONTH)+1,
				currentDate.get(Calendar.DAY_OF_MONTH));
		textView.setText("Date Read:\t" + _DateRead);
		
		textView = (TextView)findViewById(R.id._ReviewBook_Shelves);
		textView.setText("Shelves:\tread");
		_Shelves.add("read");

		try
		{
			Book bookReviews = ResponseParser.GetReviewsForBook(_BookId);
			bookImage.setScaleType(ScaleType.FIT_CENTER);
			bookImage.setMinimumHeight((int)(BOOK_IMAGE_HEIGHT * HomeActivity.get_ScalingFactor()));
			bookImage.setMinimumWidth((int)(BOOK_IMAGE_WIDTH * HomeActivity.get_ScalingFactor()));
			LazyImageLoader.LazyLoadImageView(this, new URL(bookReviews.get_ImageUrl()), R.drawable.nocover, bookImage);
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		try
		{
			User userDetails = ResponseParser.GetUserDetails(_AuthenticatedUserId);
			
			List<UserShelf> shelves = userDetails.get_Shelves();
			
			SelectShelvesSpinnerAdapter adapter = new SelectShelvesSpinnerAdapter(this);
			for (int i = 0; i < shelves.size(); i++)
			{
				adapter.AddShelf(shelves.get(i).get_Name());
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
			
		Button button = (Button)findViewById(R.id._ReviewBook_DatePickerButton);
		button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
                showDialog(DATE_DIALOG_ID);
            }
        });	
		
		button = (Button)findViewById(R.id._ReviewBook_PickShelvesButton);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showDialog(PICK_SHELVES_DIALOG);
			}
		});
		
		button = (Button)findViewById(R.id._ReviewBook_SubmitButton);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					TextView textView = (TextView)findViewById(R.id._ReviewBook_Review);
					String review = textView.getText().toString();
					RatingBar ratingBar = (RatingBar)findViewById(R.id._ReviewBook_Rating);
					int rating = (int)ratingBar.getRating();
					
					if (_ReviewId != null && !_ReviewId.equalsIgnoreCase(""))
					{
						ResponseParser.UpdateReview(_ReviewId, review, _DateRead, _Shelves, rating);
					}
					else
					{
						ResponseParser.PostReview(_BookId, review, _DateRead, _Shelves, rating);
					}
					Toast.makeText(v.getContext(), "Review submitted", Toast.LENGTH_LONG).show();
					finish();
				}
				catch (Exception e)
				{
					Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
    @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {
        	case DATE_DIALOG_ID:
        		Calendar currentDate = Calendar.getInstance();
        		return new DatePickerDialog(this,
        				new DatePickerDialog.OnDateSetListener() 
		        	    {
		        			@Override
		        	        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		        			{
		        				TextView textView = (TextView)findViewById(R.id._ReviewBook_DateRead);
		        				_DateRead = String.format("%04d-%02d-%02d", year, monthOfYear+1, dayOfMonth);
		        				textView.setText("Date Read:\t" + _DateRead);
		        	        }
		        	    },
                        currentDate.get(Calendar.YEAR), 
                        currentDate.get(Calendar.MONTH), 
                        currentDate.get(Calendar.DAY_OF_MONTH));
        		
			case PICK_SHELVES_DIALOG:
				final PickShelvesDialog pickShelvesDialog = new PickShelvesDialog(this);
				pickShelvesDialog.set_UserId(_AuthenticatedUserId);
				pickShelvesDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						if (pickShelvesDialog.is_Accepted())
						{
							_Shelves = pickShelvesDialog.get_SelectedShelves();
							String shelvesString = "Shelves:\t";
							if (_Shelves.size() > 0)
							{
								shelvesString += _Shelves.get(0);
							}
							for (int i = 1; i < _Shelves.size(); i++)
							{
								shelvesString += ", " + _Shelves.get(i);
							}
							TextView shelvesLabel = (TextView)findViewById(R.id._ReviewBook_Shelves);
							shelvesLabel.setText(shelvesString);
						}
						removeDialog(PICK_SHELVES_DIALOG);
					}
				});
				return pickShelvesDialog;
        }
        return null;
    }
}
