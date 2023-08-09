<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp"%>
<div id="wrapper" align="center">
    <div id="content">
        <div class="form-title">Change your profile</div>
        <div class="form-style">
            <form>
                <div class="form-group" align="left">
                    <label for="username">Username</label> <input type="text" value="${principal.user.username}" class="form-control" placeholder="Enter username" id="username" readonly>
                </div>
                <c:choose>
                    <c:when test="${empty principal.user.oauth}">
                        <%--value에서 principal을 통해 기존의 값을 가져오는 것을 시사함--%>
                        <%--사용자 이름은 수정할 수 없도록 readonly 설정을 걸어놨음--%>
                        <div class="form-group" align="left">
                            <label for="password">Password</label> <input type="password" class="form-control" placeholder="Enter password" id="password">
                        </div>
                        <p class="valid-text" id="valid_password" align="left"></p>
                        <div class="form-group" align="left">
                            <label for="nickname">Nickname</label>
                            <input type="text" value="${principal.user.nickname}" class="form-control" placeholder="Enter nickname" id="nickname">
                        </div>
                        <p class="valid-text" id="valid_nickname" align="left"></p>
                        <div class="form-group" align="left">
                            <label for="email">Email address</label>
                            <input type="email" value="${principal.user.email}" class="form-control" placeholder="Enter email" id="email" readonly>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="form-group" align="left">
                            <label for="nickname">Nickname</label>
                            <input type="text" value="${principal.user.nickname}" class="form-control" placeholder="Enter nickname" id="nickname" readonly>
                        </div>
                        <div class="form-group" align="left">
                            <label for="email">Email address</label>
                            <input type="email" value="${principal.user.email}" class="form-control" placeholder="Enter email" id="email" readonly>
                        </div>
                    </c:otherwise>
                </c:choose>
            </form>
            <c:choose>
                <c:when test="${empty principal.user.oauth}">
                    <div align="right">
                        <button id="btn-update" class="btn btn-confirm">
                            <i class="fa-solid fa-check"></i> 완료
                        </button>
                    </div>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-back" onclick="history.back()">뒤로</button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <br>

<%@ include file="../layout/footer.jsp"%>
<script src="/js/user.js"></script>
