package com.example.OIBG;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//네이버 API 메시지 전송
public class Msg {

    private String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";                    // one space
        String newLine = "\n";                 // new line


        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;
        try {

            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            encodeBase64String = e.toString();
        }
        return encodeBase64String;
    }

    /*
     * https://api.ncloud-docs.com/docs/ko/ai-application-service-sens-smsv2
        {
            "type":"(SMS | LMS | MMS)",
            "contentType":"(COMM | AD)",
            "countryCode":"string",
            "from":"string",
            "subject":"string",
            "content":"string",
            "messages":[
                {
                    "to":"string",
                    "subject":"string",
                    "content":"string"
                }
            ],
            "files":[
                {
                    "name":"string",
                    "body":"string"
                }
            ],
            "reserveTime": "yyyy-MM-dd HH:mm",
            "reserveTimeZone": "string",
            "scheduleCode": "string"
        }
     */
    public void sendSMS(String img, String phone) {
        String hostNameUrl = "https://sens.apigw.ntruss.com";     		// 호스트 URL
        String requestUrl= "/sms/v2/services/";                   		// 요청 URL
        String requestUrlType = "/messages";                      		// 요청 URL
        String accessKey = "개인 인증키";                     	// 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키			// Access Key : https://www.ncloud.com/mypage/manage/info > 인증키 관리 > Access Key ID
        String secretKey = "azUC78OXBxgqvSYtbiS6GObEuULn8PX6HNmcAdbT";  // 2차 인증을 위해 서비스마다 할당되는 service secret key	// Service Key : https://www.ncloud.com/mypage/manage/info > 인증키 관리 > Access Key ID
        String serviceId = "ncp:sms:kr:270308996020:oibg";       // 프로젝트에 할당된 SMS 서비스 ID							// service ID : https://console.ncloud.com/sens/project > Simple & ... > Project > 서비스 ID
        String method = "POST";											// 요청 method
        String timestamp = Long.toString(System.currentTimeMillis()); 	// current timestamp (epoch)
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        JSONObject toJson = new JSONObject();
        JSONArray toArr = new JSONArray();
        JSONObject toJsonF = new JSONObject();
        JSONArray toArrF = new JSONArray();

        //toJson.put("subject","");							// Optional, messages.subject	개별 메시지 제목, LMS, MMS에서만 사용 가능
        //toJson.put("content","sms test in spring 111");	// Optional, messages.content	개별 메시지 내용, SMS: 최대 80byte, LMS, MMS: 최대 2000byte
        toJson.put("to",phone);						// Mandatory(필수), messages.to	수신번호, -를 제외한 숫자만 입력 가능
        toArr.put(toJson);

        bodyJson.put("type","MMS");							// Madantory, 메시지 Type (SMS | LMS | MMS), (소문자 가능)
//        bodyJson.put("contentType","COMM");					// Optional, 메시지 내용 Type (AD | COMM) * AD: 광고용, COMM: 일반용 (default: COMM) * 광고용 메시지 발송 시 불법 스팸 방지를 위한 정보통신망법 (제 50조)가 적용됩니다.
        //bodyJson.put("countryCode","82");					// Optional, 국가 전화번호, (default: 82)
        bodyJson.put("from","01037281899");					// Mandatory, 발신번호, 사전 등록된 발신번호만 사용 가능
        //아래 주석
        bodyJson.put("subject","OIBG");						// Optional, 기본 메시지 제목, LMS, MMS에서만 사용 가능
        bodyJson.put("content","OIBG: QR코드");	// Mandatory(필수), 기본 메시지 내용, SMS: 최대 80byte, LMS, MMS: 최대 2000byte
        bodyJson.put("messages", toArr);					// Mandatory(필수), 아래 항목들 참조 (messages.XXX), 최대 1,000개

        toJsonF.put("name","barcode.jpg");
        toJsonF.put("body",img);
        toArrF.put(toJsonF);

        bodyJson.put("files", toArrF);

        //String body = bodyJson.toJSONString();
        String body = bodyJson.toString();

        System.out.println(body);

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
            con.setRequestMethod(method);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            System.out.println("responseCode" +" " + responseCode);
            if(responseCode == 202) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            System.out.println(response.toString());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //url to img 저장
    public void save_img(String img_url) throws IOException {
        String imgUrl = img_url;
        String path = "C:/Users/LG/Desktop/Spring/qrimage.png";
        InputStream in = null;
        OutputStream out = null;

        try {
            URL url = new URL(imgUrl);
            in = url.openStream();
            // 컴퓨터 또는 서버의 저장할 경로(절대패스로 지정해 주세요.)
            out = new FileOutputStream(path);
            while (true) {
                // 루프를 돌면서 이미지데이터를 읽어들이게 됩니다.
                int data = in.read();
                // 데이터값이 -1이면 루프를 종료하고 나오게 됩니다.
                if (data == -1) {
                    break;
                }
                // 읽어들인 이미지 데이터값을 컴퓨터 또는 서버공간에 저장하게 됩니다.
                out.write(data);
            }
            // 저장이 끝난후 사용한 객체는 클로즈를 해줍니다.
            in.close();
            out.close();
        } catch (Exception e) {
            // 예외처리
            e.printStackTrace();
        }
        Path source = Paths.get("C:/Users/LG/Desktop/Spring/qrimage.png");
        Path target = Paths.get("C:/Users/LG/Desktop/Spring/qrimage.jpg");

        BufferedImage originalImage = ImageIO.read(source.toFile());

        BufferedImage newBufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);
        ImageIO.write(newBufferedImage, "jpg", target.toFile());
    }

    public String encoder() throws Exception {
        File file = new File("C:/Users/LG/Desktop/Spring/qrimage.jpg");
        String sbase64 = null;

        if ( file.isFile() ) {
            byte[] bt = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);

            try {
                fis.read(bt);
                sbase64 = new String(Base64.getEncoder().encode(bt));

            } catch (Exception e) {
            } finally {
                fis.close();
            }
        }
        return sbase64;
    }

    public void d_file(){
        File file = new File("C:/Users/LG/Desktop/Spring/qrimage.jpg");

        //이미지 파일 삭제
        if( file.exists() ){
            if(file.delete()){
                System.out.println("파일삭제 성공");
            }else{
                System.out.println("파일삭제 실패");
            }
        }else{
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

}
