package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.InsertMovieDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;

    private final DistributorRepository distributorRepository;
    private final GradeRepository gradeRepository;

    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    private final DirectorRepository directorRepository;

    private final MovieActorRepository movieActorRepository;

    private final MovieGenreRepository movieGenreRepository;

    private final MovieDirectorRepository movieDirectorRepository;

    public List<Movie> findAllMovie() {
        List<Movie> movieList = movieRepository.findAll();
        if (movieList.isEmpty())
        {
            throw new ResourceNotFoundException("영화 목록이 없습니다.");
        }
        return movieList;
    }

    public List<Movie> findAllShowingMovie() {
        List<Movie> movieList = movieRepository.findAllByIsShowing('T');
        if(movieList.isEmpty())
            throw new ResourceNotFoundException("상영중인 영화가 없습니다.");
        return movieList;
    }

    public List<Movie> findAllWillReleaseMovie() {
        LocalDateTime now = LocalDateTime.now();
        String nowTypeString = String.format("%d%02d%02d",now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        List<Movie> movieList = movieRepository.findAllByReleaseDateAfter(nowTypeString);
        if(movieList.isEmpty())
            throw new ResourceNotFoundException("상영예정인 영화가 없습니다.");
        return movieList;
    }

    public Movie insert(InsertMovieDTO insertMovieDTO) {
        Distributor distributor = distributorRepository.findByDistNum(insertMovieDTO.getDistNum()).orElseThrow(
                () -> new ResourceNotFoundException("해당 배급사가 없습니다.")
        );

        Grade grade = gradeRepository.findByGradeCode(insertMovieDTO.getGradeCode()).orElseThrow(
                () -> new ResourceNotFoundException("해당 등급이 없습니다.")
        );

        Movie movie = Movie
                .builder()
                .title(insertMovieDTO.getTitle())
                .isShowing(insertMovieDTO.getIsShowing())
                .releaseDate(insertMovieDTO.getReleaseDate())
                .distributor(distributor)
                .grade(grade)
                .runningTime(insertMovieDTO.getRunningTime())
                .info(insertMovieDTO.getInfo())
                .build();

        insertMovieDTO.getActorList().forEach(
                actorNum -> {
                    Actor actor = actorRepository.findByActNum(actorNum).orElseThrow(
                            () -> new ResourceNotFoundException("해당 번호의 배우가 없습니다.")
                    );
                    MovieActor movieActor = MovieActor
                            .builder()
                            .movie(movie)
                            .actor(actor)
                            .build();
                    movieActorRepository.save(movieActor);
                }
        );

        insertMovieDTO.getDirectorList().forEach(
                dirNum -> {
                    Director director = directorRepository.findByDirNum(dirNum).orElseThrow(
                            () -> new ResourceNotFoundException("해당 번호의 감독이 없습니다.")
                    );
                    MovieDirector movieDirector = MovieDirector
                            .builder()
                            .movie(movie)
                            .director(director)
                            .build();
                    movieDirectorRepository.save(movieDirector);
                }
        );

        insertMovieDTO.getGenreList().forEach(
                 genreCode -> {
                     Genre genre = genreRepository.findByGenreCode(genreCode).orElseThrow(
                            () -> new ResourceNotFoundException("해당 코드의 장르가 없습니다.")
                    );
                    MovieGenre movieGenre = MovieGenre
                            .builder()
                                    .movie(movie)
                                            .genre(genre)
                                                    .build();
                   movieGenreRepository.save(movieGenre);
                }
        );

        return movieRepository.save(movie);
    }

}
