package cmcc.mobile.maintain.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.loader.ResultLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.BeanUtil;

import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.dao.DeptMapper;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.Dept;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.service.LoginService;
import cmcc.mobile.maintain.util.MD5Util;
import cmcc.mobile.maintain.vo.CustomerVo;
import cmcc.mobile.maintain.vo.MtUserVo;

@Controller
@RequestMapping("user")
public class LoginController {

	@Autowired
	private LoginService loginService;
 

	/**
	 * 登录
	 * 
	 * @param loginName 登录名
	 * @param password  密码
	 * @param request
	 * @return
	 */
	@RequestMapping("login")
	@ResponseBody
	public JsonResult login(String loginName, String password, HttpServletRequest request) {
		JsonResult result = new JsonResult();
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
			result.setMessage("不能为空");
			result.setSuccess(false);
			return result;
		}

		MtUser user = loginService.selectByLoginName(loginName);
		if (null == user) {
			result.setMessage("用户不存在");
			result.setSuccess(false);
			return result;
		}
		
		if (!MD5Util.encrypt(password).equals(user.getPassword())) {
			result.setMessage("密码不正确，请重新输入");
			result.setSuccess(false);
		} else {
			// 将用户放入session中
			request.getSession().setAttribute("user", user);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param prePwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 * @param request
	 * @return
	 */
	@RequestMapping("modifyPassword")
	@ResponseBody
	public JsonResult changePwd(String prePwd, String newPwd, String surePwd, HttpServletRequest request) {
		JsonResult result = new JsonResult();
		MtUser user = (MtUser)request.getSession().getAttribute("user");
//		MtUser user = new MtUser();
//		user.setMobile("18011111111");
//		user.setLoginName("admin");
		
		if(null == user || StringUtils.isEmpty(user.getMobile())){
			result.setMessage("缓存中没有手机号码");
			return result;
		}
		//根据电话号码去获取密码
		String password = loginService.selectPwdByMobile(user.getMobile());
		if(StringUtils.isEmpty(prePwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(surePwd)){
			result.setMessage("密码不能为空");
		}else{
			
			if(!MD5Util.encrypt(prePwd).equals(password)){
				result.setMessage("密码不正确，请重新输入");
			}else{
				if(!newPwd.equals(surePwd)){
					result.setMessage("重置密码不一致，请确认！");
				}else{
					Map<String, String>map = new HashMap<String, String>();
					map.put("password", MD5Util.encrypt(newPwd));
					if(StringUtils.isEmpty(user.getLoginName())){
						result.setMessage("登录名为空");
						return result;
					}
					map.put("loginName", user.getLoginName());
					int m = loginService.updatePwdByLoginName(map);
					if(m == 1){
						result.setSuccess(true);
						result.setMessage("修改成功");
					}
				}
			}
		}
		
		return result;
	}

	/**
	 * 获取集团信息
	 * 
	 * @param request
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("showCustomer")
	@ResponseBody
	public JsonResult showTable(HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		JsonResult result = new JsonResult();
		MtUser user = (MtUser) request.getSession().getAttribute("user");
		if (null == user || user.getDeptId() == null) {
			result.setMessage("缓存中不存在");
			result.setSuccess(false);
			return result;
		}
		Long deptId = user.getDeptId();
		// Long deptId = 7l;

		List<Dept> dept_all = loginService.selectAll();
		List<Long> deptlist = findOne(dept_all, deptId, new ArrayList<Long>());
		List list_all = new ArrayList<Long>();
		List<CustomerVo> list_new = new ArrayList<CustomerVo>();
		for (Long id : deptlist) {
			List<Customer> cust_list = loginService.selectByDeptId(id);

			if (cust_list != null && cust_list.size() != 0) {
				for (Customer customer : cust_list) {
					CustomerVo customerVo = new CustomerVo();
					String address = "";
					Dept depts = loginService.selectByPrimaryKey(id);
					do {
						depts = loginService.selectByPrimaryKey(depts.getId());
						if (StringUtils.isEmpty(address)) {
							address = depts.getDepName();
						} else {
							address = depts.getDepName() + "/" + address;
						}
						depts = loginService.selectByPrimaryKey(depts.getParentId());

					} while (depts != null);
					ConvertUtils.register(new DateConverter(null), java.util.Date.class);   
					BeanUtils.copyProperties(customerVo, customer);
					customerVo.setCompanyAddress(address);
					list_new.add(customerVo);
				}
				list_all.addAll(list_new);
			}
		}

		result.setModel(list_all);

		return result;

	}

	@RequestMapping("showUser")
	@ResponseBody
	public JsonResult showUser(HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		JsonResult result = new JsonResult();
		 MtUser user = (MtUser) request.getSession().getAttribute("user");
		 if (null == user || user.getDeptId() == null) {
		 result.setMessage("缓存不存在");
		 result.setSuccess(false);
		 return result;
		 }
		 Long deptId = user.getDeptId();
		List<Dept> dept_all = loginService.selectAll();
		List<Long> deptlist = findOne(dept_all, deptId, new ArrayList<Long>());
		List list_all = new ArrayList<Long>();
		List<MtUserVo> list_new = new ArrayList<MtUserVo>();
		for (Long id : deptlist) {
			List<MtUser> cust_list = loginService.selectAllUserByDeptId(id);

			if (cust_list != null && cust_list.size() != 0) {
				for (MtUser mtuser : cust_list) {
					MtUserVo mtUserVo = new MtUserVo();
					String address = "";
					Dept depts = loginService.selectByPrimaryKey(id);
					do {
						depts = loginService.selectByPrimaryKey(depts.getId());
						if (StringUtils.isEmpty(address)) {
							address = depts.getDepName();
						} else {
							address = depts.getDepName() + "/" + address;
						}
						depts = loginService.selectByPrimaryKey(depts.getParentId());

					} while (depts != null);
					ConvertUtils.register(new DateConverter(null), java.util.Date.class);     
					BeanUtils.copyProperties(mtUserVo, mtuser);
					mtUserVo.setCompanyAddress(address);
					list_new.add(mtUserVo);
				}
				list_all.addAll(list_new);
			}
		}

		result.setModel(list_all);
		result.setSuccess(true);

		return result;
	}

	/**
	 * 获取所有子部门
	 * 
	 * @param allOrgList
	 * @param orgId
	 * @param list
	 * @return
	 */
	private List<Long> findOne(List<Dept> allOrgList, long orgId, List<Long> list) {
		for (int i = 0; i < allOrgList.size(); i++) {
			if (orgId == (allOrgList.get(i).getParentId())) {

				list.add(allOrgList.get(i).getId());
				findOne(allOrgList, allOrgList.get(i).getId(), list);
			}
		}
		return list;
	}

}
