<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
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
        <form action = "/withdraw" method = "POST">
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
                    <input oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" type = "number" maxlength="7" id="amount" name="amount">
                </td>
            </tr>
        </table>
        </br>
        <input type="submit" value="Continue" class="btn btn-default btn-lg active"/>
        </form>
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

