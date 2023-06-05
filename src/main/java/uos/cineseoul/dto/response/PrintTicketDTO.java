package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.entity.Schedule;
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

    private List<PrintReservationDTO> reservations = new ArrayList<>();

    public void setScheduleAndTicketScheduleSeats(Ticket ticket) {
        if(ticket.getReservationSeats()==null || ticket.getReservationSeats().size()==0) return;
        List<PrintGenreDTO> genreList = new ArrayList<>();
        Schedule schedule = ticket.getReservationSeats().get(0).getScheduleSeat().getSchedule();
        Movie movie = schedule.getMovie();
        movie.getMovieGenreList().forEach(movieGenre ->
                genreList.add(new PrintGenreDTO(movieGenre.getGenre()))
        );
        PrintScheduleNotSchedSeatDTO printSchedule = new PrintScheduleNotSchedSeatDTO(schedule);
        printSchedule.getMovie().setGenreList(genreList);
        printSchedule.getMovie().setGradeName(movie.getGrade().getName());
        this.schedule = printSchedule;

        List<PrintReservationDTO> reservations = new ArrayList<>();
        ticket.getReservationSeats().forEach(reservation -> {
            reservations.add(PrintReservationDTO.builder().seat(new PrintSeatDTO(reservation.getScheduleSeat().getSeat()))
                                                                                    .audienceType(reservation.getAudienceType()).build());
        });
        this.reservations = reservations;
    }
}
