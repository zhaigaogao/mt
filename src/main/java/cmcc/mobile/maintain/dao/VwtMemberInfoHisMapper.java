package cmcc.mobile.maintain.dao;

import cmcc.mobile.maintain.entity.VwtMemberInfoHis;

import java.util.List;
import java.util.Map;

public interface VwtMemberInfoHisMapper {
	
	List<VwtMemberInfoHis> getDeleteByParams(Map<String,Object> map);

	List<VwtMemberInfoHis> selectBycorpId(String corpId);

}
