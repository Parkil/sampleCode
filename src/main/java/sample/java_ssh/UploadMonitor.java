package sample.java_ssh;

import com.jcraft.jsch.SftpProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadMonitor implements SftpProgressMonitor {
    private long uploadedSize = 0L;
    private long fileSize = 0L;

    private static final Logger logger = LoggerFactory.getLogger(UploadMonitor.class);

    /*
     * 업로드 진행상황 체크
     * true를 반환하면 아직 다운로드/업로드가 진행중이고 false를 반환하면 다운로드/업로드가 종료되었다는 의미
     *
     */
    @Override
    public boolean count(long uploadSize) {
        if(uploadedSize >= fileSize) {
            return false;
        }else {
            uploadedSize += uploadSize;
            return true;
        }
    }

    /*
     * 다운로드/업로드가 완료되면 호출
     */
    @Override
    public void end() {
        logger.info("end called");
    }

    /*
     * 최초 다운로드/업로드시 실행
     * index : 업로드/다운로드 순서
     * srcPath : 업로드/다운로드 원 url
     * dstPath : 업로드/다운로드 목적지 url
     * fileSize : 업로드할 파일 사이즈
     */
    @Override
    public void init(int index, String srcPath, String dstPath, long fileSize) {
        this.fileSize = fileSize;
    }
}
