package com.flop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.Category;
import com.flop.model.Type;
import com.flop.service.impl.CategoryServiceImpl;
import com.flop.service.inter.CategoryServiceInter;
import com.flop.service.inter.TypeServiceInter;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryServiceInter categoryService;
	
	@Autowired
	private TypeServiceInter typeService;
	
	@RequestMapping(value="/json/list")
	public @ResponseBody List<Category> listJson(
			@RequestParam(value="type", required=true) String type) {
		return categoryService.findByType(type);
	}
	
	@RequestMapping(value="/list")
	public ModelAndView list(@RequestParam(value="page", required=false, defaultValue="1") int page) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("categoryList", categoryService.findAll(15, page));
		mav.addObject("pageCount", categoryService.getPageCount(15));
		mav.addObject("currentPage", page);
		mav.setViewName("categoryList");
		return mav;
	}

	@RequestMapping("/json/pageCount")
	public @ResponseBody int getPageCount(
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize) {
		return categoryService.getPageCount(pageSize);
	}

	
	@RequestMapping("/view")
	public ModelAndView view(@RequestParam(value="id",required=true) Integer id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("news", categoryService.findById(id+""));
		mav.setViewName("newsView");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false) Integer id) {
		ModelAndView mav = new ModelAndView();
		if (id != null) {
			mav.addObject("category", categoryService.findById(id+""));
			mav.setViewName("categoryUpdate");
		} else {
			mav.setViewName("categoryAdd");
		}
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(Category category) {
		CategoryServiceInter service = new CategoryServiceImpl();
		if (category.getId() != 0) {
			service.update(category);
		} else {
			service.add(category);
		}
		return "redirect:list.do";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="id", required=true) String id) {
		if (id != null) {
			categoryService.delete(Integer.parseInt(id));
		}
		return "redirect:list.do";
	}
	
	@RequestMapping("/limit")
	public String limit() {
		return "typeUpdate"; 
	}
	
	@RequestMapping("/updateLimit")
	public ModelAndView update(Type type) {
		ModelAndView mav = new ModelAndView("typeUpdate");
		if (typeService.update(type)) {
			mav.addObject("result", "修改成功!");
		}
		return mav;
	}
	
	@RequestMapping("/findLimit")
	public @ResponseBody int findLimit(@RequestParam(value="name", required=true) String name) {
		return typeService.getLimit(name);
	}
}
