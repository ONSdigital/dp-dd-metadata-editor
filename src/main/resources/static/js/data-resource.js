$(document).ready(function() {

   $("#data-resource-form").submit(function(e) {
        createDataResource(e);
   });

  $("#data-resource-form").submit(function(e) {
       updateDataResource(e);
  });
});


function createDataResource(e) {
    var myForm = { title: $("#title").val(), dataResourceID: $("#data-resource-id").val(), metadata: $("#metadata-json").val()};
    e.preventDefault();

        $.ajax({
            type: "POST",
            url: "/dataResource",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data:  JSON.stringify(myForm)
        }).done (function(data) {
                $("#changes-successful").show();
        }).fail(function (data) {
            var responseErrors = data.responseJSON.errors
            $("#errors").empty();
            for (var i in responseErrors) {
                console.log("<div id=\"" + responseErrors[i].code + "\">" + responseErrors[i].message + "</div>");
                $("#errors").append("<div id=\"" + responseErrors[i].code + "\">" + responseErrors[i].message + "</div>");
            }
            $("#errors").show();
        });
}

function updateDataResource(e) {
    var myForm = extractForm();
    e.preventDefault();

        $.ajax({
            type: "PUT",
            url: "/dataResource/" + myForm.dataResourceID,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data:  JSON.stringify(myForm)
        }).done (function(data) {
                $("#changes-successful").show();
        }).fail(function (data) {
            var responseErrors = data.responseJSON.errors
            $("#errors").empty();
            for (var i in responseErrors) {
                $("#errors").append("<div id=\"" + responseErrors[i].code + "\">" + responseErrors[i].message + "</div>");
            }
            $("#errors").show();
        });
}

function extractForm() {
    var dataResForm = { title: $("#title").val(), dataResourceID: $("#data-resource-id").val(), metadata: $("#metadata-json").val()};
    return dataResForm;
}