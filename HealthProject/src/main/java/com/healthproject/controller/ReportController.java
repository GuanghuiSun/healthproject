package com.healthproject.controller;

import com.healthproject.Base.BaseResponse;
import com.healthproject.Base.ResultUtils;
import com.healthproject.service.MemberService;
import com.healthproject.service.ReportService;
import com.healthproject.service.SetMealService;
import com.mchange.v1.db.sql.ResultSetUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private MemberService memberService;

    @Resource
    private SetMealService setMealService;

    @Resource
    private ReportService reportService;

    /**
     * 查询会员变化图标数据
     * @return
     */
    @GetMapping("/member")
    public BaseResponse<Map<String,Object>> getMemberReport(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);

        List<String> list = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            calendar.add(Calendar.MONTH,1);
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("months",list);
        List<Integer> memberCount = memberService.findMemberCountByMonths(list);
        map.put("memberCount",memberCount);
        return ResultUtils.success(map);
    }

    /**
     * 套餐预约图表数据
     * @return
     */
    @GetMapping("/setMeal")
    public BaseResponse<Map<String,Object>> getSetMealReport(){
        Map<String,Object> map = new HashMap<>();
        List<Map<String, Object>> count = setMealService.findCount();
        map.put("setMealCount",count);
        List<String> allNames = setMealService.findAllNames();
        map.put("setMealNames",allNames);
        return ResultUtils.success(map);
    }

    /**
     * 查询所有信息
     * @return
     */
    @GetMapping("/data")
    public BaseResponse<Map<String,Object>> getBusinessData(){
        return ResultUtils.success(reportService.getBusinessData());
    }

    /**
     * 导出Excel表格
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportExcel")
    public BaseResponse<Boolean> exportBusinessReportToExcel(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取表格中的所有信息
            Map<String, Object> result = reportService.getBusinessData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map<String, Object>> hotSetMeal = (List<Map<String, Object>>) result.get("hotSetMeal");

            //获取模板文件位置(以绝对路径获取本地文件模板)
            String templateRealPath = "D:\\Java\\SpringBootDemo\\HealthProject\\src\\main\\resources\\static\\template\\report_template.xlsx";
            System.out.println(templateRealPath);

            //打开并编辑模板
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            XSSFSheet sheetAt = workbook.getSheetAt(0);

            //设置日期
            XSSFRow row = sheetAt.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            //设置会员数据
            row = sheetAt.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);
            row = sheetAt.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);

            //设置预约到诊数据
            row = sheetAt.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);
            row = sheetAt.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);
            row = sheetAt.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            //设置热门套餐数据
            int rowNum = 12;
            for(Map<String,Object> data : hotSetMeal){
                String name = (String)data.get("name");
                Long count = (Long)data.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) data.get("proportion");
                row = sheetAt.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(count);
                row.getCell(6).setCellValue(proportion.doubleValue());
            }

            //通过输出流将文件下载下来
            ServletOutputStream out = response.getOutputStream();
                //设置响应数据类型
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        }catch(IOException e){
            e.printStackTrace();
            return ResultUtils.error(false);
        }

    }

    /**
     * 导出PDF
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportPDF")
    public BaseResponse<Boolean> exportBusinessReportToPDF(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String, Object> result = reportService.getBusinessData();

            //取出返回结果数据，准备将报表数据写入到PDF文件中
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>>) result.get("hotSetMeal");

            //动态获取模板文件绝对磁盘路径
            String jrxmlPath = "D:\\Java\\SpringBootDemo\\HealthProject\\src\\main\\resources\\static\\template\\health_business3.jrxml";
            String jasperPath = "D:\\Java\\SpringBootDemo\\HealthProject\\src\\main\\resources\\static\\template\\health_business3.jasper";
            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,result, new JRBeanCollectionDataSource(hotSetmeal));

            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,out);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.success(false);
        }
    }
}
