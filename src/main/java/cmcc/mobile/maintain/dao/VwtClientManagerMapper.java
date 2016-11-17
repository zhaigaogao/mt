package cmcc.mobile.maintain.dao;

import cmcc.mobile.maintain.entity.VwtClientManager;

public interface VwtClientManagerMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(VwtClientManager record);

    int insertSelective(VwtClientManager record);

    VwtClientManager selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(VwtClientManager record);

    int updateByPrimaryKey(VwtClientManager record);
}