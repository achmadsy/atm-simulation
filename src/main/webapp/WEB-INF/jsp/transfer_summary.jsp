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
        <title>Fund Transfer</title>
    </head>
    <body>
        <table>
            <tr>
                <td>Fund Transfer Summary</td>
            </tr>
            <tr>
                <td>Destination Account</td>
                <td>: ${transaction.getDestAccount()}</td>
            </tr>
            <tr>
                <td>Transfer Amount</td>
                <td>: \$${transaction.getAmount()}</td>
            </tr>
            <tr>
                <td>Reference Number</td>
                <td>: ${transaction.getRefNumber()}</td>
            </tr>
            <tr>
                <td>Balance</td>
                <td>: \$${transaction.getAccount().getBalance().toString()}</td>
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

