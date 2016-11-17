package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.ApprovalTableConfig;
import cmcc.mobile.maintain.entity.ApprovalTableConfigDetails;

public interface ApprovalTableConfigDetailsMapper {
	int deleteByPrimaryKey(String id);

	int insert(ApprovalTableConfigDetails record);

	int insertSelective(ApprovalTableConfigDetails record);

	ApprovalTableConfigDetails selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(ApprovalTableConfigDetails record);

	int updateByPrimaryKeyWithBLOBs(ApprovalTableConfigDetails record);

	int updateByPrimaryKey(ApprovalTableConfigDetails record);

	List<ApprovalTableConfigDetails> copy(String companyId);

	void insertBatch(List<ApprovalTableConfigDetails> list);
}