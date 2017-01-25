$(document).ready(function() {
    $("#dataset-select").on('change', function() {
        displayCurrentValues();
    });

    $("#format-json-btn").click(function() {
        var jsonMetadata = $('#json-input').val();
        if (jsonMetadata != "") {
            try {
                var jsonObj = JSON.parse(jsonMetadata);
                $('#json-input').val(JSON.stringify(jsonObj, null, '\t'));
                $("#js-json-validation-err").hide();

            } catch (e) {
                if ($("#ss-validation-error").is(":visible")) {
                    // do nothing.
                } else {
                    $("#js-json-validation-err").show();
                }
            }
        }
    });

    $("#reset-btn").click(function() {
        $("#js-json-validation-err").hide();
        $("#ss-validation-error").hide();
        displayCurrentValues();
    })
});

function displayCurrentValues() {
    $("#js-json-validation-err").hide();

    if ($("#changes-successful").is(":visible")) {
        $("#changes-successful").hide("slow");
    }

    if ($('#dataset-select').val() != "") {
        $.ajax({
            url: "/metadata/" + $('#dataset-select').val()
        }).done(function(response) {
            console.log( response );
            if (response.jsonMetadata != "") {
                var jsonObj = JSON.parse(response.jsonMetadata);
                $('#json-input').val(JSON.stringify(jsonObj, null, '\t'));
            }
            $("#major-version").val(response.majorVersion);
            $("#minor-version").val(response.minorVersion);
         });
    } else {
        $('#json-input').val("");
    }
}