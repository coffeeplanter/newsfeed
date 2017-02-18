package ru.coffeeplanter.newsfeed.data.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

import ru.coffeeplanter.newsfeed.data.dao.interfaces.NewsDao;
import ru.coffeeplanter.newsfeed.data.entities.News;
import ru.coffeeplanter.newsfeed.data.entities.NewsCategory;
import ru.coffeeplanter.newsfeed.data.services.interfaces.NewsService;

@Service
public class NewsServiceImpl implements NewsService {
	
	@Autowired
	private NewsDao newsDao;

	@Override
	@Transactional
	public void saveNews(News news) {
		newsDao.saveNews(news);
	}

	@Override
	@Transactional
	public List<News> listNews() {
		return newsDao.listNews();
	}
	
	@Override
	@Transactional
	public List<News> listNewsByCategory(int categoryId) {
		return newsDao.listNewsByCategory(categoryId);
	}
	
	@Override
	@Transactional
	public List<NewsCategory> listCategories() {
		return newsDao.listCategories();
	}

	@Override
	@Transactional
	public News getNewsById(int newId) {
		return newsDao.getNewsById(newId);
	}

	@Override
	@Transactional
	public void deleteNews(int newId) {
		newsDao.deleteNews(newId);
	}
	
	@Override
	@Transactional
	public NewsCategory getNewsCategoryById(int categoryId) {
		return newsDao.getNewsCategoryById(categoryId);
	}
	
}
