package com.example.OIBG;

import com.example.OIBG.DTO.Drink;
import com.example.OIBG.DTO.Menu;
import com.example.OIBG.DTO.Side;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mysql {
    public static void insert(String barcode, String oi) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/barcode";
        String userName = "root";
        String password = "0000";

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();
        statement.executeUpdate("insert into oi values(" + barcode +",'" + oi + "')");
        //statement.executeQuery("select");

        statement.close();
        connection.close();
    }

    public static List<Menu> get_menu() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/barcode";
        String userName = "root";
        String password = "0000";

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();

        //DB에서 모든 데이터 가져오기
        ResultSet result = statement.executeQuery("select * from menu_list");

        //Menu객체 리스트 생성
        List<Menu> menu_list = new ArrayList<Menu>();;

        while(result.next()) {
            Menu menu = new Menu();
            menu.setMenu(result.getString("menu"));
            menu.setImg(result.getString("img"));
            menu.setPrice(result.getString("price"));
            menu_list.add(menu);
        }

        statement.close();
        connection.close();

        return menu_list;
    }

    public static List<Drink> get_drink() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/barcode";
        String userName = "root";
        String password = "0000";

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();

        //DB에서 모든 데이터 가져오기
        ResultSet result = statement.executeQuery("select * from drink_list");

        //Menu객체 리스트 생성
        List<Drink> drink_list = new ArrayList<Drink>();;

        while(result.next()) {
            Drink drink = new Drink();
            drink.setDrink(result.getString("drink"));
            drink.setImg(result.getString("img"));
            drink_list.add(drink);
        }

        statement.close();
        connection.close();

        return drink_list;
    }

    public static List<Side> get_side() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/barcode";
        String userName = "root";
        String password = "0000";

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();

        //DB에서 모든 데이터 가져오기
        ResultSet result = statement.executeQuery("select * from side_list");

        //Menu객체 리스트 생성
        List<Side> side_list = new ArrayList<Side>();;

        while(result.next()) {
            Side side = new Side();
            side.setSide(result.getString("side"));
            side.setImg(result.getString("img"));
            side_list.add(side);
        }

        statement.close();
        connection.close();

        return side_list;
    }
    public static List<String> get_Img(String menu) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/barcode";
        String userName = "root";
        String password = "0000";

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();

        //DB에서 메뉴에 맞는 세트이미지 가져오기
        String sql = "select img,set_img from menu_list where menu=" +"'"+ menu + "'";
        ResultSet result = statement.executeQuery(sql);

        String set_img = null;
        String img = null;

        while(result.next()) {
            img = result.getString("img");
            set_img = result.getString("set_img");
        }

        List<String> img_list = new ArrayList<String>();

        img_list.add(img);
        img_list.add(set_img);

        statement.close();
        connection.close();

        return img_list;
    }


}
