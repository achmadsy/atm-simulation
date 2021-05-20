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
    });
</script>
<html>
    <head>
        <style>.error{color:red}</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Summary!</title>
    </head>
    <body>
        <table>
            <tr>
                <td>Summary</td>
            </tr>
            <tr>
                <td>Date :</td>
                <td>${account.getLatestTransactionHistory().getFormattedDate()}</td>
            </tr>
            <tr>
                <td>Withdraw :</td>
                <td>\$${account.getLatestTransactionHistory().getAmount()}</td>
            </tr>
            <tr>
                <td>Balance :</td>
                <td>\$${account.getBalance().toString()}</td>
            </tr>
        </table>
        </br>
        <button id="btnTransaction" type="button" class="btn btn-default btn-lg active">
            Transaction
        </button>
        <button id="btnLogout" type="button" class="btn btn-default btn-lg active">
            Exit
        </button>
    </body>
</html>

