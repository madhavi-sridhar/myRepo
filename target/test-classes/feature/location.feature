Feature: Location of city bikes around the world

@In-Progress
Scenario: Verification of the cityBike Networks 
	When   I call the citybike end point
	Then   I get the response status code as 200 
	
@In-Progress
Scenario: Verification of the cityBike company stations 
	Given  The citybike networks
	When   I verify the company href
	Then   I receive the company stations
	And    I get the response status code as 200	
	 
@In-Progress
Scenario Outline: Verification of the city location
	Given  The citybike locations
	When   I check the location of the city "<city>"
	Then   Verify the given city is in the country "<country>" 
	And    Verify latitude and longitude
	 
Examples:
      |city      |country    |
      |Frankfurt |Germany    |
      |Stavanger |Norway     |
      |Galway    |Ireland    |
      	
   	
	
	
	     





