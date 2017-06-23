package com.matthew.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Friendship.
 */
@Entity
@Table(name = "friendship")
public class Friendship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user1;

    @OneToOne
    @JoinColumn(unique = true)
    private User user2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public Friendship user1(User user) {
        this.user1 = user;
        return this;
    }

    public void setUser1(User user) {
        this.user1 = user;
    }

    public User getUser2() {
        return user2;
    }

    public Friendship user2(User user) {
        this.user2 = user;
        return this;
    }

    public void setUser2(User user) {
        this.user2 = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friendship friendship = (Friendship) o;
        if (friendship.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), friendship.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Friendship{" +
            "id=" + getId() +
            "}";
    }
}
