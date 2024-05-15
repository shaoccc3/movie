package com.ispan.theater.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.logging.Level;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "auditorium")
public class Auditorium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditorium_id", nullable = false)
    private Integer id;

    @Column(name = "auditorium_number", nullable = false)
    private Integer auditoriumNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    @JsonIgnore
    private Cinema cinema;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "level_id", nullable = false)
    @JsonIgnore
    private AuditoriumLevel levelId;

    @Column(name = "layout_id", nullable = false)
    private Integer layoutId;

    @Override
    public String toString() {
        return "Auditorium{" +
                "cinema=" + cinema.getId() +
                ", levelId=" + levelId +
                ", layoutId=" + layoutId +
                '}';
    }
}