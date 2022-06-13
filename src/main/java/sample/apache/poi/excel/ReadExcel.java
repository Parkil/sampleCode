package sample.apache.poi.excel;

import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
    public static void main(String[] args) throws Exception{
        FileInputStream fis = new FileInputStream("d:/student_input.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIter = sheet.iterator();

        StringBuilder sb = new StringBuilder();
        Row tempRow;
        Cell tempCell;
        String tmp = null;
        while(rowIter.hasNext()) {
            tempRow = rowIter.next();

            for(int i = 0 ; i<7 ; i++) {
                tempCell = tempRow.getCell(i, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);

                if(tempCell == null) {
                    sb.append(" -");
                }else {
                    switch(tempCell.getCellType()) {
                        case BLANK:
                        case STRING:
                            tmp = tempCell.getStringCellValue();
                            break;
                        case BOOLEAN:
                            tmp = String.valueOf(tempCell.getBooleanCellValue());
                            break;
                        case ERROR:
                            tmp = String.valueOf(tempCell.getErrorCellValue());
                            break;
                        case NUMERIC:
                            tmp = String.valueOf((int)tempCell.getNumericCellValue());
                            break;
                    }
                    sb.append(tmp+"-");
                }
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }
}

