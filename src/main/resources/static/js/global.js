// 그리도 총건수 표기 커스텀
if(ax5.ui.grid){
	ax5.ui.grid.tmpl.page_status = function(){return '<span>총 {{totalElements}}건</span>';};
}

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
var thirdModal = new ax5.ui.modal();
var commonModal = {};

var ubiprefix = "";
switch (jwt.serverType){
    case "real" :
        ubiprefix = "http://61.97.190.240:8090/ubi4/ubihtml.jsp";
        break;
    case "dev" :
        ubiprefix = "http://61.97.190.240:8090/ubi4/ubihtml.jsp";
        break;
    default :
        ubiprefix = "http://localhost:8090/ubi4/ubihtml.jsp";
}
var openModal = function(url, width, height, title, paramObj, callBack) {
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
    	
    	// commonModal 객체 set
    	commonModal.closeTarget = modal;
    	commonModal.callBack = callBack;
    	commonModal.paramObj = paramObj;
    });
};

var openSecondModal = function(url, width, height, title, paramObj, callBack) {
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
    	
    	// commonModal 객체 set
    	commonModal.closeTarget = secondModal;
    	commonModal.callBack = callBack;
    	commonModal.paramObj = paramObj;
    });
};

var openThirdModal = function(url, width, height, title, paramObj, callBack) {
	thirdModal.open({
		header: {
			title: title,
			btns: {
	        	close: {
	                label: '<i class="fa fa-times-circle" aria-hidden="true"></i>',
	                onClick: function () {
	                	thirdModal.close();
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
    	
    	// commonModal 객체 set
    	commonModal.closeTarget = thirdModal;
    	commonModal.callBack = callBack;
    	commonModal.paramObj = paramObj;
    });
};

function parseJwt (token) {
	if(token == null) {
		if(location.href.search("/static/index.html") == -1) {
//			alert("토큰이 만료되었습니다.");
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
        	if(tokenErrorMsg.indexOf(data.responseJSON.error) > -1) {
//        		alert("토큰이 만료되었습니다.");
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
        	if(tokenErrorMsg.indexOf(data.responseJSON.error) > -1) {
//        		alert("토큰이 만료되었습니다.");
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
        	if(tokenErrorMsg.indexOf(data.responseJSON.error) > -1){
//        		alert("토큰이 만료되었습니다.");
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
        	if(tokenErrorMsg.indexOf(data.responseJSON.error) > -1){
//        		alert("토큰이 만료되었습니다.");
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
        	if(tokenErrorMsg.indexOf(data.responseJSON.error) > -1){
//        		alert("토큰이 만료되었습니다.");
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
        	if(tokenErrorMsg.indexOf(data.responseJSON.error) > -1){
//        		alert("토큰이 만료되었습니다.");
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
	if(event.keyCode != 189 && event.keyCode != 37 && event.keyCode != 39) {
		$(elem).val( $(elem).val().replace(/^(-?)([0-9]*)([^0-9]*)([0-9]*)([^0-9]*)/, '$1$2$4') );
	}
}

// 한글 제거
function exceptKorean(elem){
	$(elem).val($(elem).val().replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g,""));
}

// 계좌번호 (숫자, 하이픈만 허용)
function onlyBkac(elem){
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
	$(elem).val(Number(deleteCommaStr($(elem).val())).toLocaleString('en'));
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

//하이픈 제거 스트링변수용
function deleteHyphenStr(value){
	return value.replace(/-/g, "");
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
		var imgIdx = 1;
		$.each(accessList, function(idx, item){
			if(item.upMenuId != "MENU100" && item.menuType == "FOLDER" && item.useYn == 'Y') {
				html += '<li>';
				html += '  <img src="/static/img/svg/menu_0'+imgIdx+'.svg">';
				html += '	<a>'+item.menuNm+'</a> <!-- 서브메뉴 -->';
				html += '	<div class="sub_menu">';
				html += '		<dl id="'+item.menuId+'"></dl>';
				html += '	</div>';
				html += '</li>';
				imgIdx++;
			}
		});
		$('.menu').html(html);
		
		$.each(accessList, function(idx, item){
			if(item.menuType == "HTML" && item.useYn == 'Y') { 
				html = '<dl><dd><a href="'+item.menuUrl+'" onclick="insertPgmHistory(\''+item.menuUrl+'\');">'+item.menuNm+'</a></dd></dl>';
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
	$("#head_area").load("/static/html/header.html", function(){
		if(jwt.authInfo.indexOf("AUTH100") > -1){
			$(".logo a")[0].href = "/static/html/admin/cm/cm11/CM1101M01.html"
		}
		$("#head_area #title").html(subMenuNm);
	});
	$("#head_area").after('<div class="menu_off"><a class="off_btn"></a></div>');
	$('.off_btn').click(function () {
	    $('#head_area').toggleClass('off');
	    $('#top_area').toggleClass('on');
	    $('#main_area').toggleClass('on');
    });
	// top
	$("#top_area").load("/static/html/top.html", function(){
		$('#topMenu').text(menuNm);
		$('#topSubMenu').text(subMenuNm);
		$("#topUserNm").text(jwt.userNm);
		setMenuAuth();
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
function after30day() {
	  var d = new Date()
	  var dayOfMonth = d.getDate()
	  d.setDate(dayOfMonth + 30)
	  return dateToStr(d)
}

function formatDate(date) {
	var myMonth = date.getMonth() + 1;
	var myWeekDay = date.getDate();

	var addZero = function(num) {
		if (num < 10) {
			num = "0" + num;
		}
		return num;
	}
	var md = addZero(myMonth) + "-" + addZero(myWeekDay);
	return md;
}

function getMonth(type) {
	var now = new Date();
	var nowYear = now.getYear();
	var returnDate;
	if(type == "S") {
		returnDate = new Date(now.getYear(), now.getMonth(), 1);
	} else {
		returnDate = new Date(now.getYear(), now.getMonth() + 1, 0);
	}
	nowYear += (nowYear < 2000) ? 1900 : 0;
	return nowYear + "-" + formatDate(returnDate);
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

 //new Date(json.createDt).format("yyyy-MM-dd");
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth("S") + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};
String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};


// 양수만 입력 
function naturalNumber(elem){
	$(elem).val($(elem).val().replace(/[^0-9]/g,""));
}


// 날짜 입력 (hypen 없이 8숫자입력)
function dateMask(elem){
	
	naturalNumber(elem);
  
	var date = elem.value;

    if (date == "" || date == null || date.length < 5) {
      elem.value = date;
      return;
    }

    var DataFormat = "";
    var RegPhonNum = "";

    // 날짜 포맷(yyyy-mm-dd) 만들기 
    if (date.length <= 6) {
      DataFormat = "$1-$2"; // 포맷을 바꾸려면 이곳을 변경
      RegPhonNum = /([0-9]{4})([0-9]+)/;
    } else if (date.length <= 8) {
      DataFormat = "$1-$2-$3"; // 포맷을 바꾸려면 이곳을 변경
      RegPhonNum = /([0-9]{4})([0-9]{2})([0-9]+)/;
    }

    date = date.replace(RegPhonNum, DataFormat);

    elem.value = date;
	
    // 모두 입력됐을 경우 날짜 유효성 확인
    if (date.length == 10) {

      var isVaild = true;

      if (isNaN(Date.parse(date))) {
        // 유효 날짜 확인 여부
        isVaild = false;
      } else {

        // 년, 월, 일 0 이상 여부 확인
        var date_sp = date.split("-");
        date_sp.forEach(function(sp) {
          if (parseInt(sp) == 0) {
            isVaild = false;
          }
        });

        // 마지막 일 확인
        var last = new Date(new Date(date).getFullYear(), new Date(date).getMonth()+1, 0);
        // 일이 달의 마지막날을 초과했을 경우 다음달로 자동 전환되는 현상이 있음 (예-2월 30일 -> 3월 1일)
        if (parseInt(date_sp[1]) != last.getMonth()+1) {
					var date_sp2 = date_sp.slice(0);
					date_sp2[2] = '01';
					var date2 = date_sp2.join("-");
					last = new Date(new Date(date2).getFullYear(), new Date(date2).getMonth()+1, 0);
				}
        if (last.getDate() < parseInt(date_sp[2])) {
          isVaild = false;
        }
      }

      if (!isVaild) {
        alert("잘못된 날짜입니다. \n다시 입력하세요.");
        elem.value = "";
        elem.focus();
        return;
      }
    }
}

function insertPgmHistory(url) {
	var formData = {
		"id" : jwt.userId,
		"name" : jwt.userNm,
		"pgmId" : url.substr(url.lastIndexOf("/")+1,9)
	}
	postAjax("/admin/cm/cm06/insertPgmHistory", formData, null, function(data){
		
	});
}

function callReport(fileName, arg, width, height) {
	var url = ubiprefix;
	url += "?file="+fileName;
	url += "&arg="+encodeURIComponent(arg);
	if (width ==""){
		width = 900;	
	}
	if (height ==""){
		height = 900;	
	}
	popCenter(url, "report", width, height, "yes");	
}

function popCenter(url, name, width, height, scroll) {
	var str = "height=" + height + ",innerHeight=" + height;
	str += ",width=" + width + ",innerWidth=" + width;
	str += ",status=no,scrollbars=" + scroll;

	if (window.screen)
	{
		var ah = screen.availHeight - 30;
		var aw = screen.availWidth - 10;

		var xc = (aw - width) / 2;
		var yc = (ah - height) / 2;

		str += ",left=" + xc + ",screenX=" + xc;
		str += ",top=" + yc + ",screenY=" + yc;
	}

	return window.open(url, name, str);
}