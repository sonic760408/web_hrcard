/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tintin.dao;

import com.tintin.model.HRCardRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author maxhsieh
 */
public interface HRCardRecDao {

    HRCardRec findByPrimaryKey(String empno, Date readdt);

    List<HRCardRec> findAll(String cardtype, String limit);
    
    List<HRCardRec> findByEmpno(String empno, String cardtype, String limit);
    
    List<HRCardRec> findByEmpnoAndReaddt(String empno, String cardtype, Date eddate, String limit);
    
    void insertHRCardRec(HRCardRec hrcardrec);
    
}
