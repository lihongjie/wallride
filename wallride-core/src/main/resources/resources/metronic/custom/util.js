function fetchDataList(param, target, checkboxTarget) {

    $.ajax({
        url: "./",
        data: {
            page : param
        },
        type: 'GET',
        async: false,
        success: function (result) {
            debugger;
            $(target).empty().append(result);
            enablePagination(target);
            enableCheckbox(checkboxTarget);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

function enablePagination(target) {

    $(".pagination-a").on("click", function(e) {
        e.preventDefault();
        var page = $(this).data("page");
        if($(this).parent().hasClass('disabled')) {
            return false;
        }
        fetchDataList(page, target);
    });
}

function enableCheckbox(target) {

    var table = $(target);
    table.find('.group-checkable').change(function () {
        debugger;
        var set = jQuery(this).attr("data-set");
        var checked = jQuery(this).is(":checked");
        jQuery(set).each(function () {
            if (checked) {
                $(this).prop("checked", true);
                $(this).parents('tr').addClass("active");
            } else {
                $(this).prop("checked", false);
                $(this).parents('tr').removeClass("active");
            }
        });
    });

    table.on('change', 'tbody tr .checkboxes', function () {
        $(this).parents('tr').toggleClass("active");
    });

}

function formatRelativeTime(time) {
    return moment(time, "YYYY-MM-DD HH:mm:ss").fromNow();
}

function eableFormatTime($element) {
    $element.each(function(index, value) {
        var time = $(value).text();
        var formatedString = formatRelativeTime(time);
        $(value).html(formatedString);
    })
}