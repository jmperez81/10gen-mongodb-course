package com.tengen.week6;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class ReadPreferenceTest {
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		MongoClient client = new MongoClient(Arrays.asList(new ServerAddress("localhost", 20017), new ServerAddress("localhost", 20018), new ServerAddress("localhost", 20019)));
		
		// 1) Read preference can be set globally at client
		client.setReadPreference(ReadPreference.primary());
		
		
		DB db = client.getDB("course");			
		DBCollection coll = db.getCollection("write.test");

		// 2) Read preference can be set globally at collection
		coll.setReadPreference(ReadPreference.primaryPreferred());
		
		
		// 3) Read preference can be set per operation
		DBCursor cursor = coll.find().setReadPreference(ReadPreference.nearest());
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}
	}
}
