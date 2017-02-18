package ru.coffeeplanter.newsfeed.controllers;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
		final List<News> news = newsService.listNews();
		final List<NewsCategory> categories = newsService.listCategories();
		model.addAttribute("allNews", news);
		model.addAttribute("allCategories", categories);
		return "list";
	}
	
	@RequestMapping(value="/category/{categoryId}")
	public String showNewsCategory(@PathVariable("categoryId") int categoryId, Model model) {
		final List<News> news = newsService.listNewsByCategory(categoryId);
		if (news != null) {
			final List<NewsCategory> categories = newsService.listCategories();
			model.addAttribute("allNews", news);
			model.addAttribute("allCategories", categories);
			model.addAttribute("categoryId", categoryId);
			return "list";
		}
		else {
			return "redirect:/";
		}
	}
	
	@RequestMapping("/search")
	public String searchNews(@RequestParam("query") String query, @RequestParam("search_area") String area, Model model) {
		
		final List<News> news = newsService.listNews();
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
					setValue(calendar.getTime());
				} catch (ParseException e) {
					setValue(null);
				}
			}
		});
	}
	
}
