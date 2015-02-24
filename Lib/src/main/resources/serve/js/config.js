function createErrorMessage(message, id) {
    return $("<p>")
        .addClass("text-danger")
        .attr("id", id)
        .append("An error occurred: " + message);
}

function toggleRunning() {
    var button = $("#toggle-button");
    var container = $("#start-stop");
    var canvas = $("#image-canvas");
    var ctx = canvas[0].getContext('2d');
    if(button.text() === "Start") {
        $.ajax("/start", {
            type: "POST",

            success: function() {
                button.text("Stop");
                button.removeClass("btn-success");
                button.addClass("btn-danger");
                $("#start-error").remove();
                $(".stop-hidden").removeClass("hidden");
                window.shouldDrawZones = true;
            },

            error: function() {
                if($("#start-error").length == 0) {
                    var errorMessage = createErrorMessage("Starting the system.", "start-error");
                }
                container.append(errorMessage);
            }
        });
    } else if(button.text() === "Stop") {
        $.ajax("/stop", {
            type: "POST",

            success: function() {
                button.text("Start");
                button.removeClass("btn-danger");
                button.addClass("btn-success");
                $("#stop-error").remove();
                setTimeout(function() { ctx.clearRect(0, 0, 1280, 720); }, 100);
                $(".stop-hidden").addClass("hidden");
                window.shouldDrawZones = false;
            },

            error: function() {
                if($("#start-error").length == 0) {
                    var errorMessage = createErrorMessage("Stopping the system.", "start-error");
                }
                container.append(errorMessage);
            }
        });
    }
}

function addZone() {
    window.editState.editing = true;
    $("#name-entry-container").removeClass("hidden");
}

function confirmZone() {
    window.editState.editing = false;
    var entry = $("#name-entry");
    var canvas = $("#zone-canvas");
    var newZone = {
        id: entry.val(),
        x: window.newRect.x / canvas.width(),
        y: window.newRect.y / canvas.height(),
        w: window.newRect.w / canvas.width(),
        h: window.newRect.h / canvas.height()
    };
    window.zones.push(newZone);
    resetZoneInput();
}

function cancelZone() {
    resetZoneInput();
}

function saveZones() {
    $.ajax("/zones", {
       type: "POST",

       data: JSON.stringify(window.zones),

       error: function() {
           console.log("Could not save zones.");
       }
    });
}

function saveType() {
    var entry = $("#type-entry");

    $.ajax("/animal", {
       type: "POST",

       data: JSON.stringify(entry.val())
    });
}
function resetZoneInput() {
    var entry = $("#name-entry");
    entry.val("");
    window.editState.editing = false;
    window.newRect = {
        x: 0,
        y: 0,
        w: 0,
        h: 0
    };
    $("#name-entry-container").addClass("hidden");
}

function updateCanvas() {
    var image = new Image(1280, 720);

    image.onload = function() {
        var im = $("#image-canvas")[0].getContext("2d");
        im.drawImage(image, 0, 0);
        drawZones();
    };

    var d = new Date();
    image.src = "/image?d=" + d.getMilliseconds();
}

function drawRect(box, color) {
    var ctx = $("#zone-canvas")[0].getContext("2d");
    ctx.beginPath();
    ctx.rect(box.x, box.y, box.w, box.h);

    ctx.strokeStyle = color; // red lined box
    ctx.fillOpacity = 0;

    ctx.stroke();
}

function drawZones() {
    var zo = $("#zone-canvas")[0].getContext("2d");
    zo.clearRect(0, 0, 1280, 720);
    if(window.shouldDrawZones) {
        drawRect(window.newRect, "#00FF00");
        for (var i = 0; i < window.zones.length; i++) {
            var zone = window.zones[i];
            var rect = {
                x: zone.x * zo.canvas.width,
                y: zone.y * zo.canvas.height,
                w: zone.w * zo.canvas.width,
                h: zone.h * zo.canvas.height
            };
            drawRect(rect, "#0000FF");
        }
    }
}

function clearZones() {
    window.zones = [];
}

/**
 * Immediately invoked on the page loading - used to do one tim
 * setup of necessary page components (e.g. setting the canvas
 * to refresh the camera image every 200ms).
 */
(function () {
    var canvas = $("#zone-canvas");

    window.zones = [];
    window.shouldDrawZones = $("#toggle-button").text() === "Stop";

    if(window.shouldDrawZones) {
        $(".stop-hidden").removeClass("hidden");
    } else {
        $(".stop-hidden").addClass("hidden");
    }

    $.ajax("/getzones", {
        type: "GET",

        success: function (data) {
            var zoneList = $.parseJSON(data);

            for (var i = 0; i < zoneList.length; ++i) {
                console.log(zoneList[i]);
                var newZone = {
                    x: zoneList[i].area.centre.x,
                    y: zoneList[i].area.centre.y,
                    w: zoneList[i].area.width,
                    h: zoneList[i].area.height,
                    id: zoneList[i].id
                };
                window.zones.push(newZone);
            }
        }
    });

    window.newRect = {
        x: 0,
        y: 0,
        w: 0,
        h: 0
    };

    window.editState = {
        dragging: false,
        editing: false
    };

    canvas.mousedown(function(event) {
        if(window.editState.editing) {
            window.editState.dragging = true;
            window.newRect.x = event.offsetX;
            window.newRect.y = event.offsetY;
            window.newRect.h = 0;
            window.newRect.w = 0;
        }
    });

    canvas.mousemove(function(event) {
        if (window.editState.dragging && window.editState.editing) {
            window.newRect.w =
                event.offsetX - window.newRect.x;
            window.newRect.h =
                event.offsetY - window.newRect.y;
        }
    });

    canvas.mouseup(function(event) {
        if(window.editState.editing) {
            window.editState.dragging = false
        }
    });

    window.setInterval(updateCanvas, 250);
    window.setInterval(drawZones, 15);
})();