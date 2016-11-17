package cmcc.mobile.maintain.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.dao.CustomerMapper;
import cmcc.mobile.maintain.dao.MtCustomerMapper;
import cmcc.mobile.maintain.dao.OrganizationMapper;
import cmcc.mobile.maintain.dao.RepeatUserMapper;
import cmcc.mobile.maintain.dao.TotalUserMapper;
import cmcc.mobile.maintain.dao.UserMapper;
import cmcc.mobile.maintain.dao.VwtClientManagerMapper;
import cmcc.mobile.maintain.dao.VwtCorpHisMapper;
import cmcc.mobile.maintain.dao.VwtCorpMapper;
import cmcc.mobile.maintain.dao.VwtCustomerMapper;
import cmcc.mobile.maintain.dao.VwtDeptHisMapper;
import cmcc.mobile.maintain.dao.VwtDeptMapper;
import cmcc.mobile.maintain.dao.VwtMemberInfoHisMapper;
import cmcc.mobile.maintain.dao.VwtMemberInfoMapper;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.MtCustomer;
import cmcc.mobile.maintain.entity.Organization;
import cmcc.mobile.maintain.entity.TotalUser;
import cmcc.mobile.maintain.entity.User;
import cmcc.mobile.maintain.entity.VwtClientManager;
import cmcc.mobile.maintain.entity.VwtCorp;
import cmcc.mobile.maintain.entity.VwtCorpHis;
import cmcc.mobile.maintain.entity.VwtCustomer;
import cmcc.mobile.maintain.entity.VwtDept;
import cmcc.mobile.maintain.entity.VwtDeptHis;
import cmcc.mobile.maintain.entity.VwtMemberInfo;
import cmcc.mobile.maintain.entity.VwtMemberInfoHis;
import cmcc.mobile.maintain.service.VwtService;
import cmcc.mobile.maintain.util.IdCreateUtil;
import cmcc.mobile.maintain.vo.VwtCorpVo;

@Service
public class VwtServiceImpl implements VwtService{
	
	@Autowired
	private VwtCorpMapper vwtCorpMapper;
	
	@Autowired
	private VwtDeptMapper vwtDeptMapper;
	
	@Autowired
	private VwtMemberInfoMapper vwtMemebrtInfoMapper;
	
	@Autowired
	private CustomerMapper customerMapper; 
	
	@Autowired
	private VwtMemberInfoHisMapper vwtMemeberInfoHisMapper;
	
	@Autowired
	private VwtDeptHisMapper vwtDeptHisMapper;
	
	@Autowired
	private VwtCorpHisMapper vwtCorpHisMapper;
	
	@Autowired
	private OrganizationMapper organizationMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TotalUserMapper totalUserMapper;
	
	@Autowired
	private VwtCustomerMapper vwtCustomerMapper;
	
	@Autowired
	private VwtClientManagerMapper vwtClientManagerMapper;
	
	@Autowired
	private RepeatUserMapper repeatUserMapper ;
	
	@Autowired
	private MtCustomerMapper mtCustomerMapper ;
	public VwtCorp getInfoById(String id) {
		return vwtCorpMapper.getInfoById(id);
	}
	
	public VwtCorpVo getInfoById2(String id){
		return vwtCorpMapper.getInfoById2(id);
	}

	@Override
	public List<VwtDept> getDeptInfoByCorpId(String corpid) {
		
		return vwtDeptMapper.getInfoById(corpid);
	}

	@Override
	public List<VwtMemberInfo> getUserInfoByCorpId(String corpId) {
		
		return vwtMemebrtInfoMapper.getUserInfoByCorpId(corpId);
	}

	@Override
	public Customer getCustomerById(String id) {
		
		return customerMapper.selectByPrimaryKeys(id);
	}

	@Override
	public List<VwtDept> getDeptByParam(Map<String, Object> map) {
		
		return vwtDeptMapper.getDeptByParam(map);
	}

	@Override
	public List<VwtMemberInfo> getUserInfoByParams(Map<String, Object> map) {
		
		return vwtMemebrtInfoMapper.getUserInfoByParams(map);
	}

	@Override
	public List<VwtMemberInfoHis> getDeleteUserByParams(Map<String, Object> map) {
		
		return vwtMemeberInfoHisMapper.getDeleteByParams(map);
	}

	@Override
	public List<VwtDeptHis> getDeleteDeptByParams(Map<String, Object> map) {
		return vwtDeptHisMapper.getDeleteByParams(map);
	}

	
	public List<VwtCorpHis> getDeleteCropByParams(Map<String, Object> map) {
		return vwtCorpHisMapper.getDeleteByParams(map);
	}

	
	/**
	 * 同步分库数据
	 * @param id
	 * @return
	 */
	public boolean deleteByCorpId(String id) {
		if(organizationMapper.updateOrgByCompanyId(id)>0 && userMapper.updateUserByCompanyId(id)>0){
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * 同步主库数据
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteTotalUserByCorpId(String id) {
		if(totalUserMapper.updateByCompanyId(id)>0){
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * 同步分库数据
	 * @param deptlist
	 * @param addUserList
	 * @param updateUserList
	 * @param deleteUserList
	 * @param deleteDeptList
	 * @return
	 */
	public boolean synchroVwtDataFK(String companyId ,List<VwtDept> deptlist, List<VwtMemberInfo> addUserList,
			List<VwtMemberInfo> updateUserList, List<VwtMemberInfoHis> deleteUserList,
			List<VwtDeptHis> deleteDeptList) {
		
		SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//同步部门
		for(int i=0;i<deptlist.size();i++){
			String orgId = deptlist.get(i).getDeptid();
			Organization org = new Organization();
			org.setId(deptlist.get(i).getDeptid());
			org.setOrgName(deptlist.get(i).getPartname());
			org.setShowindex(deptlist.get(i).getSort());
			org.setPreviousId(deptlist.get(i).getParentdeptnum());
			org.setCompanyId(deptlist.get(i).getCorpid());
			org.setOrgFullname(deptlist.get(i).getPartfullname());
			org.setvId(deptlist.get(i).getDeptid());
			org.setStatus("1");
		
			/**
			if(deptlist.get(i).getParentdeptnum().equals("1")){
				org.setOrgFullname(deptlist.get(i).getPartname());
			}else{
				String fullName = organizationMapper.selectByPrimaryKey(deptlist.get(i).getParentdeptnum()).getOrgName()+"/"+deptlist.get(i).getPartname();
				org.setOrgFullname(fullName);
			}
			*/
			if(organizationMapper.selectByPrimaryKeys(orgId)==null){//新增
				organizationMapper.insertSelective(org);
			}else{//更新
				organizationMapper.updateByPrimaryKeySelective(org);
			}	
		}
		
		List<User> userList = new ArrayList<User>();
		//同步新增人员
		for(int i=0;i<addUserList.size();i++){
			User user = new User();
			user.setId(addUserList.get(i).getId()) ;
			user.setvId(addUserList.get(i).getMemid());
			user.setCompanyId(companyId);
			user.setMobile(addUserList.get(i).getTelnum());
			user.setUserName(addUserList.get(i).getMembername());
			user.setOrgId(addUserList.get(i).getDeptid());
			user.setShowindex(addUserList.get(i).getSort());
			user.setWorkNumber(addUserList.get(i).getJobnum());
			user.setStatus("0");
			user.setPassWord("111111");
			user.setCreatTime(sfmt.format(addUserList.get(i).getCreattime()));
			userList.add(user) ;
			//userMapper.insertSelective(user);
		}
		userMapper.batchInsertUser(userList);
		//同步更新人员
		for(int i=0;i<updateUserList.size();i++){
			User user = new User();
			user.setvId(updateUserList.get(i).getMemid());
			user.setCompanyId(companyId);
			user.setMobile(updateUserList.get(i).getTelnum());
			user.setUserName(updateUserList.get(i).getMembername());
			user.setOrgId(updateUserList.get(i).getDeptid());
			user.setShowindex(updateUserList.get(i).getSort());
			user.setWorkNumber(updateUserList.get(i).getJobnum());
			//user.setStatus();
			user.setCreatTime(sfmt.format(updateUserList.get(i).getCreattime()));
			userMapper.updateByPrimaryKeySelectives(user);
		}
		
		//同步删除人员
		for(int i=0;i<deleteUserList.size();i++){
			User user = new User();
			user.setvId(deleteUserList.get(i).getMemid());
			user.setName(deleteUserList.get(i).getMembername());
			user.setStatus("9");
			userMapper.updateByPrimaryKeySelectives(user);
		}
		
		//同步删除部门
		for(int i=0;i<deleteDeptList.size();i++){
			Organization org = new Organization();
			org.setStatus("9");
			org.setId(deleteDeptList.get(i).getDeptid());
			organizationMapper.updateByPrimaryKeySelective(org);
		}
		
		return true;
	}

	/**
	 * 同步主库数据
	 * @param addUserList
	 * @param updateUserList
	 * @param deleteUserList
	 * @param dbName
	 * @return
	 */
	@Override
	public boolean synchroVwtDataZK(String companyId,List<VwtMemberInfo> addUserList, List<VwtMemberInfo> updateUserList,
			List<VwtMemberInfoHis> deleteUserList,String dbName) {
		List<TotalUser> userlist = new ArrayList<TotalUser>() ;
		//同步新增人员
		for(int i=0;i<addUserList.size();i++){
			TotalUser totalUser = new TotalUser();
			String id = IdCreateUtil.createGroupId() ;
			totalUser.setId(id) ;
			totalUser.setvId(addUserList.get(i).getMemid());
			totalUser.setDatabaseName(dbName);
			totalUser.setMobile(addUserList.get(i).getTelnum());
			totalUser.setName(addUserList.get(i).getMembername());
			totalUser.setType("2");
			totalUser.setCompanyId(companyId);
			totalUser.setPassword("11111");
			userlist.add(totalUser) ;
			addUserList.get(i).setId(id) ;
			totalUserMapper.insertSelective(totalUser);
		}
		//同步更新人员
		for(int i=0;i<updateUserList.size();i++){
			//String id = IdCreateUtil.createGroupId();
			TotalUser totalUser = new TotalUser();
			totalUser.setvId(updateUserList.get(i).getMemid());
			totalUser.setDatabaseName(dbName);
			totalUser.setMobile(updateUserList.get(i).getTelnum());
			totalUser.setName(updateUserList.get(i).getMembername());
			totalUser.setType("2");
			totalUser.setCompanyId(companyId);
			
			totalUserMapper.updateByPrimaryKeySelectives(totalUser);
		}
		
		//同步删除人员
		for(int i=0;i<deleteUserList.size();i++){
			TotalUser totalUser = new TotalUser();
			totalUser.setvId(deleteUserList.get(i).getMemid());
			totalUser.setStatus("9");
			totalUserMapper.updateByPrimaryKeySelectives(totalUser);
		}
		
		return true;
	}

	
	/**
	 * 开户时增加人员部门分库
	 * @param corpId
	 * @return
	 */
	public boolean addUserAndDeptFk(String companyId,List<VwtDept> deptList,List<VwtMemberInfo> userList) {
		SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(int i=0;i<deptList.size();i++){
			Organization org = new Organization();
			org.setId(deptList.get(i).getDeptid());
			org.setOrgName(deptList.get(i).getPartname());
			org.setShowindex(deptList.get(i).getSort());
			org.setOrgFullname(deptList.get(i).getPartfullname());
			org.setvId(deptList.get(i).getDeptid());
			if("1".equals(deptList.get(i).getParentdeptnum())){
				org.setPreviousId("");
			}else{
				org.setPreviousId(deptList.get(i).getParentdeptnum());
			}
			//org.setCompanyId(deptList.get(i).getCorpid());
			org.setCompanyId(companyId);
			org.setStatus("1");
			//String fullName = organizationMapper.selectByPrimaryKey(deptList.get(i).getParentdeptnum()).getOrgName()+"/"+deptList.get(i).getPartname();
			//org.setOrgFullname(fullName);
			if(organizationMapper.selectByPrimaryKeys(org.getId())==null){				
				organizationMapper.insertSelective(org);
			}else{
				//org.setStatus("1");
				organizationMapper.updateByPrimaryKeySelective(org);
			}
		}
		
		
		List<User> userList2 = new ArrayList<User>();
		for(int i=0;i<userList.size();i++){
			User user = new User();
			//String id = IdCreateUtil.createGroupId();
			user.setId(userList.get(i).getId());
			user.setCompanyId(companyId);
			user.setMobile(userList.get(i).getTelnum());
			user.setUserName(userList.get(i).getMembername());
			user.setOrgId(userList.get(i).getDeptid());
			user.setShowindex(userList.get(i).getSort());
			user.setWorkNumber(userList.get(i).getJobnum());
			user.setStatus("0");
			user.setPassWord("111111");
			user.setCreatTime(sfmt.format(userList.get(i).getCreattime()));
			user.setvId(userList.get(i).getMemid());
			userList2.add(user);
			//userMapper.insertSelective(user);
		}
		if(userList2.size()!=0){
		userMapper.batchInsertUser(userList2);
		}
		return true;
	}


	/**
	 * 开户时增加主库人员信息
	 * @param coprId
	 * @return
	 */
	public boolean addUserZk(String companyId,List<VwtMemberInfo> userList,VwtCustomer customer) {
		SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(customer!=null){
			VwtClientManager clientManager = vwtClientManagerMapper.selectByPrimaryKey(customer.getId());
			if(clientManager==null){
				clientManager = new VwtClientManager();
				clientManager.setId(customer.getId());
				clientManager.setName(customer.getName());
				clientManager.setTelnum(customer.getTelnum());
				vwtClientManagerMapper.insertSelective(clientManager);
			}else{
				clientManager.setId(customer.getId());
				clientManager.setName(customer.getName());
				clientManager.setTelnum(customer.getTelnum());
				vwtClientManagerMapper.updateByPrimaryKeySelective(clientManager);
			}
		}
		
		
		//同步新增人员
		List<TotalUser> totalUserList = new ArrayList<TotalUser>();
		for(int i=0;i<userList.size();i++){
			String id = IdCreateUtil.createGroupId();
			TotalUser totalUser = new TotalUser();
			totalUser.setId(id);
			totalUser.setDatabaseName("business1");
			totalUser.setMobile(userList.get(i).getTelnum());
			totalUser.setName(userList.get(i).getMembername());
			totalUser.setType("2");
			totalUser.setCompanyId(companyId);
			totalUser.setPassword("111111");
			totalUser.setStatus("0");
			totalUser.setCreateTime(sfmt.format(new Date()));
			totalUser.setCreatetime(sfmt.format(new Date()));
			totalUser.setvId(userList.get(i).getMemid());
			totalUserList.add(totalUser);
			userList.get(i).setId(id);
			//totalUserMapper.insertSelective(totalUser);
		}
		if(totalUserList.size()!=0){
		totalUserMapper.batchinsertTotalUser(totalUserList);
		}
		return true;
	}

	
	public VwtCustomer getVwtCustomerById(String id) {
		return vwtCustomerMapper.getCustomerById(id);
	}

	public List<Customer> getAllCustomer() {
		return customerMapper.getALlCoustomer();
	}

	@Override
	public String selectMaxtime(String corpId, String maxTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		//获取最大时间的部门信息，有可能时间相同所以用list集合
		List<VwtDept> vwtDept =  vwtDeptMapper.selectBycorpId(corpId) ;
		//获取最大时间的删除部门信息
		List<VwtDeptHis> vwtDeptHis = vwtDeptHisMapper.selectBycorpId(corpId) ;
		//获取最大时间的人员新增信息
		List<VwtMemberInfo> vwtMemberInfos = vwtMemebrtInfoMapper.selectBycorpId(corpId) ;
		//获取最大时间的人员修改信息
		List<VwtMemberInfo> vwtMemberInfo = vwtMemebrtInfoMapper.selectBycorp(corpId) ;
		//获取最大时间的人员删除信息
		List<VwtMemberInfoHis> vwtMemberInfoHis = vwtMemeberInfoHisMapper.selectBycorpId(corpId) ;
		//设置初始值
		long maxactime = 0 ;
		long maxdeletetime = 0 ;
		long maxcreatime = 0 ;
		long maxupdatetime = 0 ;
		long maxmemberdeletetime = 0 ;
		//获取最大时间
		if (vwtDept.size()!=0) {
			maxactime = vwtDept.get(0).getActTime().getTime() ;
		}
		if(vwtDeptHis.size()!=0){
			maxdeletetime = vwtDeptHis.get(0).getDeleteTime().getTime() ;
		}
		if(vwtMemberInfos.size()!=0){
			maxcreatime = vwtMemberInfos.get(0).getCreattime().getTime() ;
		}
		if(vwtMemberInfoHis.size()!=0){
			maxmemberdeletetime = vwtMemberInfoHis.get(0).getDeleteTime().getTime() ;
		}
		if(vwtMemberInfo.size()!=0){
			maxupdatetime = vwtMemberInfo.get(0).getOperationTime().getTime() ;
		}
		//两两比较得到最大值
		long maxdepettime = Math.max(maxactime, maxdeletetime) ;
		long maxmembertime = Math.max(maxcreatime, maxmemberdeletetime) ;
		long maxdate = Math.max(maxdepettime, maxmembertime) ;
		long maxtime = Math.max(maxdate, maxupdatetime) ;
		maxTime = sdf.format(maxtime) ;
		return maxTime ;
	}

	
	/**
	 * 非V网通开户的人员信息和V网通同步
	 */
	@Override
	public Map<String, Object> synchroVwtDataMain(String companyId, List<VwtMemberInfo> userList, VwtCustomer customer,List<VwtMemberInfo> repeatList,Map<String, Object> map) {
		
		SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//如果存在客户经理则先去查询主库中是否存在客户经理，如果不存在则新增，如果存在则根据姓名ID来进行更新
		if(customer!=null){
			VwtClientManager clientManager = vwtClientManagerMapper.selectByPrimaryKey(customer.getId());
			if(clientManager==null){
				clientManager = new VwtClientManager();
				clientManager.setId(customer.getId());
				clientManager.setName(customer.getName());
				clientManager.setTelnum(customer.getTelnum());
				vwtClientManagerMapper.insertSelective(clientManager);
			}else{
				clientManager.setId(customer.getId());
				clientManager.setName(customer.getName());
				clientManager.setTelnum(customer.getTelnum());
				vwtClientManagerMapper.updateByPrimaryKeySelective(clientManager);
			}
		}			
		//同步新增人员
		List<TotalUser> totalUserList = new ArrayList<TotalUser>();
		//同步更新人员
		List<TotalUser> updateUser = new ArrayList<TotalUser>();
		for(int i=0;i<userList.size();i++){
			TotalUser totalUser = new TotalUser();
			totalUser.setMobile(userList.get(i).getTelnum()) ;
			totalUser.setName(userList.get(i).getMembername()) ;
			totalUser.setCompanyId(companyId);
			//根据手机号和姓名,公司ID查询用户是否已经存在
			TotalUser tUser = totalUserMapper.selectByMobile(totalUser) ;
			String id = IdCreateUtil.createGroupId();
			
			totalUser.setDatabaseName("business1");
			totalUser.setType("2");
			totalUser.setCompanyId(companyId);
			totalUser.setPassword("111111");
			totalUser.setStatus("0");
			totalUser.setCreateTime(sfmt.format(new Date()));
			totalUser.setCreatetime(sfmt.format(new Date()));
			totalUser.setvId(userList.get(i).getMemid());
			//如果用户存在把用户ID作为userList中的用户ID，并把需要更新的数据添加到updateUser中，用于批量更新
			//如果用户不存在则用UUid生成的ID作为userList中用户ID，并把需要新增的数据添加到totalUserList中，用于批量新增
			if(tUser!=null){
				updateUser.add(totalUser) ;
				//userList.get(i).setId(tUser.getId());
			}else{
				totalUser.setId(id);
				userList.get(i).setId(id);
				totalUserList.add(totalUser);
			}
			//totalUserMapper.insertSelective(totalUser);
		}
		//如果新增的list有数据则进行批量插入
		if(totalUserList.size()!=0){
		totalUserMapper.batchinsertTotalUser(totalUserList);
		}
		//如果更新的list有数据则进行批量更新
		if(updateUser.size()!=0){
			totalUserMapper.batchupdateTotalUser(updateUser);
		}		
		map.put("repeat", repeatList) ;
		if (repeatList==null) {
			map.put("repeatNum",0);
		}else{
		map.put("repeatNum",repeatList.size());
		}
		map.put("addNum",totalUserList.size()) ;
		map.put("updateNum", updateUser.size()) ;
		return map ;
	}

	@Override
	public void synchroVwtDataSub(String companyId, List<VwtMemberInfo> userList, List<VwtDept> deptList) {
		SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//先同步组织架构
		for(int i=0;i<deptList.size();i++){
			Organization org = new Organization();
			org.setId(deptList.get(i).getDeptid());
			org.setOrgName(deptList.get(i).getPartname());
			org.setShowindex(deptList.get(i).getSort());
			org.setOrgFullname(deptList.get(i).getPartfullname());
			org.setvId(deptList.get(i).getDeptid());
			if("1".equals(deptList.get(i).getParentdeptnum())){
				org.setPreviousId("");
			}else{
				org.setPreviousId(deptList.get(i).getParentdeptnum());
			}
			//org.setCompanyId(deptList.get(i).getCorpid());
			org.setCompanyId(companyId);
			org.setStatus("1");
			//String fullName = organizationMapper.selectByPrimaryKey(deptList.get(i).getParentdeptnum()).getOrgName()+"/"+deptList.get(i).getPartname();
			//org.setOrgFullname(fullName);
			if(organizationMapper.selectByPrimaryKeys(org.getId())==null){				
				organizationMapper.insertSelective(org);
			}else{
				//org.setStatus("1");
				organizationMapper.updateByPrimaryKeySelective(org);
			}
		}
		
		//先查询本地人员信息，相同人员进行更新组织架构。
		//人员V网通不存在的保持不变。
		List<User> userList2 = new ArrayList<User>();
		List<User> userList3 = new ArrayList<User>();
		//List<User> list = userMapper.selectByCompany(companyId) ;
		for(int i=0;i<userList.size();i++){
			
			 User user = new User();
			 user.setId(userList.get(i).getId());
			 user.setCompanyId(companyId);
			 user.setMobile(userList.get(i).getTelnum());
			 user.setUserName(userList.get(i).getMembername());
			 user.setOrgId(userList.get(i).getDeptid());
			 user.setShowindex(userList.get(i).getSort());
			 user.setWorkNumber(userList.get(i).getJobnum());
			 user.setStatus("0");
			 user.setPassWord("111111");
			 user.setCreatTime(sfmt.format(userList.get(i).getCreattime()));
			 user.setvId(userList.get(i).getMemid());	
			 List<User> vwtuser = userMapper.selectByUser(user) ;	
			 if(vwtuser.size()!=0){
				 userList2.add(user) ;
			 }else{
				 userList3.add(user) ;
			 }
		}
		if(userList3.size()!=0){
		userMapper.batchInsertUser(userList3);
		}
		if(userList2.size()!=0){
			userMapper.batchUpdateUser(userList2) ;
		}
	}
	
	/**
     * 查找重复数据
     * 
     * @param userList
     *  待查找数据
	 * @return 
     */
    public List<VwtMemberInfo> check(List<VwtMemberInfo> userList,List<VwtMemberInfo> repeatList) {
        // 复制一个list
    	Date date = new Date() ;
        List<VwtMemberInfo> blist = userList;
        for (int i = 0; i < userList.size(); i++) {
            for (int j = 0; j < blist.size(); j++) {
                // 不和本身比较
                if (j != i) {
                    // 找到相同的值
                    if (userList.get(i).getTelnum().equals(blist.get(j).getTelnum())&&userList.get(i).getMembername().equals(blist.get(i).getMembername())) {
                        // 记录重复的数据
                    	userList.get(i).setCreattime(date);
                    	repeatList.add(userList.get(i)) ;
                    }
                }
            }

        }
        if(repeatList.size()!=0){
        repeatUserMapper.insertByList(repeatList) ;
        }
        return  repeatList ;
    }

	@Override
	public JsonResult updateMtcustomer(MtCustomer mtc) {
		mtCustomerMapper.updateByPrimaryKeySelective(mtc) ;
		return null ;
	}

	@Override
	public List<VwtCorp> selectListCorp(String customerMobile) {
		VwtCustomer customer = vwtCustomerMapper.seleByMobile(customerMobile) ;
		return vwtCorpMapper.selectByCustomerId(customer.getId()) ;
	}
}
