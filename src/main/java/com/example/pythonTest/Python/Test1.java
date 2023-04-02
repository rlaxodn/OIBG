package com.example.pythonTest.Python;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Test1 {

//    private static String x;
//    private static String y;
//
//    public Test1(String x, String y) {
//        this.x = x;
//        this.y = y;
//    }

    public static String main()  {

        Date today = new Date();
        Locale local = new Locale("KOREAN","KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat time_format = new SimpleDateFormat(pattern, local);
        System.out.println("Python Call");

        String[] command = new String[3];
        command[0] = "python";
        command[1] = "C:/Users/LG/Desktop/Spring/qrcode_create.py";
        command[2] = time_format.format(today) + (int)(Math.random() * 10000);
        try {
            return execPython(command);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String execPython(String[] command) throws IOException, InterruptedException {
        CommandLine commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        executor.execute(commandLine);

        String output = outputStream.toString();
        int indexNum = output.indexOf("data");
        String result = output.substring(indexNum);
        System.out.println("output: " + result);
        return result;
    }
}
