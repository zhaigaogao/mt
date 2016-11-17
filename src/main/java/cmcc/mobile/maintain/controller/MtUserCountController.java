package cmcc.mobile.maintain.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cmcc.mobile.maintain.base.BaseController;
import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.dao.CustomerMapper;
import cmcc.mobile.maintain.dao.TotalUserMapper;
import cmcc.mobile.maintain.entity.Customer;
import cmcc.mobile.maintain.server.db.MultipleDataSource;
import cmcc.mobile.maintain.service.DeptService;
import cmcc.mobile.maintain.service.GroupService;
import cmcc.mobile.maintain.util.DBUtil;
import cmcc.mobile.maintain.vo.CustomerVo;


/**
 * 开户企业活跃度统计
 * @author wubj
 *
 */
@RequestMapping("/userCount")
@Controller
public class MtUserCountController extends BaseController{
	
	@Autowired
	private GroupService groupService;

	@Autowired
	private DeptService deptService;
	

	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private TotalUserMapper totalUserMapper ;
	private Logger logger = Logger.getLogger(this.getClass());
	public static final short detail_size = 12;
	//企业登入数和发起工单，处理工单数统计(按时间段查询)
	@RequestMapping("/getUserCount")
	@ResponseBody
	public JsonResult getUserCount(Customer c, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		//从seesion获取地区ID
		Long deptId = getUser().getDeptId();
		//同个地区ID获取所有下级ID，组装成List
		List<Long> deptIds = deptService.getChildDeptIds(deptId);
		Map<String, Object> param = new HashMap<String, Object>();
		//将传入的参数和组装好的list塞入map中
		param.put("c", c);
		param.put("ids", deptIds);
		PageHelper.startPage(pageNum, pageSize);
		//查询到集团信息
		List<Customer> customers = customerMapper.findByLike(param);
		PageInfo pageinfo = new PageInfo(customers);
		List<CustomerVo> customerVos = new ArrayList<CustomerVo>();
		//遍历集团信息
		for (Customer c1 : customers) {
			CustomerVo cvo = new CustomerVo();
			BeanUtils.copyProperties(c1, cvo);
			//获取集团地址
			cvo.setCompanyAddress(deptService.getDeptAllName(cvo.getDeptId()));
			cvo.setStartDate(c.getStartDate());
			cvo.setEndDate(c.getEndDate());
			//获取集团员工数
			int count = totalUserMapper.selectByCompanyCount(cvo.getId()) ;
			//获取集团员登入数
			int loginNum = totalUserMapper.selectByLoginCount(cvo) ;
			MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());//分库
			//获取发起工单数
			int startNum = totalUserMapper.selectByStartCount(cvo) ;
			//获取处理工单数
			int dealNum = totalUserMapper.selectByDealCount(cvo) ;
			MultipleDataSource.setDataSourceKey("");//分库
			//将获取到的工单数和登入数封装
			cvo.setCompanyNum(String.valueOf(count)) ;
			cvo.setLoginNum(String.valueOf(loginNum)) ;
			cvo.setStartNum(String.valueOf(startNum)) ;
			cvo.setDealNum(String.valueOf(dealNum)) ;
			//将对象添加到集合中
			customerVos.add(cvo);
		}
		//将数据进行分页
		pageinfo.setList(customerVos);
		
		return new JsonResult(true,"操作成功！",pageinfo) ;
	}
	
	/**
	 * 导出运营统计数据
	 * 获取报表数据源
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportUserCount")
	@ResponseBody
	public void exportUserCount(Customer c,HttpServletRequest request,HttpServletResponse response){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFCellStyle styleleft = wb.createCellStyle();styleleft.setWrapText(true);
		styleleft.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居左格式
		
		HSSFCellStyle style = wb.createCellStyle();style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
		HSSFFont dfont = wb.createFont();
		dfont.setFontHeightInPoints((short) 12);//设置字体大小
		
		style.setFont(dfont);
		
		HSSFCellStyle etitle = wb.createCellStyle();etitle.setWrapText(true);
		etitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个标题格式
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints(detail_size);//设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		etitle.setFont(font);

		HSSFCellStyle ptt = wb.createCellStyle();ptt.setWrapText(true);
		ptt.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个标题格式
		HSSFFont pfont = wb.createFont();
		pfont.setFontHeightInPoints(detail_size);//设置字体大小
		pfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		ptt.setFont(pfont);
		int l=-1;
		int i=-1;
		i=-1;
		//创建excle抬头
		HSSFRow row = sheet.createRow(++l);
		HSSFCell cell = row.createCell(++i);
		
			cell.setCellValue("企业所在地");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			cell.setCellValue("企业名称");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			cell.setCellValue("管理员帐号");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			cell.setCellValue("管理员姓名");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			cell.setCellValue("注册时间");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			cell.setCellValue("登入数");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			cell.setCellValue("发起工单数");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			cell.setCellValue("处理工单数");
			cell.setCellStyle(style);
			cell = row.createCell(++i);
			
			//获取数据源
			Long deptId = getUser().getDeptId();
			List<Long> deptIds = deptService.getChildDeptIds(deptId);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("c", c);
			param.put("ids", deptIds);
			List<Customer> customers = customerMapper.findByLike(param);
			List<CustomerVo> customerVos = new ArrayList<CustomerVo>();
			for (Customer c1 : customers) {
				CustomerVo cvo = new CustomerVo();
				BeanUtils.copyProperties(c1, cvo);
				cvo.setCompanyAddress(deptService.getDeptAllName(cvo.getDeptId()));
				cvo.setStartDate(c.getStartDate());
				cvo.setEndDate(c.getEndDate());
				int count = totalUserMapper.selectByCompanyCount(cvo.getId()) ;
				int loginNum = totalUserMapper.selectByLoginCount(cvo) ;
				MultipleDataSource.setDataSourceKey(DBUtil.getInstance().getDbName());//分库
				int startNum = totalUserMapper.selectByStartCount(cvo) ;
				int dealNum = totalUserMapper.selectByDealCount(cvo) ;
				MultipleDataSource.setDataSourceKey("");//分库
				cvo.setCompanyNum(String.valueOf(count)) ;
				cvo.setLoginNum(String.valueOf(loginNum)) ;
				cvo.setStartNum(String.valueOf(startNum)) ;
				cvo.setDealNum(String.valueOf(dealNum)) ;
				customerVos.add(cvo);
			}
			//将数据和抬头一一对应
	    Map<String,Object> vmap = new HashMap<String,Object>();
	    Map<String,Integer> imap = new HashMap<String,Integer>();
		for (CustomerVo map : customerVos) {
			i=-1;
			row = sheet.createRow(++l);
				cell = row.createCell(++i);
				cell.setCellValue(map.getCompanyAddress());
				cell.setCellStyle(style);
	       		
				cell = row.createCell(++i);
				cell.setCellValue(map.getCustomerName());
				cell.setCellStyle(style);
	       		
				cell = row.createCell(++i);
				cell.setCellValue(map.getContactUserName());
				cell.setCellStyle(style);
	       		
				cell = row.createCell(++i);
				cell.setCellValue(map.getContactName());
				cell.setCellStyle(style);
	       		
				cell = row.createCell(++i);
				cell.setCellValue(sdf.format(map.getRegisterDate()));
				cell.setCellStyle(style);
	       		
				cell = row.createCell(++i);
				cell.setCellValue(map.getLoginNum());
				cell.setCellStyle(style);
	       		
	       			
				cell = row.createCell(++i);
				cell.setCellValue(map.getStartNum());
				cell.setCellStyle(style);
				
	       		cell = row.createCell(++i);
	       		cell.setCellValue(map.getDealNum());
				cell.setCellStyle(style);
	       			    		
		}
		row = sheet.createRow(++l);
	    Set<String> ks = vmap.keySet();
	    for (String k : ks) {
			cell = row.createCell(imap.get(k));
			cell.setCellValue(vmap.get(k)+"");
			cell.setCellStyle(style);
		}
		//列数
		i=-1;
	    sheet.setColumnWidth(++i, 3500);
	    sheet.setColumnWidth(++i, 3500);
	    sheet.setColumnWidth(++i, 3500);
	    sheet.setColumnWidth(++i, 3500);
	    sheet.setColumnWidth(++i, 3500);
	    sheet.setColumnWidth(++i, 3500);
	    sheet.setColumnWidth(++i, 3500);
	    sheet.setColumnWidth(++i, 3500);


		try{
			//文件名称
			String filename = "运营统计记录.xls";
			response.setContentType("application/vnd.ms-excel");
			//中文编码
	        response.setHeader("Content-disposition", "attachment;charset=UTF-8;filename=" + URLEncoder.encode(filename, "utf-8"));   
	        OutputStream ouputStream = response.getOutputStream();     
	        wb.write(ouputStream);     
	        ouputStream.flush();     
	        ouputStream.close();     
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	}
	

