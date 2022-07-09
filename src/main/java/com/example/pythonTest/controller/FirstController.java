package com.example.pythonTest.controller;

import com.example.pythonTest.DTO.PythonForm;
import com.example.pythonTest.Python.Test1;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class FirstController {

    @GetMapping("/python/new")
    public String newPythonForm(){
        return "python/new";
    }

    @PostMapping("/python/create")
    public String createPythonForm(PythonForm form){
        Test1 test1 = new Test1(form.getX(),form.getY());
        test1.main();
        return "python/new";
    }
//        String x = form.getX();
//        String y = form.getY();
//        String[] command = new String[4];
//        command[0] = "python";
//        //command[1] = "\\workspace\\java-call-python\\src\\main\\resources\\test.py";
//        command[1] = "C:/Users/LG/Desktop/Spring/spring.py";
//        command[2] = x;
//        command[3] = y;
//        try {
//            execPython(command);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "python/new";
//    }
//
//    public static void execPython(String[] command) throws IOException, InterruptedException {
//        CommandLine commandLine = CommandLine.parse(command[0]);
//        for (int i = 1, n = command.length; i < n; i++) {
//            commandLine.addArgument(command[i]);
//        }
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
//        DefaultExecutor executor = new DefaultExecutor();
//        executor.setStreamHandler(pumpStreamHandler);
//        int result = executor.execute(commandLine);
//        System.out.println("output: " + outputStream.toString());
//    }

}
