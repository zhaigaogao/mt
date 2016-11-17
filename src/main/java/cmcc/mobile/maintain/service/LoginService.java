package cmcc.mobile.maintain.service;

import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.Dept;
import cmcc.mobile.maintain.entity.MtUser;

public interface LoginService {
	
	MtUser selectByLoginName(String loginName);
	
	List<Dept> selectAll();
	
	List<Customer> selectByDeptId(Long deptId);
	
	Dept selectByPrimaryKey(Long id);
	
	List<MtUser> selectAllUserByDeptId(Long deptId);
	
	int updatePwdByLoginName(Map<String, String>map);
	
	String selectPwdByMobile(String mobile);
	

}
