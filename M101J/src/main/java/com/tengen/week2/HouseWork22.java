package com.tengen.week2;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class HouseWork22 {
	public static void main(String[] args) throws UnknownHostException {
		DBCollection collection = DBUtil.createCollection("students", "grades");
		
		DBObject criteria = QueryBuilder.start("type").is("homework").get();
		DBCursor cursor = collection.find(criteria).sort(new BasicDBObject("student_id", 1).append("score", -1));
		
		String previousStudentId = "";
		String nextStudentId = "";
		DBObject currentDocument;
		do {
			currentDocument = cursor.next();
			nextStudentId = currentDocument.get("student_id").toString();
			if (previousStudentId.equals(nextStudentId)) {
				collection.remove(new BasicDBObject("_id", currentDocument.get("_id")));
			}
			previousStudentId = nextStudentId;
		} while (cursor.hasNext());
	}
}
