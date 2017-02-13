var ERROR_FIELD_IDS = [
    "dataset-id-err", "data-resource-err", "major-version-err",
    "minor-version-err", "ss-validation-error", "js-json-validation-err",
    "revision-reason-err", "revision-notes-err"
    ];

$(document).ready(function() {
    $("#dataset-select").on('change', function() {
        displayCurrentValues();
    });

    $("#update-metadata-form").submit(function(e) {
        e.preventDefault();
        submitForm(e, extractFormValues());
    });


    $("#format-json-btn").click(function() {
        formatJSON();
    });

    $("#reset-btn").click(function() {
        clearErrors();
        displayCurrentValues();
    })

    $("#error-banner").click(function() {
        $("#error-banner").hide("slow");
    });

});

function submitForm(e, form) {
    $.ajax({
            type: "PUT",
            url: "/metadata/" + form.datasetId,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data:  JSON.stringify(form)
        }).done (function(data, textStatus, jqXHR) {
            showSuccess(data, textStatus, jqXHR);
            refresh(jqXHR.getResponseHeader('location'));
            $('html, body').animate({ scrollTop: 0 }, 'fast');
        }).fail(function (data, textStatus, jqXHR) {
            displayErrors(data, textStatus, jqXHR);
        });
}

function formatJSON() {
    var jsonMetadata = $('#json-input').val();
    if (jsonMetadata != "") {
        try {
            var jsonObj = JSON.parse(jsonMetadata);
            $('#json-input').val(JSON.stringify(jsonObj, null, '\t'));
            clearErrors();
        } catch (e) {
            if ($("#ss-validation-error").is(":visible")) {
                // do nothing.
            } else {
                $("#js-json-validation-err").show();
            }
        }
    }
}

function extractFormValues() {
    var metadataForm = {
        datasetId: $("#hidden-dataset-id").text(),
        dataResource: $("#data-res-select").val(),
        majorVersion: $("#major-version").val(),
        majorLabel: $("#major-label").val(),
        minorVersion: $("#minor-version").val(),
        minorVersion: $("#minor-version").val(),
        revisionReason: $("#revision-reason").val(),
        revisionNotes: $("#revision-notes").val(),
        jsonMetadata: $("#json-input").val(),
    };
    return metadataForm;
}

function refresh(url) {
    $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(response) {
        if (response.jsonMetadata != "") {
            var jsonObj = JSON.parse(response.jsonMetadata);
            $('#json-input').val(JSON.stringify(jsonObj, null, '\t'));
        }
        $("#major-version").val(response.majorVersion);
        $("#major-label").val(response.majorLabel);
        $("#minor-version").val(response.minorVersion);
        $("#revision-reason").val(response.revisionReason);
        $("#revision-notes").val(response.revisionNotes);
        $("#data-res-select").val(response.dataResource);
    });
}

function displayCurrentValues() {
    clearErrors();
    $("#changes-successful").hide();
    $("#changes-successful").empty();
    if ($('#dataset-select').val() != "") {
        $.ajax({
            url: "/metadata/" + $('#dataset-select').val(),
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(response) {

            if (response.jsonMetadata != "") {
                var jsonObj = JSON.parse(response.jsonMetadata);
                $('#json-input').val(JSON.stringify(jsonObj, null, '\t'));
            }

            $("#major-version").val(response.majorVersion);
            $("#minor-version").val(response.minorVersion);
            $("#revision-reason").val(response.revisionReason);
            $("#revision-notes").val(response.revisionNotes);
            $("#data-res-select").val(response.dataResource);
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
    $("#errors").hide();
    $("#errors").empty();
    $("#js-json-validation-err").hide();
}

function displayErrors(data, textStatus, jqXHR) {
    if (data.status == 500) {
        $("#error-banner").text(data.responseJSON.message);
        $("#error-banner").show();

    } else {
        var responseErrors = data.responseJSON.errors
        $("#errors").empty();
        for (var i in responseErrors) {
            console.log("<div id=\"" + responseErrors[i].code + "\">" + responseErrors[i].message + "</div>");
            $("#errors").append("<div id=\"" + responseErrors[i].code + "\">" + responseErrors[i].message + "</div>");
        }
        $("#errors").show();
    }
}

function showSuccess(data, textStatus, jqXHR) {
    $("#changes-successful").empty();
    $("#changes-successful").append(data.message)
    $("#changes-successful").show();
    $("#errors").hide();
    $("#errors").empty();
}