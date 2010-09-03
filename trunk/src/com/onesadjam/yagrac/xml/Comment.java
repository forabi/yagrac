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

public class Comment
{
	private String _Id;
	private String _Body;
	private User _User;
	private String _CreatedAt;
	private String _UpdatedAt;
	
	public void clear()
	{
		this.set_Id("");
		this.set_Body("");
		this.set_UpdatedAt("");
		this.set_CreatedAt("");
		_User.clear();
	}
	
	public Comment copy()
	{
		Comment copyComment = new Comment();
		
		copyComment.set_Id(this.get_Id());
		copyComment.set_Body(this.get_Body());
		copyComment.set_CreatedAt(this.get_CreatedAt());
		copyComment.set_UpdatedAt(this.get_UpdatedAt());
		copyComment.set_User(_User.copy());

		return copyComment;
	}
	
	public static Comment appendSingletonListener(Element parentElement, int depth)
	{
		final Comment comment = new Comment();
		final Element commentElement = parentElement.getChild("comment");
		
		appendCommonListeners(commentElement, comment, depth);
		
		return comment;
	}
	
	public static final List<Comment> appendArrayListener(Element parentElement, int depth)
	{
		final List<Comment> comments = new ArrayList<Comment>();
		final Comment comment = new Comment();
		final Element commentElement = parentElement.getChild("comment");
		
		appendCommonListeners(commentElement, comment, depth);
		
		commentElement.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				comments.add(comment.copy());
				comment.clear();
			}
		});
		
		return comments;
	}
	
	private static void appendCommonListeners(final Element commentElement, final Comment comment, int depth)
	{
		commentElement.getChild("id").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				comment.set_Id(body);
			}
		});

		commentElement.getChild("created_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				comment.set_CreatedAt(body);
			}
		});

		commentElement.getChild("udpated_at").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				comment.set_UpdatedAt(body);
			}
		});

		commentElement.getChild("body").setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				comment.set_Body(body);
			}
		});
		
		comment.set_User(User.appendSingletonListener(commentElement, depth + 1));
	}
	
	public String get_Id()
	{
		return _Id;
	}
	public void set_Id(String _Id)
	{
		this._Id = _Id;
	}
	public String get_Body()
	{
		return _Body;
	}
	public void set_Body(String _Body)
	{
		this._Body = _Body;
	}
	public User get_User()
	{
		return _User;
	}
	public void set_User(User _User)
	{
		this._User = _User;
	}
	public String get_CreatedAt()
	{
		return _CreatedAt;
	}
	public void set_CreatedAt(String _CreatedAt)
	{
		this._CreatedAt = _CreatedAt;
	}
	public String get_UpdatedAt()
	{
		return _UpdatedAt;
	}
	public void set_UpdatedAt(String _UpdatedAt)
	{
		this._UpdatedAt = _UpdatedAt;
	}
}
