package cmcc.mobile.maintain.service;

import java.util.Map;

/**
 *
 * @author renlinggao
 * @Date 2016年7月4日
 */
public interface ApprovalService {
	public void copyApprovals(Map<String, Object> params);

	public Map<String, Object> getApprovals(String companyId);
}
