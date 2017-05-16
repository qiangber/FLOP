package com.flop.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.Appointment;
import com.flop.model.Notification;
import com.flop.model.Order;
import com.flop.model.Status;
import com.flop.service.impl.AppointServiceImpl;
import com.flop.service.impl.ExportOrderByExcel;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.NotificationServiceInter;
import com.flop.service.inter.OrderServiceInter;
import com.flop.service.inter.UserServiceInter;
import com.flop.utils.WordGenerator;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderServiceInter orderService;
	
	@Autowired
	private AppointServiceInter appointService;
	
	@Autowired
	private NotificationServiceInter notificationService;
	
	@Autowired
	private UserServiceInter userService;
	
	@Autowired
	private ExportOrderByExcel exportOrderByExcel;
	
	@RequestMapping(value="/json/deal", method=RequestMethod.POST)
	public @ResponseBody Status deal(
			@RequestParam(value="orderId", required=true) String id,
			@RequestParam(value="status", required=true) String status) {
		if (orderService.deal(status, id)) {
			return new Status("success", "处理成功！");
		} else {
			return new Status("success", "处理失败！");
		}
	}
	
	@RequestMapping(value="/reject")
	public String reject(@RequestParam(value="orderId", required=true) String id) {
		orderService.deal("reject", id);
		return "redirect:listToDeal.do";
	}
	
	@RequestMapping(value="/accept")
	public String accept(@RequestParam(value="orderId", required=true) String id) {
		orderService.deal("accept", id);
		return "redirect:listToDeal.do";
	}
	
	@RequestMapping(value="/json/listToView")
	public @ResponseBody List<Order> listlistToView(
			@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="type", required=true) String type) {
		return orderService.findToView(userId, type);
	}
	
	@RequestMapping(value="/json/listToDeal")
	public @ResponseBody Map<String, Object> listToDeal(
			@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="page", required=false, defaultValue="1") int page) {
		return orderService.findToDeal(userId, type, 10, page);
	}
	
	@RequestMapping(value="/listToDeal")
	public ModelAndView labListToDeal(
			@RequestParam(value="page", required=false, defaultValue="1") int page) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("orderList", orderService.findToDeal(15, page));
		mav.addObject("pageCount", orderService.getPageCount(15, "lab", "1"));
		mav.addObject("currentPage", page);
		mav.setViewName("dealList");
		return mav;
	}
	
	@RequestMapping(value="/json/save", method=RequestMethod.POST)
	public @ResponseBody Status order(
			@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="appointId", required=true) String[] appointIds,
			@RequestParam(value="title", required=false) String title,
			@RequestParam(value="content", required=false) String content) {
		Order order = new Order();
		order.setUserId(userId);
		order.setTitle(title);
		order.setContent(content);
		order.setTime(new DateTime().toString("YYYY-MM-dd HH:mm:ss"));
		boolean flag = true;
		List<String> slist = new ArrayList<String>(); // 预约成功列表
		List<String> elist = new ArrayList<String>(); // 预约失败列表
		List<String> rlist = new ArrayList<String>(); // 预约重复列表
		List<String> emptyList = new ArrayList<String>();
		for (String appointId : appointIds) {
			order.setAppointmentId(appointId);
			Status status = orderService.add(order);
			if (status.getStatus().equals("success")) {
				slist.add(appointId);
				notificationService.addNotification(new Notification(
						new Date(),
						Notification.ORDER_SERVER,
						"0",
						order.getAppoint().getUserInfo().getId() + "",
						order));
				notificationService.notifyUser(order.getAppoint().getUserInfo().getId() + "");
			} else if (status.getStatus().equals("error")) {
				elist.add(appointId);
				flag = false;
			} else if (status.getStatus().equals("exist")) {
				rlist.add(appointId);
				flag = false;
			} else if (status.getStatus().equals("empty")) {
				emptyList.add(appointId);
				flag = false;
			} else if (status.getStatus().equals("out")) {
				return new Status("error",status.getMsg());
			}
		}
		if (flag) {
			return new Status("success", "预约成功！");
		} else {
			if (slist.size() > 0) {
				return new Status("success", listToString(slist) + "预约成功！"
						+ (elist.size() > 0 ? listToString(elist) + "预约失败！" : "")
						+ (rlist.size() > 0 ? listToString(rlist) + "已预约过该时段！" : "")
						+ (emptyList.size() > 0 ? listToString(emptyList) + "该时段预约人数已满！" : ""));		
			} else {
				return new Status("error", (elist.size() > 0 ? listToString(elist) + "预约失败！" : "")
						+ (rlist.size() > 0 ? listToString(rlist) + "已预约过该时段！" : "")
						+ (emptyList.size() > 0 ? listToString(emptyList) + "该时段预约人数已满！" : ""));
			}
		} 
	}
	
	public static String listToString(List<String> stringList) {
		AppointServiceInter service = new AppointServiceImpl();
		if (stringList == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
        	Appointment appoint = service.findById(string);
            if (flag) {
                result.append(",");
            }else {
                flag = true;
            }
            result.append(appoint.getDate() + " " + AppointmentController.map.get(appoint.getLesson() + ""));
        }
        result.append(" ");
        return result.toString();
    }
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=true) String id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("order", orderService.findById(id));
		mav.setViewName("orderView");
		return mav;
	}
	
	@RequestMapping(value="/json/cancel", method=RequestMethod.POST)
	public @ResponseBody Status cancel(
			@RequestParam(value="orderId", required=true) String orderId) {
		return orderService.cancel(orderId);
	}
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page", required=false, defaultValue="1") Integer page,
			@RequestParam(value="type", required=false, defaultValue="writing") String type) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("orderList", orderService.findAll(15, page, type));
		mav.addObject("pageCount", orderService.getAllPageCount(15, type));
		mav.addObject("currentPage", page);
		mav.addObject("type", type);
		mav.setViewName("orderList");
		return mav;
	}
	
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(HttpServletRequest request,
			@RequestParam(value="id", required=true) String id) throws IOException {
		Order order = orderService.findById(id);
		String filePath = request.getServletContext().getRealPath("resources") + File.separator
				+ "word";
		String fileName = order.getUserInfo().getUsername() + "_" 
				+ order.getAppoint().getCategory().getName() + "_"
				+ order.getTitle();
		Map dataMap = new HashMap();
		dataMap.put("title", order.getTitle());
		dataMap.put("content", order.getContent().replace("\n", "<w:p></w:p>"));
		WordGenerator.getInstance().createWord(dataMap, filePath, fileName);
		String filepath = filePath + File.separator + fileName + ".doc";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String file_name = new String(fileName.getBytes(), "ISO-8859-1");
        headers.setContentDispositionFormData("attachment", file_name + ".doc");   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(filepath)), 
        		headers, HttpStatus.CREATED);
	}
	
	@RequestMapping("/downloadAll")
	public ResponseEntity<byte[]> downloadAll(HttpServletRequest request) throws IOException {
		List<Order> orderList = orderService.findAllWriting();
		if (orderList.size() == 0) {
			return null;
		}
		String filePath = request.getServletContext().getRealPath("resources") + File.separator
				+ "word";
		String zipFilePath = request.getServletContext().getRealPath("resources") + File.separator
				+ "zip";
		for (Order order : orderList) {
			String fileName = order.getUserInfo().getUsername() + "_"
					+ order.getAppoint().getCategory().getName() + "_"
					+ order.getTitle();
			Map dataMap = new HashMap();
			dataMap.put("title", order.getTitle());
			dataMap.put("content", order.getContent().replace("\n", "<w:p></w:p>"));
			WordGenerator.getInstance().createWord(dataMap, filePath, fileName);
		}
		WordGenerator.getInstance().compressDirectory(filePath, zipFilePath, "composition");
		String filepath = zipFilePath + File.separator + "composition.zip";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "composition.zip");   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(filepath)), 
        		headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public ModelAndView search(
			@RequestParam(value="type", required=true) String type,
			@RequestParam(value="page", required=false, defaultValue="1") int page,
			@RequestParam(value="searchDate") String date,
			@RequestParam(value="searchPlace") String place,
			@RequestParam(value="searchLesson", defaultValue="0") int lesson,
			@RequestParam(value="searchObj") String obj,
			@RequestParam(value="searchNum") String num) {
		ModelAndView mav = new ModelAndView();
		Date d;
		try {
			d = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd")).toDate();			
		} catch (Exception e) {
			d = null;
		}
		mav.addObject("orderList", orderService.findAll(15, page, type, d, place, lesson, obj, num));
		mav.addObject("currentPage", page);
		mav.addObject("pageCount", orderService.getSearchPageCount(15, type, d, place, lesson, obj, num));
		mav.addObject("searchDate", date == null ? "" : date);
		mav.addObject("searchPlace", place == null ? "" : place);
		mav.addObject("searchLesson", lesson == 0 ? "" : lesson);
		mav.addObject("searchObj", obj == null ? "" : obj);
		mav.addObject("searchNum", num == null ? "" : num);
		mav.addObject("type", type);
		mav.setViewName("orderSearchList");
		return mav;
	}
	
	@RequestMapping(value="/json/export", method=RequestMethod.GET)
	public ResponseEntity<byte[]> export(HttpServletRequest request,
			@RequestParam(value="appointId", required=true) String appointId) throws IOException {
		List<Order> orders = orderService.findByAppointId(appointId);
		Appointment appoint = appointService.findById(appointId);
		String fileName = appoint.getUserInfo().getName()
				.concat("_").concat(new DateTime(appoint.getDate()).toString("YYYY-MM-dd"))
				.concat("_").concat(AppointmentController.map.get(appoint.getLesson() + "").replace(":", "-"))
				.concat("_").concat(appoint.getPlace());
		Workbook workbook = exportOrderByExcel.createExcel(appoint, orders);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1").concat(".xlsx"));
		workbook.write(out);
		out.flush();
		out.close();
        return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.CREATED);
	}
}
