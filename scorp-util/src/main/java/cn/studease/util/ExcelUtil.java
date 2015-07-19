package cn.studease.util;

import java.text.DecimalFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel相关工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static String getCellValue(Object workbook, int rowIndex, int cellIndex) {
        try {
            if ((workbook instanceof HSSFWorkbook)) {
                HSSFCell cell = ((HSSFWorkbook) workbook).getSheetAt(0).getRow(rowIndex).getCell(cellIndex);
                String value;
                switch (cell.getCellType()) {
                    case 1:
                        String str = cell.getStringCellValue();
                        if ((str != null) && (str.length() > 0)) {
                        }
                        return str.trim();


                    case 0:
                        if (org.apache.poi.hssf.usermodel.HSSFDateUtil.isCellDateFormatted(cell)) {
                            return DateUtil.formatDate(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                        }
                        value = "" + cell.getNumericCellValue();
                        if ((value.contains(".")) && (value.contains("E"))) {
                            value = new DecimalFormat().parse(value).toString();
                        }
                        return value.endsWith(".0") ? value.substring(0, value.length() - 2) : value;
                    case 4:
                        return cell.getBooleanCellValue() ? "true" : "false";
                    case 2:
                        return String.valueOf(new org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator((HSSFWorkbook) workbook).evaluate(cell).getNumberValue());
                }


                return "";
            }
            if ((workbook instanceof XSSFWorkbook)) {
                XSSFCell cell = ((XSSFWorkbook) workbook).getSheetAt(0).getRow(rowIndex).getCell(cellIndex);
                switch (cell.getCellType()) {
                    case 1:
                        String str = cell.getStringCellValue();
                        if ((str != null) && (str.length() > 0)) {
                        }
                        return str.trim();


                    case 0:
                        if (org.apache.poi.hssf.usermodel.HSSFDateUtil.isCellDateFormatted(cell)) {
                            return DateUtil.formatDate(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                        }
                        String value = "" + cell.getNumericCellValue();
                        if ((value.contains(".")) && (value.contains("E"))) {
                            value = new DecimalFormat().parse(value).toString();
                        }
                        return value.endsWith(".0") ? value.substring(0, value.length() - 2) : value;
                    case 4:
                        return cell.getBooleanCellValue() ? "true" : "false";
                    case 2:
                        return String.valueOf(new org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator((XSSFWorkbook) workbook).evaluate(cell).getNumberValue());
                }


                return "";
            }

            return "";
        } catch (Exception e) {
            log.trace("获取单元格数据失败", e);
        }
        return "";
    }

}
