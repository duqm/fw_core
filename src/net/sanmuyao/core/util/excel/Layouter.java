package net.sanmuyao.core.util.excel;


import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import net.sanmuyao.core.util.ConverterUtil;



public class Layouter {
	

	/**
	 * 创建合格率报表 标题和表头
	 * @author lvjie
	 * @param worksheet
	 * @param startRowIndex 开始行
	 * @param startColIndex 开始列
	 * @param headSize 表头列数
	 */
    public static void buildReport_Qualified(HSSFSheet worksheet, int startRowIndex,  
            int startColIndex,int headSize) {  
    	String title = "合格率统计表";
        // 设置列的宽度  
    	for(int i = 0 ;i < headSize; i++){
    		 worksheet.setColumnWidth(i, 5000);  
    	}
        buildTitle(worksheet, startRowIndex, startColIndex,headSize,title);  
        buildHeaders_Qualified(worksheet,startRowIndex, startColIndex);  
  
    }  
  
    /**
     * 创建表格报表标题 
     * @author lvjie
     * @param worksheet
     * @param startRowIndex
     * @param startColIndex
     * @param headSize
     * @param title 
     */
    private static void buildTitle(HSSFSheet worksheet, int startRowIndex,  
            int startColIndex,int headSize,String title) {  
        // 设置报表标题字体  
        Font fontTitle = worksheet.getWorkbook().createFont();  
        fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);  
        fontTitle.setFontHeight((short) 280);  
  
        // 标题单元格样式  
        HSSFCellStyle cellStyleTitle = worksheet.getWorkbook().createCellStyle();  
        cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);  
        cellStyleTitle.setWrapText(true);  
        cellStyleTitle.setFont((HSSFFont) fontTitle);  
  
        // 报表标题  
        HSSFRow rowTitle = worksheet.createRow((short) startRowIndex);  
        rowTitle.setHeight((short) 500);  
        HSSFCell cellTitle = rowTitle.createCell(startColIndex);  
        cellTitle.setCellValue(title);  
        cellTitle.setCellStyle(cellStyleTitle);  
  
        // 合并区域内的报表标题  
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headSize));  
  
    }  
  
    /**
     * 创建合格率报表表头  
     * @author lvjie
     * @param worksheet
     * @param startRowIndex
     * @param startColIndex
     */
    private static void buildHeaders_Qualified(HSSFSheet worksheet, int startRowIndex,  
            int startColIndex) {  
        // Header字体  
       Font font = worksheet.getWorkbook().createFont();
    	
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);  
  
        // 单元格样式  
        HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();  
        headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);  
        //headerCellStyle.setFillPattern(CellStyle.FINE_DOTS);  
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);  
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
        headerCellStyle.setWrapText(true);  
        headerCellStyle.setFont((HSSFFont) font);  
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);  
  
        // 创建字段标题  
        HSSFRow rowHeader = worksheet.createRow((short) startRowIndex + 1);  
        rowHeader.setHeight((short) 500);  
  
        HSSFCell cell1 = rowHeader.createCell(startColIndex + 0);  
        cell1.setCellValue("公证员");  
        cell1.setCellStyle(headerCellStyle);  
  
        HSSFCell cell2 = rowHeader.createCell(startColIndex + 1);  
        cell2.setCellValue("质检人");  
        cell2.setCellStyle(headerCellStyle);  
  
        HSSFCell cell3 = rowHeader.createCell(startColIndex + 2);  
        cell3.setCellValue("未评数");  
        cell3.setCellStyle(headerCellStyle);  
  
        HSSFCell cell4 = rowHeader.createCell(startColIndex + 3);  
        cell4.setCellValue("合格数");  
        cell4.setCellStyle(headerCellStyle);  
  
        HSSFCell cell5 = rowHeader.createCell(startColIndex + 4);  
        cell5.setCellValue("基本合格数");  
        cell5.setCellStyle(headerCellStyle);  
  
        HSSFCell cell6 = rowHeader.createCell(startColIndex + 5);  
        cell6.setCellValue("不合格数");  
        cell6.setCellStyle(headerCellStyle);  
        
        HSSFCell cell7 = rowHeader.createCell(startColIndex + 6);  
        cell7.setCellValue("推优数");  
        cell7.setCellStyle(headerCellStyle);  
        
        HSSFCell cell8 = rowHeader.createCell(startColIndex + 7);  
        cell8.setCellValue("优秀数");  
        cell8.setCellStyle(headerCellStyle);  
        
        HSSFCell cell9 = rowHeader.createCell(startColIndex + 8);  
        cell9.setCellValue("总数");  
        cell9.setCellStyle(headerCellStyle);  
    }  
    
    /**
	 * 创建问题统计报表 标题和表头
	 * @author lvjie
	 * @param worksheet
	 * @param startRowIndex 开始行
	 * @param startColIndex 开始列
	 * @param resultQuesList 表头列数据
	 */
    public static void buildReport_Ques(HSSFSheet worksheet, int startRowIndex,  
            int startColIndex,List<Map<String,Object>> resultQuesList) {  
    	String title = "问题统计";
    	int headSize = 1;
    	for(Map<String,Object> quesMap : resultQuesList){
    		List<Map<String,Object>> smallQues = (List<Map<String,Object>>)quesMap.get("child");
    		int size = 1;
    		if(smallQues.size() != 0){
    			size = smallQues.size();
    		}
    		headSize += size;
    	}
        // 设置列的宽度  
    	for(int i = 0 ;i < headSize; i++){
    		 worksheet.setColumnWidth(i, 5000);  
    	}
        buildTitle(worksheet, startRowIndex, startColIndex,headSize,title);  
        buildHeaders_Ques(worksheet,startRowIndex, startColIndex,resultQuesList);  
  
    }
    
    /**
     * 创建问题统计报表表头  
     * @author lvjie
     * @param worksheet
     * @param startRowIndex
     * @param startColIndex
     */
    private static void buildHeaders_Ques(HSSFSheet worksheet, int startRowIndex, int startColIndex,List<Map<String,Object>> resultQuesList) {  
        // Header字体  
        Font font = worksheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);  
        // 单元格样式  
        HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();  
        headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);  
        //headerCellStyle.setFillPattern(CellStyle.FINE_DOTS);  
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);  
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
        headerCellStyle.setWrapText(true);  
        headerCellStyle.setFont((HSSFFont) font);  
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);  
  
        // 创建问题大类字段标题  
        HSSFRow rowHeader = worksheet.createRow((short) startRowIndex + 1);  
        rowHeader.setHeight((short) 500);  
  
        HSSFCell cell1 = rowHeader.createCell(startColIndex + 0);  
        cell1.setCellValue("问题");  
        cell1.setCellStyle(headerCellStyle);  
        
        int col = 1; //列数
        int j = 1;
        for(Map<String,Object> quesMap : resultQuesList){
        	
    		List<Map<String,Object>> smallQues = (List<Map<String,Object>>)quesMap.get("child");
    		String DD_VALUE = ConverterUtil.toString(quesMap.get("DD_VALUE"));//问题大类VALUE
    		for(Map<String,Object> smallMap : smallQues){
    			HSSFCell cell2 = rowHeader.createCell(startColIndex + j);
    			j++;
    			cell2.setCellValue(DD_VALUE);  
        		cell2.setCellStyle(headerCellStyle);  
    		}
    		if(smallQues.size() == 0){
    			HSSFCell cell2 = rowHeader.createCell(startColIndex + j);
    			j++;
    			cell2.setCellValue(DD_VALUE);  
        		cell2.setCellStyle(headerCellStyle);  
    		}
    		// 合并区域内的报表标题     四个参数分别是：起始行，结束行,起始列，结束列   
    		if(smallQues.size() != 0){
    			worksheet.addMergedRegion(new CellRangeAddress(1,1,col, col+smallQues.size()-1));  
    			col += smallQues.size();
    		}else {
    			col +=1;
    		}
    	}
        HSSFCell cell3 = rowHeader.createCell(startColIndex + col);  
        cell3.setCellValue("合计");  
        cell3.setCellStyle(headerCellStyle);  
        
        // 创建问题小类字段标题  
        HSSFRow rowSmallHeader = worksheet.createRow((short) startRowIndex + 2);  
        rowSmallHeader.setHeight((short) 500);  
        HSSFCell small_cell1 = rowSmallHeader.createCell(startColIndex + 0);  
        small_cell1.setCellValue("公证员");  
        small_cell1.setCellStyle(headerCellStyle); 
        
        int k = 1;
        for(Map<String,Object> quesMap : resultQuesList){
        	List<Map<String,Object>> smallQues = (List<Map<String,Object>>)quesMap.get("child");
        	for(Map<String,Object> smallMap : smallQues){
        		String DD_VALUE = ConverterUtil.toString(smallMap.get("DD_VALUE"));//问题大类VALUE 
        		HSSFCell small_cell2 = rowSmallHeader.createCell(startColIndex + k);
    			k++;
    			small_cell2.setCellValue(DD_VALUE);  
    			small_cell2.setCellStyle(headerCellStyle);  
        	}
        	if(smallQues.size() == 0){
    			HSSFCell small_cell2 = rowSmallHeader.createCell(startColIndex + k);
    			k++;
    			small_cell2.setCellValue("");  
    			small_cell2.setCellStyle(headerCellStyle);  
    		}
        }
        HSSFCell small_cell3 = rowSmallHeader.createCell(startColIndex + col);  
        small_cell3.setCellValue("");  
        small_cell3.setCellStyle(headerCellStyle);  
  
    } 
}
