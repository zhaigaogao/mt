package cmcc.mobile.maintain.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.util.BeanUtil;

import cmcc.mobile.maintain.dao.DeptMapper;
import cmcc.mobile.maintain.entity.Dept;
import cmcc.mobile.maintain.service.DeptService;

/**
 *
 * @author renlinggao
 * @Date 2016年6月29日
 */
@Service
public class DeptServiceImpl implements DeptService {
	@Autowired
	private DeptMapper deptMapper;

	@Override
	public List<Dept> getByParentId(Long parentId) {
		Dept d = new Dept();
		d.setParentId(parentId);
		List<Dept> depts = deptMapper.findByParentId(d);
		return depts;
	}

	@Override
	public Dept getById(Long id) {
		Dept d = deptMapper.selectByPrimaryKey(id);
		return d;
	}

	@Override
	public List<Long> getChildDeptIds(Long id) {
		Set<Long> resultList = new HashSet<Long>();
		Dept dept = deptMapper.selectByPrimaryKey(id);
		resultList.add(id);
		// 获取子地区id集合
		List<Long> list = deptMapper.findChildCollections(id);
		resultList.addAll(list);
		// 如果传入的id是省地区
		if (dept.getParentId() == null || dept.getParentId() < 0) {
			for (Long dId : list) {
				List<Long> ids = deptMapper.findChildCollections(dId);
				resultList.addAll(ids);
			}
		}
		return new ArrayList<Long>(resultList);
	}

	@Override
	public String getDeptAllName(Long id) {
		String deptAllName = "";
		Dept dept = new Dept();
		dept.setId(id);
		deptAllName = getDeptByParentId(dept, deptAllName);
		return deptAllName;
	}

	private String getDeptByParentId(Dept dept, String deptAllName) {

		if (dept != null) {
			Dept d = deptMapper.selectByPrimaryKey(dept.getId());
			if (d != null) {
				if (StringUtils.isEmpty(deptAllName)) {
					deptAllName = d.getDepName();
				} else {
					deptAllName = d.getDepName() + "/" + deptAllName;
				}

				if (d.getParentId() != null) {
					dept.setId(d.getParentId());
					deptAllName = getDeptByParentId(dept, deptAllName);
				}
			}
		}

		return deptAllName;
	}

}
