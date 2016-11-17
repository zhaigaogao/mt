package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.VwtCorp;
import cmcc.mobile.maintain.vo.SyschroVo;
import cmcc.mobile.maintain.vo.VwtCorpVo;

public interface VwtCorpMapper {
	
	VwtCorp getInfoById(String id);
	
	VwtCorpVo getInfoById2(String id);

	VwtCorp getCorp(String corpName, String telnum);

	VwtCorp getCorp(VwtCorp corp);

	VwtCorp getCorpByRecond(SyschroVo recond);

	List<VwtCorp> selectByCustomerId(String customerId);
}
