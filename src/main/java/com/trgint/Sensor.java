package com.trgint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Vasilis Kleanthous
 * This is the Sensor domain object mapped to a database table used Hibernate ORM.
 *
 */
@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @SequenceGenerator(name = "sensorSequence", sequenceName = "sensor_id_seq",  allocationSize = 1,  initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sensorSequence")
    private Long id;

    @Column(length = 40, unique = true)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
