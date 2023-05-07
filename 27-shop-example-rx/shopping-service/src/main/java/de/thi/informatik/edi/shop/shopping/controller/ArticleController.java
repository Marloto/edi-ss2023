package de.thi.informatik.edi.shop.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.shop.shopping.controller.dto.ArticleResponse;
import de.thi.informatik.edi.shop.shopping.model.Article;
import de.thi.informatik.edi.shop.shopping.services.ArticleService;

@RestController
@RequestMapping("/shopping/api/v1/article")
public class ArticleController {
	private ArticleService articles;

	public ArticleController(@Autowired ArticleService articles) {
		this.articles = articles;
	}
	
	@GetMapping
	public List<ArticleResponse> list() {
		List<ArticleResponse> list = new ArrayList<>();
		for(Article article : this.articles.list()) {
			list.add(ArticleResponse.fromArticle(article));
		}
		return list;
	}
}
