package uos.cineseoul.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.response.PrintUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
import uos.cineseoul.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(InsertUserDTO userDTO);

    PrintUserDTO toDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateUserDTO dto, @MappingTarget User entity);
}