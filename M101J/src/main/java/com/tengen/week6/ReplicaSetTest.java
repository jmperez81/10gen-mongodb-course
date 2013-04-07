package com.tengen.week6;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class ReplicaSetTest {
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		// Instead of connecting to a single address of the cluster, an array of addresses is passed.
		// The mongodb client just need a valid node to discover the rest of the nodes in the replica set
		MongoClient client = new MongoClient(Arrays.asList(new ServerAddress("localhost", 20017), new ServerAddress("localhost", 20018), new ServerAddress("localhost", 20019)));
		
		DBCollection test = client.getDB("course").getCollection("replica.test");
		test.drop();
		
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			test.insert(new BasicDBObject("_id", i));
			System.out.println("Inserted document: " + i);
			Thread.sleep(500);
		}
	}
}
