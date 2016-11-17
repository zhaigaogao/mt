package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.ApprovalType;

public interface ApprovalTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(ApprovalType record);

    int insertSelective(ApprovalType record);

    ApprovalType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ApprovalType record);

    int updateByPrimaryKey(ApprovalType record);
    
    List<ApprovalType> copy(String companyId);
    
    void insertBatch(List<ApprovalType> list);
}