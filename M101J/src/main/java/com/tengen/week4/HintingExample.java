package com.tengen.week4;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class HintingExample {
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("students");
		
		BasicDBObject query = new BasicDBObject("type", "exam");
		DBCollection c = db.getCollection("grades");
		
		// Way 1 : using index name
//		DBObject doc = c.find(query).hint("type_1").explain();
		
		// Way 2
		BasicDBObject myHint = new BasicDBObject("type", 1);
		DBObject doc = c.find(query).hint(myHint).explain();
		
		for (String string : doc.keySet()) {
			System.out.printf("%25s:%s\n", string, doc.get(string));
		}
	}
}
