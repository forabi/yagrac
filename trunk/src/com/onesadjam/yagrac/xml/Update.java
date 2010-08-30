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

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.StartElementListener;

public class Update
{
	private String _UpdateType;
	private String _ActionText;
	private String _Link;
	private String _ImageUrl;
	private String _UpdatedAt;
	private Actor _Actor = new Actor();
	private Action _Action = new Action();
	private UpdateObject _UpdateObject = new UpdateObject();
	
	public void clear()
	{
		this.set_UpdateType("");
		this.set_ActionText("");
		this.set_Link("");
		this.set_ImageUrl("");
		this.set_UpdatedAt("");
		_Actor.clear();
		_Action.clear();
		_UpdateObject.clear();
	}
	
	public Update copy()
	{
		Update updateCopy = new Update();
		
		updateCopy.set_UpdateType(this.get_UpdateType());
		updateCopy.set_ActionText(this.get_ActionText());
		updateCopy.set_Link(this.get_Link());
		updateCopy.set_ImageUrl(this.get_ImageUrl());
		updateCopy.set_UpdatedAt(this.get_UpdatedAt());
		updateCopy.set_Actor(_Actor.copy());
		updateCopy.set_Action(_Action.copy());
		updateCopy.set_UpdateObject(_UpdateObject.copy());
		
		return updateCopy;
	}
	
	public static Update appendSingletonListener(Element parentElement)
	{
		final Update update = new Update();
		final Element updateElement = parentElement.getChild("update");
		
		appendCommonListeners(updateElement, update);
		
		return update;
	}
	
	public static List<Update> appendArrayListener(Element parentElement)
	{
		final Update update = new Update();
		final List<Update> updateList = new ArrayList<Update>();
		final Element updatesElement = parentElement.getChild("updates"); 
		final Element updateElement = updatesElement.getChild("update");
		
		updateElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				updateList.add(update.copy());
				update.clear();
			}
		});
		
		appendCommonListeners(updateElement, update);
		
		return updateList;
	}
	
	private static void appendCommonListeners(final Element updateElement, final Update update)
	{
		updateElement.setStartElementListener(new StartElementListener()
		{
			@Override
			public void start(Attributes attributes)
			{
				update.set_UpdateType(attributes.getValue("type"));
			}
		});
		
		updateElement.getChild("action_text").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				update.set_ActionText(body);
			}
		});
		
		updateElement.getChild("link").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				update.set_Link(body);
			}
		});
		
		updateElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				update.set_ImageUrl(body);
			}
		});
		
		updateElement.getChild("updated_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				update.set_UpdatedAt(body);
			}
		});
		
		update.set_Actor(Actor.appendSingletonListener(updateElement));
		update.set_Action(Action.appendSingletonListener(updateElement));
		update.set_UpdateObject(UpdateObject.appendSingletonListener(updateElement));
	}
	
	public void set_Link(String _Link)
	{
		this._Link = _Link;
	}
	public String get_Link()
	{
		return _Link;
	}
	public String get_UpdateType()
	{
		return _UpdateType;
	}
	public void set_UpdateType(String _UpdateType)
	{
		this._UpdateType = _UpdateType;
	}
	public String get_ActionText()
	{
		return _ActionText;
	}
	public void set_ActionText(String _ActionText)
	{
		this._ActionText = _ActionText;
	}
	public String get_ImageUrl()
	{
		return _ImageUrl;
	}
	public void set_ImageUrl(String _ImageUrl)
	{
		this._ImageUrl = _ImageUrl;
	}
	public String get_UpdatedAt()
	{
		return _UpdatedAt;
	}
	public void set_UpdatedAt(String _UpdatedAt)
	{
		this._UpdatedAt = _UpdatedAt;
	}
	public Actor get_Actor()
	{
		return _Actor;
	}
	public void set_Actor(Actor _Actor)
	{
		this._Actor = _Actor;
	}
	public Action get_Action()
	{
		return _Action;
	}
	public void set_Action(Action _Action)
	{
		this._Action = _Action;
	}

	public UpdateObject get_UpdateObject()
	{
		return _UpdateObject;
	}

	public void set_UpdateObject(UpdateObject _UpdateObject)
	{
		this._UpdateObject = _UpdateObject;
	}
	
}
