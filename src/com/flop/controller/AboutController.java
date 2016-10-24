package com.flop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.About;
import com.flop.service.inter.AboutServiceInter;

@Controller
@RequestMapping("/about")
public class AboutController {
	
	@Autowired
	private AboutServiceInter aboutService;
	
	@RequestMapping("/json/list")
	public @ResponseBody About aboutJson(
			@RequestParam(value="type",required=true) String type) {
		return aboutService.findByType(type);
	}
	
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("aboutList", aboutService.findAll());
		mav.setViewName("aboutList");
		return mav;
	}
	
	@RequestMapping("/view")
	public ModelAndView view(@RequestParam(value="type",required=true) String type) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("about", aboutService.findByType(type));
		mav.setViewName("aboutUpdate");
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(About about) {
		aboutService.update(about);
		return "redirect:/about/list.do";
	}
}
