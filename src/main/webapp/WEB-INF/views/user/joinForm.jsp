<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<%@ include file="../layout/header.jsp"%>

<%--필요 없는 세션 줄이기--%>
<%@ page session="false" %>

<div class="container">
    <form>
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" class="form-control" id="username" placeholder="Enter username">
        </div>
        <p id="valid_username"></p>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" placeholder="Enter password">
        </div>
        <p id="valid_password"></p>
        <div class="form-group">
            <label for="nickname">Nickname</label>
            <input type="nickname" class="form-control" id="nickname" placeholder="Enter nickname">
        </div>
        <p id="valid_nickname"></p>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" id="email" placeholder="Enter email">
        </div>
        <p id="valid_email"></p>
    </form>
    <button id="btn-save" class="btn btn-primary">Sign up</button>
</div>
<%@ include file="../layout/footer.jsp"%>
<script src="/js/user.js"></script>
