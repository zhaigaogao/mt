package cmcc.mobile.maintain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cmcc.mobile.maintain.base.BaseController;
import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.entity.Dept;
import cmcc.mobile.maintain.service.DeptService;

/**
 *
 * @author renlinggao
 * @Date 2016年6月29日
 */
@Controller
@RequestMapping("dept")
public class DeptController extends BaseController {

	@Autowired
	private DeptService deptService;
	
	/**
	 * 通过父地区查询下面的子地区
	 * @param parentId
	 * @return
	 */
	@RequestMapping("findByParentId")
	@ResponseBody
	public JsonResult findByParentId(Long parentId) {
		JsonResult result = new JsonResult(true, null, null);
		List<Dept> depts = deptService.getByParentId(parentId);
		result.setModel(depts);
		return result;
	}
	
	/**
	 * 根据id获取地区信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getById")
	@ResponseBody
	public JsonResult getById(Long id) {
		JsonResult result = new JsonResult(true, null, null);
		Dept d = deptService.getById(id);
		if (d == null) {
			result.setSuccess(false);
		}
		else {
			result.setModel(d);
		}
		return result;
	}
}
