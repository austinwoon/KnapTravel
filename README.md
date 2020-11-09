# README #

This README documents how to set up our travel recommender project.

## What is this repository for? ##

* Quick summary

The travel recommender application allows a user to auto-generate a suitable set of tourist itineraries for a multi-day intra-city trip, depending on the user's preferences.

## How do I get set up? ##

### Standalone Algorithm Setup ###

Run the Main.java file in the `main` package. To change the country, please modify the country name in the string parameter passed to ScoringCalculator.

**Prerequisites for full-stack: Yarn and a working JDK (version 11+). A Mapbox API key will have to be provided via Yarn. See Configuration for more details.**

### Setup for full stack application: ###

1) For the Spring Boot backend:

Change to the `/backend` folder and run the following commands. The backend will be deployed on port 8080 by default.

1. `./mvnw install` or `.\mvnw install` on Windows.

2. `./mvnw spring-boot:run` or `.\mvnw spring-boot:run` on Windows

2) For the Nuxt.js frontend:

Change to the `/frontend` folder and run the following commands. The frontend will be deployed on port 3000 by default.

1. `yarn install`

2. `yarn dev`

### Docker deployment instructions (Requires docker and docker-compose to be installed) ###

1. Change to the `/backend` folder and run the following command:   
`./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=cs201-backend-docker:latest`   
or `.\mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=cs201-backend-docker:latest` if on Windows.

2. Return to project directory root and run `docker-compose up -d`.

3. Run `docker build` if changes are made to the frontend, or redo Step 1 if changes are made to the backend.

### Configuration ###

The Nuxt.js frontend uses a .env file in order to set up its Mapbox API key and backend URL. The Mapbox API Key is mandatory and can be retrieved through https://docs.mapbox.com/help/how-mapbox-works/access-tokens/.
Create an .env file with the key MAPBOX_API_KEY in order to use the frontend. The .env file also accepts a BACKEND_URL key which can be used to denote a custom backend URL if needed.

The backend does not require any configuration.