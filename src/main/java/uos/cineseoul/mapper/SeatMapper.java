package uos.cineseoul.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.InsertSeatDTO;
import uos.cineseoul.dto.UpdateSeatDTO;
import uos.cineseoul.entity.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

    Seat toEntity(InsertSeatDTO seatDTO);

    InsertSeatDTO toDTO(Seat seat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateSeatDTO dto, @MappingTarget Seat entity);
}