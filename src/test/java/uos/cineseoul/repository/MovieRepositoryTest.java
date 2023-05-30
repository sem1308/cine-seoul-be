package uos.cineseoul.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.entity.movie.Movie;

import java.util.List;


@SpringBootTest
@Slf4j
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    @Transactional
    public void repoTest() {
        Movie movie1 = Movie
                .builder()
                .title("영화1")
                .releaseDate("20110101")
                .isShowing('F')
                .info("")
                .runningTime(1)
                .build();
        Movie movie2 = Movie
                .builder()
                .title("영화2")
                .releaseDate("20100101")
                .isShowing('T')
                .info("")
                .runningTime(1)
                .build();

        movieRepository.save(movie1);
        movieRepository.save(movie2);

        List<Movie> all = movieRepository.findAll();
        for (Movie m : all) {
            System.out.println(m.getTitle());
        }

        List<Movie> showing = movieRepository.findAllByIsShowing('T');
        showing.forEach(m -> System.out.println(m.getTitle()));

        List<Movie> allByReleaseDateAfter = movieRepository.findAllByReleaseDateAfter("20101203");
        allByReleaseDateAfter.forEach(m -> System.out.println(m.getTitle()));
    }
}