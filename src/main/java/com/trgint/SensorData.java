package com.trgint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Vasilis Kleanthous
 * This is the Sensor Data domain object mapped to a database table used Hibernate ORM.
 *
 */
@Entity
@Table(name = "sensordata")
public class SensorData {

    @Id
    @SequenceGenerator(name = "sensorDataSequence", sequenceName = "sensor_data_id_seq",  allocationSize = 1,  initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sensorDataSequence")
    @NotNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "sensorid", nullable = false)
    @NotNull
    private Sensor sensor;
    
    @Column(precision=10, scale=2)
	private Double latitude;
    
    @Column(precision=10, scale=2)
	private Double longitude;
    
    @Column(precision=10, scale=2)
	private Double humidity;
    
    @Column(precision=10, scale=2)
	private Double temperature;
    
    @Column(precision=10, scale=2)
	private Double pressure;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getPressure() {
		return pressure;
	}

	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
}
