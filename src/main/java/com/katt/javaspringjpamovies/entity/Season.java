package com.katt.javaspringjpamovies.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Season {

    @Id
    @GeneratedValue
    private Long id;

    private String seasonTitle;

    private Integer number;

    private Integer length;

    @OneToOne(mappedBy = "season")
    @EqualsAndHashCode.Exclude
    private Series series;
}
