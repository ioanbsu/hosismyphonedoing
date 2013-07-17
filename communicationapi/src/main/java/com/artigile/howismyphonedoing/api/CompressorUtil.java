package com.artigile.howismyphonedoing.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Date: 7/17/13
 * Time: 9:46 AM
 *
 * @author ioanbsu
 */

public class CompressorUtil {

    private CompressorUtil() {
    }

    private static byte[] compressBytes(byte[] input) throws UnsupportedEncodingException, IOException {
        Deflater df = new Deflater();       //this function mainly generate the byte code
        df.setLevel(Deflater.BEST_COMPRESSION);
        df.setInput(input);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);   //we write the generated byte code in this array
        df.finish();
        byte[] buff = new byte[1024];   //segment segment pop....segment set 1024
        while (!df.finished()) {
            int count = df.deflate(buff);       //returns the generated code... index
            baos.write(buff, 0, count);     //write 4m 0 to count
        }
        baos.close();
        byte[] output = baos.toByteArray();
        return output;
    }


    private static byte[] extractBytes(byte[] input) throws UnsupportedEncodingException, IOException, DataFormatException {
        Inflater ifl = new Inflater();   //mainly generate the extraction
        ifl.setInput(input);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
        byte[] buff = new byte[1024];
        while (!ifl.finished()) {
            int count = ifl.inflate(buff);
            baos.write(buff, 0, count);
        }
        baos.close();
        byte[] output = baos.toByteArray();
        return output;
    }
}
