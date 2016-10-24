package com.flop.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.Appointment;
import com.flop.model.Category;
import com.flop.model.LabAppointment;
import com.flop.model.SpeakingAppointment;
import com.flop.model.Status;
import com.flop.model.UserInfo;
import com.flop.model.WritingAppointment;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.CategoryServiceInter;
import com.flop.service.inter.OrderServiceInter;
import com.flop.utils.HibernateUtils;

@Controller
@RequestMapping("/appoint")
public class AppointmentController {
	
	@Autowired
	private AppointServiceInter appointService;
	
	@Autowired
	private CategoryServiceInter categoryService;
	
	@Autowired
	private OrderServiceInter orderService;
	
	@RequestMapping(value="/json/save", method=RequestMethod.POST)
	public @ResponseBody Status add(HttpServletRequest request,
			@RequestParam(value="lessons", required=true) String[] lessons,
			@RequestParam(value="categoryId", required=true) String categoryId) throws ParseException {
		String type = request.getParameter("type");
		Appointment appoint;
		if (type.equals("writing")) {
			appoint = new WritingAppointment();
		} else {
			appoint = new SpeakingAppointment();
		}
		appoint.setDate(new SimpleDateFormat("yyyy/MM/dd").parse(request.getParameter("date")));
		appoint.setCategoryId(categoryId);
		appoint.setPublishTime(new Date());
		appoint.setType(type);
		appoint.setUserId(request.getParameter("userId"));
		boolean flag = true;
		List<String> slist = new ArrayList<String>();
		List<String> elist = new ArrayList<String>();
		List<String> writinglist = new ArrayList<String>();
		List<String> speakinglist = new ArrayList<String>();
		for (String lesson : lessons) {
			appoint.setLesson(Integer.parseInt(lesson));
			String status = appointService.add(appoint);
			if (status.equals("success")) {
				slist.add(lesson);
			} else if (status.equals("error")) {
				elist.add(lesson);
				flag = false;
			} else if (status.equals("writing")) {
				writinglist.add(lesson);
				flag = false;
			} else if (status.equals("speaking")) {
				speakinglist.add(lesson);
				flag = false;
			}
		}
		if (flag) {
			return new Status("success", "添加成功！");
		} else {
			if (slist.size() > 0) {
				return new Status("success", "第" + listToString(slist) + "节添加成功！"
						+ (elist.size() > 0 ? "第" + listToString(elist) + "节设置失败！" : "")
						+ (writinglist.size() > 0 ? "第" + listToString(writinglist) + "节已在写作预约设置！" : "")
						+ (speakinglist.size() > 0 ? "第" + listToString(speakinglist) + "节已在口语预约设置！" : ""));		
			} else {
				return new Status("error", (elist.size() > 0 ? "第" + listToString(elist) + "节设置失败！" : "")
						+ (writinglist.size() > 0 ? "第" + listToString(writinglist) + "节已在写作预约设置！" : "")
						+ (speakinglist.size() > 0 ? "第" + listToString(speakinglist) + "节已在口语预约设置！" : ""));
			}
		}
	}
	
	private static String listToString(List<String> stringList) {
        if (stringList == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String add(LabAppointment appoint,
			@RequestParam(value="lessons") String[] lessons) throws ParseException {
		appoint.setPublishTime(new Date());
		appoint.setType("lab");
		appoint.setUserId("1");
		for (String lesson : lessons) {
			appoint.setLesson(Integer.parseInt(lesson));
			appointService.add(appoint);
		}
		return "redirect:list.do";
	}
	
	@RequestMapping("/json/list")
	public @ResponseBody List<Appointment> list(
			@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="type",required=true) String type) {
		return appointService.findByUserIdAndType(userId, type);
	}
	
	@RequestMapping("/list")
	public ModelAndView listLab(@RequestParam(value="page", required=false, defaultValue="1") int page) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("appointList", appointService.findByUserIdAndType("1", "lab", 10, page));		
		mav.addObject("pageCount", appointService.getPageCount(10));
		mav.addObject("currentPage", page);
		mav.setViewName("appointList");
		return mav;
	}
	
	@RequestMapping("/json/teacher")
	public @ResponseBody List<UserInfo> listTeacher(
			@RequestParam(value="type", required=true) String type) {
		return appointService.findTeacher(type);
	}
	
	@RequestMapping("/json/category")
	public @ResponseBody List<Category> listCategory(
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="teacherId",required=false) String teacherId) {
		return appointService.findCategory(type, teacherId);
	}
	
	@RequestMapping("/json/lessons")
	public @ResponseBody List<Appointment> listLessons(
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="teacherId",required=false) String teacherId,
			@RequestParam(value="categoryId",required=true) String categoryId) {
		return appointService.findAppoint(type, teacherId, categoryId);
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id", required=false) String id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("dateList", appointService.getDate());
		mav.addObject("categoryList", categoryService.findByType("lab"));
		if (id != null) {
			mav.addObject("appoint", appointService.findById(id));
			mav.setViewName("appointUpdate");
		} else {		
			mav.setViewName("appointAdd");
		}
		return mav;
	}
	
	@RequestMapping("/json/date")
	public @ResponseBody List<String> getDate() {
		return appointService.getDate();
	}
	
	@RequestMapping("/json/close")
	public @ResponseBody Status close_json(@RequestParam(value="appointId", required=true) String appointId) {
		Appointment appoint = appointService.findById(appointId);
		appoint.setStatus("close");
		boolean flag = HibernateUtils.merge(appoint);
		flag = flag && orderService.close(appointId);
		Status status = new Status();
		if (flag) {
			status.setStatus("success");
			status.setMsg("关闭成功！");
		} else {
			status.setStatus("error");
			status.setMsg("关闭失败！");
		}
		return status;
	}
	
	@RequestMapping(value="/close", method=RequestMethod.POST)
	public String close_page(@RequestParam(value="appointId", required=true) String appointId) {
		Appointment appoint = appointService.findById(appointId);
		appoint.setStatus("close");
		HibernateUtils.merge(appoint);
		orderService.close(appointId);
		return "redirect:list.do";
	}
}
