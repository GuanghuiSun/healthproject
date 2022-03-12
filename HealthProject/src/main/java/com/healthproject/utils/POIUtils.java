package com.healthproject.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.*;


public class POIUtils {

    /**
     * 读取Excel文件
     * @param excelFile
     * @return
     * @throws IOException
     */
    public static List<String[]> readExcel(MultipartFile excelFile) throws IOException {
        checkFile(excelFile);
        Workbook workbook = getWorkbook(excelFile);
        List<String[]> list = new ArrayList<>();
        if(workbook!=null){
            for(int i = 0; i < workbook.getNumberOfSheets(); i++){
                //获得当前工作表
                Sheet sheet = workbook.getSheetAt(i);
                if(sheet==null){
                    continue;
                }
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                for(int j = firstRowNum + 1; j <= lastRowNum; j++){
                    Row row = sheet.getRow(j);
                    if(row==null){
                        continue;
                    }
                    short firstCellNum = row.getFirstCellNum();
                    short lastCellNum = row.getLastCellNum();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
//                    循环当前行
                    for(short k = firstCellNum; k < lastCellNum; k++){
                        Cell cell = row.getCell(k);
                        cells[k] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
        }
        return list;
    }

    /**
     * 检查文件是否合法
     * @param file
     * @throws IOException
     */
    public static void checkFile(MultipartFile file) throws IOException{
        if(file == null){
            throw new FileNotFoundException("文件不存在!");
        }
        String fileName = file.getOriginalFilename();
        if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
            throw new IOException("文件类型有误！");
        }
    }

    /**
     * 根据版本获取Workbook对象
     * @param excelFile
     * @return
     */
    public static Workbook getWorkbook(MultipartFile excelFile){
        String filename = excelFile.getOriginalFilename();
        Workbook workbook = null;
        try{
            InputStream inputStream = excelFile.getInputStream();
            if(filename.endsWith("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }else if(filename.endsWith("xlsx")){
                workbook = new XSSFWorkbook(inputStream);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 获取单个单元格地值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell==null){
            return cellValue;
        }
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if(dataFormatString.equals("m/d/yy")){
            cellValue = new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue());
            return cellValue;
        }
        if(cell.getCellType() == NUMERIC){
            cell.setCellType(STRING);
        }
        switch(cell.getCellType()){
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
