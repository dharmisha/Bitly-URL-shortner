package com.mkyong.rest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

@Path("/ingest")
public class MemcacheIngester {
	@GET
	// @Path("/{param}")
	public void populateMemCacheFromSqs() throws IOException, InterruptedException {
		StringBuffer sb = new StringBuffer();

		String accessKey = "AKIAJFPZRP2DN5CDSCQQ";
		String secretKey = "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";
		String queueUrl = "https://sqs.us-east-1.amazonaws.com/060340690398/CMPE281-Team1-links-mapping-queue";
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		String simpleQueue = "CMPE281-Team1-links-mapping-queue";
		AmazonSQS sqs = new AmazonSQSClient(credentials);
		sqs.setEndpoint("sqs.us-east-1.amazonaws.com");

		String memcacheConfigurationPoint = "cmpe281-team1-lr.amb3uo.cfg.use1.cache.amazonaws.com";// "127.0.0.1";
		MemcachedClient c = new MemcachedClient(new InetSocketAddress(memcacheConfigurationPoint, 11211));

		try {
			while (true) {
				// Retrieve from SQS
				// **********************************************
				ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
				List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
				for (Message m : messages) {
					String msg = m.getBody();
					String[] msgParts = msg.split(",");
					if (msgParts.length == 2) {
						String shortUrl = msgParts[0];
						String longUrl = msgParts[1];

						// Add to memcache
						// **********************************************
						c.add(shortUrl, 0, longUrl);
					}
				}

				Thread.sleep(3000);
			}
		} catch (Exception e) {

		}
		// return Response.status(200).entity(sb.toString()).build();

	}
}