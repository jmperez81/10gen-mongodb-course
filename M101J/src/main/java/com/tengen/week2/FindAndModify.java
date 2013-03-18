package com.tengen.week2;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class FindAndModify {
	public static void main(String[] args) throws UnknownHostException {
		DBCollection collection = createCollection();
		collection.drop();

		final String counterId = "abc";
		int first;
		int numNeeded;
		
		numNeeded = 2;
		first = getRange(counterId, numNeeded, collection);
		System.out.println("Range : " + first + "-" + (first + numNeeded - 1));

		numNeeded = 3;
		first = getRange(counterId, numNeeded, collection);
		System.out.println("Range : " + first + "-" + (first + numNeeded - 1));

		numNeeded = 10;
		first = getRange(counterId, numNeeded, collection);
		System.out.println("Range : " + first + "-" + (first + numNeeded - 1));

		
		printCollection(collection);
	}

	private static int getRange(String id, int range,
			DBCollection collection) {
		DBObject doc = collection.findAndModify(
				new BasicDBObject("_id", id), 
				null, 	// Fields filter
				null, 	// No order
				false, 	// Don't remove found documents
				new BasicDBObject("$inc", new BasicDBObject("counter", range)),	// The actual update
				true,	// Return the updated document (not the old)
				true	// Create the document if not exists
				);		
		return (Integer) doc.get("counter") - range + 1;
	}

	private static DBCollection createCollection() throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("course");
		DBCollection collection = db.getCollection("DotNotationTest");
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
