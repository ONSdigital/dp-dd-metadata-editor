$(document).ready(function() {

   $("#data-resource-form").submit(function(e) {
        submitForm(e);
   });
});


function submitForm(e) {
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
            var errors = data.responseJSON.errors
            for (var i in errors) {
                $("#errors").append("<div>" + errors[i] + "</div>");
            }
            $("#errors").show();
        });
}