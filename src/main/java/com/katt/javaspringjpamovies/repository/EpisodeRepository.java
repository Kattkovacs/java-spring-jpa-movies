package com.katt.javaspringjpamovies.repository;

import com.katt.javaspringjpamovies.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
}
