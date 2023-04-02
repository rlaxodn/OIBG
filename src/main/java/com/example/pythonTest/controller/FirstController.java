package com.example.pythonTest.controller;

import com.example.pythonTest.DTO.*;
import com.example.pythonTest.DTO.Menu;
import com.example.pythonTest.Msg;
import com.example.pythonTest.Mysql;
import com.example.pythonTest.Python.Barcode;
import com.example.pythonTest.Python.Test1;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FirstController {

    //장바구니
    List<Order> shopping_list;
    String send_img;


    //화면1
    @GetMapping("/python/start")
    public String start(Model model) throws IOException {
        shopping_list = new ArrayList<Order>();
        return "python/start";
    }

    //화면2
    @PostMapping("/python/menu_list")
    public String showMenu(Model model, Order form) throws SQLException {
        List<Menu> menuList = new ArrayList<Menu>();
        menuList = Mysql.get_menu();
        model.addAttribute("menu_list",menuList);

        //삭제 버튼 눌렀을 시
        if( !(form.getOrder()==null) ){
            for(int i=0; i<shopping_list.size(); i++){
                if( shopping_list.get(i).getOrder().equals(form.getOrder()) ){
                    shopping_list.remove(i);
                    break;
                }
            }
        }
        return "python/menu_list";
    }

    //화면3
    @PostMapping("/python/next_step")
    public String showDetail(Order form, Model model) throws SQLException {

        //버거에 맞는 세트이미지 이미지 가져오기
        String burger = form.getBurger();
        List<String> img_list = new ArrayList<String>();
        img_list = Mysql.get_Img(burger);

        model.addAttribute("burger_img",img_list.get(0));
        model.addAttribute("set_img",img_list.get(1));
        model.addAttribute("burger",burger);

        //사이드 리스트 받아오기
        List<Side> sideList = new ArrayList<Side>();
        sideList = Mysql.get_side();
        model.addAttribute("side_list",sideList);

        //음료 리스트 받아오기
        List<Drink> drinkList = new ArrayList<Drink>();
        drinkList = Mysql.get_drink();
        model.addAttribute("drink_list",drinkList);

        return "python/test";
    }

    //화면4
    @PostMapping("/python/add_list")
    public String check(Order form, Model model){
        form.set_order(form.getOrder());
        model.addAttribute("burger",form.getBurger());
        model.addAttribute("side",form.getSide());
        model.addAttribute("drink",form.getDrink());
        model.addAttribute("order", form.getOrder());

        //주문 리스트에 주문 추가
        Order shopping = new Order();
        shopping.setOrder(form.getOrder());
        shopping.set_order(form.getOrder());
        shopping_list.add(shopping);
        return "python/check";
    }

    //화면5 장바구니
    @PostMapping("/python/check_out")
    public String check(Model model,Order form){
        //삭제 버튼 눌렀을 시
        if( !(form.getOrder()==null) ){
            for(int i=0; i<shopping_list.size(); i++){
                if( shopping_list.get(i).getOrder().equals(form.getOrder()) ){
                    shopping_list.remove(i);
                    break;
                }
            }
        }
        model.addAttribute("shopping_list", shopping_list);
        return "python/check_out";

    }


    //바코드 생성 테스트
    @GetMapping("/python/barcode")
    public String newBarcodeForm() {
        return "python/barcode"; }

    //바코드 생성하기
    @PostMapping("/python/c_barcode")
    public String createBarcodeForm(Model model) throws SQLException {
        String oi = shopping_list.get(0).getOrder();
        for(int i=1; i<shopping_list.size(); i++){
            oi = oi.concat("/");
            oi = oi.concat(shopping_list.get(i).getOrder());
        }
        Barcode barcode = new Barcode();
        String img = barcode.main(oi);
        send_img = img;
        model.addAttribute("img",img);
        return "python/barcodeview";
    }

    //이미지 전송 후 시작화면
    @PostMapping("/python/send")
    public String send_img(PhoneForm form) throws Exception {
        Msg msg = new Msg();
        msg.save_img(send_img);
        String s = msg.encoder();
        msg.sendSMS(s,form.getPhone());
        msg.d_file();
        shopping_list = new ArrayList<Order>();
        return "python/start";
    }
}
