package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uos.cineseoul.entity.Movie;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.MovieRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

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

}
