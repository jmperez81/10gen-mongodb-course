package com.tengen.week2;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class UpdateTest {
	public static void main(String[] args) throws UnknownHostException {
		DBCollection collection = createCollection();
		
		List<String> names = Arrays.asList("alice", "bobby", "cathy", "david", "ethan");
		for (String name : names) {
			collection.insert(new BasicDBObject("_id", name));
		}
		
		collection.update(new BasicDBObject("_id", "alice"), 
				new BasicDBObject("age", 24));
		
		// If we do this, the whole document is updated, so previously updated age is lost
		//		collection.update(new BasicDBObject("_id", "alice"), 
		//				new BasicDBObject("gender", "F"));
		
		// This is the way to add fields without having to know every document's field
		collection.update(new BasicDBObject("_id", "alice"), 
				new BasicDBObject("$set", new BasicDBObject("gender", "F")));

		// Frank is not in the collection, so nothing is done
		collection.update(new BasicDBObject("_id", "frank"), 
				new BasicDBObject("$set", new BasicDBObject("gender", "F")));

		// Now the "upsert" boolean is true so a new Document for frank is created
		collection.update(new BasicDBObject("_id", "frank"), 
				new BasicDBObject("$set", new BasicDBObject("gender", "M")), true, false);

		// Multiple update with the multi boolean
		collection.update(new BasicDBObject(), 
				new BasicDBObject("$set", new BasicDBObject("title", "Dr.")), false, true);
	
		// Remove the document for alice
		collection.remove(new BasicDBObject("_id", "alice"));

		// Remove all the documents in the collection (not only the first!!! important)
		// collection.remove(new BasicDBObject());
		
		printCollection(collection);
	}

	private static DBCollection createCollection() throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("course");
		DBCollection collection = db.getCollection("DotNotationTest");
		collection.drop();
		return collection;
	}

	private static void printCollection(DBCollection collection) {
		DBCursor cursor = collection.find();
		try {
			while(cursor.hasNext()){
				DBObject cur = cursor.next();
				System.out.println(cur);
			}
		} finally {
			cursor.close();
		}
	}
}
