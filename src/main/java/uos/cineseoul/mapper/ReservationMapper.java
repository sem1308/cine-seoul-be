package uos.cineseoul.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.insert.InsertReservationDTO;
import uos.cineseoul.entity.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    //@Mapping(target = "createdAt", ignore = true)
    Reservation toEntity(InsertReservationDTO ticketDTO);

//    PrintTicketScheduleSeatDTO toDTO(TicketScheduleSeat ticket);
}