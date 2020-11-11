package com.katt.javaspringjpamovies.entity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Details {

    @Id
    @GeneratedValue
    private Long id;

    private String directedBy;

    private String WrittenBy;

    @OneToOne(mappedBy = "details")
    private Episode episode;
}
