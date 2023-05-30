package uos.cineseoul.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.dto.response.PrintSeatDTO;
import uos.cineseoul.dto.update.UpdateSeatDTO;
import uos.cineseoul.entity.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

    Seat toEntity(InsertSeatDTO seatDTO);

    PrintSeatDTO toDTO(Seat seat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateSeatDTO dto, @MappingTarget Seat entity);
}