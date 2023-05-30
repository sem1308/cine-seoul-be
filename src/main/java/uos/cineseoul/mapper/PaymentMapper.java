package uos.cineseoul.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import uos.cineseoul.dto.insert.InsertPaymentDTO;
import uos.cineseoul.dto.response.PrintPaymentDTO;
import uos.cineseoul.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    Payment toEntity(InsertPaymentDTO paymentDTO);

    PrintPaymentDTO toDTO(Payment payment);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateFromDto(UpdateScheduleDTO dto, @MappingTarget Schedule entity);
}