package com.example.OIBG.Controller;

import com.example.OIBG.CallPython.QrcodeGenerator;
import com.example.OIBG.DTO.*;
import com.example.OIBG.Msg;
import com.example.OIBG.Mysql;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    //장바구니
    List<Order> shopping_list;
    String send_img;

    //시작화면
    @GetMapping("/oibg/start")
    public String start(Model model) throws IOException {
        shopping_list = new ArrayList<Order>();
        return "oibg/start";
    }

    //화면2(전체메뉴-메뉴선택)
    @PostMapping("/oibg/menu_list")
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
        return "oibg/menu_list";
    }

    //화면3(디테일한 주문)
    @PostMapping("/oibg/next_step")
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

        return "oibg/test";
    }

    //화면4(주문확인 - 계속주문하기 or 장바구니)
    @PostMapping("/oibg/add_list")
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
        return "oibg/check";
    }

    //화면5(장바구니)
    @PostMapping("/oibg/check_out")
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
        return "oibg/check_out";

    }


    //QR코드 생성하기
    @PostMapping("/oibg/c_barcode")
    public String createBarcodeForm(Model model) throws SQLException {
        String oi = shopping_list.get(0).getOrder();
        for(int i=1; i<shopping_list.size(); i++){
            oi = oi.concat("/");
            oi = oi.concat(shopping_list.get(i).getOrder());
        }
        QrcodeGenerator qrcodeGenerator = new QrcodeGenerator();
        String img = qrcodeGenerator.main(oi);
        send_img = img;
        model.addAttribute("img",img);
        return "oibg/barcodeview";
    }

    //QR코드 문자 전송 후 시작화면
    @PostMapping("/oibg/send")
    public String send_img(PhoneForm form) throws Exception {
        Msg msg = new Msg();
        msg.save_img(send_img);
        String s = msg.encoder();
        msg.sendSMS(s,form.getPhone());
        msg.d_file();
        shopping_list = new ArrayList<Order>();
        return "oibg/start";
    }

}
