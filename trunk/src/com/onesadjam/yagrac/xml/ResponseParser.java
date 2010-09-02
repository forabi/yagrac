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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.xml.sax.SAXException;

import android.net.Uri;
import android.sax.RootElement;
import android.util.Xml;

public class ResponseParser
{
	private static String _ConsumerKey = "gSrbJMIPZcESXMUpYEtaQ";
	private static String _ConsumerSecret = "7UptROsPTSejruDpGU59aOoy0oT321NigZLcmjDDy0";
	
	private static CommonsHttpOAuthConsumer _Consumer = new CommonsHttpOAuthConsumer(_ConsumerKey, _ConsumerSecret);
	private static boolean _IsAuthenticated = false;

	public static Response parse(InputStream inputStream) throws IOException, SAXException
	{
		final Response response = new Response();
		
		RootElement root = new RootElement("GoodreadsResponse");
		
		response.set_Book(Book.appendSingletonListener(root));
		response.set_Request(Request.appendSingletonListener(root));
		response.set_User(User.appendSingletonListener(root));
		response.set_Shelves(Shelves.appendSingletonListener(root));
		response.set_Reviews(Reviews.appendSingletonListener(root));
		response.set_Search(Search.appendSingletonListener(root));
		response.set_Followers(Followers.appendSingletonListener(root));
		response.set_Friends(Friends.appendSingletonListener(root));
		response.set_Following(Following.appendSingletonListener(root));
		response.set_Updates(Update.appendArrayListener(root));
		response.set_Review(Review.appendSingletonListener(root));
		
		Xml.parse(inputStream, Xml.Encoding.UTF_8, root.getContentHandler());

		return response;
	}
	
	public static Reviews GetBooksOnShelf(String shelfName, String userId) 
		throws ClientProtocolException, IOException, IllegalStateException, SAXException
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("review/list/" + userId + ".xml");
		builder.appendQueryParameter("key", _ConsumerKey);
		builder.appendQueryParameter("shelf", shelfName);
		builder.appendQueryParameter("v", "2");
		builder.appendQueryParameter("sort", "date_updated");
		builder.appendQueryParameter("order", "d");
		HttpGet getBooksOnShelfRequest = new HttpGet(builder.build().toString());
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;

		response = httpClient.execute(getBooksOnShelfRequest);
		Response responseData = ResponseParser.parse(response.getEntity().getContent());
		
		return responseData.get_Reviews();
	}
	
	public static Review GetReview(String reviewId)
		throws ClientProtocolException, IOException, IllegalStateException, SAXException
	{
		return GetReview(reviewId, 1);
	}
	
	public static Review GetReview(String reviewId, int page)
		throws ClientProtocolException, IOException, IllegalStateException, SAXException
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("review/show.xml");
		builder.appendQueryParameter("key", _ConsumerKey);
		builder.appendQueryParameter("id", reviewId);
		HttpGet getBooksOnShelfRequest = new HttpGet(builder.build().toString());
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;

		response = httpClient.execute(getBooksOnShelfRequest);
		Response responseData = ResponseParser.parse(response.getEntity().getContent());
		
		return responseData.get_Review();
	}
	
	public static List<UserShelf> GetShelvesForUser(String userId) 
		throws ClientProtocolException, IOException, IllegalStateException, SAXException
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("shelf/list");
		builder.appendQueryParameter("format", "xml");
		builder.appendQueryParameter("user_id", userId);
		builder.appendQueryParameter("key", _ConsumerKey);

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getShelvesRequest = new HttpGet(builder.build().toString());
		HttpResponse shelvesResponse = httpClient.execute(getShelvesRequest);
		
		Response shelvesResponseData = ResponseParser.parse(shelvesResponse.getEntity().getContent());

		return shelvesResponseData.get_Shelves().get_UserShelves();
	}
	
	public static List<Update> GetFriendsUpdates() 
		throws 
			ClientProtocolException, 
			IOException, 
			IllegalStateException, 
			SAXException, 
			OAuthMessageSignerException, 
			OAuthExpectationFailedException, 
			OAuthCommunicationException
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("updates/friends.xml");

		HttpGet getUpdatesRequest = new HttpGet(builder.build().toString());

		_Consumer.sign(getUpdatesRequest);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(getUpdatesRequest);
		
		Response updatesResponse = ResponseParser.parse(response.getEntity().getContent());
		return updatesResponse.get_Updates();
	}
	
	public static Followers GetFollowers(String userId) 
		throws ClientProtocolException, IllegalStateException, IOException, SAXException
	{
		return GetFollowers(userId, 1);
	}
	
	public static Followers GetFollowers(String userId, int page) 
		throws ClientProtocolException, IOException, IllegalStateException, SAXException
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("user/" + userId + "/followers.xml");
		builder.appendQueryParameter("key", _ConsumerKey);
		builder.appendQueryParameter("sort", "first_name");
		builder.appendQueryParameter("page", Integer.toString(page));

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getFriendsRequest = new HttpGet(builder.build().toString());
		HttpResponse followersResponse;

		followersResponse = httpClient.execute(getFriendsRequest);
			
		Response followersResponseData = ResponseParser.parse(followersResponse.getEntity().getContent());
			
		return followersResponseData.get_Followers();
	}
	
	public static void AddBookToShelf(String bookId, String shelfName) throws Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.goodreads.com/shelf/add_to_shelf.xml");
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("book_id", bookId));
		parameters.add(new BasicNameValuePair("name", shelfName));
		post.setEntity(new UrlEncodedFormEntity(parameters));
		_Consumer.sign(post);
		
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() != 201)
		{
			throw new Exception(response.getStatusLine().toString());
		}
	}

	public static void UpdateReview(
			String reviewId, 
			String review,
			String dateRead,
			List<String> shelves,
			int rating) throws Exception
	{
		if (shelves.size() == 0)
		{
			throw new Exception("Select at least one shelf.");
		}
		if (rating < 1 || rating > 5)
		{
			throw new Exception("Review rating must be 1-5 stars.");
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.goodreads.com/review/" + reviewId + ".xml");
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("shelf", shelves.get(0)));
		parameters.add(new BasicNameValuePair("review[review]", review));
		parameters.add(new BasicNameValuePair("review[read-at]", dateRead));
		parameters.add(new BasicNameValuePair("review[rating]", Integer.toString(rating)));
		post.setEntity(new UrlEncodedFormEntity(parameters));
		_Consumer.sign(post);
		
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() != 201)
		{
			throw new Exception(response.getStatusLine().toString());
		}
	}

	public static void PostReview(
			String bookId, 
			String review,
			String dateRead,
			List<String> shelves,
			int rating) throws Exception
	{
		if (shelves.size() == 0)
		{
			throw new Exception("Select at least one shelf.");
		}
		if (rating < 1 || rating > 5)
		{
			throw new Exception("Review rating must be 1-5 stars.");
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.goodreads.com/review.xml");
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("shelf", shelves.get(0)));
		parameters.add(new BasicNameValuePair("review[review]", review));
		parameters.add(new BasicNameValuePair("review[read-at]", dateRead));
		parameters.add(new BasicNameValuePair("book_id", bookId));
		parameters.add(new BasicNameValuePair("review[rating]", Integer.toString(rating)));
		post.setEntity(new UrlEncodedFormEntity(parameters));
		_Consumer.sign(post);
		
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() != 201)
		{
			throw new Exception(response.getStatusLine().toString());
		}
	}
	
	public static void PostStatusUpdate(String comment)	throws Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.goodreads.com/user_status.xml");
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("user_status[body]", comment));
		post.setEntity(new UrlEncodedFormEntity(parameters));
		_Consumer.sign(post);
		
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() != 201)
		{
			throw new Exception(response.getStatusLine().toString());
		}
	}
	
	public static void PostStatusUpdate(String book, String page, String comment) 
		throws 
			Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.goodreads.com/user_status.xml");
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("user_status[book_id]", book));
		parameters.add(new BasicNameValuePair("user_status[page]", page));
		parameters.add(new BasicNameValuePair("user_status[body]", comment));
		post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		_Consumer.sign(post);
		
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() != 201)
		{
			throw new Exception(response.getStatusLine().toString());
		}
	}
	
	public static User GetUserDetails(String userId) throws Exception
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("user/show/" + userId + ".xml");
		builder.appendQueryParameter("key", _ConsumerKey);

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(builder.build().toString());
		HttpResponse response;

		response = httpClient.execute(getRequest);
			
		Response responseData = ResponseParser.parse(response.getEntity().getContent());
			
		return responseData.get_User();
	}
	
	public static Following GetFollowing(String userId) throws Exception
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("user/" + userId + "/following");
		builder.appendQueryParameter("format", "xml");
		builder.appendQueryParameter("key", _ConsumerKey);
		builder.appendQueryParameter("sort", "first_name");

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getFriendsRequest = new HttpGet(builder.build().toString());
		HttpResponse followingResponse;

		followingResponse = httpClient.execute(getFriendsRequest);
		
		Response followingResponseData = ResponseParser.parse(followingResponse.getEntity().getContent());

		return followingResponseData.get_Following();
	}
	
	public static Friends GetFriends(String userId) throws Exception
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("friend/user/" + userId);
		builder.appendQueryParameter("format", "xml");
		builder.appendQueryParameter("key", _ConsumerKey);
		builder.appendQueryParameter("sort", "first_name");

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getFriendsRequest = new HttpGet(builder.build().toString());
		HttpResponse friendsResponse;
		
		friendsResponse = httpClient.execute(getFriendsRequest);
		
		Response friendsResponseData = ResponseParser.parse(friendsResponse.getEntity().getContent());
		
		return friendsResponseData.get_Friends();
	}
	
	public static Search Search(String searchString) throws Exception
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("search/search");
		builder.appendQueryParameter("format", "xml");
		builder.appendQueryParameter("key", _ConsumerKey);
		builder.appendQueryParameter("q", searchString);

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getSearchResponse = new HttpGet(builder.build().toString());
		HttpResponse searchResponse = httpClient.execute(getSearchResponse);
		
		Response searchResponseData = ResponseParser.parse(searchResponse.getEntity().getContent());
		
		return searchResponseData.get_Search();
	}
	
	public static Book GetReviewsForBook(String bookId) throws Exception
	{
		return GetReviewsForBook(bookId, 1);
	}

	public static Book GetReviewsForBook(String bookId, int page) throws Exception
	{
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("www.goodreads.com");
		builder.path("book/show/" + bookId);
		builder.appendQueryParameter("key", _ConsumerKey);
		builder.appendQueryParameter("page", Integer.toString(page));

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getResponse = new HttpGet(builder.build().toString());
		HttpResponse response = httpClient.execute(getResponse);
		
		Response responseData = ResponseParser.parse(response.getEntity().getContent());
		
		return responseData.get_Book();
	}

	private static void set_IsAuthenticated(boolean _IsAuthenticated)
	{
		ResponseParser._IsAuthenticated = _IsAuthenticated;
	}

	public static boolean get_IsAuthenticated()
	{
		return _IsAuthenticated;
	}
	
	public static void SetTokenWithSecret(String token, String tokenSecret)
	{
		_Consumer.setTokenWithSecret(token, tokenSecret);
		set_IsAuthenticated(true);
	}
}
