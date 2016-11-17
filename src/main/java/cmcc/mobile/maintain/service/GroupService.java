package cmcc.mobile.maintain.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.entity.AdminUser;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.entity.VwtCorp;
import cmcc.mobile.maintain.vo.CustomerVo;

/**
 *
 * @author renlinggao
 * @Date 2016年6月28日
 */
public interface GroupService {
	/**
	 * 插入集团 主库插入数据
	 * 
	 * @param c
	 * @return
	 */
	public Customer addGroup1(Customer c,String type,String companyId);

	/**
	 * 插入集团 业务库插入数据
	 * 
	 * @param c
	 * @return
	 */
	public boolean addGroup2(Customer c);

	public boolean editGroup(Customer c);

	public boolean deleteGroup(String id);

	public PageInfo findByLike(Customer c, List<Long> ids,int pageNum,int pageSize);

	public boolean checkPartId(String partId);

	public Customer getById(String id);

	public void editMtUser(AdminUser user);

	public JsonResult getCorp(VwtCorp corp);

	public boolean deleteGroups(String id);
}
