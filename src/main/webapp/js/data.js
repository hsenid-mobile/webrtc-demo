

var addNewFile = function () {
    var formData = new FormData();
    formData.append("file", control.files[0]);

    var file = $("#control").val();  //Fetch the filename of the submitted file

    console.log("File >> ");
    console.log(file);
    console.log("File >> ");

    var fileName = "";
    $("input[name='file_1']").each(function () {
        fileName = $(this).val().split('/').pop().split('\\').pop();
    });

    console.log("File Name>> ");
    console.log(fileName);
    console.log("File Name>> ");

    if (file == '') {
        alert("select the file first");
    }
    else {
        $.ajax({
            type: "POST",
            url: "uploadImage.htm",
            timeout: 50000,
            cache: false,
            data: formData,
            contentType: false,
            processData: false,
            statusCode: {
                404: function () {
                    console.log("page not found");
                },
                405: function () {
                    console.log("Request Method POST not supported");
                }
            },
            before: function() {
                console.log("hi");
            },
            success: function (data) { // get the response
                console.log('ajax call finished successfully');
                var response =  JSON.parse(data);
                console.log(response)

            },
            error: function (textStatus, errorThrown) {
                console.log('Failure: ' + textStatus + ". Error thrown: " + errorThrown);

            },
            complete: function () {
                setTimeout(function() {
                    $('#addFileResult').html('')
                }, 3000)
            }

        });
    }
}