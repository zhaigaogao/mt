package cmcc.mobile.maintain.dao;

import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.entity.VwtCorpHis;

public interface VwtCorpHisMapper {
	List<VwtCorpHis> getDeleteByParams(Map<String,Object> map);
}
