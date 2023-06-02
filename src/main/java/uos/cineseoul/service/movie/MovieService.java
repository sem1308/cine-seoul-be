package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertMovieDTO;
import uos.cineseoul.dto.update.UpdateMovieDTO;
import uos.cineseoul.entity.movie.*;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.Is;

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
        if (movieList.isEmpty()) {
            throw new ResourceNotFoundException("영화 목록이 없습니다.");
        }
        return movieList;
    }

    public Page<Movie> findAllMovie(Pageable pageable, String genre) {
        Page<Movie> movieList;
        if(genre!=null){
            Genre g = genreRepository.findById(genre).orElseThrow(()->new ResourceNotFoundException("해당 코드의 장르가 없습니다."));
            movieList = movieRepository.findAllByMovieGenreList_Genre(g,pageable);
        }else {
            movieList = movieRepository.findAll(pageable);
        }
        if (movieList.isEmpty()) {
            throw new ResourceNotFoundException("영화 목록이 없습니다.");
        }
        return movieList;
    }

    public Movie findMovie(Long movieNum) {
        Movie movie = movieRepository.findByMovieNum(movieNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 번호의 영화가 없습니다.")
        );
        return movie;
    }

    public Movie findMovieByTitle(String title) {
        Movie movie = movieRepository.findByTitle(title).orElseThrow(
                () -> new ResourceNotFoundException("해당 제목의 영화가 없습니다.")
        );
        return movie;
    }

    public List<Movie> findAllShowingMovie() {
        List<Movie> movieList = movieRepository.findAllByIsShowing(Is.Y);
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("상영중인 영화가 없습니다.");
        return movieList;
    }

    public List<Movie> findAllWillReleaseMovie() {
        LocalDateTime now = LocalDateTime.now();
        String nowTypeString = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        List<Movie> movieList = movieRepository.findAllByReleaseDateAfter(nowTypeString);
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("상영예정인 영화가 없습니다.");
        return movieList;
    }

    public Page<Movie> findAllShowingMovie(Pageable pageable) {
        Page<Movie> movieList = movieRepository.findAllByIsShowing(Is.Y,pageable);
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("상영중인 영화가 없습니다.");
        return movieList;
    }

    public Page<Movie> findAllWillReleaseMovie(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        String nowTypeString = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        Page<Movie> movieList = movieRepository.findAllByReleaseDateAfter(nowTypeString,pageable);
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("상영예정인 영화가 없습니다.");
        return movieList;
    }

    public Movie update(UpdateMovieDTO updateMovieDTO) {

        Movie movie = movieRepository.findByMovieNum(updateMovieDTO.getMovie_num()).orElseThrow(
                () -> new ResourceNotFoundException("해당 영화가 없습니다.")
        );

        if (updateMovieDTO.getDistNum() != null) {
            Distributor distributor = distributorRepository.findByDistNum(updateMovieDTO.getDistNum()).orElseThrow(
                    () -> new ResourceNotFoundException("해당 배급사가 없습니다.")
            );
            movie.setDistributor(distributor);
        }

        if (updateMovieDTO.getGradeCode() != null) {
            Grade grade = gradeRepository.findByGradeCode(updateMovieDTO.getGradeCode()).orElseThrow(
                    () -> new ResourceNotFoundException("해당 등급이 없습니다.")
            );
            movie.setGrade(grade);
        }

        if (updateMovieDTO.getTitle() != null)
            movie.setTitle(updateMovieDTO.getTitle());
        if (updateMovieDTO.getInfo() != null)
            movie.setInfo(updateMovieDTO.getInfo());
        if (updateMovieDTO.getIsShowing() != null)
            movie.setIsShowing(updateMovieDTO.getIsShowing());
        if (updateMovieDTO.getReleaseDate() != null)
            movie.setReleaseDate(updateMovieDTO.getReleaseDate());
        if (updateMovieDTO.getRunningTime() != null)
            movie.setRunningTime(updateMovieDTO.getRunningTime());
        if (movie.getMovieActorList() != null &&
                updateMovieDTO.getActorList() != null &&
                !movie.getMovieActorList().equals(updateMovieDTO.getActorList())) {
            for (MovieActor m : movieActorRepository.findAllByMovie(movie))
                movieActorRepository.delete(m);
            updateMovieDTO.getActorList().forEach(
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
        }
        if (movie.getMovieDirectorList() != null &&
                updateMovieDTO.getDirectorList() != null &&
                !movie.getMovieDirectorList().equals(updateMovieDTO.getDirectorList())) {
            for (MovieDirector m : movieDirectorRepository.findAllByMovie(movie))
                movieDirectorRepository.delete(m);
            updateMovieDTO.getDirectorList().forEach(
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

        }
        if (updateMovieDTO.getGenreList() != null &&
                updateMovieDTO.getGenreList() != null &&
                !movie.getMovieGenreList().equals(updateMovieDTO.getGenreList())){
            for (MovieGenre m : movieGenreRepository.findAllByMovie(movie))
                movieGenreRepository.delete(m);
            updateMovieDTO.getGenreList().forEach(
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
        }
        return movieRepository.save(movie);
    }

    public Movie insert(InsertMovieDTO insertMovieDTO) {

        Movie result;

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

        result = movieRepository.save(movie);

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

        return result;
    }
}
