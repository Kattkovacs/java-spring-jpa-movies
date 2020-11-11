package com.katt.javaspringjpamovies;

import com.katt.javaspringjpamovies.entity.Certificate;
import com.katt.javaspringjpamovies.entity.Episode;
import com.katt.javaspringjpamovies.entity.Season;
import com.katt.javaspringjpamovies.entity.Series;
import com.katt.javaspringjpamovies.repository.EpisodeRepository;
import com.katt.javaspringjpamovies.repository.SeasonRepository;
import com.katt.javaspringjpamovies.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@SpringBootApplication
public class JavaSpringJpaMoviesApplication {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    public static void main(String[] args) {
        SpringApplication.run(JavaSpringJpaMoviesApplication.class, args);
    }
    @Bean
    @Profile("production")
    public CommandLineRunner init(){
        return args -> {

            Episode episode2 = Episode.builder()
                    .title("The Phantom Sign Writer")
                    .no(2)
                    .releaseDate(LocalDate.of(1990, 1, 14))
                    .build();

            Season season1 = Season.builder()
                    .seasonTitle("1990 Season1")
                    .length(5)
                    .episode(episode2)
                    .build();

            Series mLordSeries = Series.builder()
                    .title("You Rang, M'Lord?")
                    .releaseDate(LocalDate.of(1988, 2, 3))
                    .duration(26)
                    .certificate(Certificate.FIFTEEN)
                    .season(season1)
                    .actor("Donald Hewlett")
                    .actor("Catherine Rabett")
                    .actor("Susie Brann")
                    .build();
            mLordSeries.calculateAge();

            episode2.setSeason(season1);
            season1.setSeries(mLordSeries);

//            episodeRepository.save(episode2);
//            seasonRepository.save(season1);
            seriesRepository.save(mLordSeries);
        };
    }
}
