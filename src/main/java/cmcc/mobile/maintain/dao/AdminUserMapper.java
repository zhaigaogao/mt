package cmcc.mobile.maintain.dao;

import cmcc.mobile.maintain.entity.AdminUser;

public interface AdminUserMapper {
	int deleteByPrimaryKey(String id);

	int insert(AdminUser record);

	int insertSelective(AdminUser record);

	AdminUser selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(AdminUser record);

	int updateByPrimaryKey(AdminUser record);

	AdminUser checkMobileExist(AdminUser record);
	
	int updateByMobileSelective(AdminUser record);
}