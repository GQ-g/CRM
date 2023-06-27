layui.use(['table','layer',"form","http"],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        http = layui.http;
       var path = http.path;

       //用户列表
       var tableIns = table.render({
              elem:'#userList',
              url:path+"/user/list",
              cellMinWidth:95,
              height:'full-125',
              page:true,
              limit:10,
              limits:[10,15,20],
              id:'userListTable',
              toolbar:'#toolbarDemo',
              cols:[[
                     {type:'checkbox'},
                     {title:'编号',type:'numbers'},
                     {field:"id",hide:true},
                     {field:'userName',title:'用户名',minWidth:50,align:'center'},
                     {field:'email',title:'邮箱',minWidth:50,align:'center'},
                     {field:'phone',title:'手机号',minWidth:50,align:'center'},
                     {field:'trueName',title:'真实姓名',minWidth:50,align:'center'},
                     {field:'createDate',title:'创建时间',minWidth:50,align:'center'},
                     {field:'updateDate',title:'修改时间',minWidth:50,align:'center'},
                     {title:'操作',minWidth:150,templet:'#userListBar',align:'center'}
              ]]
       })

       //多条件查询
       $(".search_btn").on("click",function(){
              table.reload('userListTable',{
                     page:{
                            curr:1
                     },
                     where:{
                            userName:$("input[name='userName']").val(),
                            email:$("input[name='email']").val(),
                            phone:$("input[name='phone']").val()
                     }
              })
       })


       //头部工具栏处理添加与批量删除
       table.on('toolbar(users)',function(obj){
              switch (obj.event) {
                     case 'add':
                            openAddOrUpdateUserDialog();
                            break;
                     case 'del' :
                            delUser(table.checkStatus(obj.config.id).data);
                            break;
              }
       })
       //单元格工具栏事件
       table.on('tool(users)',function(obj){
              if(obj.event=='edit'){
                     openAddOrUpdateUserDialog(obj.data.id);
              }else if(obj.event=='del'){
                     layer.confirm("确认删除吗",{icon:3,title:'用户管理'},function(index){
                            $.post(path+"/user/delete",{ids:obj.data.id},function(data){
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
       //添加与修改对话框
       function openAddOrUpdateUserDialog(id){
              var title="用户管理-用户添加";
              var url = path+"/user/addOrUpdateUserPage";
              if(id){
                     title = "用户管理-用户更新";
                     url=url+"?id="+id;
              }
             layer.open({
                     title:title,
                     type:2,
                     area:['550px','450px'],
                     maxmin:true,
                     content:url
              });
              /*设置关闭弹框按钮*/
       };

       //删除
       function delUser(arr){
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
                     $.post(path+"/user/delete",ids,function(data){
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
