# Build and run

## Requirements
* [Java 18](https://jdk.java.net/18/)
* [Docker](https://www.docker.com/get-started/) for running integration tests with real MySQL

## Build
```
./gradlew claen build
```
## Run locally
works on 5000 port by default
```
./gradlew bootRun
```

# Usage
Swagger http://localhost:5000/swagger-ui/

Get filters that later can be used in restaurant request 
```
curl -X GET "http://localhost:5000/api/restaurants/filters" -H "accept: */*"
```
Get restaurant filtered by:
* query - name(dba) or cuisine description. List of cuisines can be taken from previous request
* grade - can be taken from previous request. Accepts multiple values
```
curl -X GET "http://localhost:5000/api/restaurants?grade=A&grade=B&page=1&q=thai&size=20" -H "accept: */*"
```

# Suggestions

## Database
* Rename fields to make their meaning more clear: dba, boro, community_board, census_tract, critical_flag, score,  bin, bbl, nta,
* Separate to multiple table (normalize). It makes the database easier to understand. Reduce duplication if some data appears multiple times for one restaurant, e.g.:
  * address (boro, building, street, zipcode, phone, latitude, longitude). It may be useful ta have soft delete for address to see history of addresses in case it changed.
  * inspections (inspection_date, action, violation_code, violation_description, inspection_type). Theoretically we can have multiple inspections for one restaurant
* Use more specific types
  * Use enums for critical_flag, grades, inspection_type
  * Use numeric with custom precision for latitude and longitude
* Consider using timestamp with time zone for dates if it's planned to work in multiple countries/time zones.
* Consider using indexes for columns used in filtering/sorting to speed up queries

## Solution
* Remove DB credentials from git (in application.yml)
* Use separate DTO for getting restaurants with the fields needed only
* Add other filters for getting restaurants, e.g. closest restaurants to current position by longitude and latitude
* Add sorting by some fields
* Add docker to wrap the app with it
* Add more strict rules for building app:
  * Checkstyle, Findbugs, PMD
  * Test coverage validation so app won't build until specific percentage