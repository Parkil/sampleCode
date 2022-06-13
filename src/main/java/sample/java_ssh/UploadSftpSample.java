package sample.java_ssh;

import com.jcraft.jsch.*;

public class UploadSftpSample {
    public static void main(String[] args) {
        JSch jsch = new JSch();
        Session session = null;
        Channel channel = null;

        String userName = "digicotf";
        String host = "aitag.iptime.org";
        int port = 10022;
        String pw = "New1234!";

        try {
            session = jsch.getSession(userName, host, port);

            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(pw);

            //ssh compression 설정
            session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none");
            session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none");
            session.setConfig("compression_level", "9");

            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd("/home/digicotf");
//            sftp.mkdir("zzz"); //이미 있는 디렉토리를 생성하려고 시도시 [4: Failure]에러발생

            UploadMonitor f1 = new UploadMonitor();
            UploadMonitor f2 = new UploadMonitor();

            sftp.put("d:/test.txt", "/home/digicotf/test.txt", f1);
            sftp.put("d:/test2.txt", "/home/digicotf/test2.txt", f2);
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if(channel != null) channel.disconnect();
            if(session != null) session.disconnect();
        }
    }
}
