var setCookie = function(name, value, exp) {
	var date = new Date();
	date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
	document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
};

var getCookie = function(name) {
	var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	return value? value[2] : null;
};

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

