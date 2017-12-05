
function enableSaveTag() {
    $("#save-tag").on("click", function () {
        var tagName = $("#tag-create-name-form").val();
        var data = {
            name: tagName
        };
        $.ajax({
            url: "./",
            type: "POST",
            data: data,
            async: false,
            success: function (XMLHttpRequest, textStatus, errorThrown) {
                debugger;
                fetchDataList(0, "#tag-list-wrapper", "#tag-table");
            },
            error: function () {

            }
        })
    })
}
