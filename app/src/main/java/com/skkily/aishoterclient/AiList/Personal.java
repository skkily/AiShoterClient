package com.skkily.aishoterclient.AiList;

public class Personal {
    private String name;
    private  int imageId;
    public Personal(String name, int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
