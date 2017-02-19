package ru.coffeeplanter.newsfeed.controllers;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.coffeeplanter.newsfeed.data.entities.News;
import ru.coffeeplanter.newsfeed.data.entities.NewsCategory;
import ru.coffeeplanter.newsfeed.data.services.interfaces.NewsService;

@Controller
public class NewsController {
	
	@Autowired
	private NewsService newsService;
	
	@RequestMapping("/")
	public String showMainPage(Model model) {
		final List<News> news = new ArrayList<News>();
		for (News n : newsService.listNews()) {
			n.setContent(n.getContent().replaceAll("\n", "<br/>"));
			news.add(n);
		}
		final List<NewsCategory> categories = newsService.listCategories();
		model.addAttribute("mainPage", "true");
		model.addAttribute("allNews", news);
		model.addAttribute("allCategories", categories);
		return "list";
	}
	
	@RequestMapping(value="/category/{categoryId}")
	public String showNewsCategory(@PathVariable("categoryId") int categoryId, Model model) {
		final List<News> news = new ArrayList<News>();
		for (News n : newsService.listNewsByCategory(categoryId)) {
			n.setContent(n.getContent().replaceAll("\n", "<br/>"));
			news.add(n);
		}
		final List<NewsCategory> categories = newsService.listCategories();
		model.addAttribute("allNews", news);
		model.addAttribute("allCategories", categories);
		model.addAttribute("categoryId", categoryId);
		return "list";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String searchNews(@RequestParam("query") String query, @RequestParam("search_area") String area, Model model) {
		
		String[] queryBuffer = query.trim().split(" "); // Разделяем поисковый запрос на отдельные слова
		Map<Integer, Integer> newsMap = new HashMap<>(); // Отображение для хранения id новости и частоты встречаемости искомых слов в ней
		
		// Цикл для перебора слов запроса
		for (int h = 0; h < queryBuffer.length; h++) {
			// Цикл для перебора новостей и определения частоты встречаемости слов
			for (News n : newsService.listNews()) {
				String[] buffer;
				if (area.equals("header")) {
					buffer = n.getHeader().trim().split("\\s");
				} else {
					buffer = n.getContent().trim().split("\\s");
				}
				for (int i = 0, j = 0; i < buffer.length; i++) {
					if ((buffer[i].length() > 1) && (buffer[i].toLowerCase().contains(queryBuffer[h].toLowerCase()))) {
						j++;
						Integer k = n.getId();
						if (k != null) {
							if (newsMap.get(k) != null) {
								newsMap.put(k, j > newsMap.get(k) ? j : newsMap.get(k) + j);
							}
							else {
								newsMap.put(k, j);
							}
						}
					}
				}
			}
		}
		
		// Сортировка записей отображения по значениям (т. е. по частоте встречаемости слов)
		List<Entry<Integer, Integer>> list = new ArrayList<>(newsMap.entrySet());
		Collections.sort(list, new Comparator<Entry<Integer, Integer>>() {
	        @Override
		        public int compare(Entry<Integer, Integer> e1, Entry<Integer, Integer> e2)
		        {
		            return (e2.getValue()).compareTo(e1.getValue());
		        }
		    });
		 
		// Построение списка новостей, отсортированных по частоте встречаемости слов
		final List<News> news = new LinkedList<News>();
		for (Entry<Integer, Integer> e: list) {
			News n = newsService.getNewsById(e.getKey());
			n.setContent(n.getContent().replaceAll("\n", "<br/>"));
			news.add(n);
		}
		
		final List<NewsCategory> categories = newsService.listCategories();
		model.addAttribute("allNews", news);
		model.addAttribute("allCategories", categories);
		return "list";
	}
	
	@RequestMapping(value="/add")
	public String addNews(Model model) {
		final List<NewsCategory> categories = newsService.listCategories();
		model.addAttribute("allCategories", categories);
		return "edit";
	}
	
	@RequestMapping("/edit/{newsId}")
	public String editNews(@PathVariable("newsId") int newsId, Model model) {
		News news = newsService.getNewsById(newsId);
		if (news != null ) {
			final List<NewsCategory> categories = newsService.listCategories();
			model.addAttribute("news", news);
			model.addAttribute("allCategories", categories);
			model.addAttribute("categoryId", news.getCategory().getId());
			return "edit";
		}
		else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String saveNews(@ModelAttribute News news, @RequestParam String mode) {
		if (mode.equals("edit")) {
			Calendar dateOld = Calendar.getInstance();
			dateOld.setTime(newsService.getNewsById(news.getId()).getDate_published());
			Calendar dateNew = Calendar.getInstance();
			dateNew.setTime(news.getDate_published());
			dateNew.set(Calendar.HOUR_OF_DAY, dateOld.get(Calendar.HOUR_OF_DAY));
			dateNew.set(Calendar.MINUTE, dateOld.get(Calendar.MINUTE));
			dateNew.set(Calendar.SECOND, dateOld.get(Calendar.SECOND));
			news.setDate_published(dateNew.getTime());
		}
		NewsCategory newsCategory = newsService.getNewsCategoryById(news.getCategory().getId());
		news.setCategory(newsCategory);
		newsService.saveNews(news);
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete/{newsId}")
	public String deleteNews(@PathVariable("newsId") int newsId) {
		newsService.deleteNews(newsId);
		return "redirect:/";
	}
	
	@RequestMapping(value="/remove/{newsId}")
	public String removeNews(@PathVariable("newsId") int newsId) {
		return "redirect:/delete" + newsId;
	}
	
	@RequestMapping({"/news", "/category", "/edit", "/delete", "/remove"})
	public String redirectToMainPage() {
		return "redirect:/";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String redirectFromEmptySearchToMainPage() {
		return "redirect:/";
	}

	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new SimpleDateFormat("dd.MM.yyyy").parse(value));
					Calendar calendarCurrent = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, calendarCurrent.get(Calendar.HOUR_OF_DAY));
					calendar.set(Calendar.MINUTE, calendarCurrent.get(Calendar.MINUTE));
					calendar.set(Calendar.SECOND, calendarCurrent.get(Calendar.SECOND));
					setValue(calendar.getTime());
				} catch (ParseException e) {
					setValue(null);
				}
			}
		});
	}
	
}
