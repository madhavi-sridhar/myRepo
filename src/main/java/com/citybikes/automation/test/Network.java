package com.citybikes.automation.test;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Network {
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) 
	private List <String> company;
	 private String href;
	 private String id;
	 private Location location;
	 private String name;
	 @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) 
	 private List <String> stations;


	 // Getter Methods 

	 public String getHref() {
	  return href;
	 }

	 public String getId() {
	  return id;
	 }

	 public List<String> getStations() {
		return stations;
	}

	public void setStations(List<String> stations) {
		this.stations = stations;
	}

	public Location getLocation() {
	  return location;
	 }

	 public String getName() {
	  return name;
	 }

	 // Setter Methods 

	 public void setHref(String href) {
	  this.href = href;
	 }

	 public void setId(String id) {
	  this.id = id;
	 }

	 public void setLocation(Location location) {
	  this.location = location;
	 }

	 public void setName(String name) {
	  this.name = name;
	 }
	 
	 public List<String> getCompany() {
		return company;
	 }

	public void setCompany(List<String> company) {
		this.company = company;
	}
}
