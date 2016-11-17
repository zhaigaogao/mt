package cmcc.mobile.maintain.dao;

import cmcc.mobile.maintain.entity.AdminRole;

public interface AdminRoleMapper {
    int insert(AdminRole record);

    int insertSelective(AdminRole record);
    
    int deleteByCompanyId(String companyId);
}