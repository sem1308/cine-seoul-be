package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertGenreDTO;
import uos.cineseoul.entity.movie.Genre;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.GenreRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre findGenre(String genreCode) {
        Genre genre = genreRepository.findByGenreCode(genreCode).orElseThrow(
                () -> new ResourceNotFoundException("해당 장르코드의 장르가 없습니다.")
        );
        return genre;
    }

    public Genre insert(InsertGenreDTO insertGenreDTO) {
        Genre genre = Genre
                .builder()
                .name(insertGenreDTO.getName())
                .genreCode(insertGenreDTO.getGenreCode())
                .build();
        return genreRepository.save(genre);
    }

}
