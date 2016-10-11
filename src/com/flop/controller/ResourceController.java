package com.flop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.Resource;
import com.flop.service.inter.ResourceServiceInter;

@Controller
@RequestMapping("/resource")
public class ResourceController {
	
	@Autowired
	private ResourceServiceInter resourceService;
	
	@RequestMapping("/json/list")
	public @ResponseBody List<Resource> resourceList() {
		return resourceService.findAll();
	}
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false, defaultValue="1") int page) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("resourceList", resourceService.findAll(10, page));
		mav.addObject("pageCount", resourceService.getPageCount(10));
		mav.addObject("currentPage", page);
		mav.setViewName("resourceList");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false) String id) {
		ModelAndView mav = new ModelAndView();
		if (id != null) {
			mav.addObject("resource", resourceService.findById(id));
			mav.setViewName("resourceUpdate");
		} else {
			mav.setViewName("resourceAdd");
		}
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(Resource r) {
		if (r.getId() != 0) {
			resourceService.update(r);
		} else {
			resourceService.add(r);
		}
		return "redirect:/resource/list.do";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="id", required=true) String id) {
		if (id != null) {
			resourceService.delete(id);
		}
		return "redirect:/resource/list.do";
	}
}