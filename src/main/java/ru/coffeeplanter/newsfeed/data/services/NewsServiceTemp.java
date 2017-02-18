package ru.coffeeplanter.newsfeed.data.services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ru.coffeeplanter.newsfeed.data.entities.*;

public class NewsServiceTemp {
	
	private List<News> news = new LinkedList<>();
	
	public NewsServiceTemp() {
		News news1 = this.createNews(1, "Путин Трамп", "Путин и Трамп встретятся в июле", new Date(), "Политика");
		News news2 = this.createNews(2, "Автоваз", "Автоваз увеличивает продажи", new Date(), "Экономика");
		News news3 = this.createNews(3, "Выставки", "Растёт популярность выставок художников", new Date(), "Культура");
		this.news.add(news1);
		this.news.add(news2);
		this.news.add(news3);
	}
	
	public News find(int id) {
		return this.news.stream().filter(p -> {
			return (p.getId() == id);
		}).collect(Collectors.toList()).get(0);
	}
	
	public List<News> findAll() {
		return this.news;
	}
	
	private News createNews(int id, String header, String content, Date date_published, String category) {
		News news = new News();
		news.setId(id);
		news.setHeader(header);
		news.setContent(content);
		news.setDate_published(date_published);
		NewsCategory cat = new NewsCategory();
		cat.setName(category);
		news.setCategory(cat);
		return news;
	}

}
