package com.katt.javaspringjpamovies.repository;

import com.katt.javaspringjpamovies.entity.Season;
import com.katt.javaspringjpamovies.entity.Series;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AllRepositoryTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TestEntityManager entityManager;

    //save entity
    @Test
    public void saveOneSimple(){
        Series oneSerie = Series.builder()
                .title("One")
                .releaseDate(LocalDate.of(1950,2,3))
                .duration(120)
                .build();
        seriesRepository.save(oneSerie);

        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(1);

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveUniqueFieldTwice(){
        Series seriesTwo = Series.builder()
                .title("Two")
                .releaseDate(LocalDate.of(1950,2,3))
                .duration(120)
                .build();
        seriesRepository.save(seriesTwo);

        Series seriesThree = Series.builder()
                .title("Two")
                .releaseDate(LocalDate.of(1950,2,3))
                .duration(125)
                .build();
        seriesRepository.saveAndFlush(seriesThree);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void titleShouldBeNotNull(){
        Series serie = Series.builder()
                .releaseDate(LocalDate.of(1950,2,3))
                .duration(120)
                .build();

        seriesRepository.save(serie);
    }

    @Test
    public void transientIsNotSaved(){
        Series serie = Series.builder()
                .title("Two")
                .releaseDate(LocalDate.of(1950,2,3))
                .duration(120)
                .build();

        serie.calculateAge();
        assertThat(serie.getAge()).isGreaterThanOrEqualTo(70);

        seriesRepository.save(serie);
        entityManager.clear();

        List<Series> series = seriesRepository.findAll();
        assertThat(series).allMatch(serie1 -> serie1.getAge() == 0L);
    }

    @Test
    public void seasonIsPersistedWithSeries(){
        Season go = Season.builder()
                .seasonTitle("Go")
                .number(1)
                .length(8)
                .build();

        Series series = Series.builder()
                .title("Two")
                .releaseDate(LocalDate.of(1950,2,3))
                .season(go)
                .duration(120)
                .build();

        seriesRepository.save(series);

        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons)
                .hasSize(1)
                .allMatch(season1 -> season1.getId() > 0L);

    }


}