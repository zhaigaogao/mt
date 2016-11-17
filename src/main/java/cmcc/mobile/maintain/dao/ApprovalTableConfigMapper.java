package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.ApprovalTableConfig;
import cmcc.mobile.maintain.entity.ApprovalType;

public interface ApprovalTableConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(ApprovalTableConfig record);

    int insertSelective(ApprovalTableConfig record);

    ApprovalTableConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ApprovalTableConfig record);

    int updateByPrimaryKey(ApprovalTableConfig record);
    
    List<ApprovalTableConfig> copy(String companyId);
    
    void insertBatch(List<ApprovalTableConfig> list);
}