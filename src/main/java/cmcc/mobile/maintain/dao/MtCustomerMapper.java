package cmcc.mobile.maintain.dao;

import java.util.List;

import cmcc.mobile.maintain.entity.MtCustomer;
import cmcc.mobile.maintain.vo.SyschroVo;


public interface MtCustomerMapper {
    int deleteByPrimaryKey(String id);

    int insert(MtCustomer record);

    int insertSelective(MtCustomer record);

    MtCustomer selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MtCustomer record);

    int updateByPrimaryKey(MtCustomer record);

	MtCustomer selectByEcode(String ecode);

	int  updateBySyschroVo(SyschroVo recond);

	List<MtCustomer> selectByAllEcode(String corpId);
}