package com.example.pythonTest;

import com.example.pythonTest.DTO.Menu;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@SpringBootApplication
public class PythonTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PythonTestApplication.class, args);
	}

}
