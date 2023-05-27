package uos.cineseoul.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.InsertTicketDTO;
import uos.cineseoul.dto.PrintTicketDTO;
import uos.cineseoul.dto.UpdateTicketDTO;
import uos.cineseoul.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    //@Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "issued", source="issued",
            defaultValue = "N")
    Ticket toEntity(InsertTicketDTO ticketDTO);

    PrintTicketDTO toDTO(Ticket ticket);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateTicketDTO dto, @MappingTarget Ticket entity);
}