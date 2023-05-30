package uos.cineseoul.dto;

import lombok.Data;
import uos.cineseoul.entity.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreateMovieDTO {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 4000)
    private String info;

    @Size(min = 8, max = 8)
    private String releaseDate;

    private int runningTime;

    @NotNull
    private char isShowing;

    @NotNull
    private Long distNum;

    @NotNull
    @Size(min = 2, max = 2)
    private String gradeCode;

    @NotNull
    private List<String> genreCodeList;

    @NotNull
    private List<Long> directorNumList;

    @NotNull
    private List<Long> actorNumList;
}
