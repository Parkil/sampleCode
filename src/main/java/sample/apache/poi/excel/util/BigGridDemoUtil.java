package sample.apache.poi.excel.util;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class BigGridDemoUtil {

    public static final String PERCENT = "percent";
    private static final Random random = new Random();
    private static final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024*10);
    public static final String COEFF = "coeff";
    public static final String CURRENCY = "currency";
    public static final String DATE = "date";

    private BigGridDemoUtil(){}

    public static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){
        Map<String, XSSFCellStyle> styles = new HashMap<>();
        XSSFDataFormat fmt = wb.createDataFormat();

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setAlignment(HorizontalAlignment.RIGHT);
        style1.setDataFormat(fmt.getFormat("0.0%"));
        styles.put(PERCENT, style1);


        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setDataFormat(fmt.getFormat("0.0X"));
        styles.put(COEFF, style2);

        XSSFCellStyle style3 = wb.createCellStyle();
        style3.setAlignment(HorizontalAlignment.RIGHT);
        style3.setDataFormat(fmt.getFormat("$#,##0.00"));
        styles.put(CURRENCY, style3);

        XSSFCellStyle style4 = wb.createCellStyle();
        style4.setAlignment(HorizontalAlignment.RIGHT);
        style4.setDataFormat(fmt.getFormat("mmm dd"));
        styles.put(DATE, style4);

        XSSFCellStyle style5 = wb.createCellStyle();
        XSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);
        style5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        style5.setFont(headerFont);
        styles.put("header", style5);

        return styles;
    }

    public static void genRow(Writer out, Map<String, XSSFCellStyle> styles) throws IOException {

        Calendar calendar = Calendar.getInstance();

        SpreadsheetWriter sw = new SpreadsheetWriter(out);
        sw.beginSheet();

        //insert header row
        sw.insertRow(0);
        int styleIndex = styles.get("header").getIndex();
        sw.createCell(0, "컬럼1", styleIndex);
        sw.createCell(1, "컬럼2", styleIndex);
        sw.createCell(2, "컬럼3", styleIndex);
        sw.createCell(3, "컬럼4", styleIndex);
        sw.createCell(4, "컬럼5", styleIndex);
        sw.createCell(5, "컬럼6", styleIndex);
        sw.createCell(6, "컬럼7", styleIndex);
        sw.createCell(7, "컬럼8", styleIndex);
        sw.createCell(8, "컬럼9", styleIndex);
        sw.createCell(9, "컬럼10", styleIndex);

        sw.endRow();

        //write data rows
        for (int row = 1; row <= 100000; row++) {
            sw.insertRow(row);

            sw.createCell(0, "Hello, " + row + "!");
            sw.createCell(1, (double)random.nextInt(100)/100, styles.get(PERCENT).getIndex());
            sw.createCell(2, (double)random.nextInt(10)/10, styles.get(COEFF).getIndex());
            sw.createCell(3, random.nextInt(10000), styles.get(CURRENCY).getIndex());
            sw.createCell(4, calendar, styles.get(DATE).getIndex());
            sw.createCell(5, "Hello, " + row + "!");
            sw.createCell(6, (double)random.nextInt(100)/100, styles.get(PERCENT).getIndex());
            sw.createCell(7, (double)random.nextInt(10)/10, styles.get(COEFF).getIndex());
            sw.createCell(8, random.nextInt(10000), styles.get(CURRENCY).getIndex());
            sw.createCell(9, calendar, styles.get(DATE).getIndex());

            sw.endRow();

            calendar.roll(Calendar.DAY_OF_YEAR, 1);
        }
        sw.endSheet();
    }

    @SuppressWarnings("unchecked")
    public static void substitute(File zipFile, File tempFile, String entry, OutputStream out) {

        try(
            ZipFile zip = new ZipFile(zipFile);
            ZipOutputStream zos = new ZipOutputStream(out)
        ) {
            /*
             * java.util.zip의 내용으로는 전체내용을 복사하는 도중에 특정파일내용을 빼거나 수정하는 방법외에는
             * 압축파일내의 파일을 수정하거나 삭제할수 있는 방법이 존재하지 않음
             *
             * entry(실제 엑셀데이터가 들어있는 xml파일)을 제외한 나머지 파일의 내용을 복사한다.
             */
            Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
            while (en.hasMoreElements()) {
                ZipEntry ze = en.nextElement();
                if(!ze.getName().equals(entry)){
                    zos.putNextEntry(new ZipEntry(ze.getName()));
                    InputStream zipInputStream = zip.getInputStream(ze);
                    copyStream(zipInputStream, zos);
                    zipInputStream.close();
                }
            }

            //실제 엑셀이 들어있는 부분을 입력한다.
            zos.putNextEntry(new ZipEntry(entry));
            InputStream fileInputStream = new FileInputStream(tempFile);
            copyStream(fileInputStream, zos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyStream(InputStream in, OutputStream out){
        try(
            ReadableByteChannel rc = Channels.newChannel(in);
            WritableByteChannel wc = Channels.newChannel(out)
        ){
            byteBuffer.clear();
            while (rc.read(byteBuffer) >=0 ) {
                byteBuffer.flip();
                wc.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
