package com.flop.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.News;
import com.flop.service.inter.NewsServiceInter;

@Controller
@RequestMapping("/news")
public class NewsController {
	
	@Autowired
	private NewsServiceInter newsService;
	
	@RequestMapping(value="/json/list")
	public @ResponseBody List<News> listJson(
			@RequestParam(value="page", required=false, defaultValue="1") Integer page,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize) {
		return newsService.findAll(pageSize, page);
	}

	@RequestMapping("/json/pageCount")
	public @ResponseBody int getPageCount(
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize) {
		return newsService.getPageCount(pageSize);
	}
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false, defaultValue="1") Integer page) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("newsList", newsService.findAll(10, page));
		mav.addObject("pageCount", newsService.getPageCount(10));
		mav.addObject("currentPage", page);
		mav.setViewName("newsList");
		return mav;
	}
	
	@RequestMapping("/json/view")
	public @ResponseBody News viewJson(@RequestParam(value="id",required=true) Integer id) {
		return newsService.findById(id);
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false) Integer id) {
		ModelAndView mav = new ModelAndView();
		if (id != null) {
			mav.addObject("news", newsService.findById(id));
			mav.setViewName("newsUpdate");
		} else {
			mav.setViewName("newsAdd");
		}
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(News news) {
		news.setDate(new Date());
		if (news.getId() != 0) {
			newsService.update(news);
		} else {
			newsService.add(news);
		}
		return "redirect:/news/list.do";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="id", required=true) String id) {
		if (id != null) {
			newsService.delete(Integer.parseInt(id));
		}
		return "redirect:/news/list.do";
	}
	
	@RequestMapping("/search")
	public ModelAndView search(@RequestParam(value="title", required=false) String title,
			@RequestParam(value="date", required=false) Date date) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("newsList", newsService);
		mav.setViewName("newsList");
		return mav;
	}
}
