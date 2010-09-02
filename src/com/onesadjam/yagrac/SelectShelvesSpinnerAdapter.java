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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class SelectShelvesSpinnerAdapter extends BaseAdapter
{
	private List<String> _UserShelves = new ArrayList<String>();
	private List<View> _DropDownViews = new ArrayList<View>();
	private TextView _SelectionView;
	private Button _DoneButton;
	
	private Context _Context;
	
	public SelectShelvesSpinnerAdapter(Context c)
	{
		_Context = c;
		
		_DoneButton = new Button(_Context);
		_DoneButton.setText("Done");
		_DropDownViews.add(_DoneButton);
		_UserShelves.add("Done");
		
		_SelectionView = new TextView(_Context);
		_SelectionView.setText("read");
		
		final RadioButton readRadioButton = new RadioButton(_Context);
		readRadioButton.setText("Read");
		readRadioButton.setChecked(true);
		_DropDownViews.add(readRadioButton);
		_UserShelves.add("read");
		
		final RadioButton currentlyReadingRadioButton = new RadioButton(_Context);
		currentlyReadingRadioButton.setText("Currently-Reading");
		_DropDownViews.add(currentlyReadingRadioButton);
		_UserShelves.add("currently-reading");
		
		final RadioButton toReadRadioButton = new RadioButton(_Context);
		toReadRadioButton.setText("To-Read");
		_DropDownViews.add(toReadRadioButton);
		_UserShelves.add("to-read");
		
		readRadioButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				readRadioButton.setChecked(true);
				currentlyReadingRadioButton.setChecked(false);
				toReadRadioButton.setChecked(false);
			}
		});
		currentlyReadingRadioButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				readRadioButton.setChecked(false);
				currentlyReadingRadioButton.setChecked(true);
				toReadRadioButton.setChecked(false);
			}
		});
		toReadRadioButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				readRadioButton.setChecked(false);
				currentlyReadingRadioButton.setChecked(false);
				toReadRadioButton.setChecked(true);
			}
		});
	}
	
	public void AddShelf(String shelf)
	{
		if (shelf.equalsIgnoreCase("read") ||
			shelf.equalsIgnoreCase("to-read") ||
			shelf.equalsIgnoreCase("currently-reading"))
		{
			return;
		}
		_UserShelves.add(shelf);
		CheckBox checkBox = new CheckBox(_Context);
		checkBox.setText(shelf);
		_DropDownViews.add(checkBox);
	}
	
	public void clear()
	{
		_UserShelves.clear();
	}
	
	@Override
	public int getCount()
	{
		return _UserShelves.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _UserShelves.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView textView = _SelectionView;
		List<String> selectedShelves = getSelectedShelves();
		String selectedShelvesString = "";

		if (selectedShelves.size() > 0 )
		{
			selectedShelvesString += selectedShelves.get(0);
		}
		for (int i = 1; i < selectedShelves.size(); i++)
		{
			selectedShelvesString += ", " + selectedShelves.get(i); 
		}
		textView.setText(selectedShelvesString);
		return textView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		if (position == _UserShelves.size()-1)
		{
			return _DropDownViews.get(0);
		}
		return _DropDownViews.get(position+1);
	}
	
	public List<String> getSelectedShelves()
	{
		List<String> selectedShelves = new ArrayList<String>();
		
		for (int i = 1; i < _DropDownViews.size(); i++)
		{
			if (i < 4)
			{
				RadioButton radioShelf = (RadioButton)_DropDownViews.get(i);
				if (radioShelf.isChecked())
				{
					selectedShelves.add(_UserShelves.get(i));
				}
			}
			else
			{
				CheckBox checkShelf = (CheckBox)_DropDownViews.get(i);
				if (checkShelf.isChecked())
				{
					selectedShelves.add(_UserShelves.get(i));
				}
			}
		}
		return selectedShelves;
	}
}
