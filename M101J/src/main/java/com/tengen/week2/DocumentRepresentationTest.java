package com.tengen.week2;

import java.util.Arrays;
import java.util.Date;

import com.mongodb.BasicDBObject;

public class DocumentRepresentationTest {
	public static void main(String[] args) {
		BasicDBObject doc = new BasicDBObject();
		doc.put("username", "jperez");
		doc.put("birthDate", new Date());
		doc.put("programmer", true);
		doc.put("age", 31);
		doc.put("languages", Arrays.asList("Java", "C++"));
		doc.put("address", new BasicDBObject("street", "20 Main").
				append("town", "Madrid").
				append("number", 16));
	}
}
