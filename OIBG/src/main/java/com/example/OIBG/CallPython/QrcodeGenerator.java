package com.example.OIBG.CallPython;

import com.example.OIBG.Mysql;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QrcodeGenerator {
    public static String main(String oi) throws SQLException {
        Date today = new Date();
        Locale local = new Locale("KOREAN","KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat time_format = new SimpleDateFormat(pattern, local);
        System.out.println("Python Call");

        String[] command = new String[3];
        command[0] = "python";
        // QR코드생성 파이썬 파일경로
        command[1] = "C:/Users/LG/Desktop/OIBG/python/qrcode_create.py";
        command[2] = time_format.format(today) + (int)(Math.random() * 10000);

        Mysql mysqlConnect = new Mysql();
        mysqlConnect.insert(command[2], oi);

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


        int indexNum = output.indexOf("http");
        String result = output.substring(indexNum);
        System.out.println("output: " + result);
        return result;
    }

}
