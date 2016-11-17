package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.Organization;

public interface OrganizationMapper {
    int deleteByPrimaryKey(String id);

    int insert(Organization record);

    int insertSelective(Organization record);

    Organization selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Organization record);

    int updateByPrimaryKey(Organization record);
    
    int updateOrgByCompanyId(String companyId);
     
    List<Organization> getOrgByCompanyId(String companyId);

	Organization selectByPrimaryKeys(String orgId);
}