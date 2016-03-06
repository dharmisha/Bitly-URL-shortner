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
import javax.ws.rs.core.Response.Status;

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

@Path("/v1")
public class LinkRedirectService1 {

	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String shortUrlSuffix, @Context HttpHeaders headers) throws IOException {
		String userAgent = headers.getRequestHeader("user-agent").get(0);
		System.out.println("userAgent...." + userAgent);
		String platform = ParseString.machinetype(userAgent);
		System.out.println("machineType*****" + platform);

		String clientIp = "130.65.251.138";
		if (headers.getRequestHeader("x-forwarded-for") != null) {
			clientIp = headers.getRequestHeader("x-forwarded-for").get(0);
		}
		String clientIpParsed = ParseString.ip(clientIp);
		System.out.println("Conolidated Ip to geo.." + clientIpParsed);
		String countryFromGeo = "";

		if (clientIpParsed == null) {
			clientIpParsed = clientIp;
		}

		countryFromGeo = IpToGeo.getTrendsInJSON(clientIpParsed);
		System.out.println("countryFromGeo..." + countryFromGeo);

		String output = "";// "Jersey say : " + shortUrl;
		StringBuffer sb = new StringBuffer();
		sb.append(output);

		String shortUrl = "http://CMPE281-team1-lr-2142923369.us-east-1.elb.amazonaws.com/lr/v1/" + shortUrlSuffix;// "http://CMPE281-Team1-CP-845690823.us-east-1.elb.amazonaws.com/rest/lr/MTAwMDA1";

		String hashcode = ParseString.shorturl(shortUrl);
		String longUrl = "";
		int x = 1;
		if ((x == 2)) {
			// longUrl=c.get("http://tesy.com/MTAwMDAx")
		} else {
			longUrl = DatabaseConnection.selectData(hashcode);
			System.out.println("URL RESPONSE:" + longUrl);
		}

		MongoConn.trendInserUpdate(shortUrl, platform, countryFromGeo);

		return Response.status(Status.TEMPORARY_REDIRECT).location(URI.create(longUrl)).build();

	}
}