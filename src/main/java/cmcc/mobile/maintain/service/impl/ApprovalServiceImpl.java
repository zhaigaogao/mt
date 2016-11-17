package cmcc.mobile.maintain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cmcc.mobile.maintain.dao.ApprovalTableConfigDetailsMapper;
import cmcc.mobile.maintain.dao.ApprovalTableConfigMapper;
import cmcc.mobile.maintain.dao.ApprovalTypeMapper;
import cmcc.mobile.maintain.entity.ApprovalTableConfig;
import cmcc.mobile.maintain.entity.ApprovalTableConfigDetails;
import cmcc.mobile.maintain.entity.ApprovalType;
import cmcc.mobile.maintain.service.ApprovalService;

/**
 *
 * @author renlinggao
 * @Date 2016年7月4日
 */
@Service
public class ApprovalServiceImpl implements ApprovalService {
	@Autowired
	ApprovalTypeMapper approvalTypeMapper;

	@Autowired
	ApprovalTableConfigMapper approvalTableConfigMapper;

	@Autowired
	ApprovalTableConfigDetailsMapper approvalTableConfigDetailsMapper;

	@Override
	public void copyApprovals(Map<String, Object> params) {
		List<ApprovalType> type = (List<ApprovalType>) params.get("type");
		List<ApprovalTableConfig> config = (List<ApprovalTableConfig>) params.get("config");
		List<ApprovalTableConfigDetails> details = (List<ApprovalTableConfigDetails>) params.get("details");
		approvalTypeMapper.insertBatch(type);
		approvalTableConfigMapper.insertBatch(config);
		approvalTableConfigDetailsMapper.insertBatch(details);

	}

	@Override
	public Map<String, Object> getApprovals(String companyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ApprovalType> type = approvalTypeMapper.copy(companyId);
		List<ApprovalTableConfig> config = approvalTableConfigMapper.copy(companyId);
		List<ApprovalTableConfigDetails> details = approvalTableConfigDetailsMapper.copy(companyId);
		map.put("type", type);
		map.put("config", config);
		map.put("details", details);
		return map;
	}

}
