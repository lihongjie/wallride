function fetchArticleList() {

    $.ajax({
        url: "./list",//这个就是请求地址对应sAjaxSource
        data: {
            // "aoData": JSON.stringify(aoData)
        },
        type: 'GET',
        async: false,
        success: function (result) {
            debugger;
            $("#article-list-wrapper").empty().append(result);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

        }
    });
}

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




    $('#post-cover-dropzone .remove').click(function(e) {
        $('#post-cover-dropzone :input[name="coverId"]').val('');
        $('#post-cover-dropzone .image-wrap').addClass('hide');
        $('#post-cover-dropzone img').remove();
        e.preventDefault();
    });
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



function postCoverDropzone() {
    $('#post-cover-dropzone').fileupload({
        url: 'http://localhost:8080/_admin/en/media/create.json',
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
            url:  '',
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
        createSearchChoice: function (term, data) {
            if ($(data).filter(function () {
                    return this.text.localeCompare(term) === 0;
                }).length === 0) {
                return {id: term, text: term};
            }
        },
        ajax: {
            url: '',
            dataType: 'json',
            data: function (term, page) {
                return {
                    keyword: term
                };
            },
            results: function (data, page) {
                var results = [];
                $.each(data, function () {
                    results.push({id: this.text, text: this.text});
                });
                return {results: results};
            }
        },
        initSelection: function (element, callback) {
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
            saveArticle('', 'DRAFT');
            return false;
        });
    }

    function savePublish() {
        $('#save-publish-button').on('click', function(e) {
            saveArticle('', 'PUBLISH');
        });
    }

    function saveArticle(url,status) {
        var $this = $(this);
        $this.button('loading');
        var $form = $this.closest('form');
        $(':input[name="body"]', $form).val($('#wr-page-content :input[name="body"]').froalaEditor('html.get'));
        var data = $form.serializeArray();
        data.push({name: 'draft', value: 1});
        $.ajax({
            type: "POST",
            url: $form.attr('action'),
            data: data,
            success: function(data) {
                $form.children(':input[name="id"]').val(data.id);
                $form.attr('action', '');
                var url = '';
                history.replaceState(null, null, url);
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
                $this.button('reset');
            }
        });
    }
