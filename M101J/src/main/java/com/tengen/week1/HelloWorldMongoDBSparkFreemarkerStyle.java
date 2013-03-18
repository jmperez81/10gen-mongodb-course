package com.tengen.week1;

import java.io.StringWriter;
import java.net.UnknownHostException;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HelloWorldMongoDBSparkFreemarkerStyle {
	public static void main(String[] args) throws UnknownHostException {
		final Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(
				HelloWorldFreemarkerStyle.class, "/");

		MongoClient client = new MongoClient("localhost", 27017);
		DB database = client.getDB("course");
		final DBCollection collection = database.getCollection("hello");
		
		Spark.get(new Route("/") {

			@Override
			public Object handle(Request arg0, Response arg1) {
				StringWriter writer = new StringWriter();
				try {
					Template helloTemplate = configuration
							.getTemplate("hello.ftl");					

					DBObject document = collection.findOne(); 
					
					helloTemplate.process(document, writer);					
				} catch (Exception e) {
					halt(500);
					e.printStackTrace();
				}
				return writer;
			}

		});
	}
}
