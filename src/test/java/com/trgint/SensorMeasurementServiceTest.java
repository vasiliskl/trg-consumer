package com.trgint;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

/**
 * @author Vasilis Kleanthous
 * This class implements some basic tests for the service class.
 * It uses the H2 database to persist data.
 *
 */
@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class SensorMeasurementServiceTest {

	@Inject SensorService sensorService;
	
    @Test
    public void testProcessValidMeasurement() throws Exception {
    	JsonObject measurement=Json.createObjectBuilder()
    	        .add("sensorId", 1)
    	        .add("latitude", 37.075204)
    	        .add("longitude", 35.389475)
    	        .add("pressure", 5.1)
    	        .add("humidity", 55.67)
    	        .add("temperature", 60.5)
    	        .build();
    	
    	sensorService.processSensorMeasurement(measurement);
    }
    
    @Test
    public void testProcessInvalidMeasurement() throws Exception {
    	JsonObject measurement=Json.createObjectBuilder()
    	        .add("sensorId", "abc")
    	        .add("latitude", "def")
    	        .add("longitude", "ghi")
    	        .add("pressure", 5.1)
    	        .add("humidity", 55.67)
    	        .add("temperature", 60.5)
    	        .build();
    	
    	Assertions.assertThrows(SensorMeasurementProcessingException.class, () -> {
    		sensorService.processSensorMeasurement(measurement);
    	  });
    }
}
