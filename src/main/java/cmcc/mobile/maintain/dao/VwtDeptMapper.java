package cmcc.mobile.maintain.dao;

import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.entity.VwtDept;

public interface VwtDeptMapper {
	
	List<VwtDept> getInfoById(String corpId);
	
	List<VwtDept> getDeptByParam(Map<String,Object> map);
	
	VwtDept getInfoByDeptId(String deptId);

	List<VwtDept> selectBycorpId(String corpId);
}
