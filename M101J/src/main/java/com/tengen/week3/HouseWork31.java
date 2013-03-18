package com.tengen.week3;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tengen.week2.DBUtil;

public class HouseWork31 {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws UnknownHostException {
		DBCollection collection = DBUtil.createCollection("school", "students");
		
		DBCursor cursor = collection.find();
//		DBUtil.printCollection(collection);
		
		DBObject currentDocument;
		ArrayList<BasicDBObject> currentScores;
		double lowestScoreValue;
		BasicDBObject lowestScoreObject = null;
		while(cursor.hasNext()) {
			currentDocument = cursor.next();
			lowestScoreValue = Integer.MAX_VALUE;
			
			// Retrieves scores documents for this student
			currentScores = (ArrayList<BasicDBObject>) currentDocument.get("scores");		
			
			// Loops over the scores and stores the lowest Score of type "homework"
			for (BasicDBObject score : currentScores) {
				if ("homework".equals(score.getString("type")) && 
						score.getDouble("score") < lowestScoreValue) {
					lowestScoreValue = (Double)score.get("score");
					lowestScoreObject = score;
				}
			}
			
			// Removes the lowest score from list
			currentScores.remove(lowestScoreObject);
			
			// Updates document in collection
			collection.update(new BasicDBObject("_id", currentDocument.get("_id")), currentDocument);
			
			// Prints out the removed document
			System.out.println("Removed document " + lowestScoreObject + " from " + currentDocument.get("_id") + " - " + currentDocument.get("name"));						
		};		
	}
}
