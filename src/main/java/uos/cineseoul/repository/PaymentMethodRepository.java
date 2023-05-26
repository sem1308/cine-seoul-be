package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.PaymentMethod;
import uos.cineseoul.utils.enums.PaymentMethodType;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, PaymentMethodType> {
    Optional<PaymentMethod> findByName(String name);

    Optional<PaymentMethod> findByPaymentMethodCode(String code);
}