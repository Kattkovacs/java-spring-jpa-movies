package com.katt.javaspringjpamovies.repository;

import com.katt.javaspringjpamovies.entity.Details;
import com.katt.javaspringjpamovies.entity.Episode;
import com.katt.javaspringjpamovies.entity.Season;
import com.katt.javaspringjpamovies.entity.Series;
import org.assertj.core.util.Lists;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private EpisodeRepository episodeRepository;

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

    @Test
    public void episodesArePermistedAndDeletedWithNewSeries(){
        Set<Episode> episode2 = IntStream.range(1, 10)
                .boxed()
                .map(integer -> Episode.builder()
                        .title("The Phantom Sign Writer")
                        .no(2)
                        .releaseDate(LocalDate.of(1990, 1, 14))
                        .build())
                .collect(Collectors.toSet());
        Season season1 = Season.builder()
                .episodes(episode2)
                .length(5)
                .seasonTitle("1990")
                .build();
        seasonRepository.save(season1);

        assertThat(episodeRepository.findAll())
                .hasSize(1)
                .anyMatch(episode -> episode.getTitle().equals("The Phantom Sign Writer"));

        seasonRepository.deleteAll();

        assertThat(episodeRepository.findAll())
                .hasSize(0);

    }

    @Test
    public void findByTitleStartingWithOrReleaseDateBetween(){
        Episode episode3 = Episode.builder()
                .title("A Deed of Gift")
                .build();

        Episode episode4 = Episode.builder()
                .title("Love and Money")
                .build();

        Episode episode5 = Episode.builder()
                .title("Fair Shares")
                .build();

        Episode episode6 = Episode.builder()
                .title("Beg, Borrow or Steal")
                .releaseDate(LocalDate.of(1990,2,3))
                .build();

        Episode episode1 = Episode.builder()
                .title("Pilot")
                .releaseDate(LocalDate.of(1990,2,15))
                .build();

        episodeRepository.saveAll(Lists.newArrayList(episode1, episode5, episode3, episode4, episode6));

        List<Episode> filteredEpisodes = episodeRepository.findByTitleStartingWithOrReleaseDateBetween("A",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(1990, 3, 1));

        assertThat(filteredEpisodes)
                .containsExactlyInAnyOrder(episode6, episode1, episode3);
    }

    @Test
    public void findAllDirectedBy(){
        Episode episode7 = Episode.builder()
                .title("Beg, Borrow or Steal")
                .releaseDate(LocalDate.of(1990,2,3))
                .details(Details.builder().directedBy("David Croft").build())
                .build();
        Episode episode8 = Episode.builder()
                .title("Beg, Borrow or Steal")
                .releaseDate(LocalDate.of(1990,2,3))
                .details(Details.builder().directedBy("Roy Gould").build())
                .build();
        Episode episode9 = Episode.builder()
                .title("Beg, Borrow or Steal")
                .releaseDate(LocalDate.of(1990,2,3))
                .details(Details.builder().directedBy("David Croft").build())
                .build();

        episodeRepository.saveAll(Lists.newArrayList(episode7, episode8, episode9));
        List<String> allDirectedBy = episodeRepository.findAllDirectedBy();

        assertThat(allDirectedBy)
                .hasSize(2)
                .containsOnlyOnce("David Croft");
    }


}