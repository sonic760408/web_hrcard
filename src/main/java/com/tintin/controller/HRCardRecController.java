/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tintin.controller;

import com.tintin.model.HRCardRec;
import com.tintin.model.HRCardRecForm;
import com.tintin.service.HRCardRecService;
import com.tintin.validator.HRCardRecFormValidator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.json.*;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 * @author maxhsieh
 */
@Controller
public class HRCardRecController {

    //private HRCardRecManager hrcardrecmanager = new HRCardRecManager();
    private static final Logger log = LogManager.getLogger(HRCardRecController.class);
    private static final long MIN_PERIOD = 600000; //600msec

    /*
     @Autowired
     @Qualifier("dbDataSource")
     private DataSource dataSource;

     @Autowired
     @Qualifier("transactionManager")
     private TransactionManager transactionManager;

     public void setDataSource(DataSource dataSource) {
     this.dataSource = dataSource;
     }
     */
    //@Autowired
    //HRCardRecFormValidator hrcardrecFormValidator;
    //@Autowired
    private HRCardRecService hrcardrecService;

    @Autowired
    @Qualifier("hrcardrecService")
    public void setHRCardRecService(HRCardRecService hrcardrecService) {
        this.hrcardrecService = hrcardrecService;
    }

    /* Init binder */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //binder.setValidator(hrcardrecFormValidator);
    }

    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET})
    public String index(Model model) {
        log.debug("index()");
        HRCardRecForm hrcardrecform = new HRCardRecForm();
        try {
            //Set date format
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
            Date current = new Date();
            hrcardrecform.setIdno("");
            hrcardrecform.setEmpno("");
            hrcardrecform.setCardtype("0");
            //hrcardrec.setReaddt(current);
            hrcardrecform.setDate(sdFormat.format(current));
            hrcardrecform.setIp("");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        model.addAttribute("query_hrcardrecForm", hrcardrecform);
        return "index";
    }

    // list page
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryHRCardRec(HttpServletRequest request, @ModelAttribute("hrcardrecForm") @Validated HRCardRecForm _hrcardrecform,
            Model model) {

        HRCardRec qhrcardrec;
        HRCardRecForm hrform;
        String qlimit;

        if (request.getAttribute("hrcardrecFormJSON") != null) {
            hrform = (HRCardRecForm) request.getAttribute("hrcardrecFormJSON");

            if (hrform == null) {
                return "{\"error\":\"hrform null\"}";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            Date qdate = new Date();
            try {
                qdate = sdf.parse(hrform.getDate());
            } catch (ParseException ex) {
                log.error(ex.getMessage(), ex);
            }
            qhrcardrec = new HRCardRec(hrform.getIdno(), hrform.getEmpno(),
                    qdate, hrform.getCardtype(), hrform.getIp());
            qlimit = hrform.getQlimit();

            request.removeAttribute("hrcardrecFormJSON");
        } else {
            Date qdate = new Date();

            if (_hrcardrecform.getCardtype().equals("0")) {
                _hrcardrecform.setCardtype("%");
            }

            //set date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //進行轉換
            try {
                qdate = sdf.parse(_hrcardrecform.getDate());
            } catch (ParseException ex) {
                log.error(ex.getMessage(), ex);
            }

            qhrcardrec = new HRCardRec(_hrcardrecform.getIdno(), _hrcardrecform.getEmpno(),
                    qdate, _hrcardrecform.getCardtype(), _hrcardrecform.getIp());
            qlimit = _hrcardrecform.getQlimit();
        }

        //query data
        List<HRCardRec> hrcardreclist = hrcardrecService.findByEmpnoAndReaddt(qhrcardrec.getEmpno(),
                qhrcardrec.getCardtype(), qhrcardrec.getReaddt(), qlimit); //set return model
        model.addAttribute("qhrcardrec", hrcardreclist);

        String str = genJSONResponse(hrcardreclist);

        //return json body for android parser
        return str;
    }

    // list page
    @RequestMapping(value = "/insert", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String insertHRCardRec(HttpServletRequest request, @ModelAttribute("hrcardrecForm") HRCardRecForm _hrcardrecform,
            Model model) {

        log.warn("insertUser()");
        //log.error("saveOrUpdateUser() : {}", _hrcardrec);
        //log.debug("Context :" + _hrcardrec.toString());
        //set carddt as now time
        Date nowdate = new Date();
        HRCardRec hrcardrec;
        HRCardRecForm hrform;

        if (request.getAttribute("hrcardrecFormJSON") != null) {
            hrform = (HRCardRecForm) request.getAttribute("hrcardrecFormJSON");

            if (hrform == null) {
                return "{\"error\":\"hrform null\"}";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = new Date();
            try {
                date = sdf.parse(hrform.getDate());
            } catch (ParseException ex) {
                log.error(ex.getMessage(), ex);
            }
            hrcardrec = new HRCardRec(hrform.getIdno(), hrform.getEmpno(),
                    date, hrform.getCardtype(), hrform.getIp());
            request.removeAttribute("hrcardrecFormJSON");

        } else {
            hrform = _hrcardrecform;

            if (hrform == null) {
                return "{\"error\":\"hrform null\"}";
            }

            if (hrform.getCardtype().equals("0")) {
                log.error(" CARDTYPE FORMAT ERROR");
                return "{\"error\":\"打卡名稱錯誤\"}";
            }

            hrcardrec = new HRCardRec();
            hrcardrec.setIdno(hrform.getIdno());
            hrcardrec.setEmpno(hrform.getEmpno());
            hrcardrec.setCardtype(hrform.getCardtype());

            if (hrform.getIp() == null || hrform.getIp().length() == 0) {
                hrcardrec.setIp("127.0.0.1");         //set IP as request (current time set localhost)....
            } else {
                hrcardrec.setIp(hrform.getIp());
            }
            if (hrform.getDate() == null || hrform.getDate().length() == 0) {
                hrcardrec.setReaddt(nowdate);
            } else {
                //change date as correct date/time format
                //欲轉換的日期字串
                String dateString = hrform.getDate();

                if (hrform.getDate().length() <= 8) {
                    hrcardrec.setReaddt(nowdate);
                } else {
                    hrcardrec.setReaddt(new Date(dateString));
                }
            }
        }

        //check latest card type same as cardtype
        List<HRCardRec> hrcardreclistold = hrcardrecService.findByEmpno(hrcardrec.getEmpno(),
                hrcardrec.getCardtype(), "1");

        //Timestamp(msec)
        if (hrcardreclistold.size() > 0) {
            if ((nowdate.getTime() - hrcardreclistold.get(0).getReaddt().getTime()) <= MIN_PERIOD) {
                log.error(" TIME PERIOD < 600 sec, REJECT !! ");
                return "{\"error\":\"同類別的打卡時間間隔未超過600秒\"}";
            }
        }

        //insert date
        hrcardrecService.insertHRCardRec(hrcardrec);

        //check data exist or not
        HRCardRec hrcardreclist = hrcardrecService.findByPrimaryKey(hrcardrec.getEmpno(), hrcardrec.getReaddt());
        String cardtype = "";
        if (hrcardrec.getCardtype().equals("1")) {
            cardtype = "上班";
        } else if (hrcardrec.getCardtype().equals("2")) {
            cardtype = "公出";
        } else if (hrcardrec.getCardtype().equals("3")) {
            cardtype = "公入";
        } else if (hrcardrec.getCardtype().equals("4")) {
            cardtype = "下班";
        }

        if (hrcardreclist == null) {
            return "{\"error\":\"新增員編:" + hrcardrec.getEmpno() + cardtype + " 打卡紀錄失敗\"}";
        }

        //parse the json string
        String str = genJSONResponse(hrcardreclist);

        //return json body for android parser to incident success
        //if any items is null, hrcard failed
        return str;
    }

    private String genJSONResponse(HRCardRec hrcardreclist) {
        StringBuilder sb = new StringBuilder("{\"total\":").append("1").append(",\"hrcardrec\":[");
        sb.append("{");
        sb.append("\"empno\":\"").append(hrcardreclist.getEmpno()).append("\",\"datetime\":\"").
                append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(hrcardreclist.getReaddt()))
                .append("\",\"cardtype\":\"").append(hrcardreclist.getCardtype());
        sb.append("\"}");
        sb.append("]}");
        return sb.toString();
    }

    private String genJSONResponse(List<HRCardRec> hrcardreclist) {
        StringBuilder sb = new StringBuilder("{\"total\":").append(hrcardreclist.size()).append(",\"hrcardrec\":[");
        boolean isFirst = true;
        for (HRCardRec hrcardrec : hrcardreclist) {
            if (!isFirst) {
                sb.append(",");
            }
            sb.append("{");
            sb.append("\"empno\":\"").append(hrcardrec.getEmpno()).append("\",\"datetime\":\"").
                    append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(hrcardrec.getReaddt()))
                    .append("\",\"cardtype\":\"").append(hrcardrec.getCardtype());
            sb.append("\"}");
            isFirst = false;
        }
        sb.append("]}");
        return sb.toString();
    }

    /*
     @RequestMapping("/admin/user/{account}")
     public String userinfo(@PathVariable Member account, HttpServletRequest request) {
     if (account != null) {
     request.setAttribute("member", account);
     }
     return "index";
     }
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryHRCardRecAll(@ModelAttribute("hrcardrecForm") HRCardRecForm _hrcardrecform) {

        log.debug("showAllUsersBY REQUEST()");
        //log.error("saveOrUpdateUser() : {}", _hrcardrec);
        //log.debug("Context :" + _hrcardrec.toString());

        //log.error("MODEL: " + model.toString());
        //log.debug("");
        //log.error("FORM:" + _hrcardrecform.toString());
        List<HRCardRec> hrcardreclist = hrcardrecService.findAll("%", "10000");
        //model.addAttribute("qhrcardrec", hrcardreclist);

        StringBuilder sb = new StringBuilder("{\"total\":").append(hrcardreclist.size()).append(",\"hrcardrec\":[");
        boolean isFirst = true;
        for (HRCardRec hrcardrec : hrcardreclist) {
            if (!isFirst) {
                sb.append(",");
            }
            sb.append("{");
            sb.append("\"idno\":\"").append(hrcardrec.getIdno());
            sb.append("\",\"empno\":\"").append(hrcardrec.getEmpno()).append("\",\"datetime\":\"").
                    append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(hrcardrec.getReaddt()))
                    .append("\",\"cardtype\":\"").append(hrcardrec.getCardtype());
            sb.append("\",\"ip\":\"").append(hrcardrec.getIp());
            sb.append("\"}");
            isFirst = false;
        }
        sb.append("]}");
        //log.info(" XXXX JSON SB: XXXX");
        //log.info(sb);
        //return "HRCardRecQuery";
        return sb.toString();

        //return "users/list";
    }

    //使用JSON格式來接收ANDROID資料
    @RequestMapping(value = "/DoJSON", method = {RequestMethod.POST, RequestMethod.GET}, produces = "text/html;charset=UTF-8")
    public String doJSON(HttpServletRequest request, Model model, final RedirectAttributes redir) {

        //debug
        //Debug logs, show header
        /*
         Enumeration headerNames = request.getHeaderNames();
         while (headerNames.hasMoreElements()) {
         String headerName = (String) headerNames.nextElement();
         log.info(" XXXX Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
         }
         */
        //End header
        //show request session content
        /*
         HttpSession session = request.getSession();
         Enumeration e1 = session.getAttributeNames();
         while (e1.hasMoreElements()) {
         String name = (String) e1.nextElement();
         log.info(" OOOOXXX " + name + ": " + session.getAttribute(name));
         }
         */
        //End session content
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }

        String body = stringBuilder.toString();
        log.info(" XXXX BODY: " + body);

        String jsonString = body;
        JSONObject obj = new JSONObject(jsonString);
        String redirect_addr = "";
        HRCardRecForm hrcardrecForm = new HRCardRecForm();

        //log.info(" XXXX RECV JSON: " + jsonString);
        String req = obj.getString("request");
        //GET user info
        String user = obj.getString("hrcardrec");
        JSONObject userobj = new JSONObject(user);
        if (req.equalsIgnoreCase("insert")) {

            //log.info(" XXXX USER: "+ user);
            //User 
            hrcardrecForm.setIdno(userobj.getString("idno"));
            hrcardrecForm.setEmpno(userobj.getString("empno"));
            hrcardrecForm.setDate(userobj.getString("datetime"));
            hrcardrecForm.setCardtype(userobj.getString("cardtype"));
            hrcardrecForm.setIp(userobj.getString("ip"));
            hrcardrecForm.setQlimit("0");

            redirect_addr = "forward:/insert";
            //get info
        } else if (req.equalsIgnoreCase("query")) {
            hrcardrecForm.setIdno("");
            hrcardrecForm.setEmpno(userobj.getString("empno"));
            hrcardrecForm.setDate(userobj.getString("datetime"));
            hrcardrecForm.setCardtype(userobj.getString("cardtype"));
            hrcardrecForm.setIp("");
            hrcardrecForm.setQlimit(userobj.getString("limit"));

            redirect_addr = "forward:/query";
        }

        model.addAttribute("hrcardrecFormJSON", hrcardrecForm);

        //make form
        //redir.addFlashAttribute("hrcardrecForm1", (HRCardRecForm) hrcardrecForm);
        //log.error(" XXXX FORM: " + hrcardrecForm.getEmpno());
        return redirect_addr;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Exception ex
    ) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("exception", ex);

        return model;

    }

    public void doInsHRCardRec() {

    }

}
