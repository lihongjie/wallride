
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

function enableFetchGuestTags(page) {

    var url = "/tag/data";
    var data =  {
        page : page
    };
    $.ajax({
        url: url,
        type: 'GET',
        async: false,
        data: data,
        success: function (result) {

            $("#tag-list-wrapper").empty().append(result);
            enableFetchGuestTagsPagination();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

function enableFetchGuestTagOfPosts(page) {
    var name = $("#tag-name").val();
    var url = "/tag/" + name + "/data";
    var data =  {
        page : page
    };
    $.ajax({
        url: url,
        type: 'GET',
        async: false,
        data: data,
        success: function (result) {
            debugger;
            $("#tag-of-posts-wrapper").empty().append(result);
            enableFetchGuestTagOfPostsPagination();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

function enableFetchGuestTagsPagination() {

    $(".pagination-a").on("click", function(e) {
        e.preventDefault();
        var page = $(this).data("page");
        if($(this).parent().hasClass('disabled')) {
            return false;
        }
        enableFetchGuestTags(page);
    });
}

function enableFetchGuestTagOfPostsPagination() {

    $(".pagination-a").on("click", function(e) {
        e.preventDefault();
        var page = $(this).data("page");
        if($(this).parent().hasClass('disabled')) {
            return false;
        }
        enableFetchGuestTagOfPosts(page);
    });
}
