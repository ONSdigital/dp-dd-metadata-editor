$(document).ready(function() {
    console.log( "ready!" );

    $("#dataset-select").on('change', function() {
        console.log( "Select has changed" );
        $.ajax({
            url: "/metadata/" + $('#dataset-select').val()
        }).done(function(resp) {
            console.log( resp );
            var jsonObj = JSON.parse(resp);
            $('#json-input').val(JSON.stringify(jsonObj, null, '\t'));
         });
    });
});