
/*function articlePreview() {
    $('#wr-page-header').on('click', '#article-preview', function(e) {
        e.preventDefault();
        var form = $(this).closest('form').clone();
        var action = /!*[[@{__${ADMIN_PATH}__/articles/preview}]]*!/ '#';
        form.attr('action', action);
        form.attr('target', '_blank');
        $(':input[name="body"]', form).val($('#wr-page-content :input[name="body"]').froalaEditor('html.get'));
        $('body').append(form);
        form.submit();
        form.remove();
    });
}





}*/

/*
    $(function() {
        var lastValue = $('#wr-post-form :input[name!="id"]').serialize();
        setInterval(function() {
            var currentValue = $('#wr-post-form :input[name!="id"]').serialize();
            if (lastValue != currentValue) {
                $('#save-draft-button').trigger('click');
            }
            lastValue = currentValue;
        }, 180000);
    });












    $(function() {
        var websiteLink = /!*[[${WEBSITE_LINK} + '/']]*!/ '/';
        var today = moment().format('YYYY/MM/DD/');
        $('#wr-page-content').on('change', 'input[name="date"]', function(e) {
            var inputDate = $(this).val();
            if (inputDate != "") {
                var urlPath = moment(inputDate, 'YYYY/MM/DD hh:mm').format('YYYY/MM/DD/');
                $('.url-path').text(websiteLink + urlPath);
            } else {
                $('.url-path').text(websiteLink + today);
            }
        });
        $('input[name="date"]').trigger("change");
    });*/

function enableRemovePostCover() {
    $('#post-cover-dropzone').on('click', ' .remove', function(e) {
        $('#post-cover-dropzone :input[name="coverId"]').val('');
        $('#post-cover-dropzone .image-wrap').addClass('hide');
        $('#post-cover-dropzone img').remove();
        e.preventDefault();
    });
}


function postCoverDropzone() {
    $('#post-cover-dropzone').fileupload({
        url: '/_admin/en/media/create.json',
        paramName: 'file',
        dropZone: $('#post-cover-dropzone'),
        dragover: function (e) {
            var dropZone = $('#post-cover-dropzone');
            var timeout = window.dropZoneTimeout;
            if (!timeout) {
                $('#post-cover-dropzone .dragover').removeClass('hide');
            } else {
                clearTimeout(timeout);
            }
            var found = false;
            var node = e.target;
            do {
                if (node === dropZone[0]) {
                    found = true;
                    break;
                }
                node = node.parentNode;
            } while (node != null);
            if (found) {
                $('#post-cover-dropzone .dragover').removeClass('hide');
            }
            else {
                $('#post-cover-dropzone .dragover').addClass('hide');
            }
            window.dropZoneTimeout = setTimeout(function () {
                window.dropZoneTimeout = null;
                $('#post-cover-dropzone .dragover').addClass('hide');
            }, 100);
        },
        start: function (e) {
            $('#post-cover-dropzone .progress').removeClass('hide');
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#post-cover-dropzone .progress-bar').css('width', progress + '%');
        },
        done: function (e, data) {
            $('#post-cover-dropzone :input[name="coverId"]').val(data.result.id);
            $('#post-cover-dropzone .progress').addClass('hide');
            $('#post-cover-dropzone .progress-bar').css('width', '0%');
            var img = $('<img class="wr-post-cover" />').attr('src', data.result.link + '?' + $.param({
                w: 1200,
                h: 500,
                m: 1
            }));
            $('#post-cover-dropzone img').remove();
            $('#post-cover-dropzone').append(img);
            $('#post-cover-dropzone .image-wrap').removeClass('hide');
        }
    });
}

function relatedPostFieldSet() {
    $('#related-posts-fieldset').select2({
        minimumInputLength: 1,
        multiple: true,
        ajax: {
            url:  '/_admin/en/posts/select',
            dataType: 'json',
            data: function (term, page) {
                return {
                    keyword: term
                };
            },
            results: function (data, page) {
                return {results: data};
            }
        },
        initSelection: function(element, callback) {
            var data = [];
            $(element.val().split(',')).each(function() {
                var id = this;
                if (id !== "") {
                    var url = '';
                    $.ajax(url + "/" + id,  {
                        async: false,
                        dataType: "json",
                        data: { id: id }
                    }).done(function(response) { data.push(response); });
                }
            });
            element.val('');
            callback(data);
        }
    });
};

function tagsFieldSet() {
    $('#tags-field').select2({
        minimumInputLength: 1,
        multiple: true,
        id: function (data) {
          return data.id;
        },
        open: function () {
            debugger;
        },
        createSearchChoice: function (term, data) {
            debugger;
            if ($(data).filter(function () {
                    return this.text.localeCompare(term) === 0;
                }).length === 0) {
                return {id: term, text: term};
            }
        },
        ajax: {
            url: '/_admin/en/tags/select',
            dataType: 'json',
            data: function (term, page) {
                return {
                    keyword: term
                };
            },
            results: function (data, page) {
                debugger;
                var results = [];
                $.each(data, function () {
                    results.push({id: this.text, text: this.text});
                });
                return {results: results};
            }
        },
        initSelection: function (element, callback) {
            debugger;
            var data = [];
            $(element.val().split(',')).each(function () {
                var id = this;
                if (id !== "") {
                    data.push({id: id, text: id});
                }
            });
            element.val('');
            callback(data);
        }
    });
}

function enableSaveCategory() {
    $("#category-create-modal").on('click', '#save-category', function(e) {
        e.preventDefault();
        var self = $(this);
        self.button('loading');
        var checked = [];
        $('input[name="categoryIds"]:checked', '#wr-post-form').each(function (i, element) {
            checked.push($(element).val());
        });
        var form = self.closest('form');
        var data = {
            parentId: $(':input[name="parentId"]', form).val(),
            code: $(':input[name="code"]', form).val(),
            name: $(':input[name="name"]', form).val(),
            description: $(':input[name="description"]', form).val()
        };
        $.ajax({
            url: '',
            type: 'post',
            data: data,
            success: function (savedCategory) {
                checked.push(savedCategory.id.toString());
                $.get('', function (data) {
                    data = $(data);
                    $(':input[name="categoryIds"]', data).each(function (i, element) {
                        if ($.inArray($(element).val(), checked) != -1) {
                            $(element).prop('checked', true);
                        }
                    });
                    $('#category-fieldset').html(data);
                }
            )
                ;
                self.closest('.modal').modal('hide');
            },
            error: function (jqXHR) {
                $.each(jqXHR.responseJSON.fieldErrors, function (field, message) {
                    var field = $(':input[name="' + field + '"]', form);
                    $(field).closest('.form-group').addClass('has-error');
                });
            },
            complete: function () {
                $(self).button('reset');
            }
        })
    });
}

function saveDraft() {
    $('#save-draft-button').on('click', function(e) {
        saveArticle('draft');
    });
}

function savePublish() {
    $('#save-publish-button').on('click', function() {
        saveArticle('publish');
    });
}

function saveArticle(status) {
    var coverId = $("#coverId").val();
    var title = $("#form-title").val();
    var code = $("#form-code").val();
    var body = $('#wr-page-content :input[name="body"]').froalaEditor('html.get');
    var date = $("#form-date").val();
    var seoTitle = $("#form-seo-title").val();
    var seoDescription = $("#form-seo-description").val();
    var seoKeywords = $("#form-seo-keywords").val();

    var categoryIds = new Array();
    // categoryIds[0]=1;
    $(".category-fieldset input:checkbox:checked").each(function () {
       categoryIds.push($(this).val());
    });
    var tags = $("#tags-field").val();
    var relatedPostIds = $("#related-posts-fieldset").val();
    var data = {
        coverId: coverId,
        title: title,
        code: code,
        body: body,
        date: date,
        seoTitle: seoTitle,
        seoDescription: seoDescription,
        seoKeywords: seoKeywords,
        categoryIds : categoryIds.toString(),
        tags: tags,
        relatedPostIds: relatedPostIds
    };
    var url = $("#wr-post-form").attr("action");
    $.ajax({
        type: "POST",
        url: url + '?status=' + status,
        data: data,
        success: function(data) {

            new PNotify({
                icon: false,
                title: 'Saved as draft',
                type: 'success',
                delay: 3000,
                buttons: {
                    sticker: false
                }
            });
        },
        complete: function() {

        }
    });
}

function enableSearchArticle(param) {

    $.ajax({
        url: "./articles",
        data: {
            page : param
        },
        type: 'GET',
        async: false,
        success: function (result) {

            $("#article-list-wrapper").empty().append(result);
            enablePagination();
            enableFormatTime($(".article-created-time"));

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}
function enablePagination() {

    $(".pagination-a").on("click", function(e) {
        e.preventDefault();
        var page = $(this).data("page");
        if($(this).parent().hasClass('disabled')) {
            return false;
        }
        enableSearchArticle(page);
    });
}


function enableFetchCommentsByArticleId(param) {
    var id = $("#article-id").val();
    var url = "/article/" + id + "/comments";
    $.ajax({
        url: url,
        data: {
            page : param
        },
        type: 'GET',
        async: false,
        success: function (result) {

            $("#article-comments-wrapper").empty().append(result);
            enableArticleCommentsPagination();
            enableSubmitComment();
            eableFormatTime($(".comment-created-time"));
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

function enableArticleCommentsPagination() {

    $(".pagination-a").on("click", function(e) {
        e.preventDefault();
        var page = $(this).data("page");
        if($(this).parent().hasClass('disabled')) {
            return false;
        }
        enableFetchCommentsByArticleId(page);
    });
}

function enableSubmitComment() {
    $("#post-comment-btn").on("click", function() {
        var content = $("#post-comment-content").val();
        var articleId = $("#article-id").val();
        var parentId = '';
        var data = {
            content : content,
            postId : articleId,
            parentId : parentId
        };
        var url = "/article/" + articleId + "/comments";
        $.ajax({
            url: url,
            data: data,
            type: 'POST',
            async: false,
            success: function (textStatus) {
                enableFetchCommentsByArticleId(0);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

                alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

            }
        });
    })
}

function enableFetchCategoryArchive() {
    var id = $("#article-id").val();
    var url = "/article/" + id + "/categories";
    $.ajax({
        url: url,
        type: 'GET',
        async: false,
        success: function (result) {

            $(".blog-sidebar-categories").empty().append(result);

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

function enableArticleArchive() {
    var id = $("#article-id").val();
    var url = "/article/" + id + "/archive";
    $.ajax({
        url: url,
        type: 'GET',
        async: false,
        success: function (result) {

            $(".blog-sidebar-article-archive").empty().append(result);

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

function enableFetchTags() {
    var id = $("#article-id").val();
    var url = "/article/" + id + "/tags";
    $.ajax({
        url: url,
        type: 'GET',
        async: false,
        success: function (result) {

            $(".blog-sidebar-tags").empty().append(result);

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

function enableFetchPopularPost() {
    var url = "/popular_post";
    $.ajax({
        url: url,
        type: 'GET',
        async: false,
        success: function (result) {

            $(".sidebar-popular-post").empty().append(result);

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}