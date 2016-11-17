package cmcc.mobile.maintain.dao;

import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.entity.VwtDeptHis;

public interface VwtDeptHisMapper {
	
	List<VwtDeptHis> getDeleteByParams(Map<String,Object> map);

	List<VwtDeptHis> selectBycorpId(String corpId);
}
