package ru.coffeeplanter.newsfeed.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import ru.coffeeplanter.newsfeed.data.entities.NewsCategory;
import ru.coffeeplanter.newsfeed.data.services.interfaces.NewsService;

public class StringToNewsCategoryConverter implements Converter<String, NewsCategory>{

	@Autowired
	private NewsService newsService;
	
	@Override
	public NewsCategory convert(String categoryId) {
		newsService.getNewsCategoryById(Integer.parseInt(categoryId));
		return null;
	}

}
