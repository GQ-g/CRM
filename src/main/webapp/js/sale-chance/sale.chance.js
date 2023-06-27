layui.use(['table','layer','http'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        http = layui.http;
    var path = http.path;

    var tableIns = table.render({
        elem:'#saleChanceList',
        url: path+"/sale_chance/list",
        cellMinWidth:95,
        height:'full-125',
        page:true,
        limits:[10,15,20,25,30],
        limit:10,
        id:'saleChanceListTable',
        cols : [[
            {type: "checkbox", fixed:"center"},
            {title:'编号',type:'numbers',fixed:"true"},
            {field: "id",hide:true,fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'uname', title: '指派人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'state', title: '分配状态',align:'center',templet:function(d){
                return paresStatus(d.state);
            }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function(d){
                return parseDevResult(d.devResult);
            }},
            {title:'操作',align:'center',templet:'#saleChanceListBar',width:150}
        ]],
        toolbar:'#toolbarDemo'
    })
    //解析状态含义
    function paresStatus(state){
        if(state==0){
            return "<div style='color:yellow'>未分配</div>";
        }else if(state==1){
            return "<div style='color:green'>已分配</div>";
        }else {
            return "<div style='color:red'>未知</div>"
        }
    }
    //解析开发状态
    function parseDevResult(value){
        /**
         * 0-未开发
         * 1-开发中
         * 2-开发成功
         * 3-开发失败
         */
        if(value==0){
            return "<div style='color: yellow'>未开发</div>";
        }else if(value==1){
            return "<div style='color: #00FF00;'>开发中</div>";
        }else if(value==2){
            return "<div style='color: #00B83F'>开发成功</div>";
        }else if(value==3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }
    //搜索查询
    $(".search_btn").on('click',function(){
        //重载表格
        table.reload('saleChanceListTable', {
            where: { //设定异步数据接口的额外参数，任意设
                customerName: $("input[name='customerName']").val(),
                createMan: $("input[name='createMan']").val(),
                state:$("#state").val()
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        }); //只重载数据
    })
    //头部工具栏
    table.on('toolbar(saleChances)',function(obj){
        var event = obj.event;
        switch(event){
            case "add" :
                openAddOrUpdateSaleChanceDialog();
                break;
            case "del":
                /*获取选中的数据*/
                delSaleChance(table.checkStatus(obj.config.id).data);
                break;

        }
    })
    //单元格工具栏
    table.on('tool(saleChances)',function(obj){
        console.log(obj);
        var event = obj.event;
        if(event=="edit"){
            openAddOrUpdateSaleChanceDialog(obj.data.id);
        }else if(event=="del"){
            layer.confirm("确认删除吗？",{icon:3,title:'机会数据管理'},function(index){
                $.post(path+"/sale_chance/delete",{ids:obj.data.id},function(data){
                    if(data.code==200){
                        layer.msg("删除成功");
                        /*刷新表格*/
                        tableIns.reload();
                    }else {
                        layer.msg(data.msg);
                    }
                },"json")
            })
        }
    });
    //添加或修改对话框
    function openAddOrUpdateSaleChanceDialog(sid){
        var title = "营销机会管理-机会添加";
        var url = path+"/sale_chance/addOrUpdateSaleChancePage";
        if(sid){
            title="营销机会管理-机会修改";
            console.log(sid);
            url=url+"?id="+sid;
        }
        layer.open({
            title:title,
            type:2,
            area:['700px','500px'],
            /*最大化，最小化*/
            maxmin:true,
            content:url
        })
    }
    //删除
    function delSaleChance(arr){
        if(arr.length==0){
            layer.msg("请选择要删除的记录")
            return;
        }
        layer.confirm("确认删除选中的记录吗",{
            btn:['确认','取消']
        },function(index){
            var ids = "ids=";
            for(var i=0;i<arr.length;i++){
                if(i<arr.length-1){
                    ids=ids+arr[i].id+"&ids=";
                }else {
                    ids=ids+arr[i].id;
                }

            }
            $.post(path+"/sale_chance/delete",ids,function(data){
                if(data.code==200){
                    layer.msg("删除成功");
                    tableIns.reload();
                }else {
                    layer.msg(data.msg);
                }
            },"json");
        })
    }
});
