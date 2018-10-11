/**
 * 图片浏览插件
 */
function VieViewer(src, div, width, height) {
		this.view = $(div);
    	this.rate = 1.2;
    	this.maxScale = 2.2;
    	this.minScale = 0.5;
    	this.angle = 0;
    	this.src = src;
    	
    	if (this.view) {
    		this.view.addClass('vie-viewer');
    		this.view.height(height);
    		this.view.height(width);
    		this.wrapper = $('<div class="view-wrapper"></div>');
    		this.view.append(this.wrapper);
    		this.view.append('<div class="view-footer"><ul class="view-tool"><li class="view-rotate-left"></li><li class="view-rotate-right"></li></ul></div>');
//    		this.view.append('<div class="view-close"></div>');
    	}
    	
    	this.bindRotate = function() {
    		var _this = this;
    		this.view.find('.view-rotate-left').unbind('click').on('click', function(){
    			_this.rotate(-90);
    		});
    		this.view.find('.view-rotate-right').unbind('click').on('click', function(){
    			_this.rotate(90);
    		});
    	}
    	this.bindWheelScroll = function() {
    		var _this = this;
    		this.canvas.unbind('mousewheel').on('mousewheel', function(event, delta) {
    			var oriEvent = event.originalEvent;
    		    if (oriEvent.deltaY > 0) {
    		    	_this.zoomOut();
    		    } else if (oriEvent.deltaY < 0) {
    		    	_this.zoomIn();
    		    }
    		});
    	}
    	this.bindMoveEvent = function() {
    		var _this = this;
    		this.canvas.unbind('mousedown').on('mousedown', function(downEvent){
    			if (downEvent.button != 0) {// 不是鼠标左键
    				return true;
    			}
    		    var _thisEl = $(this);//获取到控件
    		    var oriLeft = _thisEl.offset ().left;// 左侧边界位置
    		    var oriTop = _thisEl.offset().top;
    		    var oriWidth = _thisEl.width();
    		    var oriHeight = _thisEl.height();
    		    var oriRight = oriLeft + oriWidth;
    		    var oriBottom = oriTop + oriHeight;
    		    var oriMarginLeft = parseInt(_thisEl.css('marginLeft'));
    		    var oriMarginTop = parseInt(_thisEl.css('marginTop'));

    		    var _parentEl = _thisEl.parent();;
    		    var pLeft = _parentEl.offset().left;
    		    var pTop = _parentEl.offset().top;
    		    var pWidth = _parentEl.width();
    		    var pHeight = _parentEl.height();
    		    var pRight = pLeft + pWidth;
    		    var pBottom = pTop + pHeight;

    		    var startPosX = downEvent.pageX;// 鼠标起始位置
    		    var startPosY = downEvent.pageY;
    		    var startTime = new Date().getTime();
    		    // 右侧边界位置
    		    var $maskLaver = $('<div class="move-mask-layer"></div>');
    		    _parentEl.append($maskLaver);
    		    
    		    $maskLaver.on('mousemove', function (moveEvent) {
    		        var px = moveEvent.pageX;
    		        var py = moveEvent.pageY;
    		        //获取移动的宽度
    		        var moveX = moveEvent.pageX - startPosX;
    		        var moveY = moveEvent.pageY - startPosY;
    		        
    		        if (moveX + oriLeft > pLeft) {
    		        	moveX = pLeft - oriLeft;
    		        }
    		        if (moveX + oriRight < pRight) {
    		        	moveX = pRight - oriRight;
    		        }
    		        if (moveY + oriTop > pTop) {
    		        	moveY = pTop - oriTop;
    		        }
    		        if (moveY + oriBottom < pBottom) {
    		        	moveY = pBottom - oriBottom;
    		        }
    		        if (oriWidth < pWidth) {
    		        	moveX = 0;
    		        }
    		        if (oriHeight < pHeight) {
    		        	moveY = 0;
    		        }
    		        var marginLeft = oriMarginLeft + moveX;
    		        var marginTop = oriMarginTop + moveY;

    		        _thisEl.css('marginLeft', marginLeft + 'px');
    		        _thisEl.css('marginTop', marginTop + 'px');
    		        
    		        //最后返回false;
    		        return false;
    		    });
    		    //鼠标松开清空所有事件
    		    $maskLaver.on('mouseup', function (upEvent) {
    		        $maskLaver.unbind('mousemove');
    		        $maskLaver.unbind('mouseup');
    		        window.onmousemove = null;
    		        window.onmouseup = null;
    		        
    		        var layer = _parentEl.find('.move-mask-layer');
    		        _parentEl.find('.move-mask-layer').remove();
    		        
    		    });
    		    return false;
    		});
    	}
    	this.refreshCanvas = function() {
    		var $childs = this.wrapper.children('canvas');
        	if ($childs.length > 0) {
        		this.canvas = $childs.eq(0);
        	}
        	var $canvas = $('<canvas>当前浏览器不支持canvas标签</canvas>');
        	if (this.canvas) {
        		$canvas.attr('class', this.canvas.attr('class'));
        		$canvas.attr('style', this.canvas.attr('style'));
        		$canvas.attr('id', this.canvas.attr('id'));
        		this.canvas.remove();
        		this.canvas = null;
        	}
        	this.wrapper.append($canvas);
        	this.canvas = $canvas;
    	}
    	this.drawImage = function() {
    		var imgWidth = this.img.width;
			var imgHeight = this.img.height;
			var width = imgWidth;
			var height = imgHeight;
			var l1 = width;
			var sin1 = 0;
			var cos1 = 1;
			var l2 = Math.sqrt(width * width + height * height);
			var sin2 = height / l2;
			var cos2 = width / l2;
			var l3 = height;
			var sin3 = 1;
			var cos3 = 0;
			var sina = Math.sin(Math.PI * this.angle / 180);
			var cosa = Math.cos(Math.PI * this.angle / 180);
			var x1 = l1 * (cosa * cos1 - sina * sin1);
			var y1 = l1 * (sina * cos1 + cosa * sin1);
			var x2 = l2 * (cosa * cos2 - sina * sin2);
			var y2 = l2 * (sina * cos2 + cosa * sin2);
			var x3 = l3 * (cosa * cos3 - sina * sin3);
			var y3 = l3 * (sina * cos3 + cosa * sin3);
			var minx = Math.min(0, Math.min(Math.min(x1, x2), x3));
			var maxx = Math.max(0, Math.max(Math.max(x1, x2), x3));
			var miny = Math.min(0, Math.min(Math.min(y1, y2), y3));
			var maxy = Math.max(0, Math.max(Math.max(y1, y2), y3));
			
			if (!this.scale) {
				var wrapHeight = this.wrapper.height();
				this.scale = wrapHeight / (maxy - miny);
				if (this.maxScale < this.scale) {
					this.maxScale = this.scale;
					this.minScale = this.maxScale / Math.pow(this.rate, 7); 
				}
				if (this.minScale > this.scale) {
					this.minScale = this.scale;
					this.maxScale = this.minScale * Math.pow(this.rate, 7); 
				}
			}
			
			var h = (maxy - miny) * this.scale;
			var w = (maxx - minx) * this.scale;
			this.canvas.get(0).height = h;
			this.canvas.get(0).width = w;
			
			var context = this.canvas.get(0).getContext('2d');
			context.scale(this.scale, this.scale);
			context.translate(-minx, -miny);
			context.rotate(Math.PI * this.angle / 180);
			context.drawImage(this.img, 0, 0);
			
			var oriMarginLeft = parseInt(this.canvas.css('marginLeft'));
		    var oriMarginTop = parseInt(this.canvas.css('marginTop'));
		    
		    var pWidth = this.canvas.parent().width();
		    var pHeight = this.canvas.parent().height();
    	}
    	this.load = function(callback) {
    		this.img = new Image();
    		var _this = this;
    		this.img.onload = function() {
    			_this.drawImage();
    			_this.bindMoveEvent();
    			_this.bindWheelScroll();
    			_this.bindRotate();
    			if (callback) {
    				callback();
    			}
    		}
    		this.img.src = this.src;
    	}
    	this.adjustMargin = function() {
    		var moveX = 0, moveY = 0;
    		
    		var _thisEl = this.canvas;//获取到控件
    		var _parentEl = _thisEl.parent();;
    		
    		var oriMarginLeft = parseInt(_thisEl.css('marginLeft'));
		    var oriMarginTop = parseInt(_thisEl.css('marginTop'));
    		
		    var oriLeft = _thisEl.offset ().left;// 左侧边界位置
		    var oriTop = _thisEl.offset().top;
		    var oriWidth = _thisEl.width();
		    var oriHeight = _thisEl.height();
		    var oriRight = oriLeft + oriWidth;
		    var oriBottom = oriTop + oriHeight;
		    var pLeft = _parentEl.offset().left;
		    var pTop = _parentEl.offset().top;
		    var pWidth = _parentEl.width();
		    var pHeight = _parentEl.height();
		    var pRight = pLeft + pWidth;
		    var pBottom = pTop + pHeight;
		    
	        if (moveX + oriLeft > pLeft) {
	        	moveX = pLeft - oriLeft;
	        }
	        if (moveX + oriRight < pRight) {
	        	moveX = pRight - oriRight;
	        }
	        if (moveY + oriTop > pTop) {
	        	moveY = pTop - oriTop;
	        }
	        if (moveY + oriBottom < pBottom) {
	        	moveY = pBottom - oriBottom;
	        }
	        if (oriWidth < pWidth) {
	        	moveX = 0;
	        }
	        if (oriHeight < pHeight) {
	        	moveY = 0;
	        }
	        var marginLeft = oriMarginLeft + moveX;
	        var marginTop = oriMarginTop + moveY;
	        
	        _thisEl.css('marginLeft', marginLeft + 'px');
	        _thisEl.css('marginTop', marginTop + 'px');
    	}
    	this.reload = function() {
    		this.refreshCanvas();
    		var _this = this;
    		this.load(function(){
    			_this.adjustMargin();
    		});
    	}
    	this.toBlob = function(func) {
    		var cacheCanvas = this.canvas;
    		
    		var copyViewer = new VieViewer();
    		for (var attr in this) {
    			if (this[attr] instanceof Function) continue;
    			copyViewer[attr] = this[attr];
    		}
        	var $canvas = $('<canvas></canvas>');
       		$canvas.attr('class', this.canvas.attr('class'));
       		$canvas.attr('style', this.canvas.attr('style'));
       		$canvas.css('display', 'none');
       		
       		copyViewer.canvas = $canvas;
       		
    		copyViewer.img = new Image();
    		copyViewer.img.onload = function() {
    			copyViewer.drawImage();
    			copyViewer.canvas.get(0).toBlob(func);
    		}
    		copyViewer.img.src = copyViewer.src;
    	}
    	
    	this.rotate = function(rat) {
    		this.angle = this.angle + rat;
    		var cacheScale = this.scale;
    		this.scale = 1;
    		var _this = this;
    		this.toBlob(function(blob) {
    			_this.scale = cacheScale;
	    		_this.angle = 0;
    			_this.src = window.URL.createObjectURL(blob);
	    		_this.reload();
    		});
    	}
    	this.zoomIn = function() {
    		if (this.scale * this.rate > this.maxScale 
    				|| this.scale * this.rate < this.minScale) {
	    		return false;
    		}
    		this.scale = this.scale * this.rate;
    		this.reload();
    		
   			return true;
    	}
    	this.zoomOut = function() {
    		if (this.scale / this.rate > this.maxScale 
    				|| this.scale / this.rate < this.minScale) {
	    		return false;
    		}
    		this.scale = this.scale / this.rate;
    		this.reload();
    		
    		return true;
    	}
    	this.refreshCanvas();
    }