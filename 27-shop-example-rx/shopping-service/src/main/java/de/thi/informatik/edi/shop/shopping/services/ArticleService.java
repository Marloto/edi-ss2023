package de.thi.informatik.edi.shop.shopping.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.shopping.model.Article;
import de.thi.informatik.edi.shop.shopping.repositories.ArticleRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ArticleService {
	private ArticleRepository articles;

	public ArticleService(@Autowired ArticleRepository articles) {
		this.articles = articles;
	}
	
	@PostConstruct
	public void init() {
		// Texte nicht selbst geschrieben, Author war ChatGPT
		createArticle(UUID.fromString("433fe831-3401-4e28-b550-4b6efa425431"), "1kg Äpfel", "Genießen Sie einen köstlichen und gesunden Snack mit unseren frischen Äpfeln! Unser 1 Kilo Beutel enthält knackige und saftige Äpfel, die direkt von unseren Bauernhöfen geerntet wurden. Jeder Biss in diese Äpfel ist ein Genuss für die Sinne - von der süßen und erfrischenden Geschmack, bis zur knackigen Textur, die Ihnen das Gefühl gibt, in einen frischen Apfel zu beißen.", 3.99, 100);
		createArticle(UUID.fromString("603c8cde-f5d6-4cc2-89ea-aa97276d33cf"), "1kg Orangen", "Genießen Sie den erfrischenden und fruchtigen Geschmack unserer frischen Orangen! Unser 1 Kilo Beutel enthält eine Auswahl von saftigen und süßen Orangen, die direkt von unseren Bauernhöfen geerntet wurden. Jede Orange ist eine wahre Explosion von Vitamin C und hat einen erfrischenden und leckeren Geschmack, der Sie begeistern wird.", 4.49, 100);
		createArticle(UUID.fromString("0aee9302-bcc8-4dcc-9e4b-90cc984dc2fe"), "1kg Tomaten", "Entdecken Sie den Geschmack und die Aromen Italiens mit unseren frischen, italienischen Tomaten! Unser 1 Kilo Beutel enthält eine Auswahl von sonnengereiften Tomaten, die direkt aus den besten Anbaugebieten Italiens stammen. Jede Tomate ist ein wahrer Genuss für Ihren Gaumen - von dem reichhaltigen und fruchtigen Geschmack, bis hin zu der leuchtend roten Farbe und der saftigen Textur.", 4.99, 100);
		createArticle(UUID.fromString("2906a051-3c5f-4032-98e7-b5c4b45a1573"), "1l Milch", "Genießen Sie den frischen und natürlichen Geschmack unserer Milch! Unser 1 Liter Behälter enthält frische Milch von unseren Bauernhöfen, die direkt von unseren Kühen gemolken wird. Jeder Schluck unserer Milch ist ein Genuss für Ihren Gaumen - von der cremigen und seidigen Textur bis hin zum natürlichen und milden Geschmack.", 1.29, 100);
		createArticle(UUID.fromString("1a88e23c-56ce-4a17-8b5b-4d80df805792"), "1l Olivenöl", "Willkommen in der Welt des Geschmacks und der Aromen von Spanien! Unser 1 Liter Behälter enthält frisches, kaltgepresstes Olivenöl, das direkt aus den besten Anbaugebieten Spaniens stammt. Jeder Tropfen unseres Olivenöls ist ein wahrer Genuss für Ihre Sinne - von dem reichhaltigen und fruchtigen Geschmack bis hin zum würzigen und intensiven Aroma.", 12.99, 100);
	}
	
	private Article createArticle(UUID id, String name, String description, double price, int quantity) {
		Article article = new Article(id);
		article.setName(name);
		article.setDescription(description);
		article.setPrice(price);
		article.setQuantity(quantity);
		articles.save(article);
		return article;
	}

	public Iterable<Article> list() {
		return this.articles.findAll();
	}

	public Iterable<Article> getArticles(Iterable<UUID> articles) {
		return this.articles.findAllById(articles);
	}

	public Optional<Article> getArticle(UUID id) {
		return this.articles.findById(id);
	}
}
