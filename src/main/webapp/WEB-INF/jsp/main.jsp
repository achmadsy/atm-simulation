<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#btnWithdraw").click(function () {
            window.location.replace('/withdraw'); 
        });
        $("#btnTransfer").click(function () {
            window.location.replace('/transfer'); 
        });
        $("#btnHistory").click(function () {
            window.location.replace('/history'); 
        });
        $("#btnExit").click(function () {
            window.location.replace('/logout'); 
        });
    });
</script>
<html>
    <head>
        <style>.error{color:red}</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Welcome!</title>
    </head>
    <body>
        <table>
            <tr><td>
                <button id="btnWithdraw" type="button" class="btn btn-default btn-lg active">
                    Withdraw
                </button>
            </td></tr>
            <tr><td>
                <button id="btnTransfer" type="button" class="btn btn-default btn-lg active">
                    Fund Transfer
                </button>
            </td></tr>
            <tr><td>
                <button id="btnHistory" type="button" class="btn btn-default btn-lg active">
                    Transaction History
                </button>
            </td></tr>
            <tr><td>
                <button id="btnExit" type="button" class="btn btn-default btn-lg active">
                    Exit
                </button>
            </td></tr>
        </table>
        
    </body>
</html>

