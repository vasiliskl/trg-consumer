package com.trgint;

/**
 * @author Vasilis Kleanthous
 * This is an Exception thrown in case there is an error while the measurements are processed.
 *
 */
public class SensorMeasurementProcessingException extends Exception {

	private static final long serialVersionUID = -5887255651956798053L;

	@Override
	public String getMessage() {
		return "Error while processing sensor measurement";
	}

}
