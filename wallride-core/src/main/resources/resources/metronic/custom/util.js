function fetchDataList(param, target, checkboxTarget) {

    $.ajax({
        url: "./",
        data: {
            page : param
        },
        type: 'GET',
        async: false,
        success: function (result) {

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

function enableFormatTime($element) {
    $element.each(function(index, value) {
        var time = $(value).text();
        var formatedString = formatRelativeTime(time);
        $(value).html(formatedString);
    })
}

function enableFormatRealTime() {
    $(".real-time").each(function(index, value) {
        var time = $(value).text();
        var formatedString = formatRelativeTime(time);
        $(value).html(formatedString);
    })
}

function enableGetUnReadNotifications() {
    $.ajax({
        url: "../../notifications/unread",
        type: 'GET',
        async: false,
        success: function (result) {

            $(".dropdown-notification .dropdown-menu").empty().append(result);
            enableFormatTime($(".dropdown-notification .time"));
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}


function enableNotificationBar() {

    $("#header_notification_bar").on("click", function() {
        enableGetUnReadNotifications();
        $(".dropdown-notification").addClass("dropdown-hoverable");

        $(".notification-badge").empty();
    });
}

function enableGetUnReadMessages() {
    $.ajax({
        url: "../../messages/unread",
        type: 'GET',
        async: false,
        success: function (result) {

            $(".dropdown-inbox .dropdown-menu").empty().append(result);
            $(".dropdown-inbox").addClass("open");
            enableFormatTime($(".dropdown-inbox .time"));
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}


function enableMessageBar() {

    $("#header_inbox_bar").on("click", function() {
        enableGetUnReadMessages();
        $(".dropdown-inbox").addClass("dropdown-hoverable");
        $(".inbox-badge").empty();
    });
}

function deleteFile(id) {
    debugger;
    $.ajax({
        url: "/_admin/en/media/" + id,
        type: 'DELETE',
        success: function (result) {

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}