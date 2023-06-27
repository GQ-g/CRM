layui.use(['table','layer',"form","http"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        http = layui.http,
        path = http.path;

    //表格渲染
    var tableIns = table.render({
        elem:'#orderDetailList',
        url:path+"/order_details/list?orderId="+$("input[name='id']").val(),
        cellMinWidth : 95,
        hegith:'full-125',
        page:true,
        limit:10,
        limits:[10,15,10],
        cols:[[
            {type:'checkbox'},
            {title:'编号',type:'numbers'},
            {field:"id",hide:true},
            {field: 'goodsName', title: '商品名称',align:"center"},
            {field: 'goodsNum', title: '商品数量',align:"center"},
            {field: 'unit', title: '单位',align:"center"},
            {field: 'price', title: '单价(￥)',align:"center"},
            {field: 'sum', title: '总价(￥)',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"}
        ]]
    })

    
});
