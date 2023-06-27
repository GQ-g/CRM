layui.use(['table','layer',"form","http"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        http = layui.http,
        path = http.path;

    //展缓列表展示
    var tableIns = table.render({
        elem:'#customerRepList',
        url:path+"/customer_rep/list?lossId="+$("input[name='id']").val(),
        cellMinWidth:95,
        height:'full-125',
        page:true,
        limit:10,
        limits:[10,15,20],
        id:'customerRepListTable',
        toolbar:'#toolbarDemo',
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'measure', title: '暂缓措施',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#customerRepListBar"}
        ]]
    });
    //头部工具栏
    table.on('toolbar(customerReps)',function(obj){
        switch (obj.event) {
            case 'add':
                openAddOrUpdateCustomerReprDialog();
                break;
            case 'confirm':
                updateCustomerLossState();
                break;
        }
    });
    //绑定单元格事件
    table.on('tool(customerReps)',function(obj){
        var event = obj.event;
        alert(event);
        if(event=='edit'){
            openAddOrUpdateCustomerReprDialog(obj.data.id);
        }else if(event=='del'){
            layer.confirm("确认删除吗",{icon:3,title:'客户流失管理'},function(index){
                $.post(path+"/customer_rep/delete",{id:obj.data.id},function(data){
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
    //添加或修改暂缓对话框
    function openAddOrUpdateCustomerReprDialog(ids){
        alert("ids"+ids);
        alert($("input[name='id']").val());
        var title = "暂缓管理-添加暂缓";
        var url = path+"/customer_loss/addOrUpdateCustomerReprPage?lossId="+$("input[name='id']").val();
        if(ids){
            alert(ids+"ids")
            title = "展缓管理-更新展缓";
            url = url+"&id="+ids;
            alert(url+"url")
        }
        layer.open({
            title:title,
            type:2,
            area:['700px','500px'],
            maxmin:true,
            content:url
        })
    }
    //确认流失对话框
    function updateCustomerLossState(){
        layer.confirm("当前客户确认流失？",{icon:3,title:'客户流失管理'},function(index){
            layer.close(index);
            layer.prompt({title:"请输入流失原因",formType:2},function(text,index){
                alert(text);
                $.ajax({
                    type:'post',
                    url:path+"/customer_loss/updateCustomerLossStateById",
                    data:{
                        id:$("input[name='id']").val(),
                        lossReason:text
                    },
                    dataType:'json',
                    success:function(data){
                        if(data.code==200){
                            layer.msg(data.msg);
                            layer.closeAll("iframe");
                            parent.location.reload();
                        }else {
                            layer.msg(data.msg);
                        }
                    }
                })
            })
        })
    }
});
