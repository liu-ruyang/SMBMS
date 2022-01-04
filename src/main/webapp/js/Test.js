// <script src="https://cdn.bootcdn.net/ajax/libs/axios/0.21.1/axios.js"/>
// 美元符号的使用
$(function () {//①载入页面时启动时调用
    //②选择对象
    $('#userName').click()
    {
        $.get(
            'http://localhost:8080',
            {
                a: 100,
                b: 200,
            },
            function (data) {
                console.log(data)
            },
            "json"      //设置响应体类型，在服务器端也要杜应进行设置为一致
        );
    }
    $.ajax({
        //url
        url: "http://localhost:8080",
        //参数
        data: {a: 100, b: 200},
        //请求类型
        type: 'GET',        //也可以是'POST'
        //响应体结果
        dataType: 'json',
        //成功的回调
        success: function (data) {
            console.log(data);
        },
        //超时时间
        timeout: 2000,
        //失败的回调
        error: function () {
            console.log('c出错啦！！');
        },
        //头信息
        headers: {
            /*请求头*/
        }
    })
    axios.get('http://localhost:8080', {
        param: {        //url参数
            id: 100,
            vip: 7
        },
        headers: {          //请求头信息
            /*请求头信息*/
        }.then(value => {
            console.log(value);
            console.log("响应成功");
        }).throw(err => {
            console.log("失败")
        })
    });
})