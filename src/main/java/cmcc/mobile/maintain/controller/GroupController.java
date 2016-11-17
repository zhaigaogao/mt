package cmcc.mobile.maintain.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cmcc.mobile.maintain.base.BaseController;
import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.dao.CustomerMapper;
import cmcc.mobile.maintain.dao.MtCustomerMapper;
import cmcc.mobile.maintain.dao.TotalUserMapper;
import cmcc.mobile.maintain.dao.VwtGroupMapper;
import cmcc.mobile.maintain.entity.AdminUser;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.MtCustomer;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.entity.TotalUser;
import cmcc.mobile.maintain.entity.VwtCorp;
import cmcc.mobile.maintain.entity.VwtCustomer;
import cmcc.mobile.maintain.entity.VwtDept;
import cmcc.mobile.maintain.entity.VwtGroup;
import cmcc.mobile.maintain.entity.VwtMemberInfo;
import cmcc.mobile.maintain.server.db.MultipleDataSource;
import cmcc.mobile.maintain.service.ApprovalService;
import cmcc.mobile.maintain.service.DeptService;
import cmcc.mobile.maintain.service.GroupService;
import cmcc.mobile.maintain.service.VwtService;
import cmcc.mobile.maintain.util.DBUtil;
import cmcc.mobile.maintain.util.IdCreateUtil;
import cmcc.mobile.maintain.vo.CustomerVo;

/**
 *
 * @author renlinggao
 * @Date 2016年6月28日
 */
@Controller
@RequestMapping("group")
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private VwtService vwtService;
	@Autowired
	private VwtGroupMapper vwtGroupMapper ;
	@Autowired
	private MtCustomerMapper mtCustomerMapper ;
	/**
	 * 新建一个 集团
	 * 
	 * @param c
	 * @return
	 */
	@RequestMapping("addGroup")
	@ResponseBody
	public JsonResult addGroup(Customer c,String submitType,String corpId) {
		JsonResult result = new JsonResult(true, null, null);
		SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (c != null) {
			if("vwt".equals(submitType)){
				c.setSynchroDate(sfmt.format(new Date()));
				
			}
			c = groupService.addGroup1(c,submitType,corpId);
			Map<String, Object> params = approvalService.getApprovals(c.getId());
			MultipleDataSource.setDataSourceKey(c.getDbname());
			if("".equals(submitType)){
				groupService.addGroup2(c);
			}
			MultipleDataSource.setDataSourceKey(c.getDbname());
			approvalService.copyApprovals(params);
			
			/**
			 * 通过Vwt开户
			 */
			if("vwt".equals(submitType)){
				
				MultipleDataSource.setDataSourceKey("oracle");
			
				VwtCorp corp = new VwtCorp();
				corp = vwtService.getInfoById(corpId);
				String customerId = corp.getCustomerid();
				VwtCustomer customer = vwtService.getVwtCustomerById(customerId);
				List<VwtDept> deptList = vwtService.getDeptInfoByCorpId(corpId);
				List<VwtMemberInfo> userList =  vwtService.getUserInfoByCorpId(corpId);
				MultipleDataSource.setDataSourceKey("");//主库
				vwtService.addUserZk(c.getId(),userList,customer);			
				MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());//分库
				vwtService.addUserAndDeptFk(c.getId(),deptList, userList);
			}
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 新建一个 集团
	 * 
	 * @param c
	 * @return
	 */
	@RequestMapping("editMtUser")
	@ResponseBody
	public JsonResult editMtUser(AdminUser user) {
		JsonResult result = new JsonResult(true, null, null);
		groupService.editMtUser(user);
		return result;
	}

	/**
	 * 编辑一个 集团
	 * 
	 * @param c
	 * @return
	 */
	@RequestMapping("editGroup")
	@ResponseBody
	public JsonResult editGroup(Customer c) {
		JsonResult result = new JsonResult(true, null, null);
		if (c == null || !StringUtils.isNotEmpty(c.getId())) {
			result.setMessage("参数不正确");
			result.setSuccess(false);
			return result;
		}
		boolean isOk = groupService.editGroup(c);
		if (!isOk) {
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 删除一个 集团
	 * 
	 * @param c
	 * @return
	 */
	@RequestMapping("deleteGroup")
	@ResponseBody
	public JsonResult deleteGroup(String id) {
		JsonResult result = new JsonResult(true, null, null);
		if (!StringUtils.isNotEmpty(id)) {
			result.setSuccess(false);
			return result;
		}
		MultipleDataSource.setDataSourceKey("") ;
		boolean isOk = groupService.deleteGroup(id);
		MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());//分库
		boolean isY = groupService.deleteGroups(id);
		if (isY&&isOk) {
			return result;
		}
		result.setSuccess(false);
		return result;
	}

	/**
	 * 模糊查询
	 * 
	 * @param c
	 * @return
	 */
	@RequestMapping("findByCondition")
	@ResponseBody
	public JsonResult findByCondition(Customer c, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Long deptId = getUser().getDeptId();
		List<Long> deptIds = deptService.getChildDeptIds(deptId);
		JsonResult result = new JsonResult(true, null, null);
		PageInfo pageInfo = groupService.findByLike(c, deptIds,pageNum,pageSize);
		result.setModel(pageInfo);
		return result;
	}

	/**
	 * 验证企业账号唯一性
	 * 
	 * @param c
	 * @return
	 */
	@RequestMapping("checkPartId")
	@ResponseBody
	public JsonResult checkPartId(String partId) {
		JsonResult result = new JsonResult(false, null, null);
		if (StringUtils.isNotEmpty(partId)) {
			result.setSuccess(groupService.checkPartId(partId));
		}
		return result;
	}

	/**
	 * 根据id获取集团信息
	 * 
	 * @param c
	 * @return
	 */
	@RequestMapping("getById")
	@ResponseBody
	public JsonResult getById(String id) {
		JsonResult result = new JsonResult(true, null, null);
		Customer c = groupService.getById(id);
		if (c == null) {
			result.setSuccess(false);
		} else {
			result.setModel(c);
		}
		return result;
	}
	
	
	
	/**
	 * 根据企业name和客户经理mobile
	 * 获取集团编号
	 */
	@RequestMapping("getCorp")
	@ResponseBody
	public JsonResult getCorp(VwtCorp corp){
		if(StringUtils.isNotEmpty(corp.getCorpname())&&StringUtils.isNotEmpty(corp.getTelnum())){
			MultipleDataSource.setDataSourceKey("oracle");
			return groupService.getCorp(corp) ;
		}
		return new JsonResult(false,"参数错误",null) ;
	}	
	
	
	
	/**
	 * 根据客户经理手机号码查询集团信息批量开户V网通用户
	 * 
	 */
	@RequestMapping("comstomerGroup")
	@ResponseBody
	public JsonResult comstomerGroup(Customer c){
		//从分库中获取需要开户的客户经理信息
		MultipleDataSource.setDataSourceKey("business1") ;
		List<VwtGroup> listVwt = vwtGroupMapper.selectAll();
		//排除没有结果集的情况		
		if(listVwt!=null && listVwt.size()!=0){		
			//循环开户
			for(int i=0 ; i<listVwt.size() ; i++){				
				String submitType = "vwt" ;
				String corpId = listVwt.get(i).getvId() ;
				c.setEcode(corpId);
				c.setPartyId(listVwt.get(i).getAreaId()) ;
				c.setCustomermanagername(listVwt.get(i).getCustomerName());
				c.setCustomermanagermobile(listVwt.get(i).getCustomerMobile());
				MultipleDataSource.setDataSourceKey("");
				List<MtCustomer> mtCustomers  = mtCustomerMapper.selectByAllEcode(corpId) ;
				if(mtCustomers.size()==0){
				groupService.addGroup1(c,submitType,corpId);
				}else{
					c.setId(mtCustomers.get(0).getId());
				}
				VwtCorp vwtcorp = new VwtCorp();
				MultipleDataSource.setDataSourceKey("oracle");
				vwtcorp = vwtService.getInfoById(corpId);
				if(vwtcorp==null){
					return new JsonResult(false,"没有集团信息",null) ;
				}
				String customerId = vwtcorp.getCustomerid();
				VwtCustomer customer = vwtService.getVwtCustomerById(customerId);
				List<VwtDept> deptList = vwtService.getDeptInfoByCorpId(corpId);
				List<VwtMemberInfo> userList =  vwtService.getUserInfoByCorpId(corpId);
				MultipleDataSource.setDataSourceKey("");//主库
				vwtService.addUserZk(c.getId(),userList,customer);				
				MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());//分库
				vwtService.addUserAndDeptFk(c.getId(),deptList, userList);
			}
		
		
		
		return new JsonResult(true,"开户成功！",null) ;
		
	}
		return new JsonResult(false,"没有需要开户的信息",null) ;
	}
	
	@RequestMapping("/getButton")
	public String getButton(){
	  return "user/button";
	}
	
	
} 