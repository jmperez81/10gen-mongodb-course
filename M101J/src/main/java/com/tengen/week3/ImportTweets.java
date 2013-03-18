package com.tengen.week3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class ImportTweets {
	public static void main(String[] args) throws IOException, ParseException {		
		final String screenName = args.length > 0 ? args[0] : "jmpr81";
		
		List<DBObject> tweets = getLatestTweets(screenName);
		
		MongoClient client = new MongoClient();
		DBCollection tweetsCollection = client.getDB("course").getCollection("twitter");
		
//		tweetsCollection.drop();
		
		for (DBObject cur : tweets) {
			// We want the id from the feed to be the _id of the document
			massageTweetId(cur);
			
			// Process tweet
			massageTweet(cur);
			
			// Adds the screenName
			cur.put("screen_name", screenName);
			
			// We want to create the elements if not exist with upsert flag
			tweetsCollection.update(new BasicDBObject("_id", cur.get("_id")), cur, true, false);
		}
		
		System.out.println("Tweet count: " + tweetsCollection.count());
		
		client.close();		
	}

	private static void massageTweet(DBObject cur) throws ParseException {
		// Reformat created_at field
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d H:m:s Z y", Locale.ENGLISH);
		cur.put("created_at", sdf.parse((String) cur.get("created_at")));

		// Filter useless fields in user field
		DBObject userDoc = (DBObject) cur.get("user");
		Iterator<String> keyIterator = userDoc.keySet().iterator();
		while(keyIterator.hasNext()) {
			String key = keyIterator.next();
			if (!(key.equals("id") || key.equals("name") || key.equals("screen_name"))) {
				keyIterator.remove();
			}
		}
	}

	private static void massageTweetId(DBObject cur) {
		Object id = cur.get("id");
		cur.removeField("id");
		cur.put("_id", id);
	}

	@SuppressWarnings("unchecked")
	private static List<DBObject> getLatestTweets(String screenName) throws IOException {
		URL url = new URL("http://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + screenName + "&include_rts=1");
		
		InputStream is = url.openStream();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int retVal;
		while((retVal = is.read()) != -1) {
			os.write(retVal);
		}
		
		final String tweetsString = os.toString();
		
		return (List<DBObject>) JSON.parse(tweetsString);
	}
}
