package com.tengen.week2;

import java.net.UnknownHostException;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

public class FieldSelectionTest {
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("course");
		DBCollection collection = db.getCollection("fieldSelectionTest");
		collection.drop();
		Random rand = new Random();
		
		// insert 10 documents with two random integers
		for (int i=0; i<10; i++) {
			collection.insert(new BasicDBObject("x", rand.nextInt(2))
			.append("y", rand.nextInt(100)).append("z", 1000));
		}

		
		// Helper method for create the criteria by example
		QueryBuilder builder = QueryBuilder.start("x").is(0).and("y").greaterThan(10).lessThan(70);
		
		// You can build the query directly as well
//		DBObject query = new BasicDBObject("x", 0)
//			.append("y", new BasicDBObject("$gt", 10).append("$lt", 90));
				
		// Filter the x value, as we already know is 0
		DBCursor cursor = collection.find(builder.get(), 
				new BasicDBObject("x", false).append("_id", false));
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
