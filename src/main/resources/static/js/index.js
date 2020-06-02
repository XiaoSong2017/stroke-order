$.i18n.properties({// 加载properties文件
    name:'messages', // properties文件名称
    path:'/', // properties文件路径
    mode:'map', // 用 Map 的方式使用资源文件中的值
    // callback: ()=>{// 加载成功后设置显示内容
    //     alert($.i18n.prop("netErrorTip"));//获取conf.properties文件总内容
    // }
});
function isEmpty(obj) {
    return typeof obj === 'undefined' || obj == null || obj === '';
}
function queryChinese() {
    var a = $('#chinese').val().match("[\u4e00-\u9fa5]+");
    if(isEmpty(a)){
        layer.alert($.i18n.prop("emptyDataTip"));
    }else {
        var chinese = a[0];
        $.ajax({
            url: "/chinesesStroke",
            type: "POST",
            async: true,
            dataType: 'json',
            data: {chineses: chinese},
            beforeSend: () => {
                this.layerIndex = layer.load(1);
            },
            complete: () => {
                layer.close(this.layerIndex);
            },
            success: (data) => {
                // console.log(JSON.stringify(data));
                $('#content').empty();
                // var data1=JSON.stringify(data);
                // console.log(data1);
                for (let k of Object.keys(data)) {
                    $('#content').append('<div class="col-12 col-lg-4 py-3">' +
                        '                    <div class="card shadow-sm">' +
                        '                        <div class="card-body">' +
                        '                            <h5 class="card-title mb-3">' +
                        '<span class="theme-icon-holder card-icon-holder mr-2">' +
                        '<i class="fas fa-fonticons-fi"></i>' +
                        '</span><!--//card-icon-holder-->' +
                        '                                <span class="card-title-text">' + k + '</span>' +
                        '                            </h5>' +
                        '                            <div class="card-text">' + $.i18n.prop("logoText_2") + '：' + data[k] +
                        '                            </div>' +
                        '                            <a class="card-link-mask" href="#"></a>' +
                        '                        </div><!--//card-body-->' +
                        '                    </div><!--//card-->' +
                        '                </div>');
                }
            },
            error: () => {
                layer.alert(
                    $.i18n.prop("netErrorTip")
                );
                // Swal.fire($.i18n.prop("netErrorTip"));
            }
        })
    }
}