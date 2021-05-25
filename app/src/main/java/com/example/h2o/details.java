package com.example.h2o;

public class details {
    public String full,mobile,mobiletwo,address,email,paswd,conpaswd,type,logkey, veri;
    public  details(){

    }
    public details(String full, String mobile, String mobiletwo, String address, String email,String paswd, String conpaswd,String type){
        this.full = full;
        this.mobile = mobile;
        this.mobiletwo = mobiletwo;
        this.address = address;
        this.email = email;
        this.paswd = paswd;
        this.conpaswd = conpaswd;
        this.type = type;
    }

    public details(String logkey,String veri){
        this.logkey = logkey;
        this.veri = veri;
    }


}
