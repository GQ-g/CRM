layui.use(['table','layer',"form","http"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        http = layui.http,
        path = http.path;

    //表格渲染
    var tableIns = table.render({
        elem:'#customerOrderList',
        url:path+"/order/list?cusId="+$("input[name='id']").val(),
        cellMinWidth : 95,
        hegith:'full-125',
        page:true,
        limit:10,
        limits:[10,15,10],
        id:'orderListTable',
        cols:[[
            {type:'checkbox'},
            {title:'编号',type:'numbers'},
            {field:"id",hide:true},
            {field: 'orderNo', title: '订单编号',align:"center"},
            {field: 'orderDate', title: '下单日期',align:"center"},
            {field: 'address', title: '收货地址',align:"center"},
            {field: 'state', title: '支付状态',align:"center",templet:function (d) {
                    if(d.state==1){
                        return "已支付;"
                    }else{
                        return "未支付";
                    }
                }},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#customerOrderListBar"}
        ]]
    })

    //绑定单元格事件
    table.on('tool(customerOrders)',function(obj){
        if(obj.event=="info"){
            openOrderDetailDialog(obj.data.id);
        }
    })

    //订单详情对话框
    function openOrderDetailDialog(id){
        var title = "客户管理-订单详情查看"
        var url = path+"/customer/orderDetailPage?orderId="+id;
        layer.open({
            title:title,
            type:2,
            area:['700px','500px'],
            maxmin: true,
            content: url
        })
    }

});
