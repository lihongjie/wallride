$(document).ready(function(){

    $('#article-table').DataTable( {
        "sPaginationType": "full_numbers",
        "bPaginite": true,
        "bInfo": true,
        "bSort": true,
        "processing": false,
        "serverSide": true,
        "sAjaxSource": "../index/metronic/query",//这个是请求的地址
        "fnServerData": retrieveData
    } );

    function retrieveData(url, aoData, fnCallback) {

        /*var data = {"data": {"id": "123123", "name": "2s",}};*/
        $.ajax({
            url: url,//这个就是请求地址对应sAjaxSource
            data: {
                // "aoData": JSON.stringify(aoData)
            },
            type: 'GET',
            async: false,
            success: function (result) {

                //var obj=JSON.parse(result);
                console.log(result);
                fnCallback(result);//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {

                alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);

            }
        });
    }

});