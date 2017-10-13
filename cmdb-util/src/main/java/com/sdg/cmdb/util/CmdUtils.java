package com.sdg.cmdb.util;


import org.apache.commons.exec.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liangjian on 16/10/13.
 */
public class CmdUtils {

    private static final Logger logger = LoggerFactory.getLogger(CmdUtils.class);


    public static String run(CommandLine commandline) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            //CommandLine commandline = CommandLine.parse(command);

            DefaultExecutor exec = new DefaultExecutor();
            //创建监控时间60秒，超过60秒则中端执行
            ExecuteWatchdog watchdog = new ExecuteWatchdog(60*1000);
            exec.setWatchdog(watchdog);
            exec.setExitValues(null);
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            exec.setStreamHandler(streamHandler);
            exec.execute(commandline);
            String out = outputStream.toString("utf8");
            String error = errorStream.toString("utf8");
            return out + error;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return e.toString();
        }
    }


    /**
     * 执行命令并返回执行结果
     *
     * @param cmd
     * @return
     */
    public static String runCmd(String cmd) {
        Process process = null;
        List<String> processList = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            input.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        StringBuffer buffer = new StringBuffer();
        for (String line : processList) {
            buffer.append(line);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * 执行命令并返回执行结果
     *
     * @param cmd
     * @return
     */
    public static String run(String[] cmd) {
        Process process;
            BufferedReader input = null;
        List<String> processList = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec(cmd);
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            input.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {}
            }
        }
        StringBuffer buffer = new StringBuffer();
        for (String line : processList) {
            buffer.append(line);
            buffer.append("\n");
        }
        return buffer.toString();
    }


    /**
     * 执行命令并返回exitCode
     * 0 成功
     * !0 失败
     *
     * @param cmd
     * @return
     */
    public static String callShell(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            Integer exitValue = process.waitFor();
            if (0 != exitValue) {
                // call shell failed. error code is : exitValue
                return exitValue.toString();
            }
        } catch (Throwable e) {
            // call shell failed. e
            return "1";
        }
        return "0";
    }


}
