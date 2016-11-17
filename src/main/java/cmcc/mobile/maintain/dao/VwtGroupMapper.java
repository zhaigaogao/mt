package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.VwtGroup;

public interface VwtGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VwtGroup record);

    int insertSelective(VwtGroup record);

    VwtGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VwtGroup record);

    int updateByPrimaryKey(VwtGroup record);

	List<VwtGroup> selectAll();
}