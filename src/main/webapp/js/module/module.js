layui.use(['layer','table', 'treetable','http'], function () {
    var $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        table = layui.table,
        treeTable = layui.treetable,
        http = layui.http,
        path = http.path;

    //渲染树形表格
    var tableIns = treeTable.render({
        treeColIndex: 1,//层级关系展示在第几列
        treeSpid: -1,//是否有最高父id没有-1表示
        treeIdName: 'id', //菜单id
        treePidName:'parentId',//菜单的父id
        elem:'#munu-table',
        url:path+"/module/list",
        toolbar:'#toolbarDemo',
        treeDefaultClose:true,
        cols:[[
            {type:'numbers'},
            {field:'moduleName',minWidth:100,title:'菜单名称'},
            {field:'optValue',minWidth:100,title:'权限码'},
            {field:'url',minWidth:100,title:'菜单url'},
            {field:'createDate',minWidth:100,title:'创建时间'},
            {field:'updateDate',minWidth:100,title:'更新时间'},
            {field:'grade',minWidth:100,title:'类型',templet:function(obj){
                    if(obj.grade==0){
                        return '<span>目录</span>'
                    }else if(obj.grade==1){
                        return '<span>菜单</span>'
                    }else if(obj.grade==2){
                        return '<span>按钮</span>'
                    }
                }},
            {minWidth:100,title:'操作',templet:'#auth-state'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    })

    //头部工具栏事件
    table.on('toolbar(munu-table)',function(obj){
        switch (obj.event) {
            case 'add':
                openAddModuleDialog(0,-1);
                break;
            case 'expand':
                treeTable.expandAll('#munu-table');
                break;
            case 'fold':
                treeTable.foldAll('#munu-table');
                break;
        }
    })



    //单元格工具栏
    table.on('tool(munu-table)',function(obj){
             alert(obj.data.id);
        switch (obj.event) {
            case 'add':
                if(obj.data.grade==2){
                    layer.msg("暂不支持四级菜单添加")
                    return;
                }
                openAddModuleDialog(obj.data.grade+1,obj.data.id);
                break;
            case 'edit':
                openUpdateModuleDialog(obj.data.id);
                break;
            case 'del':
                /*监听不起作用*/
                openDeleteModuleDialog(obj.data.id);
                break;
        }
    })
    //添加目录对话框
    function openAddModuleDialog(grade,parentId) {
        layer.open({
            title:'菜单管理-目录添加',
            type:2,
            area:['700px','500px'],
            maxmin:true,
            content:path+"/module/addModulePage?grade="+grade+"&parentId="+parentId
        })
    }
    //修改菜单对话框
    function openUpdateModuleDialog(id){
        layer.open({
            title:'菜单管理-菜单更新',
            type:2,
            area:['700px','500px'],
            maxmin:true,
            content: path+"/module/updateModulePage?id="+id
        })
    }

    function openDeleteModuleDialog(id){
        alert(id);
        layer.confirm('您确认删除该记录吗？',{icon:3, title:"资源管理"}, function (data) {
            // 如果确认删除，则发送ajax请求
            $.post(path+ "/module/delete",{id:id},function (result) {
                // 判断是否成功
                if (result.code == 200) {
                    layer.msg("删除成功！",{icon:6});
                    // 刷新页面
                    window.location.reload();
                } else {
                    layer.msg(result.msg,{icon:5});
                }
            });
        });
    }

});