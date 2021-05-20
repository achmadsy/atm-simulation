<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#btnContinue").click(function () {
            var amount = $("#amount").val();
            window.location.replace('/fund_transfer_save_data?amount='+amount); 
        });
        $("#btnBack").click(function () {
            window.location.replace('/main'); 
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
                <td>
                    Fund Transfer
                </td>
            </tr>
            <tr>
                <td>
                    Please enter transfer amount: 
                </td>
                <td>
                    <input id="amount" type="number" />
                </td>
            </tr>
        </table>
        </br>
        <button id="btnContinue" type="button" class="btn btn-default btn-lg active">
            Continue
        </button> 
        </br>
        <button id="btnBack" type="button" class="btn btn-default btn-lg active">
            Back
        </button>     
        </br>
        <c:if test="${not empty error}">
            <span class="error">${error}</span>
            </br>
        </c:if>
    </body>
</html>

