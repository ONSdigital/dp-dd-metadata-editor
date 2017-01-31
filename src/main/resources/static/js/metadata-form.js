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
                $("#major-version-err").hide();
                $("#minor-version-err").hide();
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
        $("#major-version-err").hide();
        $("#error-banner").hide();
        $("#minor-version-err").hide();
        displayCurrentValues();
    })

    $("#error-banner").click(function() {
        $("#error-banner").hide("slow");
    });

});

function displayCurrentValues() {
    $("#js-json-validation-err").hide();
    $("#ss-validation-error").hide();
    $("#changes-successful").hide();
    $("#error-banner").hide();
    $("#major-version-err").hide();
    $("#minor-version-err").hide();

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
            $("#revision-reason").val(response.revisionReason);
            $("#revision-notes").val(response.revisionNotes);
         }).fail(function(response) {
            $("#error-banner").text(response.responseJSON.message);
            $("#error-banner").show();
         });
    } else {
        $('#json-input').val("");
        $('#major-version').val("");
        $('#minor-version').val("");
        $('#revision-reason').val("");
        $('#revision-notes').val("");
    }
}