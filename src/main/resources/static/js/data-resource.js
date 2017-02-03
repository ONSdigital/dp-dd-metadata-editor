$(document).ready(function() {

   $("#data-resource-form").submit(function(e) {
        submitForm()
   });
});


function submitForm() {
    var myForm = { title: $("#title").val(), dataResourceID: $("#data-resource-id").val(), metadata: $("#metadata-json").val()};

    $.ajax({
        type: "POST",
        url: "/dataResource",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data:  JSON.stringify(myForm),
        success: function(data) {
            alert(data);
        },
        error: function (data) {
            if (data.status == 400) {
                var errors = data.responseJSON.errors
                for (var i in errors) {
                    $("#errors").append("<div>" + errors[i] + "</div>");
                }
                $("#errors").show();
            }
        }
    });
    e.preventDefault();
}