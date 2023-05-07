package de.thi.informatik.edi.shop.warehouse.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import de.thi.informatik.edi.shop.warehouse.model.Shipping;

public interface ShippingRepository extends CrudRepository<Shipping, UUID>{
	Optional<Shipping> findByOrderRef(UUID ref);
}
