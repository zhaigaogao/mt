package cmcc.mobile.maintain.dao;

import java.util.List;
import java.util.Map;

import cmcc.mobile.maintain.entity.MtUser;

public interface MtUserMapper {
	int deleteByPrimaryKey(Long id);

	int insert(MtUser record);

	int insertSelective(MtUser record);

	MtUser selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(MtUser record);

	int updateByPrimaryKey(MtUser record);

	List<MtUser> findByLike(Map<String, Object> param);

	MtUser selectByLoginName(String loginName);

	List<MtUser> selectAllUserByDeptId(Long deptId);

	int updatePwdByLoginName(Map<String, String> map);
	
	String selectPwdByMobile(String mobile);
}