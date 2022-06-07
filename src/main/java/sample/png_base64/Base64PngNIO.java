package sample.png_base64;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Base64PngNIO {

    private static final Logger logger = LoggerFactory.getLogger(Base64PngNIO.class);

    private static final String PNG_BASE64_PREFIX = "data:image/png;base64,";

    private static String convertBase64StrNIO() {
        String base64Str = "";

        try(RandomAccessFile raf = new RandomAccessFile("d:/sample.png","rw")) {
            FileChannel fc = raf.getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());

            ByteBuffer byteBuffer = ByteBuffer.allocate((int)raf.length());

            byteBuffer.put(mbb);
            byteBuffer.flip();

            base64Str = PNG_BASE64_PREFIX+Base64.encodeBase64String(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64Str;
    }

    public static void main(String[] args) {
        String result = convertBase64StrNIO();
        logger.info("png base64 result : {}", result);
    }
}