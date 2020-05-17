package com.trgint;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;
/**
 * @author Vasilis Kleanthous
 * This service class implements the method used to process the data received from queue.
 * It does some basic validations and business logic and persists the data to the database using Hibernate ORM.
 *
 */
@ActivateRequestContext
@ApplicationScoped
public class SensorService {
	
    @Inject
    private EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(SensorService.class.getName());
    
    /**
     * This method implements the business logic of processing the measurements consumed from the queue.
     * It validates and persists the data received and does also some checks on the measurements and prints the corresponding warnings in the logs.
     * @param measurement - The measurement JSON
     * @throws SensorMeasurementProcessingException
     */
    @Transactional
    @Counted(name = "measurementsProcessed", description = "How many measurements have been processed.")
    @Timed(name = "measurementProcessedTimer", description = "A measure of how long it takes to process a measurement.", unit = MetricUnits.MILLISECONDS)
	public void processSensorMeasurement(JsonObject measurement)throws SensorMeasurementProcessingException
	{
    	try 
    	{
			if(measurement.get("sensorId")!=null)
			{
				Long sensorId=new Long(measurement.getInt("sensorId"));
				logger.info("Processing measurement from sensor "+sensorId);
				
				Sensor sensor=entityManager.find(Sensor.class, sensorId);
				
				if(sensor!=null)
				{
					SensorData data=new SensorData();
					
					data.setSensor(sensor);
					data.setLatitude(new Double(measurement.getJsonNumber("latitude").doubleValue()));
					data.setLongitude(new Double(measurement.getJsonNumber("longitude").doubleValue()));
					data.setHumidity(new Double(measurement.getJsonNumber("humidity").doubleValue()));
					data.setPressure(new Double(measurement.getJsonNumber("pressure").doubleValue()));
					data.setTemperature(new Double(measurement.getJsonNumber("temperature").doubleValue()));
					
					entityManager.persist(data);
					
					logger.info("Measurement from sensor "+sensorId+" saved.");
					
					if(data.getHumidity()>50)
					{
						logger.warn("Humidity from sensor "+sensorId+" is too high!!!");
					}
					else
					{
						logger.info("Humidity from sensor "+sensorId+" looks ok.");
					}
					
					if(data.getPressure()<2)
					{
						logger.warn("Pressure from sensor "+sensorId+" is too low!!!");
					}
					else
					{
						logger.info("Pressure from sensor "+sensorId+" looks ok.");
					}
					
					if(data.getTemperature()<0)
					{
						logger.warn("Temperature from sensor "+sensorId+" is too low!!!");
					}
					else if(data.getTemperature()>40)
					{
						logger.warn("Temperature from sensor "+sensorId+" is too high!!!");
					}
					else
					{
						logger.info("Temperature from sensor "+sensorId+" looks ok.");
					}
					
				}
				else
				{
					logger.error("Invalid sensor.");
					throw new SensorMeasurementProcessingException();
				}
			}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		throw new SensorMeasurementProcessingException();
    	}
	}
}
