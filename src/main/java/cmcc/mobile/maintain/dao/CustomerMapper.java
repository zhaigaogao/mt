package cmcc.mobile.maintain.dao;

import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.vo.CustomerVo;

public interface CustomerMapper {
	int deleteByPrimaryKey(String id);

	int insert(Customer record);

	int insertSelective(Customer record);

	Customer selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(Customer record);

	int updateByPrimaryKey(Customer record);

	List<Customer> selectByDeptId(Long deptId);

	List<Customer> findByLike(Map<String, Object> param);

	Customer checkPartId(Customer record);
	
	List<Customer> getALlCoustomer();

	Customer selectByPrimaryKeys(String id);
}