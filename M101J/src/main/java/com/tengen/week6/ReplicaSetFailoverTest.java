package com.tengen.week6;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class ReplicaSetFailoverTest {
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		// Instead of connecting to a single address of the cluster, an array of addresses is passed.
		// The mongodb client just need a valid node to discover the rest of the nodes in the replica set
		MongoClient client = new MongoClient(Arrays.asList(new ServerAddress("localhost", 20017), new ServerAddress("localhost", 20018), new ServerAddress("localhost", 20019)));
		
		DBCollection test = client.getDB("course").getCollection("replica.test");
		test.drop();
		
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			// Try to insert 3 times if insertion fails, waiting 5 seconds between tries
			for (int retries = 0; retries < 2; retries++) {
				// If the primary node falls, an exception is raised everytime we insert a document until the election of the new primary node
				try {
					test.insert(new BasicDBObject("_id", i));
					System.out.println("Inserted document: " + i);
					break;
				} catch (MongoException.DuplicateKey e) {
					// If the document was inserted before the node fell, we don't need to
					// insert it again
					System.out.println("Document already inserted : " + i);
				} catch (MongoException e) {
					System.out.println(e.getMessage());
					System.out.println("Retrying");
					Thread.sleep(5000);
				}
			}
			Thread.sleep(500);
		}
	}
}
