package cmcc.mobile.maintain.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.vo.MtUserVo;

/**
 *
 * @author renlinggao
 * @Date 2016年6月29日
 */
public interface MtUserService {
	public void addUser(MtUser user);

	public boolean editUser(MtUser user);

	public PageInfo findByLike(MtUser user,List<Long> ids,int pageNum,int pageSize);
	
	public MtUser getById(Long id);
	
	
}
