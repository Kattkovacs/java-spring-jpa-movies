package com.katt.javaspringjpamovies.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Series {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private Integer duration;

    private LocalDate releaseDate;

    @Transient
    private Long age;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Season season;

    public void calculateAge(){
        if(releaseDate != null){
            age = ChronoUnit.YEARS.between(releaseDate, LocalDate.now());
        }
    }

}
