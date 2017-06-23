package com.matthew.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Statistics.
 */
@Entity
@Table(name = "statistics")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "initial_date")
    private LocalDate initialDate;

    @Column(name = "final_date")
    private LocalDate finalDate;

    @Column(name = "distance")
    private Float distance;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "steps")
    private Integer steps;

    @Column(name = "calories_burnt")
    private Float caloriesBurnt;

    @Column(name = "average_speed")
    private Float averageSpeed;

    @Column(name = "max_speed")
    private Float maxSpeed;

    @Column(name = "number_of_activities")
    private Integer numberOfActivities;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public Statistics initialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
        return this;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public Statistics finalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
        return this;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public Float getDistance() {
        return distance;
    }

    public Statistics distance(Float distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public Statistics duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSteps() {
        return steps;
    }

    public Statistics steps(Integer steps) {
        this.steps = steps;
        return this;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Float getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public Statistics caloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
        return this;
    }

    public void setCaloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public Float getAverageSpeed() {
        return averageSpeed;
    }

    public Statistics averageSpeed(Float averageSpeed) {
        this.averageSpeed = averageSpeed;
        return this;
    }

    public void setAverageSpeed(Float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public Statistics maxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getNumberOfActivities() {
        return numberOfActivities;
    }

    public Statistics numberOfActivities(Integer numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
        return this;
    }

    public void setNumberOfActivities(Integer numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }

    public User getUser() {
        return user;
    }

    public Statistics user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Statistics statistics = (Statistics) o;
        if (statistics.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statistics.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Statistics{" +
            "id=" + getId() +
            ", initialDate='" + getInitialDate() + "'" +
            ", finalDate='" + getFinalDate() + "'" +
            ", distance='" + getDistance() + "'" +
            ", duration='" + getDuration() + "'" +
            ", steps='" + getSteps() + "'" +
            ", caloriesBurnt='" + getCaloriesBurnt() + "'" +
            ", averageSpeed='" + getAverageSpeed() + "'" +
            ", maxSpeed='" + getMaxSpeed() + "'" +
            ", numberOfActivities='" + getNumberOfActivities() + "'" +
            "}";
    }
}
