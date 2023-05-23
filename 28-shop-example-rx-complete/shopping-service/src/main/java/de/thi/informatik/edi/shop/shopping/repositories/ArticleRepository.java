package de.thi.informatik.edi.shop.shopping.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import de.thi.informatik.edi.shop.shopping.model.Article;

public interface ArticleRepository extends CrudRepository<Article, UUID>{

}
