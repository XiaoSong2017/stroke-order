$.i18n.properties({// 加载properties文件
    name: 'messages', // properties文件名称
    path: '/', // properties文件路径
    mode: 'map', // 用 Map 的方式使用资源文件中的值
});
function isEmpty(obj) {
    return typeof obj === 'undefined' || obj == null || obj === '';
}
function chineseSort() {
    // console.log($('#query').val().split('\n'))
    var strings = $('#query').val();
    // console.log(strings)
    if (isEmpty(strings)) {
        layer.alert($.i18n.prop("emptyDataTip"));
    } else {
        $.ajax({
            url: "/stroke-sort",
            type: "POST",
            async: true,
            dataType: 'json',
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(strings.split('\n')),
            beforeSend: () => {
                this.layerIndex = layer.load(1);
            },
            complete: () => {
                layer.close(this.layerIndex);
            },
            success: (data) => {
                $('#result').empty();
                for (i of data) {
                    $('#result').append(i+'\n');
                }
            },
            error: () => {
                layer.alert(
                    $.i18n.prop("netErrorTip")
                );
            }
        })
    }
}