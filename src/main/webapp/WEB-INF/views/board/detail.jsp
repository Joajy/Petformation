<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp"%>

<div class="container">
        <button class="btn btn-secondary" onclick="history.back()">Back</button>
        <c:if test="${board.user.id == principal.user.id}">
            <a href="/board/${board.id}/updateForm" class="btn btn-warning">Correction</a>
            <button id="btn-delete" class="btn btn-danger">Delete</button>
        </c:if>
        <br/>
    <div>
        Post Number: <span id="id"><i>${board.id} </i></span>
        Username: <span><i>${board.user.username} </i></span>
    </div>
        <div class="form-group">
            <h3>${board.title}</h3>

        </div>
        <div class="form-group">
            <div>${board.content}</div>
        </div>
</div>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>