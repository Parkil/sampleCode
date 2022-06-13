package sample.java_ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JSchUtil {
    private JSchUtil(){}

    public static Session getJSchSession(String host, int port, String userId, String password) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(userId, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);

        session.connect();
        return session;
    }

    public static int checkAck(InputStream in) throws IOException {
        /*
            read 0 or -1 : success
            read 1 : error
            read 2 : fatal error
            else : unknown
        */
        int read = in.read();

        //error
        if(read == 1 || read == 2) {
            StringBuilder sb = new StringBuilder();

            //get error msg
            int errorRead;
            do {
                errorRead = in.read();
                sb.append((char) errorRead);
            } while (errorRead != '\n');

            throw new IOException(sb.toString());
        }

        return read;
    }

    public static void sendNull(OutputStream out) throws IOException {
        byte[] byteArr = {0};
        out.write(byteArr, 0, 1);
        out.flush();
    }
}
