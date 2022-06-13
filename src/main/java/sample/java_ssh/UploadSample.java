package sample.java_ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class UploadSample {
    private static final Logger logger = LoggerFactory.getLogger(UploadSample.class);

    public static void main(String[] args) throws Exception {
        String userName = "digicotf";
        String host = "aitag.iptime.org";
        int port = 10022;
        String pw = "New1234!";

        Session session = JSchUtil.getJSchSession(host, port, userName, pw);
        Channel channel = getSchChannel(session, "/home/digicotf/test.txt");

        OutputStream out=channel.getOutputStream();
        InputStream in=channel.getInputStream();

        channel.connect();

        String uploadFilePath = "d:/test.txt";
        sendTimeCommand(uploadFilePath, out);

        if(JSchUtil.checkAck(in) != 0){
            System.exit(0);
        }

        sendFileNameCommand(uploadFilePath, out);

        if(JSchUtil.checkAck(in)!=0){
            System.exit(0);
        }

        sendFile(uploadFilePath, out);

        JSchUtil.sendNull(out);

        out.close();

        channel.disconnect();
        session.disconnect();

        System.exit(0);
    }


    private static Channel getSchChannel(Session session, String targetUri) throws JSchException {
        // exec 'scp -t rfile' remotely
        boolean ptimestamp = true;
        String command = String.format("scp %s -t %s", (ptimestamp ? "-p" :""), targetUri);
        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        return channel;
    }

    private static void sendTimeCommand(String uploadFilePath, OutputStream out) throws IOException {
        File file = new File(uploadFilePath);

        String timeCommand = "T"+(file.lastModified()/1000)+" 0";
        timeCommand += (" "+(file.lastModified()/1000)+" 0\n");

        out.write(timeCommand.getBytes());
        out.flush();
    }

    private static void sendFileNameCommand(String uploadFilePath, OutputStream out) throws IOException {
        File file = new File(uploadFilePath);
        long filesize = file.length();
        String fileNameCommand="C0644 "+filesize+" ";

        if(uploadFilePath.lastIndexOf('/')>0){
            fileNameCommand+=uploadFilePath.substring(uploadFilePath.lastIndexOf('/')+1);
        }else{
            fileNameCommand+=uploadFilePath;
        }
        fileNameCommand+="\n";

        out.write(fileNameCommand.getBytes());
        out.flush();
    }

    private static void sendFile(String uploadFilePath, OutputStream out) {
        byte[] byteBuffer = new byte[1024];

        try(FileInputStream fis=new FileInputStream(uploadFilePath)) {
            while(true){
                int len=fis.read(byteBuffer, 0, byteBuffer.length);
                if(len<=0) break;
                out.write(byteBuffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
