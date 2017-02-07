$(document).ready(function() {

    $("#create-data-resource-form").submit(function(e) {
        e.preventDefault();
        var formJSON = extractForm();
        submit(e, "POST", "/dataResource", formJSON);
    });

    $("#update-data-resource-form").submit(function(e) {
        e.preventDefault();
        var formJSON = extractForm();
        submit(e, "PUT", "/dataResource/" + formJSON.dataResourceID, formJSON);
    });
});


function submit(e, method, url, formObj) {
    $("#error-banner").hide();
    $("#changes-successful").hide();
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data:  JSON.stringify(formObj)
    }).done (function(data, textStatus, jqXHR) {
            showSuccess(data, textStatus, jqXHR);
    }).fail(function (data, textStatus, jqXHR) {
        displayErrors(data, textStatus, jqXHR);
    });
}

function extractForm() {
    var dataResForm = {
        title: $("#title").val(),
        dataResourceID: $("#data-resource-id").val(),
        metadata: $("#metadata-json").val()
    };

    return dataResForm;
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
