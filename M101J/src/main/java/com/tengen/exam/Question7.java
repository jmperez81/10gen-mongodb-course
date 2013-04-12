package com.tengen.exam;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Question7 {
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("finalq7");
		DBCollection albumsCollection = db.getCollection("albums");
		DBCollection imagesCollection = db.getCollection("images");
		
		// Obtain a cursor for images
		DBCursor cursorImages = imagesCollection.find();
		DBCursor cursorAux;
		DBObject currentImage;
		
		while (cursorImages.hasNext()) {
			currentImage = cursorImages.next();
			cursorAux = albumsCollection.find(new BasicDBObject("images", new BasicDBObject("$all", Arrays.asList(currentImage.get("_id")))));
			if (cursorAux.size() == 0) {
				System.out.println("Removing orphan image found with id : " + currentImage.get("_id"));
				imagesCollection.remove(new BasicDBObject("_id", currentImage.get("_id")));
			} else {
				System.out.println("Image found in some album [id, number of albums]: " + currentImage.get("_id") + "," + cursorAux.size());
			}
		}
	}
}
