package com.tengen.week3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFSTest {
	public static void main(String[] args) throws IOException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("gridfstest");
		FileInputStream fis = null;
		
		// Descriptor
		GridFS videos = new GridFS(db, "videos");
		
		try {
			fis = new FileInputStream("c:\\video.mp4");
		} catch (FileNotFoundException e) {
			System.out.println("Can't open file");
			System.exit(1);
		}
		
		GridFSInputFile video = videos.createFile(fis, "video.mp4");
		
		// Create some meta data for the object, will be stored 
		BasicDBObject meta = new BasicDBObject("description", "This is a video");
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Android");
		tags.add("mp4");
		meta.append("tags", tags);
		
		video.setMetaData(meta);
		video.save();
		
		System.out.println("Object ID in Files Collection : " + video.get("_id"));
		
		GridFSDBFile gridFile = videos.findOne(new BasicDBObject("filename", "video.mp4"));
		FileOutputStream outputStream = new FileOutputStream("c:\\video_copy.mp4");
		gridFile.writeTo(outputStream);
	}
}