<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<%@ include file="../layout/header.jsp"%>

<%--필요 없는 세션 줄이기--%>
<%@ page session="false" %>

<div id="wrapper">
    <div id="content" class="container" align="center">
        <div class="form-title">Sign up</div>
        <div class="form-style">
            <form>
                <div class="form-group" align="left">
                    <label for="username">Username</label> <input type="text" class="form-control" placeholder="Enter username" id="username">
                </div>
                <p class="valid-text" id="valid_username" align="left"></p>
                <div class="form-group" align="left">
                    <label for="password">Password</label> <input type="password" class="form-control" placeholder="Enter password" id="password">
                </div>
                <p class="valid-text" id="valid_password" align="left"></p>
                <div class="form-group" align="left">
                    <label for="nickname">Nickname</label> <input type="text" class="form-control" placeholder="Enter nickname" id="nickname">
                </div>
                <p class="valid-text" id="valid_nickname" align="left"></p>
                <div class="form-group" align="left">
                    <label for="email">Email address</label> <input type="email" class="form-control" placeholder="Enter email" id="email">
                </div>
                <p class="valid-text" id="valid_email" align="left"></p>
            </form><br>
            <div align="right">
                <button id="btn-join" class="btn btn-join"><i class="fa-solid fa-check"></i> 회원가입</button>
            </div>
        </div>
    </div>
<%@ include file="../layout/footer.jsp"%>
<script src="/js/user.js"></script>
