layui.use(['table','layer','http'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table
        http = layui.http;

       var path = http.path;

       //角色表格渲染
       var tableIns = table.render({
              elem:'#roleList',
              url:path+"/role/list",
              cellMinWidth:95,
              height:'full-125',
              page:true,
              limit:10,
              limits:[10,15,20],
              toolbar:'#toolbarDemo',
              id:'roleListTable',
              cols:[[
                     {type:'checkbox'},
                     {title:'编号',type:'numbers'},
                     {field:"id",hide:true},
                     {field:'roleName',title:'角色名',minWidth:50,align:'center'},
                     {field:'roleRemark',title:'备注',minWidth:50,align:'center'},
                     {field:'createDate',title:'创建时间',minWidth:50,align:'center'},
                     {field:'updateDate',title:'修改时间',minWidth:50,align:'center'},
                     {title:'操作',minWidth:150,templet:'#roleListBar',align:'center'}
              ]]
       })

       //多条件查询
       $(".search_btn").on("click",function(){
              table.reload('roleListTable',{
                     page:{
                            curr:1
                     },
                     where:{
                            roleName:$("input[name='roleName']").val()
                     }
              })
       })

       //头部工具栏处理添加与授权
       table.on('toolbar(roles)',function(obj){
              switch (obj.event) {
                     case 'add':
                            openAddOrUpdateRoleDialog();
                            break;
                     case 'grant':
                            openAddGrantDialog(table.checkStatus(obj.config.id).data);
                            break;
              }
       })
       //单元格工具栏事件
       table.on('tool(roles)',function(obj){
              if(obj.event=='edit'){
                     openAddOrUpdateRoleDialog(obj.data.id);
              }else if(obj.event=='del'){
                     layer.confirm("确认删除吗",{icon:3,title:'角色管理'},function(index){
                            $.post(path+"/role/delete",{id:obj.data.id},function(data){
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
       function openAddOrUpdateRoleDialog(id){
              var title="角色管理-角色添加";
              var url = path+"/role/addOrUpdateRolePage";
              if(id){
                     title = "角色管理-角色更新";
                     url=url+"?id="+id;
              }
              layer.open({
                     title:title,
                     type:2,
                     area:['700px','500px'],
                     maxmin:true,
                     content:url
              })
       }
       //授权对话框
       function openAddGrantDialog(datas){
              if(datas.length==0){
                     layer.msg("请选择待授权角色记录",{icon:5});
                     return;
              }
              if(datas.length>1){
                     layer.msg("暂不支持批量角色授权",{icon:5});
                     return;
              }
              layer.open({
                     title:'角色管理-角色授权',
                     type:2,
                     area:['700px','500px'],
                     maxmin: true,
                     content: path+"/role/toAddGrantPage?roleId="+datas[0].id
              })
       }
});
