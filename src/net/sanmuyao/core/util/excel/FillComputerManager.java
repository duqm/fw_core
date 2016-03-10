package net.sanmuyao.core.util.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import net.sanmuyao.core.util.ConverterUtil;


public class FillComputerManager {
	
	/**
	 * 合格率统计表格数据
	 * @author lvjie
	 * @param worksheet
	 * @param startRowIndex
	 * @param startColIndex
	 * @param datasource
	 */
	public static void fillReport_Qualified(HSSFSheet worksheet, int startRowIndex,  
            int startColIndex, List<Map<String,Object>> datasource) {  
  
        startRowIndex += 2;  
  
        // 列样式  
        HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();        
        bodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);  
        bodyCellStyle.setWrapText(false); //是否自动换行.  
  
        // 列数据
        for (int i=0; i< datasource.size(); i++) {  
            // Create a new row  
            HSSFRow row = worksheet.createRow(i+startRowIndex);  
  
            HSSFCell cell1 = row.createCell(startColIndex+0);  
            cell1.setCellValue(ConverterUtil.toString(datasource.get(i).get("PERS_NAME")));
            cell1.setCellStyle(bodyCellStyle);  
  
            HSSFCell cell2 = row.createCell(startColIndex+1);  
            cell2.setCellValue(ConverterUtil.toString(datasource.get(i).get("GET_ARCHIVES_PERSON")));
            cell2.setCellStyle(bodyCellStyle);  
  
            HSSFCell cell3 = row.createCell(startColIndex+2);  
            cell3.setCellValue(ConverterUtil.toString(datasource.get(i).get("WP")));
            cell3.setCellStyle(bodyCellStyle);  
  
            HSSFCell cell4 = row.createCell(startColIndex+3);  
            cell4.setCellValue(ConverterUtil.toString(datasource.get(i).get("HG")));
            cell4.setCellStyle(bodyCellStyle);  
  
            HSSFCell cell5 = row.createCell(startColIndex+4);  
            cell5.setCellValue(ConverterUtil.toString(datasource.get(i).get("JBHG")));
            cell5.setCellStyle(bodyCellStyle);  
          
            HSSFCell cell6 = row.createCell(startColIndex+5);  
            cell6.setCellValue(ConverterUtil.toString(datasource.get(i).get("BHG")));  
            cell6.setCellStyle(bodyCellStyle);  
            
            HSSFCell cell7 = row.createCell(startColIndex+6);  
            cell7.setCellValue(ConverterUtil.toString(datasource.get(i).get("TY")));  
            cell7.setCellStyle(bodyCellStyle);  
            
            HSSFCell cell8 = row.createCell(startColIndex+7);  
            cell8.setCellValue(ConverterUtil.toString(datasource.get(i).get("YX")));  
            cell8.setCellStyle(bodyCellStyle);  
            
            HSSFCell cell9 = row.createCell(startColIndex+8);  
            cell9.setCellValue(ConverterUtil.toString(datasource.get(i).get("NUM")));  
            cell9.setCellStyle(bodyCellStyle);  
        }  
    }
	
	/**
	 * 问题统计报表数据
	 * @author lvjie
	 * @param worksheet
	 * @param startRowIndex
	 * @param startColIndex
	 * @param datasource
	 */
	public static void fillReport_Ques(HSSFSheet worksheet, int startRowIndex,  
            int startColIndex, List<List<Object>> quesDataList) {  
  
        startRowIndex += 3;  
  
        // 列样式
        HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();        
        bodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);  
        bodyCellStyle.setWrapText(false); //是否自动换行.  
  
        // 列数据  
        for (int i= 0; i< quesDataList.size(); i++) {  
            // 创建行  
            HSSFRow row = worksheet.createRow(i+startRowIndex);  
            
            List<Object> list = quesDataList.get(i);
            int n = 0;
            for(Object O : list){
            	 HSSFCell cell1 = row.createCell(startColIndex+n);  
                 cell1.setCellValue(ConverterUtil.toString(O));
                 cell1.setCellStyle(bodyCellStyle);  
                 n++;
            }
        }  
    } 
}
