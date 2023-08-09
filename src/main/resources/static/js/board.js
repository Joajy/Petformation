let index_board = {
    init: function () {
        $("#btn-write").on("click", () => {
            this.write();
        });
        $("#btn-delete").on("click", () => {
            this.deleteById();
        });
        $("#btn-update").on("click", () => {
            this.update();
        });
        $("#btn-save-reply").on("click", () => {
            this.saveReply();
        });
    },

    write: function () {
        let data = {
            category: $("#category option:selected").val(),
            title: $("#title").val(),
            content: $("#board-content").val()
        }

        if(!data.title || !data.content || data.title.trim() == '') {
            alert("글 제목과 내용을 작성해주세요.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function () {
            alert("게시글이 등록되었습니다.");
            location.href = `/board?category=${data.category}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    deleteById: function () {
        let id = $("#board_id").val();
        let category = $("#category").val();

        $.ajax({
            type: "DELETE",
            url: "/api/board/" + id,
        }).done(function () {
            alert("게시글 삭제가 완료되었습니다.");
            location.href = `/board?category=${category}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    update: function () {
        let id = $("#board_id").val();
        let data = {
            category: $("#category option:selected").val(),
            title: $("#title").val(),
            content: $("#content").val(),
        }

        $.ajax({
            type: "PUT",
            url: "/api/board/" + id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function () {
            alert("게시글 수정이 완료되었습니다.");
            location.href = `/board?category=${data.category}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    saveReply: function () {
        let id = $("#board_id").val();
        let data = {
            content: $("#reply-content").val()
        }

        $.ajax({
            type: "POST",
            url: `/api/board/${id}/reply`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function () {
            alert("댓글 작성이 완료되었습니다.");
            location.href = `/board/${id}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    deleteReply: function (board_id, reply_id) {
        $.ajax({
            type: "DELETE",
            url: `/api/board/${board_id}/reply/${reply_id}`,
            dataType: "json"
        }).done(function () {
            alert("댓글이 삭제되었습니다.");
            location.href = `/board/${board_id}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    recommend: function(board_id, recommend_state) {
        let recommend = $("#btn-recommend");
        if (!recommend_state) {
            $.ajax({
                type: "POST",
                url: `/api/board/${board_id}/recommend`,
                dataType: "json"
            }).done(() => {
                recommend.removeClass("btn-outline-recommend");
                location.reload();
            }).fail(error => {
                console.log(error);
            });
        }
        else {
            $.ajax({
                type: "DELETE",
                url: `/api/board/${board_id}/recommend`,
                dataType: "json"
            }).done(() => {
                recommend.addClass("btn-outline-recommend");
                location.reload();
            }).fail(error => {
                console.log(error);
            });
        }
    }
}

index_board.init();

function selectSearchType() {
    $("#searchType").val($("#select option:selected").val());
}