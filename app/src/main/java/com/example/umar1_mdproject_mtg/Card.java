package com.example.umar1_mdproject_mtg;

//made a card object for ease of info accessibility
public class Card {
    private int userCardId;
    private String cardID;
    private String cardTitle;
    private String setName;
    private String cardColor;
    private int cardQty;
    private Float cardPrice;
    private String cardImgLink;

    public Card(int uID, String cID, String cTitle,String sName, String cColor, int qty, Float price, String imgLink){
        this.userCardId = uID;
        this.cardID = cID;
        this.cardTitle = cTitle;
        this.setName = sName;
        this.cardColor = cColor;
        this.cardQty = qty;
        this.cardPrice = price;
        this.cardImgLink = imgLink;
    }

    public int getUserCardId(){
        return this.userCardId;
    }
    public String getCardID(){
        return this.cardID;
    }
    public String getCardTitle(){
        return this.cardTitle;
    }
    public String getSetName(){
        return this.setName;
    }
    public String getCardColor(){
        return this.cardColor;
    }
    public int getCardQty(){
        return this.cardQty;
    }
    public void setCardQty(int newQty){
        this.cardQty = newQty;
    }
    public String getCardPrice() {
        String longFloat = Float.toString(this.cardPrice);
        int decIndex = longFloat.indexOf('.');
        return "US $ "+ longFloat.substring(0,decIndex+3);
    }
    public String getCardImgLink(){
        return this.cardImgLink;
    }
}
