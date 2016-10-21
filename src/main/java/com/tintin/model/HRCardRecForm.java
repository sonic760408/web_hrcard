/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tintin.model;

import java.util.Date;

/**
 *
 * @author hsiehkaiyang
 */
public class HRCardRecForm {

    private String f_idno;
    private String f_empno;
    private String f_date; //Format (yyyyMMdd HH:mm:ss)
    private String f_cardtype;
    private String f_ip;
    private String f_limit;

    public HRCardRecForm() {
        f_idno = "";
        f_empno = "";
        f_date = "";
        f_cardtype = "";
        f_ip = "";
        f_limit = "";
    }

    //For add
    public HRCardRecForm(String f_idno, String f_empno, String f_date, String f_cardtype, String f_ip) {
        this.f_idno = f_idno;
        this.f_empno = f_empno;
        this.f_date = f_date;
        this.f_cardtype = f_cardtype;
        this.f_ip = f_ip;
        this.f_limit="";
    }

    //For query
    public HRCardRecForm(String q_empno, String q_date, String q_cardtype, String qlimit) {
        this.f_empno = q_empno;
        this.f_date = q_date;
        this.f_cardtype = q_cardtype;
        this.f_limit=qlimit;
    }

    public void setIdno(String idno)
    {
        this.f_idno = idno;
    }
    
    public String getIdno()
    {
        return f_idno;
    }
    
    public void setEmpno(String empno)
    {
        this.f_empno = empno;
    }
    
    public String getEmpno()
    {
        return f_empno;
    }

    public void setDate(String date)
    {
        this.f_date = date;
    }
    
    public String getDate()
    {
        return f_date;
    }

    public void setCardtype(String cardtype)
    {
        this.f_cardtype = cardtype;
    }
    
    public String getCardtype()
    {
        return f_cardtype;
    }

    public void setIp(String ip)
    {
        this.f_ip = ip;
    }
    
    public String getIp()
    {
        return f_ip;
    }

    public void setQlimit(String qlimit)
    {
        this.f_limit = qlimit;
    }
    
    public String getQlimit()
    {
        return f_limit;
    }    
}
