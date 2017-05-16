package com.flop.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.Appointment;
import com.flop.model.Category;
import com.flop.model.Status;
import com.flop.model.UserInfo;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.CategoryServiceInter;
import com.flop.service.inter.ExcelHandler;
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
	
	@Autowired
	private ExcelHandler addAppointByExcel;
	
	@RequestMapping(value="/json/save", method=RequestMethod.POST)
	public @ResponseBody Status add(HttpServletRequest request,
			@RequestParam(value="lessons", required=true) String[] lessons,
			@RequestParam(value="categoryId", required=true) String categoryId) throws ParseException {
		DateTime date = DateTime.parse(request.getParameter("date"), DateTimeFormat.forPattern("yyyy/MM/dd"));
		if (date.plusDays(-1).isBeforeNow()) {
			return new Status("error", "请选择两天后的日期！");
		}
		String type = request.getParameter("type");
		Appointment appoint = new Appointment();
		appoint.setType(type);
		appoint.setDate(date.toDate());
		appoint.setCategoryId(categoryId);
		appoint.setPublishTime(new DateTime().toString("YYYY-MM-dd HH:mm:ss"));
		appoint.setUserId(request.getParameter("userId"));
		appoint.setPlace(request.getParameter("place"));
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
				return new Status("success", listToString(slist) + "添加成功！"
						+ (elist.size() > 0 ? listToString(elist) + "设置失败！" : "")
						+ (writinglist.size() > 0 ? listToString(writinglist) + "已在写作预约设置！" : "")
						+ (speakinglist.size() > 0 ? listToString(speakinglist) + "已在口语预约设置！" : ""));		
			} else {
				return new Status("error", (elist.size() > 0 ? listToString(elist) + "设置失败！" : "")
						+ (writinglist.size() > 0 ? listToString(writinglist) + "已在写作预约设置！" : "")
						+ (speakinglist.size() > 0 ? listToString(speakinglist) + "已在口语预约设置！" : ""));
			}
		}
	}

//	22节课时间表	
//	private static String[] timeList = {
//			"8:30", "8:55", "9:20", "9:45", "10:20", "10:45", "11:10", "11:35",
//			"14:30", "14:55", "15:20", "15:45", "16:20", "16:45", "17:10", "17:35",
//			"19:30", "19:55", "20:20", "20:45", "21:10", "21:35"};

//  11节课时间表
	private static String[] timeList = {
			"8:30", "9:20", "10:20", "11:10", "12:40", "13:30",
			"14:30", "15:20", "16:20", "17:10", "18:40",
			"19:30", "20:20", "21:10"};
	
	public static Map<String, String> map = new HashMap<String, String>();
	
	static {
		for (int i = 0; i < timeList.length; i++) {
			map.put(i + 1 + "", timeList[i]);
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
            result.append(map.get(string));
        }
        return result.toString();
    }
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public ModelAndView add(Appointment appoint,
			@RequestParam(value="lessons") String[] lessons) throws ParseException {
		DateTime date = new DateTime(appoint.getDate());
		if (date.plusDays(-1).isBeforeNow()) {
			return new ModelAndView("redirect:preSave.do", "error", "请选择两天后的日期！");
		}
		appoint.setPublishTime(new DateTime().toString("YYYY-MM-dd HH:mm:ss"));
		appoint.setType("lab");
		appoint.setUserId("1");
		for (String lesson : lessons) {
			appoint.setLesson(Integer.parseInt(lesson));
			appointService.add(appoint);
		}
		return new ModelAndView("redirect:list.do");
	}
	
	@RequestMapping("/json/list")
	public @ResponseBody Map<String, Object> list(
			@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="page", required=false,  defaultValue="1") int page) {
		Map<String, Object> map = new HashMap<>(2);
		map.put("pageCount", appointService.getPageCount(userId, type, 10));
		map.put("appoints", appointService.findByUserIdAndType(userId, type, 10, page));
		return map;
	}
	
	@RequestMapping("/list")
	public ModelAndView listLab(@RequestParam(value="page", required=false, defaultValue="1") int page) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("appointList", appointService.findByUserIdAndType("1", "lab", 15, page));		
		mav.addObject("pageCount", appointService.getPageCount("1", "lab", 15));
		mav.addObject("currentPage", page);
		mav.setViewName("appointList");
		return mav;
	}
	
	@RequestMapping("/json/teacher")
	public @ResponseBody List<UserInfo> listTeacher(
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="categoryId",required=false) String categoryId) {
		return appointService.findTeacher(type, categoryId);
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
	
	@RequestMapping(value="/json/close", method=RequestMethod.POST)
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
	
	@RequestMapping(value="/close")
	public String close_page(@RequestParam(value="appointId", required=true) String appointId) {
		Appointment appoint = appointService.findById(appointId);
		appoint.setStatus("close");
		HibernateUtils.merge(appoint);
		orderService.close(appointId);
		return "redirect:list.do";
	}
	
	@RequestMapping(value="/batchAdd")
	public String batchAdd() {
		return "appointBatchAdd";
	}
	
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
		String fileName = "addAppoint.xlsx";
		String fileType = "excel";
		String filepath = request.getServletContext().getRealPath("resources") + File.separator
				+ fileType + File.separator + fileName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(filepath)), 
        		headers, HttpStatus.CREATED);
	}
	
	@RequestMapping("/add")
	public ModelAndView uploadFile(@RequestParam("excel") MultipartFile file, HttpServletRequest request) {
		String fileName = file.getOriginalFilename().equals("addAppoint.xlsx")
				?"addUserTemp":file.getOriginalFilename();
		String filePath = request.getServletContext().getRealPath("resources") + File.separator
				+ "excel" + File.separator + fileName;
		try {
			file.transferTo(new File(filePath));
			Status status = addAppointByExcel.getInfoFromExcel(filePath);
			if (status.getStatus().equals("success")) {
				request.setAttribute("result", status.getMsg());
			} else {
				request.setAttribute("result", status.getMsg());
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("appointBatchAdd");
		return mav;
	}
	
	@RequestMapping(value="/json/getAppointDate", method=RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> getAppointDate(@RequestParam(value="type", required=true) String type,
			@RequestParam(value="userId", required=true) String userId) {
		List<Map<String, String>> list = new ArrayList<>();
		List<Appointment> appoints = appointService.getAppointByDate(DateTime.now().minusDays(3), 7, type, userId);
		for (Appointment appointment : appoints) {
			Map<String, String> dateMap = new HashMap<>();
			dateMap.put("date", new DateTime(appointment.getDate()).toString("YYYY-MM-dd") + " " + map.get(appointment.getLesson() + ""));
			dateMap.put("appointId", appointment.getId() + "");
			list.add(dateMap);
		}
		return list;
	}
}