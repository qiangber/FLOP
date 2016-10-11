package com.flop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
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
import com.flop.service.impl.OrderServiceImpl;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.NotificationServiceInter;
import com.flop.service.inter.OrderServiceInter;
import com.flop.utils.WordGenerator;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderServiceInter orderService;
	
	@Autowired
	private NotificationServiceInter notificationService;
	
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
		mav.addObject("orderList", orderService.findToDeal(10, page));
		mav.addObject("pageCount", orderService.getPageCount(10, "lab", "1"));
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
		order.setTime(new Date());
		order.setStatus("verify");
		boolean flag = true;
		List<String> slist = new ArrayList<String>();
		List<String> elist = new ArrayList<String>();
		List<String> rlist = new ArrayList<String>();
		for (String appointId : appointIds) {
			order.setAppointmentId(appointId);
			Status status = orderService.add(order);
			if (status.getStatus().equals("success")) {
				slist.add(appointId);
			} else if (status.getStatus().equals("error")) {
				elist.add(appointId);
				flag = false;
			} else if (status.getStatus().equals("exist")) {
				rlist.add(appointId);
				flag = false;
			}
		}
		if (flag) {
			notificationService.addNotification(new Notification(
					new Date(),
					Notification.ORDER_SERVER,
					"0",
					order.getAppoint().getUserInfo().getId() + "",
					order));
			notificationService.notifyUser(order.getAppoint().getUserInfo().getId() + "");
			return new Status("success", "预约成功，请等待回复！");
		} else {
			if (slist.size() > 0) {
				return new Status("success", listToString(slist) + "预约成功，请等待回复！"
						+ (elist.size() > 0 ? listToString(elist) + "预约失败！" : "")
						+ (rlist.size() > 0 ? listToString(rlist) + "已预约过该时段！" : ""));		
			} else {
				return new Status("error", (elist.size() > 0 ? listToString(elist) + "预约失败！" : "")
						+ (rlist.size() > 0 ? listToString(rlist) + "已预约过该时段！" : ""));
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
            result.append(appoint.getDate() + " " + appoint.getLesson() + "节");
        }
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
	public @ResponseBody Status listlistToView(
			@RequestParam(value="orderId", required=true) String orderId) {
		return orderService.cancel(orderId);
	}
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page", required=false, defaultValue="1") Integer page,
			@RequestParam(value="type", required=false, defaultValue="writing") String type) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("orderList", orderService.findAll(10, page, type));
		mav.addObject("pageCount", orderService.getAllPageCount(10, type));
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
}
