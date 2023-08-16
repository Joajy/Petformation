<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div id="wrapper">
    <div class="container" id="content" >
        <div class="board-style">
            <input type="hidden" id="board_id" name="board_id" value="${board.id}"/>
            <input type="hidden" id="category" name="category" value="${param.category}">
            <div class="d-flex justify-content-between">
                <div>
                    <button class="btn btn-list" onclick="location.href='/board?category=${category}&page=${page}&sort=${sort}&searchType=${searchType}&searchKeyword=${searchKeyword}'">
                        <i class="fa-solid fa-list"></i>목록
                    </button>
                </div>
                <c:if test="${board.user.id == principal.user.id or principal.user.role eq 'ADMIN'}">
                    <div>
                        <button class="btn btn-retouch" onclick="location.href='/board/${board.id}/updateForm'"><i class="fa-solid fa-pen-to-square"></i> 수정</button>
                        <button id="btn-delete" class="btn btn-delete"><i class="fa-solid fa-trash"></i> 삭제</button>
                    </div>
                </c:if>
            </div>
            <br><br>
            <div class="d-flex justify-content-between">
                <c:choose>
                    <c:when test="${board.category eq 'secret'}">
                        <div><i class="fa-solid fa-user"></i> 익명</div><br>
                    </c:when>
                    <c:otherwise>
                        <div><img class="rounded-circle" src="/upload/${board.user.profileImageUrl}" onerror="this.src='/image/profile.jpg'" width="30" height="30"> ${board.user.nickname}</div><br>
                    </c:otherwise>
                </c:choose>
                <div>
                    <i class="fa-solid fa-calendar-days"></i> ${board.createDate} &nbsp;&nbsp;
                    <i class="fa-solid fa-eye"></i> ${board.count} &nbsp;&nbsp;
                    <i class="fa-solid fa-thumbs-up"></i> ${board.recommendCount}
                </div>
            </div>
            <hr>
            <div class="form-group">
                <h3>${board.title}</h3>
            </div>
            <hr>
            <div class="form-group">
                <div>${board.content}</div>
            </div>
            <c:choose>
                <c:when test="${board.recommendState}">
                    <div align="center">
                        <button onClick="index_board.recommend(${board.id}, ${board.recommendState})" class="btn btn-recommend">
                            <i class="fa-regular fa-thumbs-up"></i> 추천 <span>${board.recommendCount}</span>
                        </button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div align="center">
                        <button onClick="index_board.recommend(${board.id}, ${board.recommendState})" class="btn btn-recommend btn-outline-recommend"
                                <c:if test="${board.user.id == principal.user.id}">disabled</c:if>>
                            <i class="fa-regular fa-thumbs-up"></i> 추천 <span>${board.recommendCount}</span>
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>
            <br>
            <div class="card">
                <div class="card-body flex">
                    <textarea id="reply-content" class="form-control" rows="1"></textarea> &nbsp;
                    <button id="btn-save-reply" class="btn btn-comment"><i class="fa-solid fa-check"></i> 등록 </button>
                </div>
            </div>
            <br>
            <div class="card">
                <div class="card-header"><i class="fa-solid fa-comment-dots"></i> 댓글 <span class="reply">[${fn:length(board.replies)}]</span></div>
                <ul id="reply-box" class="list-group">
                    <c:choose>
                        <c:when test="${board.replies.size() > 0}">
                            <c:forEach var="reply" items="${board.replies}">
                                <li id="reply-${reply.id}" class="list-group-item justify-content-between">
                                    <div class="d-flex">
                                        <p class="font-bold">
                                            <c:choose>
                                            <c:when test="${board.category eq 'secret'}">
                                        <p><i class="fa-solid fa-user"></i> 익명</p>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="rounded-circle" src="/upload/${reply.user.profileImageUrl}" onerror="this.src='/image/profile.jpg'" width="30" height="30">
                                            &nbsp;&nbsp;${reply.user.nickname}
                                        </c:otherwise>
                                        </c:choose>
                                        </p>&nbsp;
                                        <div class="create-datetime">(${reply.createDate})</div>
                                    </div>
                                    <div>${reply.content}</div>
                                    <c:if test="${reply.user.nickname == principal.user.nickname}">
                                        <button onClick="index_board.deleteReply(${board.id}, ${reply.id})" class="badge float-right"><i class="fa-solid fa-trash"></i> 삭제</button>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="reply-empty" align="center">댓글이 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
            <br>
            <div class="list-group">
                <a href="/board/${board.nextBoard.id}?page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"
                   class="list-group-item list-group-item-action <c:if test="${empty board.nextBoard}">disabled</c:if>">
                    <span class="font-bold"><i class="fa-solid fa-arrow-up"></i>&nbsp; 다음글</span> │
                    <c:choose>
                        <c:when test="${empty board.nextBoard}">
                            다음글이 없습니다.
                        </c:when>
                        <c:otherwise>
                            <span class="detail-bottom">${board.nextBoard.title}</span>
                        </c:otherwise>
                    </c:choose>
                </a>
                <a href="/board/${board.prevBoard.id}?page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"
                   class="list-group-item list-group-item-action <c:if test="${empty board.prevBoard}">disabled</c:if>">
                    <span class="font-bold"><i class="fa-solid fa-arrow-down"></i>&nbsp; 이전글</span> │
                    <c:choose>
                        <c:when test="${empty board.prevBoard}">
                            이전글이 없습니다.
                        </c:when>
                        <c:otherwise>
                            <span class="detail-bottom">${board.prevBoard.title}</span>
                        </c:otherwise>
                    </c:choose>
                </a>
            </div>
        </div>
    </div>
    <br>

    <script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>