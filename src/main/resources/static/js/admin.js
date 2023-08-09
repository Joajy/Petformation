function userKick(user_id) {
	$.ajax({
		type: "DELETE",
		url: `/api/user/delete/${user_id}`
	}).done(function() {
		alert("회원을 삭제하였습니다.");
		location.reload();
	}).fail(function(error) {
		console.log(error);
	});
}

function modalOpen(type, nickname, user_id) {
	if(type == "board") {
		$("#modalTitle").text(nickname + "님의 게시물");
		
		let item = 
			`<tr>
				<th class="board-table-no">번호</th>
				<th class="board-table-title">제목</th>
				<th class="board-table-date">작성일</th>
				<th class="board-table-view">조회수</th>
				<th class="board-table-recommend">추천수</th>
			</tr>`
		
		$.ajax({
		url: `/api/board/${user_id}`,
		dataType: "json"
		}).done(resp => {
			if(resp.length > 0) {
				resp.forEach((board) => {
					item += getBoardModalItem(board);
				});
			} else {
				item = '<div class="board-empty" align="center">등록한 게시글이 없습니다.</div>';
			}
			$("#modalTableBody").append(item);
		}).fail(error => {
			console.log(error);
		});
	} else {
		$("#modalTitle").text(nickname + "님의 댓글");
		
		let item = 
			`<tr>
				<th class="board-table-no">번호</th>
				<th class="board-table-title">내용</th>
				<th class="board-table-date">작성일</th>
			</tr>`
		
		$.ajax({
		url: `/api/reply/${user_id}`,
		dataType: "json"
		}).done(resp => {
			if(resp.length > 0) {
				resp.forEach((reply) => {
					item += getReplyModalItem(reply);
				});
			} else {
				item = '<div class="board-empty" align="center">등록한 댓글이 없습니다.</div>';
			}
			$("#modalTableBody").append(item);
		}).fail(error => {
			console.log(error);
		});
	}
}

function getBoardModalItem(board) {
	let item =
		`<tr onclick="location.href='/board/${board.id}/'">
			<th>${board.id}</th>				
			<th class="board-table-title">${board.title}</th>
			<th>${board.createDate}</th>
			<th>${board.views}</th>
			<th>${board.recommends}</th>
		</tr>`;

	return item;
}

function getReplyModalItem(reply) {
	let item = 
		`<tr>
			<th>${reply.id}</th>				
			<th class="board-table-title">${reply.content}</th>
			<th>${reply.createDate}</th>
		</tr>`;

	return item;
}

function modalClose() {
	$("#modalTableHead > tr").remove();
	$("#modalTableBody > tr").remove();
	
	$("#modalTableHead > div").remove();
	$("#modalTableBody > div").remove();
}