function createErrorMessage(message, id) {
    return $("<p>")
        .addClass("text-danger")
        .attr("id", id)
        .append("An error occurred: " + message);
}

function toggleRunning() {
    var button = $("#toggle-button");
    var container = $("#start-stop");
    if(button.text() === "Start") {
        $.ajax("/start", {
            type: "POST",

            success: function() {
                button.text("Stop");
                button.removeClass("btn-success");
                button.addClass("btn-danger");
                $("#start-error").remove();
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

function updateCanvas() {
    var image = new Image(1280, 720);

    image.onload = function() {
        var im = $("#image-canvas")[0].getContext("2d");
        im.drawImage(image, 0, 0);
        drawZones();
    };

    image.src = "/image";
}

function drawRect(box) {
    var ctx = $("#zone-canvas")[0].getContext("2d");
    ctx.beginPath();
    ctx.rect(box.x, box.y, box.w, box.h);

    ctx.strokeStyle = "#FF0000"; // red lined box
    ctx.fillOpacity = 0;

    ctx.stroke();
}

function drawZones() {
    var zo = $("#zone-canvas")[0].getContext("2d");
    zo.clearRect(0, 0, 1280, 720);
    if(window.editState.dragging) {
        drawRect(window.newRect);
    }
}

/**
 * Immediately invoked on the page loading - used to do one tim
 * setup of necessary page components (e.g. setting the canvas
 * to refresh the camera image every 200ms).
 */
(function() {
    var canvas = $("#zone-canvas");

    window.zones = [];
    window.newRect = {
        x: 0,
        y: 0,
        w: 0,
        h: 0
    };

    window.editState = {
        dragging: false
    };

    canvas.mousedown(function(event) {
        window.editState.dragging = true;
        window.newRect.x = event.offsetX;
        window.newRect.y = event.offsetY;
        window.newRect.h = 0;
        window.newRect.w = 0;
    });

    canvas.mousemove(function(event) {
        if (window.editState.dragging) {
            window.newRect.w =
                event.offsetX - window.newRect.x;
            window.newRect.h =
                event.offsetY - window.newRect.y;
        }
    });

    canvas.mouseup(function(event) {
        window.editState.dragging = false
    });

    window.setInterval(updateCanvas, 150);
    window.setInterval(drawZones, 15);
})();