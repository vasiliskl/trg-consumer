package com.trgint;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.validation.Validator;

@ApplicationScoped
public class SensorService {

	@Inject
    private Validator validator;
	
    @Inject
    private EntityManager entityManager;
    
	public void processSensorMeasurement(JsonObject measurement)throws Exception
	{
		Long sensorId=new Long(measurement.getInt("sensorId"));
		
		if(sensorId!=null)
		{
			Sensor sensor=entityManager.find(Sensor.class, sensorId);
			
			if(sensor!=null)
			{
				SensorData data=new SensorData();
				
				data.setSensor(sensor);
				data.setLatitude(new Double(measurement.getString("latitude")));
				data.setLongitude(new Double(measurement.getString("longitude")));
				data.setHumidity(new Double(measurement.getString("humidity")));
				data.setPressure(new Double(measurement.getString("pressure")));
				data.setTemperature(new Double(measurement.getString("temperature")));
				
				entityManager.persist(data);
			}
		}
	}
}
