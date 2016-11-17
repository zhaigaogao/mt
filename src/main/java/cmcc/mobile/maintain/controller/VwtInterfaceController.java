package cmcc.mobile.maintain.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cmcc.mobile.maintain.base.BaseController;
import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.dao.MtCustomerMapper;
import cmcc.mobile.maintain.dao.TotalUserMapper;
import cmcc.mobile.maintain.dao.VwtCorpMapper;
import cmcc.mobile.maintain.dao.VwtCustomerMapper;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.MtCustomer;
import cmcc.mobile.maintain.entity.TotalUser;
import cmcc.mobile.maintain.entity.VwtCorp;
import cmcc.mobile.maintain.entity.VwtCorpHis;
import cmcc.mobile.maintain.entity.VwtCustomer;
import cmcc.mobile.maintain.entity.VwtDept;
import cmcc.mobile.maintain.entity.VwtDeptHis;
import cmcc.mobile.maintain.entity.VwtMemberInfo;
import cmcc.mobile.maintain.entity.VwtMemberInfoHis;
import cmcc.mobile.maintain.server.db.MultipleDataSource;
import cmcc.mobile.maintain.service.VwtService;
import cmcc.mobile.maintain.util.DBUtil;
import cmcc.mobile.maintain.util.IdCreateUtil;
import cmcc.mobile.maintain.vo.SyschroVo;
import cmcc.mobile.maintain.vo.VwtCorpVo;

@Controller
@RequestMapping("vwt")
public class VwtInterfaceController extends BaseController{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private VwtService vwtService;
	@Autowired
	private MtCustomerMapper mtCustomerMapper ;
	@Autowired
	private VwtCorpMapper vwtCorpMapper;
	/**
	 * 获取集团信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getCorpById")
	@ResponseBody
	public JsonResult login(String id) {
		JsonResult json = new JsonResult();
		if(!StringUtils.isNotEmpty(id)){
			json.setSuccess(false);
			json.setMessage("请输入集团编号");
			return json;
		}
		try{
			MultipleDataSource.setDataSourceKey("");
			Customer c = new Customer();
			c = vwtService.getCustomerById(id);
			if(c!=null){
				json.setMessage("该集团已经存在");
				json.setSuccess(false);
				return json;
			}
			MultipleDataSource.setDataSourceKey("oracle");
			VwtCorpVo corp = vwtService.getInfoById2(id);
			if(corp==null){
				json.setMessage("没有该集团信息");
				json.setSuccess(false);
				return json;
			}
			json.setSuccess(true);
			json.setModel(corp);
		}catch(Exception e){
			logger.info(e);
			json.setSuccess(false);
			json.setMessage("服务器异常");
		}
		return json;
	}
	

	/**
	 * 没有通过V网通开户的同步
	 */
	@RequestMapping("synchroVwtData")
	@ResponseBody
	public JsonResult synchroVwtData(HttpServletRequest request,SyschroVo recond){
		Map<String, Object> map = new HashMap<String , Object>() ;
		String maxTime = null ;
		//因为没有通过V网通开户的用户没有V网通信息，需要先获取集团编号
		if (StringUtils.isNotEmpty(recond.getEcode())) {
			MultipleDataSource.setDataSourceKey("oracle");
			//根据集团编号查询集团是否存在			
			VwtCorp vwtCorp  = vwtCorpMapper.getCorpByRecond(recond) ;
			if(vwtCorp==null){
				return new JsonResult(false,"该集团有误",null) ;
			}
					MultipleDataSource.setDataSourceKey("");
					//查询该集团编号是否存在
					MtCustomer mtCustomer = mtCustomerMapper.selectByEcode(recond.getEcode()) ;
					if(mtCustomer==null){
						return new JsonResult(false,"该集团已经进行过V网通数据关联同步或者该集团是通过V网通开户的",null) ;
					}
					//把V网通的相关人员组织信息按集团编号查询
					VwtCorp corp = new VwtCorp();
					MultipleDataSource.setDataSourceKey("oracle");
					corp = vwtService.getInfoById(recond.getEcode());
					String customerId = corp.getCustomerid();
					VwtCustomer customer = vwtService.getVwtCustomerById(customerId);
					List<VwtDept> deptList = vwtService.getDeptInfoByCorpId(recond.getEcode());
					List<VwtMemberInfo> userList =  vwtService.getUserInfoByCorpId(recond.getEcode());	
					//获取最大时间
					maxTime = vwtService.selectMaxtime(recond.getEcode(),maxTime) ;
					//记录重复记录
					MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());
					List<VwtMemberInfo> repeatList = new ArrayList<VwtMemberInfo>() ;
					repeatList = vwtService.check(userList,repeatList) ;
					//把V网通的相关数据和主库信息进行逻辑处理
					MultipleDataSource.setDataSourceKey("");
					MtCustomer mtcorp = mtCustomerMapper.selectByEcode(recond.getEcode()) ;
					map = vwtService.synchroVwtDataMain(mtcorp.getId(),userList,customer,repeatList,map) ;
					//把V网通的相关数据和风库信息进行逻辑处理然后同步
					MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());
					vwtService.synchroVwtDataSub(mtcorp.getId(),userList,deptList) ;
					
		
					MultipleDataSource.setDataSourceKey("");
					MtCustomer mtc = new MtCustomer() ;
					mtc.setSynchroDate(maxTime);
					mtc.setEcode(recond.getEcode());
					mtc.setCustomermanagermobile(vwtCorp.getCorpmobilephone());
					mtc.setCustomermanagername(vwtCorp.getCorppersonname());
					vwtService.updateMtcustomer(mtc) ;
					return new JsonResult(true , "操作成功",map) ;
		}
		return new JsonResult(false,"集团编号必填",null) ;
	}
	
	
	/**
	 * 开户时 导入 V网通 人员和部门数据
	 * @param corpId
	 * @return
	
	public JsonResult addVwtData(String corpId){
		JsonResult json = new JsonResult();
		try{
			MultipleDataSource.setDataSourceKey("oracle");
			VwtCorp corp = new VwtCorp();
			corp = vwtService.getInfoById(corpId);
			String customerId = corp.getCustomerid();
			VwtCustomer customer = vwtService.getVwtCustomerById(customerId);
			List<VwtDept> deptList = vwtService.getDeptInfoByCorpId(corpId);
			List<VwtMemberInfo> userList =  vwtService.getUserInfoByCorpId(corpId);
			MultipleDataSource.setDataSourceKey("");//主库
			String companyId = UUID.randomUUID().toString() ;
			vwtService.addUserZk(companyId,userList,customer);
			List<TotalUser> list =  totalUserMapper.selectByUser(userList.get(0).getCorpid()) ;
			
			MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());//分库
			vwtService.addUserAndDeptFk(companyId,deptList, userList,list);
			json.setSuccess(true);
		}catch(Exception e){
			json.setMessage("服务器异常");
			json.setSuccess(false);
		}
		return json;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 自动同步V网通数据
	 * @param corpId
	 * @param synchroDate
	 * @param dbName
	 * @return

	public JsonResult autoSynchroVwtData() {
		JsonResult json = new JsonResult();
		List<Customer> customerList = new ArrayList<Customer>();
		String companyId = UUID.randomUUID().toString() ;
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			for(int i=0;i<customerList.size();i++){
				String corpId = customerList.get(i).getId();
				String synchroDate = customerList.get(i).getSynchroDate();
				String dbName = customerList.get(i).getDbname();
				
				MultipleDataSource.setDataSourceKey("oracle");
				VwtCorp corp = new VwtCorp();
				corp = vwtService.getInfoById(corpId);
				if(corp !=null){
					map.put("corpId", corpId);
					map.put("actTime", synchroDate);
					List<VwtDept> deptlist = vwtService.getDeptByParam(map);
					map.put("createtime", synchroDate);
					List<VwtMemberInfo> addUserList = vwtService.getUserInfoByParams(map);
					map.put("createtime", null);
					map.put("operationTime",synchroDate);
					map.put("date",synchroDate);
					List<VwtMemberInfo> updateUserList = vwtService.getUserInfoByParams(map);
					List<VwtMemberInfoHis> deleteUserList = vwtService.getDeleteUserByParams(map);
					List<VwtDeptHis> deleteDeptList = vwtService.getDeleteDeptByParams(map);
					MultipleDataSource.setDataSourceKey(dbName);
					vwtService.synchroVwtDataFK(companyId,deptlist, addUserList, updateUserList, deleteUserList, deleteDeptList);
					MultipleDataSource.setDataSourceKey("");
					vwtService.synchroVwtDataZK(companyId,addUserList, updateUserList, deleteUserList,dbName);
				}else{
					map.put("corpId", corpId);
					map.put("date", synchroDate);
					List<VwtCorpHis> corphis = vwtService.getDeleteCropByParams(map);
					if(corphis!=null && corphis.size()>0){
						MultipleDataSource.setDataSourceKey(dbName);
						vwtService.deleteByCorpId(corpId);
						MultipleDataSource.setDataSourceKey("");
						vwtService.deleteTotalUserByCorpId(corpId);
					}
				}
			}
			json.setSuccess(true);
			json.setMessage("同步成功");
		}catch(Exception e){
			logger.info(e);
			json.setSuccess(false);
			json.setMessage("服务器异常");
		}
		return json;
	}
	
	
	
	
	
	

	/**
	 * 获取部门信息
	 * @param id
	 * @return

	@RequestMapping("getDeptInfoByCorpId")
	@ResponseBody
	public JsonResult getDeptInfoByCorpId(String corpId) {
		JsonResult json = new JsonResult();
		try{
			MultipleDataSource.setDataSourceKey("oracle");
			List<VwtDept> list = vwtService.getDeptInfoByCorpId(corpId);
			json.setSuccess(true);
			json.setModel(list);
		}catch(Exception e){
			logger.info(e);
			json.setSuccess(false);
			json.setMessage("服务器异常");
		}
		return json;
	}

	/**
	 * 获取人员信息
	 * @param corpId
	 * @return

	@RequestMapping("getUserInfoByCorpId")
	@ResponseBody
	public JsonResult getUserInfoByCorpId(String corpId) {
		JsonResult json = new JsonResult();
		try{
			MultipleDataSource.setDataSourceKey("oracle");
			List<VwtMemberInfo> list = vwtService.getUserInfoByCorpId(corpId);
			json.setSuccess(true);
			json.setModel(list);
		}catch(Exception e){
			logger.info(e);
			json.setSuccess(false);
			json.setMessage("服务器异常");
		}
		return json;
	}
	
	

	/**
	 * 手动同步V网通数据
	 * @param corpId
	 * @return
	
	@RequestMapping("synchroVwtData")
	@ResponseBody
	public JsonResult synchroVwtData(String corpId,String synchroDate,String dbName) {
		JsonResult json = new JsonResult();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			MultipleDataSource.setDataSourceKey("oracle");
			VwtCorp corp = new VwtCorp();
			corp = vwtService.getInfoById(corpId);
			if(corp !=null){
				map.put("corpId", corpId);
				map.put("actTime", synchroDate);
				List<VwtDept> deptlist = vwtService.getDeptByParam(map);
				map.put("createtime", synchroDate);
				List<VwtMemberInfo> addUserList = vwtService.getUserInfoByParams(map);
				map.put("createtime", null);
				map.put("operationTime",synchroDate);
				map.put("date",synchroDate);
				List<VwtMemberInfo> updateUserList = vwtService.getUserInfoByParams(map);
				List<VwtMemberInfoHis> deleteUserList = vwtService.getDeleteUserByParams(map);
				List<VwtDeptHis> deleteDeptList = vwtService.getDeleteDeptByParams(map);
				MultipleDataSource.setDataSourceKey(dbName);
				vwtService.synchroVwtDataFK(deptlist, addUserList, updateUserList, deleteUserList, deleteDeptList);
				MultipleDataSource.setDataSourceKey("");
				vwtService.synchroVwtDataZK(addUserList, updateUserList, deleteUserList,dbName);
			}else{
				map.put("corpId", corpId);
				map.put("date", synchroDate);
				List<VwtCorpHis> corphis = vwtService.getDeleteCropByParams(map);
				if(corphis!=null && corphis.size()>0){
					MultipleDataSource.setDataSourceKey(dbName);
					vwtService.deleteByCorpId(corpId);
					MultipleDataSource.setDataSourceKey("");
					vwtService.deleteTotalUserByCorpId(corpId);
				}
			}
			json.setSuccess(true);
			json.setMessage("同步成功");
		}catch(Exception e){
			logger.info(e);
			json.setSuccess(false);
			json.setMessage("服务器异常");
		}
		return json;
	}
	
	
	@RequestMapping("synchroVwtData2")
	public void synchroData(){
		MultipleDataSource.setDataSourceKey("");
		List<Customer> customerList = vwtService.getAllCustomer();
		Map<String,Object> map = new HashMap<String,Object>();
		String companyId = "";
		String synchroDate = "";
		String dbName = "";
		for(int i=0;i<customerList.size();i++){
			companyId = customerList.get(i).getId();
			synchroDate = customerList.get(i).getSynchroDate();
			dbName = customerList.get(i).getDbname();
			
			MultipleDataSource.setDataSourceKey("oracle");
			VwtCorp corp = new VwtCorp();
			corp = vwtService.getInfoById(companyId);
			if(corp !=null){
				map.put("corpId", companyId);
				map.put("actTime", synchroDate);
				List<VwtDept> deptlist = vwtService.getDeptByParam(map);
				map.put("createtime", synchroDate);
				List<VwtMemberInfo> addUserList = vwtService.getUserInfoByParams(map);
				map.put("createtime", null);
				map.put("operationTime",synchroDate);
				map.put("date",synchroDate);
				List<VwtMemberInfo> updateUserList = vwtService.getUserInfoByParams(map);
				List<VwtMemberInfoHis> deleteUserList = vwtService.getDeleteUserByParams(map);
				List<VwtDeptHis> deleteDeptList = vwtService.getDeleteDeptByParams(map);
				MultipleDataSource.setDataSourceKey(dbName);
				vwtService.synchroVwtDataFK(deptlist, addUserList, updateUserList, deleteUserList, deleteDeptList);
				MultipleDataSource.setDataSourceKey("");
				vwtService.synchroVwtDataZK(addUserList, updateUserList, deleteUserList,dbName);
			}else{
				map.put("corpId", companyId);
				map.put("date", synchroDate);
				List<VwtCorpHis> corphis = vwtService.getDeleteCropByParams(map);
				if(corphis!=null && corphis.size()>0){
					MultipleDataSource.setDataSourceKey(dbName);
					vwtService.deleteByCorpId(companyId);
					MultipleDataSource.setDataSourceKey("");
					vwtService.deleteTotalUserByCorpId(companyId);		
				}
			}
		}
	}
	
	*/
}
