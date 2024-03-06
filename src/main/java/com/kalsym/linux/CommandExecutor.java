package com.kalsym.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author admin
 */
public class CommandExecutor {

    /**
     * Executes the linux command uses LogProperties for logging
     *
     * @param command The command to execute
     */
    public static void execute(String command) {
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                //LogProperties.WriteLog("line: " + s);
            }
            br.close();
            p.waitFor();
            //LogProperties.WriteLog("command [" + command + "] executed exited: " + p.exitValue());
            p.destroy();
        } catch (IOException e) {
            //LogProperties.WriteLog("[CommandExecutor] [" + command + "]" + e);
        } catch (InterruptedException e) {
            //LogProperties.WriteLog("[CommandExecutor] [" + command + "]" + e);
        }
    }
}
