package com.tintin.model;

import java.io.Serializable;
import java.util.Date;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maxhsieh
 */

public class HRCardRec implements Serializable{

    private static final long serialVersionUID = 1L;

    /*
     @SequenceGenerator(name = "magazineSeq", sequenceName = "ecmobile_mid_seq")
     @GeneratedValue(generator = "magazineSeq", strategy = GenerationType.IDENTITY)
     @Column(name = "mid")
     */
    private String idno;
    private String empno;
    private Date readdt;
    private String cardtype;
    private String ip;

    public HRCardRec()
    {
        
    }
    
    public HRCardRec(String idno, String empno, Date readdt, String cardtype, String ip) {
        this.idno = idno;
        this.empno = empno;
        this.readdt = readdt;
        this.cardtype = cardtype;
        this.ip = ip;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public Date getReaddt() {
        return readdt;
    }

    public void setReaddt(Date readdt) {
        this.readdt = readdt;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int compareTo(HRCardRec o) {
        return this.idno.compareTo(o.idno);
    }

    /*
     @Override
     public boolean equals(Object obj) {
     if (obj == null) {
     return false;
     }
     if (getClass() != obj.getClass()) {
     return false;
     }
     final HRCardRec other = (HRCardRec) obj;
     if (!this.idno.equals(other.idno)) {
     return false;
     }
     return true;
     }
     */
    @Override
    public String toString() {
        return idno + ", " + empno + ", " + readdt.toString() + ", " + cardtype + ", " + ip;
    }

    /*
    public static class HRCardRecMapper implements ResultSetMapper<HRCardRec> {

        @Override
        public HRCardRec map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
            String idno = rs.getString("idno");
            String empno = rs.getString("empno");
            Date readdt = rs.getDate("readdt");
            String cardtype = rs.getString("cardtype");
            String ip = rs.getString("ip");
            
            return new HRCardRec(idno,empno,readdt,cardtype,ip);
        }
    }
    */
    
}
