layui.use(['table','layer',"form","http"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        http = layui.http,
        path = http.path;

    //表格渲染
    var tableIns = table.render({
        elem:'#customerList',
        url:path+"/customer/list",
        cellMinWidth : 95,
        hegith:'full-125',
        page:true,
        limit:10,
        limits:[10,15,10],
        id:'customerListTable',
        toolbar:'#toolbarDemo',
        cols:[[
            {type:'checkbox'},
            {title:'编号',type:'numbers'},
            {field:"id",hide:true},
            {field: 'name', title: '客户名',align:"center"},
            {field: 'fr', title: '法人',  align:'center'},
            {field: 'khno', title: '客户编号', align:'center'},
            {field: 'area', title: '地区', align:'center'},
            {field: 'cusManager', title: '客户经理',  align:'center'},
            {field: 'myd', title: '满意度', align:'center'},
            {field: 'level', title: '客户级别', align:'center'},
            {field: 'xyd', title: '信用度', align:'center'},
            {field: 'address', title: '详细地址', align:'center'},
            {field: 'postCode', title: '邮编', align:'center'},
            {field: 'phone', title: '电话', align:'center'},
            {field: 'webSite', title: '网站', align:'center'},
            {field: 'fax', title: '传真', align:'center'},
            {field: 'zczj', title: '注册资金', align:'center'},
            {field: 'yyzzzch', title: '营业执照', align:'center'},
            {field: 'khyh', title: '开户行', align:'center'},
            {field: 'khzh', title: '开户账号', align:'center'},
            {field: 'gsdjh', title: '国税', align:'center'},
            {field: 'dsdjh', title: '地税', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '更新时间', align:'center'},
            {title: '操作', templet:'#customerListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    })

    //多条件搜索
    $(".search_btn").click(function(){
        table.reload('customerListTable',{
            page:{
                curr:1
            },
            where:{
                // 通过文本框，设置传递的参数
                customerName: $("[name='name']").val() // 客户名称
                ,customerNo: $("[name='khno']").val() // 客户编号
                ,level:$("#level").val() // 客户级别
            }
        })
    })

    //头部工具栏
    table.on('toolbar(customers)',function(obj){
        switch (obj.event) {
            case 'add':
                openAddOrUpdateCustomerDialog();
                break;
            case 'order':
                openOrderInfoDialog(table.checkStatus(obj.config.id).data);
                break;
        }
    });
    //单元格工具栏
    table.on('tool(customers)',function(obj){
        var event = obj.event;
        if(event=='edit'){
            openAddOrUpdateCustomerDialog(obj.data.id);
        }else if(event=='del'){
            layer.confirm("确认删除吗",{icon:3,title:'角色管理'},function(index){
                $.post(path+"/customer/delete",{id:obj.data.id},function(data){
                    if(data.code==200){
                        layer.msg(data.msg);
                        tableIns.reload();
                    }else {
                        layer.msg(data.msg);
                    }
                },"json")
            });
        }
    })

    //添加或修改对话框
    function openAddOrUpdateCustomerDialog(id){
        var title = "客户管理-客户添加";
        var url = path+"/customer/addOrUpdateCustomerPage";
        if(id){
            title = "客户管理-客户修改";
            url = url+"?id="+id;
        }
        layer.open({
            title:title,
            type:2,
            area:['700px','500px'],
            maxmin:true,
            content:url
        })
    }
    //订单信息对话框
    function openOrderInfoDialog(datas){
        if(datas.length==0){
            layer.msg("请选择查看订单的对应客户");
            return;
        }
        if(datas.length>1){
            layer.msg("赞不支持批量查看");
            return;
        }
        layer.open({
            title:'客户管理-订单信息查看',
            type:2,
            area:['700px','500px'],
            maxmin: true,
            content: path+"/customer/orderInfoPage?cid="+datas[0].id
        })
    }
});
