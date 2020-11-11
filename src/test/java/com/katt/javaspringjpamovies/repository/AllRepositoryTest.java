package com.katt.javaspringjpamovies.repository;

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



}