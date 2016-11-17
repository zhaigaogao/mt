package cmcc.mobile.maintain.service;


import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.base.JsonResult;
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
import cmcc.mobile.maintain.vo.VwtCorpVo;


public interface VwtService {
	
	public VwtCorpVo getInfoById2(String id);
	
	public VwtCorp   getInfoById(String id);
	
	public List<VwtDept> getDeptInfoByCorpId(String corpid);
	
	public List<VwtMemberInfo> getUserInfoByCorpId(String corpId);
	
	public Customer getCustomerById(String id);
	
	public List<VwtDept> getDeptByParam(Map<String,Object> map);
	
	public List<VwtMemberInfo> getUserInfoByParams(Map<String,Object> map);
	
	public List<VwtMemberInfoHis> getDeleteUserByParams(Map<String,Object> map);
	
	public List<VwtDeptHis> getDeleteDeptByParams(Map<String,Object> map);
	
	public List<VwtCorpHis> getDeleteCropByParams(Map<String,Object> map);
	
	public boolean deleteByCorpId(String id);
	
	public boolean deleteTotalUserByCorpId(String id);
	
	public VwtCustomer getVwtCustomerById(String customerId);
	
	/**
	 * 手动同步分库数据
	 * @param deptlist
	 * @param addUserList
	 * @param updateUserList
	 * @param deleteUserList
	 * @param deleteDeptList
	 * @return
	 */
	public boolean synchroVwtDataFK(String companyId,List<VwtDept> deptlist,List<VwtMemberInfo> addUserList,List<VwtMemberInfo> updateUserList,List<VwtMemberInfoHis> deleteUserList,List<VwtDeptHis> deleteDeptList);
	
	/**
	 * 手动同步主库数据
	 * @param addUserList
	 * @param updateUserList
	 * @param deleteUserList
	 * @param dbName
	 * @return
	 */
	public boolean synchroVwtDataZK(String companyId,List<VwtMemberInfo> addUserList,List<VwtMemberInfo> updateUserList,List<VwtMemberInfoHis> deleteUserList,String dbName);


	/**
	 * 开户增加分库人员和部门
	 * @return
	 */
	public boolean addUserAndDeptFk(String companyId,List<VwtDept> deptList,List<VwtMemberInfo> userList);
	
	
	/**
	 * 开户增加主库人员
	 * @param companyId 
	 * @param companyId2 
	 * @return
	 */
	public boolean addUserZk(String companyId, List<VwtMemberInfo> userList,VwtCustomer customer);

	
	/**
	 * 获取所有VWT需要同步集团
	 */
	
	public List<Customer> getAllCustomer();

	public String selectMaxtime(String corpId, String maxTime);

	public Map<String, Object> synchroVwtDataMain(String companyId, List<VwtMemberInfo> userList, VwtCustomer customer, List<VwtMemberInfo> repeatList, Map<String, Object> map);

	public List<VwtMemberInfo> check(List<VwtMemberInfo> userList, List<VwtMemberInfo> repeatList);

	public void synchroVwtDataSub(String company, List<VwtMemberInfo> userList, List<VwtDept> deptList);

	public JsonResult updateMtcustomer(MtCustomer mtc);

	public List<VwtCorp> selectListCorp(String customerMobile);
}
