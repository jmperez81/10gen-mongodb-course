package com.tengen.week6;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class WriteConcernTest {
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		MongoClient client = new MongoClient(Arrays.asList(new ServerAddress("localhost", 20017), new ServerAddress("localhost", 20018), new ServerAddress("localhost", 20019)));
		
		// Acknowledge level can be set globally through client
		client.setWriteConcern(WriteConcern.JOURNALED);
		
		DB db = client.getDB("course");
		// Acknowledge level can be set at db
		db.setWriteConcern(WriteConcern.JOURNALED);
		DBCollection coll = db.getCollection("write.test");
		// Acknowledge level can be set at collection
		coll.setWriteConcern(WriteConcern.JOURNALED);
		
		coll.drop();
		
		DBObject doc = new BasicDBObject("_id", 1);
		
		// Acknowledge level can be set per insert operation
		coll.insert(doc, WriteConcern.JOURNALED);
		
		// The getLastError is not used so the failed insertion does not
		// raise any exception
		try {
			coll.insert(doc, WriteConcern.UNACKNOWLEDGED);
		} catch (MongoException.DuplicateKey e) {
			System.out.println(e.getMessage());
		}
	}
}
