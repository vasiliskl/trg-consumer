package com.trgint;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.quarkus.runtime.util.ExceptionUtil;

/**
 * @author Vasilis Kleanthous
 * This is the consumer class used to consume the measurements from the queue.
 * It is a reactive implementation so any processing needs to be async.
 */
@ApplicationScoped
public class SensorMeasurementConsumer {

    private static final Logger logger = Logger.getLogger(SensorMeasurementConsumer.class.getName());
    @Inject ThreadContext threadContext;
    @Inject SensorService sensorService;
    
	/**
	 * This method consumes the incoming measurements.
	 * @param input - the measurement JSON
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Incoming("measurement-in")
	public void consumeMeasurement(String input) throws JsonParseException, JsonMappingException, IOException {

		logger.info("Consuming Sensor Measurement: "+input);
		
		try
		{
			JsonReader jsonReader = Json.createReader(new StringReader(input));
		    JsonObject measurement = jsonReader.readObject();
			
		    //Async processing of the measurement.
			threadContext.withContextCapture(CompletableFuture.runAsync(() ->
			{
				try
				{
					sensorService.processSensorMeasurement(measurement);
					
					logger.info("Succesfuly Consumed Sensor Measurement:"+input);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.error("Error Consuming Sensor Measurement. Error message:"+ ExceptionUtil.generateStackTrace(e));
				}
			}));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Error Incoming topic: anonymize-contact with error message:"+ ExceptionUtil.generateStackTrace(e));
		}
	}
}
