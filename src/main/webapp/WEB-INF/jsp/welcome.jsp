<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<html>
    <head>
        <style>.error{color:red}</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Welcome!</title>
    </head>
    <body>
        
        <form:form action = "/login" method = "POST" modelAttribute="account">
            <table>
                <tr>
                    <td><form:label path="accountNumber">Enter Account Number : </form:label></td>
                    <td><form:input path="accountNumber" type="text" /></td>
                </tr>
                <tr>
                    <td><form:label path="pin">Enter PIN : </form:label></td>
                    <td><form:input path="pin" type="password" /></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="Login" />
                    </td>
                </tr>
            </table>
            <c:if test="${not empty accountError}">
            <span class="error">${accountError}</span>
            </br>
            </c:if>
            <form:errors path="accountNumber" cssClass="error" />
            </br>
            <form:errors path="pin" cssClass="error" />
            </br>
        </form:form>
        
    </body>
</html>

