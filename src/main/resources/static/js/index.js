function queryChinese() {
    var a = $('#chinese').val().match("[\u4e00-\u9fa5]+");
    var chinese = a == null ? "" : a[0];
    // for(var i=0;i<chinese.length;++i){
    //     chineses.push(new Array(chinese[i]));
    // }
    // console.log(chineses);
    // console.log(chinese.length);
    //    action="/chineseStroke" method="post"
    $.ajax({
        url:"../chinesesStroke",
        type:"POST",
        async:true,
        dataType:'json',
        data:{chineses:chinese},
        success:(data) => {
            // console.log(JSON.stringify(data));
            $('#content').empty();
            // var data1=JSON.stringify(data);
            // console.log(data1);
            for(let k of Object.keys(data)){
                $('#content').append('<div class="col-12 col-lg-4 py-3">' +
                    '                    <div class="card shadow-sm">'+
                    '                        <div class="card-body">' +
                    '                            <h5 class="card-title mb-3">' +
                    '<span class="theme-icon-holder card-icon-holder mr-2">' +
                    '<i class="fas fa-fonticons-fi"></i>' +
                    '</span><!--//card-icon-holder-->' +
                    '                                <span class="card-title-text">'+k+'</span>' +
                    '                            </h5>' +
                    '                            <div class="card-text">' +'笔顺：'+data[k]+
                    '                            </div>' +
                    '                            <a class="card-link-mask" href="#"></a>' +
                    '                        </div><!--//card-body-->' +
                    '                    </div><!--//card-->' +
                    '                </div>');
            }
        },
        error:() => {
            alert("网络错误！");
        }
    })
}