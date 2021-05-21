<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#btnTransaction").click(function () {
            window.location.replace('/main'); 
        });
        $("#btnLogout").click(function () {
            window.location.replace('/logout'); 
        });
        $("#btnFilter").click(function () {
            var date = $("#datepicker").val();
            window.location.replace('/history?filterDate='+date); 
        });
    });
</script>
<html>
    <head>
        <style>.error{color:red}</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Transaction History</title>
    </head>
    <body>
        <table>
            <tr>
                <td>Transaction History</td>
            </tr>
            <tr></tr>
            <tr>
                <td>Type</td>
                <td>Amount</td>
            </tr>
            <c:forEach items="${transHistory}" var="item" varStatus="status">
                <tr>
                    <td>${item.transactionType}</td>
                    <td>\$${item.amount}</td>
                </tr>
             </c:forEach>
        </table>
        </br>
        <input type="date" id="datepicker" name="datepicker"
        value="${filterDate}"
        min="2018-01-01" max="2021-12-31">
        <button id="btnFilter" type="button" class="btn btn-default btn-lg active">
            Filter Result
        </button>
        <button id="btnTransaction" type="button" class="btn btn-default btn-lg active">
            Transaction
        </button>
        <button id="btnLogout" type="button" class="btn btn-default btn-lg active">
            Exit
        </button>
    </body>
</html>

