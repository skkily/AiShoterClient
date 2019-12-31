package com.skkily.aishoterclient.Control;

import java.io.Serializable;

public class ControlObj implements Serializable {
    private int code;
    private String msg;
    public ControlObj(){

    }
    public ControlObj(int code, String msg){
        this.code=code;
        this.msg=msg;
    }
}
