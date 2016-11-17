package cmcc.mobile.maintain.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cmcc.mobile.maintain.service.GroupService;
import cmcc.mobile.maintain.service.MtUserService;
import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.server.db.MultipleDataSource;

@Controller
public class RouteController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private MtUserService mtUserService;

	// login
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	// password modify
	@RequestMapping("/password")
	public String password() {
		return "password";
	}

	// enterprise
	@RequestMapping("/group/manager")
	public String enterpriseManager() {
		return "enterprise/manager";
	}

	@RequestMapping("/group/input")
	public String enterpriseInput(String id, HttpServletRequest request) {
		MultipleDataSource.setDataSourceKey("");
		Customer c = groupService.getById(id);
		request.setAttribute("group", c);
		return "enterprise/input";
	}
	
	// enterprise
	@RequestMapping("/group/report")
	public String enterpriseReportManager() {
		return "enterprise/report";
	}

	// user
	@RequestMapping("/mtuser/manager")
	public String userManager() {
		return "user/manager";
	}

	@RequestMapping("/mtuser/input")
	public String userInput(Long id, HttpServletRequest request) {
		MtUser u = mtUserService.getById(id);
		request.setAttribute("mtuser", u);
		return "user/input";
	}

	/**
	 * 退出
	 */
	@RequestMapping("/exit")
	@ResponseBody
	public JsonResult exit(HttpServletResponse response, HttpServletRequest request) throws IOException {
		request.getSession().invalidate();// 清空session
		// return new ModelAndView("login");
		// response.sendRedirect(request.getContextPath() + "/login.do");
		return new JsonResult(true, null, null);
	}
}
