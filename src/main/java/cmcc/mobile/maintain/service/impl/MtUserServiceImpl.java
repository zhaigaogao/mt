package cmcc.mobile.maintain.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cmcc.mobile.maintain.dao.MtUserMapper;
import cmcc.mobile.maintain.entity.MtUser;
import cmcc.mobile.maintain.service.DeptService;
import cmcc.mobile.maintain.service.MtUserService;
import cmcc.mobile.maintain.vo.MtUserVo;

/**
 *
 * @author renlinggao
 * @Date 2016年6月29日
 */
@Service
public class MtUserServiceImpl implements MtUserService {

	@Autowired
	private MtUserMapper userMapper;

	@Autowired
	private DeptService deptService;

	@Override
	public void addUser(MtUser user) {
		user.setCreateDate(new Date());
		userMapper.insert(user);
	}

	@Override
	public boolean editUser(MtUser user) {
		if (StringUtils.isEmpty(user.getPassword())) {
			user.setPassword(null);
		}
		MtUser u = userMapper.selectByPrimaryKey(user.getId());
		if (u != null) {
			userMapper.updateByPrimaryKeySelective(user);
			return true;
		}
		return false;
	}

	@Override
	public PageInfo findByLike(MtUser user, List<Long> ids, int pageNum, int pageSize) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("u", user);
		param.put("ids", ids);
		PageHelper.startPage(pageNum, pageSize);
		List<MtUser> users = userMapper.findByLike(param);
		PageInfo pageInfo = new PageInfo(users);
		List<MtUserVo> userVos = new ArrayList<MtUserVo>();
		for (MtUser u : users) {
			MtUserVo uv = new MtUserVo();
			BeanUtils.copyProperties(u, uv);
			uv.setCompanyAddress(deptService.getDeptAllName(u.getDeptId()));
			userVos.add(uv);
		}
		pageInfo.setList(userVos);
		return pageInfo;
	}

	@Override
	public MtUser getById(Long id) {
		MtUser u = userMapper.selectByPrimaryKey(id);
		return u;
	}
}
