

function init() {
    $(function() {
        $('#search-form select.select2').each(function(index) {
            var $self = $(this);
            $self.select2({
                allowClear: true,
                dropdownAutoWidth: true,
                dropdownCssClass: 'select2-drop-search-form',
            });
        });
        $('#search-form input.select2').each(function(index) {
            var $self = $(this);
            $self.select2({
                minimumInputLength: 1,
                allowClear: true,
                dropdownAutoWidth: true,
                dropdownCssClass: 'select2-drop-search-form',
                ajax: {
                    url:  $self.attr('data-url'),
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
                    var id = $(element).val();
                    if (id !== "") {
                        $.ajax($self.attr('data-url') + '/' + id + ".json", {
                            dataType: "json"
                        }).done(function(data) {
                            callback(data);
                        });
                    }
                }
            });
        });
    });
}




