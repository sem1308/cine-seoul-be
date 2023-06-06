package uos.cineseoul.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.insert.InsertReservationSeatDTO;
import uos.cineseoul.entity.ReservationSeat;

@Mapper(componentModel = "spring")
public interface ReservationSeatMapper {
    ReservationSeatMapper INSTANCE = Mappers.getMapper(ReservationSeatMapper.class);

    //@Mapping(target = "createdAt", ignore = true)
    ReservationSeat toEntity(InsertReservationSeatDTO ticketDTO);

//    PrintTicketScheduleSeatDTO toDTO(TicketScheduleSeat ticket);
}