var ERROR_FIELD_IDS = [
    "dataset-id-err", "data-resource-err", "major-version-err",
    "minor-version-err", "ss-validation-error", "js-json-validation-err",
    "revision-reason-err", "revision-notes-err"
    ];

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
                clearErrors();
/*                $("#js-json-validation-err").hide();
                $("#major-version-err").hide();
                $("#minor-version-err").hide();
                $("#ss-validation-error").hide();*/
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
        clearErrors();
        displayCurrentValues();
    })

    $("#error-banner").click(function() {
        $("#error-banner").hide("slow");
    });

});

function displayCurrentValues() {
    clearErrors();
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

function clearErrors() {
    for (var i in ERROR_FIELD_IDS) {
        $("#" + ERROR_FIELD_IDS[i]).hide();
    }
}