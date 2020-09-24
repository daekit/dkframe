var setCookie = function(name, value, exp) {
	var date = new Date();
	date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
	document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
};

var getCookie = function(name) {
	var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	return value? value[2] : null;
};

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

function fileAjax(url, data, callback) {
	$.ajax({
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



function onlyNumber(elem){
	 $(elem).val($(elem).val().replace(/[^0-9]/g,""));
}