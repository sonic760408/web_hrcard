<%-- 
    Document   : HRCardRecQuery
    Created on : 2016/10/1, 下午 04:59:38
    Author     : maxhsieh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="header.jsp" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="css/screen.css" type="text/css" media="screen, print" />
        <link rel="stylesheet" href="table/table.css" type="text/css" media="screen, print" />
        <script src="/WebRes/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"></script>
        <title>人資打卡備援(FOR ANDRIOD)</title>
        <style type="text/css">
            body,table {margin-top: 5px}
            th,td{white-space: nowrap}
            input[type="button"],input[type="submit"]{
                -webkit-border-radius: 5px;
                border-radius: 5px;
            }
        </style>
        <script type="text/javascript">
            function doAddSubmmit(form) {
                if (!form.elements["idno"].value)
                    return alert("請輸入身分證號"), form.elements["idno"].focus(), false;
                if (!form.elements["empno"].value)
                    return alert("請輸入人資員編"), form.elements["empno"].focus(), false;
                if (!/^\d+$/.test(form.elements["empno"].value))
                    return alert("員編只可以輸入數字"), form.elements["empno"].focus(), false;
                if (!/^[A-Za-z]\d{9}$/.test(form.elements["idno"].value))
                    return alert("身分證號格式錯誤"), form.elements["idno"].focus(), false;
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
        <body>

        <spring:url value="/query" var="query_hrcardrecURL" />
        <%-- <spring:url value="/queryAll" var="query_hrcardrecURL" /> --%>
        <spring:url value="/insert" var="insert_hrcardrecURL" />

        <%--
        <form action="${cp}/insert" method="POST" onsubmit="return doAddSubmmit(this)">
            <table class="mars" style="margin: auto; width: 50%">
                <thead>

                </thead>
                <tbody>
                    <tr><td colspan="2" style="color: #006AA9;background-color: #ffa500">人資打卡備援(無須先登錄),請在宣揚人資掛點時使用，並事後回報人事單位
                            <div style="color:red;background-color:white">請注意：登打完成後會警示登打內容，若沒有看到警示訊息，則表示未完成登打</div></td></tr>
                    <tr class="odd"><td colspan="2">
                            <input type="radio" name="cardtype" value="1"/>上班&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="cardtype" value="4" />下班&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="cardtype" value="2" />公出&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="cardtype" value="3" />公入
                        </td></tr>
                    <tr class="odd"><td>身分證號</td><td class="left">
                            <input type="text" name="idno" maxlength="10" size="12" onfocus="select()" style="text-transform:uppercase" required placeholder="請輸入身分證號"/></td>
                    </tr>
                    <tr class="odd"><td>人資員編</td><td class="left">
                            <input type="text" name="empno" maxlength="8" size="10" onfocus="select()" required placeholder="請輸入人資員編"/>
                            (前面自動補0到8位)</td>
                    </tr>
                    <tr><td colspan="2" style="background-color: rgb(153, 204, 153);">
                            <input type="hidden" property="qct"/>
                            <input type="submit" value="打卡"/>
                        </td></tr>
                </tbody>

                <c:if test="${not empty redmsg}">
                    <tfoot>
                        <tr><td colspan="2" style="color:red;background-color: wheat">${redmsg}</td></tr>
                    </tfoot>
                </c:if>
            </table>
        </form><br/>
        --%>

        <form:form method="post" modelAttribute="query_hrcardrecForm" onsubmit="return doAddSubmmit(this)" 
                   action="${insert_hrcardrecURL}">
            <table class="mars" style="margin: auto; width: 50%">
                <thead>

                </thead>
                <tbody>
                    <tr><td colspan="2" style="color: #006AA9;background-color: #ffa500">人資打卡備援(無須先登錄),請在宣揚人資掛點時使用，並事後回報人事單位
                            <div style="color:red;background-color:white">請注意：登打完成後會警示登打內容，若沒有看到警示訊息，則表示未完成登打</div></td></tr>
                    <tr class="odd"><td colspan="2">
                            <form:radiobutton path="cardtype" value="1"/>上班&nbsp;
                            <form:radiobutton path="cardtype" value="4"/>下班&nbsp;
                            <form:radiobutton path="cardtype" value="2"/>公出&nbsp;
                            <form:radiobutton path="cardtype" value="3"/>公入&nbsp;
                        </td></tr>
                    <tr class="odd"><td>身分證號</td><td class="left">
                            <form:input path="idno" type="text" name="idno" maxlength="10" size="12"/>
                            <form:errors path="idno"/></td>
                    </tr>
                    <tr class="odd"><td>人資員編</td><td class="left">
                            <form:input path="empno" type="text" id="empno" maxlength="8"/>(前面自動補0到8位)
                            <form:errors path="empno"/></td>
                    </tr>
                    <tr><td colspan="2" style="background-color: rgb(153, 204, 153);">
                            <form:hidden path="date"/>
                            <input type="submit" value="打卡"/>
                        </td></tr>
                </tbody>

                <c:if test="${not empty redmsg}">
                    <tfoot>
                        <tr><td colspan="2" style="color:red;background-color: wheat">${redmsg}</td></tr>
                    </tfoot>
                </c:if>
            </table>
        </form:form><br/>        


        <form:form method="post" modelAttribute="query_hrcardrecForm" action="${query_hrcardrecURL}" >
            <table style="margin: auto;">
                <tr><td>
                        <form:radiobutton path="cardtype" value="0"/>全部&nbsp;
                        <form:radiobutton path="cardtype" value="1"/>上班&nbsp;
                        <form:radiobutton path="cardtype" value="4"/>下班&nbsp;
                        <form:radiobutton path="cardtype" value="2"/>公出&nbsp;
                        <form:radiobutton path="cardtype" value="3"/>公入&nbsp;                                
                        <form:errors path="cardtype"/>
                    </td><td>
                        員編:<form:input path="empno" type="text" id="empno" maxlength="8"/>(前面自動補0到8位)
                        <form:errors path="empno"/>
                    </td><td>
                        日期：<form:input path="date" type="text" id="date" maxlength="8"/>
                        <form:errors path="date"/>
                    </td><td>
                        顯示<form:select path="qlimit">
                            <form:option value="10" label="10"/>
                            <form:option value="20" label="20"/>
                            <form:option value="50" label="50"/>
                            <form:option value="100" label="100"/>
                            <form:option value="1000" label="1000"/>
                            <form:option value="10000" label="10000"/>
                            <form:option value="100000" label="100000"/>
                            <form:errors path="qlimit"/> 
                        </form:select> 筆
                    </td><td>
                        <input type="submit" value="查詢"/>
                        <input type="button" onclick="dw()" value="下載"/>
                    </td></tr>
            </table>
        </form:form>

        <%--
        <form id="qform" name="qform" action="${cp}/query" method="POST" onsubmit="return doQuerySubmmit(this)">
            <table style="margin: auto;">
                <tr><td>
                        <input type="radio" name="qct" value="0"/>全部&nbsp;
                        <input type="radio" name="qct" value="1"/>上班&nbsp;
                        <input type="radio" name="qct" value="4" />下班&nbsp;
                        <input type="radio" name="qct" value="2" />公出&nbsp;
                        <input type="radio" name="qct" value="3" />公入

                    </td><td>
                        員編:<input type="text" name="empno" maxlength="8" size="10" onfocus="select()"/>(前面自動補0到8位)
                    </td><td>
                        日期：<input type="text" name="date" maxlength="8" size="10" onfocus="select()"/>
                    </td><td>
                        顯示<select id="cnt">
                            <option value="20">20</option>
                            <option value="30">30</option>
                            <option value="40">40</option>
                            <option value="50">50</option>
                            <option value="80">80</option>
                            <option value="100">100</option>
                            <option value="99999">99999</option>
                        </select>筆
                    </td><td>
                        <input type="submit" value="查詢"/>
                        <c:if test="${not empty member}">
                            <span style="width:40px">&nbsp</span>
                            <input type="hidden" name="download"/>
                            <input type="button" onclick="dw()" value="下載"/>
                        </c:if>
                    </td></tr>
            </table>
        </form>
        --%>

    </body>
</html>

