package com.mkyong.rest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

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

import net.spy.memcached.MemcachedClient;

@Path("/campaign")
public class LinkRedirectService {
	@Autowired
	private MemcachedClient memcachedClient;

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String shortUrl, @Context HttpHeaders headers) throws IOException {
		String userAgent = headers.getRequestHeader("user-agent").get(0);

		String clientIp = null;
		if (headers.getRequestHeader("x-forwarded-for") != null) {
			clientIp = headers.getRequestHeader("x-forwarded-for").get(0);
		}
		String clientIpParsed = ParseString.ip(clientIp);

		String output = "Jersey say : " + shortUrl;
		StringBuffer sb = new StringBuffer();
		sb.append(output);
		sb.append("clientIp==" + clientIp + "<--");
		sb.append("clientIpParsed==" + clientIpParsed + "<--");
		String countryFromGeo = IpToGeo.getTrendsInJSON(clientIpParsed);
		sb.append("countryFromGeo==" + countryFromGeo + "<--");

		sb.append("\n" + "==>headers-->");
		for (String header : headers.getRequestHeaders().keySet()) {
			System.out.println(header);
			sb.append("\n" + header + " : " + headers.getRequestHeaders().get(header) + "\n");
		}

		// memcache
		// ******************************************************************************************
		String ip = "cmpe281-team1-lr.amb3uo.cfg.use1.cache.amazonaws.com";// "127.0.0.1";
		// "ws.amb3uo.cfg.use1.cache.amazonaws.com";//
		// "lr-memcached-cluster.gxiwqx.cfg.usw2.cache.amazonaws.com";
		MemcachedClient c = new MemcachedClient(new InetSocketAddress(ip, 11211));
		c.add("b", 0, shortUrl);
		sb.append("value for key b= " + c.get("b"));
		System.out.println("URL RESPONSE:" + DatabaseConnection.selectData("MTAwMDAx"));
		sb.append("URL RESPONSE:" + DatabaseConnection.selectData("MTAwMDAx"));

		// SQS
		// **************************************************************************************************
		String accessKey = "AKIAJFPZRP2DN5CDSCQQ";
		String secretKey = "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";
		String queueUrl = "https://sqs.us-east-1.amazonaws.com/060340690398/CMPE281-Team1-links-mapping-queue";
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		String simpleQueue = "CMPE281-Team1-links-mapping-queue";
		AmazonSQS sqs = new AmazonSQSClient(credentials);
		sqs.setEndpoint("sqs.us-east-1.amazonaws.com");
		SendMessageResult messageResult = sqs.sendMessage(new SendMessageRequest(queueUrl, "msg2"));
		sb.append(" --msg sent-- " + messageResult.toString());

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		sb.append("messages size==" + messages.size() + " 1st msg" + messages.get(0));
		// **************************************************************************************************

		return Response.status(200).entity(sb.toString()).build();

		// return
		// Response.status(Status.TEMPORARY_REDIRECT).location(URI.create("http://www.google.com")).build();

	}
}