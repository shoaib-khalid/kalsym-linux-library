package com.kalsym.linux;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 *
 * @author Ali Khan
 */
public class ScriptExecutor {

    public static Charset charset = Charset.forName("UTF-8");
    public static CharsetEncoder encoder = charset.newEncoder();
    File batchFile;

    /**
     *
     * @param commands LINEFEED separated commands to be written in the script
     * file to execute uses LogProperties for logging
     */
    public ScriptExecutor(String commands) {
        try {
            batchFile = File.createTempFile("myBatch", ".sh");
            final FileChannel fileChannel = new RandomAccessFile(batchFile, "rw").getChannel();

            final ByteBuffer bb = strToBB(commands);

            fileChannel.write(bb);
            bb.clear();
            fileChannel.close();

            batchFile.setExecutable(true);
            //LogProperties.WriteLog("File created at Path: " + batchFile.getPath() + " batchFile.canExecute(): " + batchFile.canExecute());
        } catch (Exception exp) {
            //LogProperties.WriteLog("Exception in Creating file " + exp);
        }
    }

    private static ByteBuffer strToBB(String msg) {
        try {
            return encoder.encode(CharBuffer.wrap(msg));
        } catch (Exception e) {
            //LogProperties.WriteLog("Exception in Creating ByteBufer " + e);
        }
        return null;
    }

    /**
     * Executes the script file created and returns the output as string
     *
     * @return The output of the script file, returns empty on any exception
     */
    public String execute() {
        try {
            ProcessBuilder pb = new ProcessBuilder(batchFile.getPath(), "", "");
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                //LogProperties.WriteLog(line);
            }
            //LogProperties.WriteLog("Command Executed through PB, returning: [" + sb.toString() + "]");
            return sb.toString();
        } catch (Exception exp) {
            //LogProperties.WriteLog("executeThrough PB: " + exp);
        }
        return "";
    }
}
