package com.tengen.week2;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class InsertTest {
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB courseDB = client.getDB("course");
		DBCollection collection = courseDB.getCollection("insertTest");
		
		collection.drop();
		
		DBObject doc = new BasicDBObject("x", 1).append("_id", new ObjectId());
		DBObject doc2 = new BasicDBObject().append("x", 2);
				
		// Insertion of multiple docs
		collection.insert(Arrays.asList(doc, doc2));
		
		// Exception thrown if duplicated document inserted
		collection.insert(doc);
		
		System.out.println(doc.toString() + "\n" + doc2);		
	}
}
