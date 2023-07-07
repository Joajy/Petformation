<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %>

<div class="container">
    <button class="btn btn-secondary" onclick="history.back()">Back</button>
    <c:if test="${board.user.id == principal.user.id}">
        <a href="/board/${board.id}/updateForm" class="btn btn-warning">Edit</a>
        <button id="btn-delete" class="btn btn-danger">Delete</button>
    </c:if>
    <br/><br/>
    <div class="form-group">
        <h3>${board.title}</h3>
    </div>

    <div class="form-group">
        <div>${board.content}</div>
        <c:choose>
            <c:when test="${board.recommendState}">
                <div style="text-align: center;">
                    <c:choose>
                        <c:when test="${board.user.id != principal.user.id}">
                            <button onClick="index.recommend(${board.id}, ${board.recommendState})"
                                    class="btn btn-success" style="display: inline-block;">
                                추천 <span>${board.recommendCount}</span>
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button onClick="index.recommend(${board.id}, ${board.recommendState})"
                                    class="btn btn-success" style="display: inline-block;" disabled>
                                추천 <span>${board.recommendCount}</span>
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center;">
                    <c:choose>
                        <c:when test="${board.user.id != principal.user.id}">
                            <button onClick="index.recommend(${board.id}, ${board.recommendState})"
                                    class="btn btn-outline-success" style="display: inline-block;">
                                추천 <span>${board.recommendCount}</span>
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button onClick="index.recommend(${board.id}, ${board.recommendState})"
                                    class="btn btn-outline-success" style="display: inline-block;" disabled>
                                추천 <span>${board.recommendCount}</span>
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <span style="float:right">Post Number: ${board.id}</span><br/>
    <span style="float:right">Writer: ${board.userNickname}</span><br/>
    <span style="float:right">Views: ${board.count}</span><br/>

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
    <div class="list-group">
        <a href="/board/${board.nextBoard.id}?page=${param.page}&searchKeyword=${param.searchKeyword}"
           class="list-group-item list-group-item-action <c:if test="${empty board.nextBoard}">disabled</c:if>">
            <span style="font-weight: bold;">Next</span> |
             <c:choose>
                <c:when test="${empty board.nextBoard}">
                    There is no Next Post.
                </c:when>
                <c:otherwise>
                    <span style="color: dodgerblue;">${board.nextBoard.title}</span>
                </c:otherwise>
            </c:choose>
        </a>
        <a href="/board/${board.prevBoard.id}?page=${param.page}&searchKeyword=${param.searchKeyword}"
           class="list-group-item list-group-item-action <c:if test="${empty board.prevBoard}">disabled</c:if>">
            <span style="font-weight: bold;">Previous</span> |
            <c:choose>
                <c:when test="${empty board.prevBoard}">
                    There is no previous Post.
                </c:when>
            <c:otherwise>
                <span style="color: dodgerblue;">${board.prevBoard.title}</span>
            </c:otherwise>
        </c:choose>
        </a>
    </div>
    <br/>
    <div class="card">
        <div class="card-header">Comment List</div>
        <ul id="comment-box" class="list-group">
            <c:forEach var="reply" items="${board.reply}">
                <li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
                    <div>${reply.content}</div>
                    <div class="d-flex">
                        <div class="font-italic">User: ${reply.user.username} &nbsp</div>
                        <br/>
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
<%@ include file="../layout/footer.jsp" %>