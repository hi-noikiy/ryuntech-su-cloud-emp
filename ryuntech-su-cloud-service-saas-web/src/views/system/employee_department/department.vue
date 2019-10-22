<!--Author liugg 2019年10月22日14:10:16-->
<template>
    <div class="app-container">
        <bannerHeader active="two"/>
        <div class="operator-banner">
            <el-button type="primary" @click="addArea()">新增部门</el-button>
        </div>
        <div class="area-container">
            <div class="area-item" :class="'area-item-level0' + item1.level" v-for="item1 in area">
                <div class="item">
                    <div class="item-name" @click="item1.opened = !item1.opened" :class="getItemClassObj(item1)"><i class="status-icon"></i><span>{{item1.department_name}}</span></div>
                    <div class="button-container">
                        <span class="button-item add" @click="addArea(item1)">新增子部门</span>
                        <span class="button-item edit" @click="editArea(item1)">编辑</span>
                        <span class="button-item delete" @click="deleteArea(item1)">删除</span>
                    </div>
                </div>
                <div class="area-item" :class="'area-item-level0' + item2.level" v-if="item1.children && item1.opened" v-for="item2 in item1.children">
                    <div class="item">
                        <div class="item-name" @click="item2.opened = !item2.opened" :class="getItemClassObj(item2)"><i class="status-icon"></i><span>{{item2.department_name}}</span></div>
                        <div class="button-container">
                            <span class="button-item add" @click="addArea(item2)">新增子部门</span>
                            <span class="button-item edit" @click="editArea(item2)">编辑</span>
                            <span class="button-item delete" @click="deleteArea(item2)">删除</span>
                        </div>
                    </div>
                    <div class="area-item" :class="'area-item-level0' + item3.level" v-if="item2.children && item2.opened" v-for="item3 in item2.children">
                        <div class="item">
                            <div class="item-name" @click="item3.opened = !item3.opened" :class="getItemClassObj(item3)"><i class="status-icon"></i><span>{{item3.department_name}}</span></div>
                            <div class="button-container">
                                <span class="button-item add" @click="addArea(item3)">新增子部门</span>
                                <span class="button-item edit" @click="editArea(item3)">编辑</span>
                                <span class="button-item delete" @click="deleteArea(item3)">删除</span>
                            </div>
                        </div>
                        <div class="area-item" :class="'area-item-level0' + item4.level" v-if="item3.children && item3.opened" v-for="item4 in item3.children">
                            <div class="item">
                                <div class="item-name" @click="item4.opened = !item4.opened" :class="getItemClassObj(item4)"><i class="status-icon"></i><span>{{item4.department_name}}</span></div>
                                <div class="button-container">
                                    <span class="button-item add" @click="addArea(item4)">新增子部门</span>
                                    <span class="button-item edit" @click="editArea(item4)">编辑</span>
                                    <span class="button-item delete" @click="deleteArea(item4)">删除</span>
                                </div>
                            </div>
                            <div class="area-item" :class="'area-item-level0' + item5.level" v-if="item4.children && item4.opened" v-for="item5 in item4.children">
                                <div class="item">
                                    <div class="item-name" @click="item5.opened = !item5.opened" :class="getItemClassObj(item5)"><i class="status-icon"></i><span>{{item5.department_name}}</span></div>
                                    <div class="button-container" > <!-- 最多5级 -->
                                        <span class="button-item add" @click="addArea(item5)" v-if="false" >新增子部门555</span>
                                        <span class="button-item edit" @click="editArea(item5)">编辑</span>
                                        <span class="button-item delete" @click="deleteArea(item5)">删除</span>
                                    </div>
                                </div>
                                <div class="area-item" :class="'area-item-level0' + item6.level" v-if="item5.children && item5.opened" v-for="item6 in item5.children">
                                    <div class="item">
                                        <div class="item-name" @click="item6.opened = !item6.opened" :class="getItemClassObj(item6)"><i class="status-icon"></i><span>{{item6.department_name}}</span></div>
                                        <div class="button-container">
                                            <span class="button-item add" @click="addArea(item6)">新增子部门</span>
                                            <span class="button-item edit" @click="editArea(item6)">编辑</span>
                                            <span class="button-item delete" @click="deleteArea(item6)">删除</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <el-dialog
                :visible.sync="addDialogVisible"
                width="499px"
                class="Department-dialog">
            <div slot="title" class="dialog-h">新增部门</div>
            <div class="input-item">
                <label>部门名称</label>
                <el-input v-model="addForm.departmentName"></el-input>
            </div>
            <div class="input-item">
                <label>上级部门</label>
                <el-cascader :disabled="disabledSelect"
                             v-model="addForm.path"
                             placeholder="根部门"
                             :options="area"
                             :props="{ checkStrictly: true, value: 'department_id', label: 'department_name' }"
                             clearable></el-cascader>
            </div>
            <div class="input-item footer" slot="footer">
                <el-button style="width: 100px" @click="addDialogVisible = false">取消</el-button>
                <el-button type="primary" style="width: 100px" @click="submitAdd">确定</el-button>
            </div>
        </el-dialog>

        <el-dialog
                :visible.sync="editDialogVisible"
                width="499px"
                class="Department-dialog">
            <div slot="title" class="dialog-h">编辑部门</div>
            <div class="input-item">
                <label>部门名称</label>
                <el-input v-model="editForm.departmentName"></el-input>
            </div>
            <div class="input-item footer" slot="footer">
                <el-button style="width: 100px" @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" style="width: 100px" @click="submitEdit">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
  import { getDepartmentList, addDepartment, editDepartment, deleteDepartment } from '@/api/system/department'
  import bannerHeader from './bannerHeader.vue'
  export default {
    name: 'EmployeeDepartmentDepartment',
    components: { bannerHeader },
      data() {
      return {
        area:[],
        /*
        {
       department_id: 2,
       department_name: '部门01',
       level: '级别',
       pid: '1',
       children: [
         {},{}
       ]}
         */
        addDialogVisible: false,
        editDialogVisible: false,
        addForm: {
          departmentName: undefined,
          pid: undefined,
          path: []
        },
        editForm: {
          id: undefined,
          departmentName: undefined
        },
        disabledSelect: false
      }
    },
    created() {
      this.getDepartmentList()
    },
    methods:{
      getDepartmentList() {
        getDepartmentList().then( res => {
          this.area = res.data
        }).catch( er => { console.log( er )})
      },
      addArea(item) {
        this.disabledSelect = true
        this.addForm.departmentName = undefined
        this.addForm.pid = undefined
        if (item) {
          this.addForm.pid = item.department_id
          this.addForm.path = item.path
        } else {
          this.addForm.pid = 0
          this.addForm.path = []
        }
        this.addDialogVisible = true
      },
      editArea(item) {
        this.disabledSelect = true
        this.editForm.departmentName = item.department_name
        this.editForm.id = item.department_id
        this.editDialogVisible = true
      },
      deleteArea(item) {
        if (item.children && item.children.length > 0) {
          this.$message.error('当前部门有子部门，不能删除')
          return ;
        }
        deleteDepartment({ id: item.department_id }).then(res => {
          this.getDepartmentList()
          this.$message.success('删除成功')
        }).catch( re =>{ console.log(re)})
      },
      submitAdd() {
        if (!this.addForm.departmentName) return this.$message.error("部门名称不能为空")
        addDepartment(this.addForm).then( res => {
          this.getDepartmentList()
          this.$message.success('操作成功')
          this.addDialogVisible = false
        }).catch( er => { console.log(er) })
      },
      submitEdit() {
        editDepartment(this.editForm).then( res => {
          this.getDepartmentList()
          this.$message.success('操作成功')
          this.editDialogVisible = false
        }).catch( re =>{ console.log(re)})
      },
      getItemClassObj(obj) {
        return {
          opened: obj.opened && obj.children && obj.children.length > 0,
          closed: (!obj.opened) && obj.children && obj.children.length > 0,
          normal: !obj.children || obj.children == 0
        }
      }
    }

  }
</script>

<style lang="scss" scoped>
    $--area-padding: 30px;
    $--area-border-color: rgba(0,0,0,0.08);
    .operator-banner{
        margin-bottom: 20px;
        display: flex;
        justify-content: flex-end;
        padding-right: 20px;
    }
    .area-container{
        font-size: 14px;
        font-weight:400;
        color:rgba(57,69,81,1);
        user-select: none;
        .area-item:nth-child(1) > .item:nth-child(1) {
            border-top: 1px solid $--area-border-color;
        }
        .item{
            background-color: #ffffff;
            /*margin-top: 1px;*/
            display: flex;
            align-items: center;
            justify-content: space-between;
            border-bottom: 1px solid $--area-border-color;
            height: 52px;
            &:hover{
                background:rgba(249,250,251,1);;
                .button-container{
                    visibility: visible;
                }
            }
            .item-name{
                display: flex;
                align-items: center;
                .status-icon{
                    width: 19px;
                    height: 19px;
                    display: inline-block;
                }
                span{
                    text-indent: 25px;
                    display: inline-block;
                    background: url("~@/assets/department/dir.png") no-repeat;
                    background-position: 2px center;
                    background-size: 19px 12px;
                }
                &.opened{
                    cursor: pointer;
                    .status-icon{
                        background: url("~@/assets/department/opened.png") no-repeat;
                        background-size: 12px;
                        background-position: center center;
                    }
                }
                &.closed{
                    cursor: pointer;
                    .status-icon{
                        background: url("~@/assets/department/closed.png") no-repeat;
                        background-size: 12px;
                        background-position: center center;
                    }
                }
                &.normal{
                    cursor: default;
                }
            }
            .button-container{
                visibility: hidden;
                display: flex;
                align-items: center;
                font-size: 14px;
                color: #394551;
                .button-item{
                    margin-right: 10px;
                    cursor: pointer;
                    text-indent: 16px;
                    background-repeat: no-repeat;
                    background-position: left center;
                    background-size: 13px;
                }
            }
        }
        .area-item{
            .area-item-level02 .item{
                padding-left: $--area-padding * 1;
            }
            .area-item-level03 .item{
                padding-left: $--area-padding * 2;
            }
            .area-item-level04 .item{
                padding-left: $--area-padding * 3;
            }
            .area-item-level05 .item{
                padding-left: $--area-padding * 4;
            }
            .area-item-level06 .item{
                padding-left: $--area-padding * 5;
            }
            .area-item-level07 .item{
                padding-left: $--area-padding * 6;
            }
        }
    }
    .Department-dialog{
        box-shadow:0px 10px 30px 0px rgba(0,0,0,0.08);
        border-radius:3px;
        border:1px solid rgba(0,0,0,0.08);
        .dialog-h{
            font-size: 16px;
            font-weight: 500;
        }
        .input-item{
            display: flex;
            align-items: center;
            & + .input-item{
                margin-top: 20px;
            }
            label{
                font-size:14px;
                font-weight:400;
                color:rgba(57,69,81,1);
                width: 60px;
            }
            .el-input,.el-cascader{
                flex: 1;
                margin-left: 10px;
            }
            .el-button{
                float: right;
            }
            &.footer{
                justify-content: flex-end;
            }
        }
    }
</style>
