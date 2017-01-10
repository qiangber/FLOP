package com.flop.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.flop.model.Status;
import com.flop.model.User;
import com.flop.model.UserInfo;
import com.flop.service.impl.AddUserByExcel;
import com.flop.service.inter.ExcelHandler;
import com.flop.service.inter.UserServiceInter;
import com.flop.utils.TestExcel;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserServiceInter userService;
	
	@Autowired
	private ExcelHandler addUserByExcel;

	@RequestMapping(value="/json/login", method=RequestMethod.POST)
	public @ResponseBody Object login(HttpServletRequest request, HttpServletResponse response) {
		Status status;
		String request_vcode = request.getParameter("verifycode");
		String session_vcode = (String)request.getSession().getAttribute("vCode");
		if(!request_vcode.equalsIgnoreCase(session_vcode)) {	
			status = new Status("error", "验证码错误！");
		} else {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			User user = new User(username, password);
			UserInfo currentUser = userService.CheckUser(user);
			if(currentUser != null){
				status = new Status("success", "登录成功!");
				request.getSession().setAttribute("user", currentUser);
				return currentUser;
			} else {
				status = new Status("error", "用户名或者密码错误!");
			}			
		}
		return status;
	}
	
	@RequestMapping(value="/app/login", method=RequestMethod.POST)
	public @ResponseBody Object applogin(HttpServletRequest request, HttpServletResponse response) {
		Status status;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User(username, password);
		UserInfo currentUser = userService.CheckUser(user);
		if(currentUser != null){
			status = new Status("success", "登录成功!");
			request.getSession().setAttribute("user", currentUser);
			return currentUser;
		} else {
			status = new Status("error", "用户名或者密码错误!");
		}			
		return status;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView adminLogin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		String request_vcode = request.getParameter("verifyCode");
		String session_vcode = (String)request.getSession().getAttribute("validateCode");
		if(!request_vcode.equalsIgnoreCase(session_vcode)) {	
			request.getSession().setAttribute("loginError", "验证码错误！");
			mav.setViewName("redirect:/admin/");
		} else {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			User user = new User(username, password);
			UserInfo currentUser = userService.CheckAdminUser(user);
			if(currentUser != null){
				request.getSession().setAttribute("adminuser", currentUser);
				mav.setViewName("redirect:/order/listToDeal.do");
			} else {
				request.getSession().setAttribute("loginError", "用户名或者密码错误！");
				mav.setViewName("redirect:/admin/");
			}			
		}
		return mav;
	}
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="type", required=true) String type,
			@RequestParam(value="page", required=false, defaultValue="1") int page) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("userList", userService.findAll(15, page, type));
		mav.addObject("currentPage", page);
		mav.addObject("pageCount", userService.getPageCount(15, type));
		if (type.equals("teacher")) {
			mav.setViewName("teacherList");			
		} else if (type.equals("student")) {
			mav.setViewName("studentList");
		}
		return mav;
	}
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public ModelAndView search(@RequestParam(value="type", required=true) String type,
			@RequestParam(value="page", required=false, defaultValue="1") int page,
			@RequestParam(value="searchName") String name,
			@RequestParam(value="searchNum") String num) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("userList", userService.findAll(15, page, type, name, num));
		mav.addObject("currentPage", page);
		mav.addObject("pageCount", userService.getPageCount(15, type, name, num));
		mav.addObject("searchName", name);
		mav.addObject("searchNum", num);
		if (type.equals("teacher")) {
			mav.setViewName("teacherSearchList");			
		} else if (type.equals("student")) {
			mav.setViewName("studentSearchList");
		}
		return mav;
	}
	
	@RequestMapping("/view")
	public ModelAndView view(@RequestParam(value="id",required=false) String id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("news", userService.findById(id));
		mav.setViewName("newsView");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false) String id,
			@RequestParam(value="type") String type) {
		ModelAndView mav = new ModelAndView();
		String update = "",add = "";
		if (type.equals("teacher")) {
			update = "teacherUpdate";
			add = "teacherAdd";
		} else if (type.equals("student")) {
			update = "studentUpdate";
			add = "studentAdd";
		}
		if (id != null) {
			mav.addObject("user", userService.findById(id));
			mav.setViewName(update);
		} else {
			mav.setViewName(add);
		}
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(UserInfo userInfo,
			@RequestParam(value="password") String password) {
		if (userInfo.getId() != 0) {
			User user = userService.findById(userInfo.getId() + "");
			user.setUserInfo(userInfo);
			user.setUsername(userInfo.getUsername());
			user.setPassword(password);
			userService.update(user);
		} else {
			if (userService.CheckUsername(userInfo.getUsername()) == null) {
				User user = new User();
				user.setUserInfo(userInfo);
				user.setUsername(userInfo.getUsername());
				user.setPassword(password);
				userService.add(user);				
			}
		}
		return "redirect:list.do?type=" + userInfo.getType();			
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="id", required=true) String id,
			@RequestParam(value="type") String type) {
		if (id != null) {
			userService.delete(id);
		}
		return "redirect:/user/list.do?type=" + type;
	}
	
	@RequestMapping(value = "/loginout", method = RequestMethod.POST)
	public String adminloginout(HttpServletRequest request) {
		request.getSession().removeAttribute("adminuser");
		return "redirect:/admin/";
	}
	
	@RequestMapping(value = "/json/loginout", method = RequestMethod.POST)
	public @ResponseBody Status loginout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		return new Status("success", "注销登录成功！");
	}
	
	@RequestMapping("/json/unlogin")
	public @ResponseBody Status unlogin() {
		return new Status("error", "请重新登录！");
	}
	
	@RequestMapping("/pwd")
	public ModelAndView pwd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute("adminuser");
		mav.addObject("id", userInfo.getId());
		mav.addObject("username", userService.findById(userInfo.getId()+"").getUsername());
		mav.setViewName("pwdUpdate");
		return mav;
	}
	
	@RequestMapping("/pwdUpdate")
	public String pwdUpdate(HttpServletRequest request) {
		String oldusername = request.getParameter("oldusername");
		String username = request.getParameter("username");
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		String id = request.getParameter("id");
		if (userService.CheckAdminUser(new User(oldusername, oldpassword)) != null) {
			User user = userService.findById(id);
			user.setUsername(username);
			user.setPassword(newpassword);
			user.getUserInfo().setUsername(username);
			userService.update(user);
			request.getSession().setAttribute("loginError", "修改成功,请重新登录！");			
		} else {
			request.getSession().setAttribute("loginError", "用户名或者密码错误,请重新登录!");
		}
		request.getSession().removeAttribute("adminuser");
		return "redirect:/admin/";
	}
	
	@RequestMapping(value="/json/update", method=RequestMethod.POST)
	public @ResponseBody UserInfo update(UserInfo userInfo) {
		User user = userService.findById(userInfo.getId()+"");
		user.getUserInfo().setEmail(userInfo.getEmail());
		user.getUserInfo().setPhone(userInfo.getPhone());
		userService.update(user);
		return userService.findById(userInfo.getId()+"").getUserInfo();
	}
	
	@RequestMapping("/add")
	public ModelAndView uploadFile(@RequestParam("excel") MultipartFile file, HttpServletRequest request) {
		String fileName = file.getOriginalFilename().equals("addUser.xlsx")
				?"addUserTemp":file.getOriginalFilename();
		String filePath = request.getServletContext().getRealPath("resources") + File.separator
				+ "excel" + File.separator + fileName;
		try {
			file.transferTo(new File(filePath));
			Status status = addUserByExcel.getInfoFromExcel(filePath);
			if (status.getStatus().equals("success")) {
				request.setAttribute("result", status.getMsg());
			} else {
				request.setAttribute("result", "第" + status.getMsg() + "行出错，请检查！");
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("type", "teacher");
		mav.setViewName("userAdd");
		return mav;
	}
	
	@RequestMapping("/userAdd")
	public ModelAndView userAdd(@RequestParam(value="type") String type) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("type", type);
		mav.setViewName("userAdd");
		return mav;
	}
	
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
		String fileName = "addUser.xlsx";
		String fileType = "excel";
		String filepath = request.getServletContext().getRealPath("resources") + File.separator
				+ fileType + File.separator + fileName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(filepath)), 
        		headers, HttpStatus.CREATED);
	}
}