package ru.coffeeplanter.newsfeed.data.dao.interfaces;

import java.util.List;

import ru.coffeeplanter.newsfeed.data.entities.News;
import ru.coffeeplanter.newsfeed.data.entities.NewsCategory;

public interface NewsDao {
	
	public void saveNews(News news);
	public List<News> listNews();
	public List<News> listNewsByCategory(int categoryId);
	public List<NewsCategory> listCategories();
	public News getNewsById(int newId);
	public void deleteNews(int newId);
	public NewsCategory getNewsCategoryById(int categoryId);

}
