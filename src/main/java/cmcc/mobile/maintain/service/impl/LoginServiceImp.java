package cmcc.mobile.maintain.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cmcc.mobile.maintain.dao.CustomerMapper;
import cmcc.mobile.maintain.dao.DeptMapper;
import cmcc.mobile.maintain.dao.MtUserMapper;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.Dept;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.service.LoginService;
import cmcc.mobile.maintain.vo.CustomerVo;
@Service("loginServiceImp")
public class LoginServiceImp implements LoginService{
	

	@Autowired
	private MtUserMapper mtuserMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	
	private CustomerMapper customerMapper;
	
	
	
	@Override
	public MtUser selectByLoginName(String loginName) {
		
		return mtuserMapper.selectByLoginName(loginName);
	}



	@Override
	public List<Dept> selectAll() {
		
		return deptMapper.selectAll();
	}



	//根据deptId 获取集团信息
	@Override
	public List<Customer> selectByDeptId(Long deptId) {
		
		return customerMapper.selectByDeptId(deptId);
	}


	//根据Id获取部门
	@Override
	public Dept selectByPrimaryKey(Long id) {
		
		return deptMapper.selectByPrimaryKey(id);
	}



	/**
	 * 根据部门deptId获取用户信息
	 */
	@Override
	public List<MtUser> selectAllUserByDeptId(Long deptId) {
		
		return mtuserMapper.selectAllUserByDeptId(deptId);
	}



	/**
	 * 根据用户名修改密码
	 */
	@Override
	public int updatePwdByLoginName(Map<String, String> map) {
		
		return mtuserMapper.updatePwdByLoginName(map);
	}



	@Override
	public String selectPwdByMobile(String mobile) {
		
		return mtuserMapper.selectPwdByMobile(mobile);
	}


}
