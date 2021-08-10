package apache.poi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.security.GeneralSecurityException;


/*
    apache poi 를 이용하여 excel 파일에 암호를 설정하는 예제

    [주의점]
    2021-08-20 현재까지는 streaming 도중 암호화를 할수 있는 방법을 아직 찾지 못하였음.
    아래 예제는 파일을 완전히 생성한 다음 암호화를 수행하는 예제 이므로 streaming(SXSSF) 상황에서 사용하지 말것
 */
public class SXSSFSetPasswordSample {

    static File createExcelFile() {
        XSSFWorkbook wb = null;
        OutputStream fileOut = null;
        File file = new File("c:/webdown/workbook1.xlsx");
        try {
            file.createNewFile();
            fileOut = new FileOutputStream(file);
            wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Venu");
            wb.write(fileOut);
            wb.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(wb != null) wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    static void setPassword(File file, String pwd) {
        try (POIFSFileSystem fs = new POIFSFileSystem()) {
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor enc = info.getEncryptor();
            enc.confirmPassword(pwd);

            try {
                OPCPackage opc = OPCPackage.open(file, PackageAccess.READ_WRITE);
                OutputStream os = enc.getDataStream(fs);
                opc.save(os);
                os.close();
            } catch (InvalidFormatException | IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fos = new FileOutputStream(file);
                fs.writeFilesystem(fos);
                fos.close();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    public static void main(String[] args) {
        File file = createExcelFile();
        setPassword(file, "aaabbb");
    }
}
