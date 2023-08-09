<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div id="wrapper">
    <div class="container">
        <form>
            <select id="category" class="form-control">
                <option value="none">자유게시판</option>
                <option value="secret">비밀게시판</option>
                <option value="screenshot">스크린샷 게시판</option>
                <option value="question">질문과 답변</option>
            </select><br>
            <input type="hidden" id="board_id" value="${board.id}">
            <div class="form-group">
                <label for="title"><i class="fa-solid fa-pen-nib"></i> 제목</label>
                <input value="${board.title}" type="text" class="form-control" placeholder="Enter title" id="title">
            </div>
            <div class="form-group">
                <label for="content"><i class="fa-solid fa-pen-nib"></i> 내용</label>
                <textarea class="form-control summernote" rows="5" id="content">${board.content}</textarea>
            </div>
        </form>
        <button id="btn-update" class="btn btn-primary"><i class="fa-solid fa-check"></i> 완료</button>
    </div>

    <script>
        $('.summernote').summernote({
            tabsize : 2,
            height : 300
        });
    </script>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>