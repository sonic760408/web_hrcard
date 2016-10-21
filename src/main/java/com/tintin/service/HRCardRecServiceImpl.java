/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tintin.service;

import com.tintin.dao.HRCardRecDao;
import com.tintin.model.HRCardRec;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author maxhsieh
 */

@Service("hrcardrecService")
public class HRCardRecServiceImpl implements HRCardRecService {

    HRCardRecDao hrcardrecDao;

    @Autowired
    public void setHRCardRecDao(HRCardRecDao hrcardrecDao) {
        this.hrcardrecDao = hrcardrecDao;
    }

    @Override
    public HRCardRec findByPrimaryKey(String empno, Date readdt) {
        return hrcardrecDao.findByPrimaryKey(empno, readdt);
    }

    @Override
    public List<HRCardRec> findAll(String cardtype, String limit) {
        return hrcardrecDao.findAll(cardtype, limit);
    }

    @Override
    public List<HRCardRec> findByEmpno(String empno, String cardtype, String limit) {
       return hrcardrecDao.findByEmpno(empno, cardtype, limit);
    }

    @Override
    public List<HRCardRec> findByEmpnoAndReaddt(String empno, String cardtype, Date eddate, String limit) {
        return hrcardrecDao.findByEmpnoAndReaddt(empno, cardtype, eddate, limit);
    }

    @Override
    public void insertHRCardRec(HRCardRec hrcardrec) {
        hrcardrecDao.insertHRCardRec(hrcardrec);
    }

}
