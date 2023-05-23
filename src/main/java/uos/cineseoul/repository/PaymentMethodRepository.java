package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    Optional<PaymentMethod> findByName(String name);

    Optional<PaymentMethod> findByPaymentMethodCode(String code);
}