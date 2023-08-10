package com.Kim.community.controller.api;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

	@PostMapping("/api/admin/data")
	public ResponseEntity<?> alarmConfirm() {

		Connection connection = null;
		JSONObject responseObj = new JSONObject();
		
		try {
			String url = "jdbc:mysql://localhost:3306/blog?serverTimezone=Asia/Seoul";
			String user = "root";
			String passwd = "1234";
		
			// call driver and connect connection
			connection = DriverManager.getConnection(url, user, passwd);

			// Object that include JSON data of DB, and will include in responseObject
			List<JSONObject> boardList = new LinkedList<>();

			String query = "select date(create_date) as 'cd', "
					+ "count(case when category = 'none' then 0 end) as 'none', "
					+ "count(case when category = 'secret' then 0 end) as 'secret', "
					+ "count(case when category = 'screenshot' then 0 end) as 'screenshot', "
					+ "count(case when category = 'question' then 0 end) as 'question' "
					+ "from board where create_date between date_add(now(), interval - 1 week) and now() group by cd "
					+ "order by cd";
			PreparedStatement prepareStatement = connection.prepareStatement(query);
			ResultSet rs = prepareStatement.executeQuery(query);

			// make JSON to response ajax
			//가변 "" 이니셜라이저 null은 불필요하다는 메시지 발생
			JSONObject lineObj = null;

			while (rs.next()) {
				String create_date = rs.getString("cd");
				int none = rs.getInt("none");
				int secret = rs.getInt("secret");
				int screenshot = rs.getInt("screenshot");
				int question = rs.getInt("question");

				lineObj = new JSONObject();
				lineObj.put("create_date", create_date);
				lineObj.put("none", none);
				lineObj.put("secret", secret);
				lineObj.put("screenshot", screenshot);
				lineObj.put("question", question);

				boardList.add(lineObj);
			}
			responseObj.put("boardList", boardList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new ResponseEntity<>(responseObj, HttpStatus.OK);
	}
}
