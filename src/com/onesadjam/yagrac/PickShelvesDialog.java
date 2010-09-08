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

import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.UserShelf;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

public class PickShelvesDialog extends Dialog
{
	private List<String> _SelectedShelves = new ArrayList<String>();
	private String _UserId;
	private Context _Context;
	private String _BookId;
	private String _ReviewId;
	private List<CheckBox> _UserShelfCheckboxes = new ArrayList<CheckBox>();
	private List<RadioButton> _SystemShelfButtons = new ArrayList<RadioButton>();
	private boolean _Accepted = false;
	
	public PickShelvesDialog(Context context)
	{
		super(context);
		_Context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pickshelves);
		setTitle("Add to Shelves");
				
		try
		{
			// TODO: select the current shelves the book belongs to.
//			Review review = ResponseParser.GetReview(_ReviewId);
			LinearLayout linearLayout = (LinearLayout)findViewById(R.id._PickShelves_UserShelves);
			RadioButton readButton = (RadioButton)findViewById(R.id._PickShelves_ReadButton);
			RadioButton toReadButton = (RadioButton)findViewById(R.id._PickShelves_ToReadButton);
			RadioButton currentlyReadingButton = (RadioButton)findViewById(R.id._PickShelves_CurrentlyReadingButton);
			_SystemShelfButtons.add(readButton);
			_SystemShelfButtons.add(currentlyReadingButton);
			_SystemShelfButtons.add(toReadButton);

			List<UserShelf> userShelves = ResponseParser.GetShelvesForUser(_UserId);
			for (int i = 0; i < userShelves.size(); i++)
			{
				String shelfName = userShelves.get(i).get_Name();
				if ( shelfName.equalsIgnoreCase("read") ||
					 shelfName.equalsIgnoreCase("to-read") ||
					 shelfName.equalsIgnoreCase("currently-reading") )
				{
					continue;
				}

				CheckBox shelfCheckbox = new CheckBox(_Context);
				shelfCheckbox.setText(shelfName);
				linearLayout.addView(shelfCheckbox);
				_UserShelfCheckboxes.add(shelfCheckbox);
			}
			
			Button acceptButton = (Button)findViewById(R.id._PickShelves_OkButton);
			acceptButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					_Accepted = true;
					for (int i = 0; i < _SystemShelfButtons.size(); i++)
					{
						if (_SystemShelfButtons.get(i).isChecked())
						{
							_SelectedShelves.add(_SystemShelfButtons.get(i).getText().toString());
							break;
						}
					}
					for (int i = 0; i < _UserShelfCheckboxes.size(); i++)
					{
						if (_UserShelfCheckboxes.get(i).isChecked())
						{
							_SelectedShelves.add(_UserShelfCheckboxes.get(i).getText().toString());
						}
					}
					dismiss();
				}
			});
		}
		catch (Exception e)
		{
			Toast.makeText(_Context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public List<String> get_SelectedShelves()
	{
		return _SelectedShelves;
	}

	public String get_UserId()
	{
		return _UserId;
	}

	public void set_UserId(String _UserId)
	{
		this._UserId = _UserId;
	}

	public void set_BookId(String _BookId)
	{
		this._BookId = _BookId;
	}

	public String get_BookId()
	{
		return _BookId;
	}

	public void set_ReviewId(String _ReviewId)
	{
		this._ReviewId = _ReviewId;
	}

	public String get_ReviewId()
	{
		return _ReviewId;
	}
	
	public boolean is_Accepted()
	{
		return _Accepted;
	}
}
