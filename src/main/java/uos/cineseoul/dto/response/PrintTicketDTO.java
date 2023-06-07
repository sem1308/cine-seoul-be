package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.entity.Ticket;
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

    private List<PrintReservationSeatDTO> reservationSeats = new ArrayList<>();

    private List<PrintTicketAudienceDTO> audienceTypes = new ArrayList<>();

    public void setByTicket(Ticket ticket) {
        if(ticket.getReservationSeats()==null || ticket.getReservationSeats().size()==0) return;
        List<PrintGenreDTO> genreList = new ArrayList<>();
        Movie movie = ticket.getSchedule().getMovie();
        movie.getMovieGenreList().forEach(movieGenre ->
                genreList.add(new PrintGenreDTO(movieGenre.getGenre()))
        );
        this.schedule.getMovie().setGenreList(genreList);
        this.schedule.getMovie().setGradeName(movie.getGrade().getName());
        this.schedule.getMovie().setDistName(ticket.getSchedule().getMovie().getDistributor().getName());
        this.reservationSeats.forEach(reservation -> {
            reservation.getSeat().setSeatPrice(reservation.getSeat().getSeatGrade().getPrice());
        });
        this.audienceTypes.forEach(audienceDTO -> {
            audienceDTO.setDisplayName(audienceDTO.getAudienceType().getDisplayName());
        });
    }
}
