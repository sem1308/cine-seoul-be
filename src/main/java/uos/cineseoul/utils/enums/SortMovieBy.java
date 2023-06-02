package uos.cineseoul.utils.enums;

public enum SortMovieBy {
    GENRE("movieGenreList.genre.name"),
    GRADE("grade"),
    TICKETCOUNT("ticketCount"),
    MOVIENUM("movieNum"),
    RELEASEDATE("releaseDate");
    String fieldName;

    SortMovieBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
