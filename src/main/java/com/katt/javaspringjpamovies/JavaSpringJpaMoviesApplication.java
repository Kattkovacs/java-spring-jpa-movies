package com.katt.javaspringjpamovies;

import com.katt.javaspringjpamovies.entity.Series;
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

    public static void main(String[] args) {
        SpringApplication.run(JavaSpringJpaMoviesApplication.class, args);
    }
    @Bean
    @Profile("production")
    public CommandLineRunner init(){
        return args -> {
            Series oneSerie = Series.builder()
                    .title("One")
                    .releaseDate(LocalDate.of(1950, 2, 3))
                    .duration(120)
                    .build();
            seriesRepository.save(oneSerie);
        };
    }
}
