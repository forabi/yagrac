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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * Class for loading images in a background thread.
 * @author ajones
 *
 */
public class LazyImageLoader
{
	private static Map<URL, Drawable> _CachedImages = new HashMap<URL, Drawable>();
	
	/**
	 * Loads an image in a background thread and populates a drawable with the image when complete.
	 * If the request for the image fails for any reason, the background thread terminates and
	 * leaves the drawable unmodified.
	 * @param context Current context of the drawable to be updated.
	 * @param imageUrl URL of the image to retrieve.
	 * @param defaultImage Image to use for the drawable until the requested image is received.
	 * @param convertImageView Drawable object to populate with the image.  If null, creates a new ImageView.
	 * @return Reference to the ImageView that will display the image.
	 */
	public static ImageView LazyLoadImageView(
			Context context, 
			final URL imageUrl, 
			int defaultImage, 
			final ImageView convertImageView) 
	{
		final ImageView imageView = (convertImageView == null) ? new ImageView(context) : convertImageView;

		if (_CachedImages.containsKey(imageUrl))
		{
			imageView.setImageDrawable(_CachedImages.get(imageUrl));
			return imageView;
		}
		
		imageView.setImageResource(defaultImage);
		
		final Handler imageLoadedHandler = new Handler() 
		{
    		@Override
    		public void handleMessage(Message message) 
    		{
    			
    			imageView.setImageDrawable((Drawable) message.obj);
    		}
    	};
    	
    	Thread thread = new Thread()
    	{
    		@Override
    		public void run() 
    		{
    			Drawable webImage;
				try
				{
					webImage = Drawable.createFromStream((InputStream)imageUrl.getContent(), "src");
					if (webImage != null)
					{
						_CachedImages.put(imageUrl, webImage);
		    			Message message = imageLoadedHandler.obtainMessage(1, webImage);
		    			imageLoadedHandler.sendMessage(message);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
    		}
    	};
    	thread.start();
		
		return imageView;
	}
}
