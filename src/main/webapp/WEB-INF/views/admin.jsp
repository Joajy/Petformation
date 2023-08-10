<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 헤더, 사이드바 레이아웃 설정 -->
<%@ include file="layout/header.jsp"%>
<!-- 헤더, 사이드바 레이아웃 설정 끝 -->

<div id="wrapper">
    <div id="content" class="container">
        <c:if test="${category eq 'user'}">
            <div class="board-title">| 회원 관리</div>

            <!-- 정렬 및 검색 탭 -->
            <form action="/admin" method="GET" class="form-inline bd-highlight justify-content-between">
                <div></div>
                <div>
                    <select id="select" class="form-control" onchange="selectSearchType()">
                        <option value="username">아이디</option>
                        <option value="nickname">닉네임</option>
                    </select>
                    <input type="hidden" name="category" id="category" value="${param.category}">
                    <input type="hidden" name="searchType" id="searchType" value="username">
                    <input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="입력">
                    <button type="submit" class="btn btn-search">
                        <i class="fa-solid fa-magnifying-glass"></i> 검색
                    </button>
                </div>
            </form>
            <!-- 정렬 및 검색 탭 끝 -->

            <!-- 회원 관리 탭 -->
            <table class="table board-table table-hover">
                <thead>
                <tr>
                    <th class="board-table-no">번호</th>
                    <th class="board-table-writer">아이디</th>
                    <th class="board-table-writer">닉네임</th>
                    <th class="board-table-date">이메일</th>
                    <th class="board-table-date">가입일</th>
                    <th class="board-table-no">게시글수</th>
                    <th class="board-table-no">댓글수</th>
                    <th class="board-table-no">관리</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users.content}">
                    <tr>
                        <th>${user.id}</th>
                        <th>${user.username}</th>
                        <th>${user.nickname}</th>
                        <th>${user.email}</th>
                        <th>${user.createDate}</th>
                        <th>
                            <button type="button" class="btn-admin-modal" data-toggle="modal" data-target="#modal" onclick="modalOpen('board', '${user.nickname}', ${user.id})">
                                    ${user.boardCount}
                            </button>
                        </th>
                        <th>
                            <button type="button" class="btn-admin-modal" data-toggle="modal" data-target="#modal" onclick="modalOpen('reply', '${user.nickname}', ${user.id})">
                                    ${user.replyCount}
                            </button>
                        </th>
                        <th><button class="btn btn-admin" onclick="userKick(${user.id})">회원 추방</button></th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <!-- 회원 관리 탭 끝 -->

            <!-- 페이징 -->
            <c:set var="startPage" value="${users.number - users.number % 5}" />
            <ul class="pagination justify-content-center">
                <li class="page-item <c:if test='${users.number < 5}'>disabled</c:if>">
                    <a class="page-link" href="/admin?category=${param.category}&page=${startPage - 5}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"><</a>
                </li>
                <c:forEach var="page" begin="1" end="5">
                    <c:if test="${(startPage + page) <= users.totalPages}">
                        <li class="page-item <c:if test='${users.number eq startPage + page - 1}'>active</c:if>">
                            <a class="page-link" href="/admin?category=${param.category}&page=${startPage + page - 1}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">${startPage + page}</a>
                        </li>
                    </c:if>
                </c:forEach>
                <li class="page-item <c:if test='${startPage + 5 > users.totalPages}'>disabled</c:if>">
                    <a class="page-link" href="/admin?category=${param.category}&page=${startPage + 5}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">></a>
                </li>
            </ul>
            <!-- 페이징 끝 -->
        </c:if>

        <c:if test="${category eq 'summary'}">
            <div class="data">
                <div class="data-summary">
                    <dl>
                        <dt style="color: red;">자유 게시판</dt>
                        <dd>${countNone}</dd>
                    </dl>
                    <dl>
                        <dt style="color: orange;">비밀 게시판</dt>
                        <dd>${countSecret}</dd>
                    </dl>
                    <dl>
                        <dt style="color: green;">스크린샷 게시판</dt>
                        <dd>${countScreenshot}</dd>
                    </dl>
                    <dl>
                        <dt style="color: purple;">질문과 답변</dt>
                        <dd>${countQuestion}</dd>
                    </dl>
                </div>
                <div class="data-summary">
                    <dl>
                        <dt>오늘&nbsp;작성글</dt>
                        <dd>${countToday}</dd>
                    </dl>
                    <dl>
                        <dt>어제&nbsp;작성글</dt>
                        <dd>${countYesterday}</dd>
                    </dl>
                    <dl>
                        <dt>누적&nbsp;작성글</dt>
                        <dd>${countTotal}</dd>
                    </dl>
                </div>

                <br><br>

                <div class="chart-title">최근 7일 통계</div>
                <div id="Line_Controls_Chart">
                    <!-- 라인 차트 생성할 영역 -->
                    <div id="lineChartArea" class="chart"></div>
                    <!-- 컨트롤바를 생성할 영역 -->
                    <div id="controlsArea" style="display: none;"></div>
                </div>
            </div>
        </c:if>
    </div>

    <!-- 게시글 수 모달 -->
    <div class="modal fade" id="modal">
        <div class="modal-dialog modal-lg modal-dialog-scrollable">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title" id="modalTitle"></h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>

                <!-- Modal body -->
                <div class="modal-body" id="modalBody">
                    <table class="table board-table table-hover">
                        <thead id="modalTableHead"></thead>
                        <tbody id="modalTableBody"></tbody>
                    </table>
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="modalClose()">Close</button>
                </div>

            </div>
        </div>
    </div>
    <!-- 게시글 수 모달 끝 -->

    <!-- 스크립트 설정 -->
    <script src="/js/board.js"></script>
    <script src="/js/user.js"></script>
    <script src="/js/admin.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        var chartDrowFun = {

            chartDrow : function(){
                var queryObject = "";
                var queryObjectLen = "";

                $.ajax({
                    type : 'POST',
                    url : '/api/admin/data',
                    dataType : 'json',
                    success : function(data) {
                        queryObject = eval('(' + JSON.stringify(data, null, 2)
                            + ')');
                        queryObjectLen = queryObject.boardList.length;
                        //alert('Total lines : ' + queryObjectLen + 'EA');
                    },
                    error : function(xhr, type) {
                        //alert('server error occoured')
                        alert('server msg : ' + xhr.status)
                    }
                });

                var chartData = '';

                //날짜형식 변경하고 싶으시면 이 부분 수정하세요.
                var chartDateformat 	= 'MM월 dd일';
                //라인차트의 라인 수
                var chartLineCount    = 10;
                //컨트롤러 바 차트의 라인 수
                var controlLineCount	= 10;


                function drawDashboard() {

                    var data = new google.visualization.DataTable();
                    //그래프에 표시할 컬럼 추가
                    data.addColumn('datetime' , '날짜');
                    data.addColumn('number'   , '전체');
                    data.addColumn('number'   , '자유게시판');
                    data.addColumn('number'   , '비밀게시판');
                    data.addColumn('number'   , '스크린샷 게시판');
                    data.addColumn('number'   , '질문과 답변');

                    //그래프에 표시할 데이터
                    var dataRow = [];
                    var dateList = [];

                    for (var i = 0; i < queryObjectLen; i++) {
                        var createDate = queryObject.boardList[i].createDate;
                        var none = queryObject.boardList[i].none;
                        var secret = queryObject.boardList[i].secret;
                        var screenshot = queryObject.boardList[i].screenshot;
                        var question = queryObject.boardList[i].question;

                        dateList.push(new Date(createDate.substring(0, 4), createDate.substring(5, 7) - 1, createDate.substring(8, 10)));
                        dataRow = [new Date(createDate.substring(0, 4), createDate.substring(5, 7) - 1, createDate.substring(8, 10)),
                            none + secret + screenshot + question, none, secret, screenshot, question];
                        data.addRow(dataRow);
                    }

                    var chart = new google.visualization.ChartWrapper({
                        chartType   : 'LineChart',
                        containerId : 'lineChartArea', //라인 차트 생성할 영역
                        options     : {
                            chartArea: {width: '85%', height: '70%'},
                            curveType: 'function',
                            isStacked   : 'percent',
                            focusTarget : 'category',
                            height		  : 500,
                            width			  : '100%',
                            legend		  : { position: "top", textStyle: {fontName: 'nanumsquare', fontSize: 13}},
                            pointSize		: 5,
                            tooltip		  : {textStyle : {fontName: 'nanumsquare', fontSize:12}, showColorCode : true,trigger: 'both'},
                            hAxis			  : {format: chartDateformat, gridlines:{count:chartLineCount,units: {
                                        years : {format: ['yyyy년']},
                                        months: {format: ['MM월']},
                                        days  : {format: ['dd일']},
                                        hours : {format: ['HH시']}}
                                },textStyle: {fontName: 'nanumsquare', fontSize:12},
                                ticks: dateList},
                            vAxis			  : {minValue: 15,viewWindow:{min:0},gridlines:{count:-1}, format: '#', textStyle:{fontName: 'nanumsquare', fontSize:12}},
                            animation		: {startup: true,duration: 1000,easing: 'in' },
                            annotations	: {pattern: chartDateformat,
                                textStyle: {
                                    fontName: 'nanumsquare',
                                    fontSize: 15,
                                    bold: true,
                                    italic: true,
                                    color: '#871b47',
                                    auraColor: '#d799ae',
                                    opacity: 0.8,
                                    pattern: chartDateformat
                                }
                            }
                        }
                    });

                    var control = new google.visualization.ControlWrapper({
                        controlType: 'ChartRangeFilter',
                        containerId: 'controlsArea',  //control bar를 생성할 영역
                        options: {
                            ui:{
                                chartType: 'LineChart',
                                chartOptions: {
                                    chartArea: {'width': '60%','height' : 80},
                                    hAxis: {'baselineColor': 'none', format: chartDateformat, textStyle: {fontName: 'nanumsquare', fontSize:12},
                                        gridlines:{count:controlLineCount,units: {
                                                years : {format: ['yyyy년']},
                                                months: {format: ['MM월']},
                                                days  : {format: ['dd일']},
                                                hours : {format: ['HH시']}}
                                        }}
                                }
                            },
                            filterColumnIndex: 0
                        }
                    });

                    var date_formatter = new google.visualization.DateFormat({ pattern: chartDateformat});
                    date_formatter.format(data, 0);

                    var dashboard = new google.visualization.Dashboard(document.getElementById('Line_Controls_Chart'));
                    window.addEventListener('resize', function() { dashboard.draw(data); }, false); //화면 크기에 따라 그래프 크기 변경
                    dashboard.bind([control], [chart]);
                    dashboard.draw(data);

                }
                google.charts.setOnLoadCallback(drawDashboard);

            }
        }

        $(document).ready(function(){
            google.charts.load('50', {'packages':['line','controls']});
            chartDrowFun.chartDrow(); //chartDrow() 실행
        });
    </script>
    <!-- 스크립트 설정 끝 -->

    <!-- 푸터 레이아웃 설정 -->
    <%@ include file="layout/footer.jsp"%>
    <!-- 푸터 레이아웃 설정 끝 -->