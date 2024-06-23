package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.movie.Distributor;
import uos.cineseoul.entity.movie.Grade;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintTicketDTO {
    private Long ticketNum;

    private Integer stdPrice;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState;

    private LocalDateTime createdAt;

    private PrintUserDTO user;

    private PrintScheduleNotSchedSeatDTO schedule;

    @Builder.Default
    private List<PrintTicketSeatDTO> ticketSeats = new ArrayList<>();

    @Builder.Default
    private List<PrintTicketAudienceDTO> audienceTypes = new ArrayList<>();

    public void setByTicket(Ticket ticket) {
        if(ticket.getTicketSeats()==null || ticket.getTicketSeats().size()==0) return;
        List<PrintGenreDTO> genreList = new ArrayList<>();
        Movie movie = ticket.getSchedule().getMovie();
        movie.getMovieGenreList().forEach(movieGenre ->
                genreList.add(new PrintGenreDTO(movieGenre.getGenre()))
        );
        this.schedule.getMovie().setGenreList(genreList);
        Grade grade = movie.getGrade();
        if(grade!=null)
            this.schedule.getMovie().setGradeName(grade.getName());
        Distributor distributor = ticket.getSchedule().getMovie().getDistributor();
        if(distributor!=null)
            this.schedule.getMovie().setDistName(distributor.getName());
        this.ticketSeats.forEach(ticketSeat -> {
            ticketSeat.getSeat().setSeatPrice(ticketSeat.getSeat().getSeatGrade().getPrice());
        });
        this.audienceTypes.forEach(audienceDTO -> {
            audienceDTO.setDisplayName(audienceDTO.getAudienceType().getDisplayName());
        });
    }
}
