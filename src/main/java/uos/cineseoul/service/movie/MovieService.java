package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertMovieDTO;
import uos.cineseoul.dto.update.UpdateMovieDTO;
import uos.cineseoul.entity.Country;
import uos.cineseoul.entity.movie.*;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.ActorAndRole;
import uos.cineseoul.utils.enums.ActorRole;
import uos.cineseoul.utils.enums.Is;

import java.time.LocalDateTime;
import java.util.*;

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

    private final CountryRepository countryRepository;

    public List<Movie> findAllMovie() {
        List<Movie> movieList = movieRepository.findAll();
        if (movieList.isEmpty()) {
            throw new ResourceNotFoundException("영화 목록이 없습니다.");
        }
        return movieList;
    }

    public Page<Movie> findAllMovie(Pageable pageable, String genre) {
        Page<Movie> movieList;
        if (genre != null) {
            Genre g = genreRepository.findById(genre).orElseThrow(() -> new ResourceNotFoundException("해당 코드의 장르가 없습니다."));
            movieList = movieRepository.findAllByMovieGenreList_Genre(g, pageable);
        } else {
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

    public List<Movie> findAllMovieLike(String title) {
        List<Movie> movieList = movieRepository.findAllByTitleContains(title);
        movieList.sort(Comparator.comparingInt(movie -> Math.abs(movie.getTitle().length() - title.length())));
        return movieList;
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

    public Page<Movie> findAllCountryMovie(Pageable pageable, String country) {
        Page<Movie> movieList;
        movieList = movieRepository.findAllByMovieCountryList_Country(
                countryRepository
                        .findByCountryCode(country)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("해당 코드의 국가가 없습니다.")
                        ), pageable
        );
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("해당 국가의 영화가 없습니다.");
        return movieList;
    }


    public Page<Movie> findAllShowingMovie(Pageable pageable, String genre) {
        Page<Movie> movieList;
        if (genre != null) {
            Genre g = genreRepository.findById(genre).orElseThrow(() -> new ResourceNotFoundException("해당 코드의 장르가 없습니다."));
            movieList = movieRepository.findAllByIsShowingAndMovieGenreList_Genre(Is.Y, g, pageable);
        } else {
            movieList = movieRepository.findAllByIsShowing(Is.Y, pageable);
        }
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("상영중인 영화가 없습니다.");
        return movieList;
    }

    public Page<Movie> findAllWillReleaseMovie(Pageable pageable, String genre) {
        LocalDateTime now = LocalDateTime.now();
        String nowTypeString = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        Page<Movie> movieList;
        if (genre != null) {
            Genre g = genreRepository.findById(genre).orElseThrow(() -> new ResourceNotFoundException("해당 코드의 장르가 없습니다."));
            movieList = movieRepository.findAllByReleaseDateAfterAndMovieGenreList_Genre(nowTypeString, g, pageable);
        } else {
            movieList = movieRepository.findAllByReleaseDateAfter(nowTypeString, pageable);
        }
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("상영예정인 영화가 없습니다.");
        return movieList;
    }

    public Movie update(UpdateMovieDTO updateMovieDTO) {

        List<Genre> genreList = null;
        List<Director> directorList = null;
        List<Country> countryList = null;
        Map<Actor, ActorRole> actorActorRoleMap = null;

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

        if (updateMovieDTO.getActorList() != null) {
            actorActorRoleMap = new HashMap<>();
            for (ActorAndRole actorAndRole : updateMovieDTO.getActorList()) {
                actorActorRoleMap.put(actorRepository.findByActNum(actorAndRole.getActor()).orElseThrow(
                        () -> new ResourceNotFoundException("해당 번호의 배우가 없습니다.")), actorAndRole.getRole()
                );
            }
        }

        if (updateMovieDTO.getDirectorList() != null) {
            directorList = new ArrayList<>();
            for (Long dirNum : updateMovieDTO.getDirectorList()) {
                directorList.add(
                        directorRepository.findByDirNum(dirNum).orElseThrow(
                                () -> new ResourceNotFoundException("해당 번호의 감독이 없습니다.")
                        )
                );
            }
        }

        if (updateMovieDTO.getGenreList() != null) {
            genreList = new ArrayList<>();
            for (String genreCode : updateMovieDTO.getGenreList()) {
                genreList.add(
                        genreRepository.findByGenreCode(genreCode).orElseThrow(
                                () -> new ResourceNotFoundException("해당 번호의 장르가 없습니다.")
                        )
                );
            }
        }

        if (updateMovieDTO.getCountryList() != null) {
            countryList = new ArrayList<>();
            for (String countryCode : updateMovieDTO.getCountryList()) {
                countryList.add(
                        countryRepository.findByCountryCode(countryCode).orElseThrow(
                                () -> new ResourceNotFoundException("해당 번호의 국가가 없습니다.")
                        )
                );
            }
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
        if (updateMovieDTO.getPoster() != null)
            movie.setPoster(updateMovieDTO.getPoster());

        if (genreList != null) {
            movie.getMovieGenreList().clear();
            for (Genre genre : genreList) {
                MovieGenre movieGenre = MovieGenre
                        .builder()
                        .genre(genre)
                        .movie(movie)
                        .build();
                movie.getMovieGenreList().add(movieGenre);
            }
        }

        if (actorActorRoleMap != null) {
            movie.getMovieActorList().clear();
            for (Map.Entry<Actor, ActorRole> entry : actorActorRoleMap.entrySet()) {
                MovieActor movieActor = MovieActor
                        .builder()
                        .actor(entry.getKey())
                        .movie(movie)
                        .creditRole(entry.getValue())
                        .build();
                movie.getMovieActorList().add(movieActor);
            }
        }

        if (countryList != null) {
            movie.getMovieCountryList().clear();
            for (Country country : countryList) {
                MovieCountry movieCountry = MovieCountry
                        .builder()
                        .movie(movie)
                        .country(country)
                        .build();
                movie.getMovieCountryList().add(movieCountry);
            }
        }

        if (directorList != null) {
            movie.getMovieDirectorList().clear();
            for (Director director : directorList) {
                MovieDirector movieDirector = MovieDirector
                        .builder()
                        .movie(movie)
                        .director(director)
                        .build();
                movie.getMovieDirectorList().add(movieDirector);
            }
        }

        return movieRepository.save(movie);
    }

    public Movie insert(InsertMovieDTO insertMovieDTO) {

        Map<Actor, ActorRole> actorActorRoleMap = new HashMap<>();
        List<Genre> genreList = new ArrayList<>();
        List<Director> directorList = new ArrayList<>();
        List<Country> countryList = new ArrayList<>();
        Grade grade = null;

        Distributor distributor = distributorRepository.findByDistNum(insertMovieDTO.getDistNum()).orElseThrow(
                () -> new ResourceNotFoundException("해당 배급사가 없습니다.")
        );

        if (insertMovieDTO.getGradeCode() != null)
            grade = gradeRepository.findByGradeCode(insertMovieDTO.getGradeCode()).orElseThrow(
                    () -> new ResourceNotFoundException("해당 등급이 없습니다.")
            );

        insertMovieDTO.getActorList().forEach(
                actorAndRole ->
                        actorActorRoleMap.put(actorRepository.findByActNum(actorAndRole.getActor()).orElseThrow(
                                () -> new ResourceNotFoundException("해당 번호의 배우가 없습니다.")
                        ), actorAndRole.getRole())
        );

        insertMovieDTO.getDirectorList().forEach(
                dirNum -> directorList.add(directorRepository.findByDirNum(dirNum).orElseThrow(
                        () -> new ResourceNotFoundException("해당 번호의 감독이 없습니다.")
                ))
        );

        insertMovieDTO.getGenreList().forEach(
                genreCode -> genreList.add(genreRepository.findByGenreCode(genreCode).orElseThrow(
                        () -> new ResourceNotFoundException("해당 코드의 장르가 없습니다.")
                ))
        );

        insertMovieDTO.getCountryList().forEach(
                countryCode -> countryList.add(countryRepository.findByCountryCode(countryCode).orElseThrow(
                        () -> new ResourceNotFoundException("해당 코드의 국가가 없습니다.")
                ))
        );


        Movie movie = Movie
                .builder()
                .title(insertMovieDTO.getTitle())
                .poster(insertMovieDTO.getPoster())
                .isShowing(insertMovieDTO.getIsShowing())
                .releaseDate(insertMovieDTO.getReleaseDate())
                .distributor(distributor)
                .grade(grade)
                .runningTime(insertMovieDTO.getRunningTime())
                .info(insertMovieDTO.getInfo())
                .build();

        for (Map.Entry<Actor, ActorRole> entry : actorActorRoleMap.entrySet()) {
            MovieActor movieActor = MovieActor.builder()
                    .movie(movie)
                    .actor(entry.getKey())
                    .creditRole(entry.getValue())
                    .build();
            movie.getMovieActorList().add(movieActor);
        }

        for (Genre genre : genreList) {
            MovieGenre movieGenre = MovieGenre
                    .builder()
                    .movie(movie)
                    .genre(genre)
                    .build();
            movie.getMovieGenreList().add(movieGenre);
        }

        for (Director director : directorList) {
            MovieDirector movieDirector = MovieDirector
                    .builder()
                    .movie(movie)
                    .director(director)
                    .build();
            movie.getMovieDirectorList().add(movieDirector);
        }

        for (Country country : countryList) {
            MovieCountry movieCountry = MovieCountry
                    .builder()
                    .movie(movie)
                    .country(country)
                    .build();
            movie.getMovieCountryList().add(movieCountry);
        }
        return movieRepository.save(movie);
    }
}
