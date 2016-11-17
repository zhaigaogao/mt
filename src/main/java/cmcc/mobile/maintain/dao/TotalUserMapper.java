package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.TotalUser;
import cmcc.mobile.maintain.entity.User;
import cmcc.mobile.maintain.entity.VwtMemberInfo;
import cmcc.mobile.maintain.vo.CustomerVo;

public interface TotalUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(TotalUser record);

    int insertSelective(TotalUser record);

    TotalUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TotalUser record);

    int updateByPrimaryKey(TotalUser record);
    
    int updateByCompanyId(String companyId);
    
    int batchinsertTotalUser(List<TotalUser> totalUserList);

	int updateByPrimaryKeySelectives(TotalUser totalUser);

	List<TotalUser> selectByUser(String corpId);

	List<TotalUser> selectByTotalUser(String corpId,String createtime);

	int selectByCompanyCount(String id);

	int selectByLoginCount(CustomerVo cvo);

	int selectByStartCount(CustomerVo cvo);

	int selectByDealCount(CustomerVo cvo);

	TotalUser selectByMobile(TotalUser user);

	int batchupdateTotalUser(List<TotalUser> updateUser); 
}