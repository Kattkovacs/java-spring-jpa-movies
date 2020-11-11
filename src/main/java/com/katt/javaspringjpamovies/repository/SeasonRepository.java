package com.katt.javaspringjpamovies.repository;

import com.katt.javaspringjpamovies.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SeasonRepository extends JpaRepository<Season, Long> {

}
