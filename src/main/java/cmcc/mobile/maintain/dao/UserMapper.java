package cmcc.mobile.maintain.dao;

import java.util.HashMap;
import java.util.List;

import cmcc.mobile.maintain.entity.User;

public interface UserMapper {
	int deleteByPrimaryKey(String id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);
	
	int updateUserByCompanyId(String companyId);

	int batchInsertUser(List<User> list);

	int updateByPrimaryKeySelectives(User user);

	List<User> selectByCompany(String companyId);

	int batchUpdateUser(List<User> userList3);

	List<User> selectByUser(User user);

}