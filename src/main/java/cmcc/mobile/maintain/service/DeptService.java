package cmcc.mobile.maintain.service;

import java.util.List;

import cmcc.mobile.maintain.entity.Dept;

/**
 *
 * @author renlinggao
 * @Date 2016年6月29日
 */
public interface DeptService {
	public List<Dept> getByParentId(Long parentId);

	public Dept getById(Long id);

	public List<Long> getChildDeptIds(Long id);
	
	public String getDeptAllName(Long id);
}
