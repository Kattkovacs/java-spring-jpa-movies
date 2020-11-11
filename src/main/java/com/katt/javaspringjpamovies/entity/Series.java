package com.katt.javaspringjpamovies.entity;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    private Certificate certificate;

    @Transient
    private Long age;

    @ElementCollection
    @Singular
    private List<String> actors;

    @Singular
    @OneToMany(mappedBy = "series", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @EqualsAndHashCode.Exclude
    private Set<Season> seasons;

    public void calculateAge(){
        if(releaseDate != null){
            age = ChronoUnit.YEARS.between(releaseDate, LocalDate.now());
        }
    }

}
