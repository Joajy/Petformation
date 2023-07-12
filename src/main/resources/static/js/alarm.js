function alarmConfirm(reply_id, board_id) {
    $.ajax({
        type: "PUT",
        url: `/api/confirm/${reply_id}`
    }).done(function() {
        location.href = `/board/${board_id}`;
    }).fail(function(error) {
        console.log(error);
    });
}