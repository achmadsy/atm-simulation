<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#btnOpt1").click(function () {
            window.location.replace('/withdraw/10'); 
        });
        $("#btnOpt2").click(function () {
            window.location.replace('/withdraw/50'); 
        });
        $("#btnOpt3").click(function () {
            window.location.replace('/withdraw/100'); 
        });
        $("#btnOther").click(function () {
            window.location.replace('/withdraw/other'); 
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
        <title>Welcome!</title>
    </head>
    <body>
        <table>
            <tr><td>
                <button id="btnOpt1" type="button" class="btn btn-default btn-lg active">
                    $10
                </button>
            </td></tr>
            <tr><td>
                <button id="btnOpt2" type="button" class="btn btn-default btn-lg active">
                    $50
                </button>
            </td></tr>
            <tr><td>
                <button id="btnOpt3" type="button" class="btn btn-default btn-lg active">
                    $100
                </button>
            </td></tr>
            <tr><td>
                <button id="btnOther" type="button" class="btn btn-default btn-lg active">
                    Other
                </button>
            </td></tr>
            <tr><td>
                <button id="btnBack" type="button" class="btn btn-default btn-lg active">
                    Back
                </button>
            </td></tr>
        </table>
        </br>
        <c:if test="${not empty error}">
            <span class="error">${error}</span>
        </c:if>
    </body>
</html>

