package sample.java_ssh;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.jcraft.jsch.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/*
 * jsch 라이브러리를 이용하여 sftp download 를 수행하는 예제
 * 해당예제는 exec 모드를 이용하여 scp 커맨드를 실행하는 방식으로  다운로드를 수행
 */
public class DownloadSample {
    private static final Logger logger = LoggerFactory.getLogger(DownloadSample.class);

    public static void main(String[] arg) {
        String srcUri = "/home/digicotf/AI_TGG_Content_2021092701.xml";
        String destUri = "d:/AI_TGG_Content_2021092701.xml";

        String userName = "digicotf";
        String host = "aitag.iptime.org";
        int port = 10022;
        String pw = "New1234!";

        try {
            Session session = JSchUtil.getJSchSession(host, port, userName, pw);
            Channel channel = getJSchChannel(srcUri, session);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] buf = new byte[1024];
            
            // -- chunk 3
            // send '\0'
            JSchUtil.sendNull(out);
            // -- chunk3 end

            while (true) {
                int c = JSchUtil.checkAck(in);
                if (c != 'C') {
                    break;
                }

                // read '0644 ' 48,54,52,52,32
                in.read(buf, 0, 5);

                long filesize = getFilesize(in);
                logger.info("fileSize : {}", filesize);

                String fileName = getFileName(in);
                logger.info("fileName : {}", fileName);

                JSchUtil.sendNull(out);

                saveFile(in, filesize, destUri);

                if (JSchUtil.checkAck(in) != 0) {
                    System.exit(0);
                }

                // send '\0'
                JSchUtil.sendNull(out);
            }

            session.disconnect();

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void saveFile(InputStream in, long fileSize, String destUri) {
        byte[] fileReadBuffer = new byte[1024];

        try(FileOutputStream out = new FileOutputStream(destUri)) {
            while (fileSize != 0L) {
                int readLength = (fileReadBuffer.length < fileSize) ? fileReadBuffer.length : (int)fileSize;

                int readSize = in.read(fileReadBuffer, 0, readLength);
                if (readSize < 0) {
                    break;
                }

                out.write(fileReadBuffer, 0, readSize);
                fileSize -= readSize;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileName(InputStream in) throws IOException {
        List<Byte> readByteList = new ArrayList<>();

        int readByte;

        while((readByte = in.read()) != (byte)0x0a) {
            if(readByte < 0) {
                break;
            }

            readByteList.add((byte)readByte);
        }

        byte[] fileNameByteArr = new byte[readByteList.size()];

        AtomicInteger arrIdx = new AtomicInteger(0);
        readByteList.forEach(val -> fileNameByteArr[arrIdx.getAndIncrement()] = val);

        return new String(fileNameByteArr);
    }

    private static long getFilesize(InputStream in) throws IOException {
        int readByte;
        long fileSize = 0L;

        while((readByte = in.read()) != ' ') {
            if(readByte < 0) {
                break;
            }

            fileSize = (fileSize * 10L) + (readByte - '0');
        }

        return fileSize;
    }



    private static Channel getJSchChannel(String srcUri, Session session) throws JSchException {
        String command = "scp -f " + srcUri;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        return channel;
    }
}