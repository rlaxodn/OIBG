package com.example.OIBG;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImgProcess {
    //이미지를 디코딩하여 저장
    public boolean decoder(String base64){

//        String data = base64.split(",")[1];
        String data = base64;

//            byte[] imageBytes = DatatypeConverter.parseBase64Binary(data);

        byte[] imageBytes = Base64.getDecoder().decode(data);

        try {
            BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imageBytes));
            ImageIO.write(bufImg, "jpg", new File("C:/Users/LG/Desktop/Spring/barcode.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //이미지 파일을 인코딩
    public String encoder() throws Exception {
        File file = new File("C:/Users/LG/Desktop/Spring/barcode.jpg");
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

    //이미지 파일 삭제
    public void d_file(){
        File file = new File("C:/Users/LG/Desktop/Spring/barcode.jpg");

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
