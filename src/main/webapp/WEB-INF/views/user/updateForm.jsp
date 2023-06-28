<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<%@ include file="../layout/header.jsp"%>

<div class="container">
    <form>
        <input type="hidden" id="id" value="${principal.user.id}"/>
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" value="${principal.user.username}" class="form-control" id="username" placeholder="Enter username" readonly>
            <%--value에서 principal을 통해 기존의 값을 가져오는 것을 시사함--%>
            <%--사용자 이름은 수정할 수 없도록 readonly 설정을 걸어놨음--%>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" placeholder="Enter password">
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" value="${principal.user.email}"  class="form-control" id="email" placeholder="Enter email">
        </div>
    </form>
    <button id="btn-update" class="btn btn-primary">수정</button>
</div>
<%@ include file="../layout/footer.jsp"%>
<script src="/js/user.js"></script>
