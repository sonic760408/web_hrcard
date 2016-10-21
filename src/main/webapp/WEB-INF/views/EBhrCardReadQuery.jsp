<%-- 
    Author     : Kent Yeh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="header.jsp" %><c:set var="cp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="css/screen.css" type="text/css" media="screen, print" />
        <link rel="stylesheet" href="table/table.css" type="text/css" media="screen, print" />
        <link rel="stylesheet" href="<html:rewrite forward="CSS_Calendar"/>" type="text/css" media="all" />
        <title>人資打卡備援(${empty member?'未登錄':member.name})</title>
        <style type="text/css">
            body,table {margin-top: 5px}
            th,td{white-space: nowrap}
            input[type="button"],input[type="submit"]{
                -webkit-border-radius: 5px;
                border-radius: 5px;
            }
        </style>
        <script type="text/javascript" src="<html:rewrite forward="JS_Calendar1"/>"></script>
        <script type="text/javascript" src="<html:rewrite forward="JS_Calendar2"/>"></script>
        <script type="text/javascript" src="<html:rewrite forward="JS_Calendar3"/>"></script>
        <script type="text/javascript">
            function doAddSubmmit(form) {
                if (!form.elements["idno"].value)
                    return alert("請輸入身分證號"),form.elements["idno"].focus(),false;
                if (!form.elements["empno"].value)
                    return alert("請輸入人資員編"),form.elements["empno"].focus(),false;
                if (!/^\d+$/.test(form.elements["empno"].value))
                    return alert("員編只可以輸入數字"),form.elements["empno"].focus(),false;
                if (!/^[A-Za-z]\d{9}$/.test(form.elements["idno"].value))
                    return alert("身分證號格式錯誤"),form.elements["idno"].focus(),false;
                return true;
            }

            function doQuerySubmmit(form) {<c:if test="${'A' ne member.level}">
                if (!form.elements["empno"].value) {
                    form.elements["empno"].focus();
                    alert("請輸入人資員編");
                    return false;
                }</c:if>
                form.elements["download"].value = "F";
                return true;
            }<c:if test="${not empty member}">
            function dw() {
                var form = document.getElementById("queryForm");
                if (doQuerySubmmit(form)) {
                    form.elements["download"].value = "T";
                    form.submit();
                }
            }</c:if>
        </script>
        </head>
        <body<logic:present name="errormsg"> onload="alert('${errormsg}');"</logic:present>>
        <html:form action="/EBhrCardReadAddAction.do" method="POST" onsubmit="return doAddSubmmit(this)">
            <table class="mars" style="margin: auto; width: 50%">
                <thead>
                    <tr><th colspan="2"><input type="button" onclick="location.href = '${cp}/index.jsp'" value="返回"/></th></tr>
                </thead>
                <tbody>
                    <tr><td colspan="2" style="color: #006AA9;background-color: #ffa500">人資打卡備援(無須先登錄),請在宣揚人資掛點時使用，並事後回報人事單位
                        <div style="color:red;background-color:white">請注意：登打完成後會警示登打內容，若沒有看到警示訊息，則表示未完成登打</div></td></tr>
                    <tr class="odd"><td colspan="2">
                            <html:radio property="cardtype" value="1" styleId="cat1"/><label for="cat1">上班</label>&nbsp;&nbsp;&nbsp;
                            <html:radio property="cardtype" value="4" styleId="cat4"/><label for="cat4">下班</label>&nbsp;&nbsp;&nbsp;
                            <html:radio property="cardtype" value="2" styleId="cat2"/><label for="cat2">公出</label>&nbsp;&nbsp;&nbsp;
                            <html:radio property="cardtype" value="3" styleId="cat3"/><label for="cat3">公入</label>
                        </td></tr>
                    <tr class="odd"><td>身分證號</td><td class="left">
                            <input type="text" name="idno" maxlength="10" size="12" onfocus="select()" style="text-transform:uppercase" required placeholder="請輸入身分證號"/></td>
                    </tr>
                    <tr class="odd"><td>人資員編</td><td class="left">
                            <input type="text" name="empno" maxlength="8" size="10" onfocus="select()" required placeholder="請輸入人資員編"/>
                            (前面自動補0到8位)</td>
                    </tr>
                    <tr><td colspan="2" style="background-color: rgb(153, 204, 153);">
                            <html:hidden property="qct"/>
                            <html:submit value="打卡"/>
                        </td></tr>
                </tbody>
                
              <c:if test="${not empty redmsg}">
                  <tfoot>
                      <tr><td colspan="2" style="color:red;background-color: wheat">${redmsg}</td></tr>
                  </tfoot>
              </c:if>
            </table>
        </html:form><br/>
        <html:form action="/EBhrCardReadQueryAction.do" method="POST" onsubmit="return doQuerySubmmit(this)" styleId="queryForm">
            <table style="margin: auto;">
                <tr><td>
                        <html:radio property="qct" value="0" styleId="qct0"/><label for="qct0">全部</label>&nbsp;
                        <html:radio property="qct" value="1" styleId="qct1"/><label for="qct1">上班</label>&nbsp;
                        <html:radio property="qct" value="4" styleId="qct4"/><label for="qct4">下班</label>&nbsp;
                        <html:radio property="qct" value="2" styleId="qct2"/><label for="qct2">公出</label>&nbsp;
                        <html:radio property="qct" value="3" styleId="qct3"/><label for="qct3">公入</label>
                    </td><td>
                        員編:<html:text property="empno" styleId="empno" maxlength="8" size="10" onfocus="select()"/>(前面自動補0到8位)
                    </td><td>日期：<html:text property="date" styleId="date" maxlength="8" size="10" onfocus="select()"/>
                        <input type="button" name="popup" value="..." id="popup">
                        <script type="text/javascript">
                            Calendar.setup({inputField: "date", ifFormat: "%Y%m%d", button: "popup"});
                        </script>
                    </td><td>
                        顯示<html:select property="cnt">
                            <html:option value="20">20</html:option>
                            <html:option value="30">30</html:option>
                            <html:option value="40">40</html:option>
                            <html:option value="50">50</html:option>
                            <html:option value="80">80</html:option>
                            <html:option value="100">100</html:option>
                            <html:option value="99999">99999</html:option>
                        </html:select>筆
                    </td><td>
                        <html:submit value="查詢"/>
                        <c:if test="${not empty member}">
                        <span style="width:40px">&nbsp</span>
                        <input type="hidden" name="download"/>
                        <input type="button" onclick="dw()" value="下載"/>
                        </c:if>
                    </td></tr>
            </table>
        </html:form><br/>
        <logic:present name="cardreads">
            <table class="mars" style="margin: auto;">
                <thead><tr><th>身分證號</th><th>人資員編</th><th colspan="3">類別</th><th>時間</th></tr></thead>
                <tbody>
                    <c:forEach var="cr" items="${cardreads}">
                        <tr class="odd"><td>${cr.idno}</td><td>${cr.empno}</td>
                                <c:choose>
                                    <c:when test="${cr.cardtype eq 1}">
                                        <td>上</td><td>班</td><td>&nbsp;</td>
                                    </c:when>
                                    <c:when test="${cr.cardtype eq 4}">
                                        <td>&nbsp;</td><td>下</td><td>班</td>
                                    </c:when>
                                    <c:when test="${cr.cardtype eq 2}">
                                        <td>公</td><td>&nbsp;</td><td>出</td>
                                    </c:when>
                                    <c:when test="${cr.cardtype eq 3}">
                                        <td>公</td><td>入</td><td>&nbsp;</td>
                                    </c:when>
                                </c:choose>
                            <td>${cr.cardTime}</td></tr>
                            </c:forEach>
                </tbody>
            </table>
        </logic:present>
    </body>
</html>
