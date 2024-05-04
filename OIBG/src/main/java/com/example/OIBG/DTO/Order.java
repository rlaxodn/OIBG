package com.example.OIBG.DTO;

//장바구니에 담기위한 버거세트 1개에 대한 주문정보 객체
public class Order {

    private String order;
    private String burger;
    private String side;
    private String drink;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void set_order(String order){
        String[] sp = order.split(",");
        setBurger(sp[0]);
        if(sp.length!=1){
            setSide(sp[1]);
            setDrink(sp[2]);
        }
        else{
            setSide(" ");
            setDrink(" ");
        }
    }
    public String getBurger() {
        return burger;
    }

    public void setBurger(String burger) {
        this.burger = burger;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }
}
