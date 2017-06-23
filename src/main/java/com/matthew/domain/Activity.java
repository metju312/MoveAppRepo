package com.matthew.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1)
    @Column(name = "activity_type", length = 1)
    private String activityType;

    @Column(name = "jhi_date")
    private Instant date;

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

    @Column(name = "jhi_shared")
    private Integer shared;

    @OneToMany(mappedBy = "activity")
    @JsonIgnore
    private Set<Node> nodes = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public Activity activityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Instant getDate() {
        return date;
    }

    public Activity date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Float getDistance() {
        return distance;
    }

    public Activity distance(Float distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public Activity duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSteps() {
        return steps;
    }

    public Activity steps(Integer steps) {
        this.steps = steps;
        return this;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Float getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public Activity caloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
        return this;
    }

    public void setCaloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public Float getAverageSpeed() {
        return averageSpeed;
    }

    public Activity averageSpeed(Float averageSpeed) {
        this.averageSpeed = averageSpeed;
        return this;
    }

    public void setAverageSpeed(Float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public Activity maxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getShared() {
        return shared;
    }

    public Activity shared(Integer shared) {
        this.shared = shared;
        return this;
    }

    public void setShared(Integer shared) {
        this.shared = shared;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Activity nodes(Set<Node> nodes) {
        this.nodes = nodes;
        return this;
    }

    public Activity addNodes(Node node) {
        this.nodes.add(node);
        node.setActivity(this);
        return this;
    }

    public Activity removeNodes(Node node) {
        this.nodes.remove(node);
        node.setActivity(null);
        return this;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public User getUser() {
        return user;
    }

    public Activity user(User user) {
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
        Activity activity = (Activity) o;
        if (activity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", activityType='" + getActivityType() + "'" +
            ", date='" + getDate() + "'" +
            ", distance='" + getDistance() + "'" +
            ", duration='" + getDuration() + "'" +
            ", steps='" + getSteps() + "'" +
            ", caloriesBurnt='" + getCaloriesBurnt() + "'" +
            ", averageSpeed='" + getAverageSpeed() + "'" +
            ", maxSpeed='" + getMaxSpeed() + "'" +
            ", shared='" + getShared() + "'" +
            "}";
    }
}
