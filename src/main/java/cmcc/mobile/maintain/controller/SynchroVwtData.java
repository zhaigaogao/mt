package cmcc.mobile.maintain.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cmcc.mobile.maintain.dao.TotalUserMapper;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.entity.TotalUser;
import cmcc.mobile.maintain.entity.VwtCorp;
import cmcc.mobile.maintain.entity.VwtCorpHis;
import cmcc.mobile.maintain.entity.VwtDept;
import cmcc.mobile.maintain.entity.VwtDeptHis;
import cmcc.mobile.maintain.entity.VwtMemberInfo;
import cmcc.mobile.maintain.entity.VwtMemberInfoHis;
import cmcc.mobile.maintain.server.db.MultipleDataSource;
import cmcc.mobile.maintain.service.VwtService;


/**
 * 定时同步V网通数据
 * @author Administrator
 *
 */
@Service
public class SynchroVwtData {

	@Autowired
	private VwtService vwtService;

	@Autowired
	private TotalUserMapper totalUserMapper;
	
	@Scheduled(cron="0 0 2 ? * *")
	public void synchroData(){
		//SimpleDateFormat sfmt = new SimpleDateFormat("yyyy-MM-dd");
		MultipleDataSource.setDataSourceKey("");
		List<Customer> customerList = vwtService.getAllCustomer();
		Map<String,Object> map = new HashMap<String,Object>();
		String companyId = "";
		String synchroDate = "";
		String dbName = "";
		String corpId = "" ;
		String maxTime = "" ;
		for(int i=0;i<customerList.size();i++){
			companyId = customerList.get(i).getId();
			synchroDate = customerList.get(i).getSynchroDate();
			dbName = customerList.get(i).getDbname();
			corpId = customerList.get(i).getEcode() ;
			MultipleDataSource.setDataSourceKey("oracle");
			VwtCorp corp = new VwtCorp();
			corp = vwtService.getInfoById(corpId);
			if(corp !=null){
				map.put("corpId", corpId);
				map.put("actTime", synchroDate);
				List<VwtDept> deptlist = vwtService.getDeptByParam(map);
				map.put("createtime", synchroDate);
				List<VwtMemberInfo> addUserList = vwtService.getUserInfoByParams(map);
				map.put("createtime", null);
				map.put("operationTime",synchroDate);
				map.put("date",synchroDate);
				
				List<VwtMemberInfo> updateUserList = vwtService.getUserInfoByParams(map);
				List<VwtMemberInfoHis> deleteUserList = vwtService.getDeleteUserByParams(map);
				List<VwtDeptHis> deleteDeptList = vwtService.getDeleteDeptByParams(map);
				//获取最大时间
				maxTime = vwtService.selectMaxtime(corpId,maxTime) ;
				MultipleDataSource.setDataSourceKey("");
				vwtService.synchroVwtDataZK(companyId,addUserList, updateUserList, deleteUserList,dbName);
				MultipleDataSource.setDataSourceKey(dbName);
				vwtService.synchroVwtDataFK(companyId,deptlist, addUserList,updateUserList, deleteUserList, deleteDeptList);
				
			
			}else{
				map.put("corpId", corpId);
				map.put("date", synchroDate);
				List<VwtCorpHis> corphis = vwtService.getDeleteCropByParams(map);
				if(corphis!=null && corphis.size()>0){
					MultipleDataSource.setDataSourceKey(dbName);
					vwtService.deleteByCorpId(companyId);
					MultipleDataSource.setDataSourceKey("");
					vwtService.deleteTotalUserByCorpId(companyId);		
				}
			}
		}
	}

}
