package com.katt.javaspringjpamovies.repository;

import com.katt.javaspringjpamovies.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {

}
