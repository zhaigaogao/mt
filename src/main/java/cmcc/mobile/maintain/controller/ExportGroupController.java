  package cmcc.mobile.maintain.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cmcc.mobile.maintain.base.BaseController;
import cmcc.mobile.maintain.base.JsonResult;
import cmcc.mobile.maintain.service.DeptService;
import cmcc.mobile.maintain.service.GroupService;

/**
 * 导出开户人员
 * @author wubj
 *
 */
@Controller
@RequestMapping("/export")
public class ExportGroupController extends BaseController{
	private Logger logger = Logger.getLogger(this.getClass());
	public static final short detail_size = 12;
	
	@Autowired
	private GroupService groupService ;
	@Autowired
	private DeptService deptService ;
	
	/**
	 * 获取报表数据源
	 * @param request
	 * @return
	 */
	@RequestMapping("/getReport")
	@ResponseBody
	public JsonResult getReport(HttpServletRequest request ,String sdate , String edate){
		
		
		
		return null ;
		
	}
	
	
	
//	//导出开户人员
//	@RequestMapping("/getGroup")
//	@ResponseBody
//	public JsonResult getGroup(HttpServletRequest request){
//		HSSFWorkbook wb = new HSSFWorkbook();
//		HSSFSheet sheet = wb.createSheet();
//		HSSFCellStyle styleleft = wb.createCellStyle();styleleft.setWrapText(true);
//		styleleft.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居左格式
//		
//		HSSFCellStyle style = wb.createCellStyle();style.setWrapText(true);
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
//		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
//		HSSFFont dfont = wb.createFont();
//		dfont.setFontHeightInPoints((short) 12);//设置字体大小
//		
//		style.setFont(dfont);
//		
//		HSSFCellStyle etitle = wb.createCellStyle();etitle.setWrapText(true);
//		etitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个标题格式
//		HSSFFont font = wb.createFont();
//		font.setFontHeightInPoints(detail_size);//设置字体大小
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
//		etitle.setFont(font);
//
//		HSSFCellStyle ptt = wb.createCellStyle();ptt.setWrapText(true);
//		ptt.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个标题格式
//		HSSFFont pfont = wb.createFont();
//		pfont.setFontHeightInPoints(detail_size);//设置字体大小
//		pfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
//		ptt.setFont(pfont);
//		int l=-1;
//		int i=-1;
//		i=-1;
//		HSSFRow row = sheet.createRow(++l);
//		HSSFCell cell = row.createCell(++i);
//			cell.setCellValue("企业所在地");
//			cell.setCellStyle(style);
//			cell = row.createCell(++i);
//			cell.setCellValue("企业名称");
//			cell.setCellStyle(style);
//			cell = row.createCell(++i);
//			cell.setCellValue("管理员帐号");
//			cell.setCellStyle(style);
//			cell = row.createCell(++i);
//			cell.setCellValue("管理员姓名");
//			cell.setCellStyle(style);
//			cell = row.createCell(++i);
//			cell.setCellValue("注册时间");
//			cell.setCellStyle(style);
//			cell = row.createCell(++i);
//			
//		Long deptId = getUser().getDeptId();
//		List<Long> deptIds = deptService.getChildDeptIds(deptId);
//		List<Map<String,Object>> rs = groupService.findByLike(c, ids, pageNum, pageSize) ;
//	    Map<String,Object> vmap = new HashMap<String,Object>();
//	    Map<String,Integer> imap = new HashMap<String,Integer>();
//		for (Map<String, Object> map : rs) {
//			i=-1;
//			row = sheet.createRow(++l);
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("text")+"");
//				cell.setCellStyle(style);
//	       		
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("sale_name")+"");
//				cell.setCellStyle(style);
//	       		
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("province")+"");
//				cell.setCellStyle(style);
//	       		
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("city")+"");
//				cell.setCellStyle(style);
//	       		
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("area_text")+"");
//				cell.setCellStyle(style);
//	       		
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("stock")+"");
//				cell.setCellStyle(style);
//	       		
//	       			
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("qqty")+"");
//				cell.setCellStyle(style);
//				
//	       		cell = row.createCell(++i);
//	       		cell.setCellValue(map.get("tel")+"");
//				cell.setCellStyle(style);
//	       			
//				cell = row.createCell(++i);
//				cell.setCellValue(map.get("wchat")+"");
//				cell.setCellStyle(style);
//				
//	
//				
//
//	       			
//
//					
//					
//
//	       		
//		}
//		row = sheet.createRow(++l);
//	    Set<String> ks = vmap.keySet();
//	    for (String k : ks) {
//			cell = row.createCell(imap.get(k));
//			cell.setCellValue(vmap.get(k)+"");
//			cell.setCellStyle(style);
//		}
//		
//		i=-1;
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//	    sheet.setColumnWidth(++i, 3500);
//		try{
//			String filename = Constant.PROJECT_NAME+"代理商列表"+"."+keys;
//			response.setContentType("application/vnd.ms-excel");
//	        response.setHeader("Content-disposition", "attachment;charset=UTF-8;filename=" + URLEncoder.encode(filename, "utf-8"));   
//	         OutputStream ouputStream = response.getOutputStream();     
//	        wb.write(ouputStream);     
//	        ouputStream.flush();     
//	        ouputStream.close();     
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//	}
	
}
