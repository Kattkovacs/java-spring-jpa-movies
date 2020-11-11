package com.katt.javaspringjpamovies.repository;

import com.katt.javaspringjpamovies.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findByTitleStartingWithOrReleaseDateBetween(String title, LocalDate from, LocalDate to);

    @Query("SELECT distinct e.details.directedBy from Episode e")
    List<String> findAllDirectedBy();

}
