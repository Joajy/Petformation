<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp"%>

<div class="container">
    <form>
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" placeholder="Enter username">
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" placeholder="Enter password">
        </div>
    </form>
    <button id="btn-login" class="btn btn-primary">로그인</button>
</div>
<%@ include file="../layout/footer.jsp"%>
<script src="/blog/js/user.js"></script>