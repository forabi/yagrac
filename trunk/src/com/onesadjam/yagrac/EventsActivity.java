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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.onesadjam.yagrac.xml.Event;
import com.onesadjam.yagrac.xml.ResponseParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity used to display the list of events that are scheduled in the user's area.
 * @author ajones
 *
 */
public class EventsActivity extends Activity
{
	private LayoutInflater _LayoutInflater;
	private Context _Context;
	private ProgressDialog _BusySpinner;
	private ArrayAdapter<Event> _Adapter;
	private Location _LatestLocation = null;
	private LocationManager _LocationManager = null;
	private LocationListener _LocationListener = null;
	private static final SimpleDateFormat _GoodreadsDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		_Context = this;
		
		setContentView(R.layout.eventsactivitylayout);
		
		ListView eventsList = (ListView)findViewById(R.id._EventsActivity_ListView);
		
		_LayoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		_Adapter = new ArrayAdapter<Event>(this, R.layout.eventitemlayout)
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View eventItem;
				if (convertView == null)
				{
					eventItem = (View)_LayoutInflater.inflate(R.layout.eventitemlayout, null);
				}
				else
				{
					eventItem = convertView;
				}
				
				Event event = getItem(position);
				
				ImageView imageView = (ImageView)eventItem.findViewById(R.id._EventItem_Image);
				if (event.get_ImageUrl() != null && event.get_ImageUrl() != "")
				{
					try
					{
						LazyImageLoader.LazyLoadImageView(
								_Context, 
								new URL(event.get_ImageUrl()), 
								R.drawable.nocover,
								imageView);
					}
					catch (MalformedURLException e)
					{
						e.printStackTrace();
					}
				}

				String formattedDate = "";
				try
				{
					if (event.get_EventAt() != null)
					{
						Date timestamp = _GoodreadsDateFormat.parse(event.get_EventAt());
						formattedDate = timestamp.toLocaleString();
					}
				}
				catch (Exception e)
				{
					formattedDate = event.get_EventAt();
				}
				TextView text = (TextView)eventItem.findViewById(R.id._EventItem_EventDateTime);
				text.setText(formattedDate);
				
				text = (TextView)eventItem.findViewById(R.id._EventItem_Title);
				text.setText(event.get_Title());

				text = (TextView)eventItem.findViewById(R.id._EventItem_Description);
				text.setMovementMethod(LinkMovementMethod.getInstance());
				text.setText(Html.fromHtml(event.get_Description()));

				text = (TextView)eventItem.findViewById(R.id._EventItem_Venue);
				text.setText(event.get_Venue());

				text = (TextView)eventItem.findViewById(R.id._EventItem_Address);
				text.setText(event.get_City() + ", " + event.get_StateCode() + ", " +event.get_CountryCode());

				return eventItem;
			}
		};
		eventsList.setAdapter(_Adapter);
		
		_BusySpinner = ProgressDialog.show(this, "", "Acquiring location...");
		new AcquireLocationTask().execute();

	}
	
	private class AcquireLocationTask extends AsyncTask<Void, Void, Location>
	{
		@Override
		protected Location doInBackground(Void... params)
		{
			return _LatestLocation;
		}

		@Override
		protected void onPostExecute(Location result)
		{
			_BusySpinner.dismiss();
			if (result == null)
			{
				Toast.makeText(_Context, "Unable to retrieve location. Loading default event list.", Toast.LENGTH_LONG);
			}
			_BusySpinner = ProgressDialog.show(_Context, "", "Loading events...");
			
			new LoadEventsTask().execute(result);
		}
	}

	private class LoadEventsTask extends AsyncTask<Location, Void, List<Event>>
	{
		@Override
		protected List<Event> doInBackground(Location... params)
		{
			try
			{
				if (params.length > 0 && params[0] != null)
				{
					Location latestLocation = params[0];
					return ResponseParser.GetNearbyEvents(latestLocation);
				}
				else
				{
					return ResponseParser.GetEvents();
				}
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(List<Event> result)
		{
			_BusySpinner.dismiss();
			if (result == null)
			{
				Toast.makeText(_Context, "Unable to retrieve events.", Toast.LENGTH_LONG);
				return;
			}
			
			_Adapter.clear();
			for (int i = 0; i < result.size(); i++ )
			{
				_Adapter.add(result.get(i));
			}
			_Adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		
		_LocationManager.removeUpdates(_LocationListener);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		_LocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		_LatestLocation = _LocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		_LocationListener = new LocationListener() 
		{
			@Override
		    public void onLocationChanged(Location location) 
		    {
				_LatestLocation = location;
		    }

			@Override
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

			@Override
		    public void onProviderEnabled(String provider) {}

			@Override
		    public void onProviderDisabled(String provider) {}
		  };

		_LocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _LocationListener);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.eventsmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id._Events_RefreshMenuItem:
				_BusySpinner = ProgressDialog.show(this, "", "Acquiring location...");
				new AcquireLocationTask().execute();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}