package cmcc.mobile.maintain.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.dao.AdminRoleMapper;
import cmcc.mobile.maintain.dao.AdminUserMapper;
import cmcc.mobile.maintain.dao.CustomerMapper;
import cmcc.mobile.maintain.dao.OrganizationMapper;
import cmcc.mobile.maintain.dao.TotalUserMapper;
import cmcc.mobile.maintain.dao.UserMapper;
import cmcc.mobile.maintain.dao.VwtCorpMapper;
import cmcc.mobile.maintain.entity.AdminRole;
import cmcc.mobile.maintain.entity.AdminUser;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.entity.Organization;
import cmcc.mobile.maintain.entity.VwtCorp;
import cmcc.mobile.maintain.server.db.MultipleDataSource;
import cmcc.mobile.maintain.service.ApprovalService;
import cmcc.mobile.maintain.service.DeptService;
import cmcc.mobile.maintain.service.GroupService;
import cmcc.mobile.maintain.util.DBUtil;
import cmcc.mobile.maintain.util.DateUtil;
import cmcc.mobile.maintain.util.IdCreateUtil;
import cmcc.mobile.maintain.vo.CustomerVo;

@Service("groupService")
public class GroupServiceImpl implements GroupService {
	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private OrganizationMapper organizationMapper;

	@Autowired
	private AdminUserMapper adminUserMapper;

	@Autowired
	private AdminRoleMapper adminRoleMapper;

	@Autowired
	private UserMapper userMapper ;
	
	@Autowired
	DeptService deptService;

	@Autowired
	private ApprovalService approvalService;

	@Autowired
	private TotalUserMapper totalUserMapper;

	@Autowired
	private VwtCorpMapper vwtCorpMapper;
	@Override
	public Customer addGroup1(Customer c,String type,String id) {
		MultipleDataSource.setDataSourceKey("dataSource");
		// 插入集团库
		String companyId = IdCreateUtil.createGroupId();
//		if(type.equals("vwt")){
//			c.setId(id);
//		}else{
			c.setId(companyId);
		//}
		c.setRegisterDate(new Date());
		c.setDbname(DBUtil.getInstance().getDbName());
		//c.setEcode(id);
		customerMapper.insertSelective(c);
		// 插入adminuser表和adminrole表

		String userId = IdCreateUtil.createAdminUserId();
		AdminUser user = new AdminUser();
		user.setMobile(c.getContactUserName());
		user.setUsername(c.getContactUserName());
		user.setPassword(c.getContactPassword());
		user.setCreateTime(new Date());
		user.setId(userId);
		user.setName(c.getContactName());
		// 判断这个手机号用户存不存在
		AdminUser u = adminUserMapper.checkMobileExist(user);
		if (u == null) {
			adminUserMapper.insert(user);
		} else {
			user.setId(u.getId());
		}

		AdminRole role = new AdminRole();
		role.setAdminUserId(user.getId());
		/**
		if(type.equals("vwt")){
			role.setCompanyId(id);
		}else{
			role.setCompanyId(companyId);
		}
		*/
		role.setCompanyId(companyId);
		role.setRoleId("-1");
		adminRoleMapper.insert(role);
		return c;

	}

	@Override
	public boolean addGroup2(Customer c) {
		// 插入部门表
		Organization o = new Organization();
		o.setId(c.getId());
		o.setCompanyId(c.getId());
		o.setCreatTime(DateUtil.getDateStr(new Date()));
		o.setOrgName(c.getCustomerName());
		o.setOrgFullname(c.getCustomerName());		
		organizationMapper.insert(o);

		return true;
	}

	@Override
	public boolean editGroup(Customer c) {
		Customer oldC = customerMapper.selectByPrimaryKey(c.getId());
		if (oldC != null) {
			c.setDbname(oldC.getDbname());
			customerMapper.updateByPrimaryKeySelective(c);
			if (StringUtils.isNotEmpty(c.getContactPassword())) {
				AdminUser user = new AdminUser();
				user.setMobile(oldC.getContactUserName());
				user.setPassword(c.getContactPassword());
				adminUserMapper.updateByMobileSelective(user);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteGroup(String id) {
		Customer c = customerMapper.selectByPrimaryKey(id);
		if (c != null) {
			c.setStatus("0");// 逻辑删除
			customerMapper.updateByPrimaryKeySelective(c);
			adminRoleMapper.deleteByCompanyId(id);
			totalUserMapper.updateByCompanyId(id);
			return true;
		}
		return false;
	}

	@Override
	public PageInfo findByLike(Customer c, List<Long> ids, int pageNum, int pageSize) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("c", c);
		param.put("ids", ids);
		PageHelper.startPage(pageNum, pageSize);
		List<Customer> customers = customerMapper.findByLike(param);
		PageInfo pageinfo = new PageInfo(customers);
		List<CustomerVo> customerVos = new ArrayList<CustomerVo>();
		for (Customer c1 : customers) {
			CustomerVo cvo = new CustomerVo();
			BeanUtils.copyProperties(c1, cvo);
			cvo.setCompanyAddress(deptService.getDeptAllName(cvo.getDeptId()));
			customerVos.add(cvo);
		}
		pageinfo.setList(customerVos);
		return pageinfo;
	}

	@Override
	public boolean checkPartId(String partyId) {
		boolean isRepeat = true;
		Customer customer = new Customer();
		customer.setPartyId(partyId);
		customer = customerMapper.checkPartId(customer);
		if (customer == null)
			isRepeat = false;
		return isRepeat;
	}

	@Override
	public Customer getById(String id) {
		Customer c = customerMapper.selectByPrimaryKey(id);
		return c;
	}

	@Override
	public void editMtUser(AdminUser user) {
		user.setUpdateTime(new Date());
		adminUserMapper.updateByMobileSelective(user);
	}

	@Override
	public JsonResult getCorp(VwtCorp corp) {
		
		 corp = vwtCorpMapper.getCorp(corp) ;
		if(corp==null){
			return new JsonResult(false,"没有改集团信息",null);
		}
		return new JsonResult(true,"查询成功",corp);
	}

	@Override
	public boolean deleteGroups(String id) {		
			organizationMapper.updateOrgByCompanyId(id) ;
			userMapper.updateUserByCompanyId(id) ;
			return true;
	}
}
