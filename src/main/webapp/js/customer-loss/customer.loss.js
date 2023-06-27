layui.use(['table','layer',"form","http"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        http = layui.http,
        path = http.path;

    //客户流失表格
    var tableIns = table.render({
        elem:'#customerLossList',
        url:path+"/customer_loss/list",
        cellMinWidth:95,
        height:'full-125',
        page:true,
        limit:10,
        limits:[10,15,20],
        id:'customerLossListTable',
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'cusNo', title: '客户编号',align:"center"},
            {field: 'cusName', title: '客户名称',align:"center"},
            {field: 'cusManager', title: '客户经理',align:"center"},
            {field: 'lastOrderTime', title: '最后下单时间',align:"center"},
            {field: 'lossReason', title: '流失原因',align:"center"},
            {field: 'confirmLossTime', title: '确认流失时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#op"}
        ]]
    });

    //多条件搜索
    $(".search_btn").click(function(){
        table.reload('customerLossListTable',{
            page:{
                curr:1
            },
            where:{
                cusNo:$("input[name='cusNo']").val(),
                cusName:$("input[name='cusName']").val(),
                state:$("#state").val()
            }
        })
    })

    //单元格工具栏
    table.on('tool(customerLosses)',function(obj){
        var event = obj.event;
        if(event=='add'){
            openCustomerReprDialog("展缓措施维护",obj.data.id);
        }else if(event=='info'){
            //展缓详情数据查看
            openCustomerReprDialog("展缓数据查看",obj.data.id)
        }
    })

    //展缓措施维护
    function openCustomerReprDialog(title,id){
        layer.open({
            title:title,
            type:2,
            area:['700px','500px'],
            maxmin:true,
            content:path+"/customer_loss/toCustomerReprPage?id="+id
        })
    }
});
