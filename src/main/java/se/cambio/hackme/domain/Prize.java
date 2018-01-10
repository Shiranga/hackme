package se.cambio.hackme.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Prize.
 */
@Entity
@Table(name = "prize")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Prize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "place")
    private String place;

    @Column(name = "prize")
    private String prize;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    private Event event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public Prize place(String place) {
        this.place = place;
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrize() {
        return prize;
    }

    public Prize prize(String prize) {
        this.prize = prize;
        return this;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Prize isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Event getEvent() {
        return event;
    }

    public Prize event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prize prize = (Prize) o;
        if (prize.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, prize.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prize{" +
            "id=" + id +
            ", place='" + place + "'" +
            ", prize='" + prize + "'" +
            ", isActive='" + isActive + "'" +
            '}';
    }
}
