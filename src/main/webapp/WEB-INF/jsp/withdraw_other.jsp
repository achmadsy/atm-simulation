<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#btnContinue").click(function () {
            var amount = $("#amount").val();
            window.location.replace('/withdraw/'+amount); 
        });
        $("#btnBack").click(function () {
            window.location.replace('/withdraw'); 
        });
    });
</script>
<html>
    <head>
        <style>.error{color:red}</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Withdraw - Other</title>
    </head>
    <body>
        <table>
            <tr>
                <td>
                    Other Withdraw
                </td>
            </tr>
            <tr>
                <td>
                    Enter amount (multiples of 10) to withdraw: 
                </td>
                <td>
                    <input type="number" id="amount"/>
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
        </c:if>
    </body>
</html>

