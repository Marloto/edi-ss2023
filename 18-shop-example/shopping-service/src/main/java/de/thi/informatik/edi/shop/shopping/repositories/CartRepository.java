package de.thi.informatik.edi.shop.shopping.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import de.thi.informatik.edi.shop.shopping.model.Cart;

public interface CartRepository extends CrudRepository<Cart, UUID>{

}
