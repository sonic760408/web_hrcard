package com.tintin.dao;

import com.tintin.controller.HRCardRecController;
import com.tintin.model.HRCardRec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maxhsieh
 */
@Repository
public class HRCardRecDaoImpl implements HRCardRecDao {
    
    //@Autowired
    //@Qualifier("transactionManager")
    //TransactionManager transactionManager;
    
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /*
    public void setTransactionManager(
            TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    */

    @Autowired
    @Qualifier("namedParameterJdbcTemplate")
    public void setNamedParameterJdbcTemplate(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final Logger log = LogManager.getLogger(HRCardRecDaoImpl.class);

    @Override
    public HRCardRec findByPrimaryKey(String empno, Date readdt) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empno", empno);
        params.put("readdt", new Timestamp(readdt.getTime()));
        String query = "SELECT * FROM HRSCARDREAD WHERE empno = :empno AND readdt = :readdt ";

        HRCardRec result = null;
        try {
            result = namedParameterJdbcTemplate.queryForObject(query, params, new HRCardRecMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    @Override
    public List<HRCardRec> findAll(String cardtype, String limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("limit", limit);
        params.put("cardtype", cardtype);
        String query = "SELECT * FROM HRSCARDREAD WHERE cardtype LIKE (:cardtype) "
                + " ORDER BY readdt DESC LIMIT (:limit::integer) ";
        List<HRCardRec> result = null;
        try {
            result = namedParameterJdbcTemplate.query(query, params, new HRCardRecMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    @Override
    public List<HRCardRec> findByEmpno(String empno, String cardtype, String limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empno", empno);
        params.put("limit", Integer.parseInt(limit));
        params.put("cardtype", cardtype);
        String query = "SELECT * FROM HRSCARDREAD WHERE empno = :empno AND cardtype LIKE (:cardtype)"
                + " ORDER BY readdt DESC LIMIT :limit ";
        List<HRCardRec> result = null;
        try {
            result = namedParameterJdbcTemplate.query(query, params, new HRCardRecMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public List<HRCardRec> findByEmpnoAndReaddt(String empno, String cardtype, Date readdt, String limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empno", empno);
        params.put("limit", Integer.parseInt(limit));
        params.put("cardtype", cardtype);
        params.put("readdt", new Timestamp(readdt.getTime())); //timestamp, need to date to timestamp
        String query = "SELECT * FROM HRSCARDREAD WHERE empno = :empno"
                + " AND readdt <= :readdt"
                + " AND cardtype LIKE (:cardtype)"
                + " ORDER BY readdt DESC LIMIT :limit ";
        List<HRCardRec> result = null;
        try {
            result = namedParameterJdbcTemplate.query(query, params, new HRCardRecMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    @Transactional
    public void insertHRCardRec(HRCardRec hrcardrec) {
        String query = "INSERT INTO HRSCARDREAD (idno, empno, readdt, cardtype, ip)"
                + "VALUES(:idno, :empno, :readdt, :cardtype, :ip)";
        try {
            //transactionManager.begin();
            namedParameterJdbcTemplate.update(query, getSqlParameterByModel(hrcardrec));
            //transactionManager.commit();
        } catch (Exception ex) {
           // try {
                //transactionManager.rollback();
            //} catch (IllegalStateException | SecurityException | SystemException ex1) {
            //    log.error(ex.getMessage(), ex1);
            //}
            log.error(" ========== ROLLBACK =========== ");
            log.error(ex.getMessage(), ex);
        }
    }

    private SqlParameterSource getSqlParameterByModel(HRCardRec hrcardrec) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("idno", hrcardrec.getIdno());
        paramSource.addValue("empno", hrcardrec.getEmpno());
        paramSource.addValue("readdt", 
                new java.sql.Timestamp(hrcardrec.getReaddt().getTime())); //set as timestamp
        paramSource.addValue("cardtype", hrcardrec.getCardtype());
        paramSource.addValue("ip", hrcardrec.getIp());

        return paramSource;
    }

    private static final class HRCardRecMapper implements RowMapper<HRCardRec> {

        @Override
        public HRCardRec mapRow(ResultSet rs, int rowNum) throws SQLException {
            HRCardRec hrcardrec = new HRCardRec();
            hrcardrec.setIdno(rs.getString("idno"));
            hrcardrec.setEmpno(rs.getString("empno"));
            hrcardrec.setCardtype(rs.getString("cardtype"));
            hrcardrec.setIp(rs.getString("ip"));
            hrcardrec.setReaddt(new Date(rs.getTimestamp("readdt").getTime()));
            return hrcardrec;
        }
    }

}
