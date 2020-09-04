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

var mask = new ax5.ui.mask();
var modal = new ax5.ui.modal();

var openModal = function(url, width, height, title) {
	modal.open({
		header: {
	        title: "<i class='fa fa-arrows' aria-hidden='true'></i> "+title,
	        btns: {
	          close: {
	            label: '<i class="fa fa-times-circle" aria-hidden="true"></i> X',
	            onClick: function() {
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

function parseJwt (token) {
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

function postAjax(url, data, contentType, callback) {
	if(contentType == null) {
		contentType = "application/json; charset=utf-8";
	}
	$.ajax({
	    type: "POST",
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
        	if(data.responseJSON.error == "unauthorized"){
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
        	if(data.responseJSON.error == "unauthorized"){
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
        	if(data.responseJSON.error == "unauthorized"){
        		alert("토큰이 만료되었습니다.");
        		location.href = "/static/index.html";
        	}
        }
	});
}