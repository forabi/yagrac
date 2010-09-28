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
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;

public class Event
{
	private String _Access;
	private String _Address;
	private boolean _CanInvite;
	private String _City;
	private String _CountryCode;
	private String _CreatedAt;
	private String _EventAt;
	private int _EventAttendingCount;
	private int _EventResponseCount;
	private String _EventType;
	private String _Id;
	private String _PostalCode;
	private int _PublicFlag;
	private String _ReminderAt;
	private int _ResourceId;
	private String _ResourceType;
	private String _Source;
	private String _SourceUrl;
	private String _StateCode;
	private String _Title;
	private String _UpdatedAt;
	private String _UserId;
	private String _Venue;
	private String _ImageUrl;
	private String _Link;
	private String _Description;
	
	public void clear()
	{
		this.set_Access("");
		this.set_Address("");
		this.set_CanInvite(false);
		this.set_City("");
		this.set_CountryCode("");
		this.set_CreatedAt("");
		this.set_EventAt("");
		this.set_EventAttendingCount(0);
		this.set_EventResponseCount(0);
		this.set_EventType("");
		this.set_Id("");
		this.set_PostalCode("");
		this.set_PublicFlag(0);
		this.set_ReminderAt("");
		this.set_ResourceId(0);
		this.set_ResourceType("");
		this.set_Source("");
		this.set_SourceUrl("");
		this.set_StateCode("");
		this.set_Title("");
		this.set_UpdatedAt("");
		this.set_UserId("");
		this.set_Venue("");
		this.set_ImageUrl("");
		this.set_Link("");
		this.set_Description("");
	}
	
	public Event copy()
	{
		Event eventCopy = new Event();
		
		eventCopy.set_Access(this.get_Access());
		eventCopy.set_Address(this.get_Address());
		eventCopy.set_CanInvite(this.get_CanInvite());
		eventCopy.set_City(this.get_City());
		eventCopy.set_CountryCode(this.get_CountryCode());
		eventCopy.set_CreatedAt(this.get_CreatedAt());
		eventCopy.set_EventAt(this.get_EventAt());
		eventCopy.set_EventAttendingCount(this.get_EventAttendingCount());
		eventCopy.set_EventResponseCount(this.get_EventResponseCount());
		eventCopy.set_EventType(this.get_EventType());
		eventCopy.set_Id(this.get_Id());
		eventCopy.set_PostalCode(this.get_PostalCode());
		eventCopy.set_PublicFlag(this.get_PublicFlag());
		eventCopy.set_ReminderAt(this.get_ReminderAt());
		eventCopy.set_ResourceId(this.get_ResourceId());
		eventCopy.set_ResourceType(this.get_ResourceType());
		eventCopy.set_Source(this.get_Source());
		eventCopy.set_SourceUrl(this.get_SourceUrl());
		eventCopy.set_StateCode(this.get_StateCode());
		eventCopy.set_Title(this.get_Title());
		eventCopy.set_UpdatedAt(this.get_UpdatedAt());
		eventCopy.set_UserId(this.get_UserId());
		eventCopy.set_Venue(this.get_Venue());
		eventCopy.set_ImageUrl(this.get_ImageUrl());
		eventCopy.set_Link(this.get_Link());
		eventCopy.set_Description(this.get_Description());
		
		return eventCopy;
	}
	
	public static Event appendSingletonListener(Element parentElement, int depth)
	{
		final Event event = new Event();
		final Element eventElement = parentElement.getChild("event");
		
		appendCommonListeners(eventElement, event, depth);
				
		return event;
	}
	
	public static List<Event> appendArrayListener(Element parentElement, int depth)
	{
		final List<Event> events = new ArrayList<Event>();
		final Element eventsElement = parentElement.getChild("events");
		
		final Event event = new Event();
		final Element eventElement = eventsElement.getChild("event");
		
		eventElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				events.add(event.copy());
				event.clear();
			}
		});
		
		appendCommonListeners(eventElement, event, depth);
		
		return events;
	}
	
	private static void appendCommonListeners(final Element eventElement, final Event event, int depth)
	{
		eventElement.getChild("access").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Access(body);
			}
		});

		eventElement.getChild("address").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Address(body);
			}
		});

		eventElement.getChild("can_invite_flag").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				boolean parsedBoolean = false;
				try
				{
					parsedBoolean = Boolean.parseBoolean(body);
				}
				catch (Exception e){}
				event.set_CanInvite(parsedBoolean);
			}
		});

		eventElement.getChild("city").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_City(body);
			}
		});

		eventElement.getChild("country_code").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_CountryCode(body);
			}
		});

		eventElement.getChild("created_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_CreatedAt(body);
			}
		});

		eventElement.getChild("event_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_EventAt(body);
			}
		});

		eventElement.getChild("event_attending_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				int count = 0;
				try
				{
					count = Integer.parseInt(body);
				}
				catch (Exception e) {}
				
				event.set_EventAttendingCount(count);
			}
		});

		eventElement.getChild("event_responses_count").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				int count = 0;
				try
				{
					count = Integer.parseInt(body);
				}
				catch (Exception e) {}
				
				event.set_EventResponseCount(count);
			}
		});

		eventElement.getChild("event_type").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_EventType(body);
			}
		});

		eventElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Id(body);
			}
		});

		eventElement.getChild("postal_code").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_PostalCode(body);
			}
		});

		eventElement.getChild("public_flag").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				int count = 0;
				try
				{
					count = Integer.parseInt(body);
				}
				catch (Exception e) {}
				
				event.set_PublicFlag(count);
			}
		});

		eventElement.getChild("reminder_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_ReminderAt(body);
			}
		});

		eventElement.getChild("resource_id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				int count = 0;
				try
				{
					count = Integer.parseInt(body);
				}
				catch (Exception e) {}
				
				event.set_ResourceId(count);
			}
		});

		eventElement.getChild("resource_type").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_ResourceType(body);
			}
		});

		eventElement.getChild("source").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Source(body);
			}
		});

		eventElement.getChild("source_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_SourceUrl(body);
			}
		});

		eventElement.getChild("state_code").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_StateCode(body);
			}
		});

		eventElement.getChild("title").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Title(body);
			}
		});

		eventElement.getChild("updated_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_UpdatedAt(body);
			}
		});

		eventElement.getChild("user_id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_UserId(body);
			}
		});

		eventElement.getChild("venue").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Venue(body);
			}
		});

		eventElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_ImageUrl(body);
			}
		});

		eventElement.getChild("link").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Link(body);
			}
		});

		eventElement.getChild("description").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				event.set_Description(body);
			}
		});
	}

	public String get_Access()
	{
		return _Access;
	}

	public void set_Access(String _Access)
	{
		this._Access = _Access;
	}

	public String get_Address()
	{
		return _Address;
	}

	public void set_Address(String _Address)
	{
		this._Address = _Address;
	}

	public boolean get_CanInvite()
	{
		return _CanInvite;
	}

	public void set_CanInvite(boolean _CanInvite)
	{
		this._CanInvite = _CanInvite;
	}

	public String get_City()
	{
		return _City;
	}

	public void set_City(String _City)
	{
		this._City = _City;
	}

	public String get_CountryCode()
	{
		return _CountryCode;
	}

	public void set_CountryCode(String _CountryCode)
	{
		this._CountryCode = _CountryCode;
	}

	public String get_CreatedAt()
	{
		return _CreatedAt;
	}

	public void set_CreatedAt(String _CreatedAt)
	{
		this._CreatedAt = _CreatedAt;
	}

	public String get_EventAt()
	{
		return _EventAt;
	}

	public void set_EventAt(String _EventAt)
	{
		this._EventAt = _EventAt;
	}

	public int get_EventAttendingCount()
	{
		return _EventAttendingCount;
	}

	public void set_EventAttendingCount(int _EventAttendingCount)
	{
		this._EventAttendingCount = _EventAttendingCount;
	}

	public int get_EventResponseCount()
	{
		return _EventResponseCount;
	}

	public void set_EventResponseCount(int _EventResponseCount)
	{
		this._EventResponseCount = _EventResponseCount;
	}

	public String get_Id()
	{
		return _Id;
	}

	public void set_Id(String _Id)
	{
		this._Id = _Id;
	}

	public String get_PostalCode()
	{
		return _PostalCode;
	}

	public void set_PostalCode(String _PostalCode)
	{
		this._PostalCode = _PostalCode;
	}

	public int get_PublicFlag()
	{
		return _PublicFlag;
	}

	public void set_PublicFlag(int _PublicFlag)
	{
		this._PublicFlag = _PublicFlag;
	}

	public String get_ReminderAt()
	{
		return _ReminderAt;
	}

	public void set_ReminderAt(String _ReminderAt)
	{
		this._ReminderAt = _ReminderAt;
	}

	public int get_ResourceId()
	{
		return _ResourceId;
	}

	public void set_ResourceId(int _ResourceId)
	{
		this._ResourceId = _ResourceId;
	}

	public String get_ResourceType()
	{
		return _ResourceType;
	}

	public void set_ResourceType(String _ResourceType)
	{
		this._ResourceType = _ResourceType;
	}

	public String get_Source()
	{
		return _Source;
	}

	public void set_Source(String _Source)
	{
		this._Source = _Source;
	}

	public String get_SourceUrl()
	{
		return _SourceUrl;
	}

	public void set_SourceUrl(String _SourceUrl)
	{
		this._SourceUrl = _SourceUrl;
	}

	public String get_StateCode()
	{
		return _StateCode;
	}

	public void set_StateCode(String _StateCode)
	{
		this._StateCode = _StateCode;
	}

	public String get_Title()
	{
		return _Title;
	}

	public void set_Title(String _Title)
	{
		this._Title = _Title;
	}

	public String get_UpdatedAt()
	{
		return _UpdatedAt;
	}

	public void set_UpdatedAt(String _UpdatedAt)
	{
		this._UpdatedAt = _UpdatedAt;
	}

	public String get_UserId()
	{
		return _UserId;
	}

	public void set_UserId(String _UserId)
	{
		this._UserId = _UserId;
	}

	public String get_Venue()
	{
		return _Venue;
	}

	public void set_Venue(String _Venue)
	{
		this._Venue = _Venue;
	}

	public String get_ImageUrl()
	{
		return _ImageUrl;
	}

	public void set_ImageUrl(String _ImageUrl)
	{
		this._ImageUrl = _ImageUrl;
	}

	public String get_Link()
	{
		return _Link;
	}

	public void set_Link(String _Link)
	{
		this._Link = _Link;
	}

	public String get_Description()
	{
		return _Description;
	}

	public void set_Description(String _Description)
	{
		this._Description = _Description;
	}

	public String get_EventType()
	{
		return _EventType;
	}

	public void set_EventType(String _EventType)
	{
		this._EventType = _EventType;
	}
}
