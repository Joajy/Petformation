let index = {
    init: function () {
        $("#btn-save").on("click", () => {
            this.save();
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

    save: function () {
        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        }

        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp) {
            if (resp.status == 500) {
                alert("게시글 업로드 중 문제가 발생했습니다.");
                return false;
            }
            alert("게시글이 등록되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    deleteById: function () {
        let id = $("#id").val();

        $.ajax({
            type: "DELETE",
            url: "/api/board/" + id,
            dataType: "json"
        }).done(function (resp) {
            if (resp.status == 500) {
                alert("게시글 삭제 중 문제가 발생했습니다.");
                return false;
            }
            alert("게시글 삭제가 완료되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    update: function () {
        let id = $("#id").val();

        let data = {
            title: $("#title").val(),
            content: $("#content").val(),
        }

        $.ajax({
            type: "PUT",
            url: "/api/board/" + id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp) {
            if (resp.status == 500) {
                alert("게시글 수정 중 문제가 발생했습니다.");
                return false;
            }
            alert("게시글 수정이 완료되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    saveReply: function () {
        let id = $("#boardId").val();
        let data = {
            content: $("#reply-content").val()
        }

        $.ajax({
            type: "POST",
            url: `/api/board/${id}/reply`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp) {
            if (resp.status == 500) {
                alert("댓글 작성 중 문제가 발생했습니다.");
                return false;
            }
            alert("댓글 작성이 완료되었습니다.");
            location.href = `/board/${id}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    deleteReply: function (boardId, replyId) {
        $.ajax({
            type: "DELETE",
            url: `/api/board/${boardId}/reply/${replyId}`,
            dataType: "json"
        }).done(function (resp) {
            if (resp.status == 500) {
                alert("댓글 삭제 중 문제가 발생했습니다.");
                return false;
            }
            alert("댓글이 삭제되었습니다.");
            location.href = `/board/${boardId}`;
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
                recommend.removeClass("btn-outline-success");
                recommend.addClass("btn-success");

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
                recommend.removeClass("btn-success");
                recommend.addClass("btn-outline-success");

                location.reload();
            }).fail(error => {
                console.log(error);
            });
        }
    }
};

index.init();

function selectSearchType() {
    $("#searchType").val($("#select option:selected").val())
}