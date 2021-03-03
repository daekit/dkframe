// 그리도 총건수 표기 커스텀
ax5.ui.grid.tmpl.page_status = function(){return '<span>총 {{totalElements}}건</span>';};

var setCookie = function(name, value, exp) {
	var date = new Date();
	date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
	document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
};

var getCookie = function(name) {
	var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	return value? value[2] : null;
};

var deleteCookie = function (name) {
	var temp = getCookie(name);
	if(temp){
		setCookie(name, temp, 0);
	} 
}

var authorizationToken = getCookie("jwtToken");
var jwt = parseJwt(authorizationToken);
var menuIdx = getCookie("menuIdx");

var mask = new ax5.ui.mask();
var modal = new ax5.ui.modal();
var secondModal = new ax5.ui.modal();

var openModal = function(url, width, height, title) {
	modal.open({
		header: {
			title: title,
	        btns: {
	        	close: {
	                label: '<i class="fa fa-times-circle" aria-hidden="true"></i>',
	                onClick: function () {
	                    modal.close();
	                }
	            }
	        }
	    },
        width: width,
        height: height,
        onStateChanged: function () {
            // mask
            if (this.state === "open") {
                mask.open();
            }
            else if (this.state === "close") {
                mask.close();
            }
        }
    }, function () {
    	var targetEl = this.$["body-frame"];
    	$.get(url, function(data) {    	        
    		targetEl.append(data);
      	});
    });
};


var openSecondModal = function(url, width, height, title) {
	secondModal.open({
		header: {
			title: title,
			btns: {
	        	close: {
	                label: '<i class="fa fa-times-circle" aria-hidden="true"></i>',
	                onClick: function () {
	                	secondModal.close();
	                }
	            }
	        }
	    },
        width: width,
        height: height
    }, function () {
    	var targetEl = this.$["body-frame"];
    	$.get(url, function(data) {    	        
    		targetEl.append(data);
      	});
    });
};

function parseJwt (token) {
	if(token == null) {
		if(location.href.search("/static/index.html") == -1) {
			alert("토큰이 만료되었습니다.");
        	location.href = "/static/index.html";
		} else {
			return;
		}
	}
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};


function selectGridValidation(obj) {
	if(obj.getList("selected").length > 1) {
		alert("한건 만 선택해주세요.");
		return true;
	}
	if(obj.getList("selected").length == 0) {
		alert("선택된 데이터가 없습니다.");
		return true;
	}
}

var tokenErrorMsg = ["unauthorized", "invalid_token"];

function postAjax(url, data, contentType, callback) {
	if(contentType == null) {
		contentType = "application/json; charset=utf-8";
		data = JSON.stringify(data);
	} else if(contentType == "form") {
		contentType = "x-www-form-urlencoded; charset=utf-8";
	} else {
		contentType = contentType;
	}
	$.ajax({
	    type: "POST",
	    url: url,
	    contentType: contentType,
	    data: data,
	    beforeSend: function (request) {
            request.setRequestHeader("Authorization", authorizationToken);
        },
	    success: function(data){
	    	callback(data);
	    },
        error: function (data) {
        	if(tokenErrorMsg.includes(data.responseJSON.error)) {
        		alert("토큰이 만료되었습니다.");
        		location.href = "/static/index.html";
        	}
        }
	});
}

function postAjaxSync(url, data, contentType, callback) {
	if(contentType == null) {
		contentType = "application/json; charset=utf-8";
		data = JSON.stringify(data);
	} else if(contentType == "form") {
		contentType = "x-www-form-urlencoded; charset=utf-8";
	}
	$.ajax({
	    type: "POST",
	    url: url,
	    contentType: contentType,
	    data: data,
	    async: false,
	    beforeSend: function (request) {
            request.setRequestHeader("Authorization", authorizationToken);
        },
	    success: function(data){
	    	callback(data);
	    },
        error: function (data) {
        	if(tokenErrorMsg.includes(data.responseJSON.error)) {
        		alert("토큰이 만료되었습니다.");
        		location.href = "/static/index.html";
        	}
        }
	});
}

function deleteAjax(url, data, contentType, callback) {
	if(contentType == null) {
		contentType = "application/json; charset=utf-8";
	}
	$.ajax({
	    type: "DELETE",
	    url: url,
	    contentType: contentType,
	    data: JSON.stringify(data),
	    beforeSend: function (request) {
            request.setRequestHeader("Authorization", authorizationToken);
        },
	    success: function(data){
	    	callback(data);
	    },
        error: function (data) {
        	if(tokenErrorMsg.includes(data.responseJSON.error)){
        		alert("토큰이 만료되었습니다.");
        		location.href = "/static/index.html";
        	}
        }
	});
}

function putAjax(url, data, contentType, callback) {
	if(contentType == null) {
		contentType = "application/json; charset=utf-8";
	}
	$.ajax({
	    type: "PUT",
	    url: url,
	    contentType: contentType,
	    data: JSON.stringify(data),
	    beforeSend: function (request) {
            request.setRequestHeader("Authorization", authorizationToken);
        },
	    success: function(data){
	    	callback(data);
	    },
        error: function (data) {
        	if(tokenErrorMsg.includes(data.responseJSON.error)){
        		alert("토큰이 만료되었습니다.");
        		location.href = "/static/index.html";
        	}
        }
	});
}

function filePostAjax(url, data, callback) {
	$.ajax({
//		enctype: 'multipart/form-data',
	    type: "POST",
	    url: url,
	    processData: false,
		contentType: false,
	    data: data,
	    beforeSend: function (request) {
            request.setRequestHeader("Authorization", authorizationToken);
        },
	    success: function(data){
	    	callback(data);
	    },
        error: function (data) {
        	if(tokenErrorMsg.includes(data.responseJSON.error)){
        		alert("토큰이 만료되었습니다.");
        		location.href = "/static/index.html";
        	}
        }
	});
}

function filePutAjax(url, data, callback) {
	$.ajax({
//		enctype: 'multipart/form-data',
	    type: "PUT",
	    url: url,
	    processData: false,
		contentType: false,
	    data: data,
	    beforeSend: function (request) {
            request.setRequestHeader("Authorization", authorizationToken);
        },
	    success: function(data){
	    	callback(data);
	    },
        error: function (data) {
        	if(tokenErrorMsg.includes(data.responseJSON.error)){
        		alert("토큰이 만료되었습니다.");
        		location.href = "/static/index.html";
        	}
        }
	});
}

function inputValidation(inputList) {
	var isValid = true;
	$.each(inputList, function(idx, elem){
		if($.trim(elem.value) == ""){
			isValid = false;
			alert("필수값을 입력해 주세요.");
			$(elem).focus();
			return false;
		}
	});
	return isValid;
}

// 숫자만 입력 (음수, 정수 가능.  소수 불가능)
function onlyNumber(elem){
	//	$(elem).val($(elem).val().replace(/[^0-9]/g,""));
	$(elem).val( $(elem).val().replace(/^(-?)([0-9]*)([^0-9]*)([0-9]*)([^0-9]*)/, '$1$2$4') );
}

// 한글 제거
function exceptKorean(elem){
	$(elem).val($(elem).val().replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g,""));
}

// 계좌번호 (숫자, 하이픈만 허용)
function onlBkac(elem){
	$(elem).val($(elem).val().replace(/^[-]|[^0-9-]/gi,""));
}

// 전화번호 포맷 변경
function telNumberFormatter(elem){
	onlyNumber(elem);
	$(elem).val($(elem).val().replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/g,"$1-$2-$3"));
}

// 사업자 등록번호 포맷 변경
function crnFormatter(elem){
	onlyNumber(elem);
	$(elem).val($(elem).val().replace(/(\d{3})(\d{2})(\d{5})/g, "$1-$2-$3"));
}

// 원단위 콤마 추가
function addComma(elem) {
	onlyNumber(elem);
	$(elem).val($(elem).val().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
}

// 콤마 제거
function deleteComma(elem) {
	$(elem).val($(elem).val().replace(/,/g, ""));
}

// 원단위 콤마 추가 스트링변수용
function addCommaStr(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 콤마 제거 스트링변수용
function deleteCommaStr(x) {
    return x.toString().replace(/,/g, "");
}

// 하이픈 제거
function deleteHyphen(elem){
	$(elem).val($(elem).val().replace(/-/g, ""));
}

// 권한에 따른 메뉴 보여주기
function setMenuAuth() {
	var formData = {
		"authInfo" : jwt.authInfo
	}
	postAjax("/selectMenuAuth", formData, null, function(data) {
		checkMenuAuth(data.accessList);
	});
}
	
function checkMenuAuth(accessList) {
		var html = "";
		$.each(accessList, function(idx, item){
			if(item.upMenuId != "MENU100" && item.menuType == "FOLDER" && item.useYn == 'Y') {
				html += '<li>';
				html += '  <img src="/static/img/svg/menu_02.svg">';
				html += '	<a>'+item.menuNm+'</a> <!-- 서브메뉴 -->';
				html += '	<div class="sub_menu">';
				html += '		<dl id="'+item.menuId+'"></dl>';
				html += '	</div>';
				html += '</li>';
			}
		});
		$('.menu').html(html);
		
		$.each(accessList, function(idx, item){
			if(item.menuType == "HTML" && item.useYn == 'Y') {
				html = '<dl><dd><a href="'+item.menuUrl+'">'+item.menuNm+'</a></dd></dl>';
				$("#"+item.upMenuId).append(html);
			}
		});
	}

//로그아웃 
function logoutClick(){
		deleteCookie("jwtToken");
		deleteCookie("menuIdx");
		location.href = "/";
}

//공통코드 검색 함수 
function setCommonSelect(el){
	$.each(el, function(idx, elem){
		var param = {
			"codeKind" : $(elem).data('kind'),
			"codeRprc" : $(elem).data('rprc')
		};
		postAjaxSync("/admin/cm/cm05/selectChildCodeList", param , null,  function(data){
			var optionHtml = '';
			var codeList = data.childCodeList;
			$.each(codeList, function (index, item){
				optionHtml += '<option value='+item.codeId+' data-etc="'+item.codeEtc+'">';
				optionHtml += item.codeNm;
				optionHtml += '</option>';
			});
			$(elem).append(optionHtml);	
		})
	})
}

function mainDefaultLoad(menuNm, subMenuNm) {
	// left
	$("#head_area").load("/static/html/header.html");
	$("#head_area").after('<div class="menu_off"><a class="off_btn"></a></div>');
	$('.off_btn').click(function () {
	    $('#head_area').toggleClass('off');
	    $('#top_area').toggleClass('on');
	    $('#main_area').toggleClass('on');
    });
	setMenuAuth();
	
	// top
	$("#top_area").load("/static/html/top.html", function(){
		$('#topMenu').text(menuNm);
		$('#topSubMenu').text(subMenuNm);
	});
}

function dateToStr(str) {
	var format = new Date(str);
    var year = format.getFullYear();
    var month = format.getMonth() + 1;
    if(month<10) month = '0' + month;
    var date = format.getDate();
    if(date<10) date = '0' + date;
    var hour = format.getHours();
    if(hour<10) hour = '0' + hour;
    var min = format.getMinutes();
    if(min<10) min = '0' + min;
    var sec = format.getSeconds();
    if(sec<10) sec = '0' + sec;
    return year + "-" + month + "-" + date;
}

function lastWeek() {
	  var d = new Date()
	  var dayOfMonth = d.getDate()
	  d.setDate(dayOfMonth - 7)
	  return dateToStr(d)
}

function before30day() {
	  var d = new Date()
	  var dayOfMonth = d.getDate()
	  d.setDate(dayOfMonth - 30)
	  return dateToStr(d)
}

function dateValidation() {
	if($(".input_calendar")[0].value > $(".input_calendar")[1].value) {
		alert("날짜를 확인해주세요");
		$(".input_calendar")[0].value = "";
		return;
	} else {
		$(".datepicker").remove();
	}
}
