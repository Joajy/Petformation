<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div id="wrapper">
	<div id="content" class="container">
		<form>
			<label for="category"><i class="fa-solid fa-pen-nib"></i> Category</label>
			<select id="category" class="form-control">
				<option value="none">자유 게시판</option>
				<option value="secret">비밀 게시판</option>
				<option value="screenshot">스크린샷 게시판</option>
				<option value="question">Q&A</option>
			</select><br>
			<div class="form-group">
				<label for="title"><i class="fa-solid fa-pen-nib"></i> 제목</label> <input type="text" class="form-control" placeholder="Enter title" id="title">
			</div>
			<div class="form-group">
				<label for="board-content"><i class="fa-solid fa-pen-nib"></i> 내용</label>
				<textarea class="form-control summernote" rows="5" id="board-content"></textarea>
			</div>
		</form>
		<div align="right">
			<button id="btn-write" class="btn btn-confirm">
				<i class="fa-solid fa-circle-chevron-down"></i> 작성
			</button>
		</div>
	</div>

<script>
	$('.summernote').summernote({
	  // 에디터 높이
	  height: 600,
	  // 에디터 한글 설정
	  lang: "ko-KR",
	  // 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
	  focus : true,
	  toolbar: [
		    // 글꼴 설정
		    ['fontname', ['fontname']],
		    // 글자 크기 설정
		    ['fontsize', ['fontsize']],
		    // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
		    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
		    // 글자색
		    ['color', ['forecolor','color']],
		    // 표만들기
		    ['table', ['table']],
		    // 글머리 기호, 번호매기기, 문단정렬
		    ['para', ['ul', 'ol', 'paragraph']],
		    // 줄간격
		    ['height', ['height']],
		    // 그림첨부, 링크만들기, 동영상첨부
		    ['insert',['picture','link','video']],
		    // 코드보기, 확대해서보기, 도움말
		    ['view', ['codeview','fullscreen', 'help']]
		],
		  // 추가한 글꼴
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
		 // 추가한 폰트사이즈
		fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
		callbacks: {	//여기 부분이 이미지를 첨부하는 부분
			onImageUpload : function(files) {
				uploadSummernoteImageFile(files[0],this);
			},
			onPaste: function (e) {
				var clipboardData = e.originalEvent.clipboardData;
				if (clipboardData && clipboardData.items && clipboardData.items.length) {
					var item = clipboardData.items[0];
					if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
						e.preventDefault();
					}
				}
			}
		}
	}).on("summernote.enter", function(we, e) {
        $(this).summernote("pasteHTML", "<br><br>");
        e.preventDefault();
    }); //Enter 후에 <br><br> 제거 하는 코드

	/**
	* 이미지 파일 업로드
	*/
	function uploadSummernoteImageFile(file, editor) {
		data = new FormData();
		data.append("file", file);
		$.ajax({
			data : data,
			type : "POST",
			url : "/uploadSummernoteImageFile",
			contentType : false,
			processData : false,
			success : function(data) {
            	//항상 업로드된 파일의 url이 있어야 한다.
				$(editor).summernote('insertImage', data.url, data.filename);
			}
		});
	}
</script>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>