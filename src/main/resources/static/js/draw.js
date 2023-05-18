// 清除覆盖物
function removeOverlay() {
    map.clearOverlays();
}

//绘制工具
function draw(e) {
    var arr = document.getElementsByClassName('bmap-btn');
    for (var i = 0; i < arr.length; i++) {
        arr[i].style.backgroundPositionY = '0';
    }
    e.style.backgroundPositionY = '-52px';
    switch (e.id) {
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

    if (drawingType === 'polygon') {
        drawingManager.addEventListener("overlaycomplete", function (e) {
            const polygon = e.overlay;
            var array = [];
            array = polygon.getPath();
            var position = "";
            for (i = 0; i < array.length; i++) {
                position = position + array[i].lng + "," + array[i].lat + ";";
            }
            var locationId = document.getElementById("locationId").getAttribute("value");
            if (window.confirm("是否提交")) {
                $.ajax({
                    url: '/location/add/' + locationId,
                    type: 'post',
                    traditional: true,
                    data: {
                        "position": position,
                    },
                    success: function () {
                        alert("添加成功");
                    },
                    error: function () {
                        alert("添加失败");
                    }
                })
            } else {
                removeOverlay();
            }
            ;
        });
    }
}

//绘制点
function drawPoint(e) {
    var trailList = JSON.parse(e);
    trailList.map(function (trail) {
        var coordinate = trail.coordinate;
        var index = coordinate.indexOf(",");
        var lon = coordinate.substring(0, index);
        var lat = coordinate.substring(index + 1);
        var marker = new BMapGL.Marker(new BMapGL.Point(lon, lat));
        map.addOverlay(marker);
    })
}

//查看围栏
function selectLocation(e) {
    var location = JSON.parse(e);
    var coordinateString = location.coordinate;
    var coordinate = subCoordinate(coordinateString);
    var polygon = new BMapGL.Polygon(coordinate, {strokeColor: "blue", strokeWeight: 2, strokeOpacity: 0.5});
    map.addOverlay(polygon);
}

//反绘制围栏
function drawLocation(e){
    var locationList = JSON.parse(e);
    locationList.map(function (location){
        var coordinateString = location.coordinate;
        if(coordinateString != "" && coordinateString != null){
            var coordinate = subCoordinate(coordinateString);
            var polygon = new BMapGL.Polygon(coordinate, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
            map.addOverlay(polygon);
        }
    })
}

function  drawPath(e) {
    var pathList = JSON.parse(e);
    pathList.map(function (LocationPoints) {
        var pointList = new Array();
        LocationPoints.map(function (locationPoint) {
            var lon = locationPoint.longitude;
            var lat = locationPoint.latitude;
            var point = new BMapGL.Point(lon, lat);
            pointList.push(point);
        });
        var polyline = new BMapGL.Polyline(pointList, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(polyline);
    })
}

//截取坐标
function subCoordinate(e) {
    var coordinate = e;
    var pointList = new Array();
    var is_Loop = true;
    while (is_Loop) {
        var index1 = coordinate.indexOf(",");
        if (index1 >= 0) {
            //截取x
            var string_lon = coordinate.substring(0, index1);
            var lon = parseFloat(string_lon);
            //字符串中首次出现;的位置
            var index2 = coordinate.indexOf(";");
            if (index2 == -1) {
                var string_lat = coordinate.substring(index1 + 1);
                var lat = parseFloat(string_lat);
                var point = new BMapGL.Point(lon, lat);
                pointList.push(point);
                break;
            }
            //截取y
            var string_lat = coordinate.substring(index1 + 1, index2);
            var lat = parseFloat(string_lat);

            var point = new BMapGL.Point(lon, lat);
            pointList.push(point);

            //截取剩余部分重新赋值
            coordinate = coordinate.substring(index2 + 1);
        } else {
            is_Loop = false;
        }
    }
    return pointList;
}
