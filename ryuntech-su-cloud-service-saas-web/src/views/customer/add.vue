<template>
  <div class="add-customer-center">
    <div>
      <h2 v-if="form.customerId === ''">新增客户<hr/></h2>
      <h2 v-else>编辑客户<hr/></h2>
    </div>

  <!-- <el-dialog :title="dialogTitle" :before-close="handleClose" :visible.sync="dialogVisible" width="30%"> -->
    <!-- <el-form ref="form" :rules="rules" :model="form" status-icon label-position="right" label-width="80%"> -->
    <el-form ref="form" :rules="rules" :model="form" status-icon style="padding: 0px 46% 0px 26%;">
      <div>
        <h3>客户信息</h3>
        <el-form-item v-if="false" label="客户编号" prop="customerId" label-width="120px">
          <el-input v-model="form.customerId"/>
        </el-form-item>
        <!-- <el-form-item v-if="form.contractId != null" label="客户编号" prop="customerId" label-width="120px">
          <el-input v-model="form.customerId" :disabled="true" />
        </el-form-item> -->
        <el-form-item label="客户名称" prop="customerName" label-width="120px">
          <el-input v-model="form.customerName" placeholder="请输入客户名称" />
        </el-form-item>
      </div>
      
      <div>
        <div>
          <h3>联系人</h3>
          <h5>该客户主要联系人</h5>
        </div>
        <div>
          <el-form-item label="联系人" prop="contacts" label-width="120px">
            <el-input v-model="form.contacts" placeholder="请输入联系人" />
          </el-form-item>

          <el-form-item label="联系电话" prop="contactsPhone" label-width="120px">
            <el-input v-model="form.contactsPhone" placeholder="请输入联系电话" />
          </el-form-item>
        </div>
      </div>

      <div>
        <h3>地址</h3>
        <h5>该客户主要地址</h5>
        <el-form-item label="省市区" prop="cityId" label-width="120px">
          <el-input v-model="form.cityId" placeholder="请输入省市区" />
        </el-form-item>

        <el-form-item label="详细地址" prop="address" label-width="120px">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
      </div>
      <div>
        <h3>负责员工</h3>
        <h5>该客户的负责员工</h5>
        <el-form-item label="负责员工" prop="staffId" label-width="120px">
          <!-- <el-input v-model="form.staffName" placeholder="请输入详细地址" /> -->
          <div @click="toggerSelectEmployee" class="contract-select-btn">
                <span v-if="form.staffName === null" class="ph">
                    请选择负责员工
                </span>
                <span v-else class="ph">
                    {{form.staffName}}
                </span>
            </div>
          <!-- <el-input v-model="form.staffId" placeholder="请选择负责员工员工" @click="toggerSelectContract"/> -->
        </el-form-item>
      </div>
    </el-form>
    <div style="margin: 20px 50% 0px 32%;">
      <el-button @click="$router.push('/customer/list')">
        <h3 style="width: 100px;">取消</h3>
      </el-button>
      <el-button type="primary" @click="onSubmit('form')" style="margin-left:20px;">
        <h3 style="width: 100px;">提交</h3>
      </el-button>
    </div>

    <el-dialog title="选择负责员工" class="contract-select-dialog" :visible.sync="showSelectEmployee" width="90%">
      <div style="margin-bottom: 10px">
          <el-input style="width: 200px" size="mini" v-model="search.name" placeholder="请输入员工名称"></el-input>
          <el-button type="primary" size="mini" @click="getEmployeeList">搜索</el-button>
      </div>

      <el-table v-loading="listLoading" @current-change="handleCurrentChange" :data="employeeList" border fit highlight-current-row>
          <el-table-column label=" " width="40">
              <template slot-scope="scope" align="center">
                  <span v-if="checkedEmployee && checkedEmployee.employeeId == scope.row.employeeId">√</span>
              </template>
          </el-table-column>

          <el-table-column align="center" label="名字" width="180">
              <template slot-scope="scope">
                  {{ scope.row.name }}
              </template>
          </el-table-column>
          <el-table-column align="center" label="角色" min-width="150">
              <template slot-scope="scope">
                  {{ scope.row.rname }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="部门" min-width="100">
              <template slot-scope="scope">
                  {{ scope.row.departmentName }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="手机号" min-width="100">
              <template slot-scope="scope">
                  {{ scope.row.mobile }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="状态" min-width="100">
              <template slot-scope="scope">
                  <span v-if='scope.row.status==0'>正常</span>
                  <span v-if='scope.row.status==1'>禁用</span>
              </template>
          </el-table-column>

      </el-table>

      <pagination
              v-show="employeeTotal>0"
              :total="employeeTotal"
              :page.sync="listQuery.page"
              :limit.sync="listQuery.limit"
              @pagination="getEmployeeList"
      />

      <div slot="footer" class="dialog-footer" style="margin-right: 30px">
          <el-button type="primary" @click="checkEmployee">　确 定　</el-button>
          <el-button @click="showSelectEmployee = false">　取 消　</el-button>
      </div>
    </el-dialog>

  <!-- </el-dialog> -->
  </div>
</template>

<script>
import { save, edit, getEmployeeList } from '@/api/customer'

export default {
  // 父组件向子组件传值，通过props获取。
  // 一旦父组件改变了`sonData`对应的值，子组件的`sonData`会立即改变，通过watch函数可以实时监听到值的变化
  // `props`不属于data，但是`props`中的参数可以像data中的参数一样直接使用
  // props: ['add'],

  data() {
    return {
      showSelectEmployee: false,
      dialogVisible: false,
      showTitle: '',
      form: {
        customerId: '',
        customerName: '',
        contacts: '',
        contactsPhone: '',
        cityId: '',
        address: '',
        staffId: '',
        provinceId: '',
        districtId: '',
        companyId: '',
        companyName: '',
        department: '',
        staffName: null
      },
      listQuery: {
        page: 1,
        limit: 20
      },
      search: {
        name: undefined
      },
      listLoading: false,
      employeeList: [],
      employeeTotal: 0,
      checkedEmployee: undefined,
      rules: {
        customerName: [{ required: true, trigger: 'blur', message: '请输入客户名称' }],
        contacts: [{ required: true, trigger: 'blur', message: '请输入联系人' }],
        contactsPhone: [{ required: true, trigger: 'blur', message: '请输入联系电话' },
                          { pattern: /^1[34578]\d{9}$/, message: '请正确输入电话', trigger: 'blur' }],
        cityId: [{ required: true, trigger: 'blur', message: '请输入城市' }],
        address: [{ required: true, trigger: 'blur', message: '请输入详细地址' }],
        staffId: [{ required: true, trigger: 'blur', message: '请选择负责员工' }]
      }
    }
  },
  created(){
    this.showTitle = this.$route.params.title
    if(typeof(this.$route.params.data) !== "undefined") {
      this.form = this.$route.params.data
    }
  },
  methods: {
    handleCurrentChange(val) {
      this.checkedEmployee = val
    },
    getEmployeeList() {
      this.listLoading = true
      getEmployeeList(this.listQuery, this.search).then(response => {
        this.employeeList = response.data.records
        this.employeeTotal = response.data.total
        this.listLoading = false
      })
    },
    toggerSelectEmployee(){
      this.showSelectEmployee = !this.showSelectEmployee
      if (this.showSelectEmployee) {
        this.getEmployeeList()
      }
    },
    checkEmployee() {
        if (!this.checkedEmployee) this._notify("请先选择员工！",'e')
        this.form.staffId = this.checkedEmployee.employeeId
        this.form.staffName = this.checkedEmployee.name
        this.form.department = this.checkedEmployee.departmentName
        // this.form.companyId = this.checkEmployee.companyId
        // this.form.companyName = this.checkEmployee.companyName
        this.showSelectEmployee = false
      },
    _notify(message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    clearForm() {
      this.form.customerId = '',
      this.form.customerName = '',
      this.form.contacts = '',
      this.form.contactsPhone = '',
      this.form.cityId = '',
      this.form.address = '',
      this.form.staffId = '',
      this.form.provinceId = '',
      this.form.districtId = '',
      this.form.companyId = '',
      this.form.companyName = ''
    },
    handleClose() {
      this.clearForm()
      // this.dialogVisible = false
      this.$router.push('/customer/list')
    },
    onSubmit(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          if (this.form.customerId === '') {
            save(this.form).then(response => {
              if (response.tcode === 200) {
                this._notify(response.msg, 'success')
                this.clearForm()
                this.$router.push('/customer/list')
                // this.$emit('sonStatus', true)
                // this.dialogVisible = false
              } else {
                this._notify(response.msg, 'error')
              }
            })
          } else {
            edit(this.form).then(response => {
              if (response.tcode === 200) {
                this._notify(response.msg, 'success')
                this.clearForm()
                this.$router.push('/customer/list')
                // this.$emit('sonStatus', true)
                // this.dialogVisible = false
              } else {
                this._notify(response.msg, 'error')
              }
            })
          }
        } else {
          this.$message('error submit!!')
          return false
        }
      })
    }

  }
}
</script>

<style lang="css">
  .line {
    text-align: center;
  }
  .add-customer-center {
    padding: 20px 20px 20px 70px;
  }
  .contract-select-btn{
        width: 330px;
        border: 1px solid #cccccc;
        border-radius: 4px;
        padding: 0px 10px;
        display: flex;
        justify-content: space-between;
        cursor: pointer;
        font-size: 12px;
        span.ph{
            color: #333;
        }
    }
</style>

