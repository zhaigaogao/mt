package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.Dept;

public interface DeptMapper {
	int deleteByPrimaryKey(Long id);

	int insert(Dept record);

	int insertSelective(Dept record);

	Dept selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Dept record);

	int updateByPrimaryKey(Dept record);

	List<Dept> findByParentId(Dept record);

	List<Dept> selectAll();

	List<Long> findChildCollections(Long id);
}