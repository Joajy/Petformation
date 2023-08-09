<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp"%>

<%--필요 없는 세션 줄이기--%>
<%@ page session="false" %>

<div class="container" align="center">
    <div class="form-title">Sign in</div>
    <div class="form-style">
        <form action="/auth/loginProc" method="POST">
            <div class="form-group" align="left">
                <label for="username">Username</label> <input type="text" name="username" class="form-control" placeholder="Enter username" id="username">
            </div>
            <div class="form-group" align="left">
                <div class="flex justify-content-between">
                    <label for="password">Password</label>
                    <a href="/auth/findForm">비밀번호&nbsp 찾기</a>
                </div>
                <input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
            </div>
            <div class="form-group form-check" align="left">
                <label class="form-check-label"> <input class="form-check-input" name="remember" type="checkbox"> Remember me
                </label>
            </div>
            <span>
				<c:if test="${error}">
                    <p id="valid" class="alert alert-danger">${exception}</p>
                </c:if>
			</span>
            <div align="right">
                <button id="btn-login" class="btn btn-login"><i class="fa-solid fa-lock"></i> 로그인</button>
                <a href="https://kauth.kakao.com/oauth/authorize?client_id=51f6c7b1350fbf2b147798b81b8649a7&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code">
                    <img height="38px" src="/image/kakao_login_button.png" />
                </a>
            </div>
        </form>
    </div>
</div>
<br>
<%@ include file="../layout/footer.jsp"%>