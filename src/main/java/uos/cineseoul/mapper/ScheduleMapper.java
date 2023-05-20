package uos.cineseoul.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.InsertScheduleDTO;
import uos.cineseoul.dto.UpdateScheduleDTO;
import uos.cineseoul.entity.Schedule;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    Schedule toEntity(InsertScheduleDTO scheduleDTO);

    InsertScheduleDTO toDTO(Schedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateScheduleDTO dto, @MappingTarget Schedule entity);
}