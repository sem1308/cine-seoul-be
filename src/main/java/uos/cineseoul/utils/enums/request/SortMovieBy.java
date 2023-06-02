package uos.cineseoul.utils.enums.request;

public enum SortMovieBy {
    genre("movieGenreList.genre.name"),
    grade("grade"),
    ticketCount("ticketCount"),
    movieNum("movieNum"),
    releaseDate("releaseDate");
    String fieldName;

    SortMovieBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
