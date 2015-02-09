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
        var ctx = $("#zone-canvas")[0].getContext("2d");
        ctx.drawImage(image, 0, 0);
    };

    image.src = "/image";
}

(function() {
    window.setInterval(updateCanvas, 200);
})();