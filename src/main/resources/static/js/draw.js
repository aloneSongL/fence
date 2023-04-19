// 清除覆盖物
function removeOverlay() {
    map.clearOverlays();
}

//绘制工具
function draw(e) {
    var arr = document.getElementsByClassName('bmap-btn');
    for(var i = 0; i<arr.length; i++) {
        arr[i].style.backgroundPositionY = '0';
    }
    e.style.backgroundPositionY = '-52px';
    switch(e.id) {
        case 'marker': {
            var drawingType = BMAP_DRAWING_MARKER;
            break;
        }
        case 'polyline': {
            var drawingType = BMAP_DRAWING_POLYLINE;
            break;
        }
        case 'rectangle': {
            var drawingType = BMAP_DRAWING_RECTANGLE;
            break;
        }
        case 'polygon': {
            var drawingType = BMAP_DRAWING_POLYGON;
            break;
        }
        case 'circle': {
            var drawingType = BMAP_DRAWING_CIRCLE;
            break;
        }
    }

    // 进行绘制
    if (drawingManager._isOpen && drawingManager.getDrawingMode() === drawingType) {
        drawingManager.close();
    } else {
        drawingManager.setDrawingMode(drawingType);
        drawingManager.open();
    }

    if(drawingType === 'polygon') {
        drawingManager.addEventListener("overlaycomplete", function(e) {
            const polygon = e.overlay;
            var array = [];
            array = polygon.getPath();
            var position = "";
            for(i = 0;i < array.length;i++) {
                position = position + array[i].lng + "," + array[i].lat + ";";
            }
            var locationId = document.getElementById("locationId").getAttribute("value");
            if(window.confirm("是否提交")){
                $.ajax({
                    url: '/location/add/' + locationId,
                    type: 'post',
                    traditional: true,
                    data: {
                        "position":position,
                    },
                    success:function (){
                        alert("添加成功");
                    },
                    error:function (){
                        alert("添加失败");
                    }
                })
            }else{
                removeOverlay();
            };
        });
    }
}

//绘制点
function drawPoint(e){
    var trailList = JSON.parse(e);
    trailList.map(function (trail) {
        var coordinate = trail.coordinate;
        var index = coordinate.indexOf(",");
        var lon = coordinate.substring(0,index);
        var lat = coordinate.substring(index+1);
        var marker = new BMapGL.Marker(new BMapGL.Point(lon,lat));
        map.addOverlay(marker);
    })

}

