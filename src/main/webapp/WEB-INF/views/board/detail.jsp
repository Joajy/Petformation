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

    <div class="card">
        <form>
            <input type="hidden" id="boardId" value="${board.id}"/>
            <div class="card-body">
                <textarea id="reply-content" class="form-control" rows="1"></textarea>
            </div>
            <div class="card-footer">
                <button type="button" id="btn-save-reply" class="btn btn-primary">Submit</button>
            </div>
        </form>
    </div>
    <br/>
    <div class="card">
        <div class="card-header">Comment List</div>
        <ul id="comment-box" class="list-group">
            <c:forEach var="reply" items="${board.reply}">
                <li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
                    <div>${reply.content}</div>
                    <div class="d-flex">
                        <div class="font-italic">User: ${reply.user.username} &nbsp</div><br/>
                        <span style="float:right">${board.createDate} &nbsp</span><br/>
                        <c:if test="${reply.user.username==principal.user.username}">
                            <button onclick="index.deleteReply(${board.id}, ${reply.id})" class="badge">Delete</button>
                        </c:if>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>



</div>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>