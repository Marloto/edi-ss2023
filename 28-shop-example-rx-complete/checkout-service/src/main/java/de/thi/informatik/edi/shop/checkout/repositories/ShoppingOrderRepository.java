package de.thi.informatik.edi.shop.checkout.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import de.thi.informatik.edi.shop.checkout.model.ShoppingOrder;

public interface ShoppingOrderRepository extends CrudRepository<ShoppingOrder, UUID>{
	Optional<ShoppingOrder> findByCartRef(UUID cartRef);
}
