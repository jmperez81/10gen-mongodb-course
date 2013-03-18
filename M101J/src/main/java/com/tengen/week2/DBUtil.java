package com.tengen.week2;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DBUtil {
	public static DBCollection createCollection(String dbName, String collectionName) throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB(dbName);
		DBCollection collection = db.getCollection(collectionName);
		return collection;
	}

	public static void printCollection(DBCollection collection) {
		DBCursor cursor = collection.find();
		try {
			while (cursor.hasNext()) {
				DBObject cur = cursor.next();
				System.out.println(cur);
			}
		} finally {
			cursor.close();
		}
	}
	
	public static void printCursor(DBCursor cursor) {
		try {
			while (cursor.hasNext()) {
				DBObject cur = cursor.next();
				System.out.println(cur);
			}
		} finally {
			cursor.close();
		}
	}
}
