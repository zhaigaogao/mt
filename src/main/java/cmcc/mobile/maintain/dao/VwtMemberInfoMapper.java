package cmcc.mobile.maintain.dao;

import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.entity.VwtMemberInfo;

public interface VwtMemberInfoMapper {
	
	List<VwtMemberInfo> getUserInfoByCorpId(String corpId);
	
	List<VwtMemberInfo> getUserInfoByParams(Map<String,Object> map);

	List<VwtMemberInfo> selectBycorpId(String corpId);

	List<VwtMemberInfo> selectBycorp(String corpId);
}
