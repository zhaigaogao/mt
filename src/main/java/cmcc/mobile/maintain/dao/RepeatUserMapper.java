package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.RepeatUser;
import cmcc.mobile.maintain.entity.VwtMemberInfo;

public interface RepeatUserMapper {
    int insert(RepeatUser record);

    int insertSelective(RepeatUser record);

	int insertByList(List<VwtMemberInfo> repeatList);
}