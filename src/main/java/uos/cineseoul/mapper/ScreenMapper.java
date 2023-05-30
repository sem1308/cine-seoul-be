package uos.cineseoul.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.insert.InsertScreenDTO;
import uos.cineseoul.dto.response.PrintScreenDTO;
import uos.cineseoul.dto.update.UpdateScreenDTO;
import uos.cineseoul.entity.Screen;

@Mapper(componentModel = "spring")
public interface ScreenMapper {
    ScreenMapper INSTANCE = Mappers.getMapper(ScreenMapper.class);

    @Mapping(target = "totalSeat", source="totalSeat",
            defaultValue = "0")
    Screen toEntity(InsertScreenDTO screenDTO);

    PrintScreenDTO toDTO(Screen screen);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateScreenDTO dto, @MappingTarget Screen entity);
}