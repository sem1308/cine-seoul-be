package uos.cineseoul.utils.enums.request;

public enum SortMovieBy {
    genre("movieGenreList.genre.name"),
    grade("grade"),
    ticket_count("reservationCount"),
    movie_num("movieNum"),
    release_date("releaseDate");
    String fieldName;

    SortMovieBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
