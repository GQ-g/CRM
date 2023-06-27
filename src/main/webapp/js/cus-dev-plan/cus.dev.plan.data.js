layui.use(['table','layer','http'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        laydate = layui.laydate,
        http = layui.http;
    var path = http.path;
    //计划项列表数据
    var tableIns = table.render({
        elem:'#cusDevPlanList',
        url:path+'/cus_dev_plan/list?sId='+$("input[name='id']").val(),
        cellMinWidth:95,
        page:true,
        height:'full-125',
        limits:[10,15,20],
        limit:10,
        toolbar:'#toolbarDemo',
        id:'cusDevPlanListTable',
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执行效果',align:"center"},
            {field: 'planDate', title: '执行时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
        ]]
    })

    //表格头事件
    table.on('toolbar(cusDevPlans)',function(obj){
        switch (obj.event) {
            case 'add':
                openAddOrUpdateCusDevPlanDialog();
                break;
            case 'success':
                updateSaleChanceDevResult($("input[name='id']").val(),2);
                break;
            case 'failed':
                updateSaleChanceDevResult($("input[name='id']").val(),3);
                break;
        }
    })
    //单元格事件
    table.on('tool(cusDevPlans)',function(obj){
        alert(obj.data.id);
        if(obj.event=="edit"){
            openAddOrUpdateCusDevPlanDialog(obj.data.id);
        }else if(obj.event="del"){
            layer.confirm("确认删除当前记录吗？",{icon:3,title:'客户开发计划管理'},function(index){
                $.post(path+"/cus_dev_plan/delete",{id:obj.data.id},function(data){
                    if(data.code==200){
                        layer.msg(data.msg);
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg);
                    }
                },"json");
            });
        }
    });
    //添加或修改对话框
    function openAddOrUpdateCusDevPlanDialog(id){
        var title = "计划项管理 - 添加计划项";
        var url = path+"/cus_dev_plan/addOrUpdateCusDevPlanPage?sid="+$("input[name='id']").val();
        // 判断计划项的ID是否为空 （如果为空，则表示添加；不为空则表示更新操作）
        if (id != null && id != '') {
            // 更新计划项
            title = "计划项管理 - 更新计划项";
            url =url + "&id="+id;
        }
        // iframe层
        layui.layer.open({
            // 类型
            type: 2,
            // 标题
            title: title,
            // 宽高
            area: ['500px', '300px'],
            // url地址
            content: url,
            // 可以最大化与最小化
            maxmin:true
        });
    }
    //更新营销机会状态成功或失败
    function updateSaleChanceDevResult(sid,devResult){
        layer.confirm("确认更新营销机会数据状态吗？",{icon:3,title:'客户开发计划管理'},function (index) {
            $.post(path+"/sale_chance/updateSaleChanceDevResultVo",{
                id:sid,
                devResult:devResult
            },function(data){
                if(data.code==200){
                    layer.msg(data.msg);
                    layer.closeAll('iframe');
                    parent.location.reload();
                }else {
                    layer.msg(data.msg);
                }
            },"json");
        })
    }
});
