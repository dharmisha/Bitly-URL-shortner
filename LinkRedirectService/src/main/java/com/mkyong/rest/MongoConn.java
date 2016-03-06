package com.mkyong.rest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

public class MongoConn {
	public static void trendInserUpdate(String shorturl, String platform,
			String countryFromIp) throws IOException {
		MongoClient mongoClient = null;
		try {
			// Mongo mongo = new
			// Mongo("mongodb://admin:password@ds059644.mongolab.com:59644/cmpe281-team1",
			// 27017);
			MongoCredential credential = MongoCredential.createCredential(
					"admin", "cmpe281-team1", "password".toCharArray());
			mongoClient = new MongoClient(new ServerAddress(
					"ds059644.mongolab.com", 59644), Arrays.asList(credential));
			@SuppressWarnings("deprecation")
			DB db = mongoClient.getDB("cmpe281-team1");
			// Trend Update
			DBCollection collection = db.getCollection("trends");
			DBCursor query = collection
					.find(new BasicDBObject("url", shorturl));
			if (query.length() == 0) {
				DBObject urlcount = new BasicDBObject().append("url", shorturl)
						.append("count", 1);
				collection.insert(urlcount);
			} else {
				BasicDBObject newDocument = new BasicDBObject().append("$inc",
						new BasicDBObject().append("count", 1));
				collection.update(new BasicDBObject().append("url", shorturl),
						newDocument);
			}
			
			collection = db.getCollection("platforms");
			DBCursor queryFindPlatform = collection
					.find(new BasicDBObject("platform", platform));
			if (queryFindPlatform.length() == 0) {
				System.out.println("DIDNT get in platform....");
				DBObject urlcount = new BasicDBObject()
						//.append("url", shorturl)
						.append("platform", platform).append("count", 1);
				collection.insert(urlcount);
			} else {
				System.out.println("GOT IT in platform get...");
				BasicDBObject newDocument = new BasicDBObject().append("$inc",
						new BasicDBObject().append("count", 1));

				collection.update(new BasicDBObject().append("platform", platform),
						newDocument);
			}

			// country insert Update
			System.out.println("country.." + countryFromIp);
			collection = db.getCollection("countries");
			DBCursor queryFindCountry = collection
					.find(new BasicDBObject("country", countryFromIp));
			if (queryFindCountry.length() == 0) {
				System.out.println("DIDNT get in country*****");
				DBObject urlcount = new BasicDBObject()
						//.append("url", shorturl)
						.append("country", countryFromIp).append("count", 1);
				collection.insert(urlcount);
			} else {
				System.out.println("GOT IT in contrt****");
				BasicDBObject newDocument = new BasicDBObject().append("$inc",
						new BasicDBObject().append("count", 1));
				collection.update(new BasicDBObject().append("country", countryFromIp),
						newDocument);
			}

			
			//Date insert Update
			DateFormat timeStampvalue = new SimpleDateFormat("yyyy-MM-dd");
	     	Calendar calobj = Calendar.getInstance();
	     	String dateValue=timeStampvalue.format(calobj.getTime());
			System.out.println("Date.." + dateValue);
			collection = db.getCollection("dates");
			DBCursor queryFindDate = collection
					.find(new BasicDBObject("date", dateValue));
			if (queryFindDate.length() == 0) {
				System.out.println("DIDNT get in date*****");
				DBObject urlcount = new BasicDBObject()
						.append("date", dateValue)
						//.append("country", countryFromIp)
						.append("count", 1);
				collection.insert(urlcount);
			} else {
				System.out.println("GOT IT in date****");
				BasicDBObject newDocument = new BasicDBObject().append("$inc",
						new BasicDBObject().append("count", 1));
				collection.update(new BasicDBObject().append("date", dateValue),
						newDocument);
			}

			// To print data from mongo collection
			/*
			 * cursor = collection.find(); while (cursor.hasNext()) {
			 * System.out.println(cursor.next()); }
			 */

			mongoClient.close();

		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
}
