package com.citibike.automation.bdd.stepDef;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.citybikes.automation.test.CitybikeNetwork;
import com.citybikes.automation.test.CitybikeNetworks;
import com.citybikes.automation.test.Location;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.LatLng;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class LocationStep {
	String endpoint = "http://api.citybik.es/v2/networks";
	Response response;
	CitybikeNetworks citybikeNetworks;
	List<Location> cityLocations;
		
	public Response doGetRequest(String endpoint) {
        RestAssured.defaultParser = Parser.JSON;
        return  RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                        when().get(endpoint).
                        then().contentType(ContentType.JSON).extract().response();
    }

	@When("^I call the citybike end point$")
	public void i_call_the_citybike_end_point() throws Exception {
		response = 	doGetRequest(endpoint);		
	}

	@Then("^I get the response status code as (\\d+)$")
	public void i_get_the_response_status_code_as(int arg1) throws Exception {
		assertEquals(200,response.getStatusCode());
	}	
	
	@Given("^The citybike networks$")
	public void the_citybike_networks() throws Exception {
		response = 	doGetRequest(endpoint);
	}
	
	@When("^I verify the company href$")
	public void i_verify_the_company_stations() throws Exception {
	    String domain = "http://api.citybik.es";
	    request_the_citybike_locations();
	    String href = citybikeNetworks.getNetworks().get(0).getHref();
	    response = doGetRequest(domain+href);	    
	}

	@Then("^I receive the company stations$")
	public void i_receive_the_company_stations() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		CitybikeNetwork network = mapper.readValue(response.getBody().asString(),CitybikeNetwork.class);
		List<String> stations = network.getNetwork().getStations();
		assertNotNull(stations);
	}	
	
	@Given("^The citybike locations$")
	public void request_the_citybike_locations() throws JsonParseException, JsonMappingException, IOException {
		response = doGetRequest(endpoint);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		citybikeNetworks = mapper.readValue(response.getBody().asString(),CitybikeNetworks.class);		
		assertEquals(200,response.getStatusCode());

	}

	@When("^I check the location of the city \"([^\"]*)\"$")
	public void i_check_the_location_of_the_city(String city) throws Exception{
		cityLocations = new ArrayList<>();
		citybikeNetworks.getNetworks().forEach (network -> {
			if(network.getLocation().getCity().equalsIgnoreCase(city)){
				cityLocations.add(network.getLocation());
			}
		});			
	}
		
	@Then("^Verify the given city is in the country \"([^\"]*)\"$")
	public void verify_city_country(String country) {
		cityLocations.forEach(location -> {
			assertEquals(country, new Locale("", location.getCountry()).getDisplayCountry());
		});	
		
	}
	
	@And("^Verify latitude and longitude$")
	public void verify_latitude_and_longitude() {
		cityLocations.forEach(location -> {
			//Doesn't work without API key to verify city latitude and longitude
			/*String countryName = RestAssured.get("http://maps.googleapis.com/maps/api/geocode/json?latlng={lat},{long}&sensor=false", String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())).
                    jsonPath().getString("results.address_components.flatten().find { it.types.flatten().contains('country') }?.long_name");
			
			*/
			
			Geocoder geocoder = new Geocoder() ;
			LatLng _location = new LatLng(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(_location).setLanguage("en").getGeocoderRequest();
			GeocodeResponse geocoderResponse = null;
			try {
				geocoderResponse = geocoder.geocode(geocoderRequest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Status : " + geocoderResponse.getStatus().value());

		});	
	}
	
	    
	


}
