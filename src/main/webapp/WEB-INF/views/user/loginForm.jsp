<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp"%>

<%--필요 없는 세션 줄이기--%>
<%@ page session="false" %>

<div class="container">
    <form action="/auth/loginProc" method="POST">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" name="username" class="form-control" id="username" placeholder="Enter username">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" name="password" class="form-control" id="password" placeholder="Enter password">
        </div>
        <span>
				<c:if test="${error}">
                    <p id="valid" class="alert alert-danger">${exception}</p>
                </c:if>
		</span>
        <button id="btn-login" class="btn btn-primary" >Sign in</button>
        <a href="https://kauth.kakao.com/oauth/authorize?client_id=d8b0fe0ec31fa20c787dcf4706b50843&redirect_uri=http://localhost:8080/auth/kakao/callback&response_type=code">
            <img height="39px" src="/image/kakao_login_button.png"></a>
    </form>

</div>
<%@ include file="../layout/footer.jsp"%>