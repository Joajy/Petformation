let index = {
    init: function (){
        $("#btn-save").on("click", () =>{
            this.save();
        });
        $("#btn-update").on("click", () =>{
            this.update();
        });
        $("#btn-find").on("click", () =>{
            this.find();
        });
    },

    save: function () {
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            nickname: $("#nickname").val(),
            email: $("#email").val()
        };

        $.ajax({
            type: "POST",
            url: "/auth/joinProc",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function(resp) {
            if(resp.status == 500){
                alert("중복된 회원가입입니다.");
            }
            if(resp.status == 400) {
                alert("회원가입 입력 정보를 다시 확인해주십시오.");
                if(resp.data.hasOwnProperty('valid_username')){
                    $('#valid_username').text(resp.data.valid_username).css('color', 'red');
                    $('#username').focus();
                }
                else $('#valid_username').text('');

                if(resp.data.hasOwnProperty('valid_password')){
                    $('#valid_password').text(resp.data.valid_password).css('color', 'red');
                    $('#password').focus();
                }
                else $('#valid_password').text('');

                if(resp.data.hasOwnProperty('valid_nickname')){
                    $('#valid_nickname').text(resp.data.valid_nickname).css('color', 'red');
                    $('#nickname').focus();
                }
                else $('#valid_nickname').text('');

                if(resp.data.hasOwnProperty('valid_email')){
                    $('#valid_email').text(resp.data.valid_email).css('color', 'red');
                    $('#email').focus();
                }
                else $('#valid_email').text('');
            }
            else {
                alert("회원가입이 완료되었습니다.");
                location.href = "/";
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },

    update: function () {
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            nickname: $("#nickname").val()
        };

        if(!data.nickname || data.nickname.trim() === "" || !data.password || data.password.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }

        if(!/(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}/.test(data.password)) {
            $("#valid_password").text("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.").css('color', 'red');
            $('#password').focus();
            return false;
        }
        if(!/^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$/.test(data.nickname)) {
            $("#valid_nickname").text("닉네임은 특수문자를 제외한 2~10자리여야 합니다.").css('color', 'red');
            $('#nickname').focus();
            return false;
        }

        $.ajax({
            type: "PUT",
            url: "/user",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp) {
            if(resp.status === 500) {
                $("#valid_nickname").text("이미 사용중인 닉네임입니다.").css('color', 'red');
                $("#nickname").focus();
                return false;
            }
            alert("회원정보 수정이 완료되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    find: function() {
        LoadingWithMask();

        let data = {
            username: $("#username").val(),
            email: $("#email").val()
        };

        $.ajax({
            type: "POST",
            url: "/auth/find",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8"
        }).done(function(resp) {
            if (resp.status == 400) {
                if (resp.data.hasOwnProperty('valid_email')) {
                    $('#valid_email').text(resp.data.valid_email);
                    $('#email').focus();
                } else {
                    $('#valid_email').text('');
                }

                if (resp.data.hasOwnProperty('valid_username')) {
                    $('#valid_username').text(resp.data.valid_username);
                    $('#username').focus();
                } else {
                    $('#valid_username').text('');
                }

                closeLoadingWithMask();
            } else {
                alert("임시 비밀번호가 발송되었습니다.");
                location.href = "/auth/loginForm";
            }
        }).fail(function(error) {
            console.log(error);
        });
    }
}

index.init();

function profileImageUpload(user_id) {
    $("#userProfileImageInput").click();

    $("#userProfileImageInput").on("change", (e) => {
        let f = e.target.files[0];

        if (!f.type.match("image.*")) {
            alert("이미지를 등록해야 합니다.");
            return;
        }

        // FormData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의 key/value 쌍을 담을 수 있음
        let userProfileImageForm = $("#userProfileImageForm")[0];
        let formData = new FormData(userProfileImageForm);

        // 서버에 이미지 전송하기
        $.ajax({
            type: "PUT",
            url: `/api/user/${user_id}/profileImageUrl`,
            data: formData,
            contentType: false,	// x-www-form-urlencoded로 파싱되는 것을 방지
            processData: false,	// contentType을 false로 설정할 경우 QueryString이 자동 설정되는 것을 방지
            enctype: "multipart/form-data",
            dataType: "json"
        }).done(resp => {
            // 사진 전송 성공시 이미지 변경
            let reader = new FileReader();
            reader.onload = (e) => {
                $("#userProfileImage").attr("src", e.target.result);
            }
            reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
        }).fail(error => {
            console.log(error);
        });
    });
}