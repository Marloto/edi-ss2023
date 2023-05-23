package de.thi.informatik.edi.shop.payment.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import de.thi.informatik.edi.shop.payment.model.Payment;

public interface PaymentRepository extends CrudRepository<Payment, UUID>{
	public Optional<Payment> findByOrderRef(UUID orderRef);
}
