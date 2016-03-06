package com.mkyong.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

@Path("/geo")
public class IpToGeo {

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	// public static List<CountryMap> getTrendsInJSON
	public static String getTrendsInJSON(@QueryParam("ip") String ip) {

		LookupService cl = null;
		try {
			cl = // new LookupService("C:/281-workspace/GeoLiteCity.dat",
					// LookupService.GEOIP_MEMORY_CACHE |
					// LookupService.GEOIP_CHECK_CACHE);

			new LookupService("/home/ubuntu/GeoLiteCity.dat",
					LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
		} catch (IOException e) {
		}

		CountryMap country = new CountryMap();

		if (cl != null) {
			Location location = cl.getLocation(ip);
			country.setCountry((String) location.countryName);
			return (String) location.countryName;
		}

		country.setCountry("United States");
		country.setUrl(ip);

		return country.getCountry();

	}
}