package cmcc.mobile.maintain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cmcc.mobile.maintain.base.BaseController;
import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.service.DeptService;
import cmcc.mobile.maintain.service.MtUserService;
import cmcc.mobile.maintain.vo.MtUserVo;

/**
 *
 * @author renlinggao
 * @Date 2016年6月29日
 */
@Controller
@RequestMapping("mtuser")
public class MtUserController extends BaseController {
	@Autowired
	private MtUserService mtUserService;

	@Autowired
	private DeptService deptService;

	/**
	 * 添加人员
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("addUser")
	@ResponseBody
	public JsonResult addUser(MtUser user) {
		JsonResult result = new JsonResult(true, null, null);
		mtUserService.addUser(user);
		return result;
	}

	/**
	 * 编辑人员（编辑 删除 停用 密码重置 停用启用）
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("editUser")
	@ResponseBody
	public JsonResult editUser(MtUser user) {
		JsonResult result = new JsonResult(false, null, null);
		boolean isOk = mtUserService.editUser(user);
		result.setSuccess(isOk);
		return result;

	}

	/**
	 * 模糊查询
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("findByCondition")
	@ResponseBody
	public JsonResult findByCondition(MtUser user, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Long deptId = getUser().getDeptId() ;
		List<Long> deptIds = deptService.getChildDeptIds(deptId) ;
		JsonResult result = new JsonResult(true, null, null) ;
		PageInfo users = mtUserService.findByLike(user, deptIds, pageNum, pageSize) ;
		result.setModel(users);
		return result;
	}
}
