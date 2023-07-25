<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<div class="bg-dark" id="sidebar" align="center">
		<form id="userProfileImageForm">
			<input type="file" name="profileImageFile" id="userProfileImageInput" style="display: none;"/>
		</form>
		<c:choose>
			<c:when test="${not empty principal}">
				<img id="userProfileImage" src="/upload/${principal.user.profile_image_url}" onerror="this.src='/image/profile.jpg'" class="rounded-circle profile"
					onclick="profileImageUpload(${principal.user.id})" style="cursor: pointer;">
				<div class="font-bold">${principal.user.nickname}</div>
			</c:when>
			<c:otherwise>
				<img id="userProfileImage" src="/image/profile.jpg" class="rounded-circle profile">
				<div>비로그인 상태입니다.</div>
			</c:otherwise>
		</c:choose>
		<br>
		<ul class="category-ul">
			<c:if test="${principal.user.role eq 'ADMIN'}">
				<li class="category-li" onclick="location.href='/admin?category=user'"><i class="fa-solid fa-link"></i>&nbsp; 회원 관리</li>
				<li class="category-li" onclick="location.href='/admin?category=summary'"><i class="fa-solid fa-link"></i>&nbsp; 데이터 관리</li>
			</c:if>
			<li class="category-li" onclick="location.href='/board?category=none'"><i class="fa-solid fa-link"></i>&nbsp; 자유게시판</li>
			<li class="category-li" onclick="location.href='/board?category=popular'"><i class="fa-solid fa-link"></i>&nbsp; 인기게시판</li>
			<li class="category-li" onclick="location.href='/board?category=secret'"><i class="fa-solid fa-link"></i>&nbsp; 비밀게시판</li>
			<li class="category-li" onclick="location.href='/board?category=screenshot'"><i class="fa-solid fa-link"></i>&nbsp; 스크린샷 게시판</li>
			<li class="category-li" onclick="location.href='/board?category=question'"><i class="fa-solid fa-link"></i>&nbsp; 질문과 답변</li>
		</ul>
	</div>
	<div class="overlay" onclick="sidebarHide()"></div>