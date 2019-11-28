<template>
  <div class="add-contract-center">
    <div>
      <h2 v-if="form.contractId === ''">新增合同<hr/></h2>
      <h2 v-else>编辑合同<hr/></h2>
    </div>


  <!-- <el-dialog :title="dialogTitle" :before-close="handleClose" :visible.sync="dialogVisible" width="70%"> -->
    <el-form ref="form" :rules="rules" :model="form" status-icon style="margin: 0px 4% 0px 2%;width: 94%;border: 1px solid #dddddd;float:left;">
      <div class="d-contract-edit-form-first">
        <div style="margin: 0px 80% 10px 2%;">
          <h3>合同详细信息</h3>
        </div>

        <el-form-item v-if="false" label="合同编号" prop="contractId">
          <el-input v-model="form.contractId" :disabled="true" />
        </el-form-item>

        <el-form-item label="合同名称" prop="contractName">
          <el-input v-model="form.contractName" placeholder="请输入合同名称" />
        </el-form-item>

        <el-form-item label="合同编码" prop="contractCode">
          <el-input v-model="form.contractCode" :disabled="true" placeholder="系统自动生成" />
        </el-form-item>

        <el-form-item label="合同金额" prop="contractAmount">
          <el-input v-model="form.contractAmount" placeholder="请输入合同金额" />
        </el-form-item>

        <el-form-item label="合同签订日期" prop="contractTime">
          <el-date-picker
            v-model="form.contractTime"
            style="width: 100%"
            type="datetime"
            placeholder="选择签订日期"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>

        <el-form-item label="联系人" prop="contacts">
          <el-input v-model="form.contacts" placeholder="请输入联系人" />
        </el-form-item>

        <el-form-item label="联系电话" prop="contactsPhone">
          <el-input v-model="form.contactsPhone" placeholder="请输入联系电话" />
        </el-form-item>
      </div>

      <div class="d-contract-edit-form-second">
        <div style="margin: 0px 80% 10px 2%;">
          <h3>签约客户</h3>
        </div>
        <el-form-item label="客户" prop="staffId" label-width="120px">
          <div @click="toggerSelectCustomer" class="contract-select-btn">
                <span v-if="form.customerName === ''" class="ph">
                    请选择客户
                </span>
                <span v-else class="ph">
                    {{form.customerName}}
                </span>
            </div>
        </el-form-item>
      </div>

      <div class="d-contract-edit-form-second">
        <div style="margin: 0px 80% 10px 2%;">
          <h3>负责员工</h3>
        </div>
        <el-form-item label="员工" prop="staffId" label-width="120px">
          <div @click="toggerSelectEmployee" class="contract-select-btn">
                <span v-if="form.staffName === ''" class="ph">
                    请选择员工
                </span>
                <span v-else class="ph">
                    {{form.staffName}}
                </span>
            </div>
        </el-form-item>
      </div>

      <div class="d-contract-edit-form-third">
        <el-form-item label="合同附件">
          <el-upload
            class="avatar-uploader"
            :action="localUpload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imgURL" :src="imgURL" class="avatar">
            <template v-else>
              <i class="el-icon-plus avatar-uploader-icon" />
              <div class="el-upload__text">点我上传合同附件</div>
            </template>
          </el-upload>
        </el-form-item>
      </div>

      <div class="d-contract-edit-form-fourth">
        <el-form-item label="回款计划">
          <el-button :disabled="tianJiaHuiKuan" @click="addItem" type="text">添加回款计划</el-button>
        </el-form-item>

        <div v-for="(item, index) in form.receivableCollectionPlanDTOs" :key="index" class="d-contract-edit-form-div">
            <el-form-item label="计划日期" :prop="'receivableCollectionPlanDTOs.' + index + '.planTime'" :rules="formRules.planTime">
                <el-date-picker
                  v-model="item.planTime"
                  type="datetime"
                  placeholder="请选择计划日期"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  format="yyyy-MM-dd HH:mm:ss"
                  @input="change1($event)"
                />
              </el-form-item>
            <el-form-item label="计划金额" :prop="'receivableCollectionPlanDTOs.' + index + '.planAmount'" :rules="formRules.planAmount">
                <el-input v-model="item.planAmount" placeholder="总额应等于小于合同金额" @input="change2(item)"></el-input>
            </el-form-item>
            <el-form-item label="备注" :prop="'receivableCollectionPlanDTOs.' + index + '.remakes'" :rules="formRules.remakes" style="margin-left: 3%;">
                <el-input v-model="item.remakes" placeholder="请输入备注" @input="change3($event)"></el-input>
            </el-form-item>
            <el-form-item style="margin-left:-110px;">
                <a class="el-icon-delete" @click="deleteItem(item, index)"></a>
            </el-form-item>
        </div>
      </div>

    </el-form>
    
    <div style="margin: 38% 50% 0px 32%;">
      <el-button @click="$router.push('/contract/list')">
        <h3 style="width: 100px;">取消</h3>
      </el-button>
      <el-button type="primary" @click="onSubmit('form')" style="margin-left:20px;">
        <h3 style="width: 100px;">提交</h3>
      </el-button>
    </div>

    <el-dialog title="选择负责员工" class="contract-select-dialog" :visible.sync="showSelectEmployee" width="90%">
      <div style="margin-bottom: 10px">
          <el-input style="width: 200px" size="mini" v-model="employeeSearch.name" placeholder="请输入员工名称"></el-input>
          <el-button type="primary" size="mini" @click="getEmployeeList">搜索</el-button>
      </div>

      <el-table v-loading="employeeListLoading" @current-change="handleCurrentChangeEm" :data="employeeList" border fit highlight-current-row>
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
              :page.sync="employeeListQuery.page"
              :limit.sync="employeeListQuery.limit"
              @pagination="getEmployeeList"
      />

      <div slot="footer" class="dialog-footer" style="margin-right: 30px">
          <el-button type="primary" @click="checkEmployee">　确 定　</el-button>
          <el-button @click="showSelectEmployee = false">　取 消　</el-button>
      </div>
    </el-dialog>

    <el-dialog title="选择客户" class="contract-select-dialog" :visible.sync="showSelectCustomer" width="90%">
      <div style="margin-bottom: 10px">
          <el-input style="width: 200px" size="mini" v-model="customerSearch.name" placeholder="请输入客户名称"></el-input>
          <el-button type="primary" size="mini" @click="getEmployeeList">搜索</el-button>
      </div>

      <el-table v-loading="customerListLoading" @current-change="handleCurrentChangeCu" :data="customerList" border fit highlight-current-row>
          <el-table-column label=" " width="40">
              <template slot-scope="scope" align="center">
                  <span v-if="checkedCustomer && checkedCustomer.customerId == scope.row.customerId">√</span>
              </template>
          </el-table-column>

          <el-table-column align="center" label="客户编号" width="180">
              <template slot-scope="scope">
                  {{ scope.row.customerId }}
              </template>
          </el-table-column>
          <el-table-column align="center" label="客户名称" min-width="150">
              <template slot-scope="scope">
                  {{ scope.row.customerName }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="联系人" min-width="100">
              <template slot-scope="scope">
                  {{ scope.row.contacts }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="联系电话" min-width="100">
              <template slot-scope="scope">
                  {{ scope.row.contactsPhone }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="负责员工" min-width="100">
              <template slot-scope="scope">
                  {{ scope.row.staffName }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="部门" min-width="100">
              <template slot-scope="scope">
                  {{ scope.row.department }}
              </template>
          </el-table-column>
      </el-table>

      <pagination
              v-show="customerTotal>0"
              :total="customerTotal"
              :page.sync="customerListQuery.page"
              :limit.sync="customerListQuery.limit"
              @pagination="getCustomerList"
      />

      <div slot="footer" class="dialog-footer" style="margin-right: 30px">
          <el-button type="primary" @click="checkCustomer">　确 定　</el-button>
          <el-button @click="showSelectCustomer = false">　取 消　</el-button>
      </div>
    </el-dialog>
  <!-- </el-dialog> -->
  </div>
</template>


<script>
import { save, edit, upload } from '@/api/contract'
import { parseTime } from '@/utils/index'
import { getEmployeeList, getList as getCustomerList } from '@/api/customer'

export default {
  data() {
    return {
      employeeListLoading: false,
      showSelectEmployee: false,
      employeeList: [],
      employeeTotal: 0,
      employeeSearch: {
        name: undefined
      },
      employeeListQuery: {
        page: 1,
        limit: 20
      },
      checkedEmployee: undefined,

      customerListLoading: false,
      showSelectCustomer: false,
      customerList: [],
      customerTotal: 0,
      customerSearch: {
        name: undefined
      },
      customerListQuery: {
        page: 1,
        limit: 20
      },
      checkedCustomer: undefined,

      tianJiaHuiKuan: false,
      // dialogVisible: false,
      // dialogTitle: '合同详细信息',
      localUpload: upload,
      imgURL: '',
      // backPlanForm: [{
      //   planTime: '',
      //   planAmount: '',
      //   remakes: ''
      // }],
      form: {
        contractId: '',
        contractName: '',
        contractCode: '',
        contractAmount: '',
        contractTime: '',
        contacts: '',
        contactsPhone: '',
        customerId: '',
        customerName: '',
        staffId: '',
        staffName: '',
        department: '',
        attachmentCode: '',
        url: '',
        // 回款计划
        // receivableCollectionPlanDTOs: [{
        //   planTime: '',
        //   planAmount: '',
        //   status: '',
        //   remakes: ''
        // }]
        receivableCollectionPlanDTOs: []
      },
      formRules: {
        planTime: [],
        planAmount: [],
        remakes: []
      },
      // formRules: {
      //   planTime: [{required: true, message: '选择计划日期', trigger: 'blur'}],
      //   planAmount: [{required: true, message: '计划金额', trigger: 'blur'}],
      //   remakes: [{required: true, message: '备注', trigger: 'blur'}]
      // },
      rules: {
        contractId: [{ required: false, trigger: 'blur', message: '请输入合同编号' }],
        contractName: [{ required: true, trigger: 'blur', message: '请输入合同名' }],
        contractCode: [{ required: false, trigger: 'blur', message: '请输入合同编码' },
                        { pattern: /^([1-9][0-9]*)$/, message: '合同编码为纯数字', trigger: 'blur' }],
        contractAmount: [{ required: true, trigger: 'blur', message: '请输入合同金额' },
                          { pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请正确输入金额', trigger: 'blur' }],
        contractTime: [{ required: true, trigger: 'blur', message: '请输入签订日期' }],
        contacts: [{ required: true, trigger: 'blur', message: '请输入联系人' }],
        contactsPhone: [{ required: true, trigger: 'blur', message: '请输入联系电话' },
                          { pattern: /^1[34578]\d{9}$/, message: '请正确输入电话', trigger: 'blur' }],
        customerId: [{ required: true, trigger: 'blur', message: '请选择客户' }],
        staffId: [{ required: true, trigger: 'blur', message: '请选择负责员工' }]
      }
    }
  },
  created(){
    this.showTitle = this.$route.params.title
    if(typeof(this.$route.params.data) !== "undefined") {
      this.form = this.$route.params.data
      this.imgURL = this.form.url
    }
    let sum = 0
    this.form.receivableCollectionPlanDTOs.forEach((item) => {
      sum += parseFloat(item.planAmount)
      if(parseFloat(sum) > parseFloat(this.form.contractAmount)) {
        this.tianJiaHuiKuan = true
        item.planAmount = ''
      } else if(parseFloat(sum) == parseFloat(this.form.contractAmount)) {
        this.tianJiaHuiKuan = true
      } else {
        this.tianJiaHuiKuan = false
      }
    })
  },
  // watch: {
  //   'this.form': function(newVal, oldVal) {
  //     this.form = newVal
  //     console.log(this.form + '=====================================================================')
  //     this.imgURL = this.form.url
  //     // this.dialogVisible = true
  //     if (newVal.id != null) {
  //       this.dialogTitle = '合同详细信息'
  //     } else {
  //       this.dialogTitle = '合同详细信息'
  //     }
  //     let sum = 0
  //     this.form.receivableCollectionPlanDTOs.forEach((item) => {
  //       sum += parseFloat(item.planAmount)
  //       if(parseFloat(sum) > parseFloat(this.form.contractAmount)) {
  //         this.tianJiaHuiKuan = true
  //         item.planAmount = ''
  //       } else if(parseFloat(sum) == parseFloat(this.form.contractAmount)) {
  //         this.tianJiaHuiKuan = true
  //       } else {
  //         this.tianJiaHuiKuan = false
  //       }
  //     })
  //   }
  // },
  methods: {
    handleCurrentChangeEm(val) {
      this.checkedEmployee = val
    },
    handleCurrentChangeCu(val) {
      this.checkedCustomer = val
    },
    getEmployeeList() {
      this.employeeListLoading = true
      getEmployeeList(this.employeeListQuery, this.employeeSearch).then(response => {
        this.employeeList = response.data.records
        this.employeeTotal = response.data.total
        this.employeeListLoading = false
      })
    },
    getCustomerList() {
      this.customerListLoading = true
      getCustomerList(this.customerListQuery, this.customerSearch).then(response => {
        this.customerList = response.data.records
        this.customerTotal = response.data.total
        this.customerListLoading = false
      })
    },
    toggerSelectEmployee(){
      this.showSelectEmployee = !this.showSelectEmployee
      if (this.showSelectEmployee) {
        this.getEmployeeList()
      }
    },
    toggerSelectCustomer(){
      this.showSelectCustomer = !this.showSelectCustomer
      if (this.showSelectCustomer) {
        this.getCustomerList()
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
    checkCustomer() {
      if (!this.checkedCustomer) this._notify("请先选择客户！",'e')
      this.form.customerId = this.checkedCustomer.customerId
      this.form.customerName = this.checkedCustomer.customerName
      // this.form.companyId = this.checkEmployee.companyId
      // this.form.companyName = this.checkEmployee.companyName
      this.showSelectCustomer = false
    },
    change1() {
      this.formRules.planTime = [{required: false, message: '选择计划日期', trigger: 'blur'}]
      this.$forceUpdate()
    },
    change2(item) {
      this.formRules.planAmount = [{required: false, message: '计划金额', trigger: 'blur'}]
      this.$forceUpdate()
      let sum = 0
      this.form.receivableCollectionPlanDTOs.forEach((item) => {
        sum += parseFloat(item.planAmount)
        if(parseFloat(sum) > parseFloat(this.form.contractAmount)) {
          this.tianJiaHuiKuan = true
          item.planAmount = ''
        } else if(parseFloat(sum) == parseFloat(this.form.contractAmount)) {
          this.tianJiaHuiKuan = true
        } else {
          this.tianJiaHuiKuan = false
        }
      })
    },
    change3() {
      this.formRules.remakes = [{required: false, message: '备注', trigger: 'blur'}]
      this.$forceUpdate()
    },
    addItem() {
      this.formRules.planTime = [{required: true, message: '选择计划日期', trigger: 'blur'}]
      this.formRules.planAmount = [{required: true, message: '计划金额', trigger: 'blur'}],
      this.formRules.remakes = [{required: true, message: '备注', trigger: 'blur'}]
      if(typeof(this.form.receivableCollectionPlanDTOs) === 'undefined') {
        this.form.receivableCollectionPlanDTOs = []
      }
      this.form.receivableCollectionPlanDTOs.push({
          planTime: '',
          planAmount: '',
          status: '',
          remakes: ''
      })
      this.form.receivableCollectionPlanDTOs.contractId = this.form.contractId
      this.$forceUpdate()
    },
    deleteItem(item, index) {
      this.tianJiaHuiKuan = false
      index = this.form.receivableCollectionPlanDTOs.indexOf(item);
        if (index !== -1) {
          this.form.receivableCollectionPlanDTOs.splice(index, 1)
          this.$forceUpdate()
        }
    },
    clearForm() {
      this.form.contractId = null
      this.form.contractName = null
      this.form.contractCode = null
      this.form.contractAmount = null
      this.form.contractTime = null
      this.form.contacts = null
      this.form.contactsPhone = null
      this.form.customerId = null
      this.form.staffId = null
      this.form.attachmentCode= null
      this.form.url = null
      this.form.createTime = parseTime(new Date(), '')
    },
    handleClose() {
      this.clearForm()
      this.$refs.form.clearValidate()
      this.$router.push('/contract/list')
      // this.dialogVisible = false
    },
    onSubmit(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          if (this.form.contractId === '') {
            save(this.form).then(response => {
              if (response.tcode === 200) {
                this._notify(response.msg, 'success')
                this.clearForm()
                // this.$emit('sonStatus', true)
                // this.dialogVisible = false
                this.tianJiaHuiKuan = false
                this.$router.push('/contract/list')
              } else {
                this._notify(response.msg, 'error')
              }
            })
          } else {
            edit(this.form).then(response => {
              if (response.tcode === 200) {
                this._notify(response.msg, 'success')
                this.clearForm()
                // this.$emit('sonStatus', true)
                // this.dialogVisible = false
                this.tianJiaHuiKuan = false
                this.$router.push('/contract/list')
              } else {
                this._notify(response.msg, 'error')
              }
            })
          }
        } else {
          this.$message('请完善合同信息 !')
          return false
        }
      })
    },
    // 文件上传成功的钩子函数
    handleAvatarSuccess(res, file, fileList) {
      if (res.tcode == 200) {
        this._notify('图片上传成功', 'success')
        this.form.url = res.data.url
        this.imgURL = res.data.url
        this.form.attachmentCode = this.form.url
      }
    },
    // 文件上传前的前的钩子函数
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isGIF = file.type === 'image/gif'
      const isPNG = file.type === 'image/png'
      const isBMP = file.type === 'image/bmp'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isGIF && !isPNG && !isBMP) {
        this.$message.error('上传图片必须是JPG/GIF/PNG/BMP 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return (isJPG || isBMP || isGIF || isPNG) && isLt2M
    }
  }
}
</script>

<style lang="scss">

  .add-contract-center {
    padding: 20px 20px 20px 70px;
  }

  .line {
    text-align: center;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
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

  .d-contract-edit-form {
    float: left;
    border: 1px solid #dddddd;
    margin: 0px;
    width: 60%;
    display: flex;
    flex-wrap: wrap;
    // @media screen and (max-width: 1024px) {
    //   .el-form-item{
    //     padding: 0px 20px;
    //     width: 10%;
    //   }
    // }
    // @media screen and (min-width: 1024px){
      .el-form-item{
        padding: 0px 20px;
        width: 48%;
        &:nth-of-type(2n+1) {
          margin-right: 2%;
        }
      // }
    }
  }
  .d-contract-edit-form-first {
    float: left;
    border: 1px solid #dddddd;
    margin: 0px 0px 20px 0px;
    width: 60%;
    display: flex;
    flex-wrap: wrap;
    .el-form-item{
      padding: 0px 20px;
      width: 48%;
      &:nth-of-type(2n+1) {
        margin-right: 2%;
      }
    }
  }
  .d-contract-edit-form-second {
    float: right;
    margin: 0px 2% 10px 20px;
    border: 1px solid #dddddd;
    width: 32%;
    display: flex;
    flex-wrap: wrap;
    .el-form-item{
      padding: 0px 20px;
      width: 98%;
      &:nth-of-type(2n+1) {
        margin-right: 2%;
      }
    }
  }
  .d-contract-edit-form-third {
    float: right;
    margin: 0px 2% 10px 20px;
    border: 1px solid #dddddd;
    width: 32%;
    display: flex;
    flex-wrap: wrap;
    .el-form-item{
      padding: 0px 20px;
      width: 98%;
      &:nth-of-type(2n+1) {
        margin-right: 2%;
      }
    }
  }
  .d-contract-edit-form-fourth {
    // float: left;
    border: 1px solid #dddddd;
    margin: 0px 0px 20px 0px;
    width: 60%;
    display: flex;
    flex-wrap: wrap;
    .el-form-item{
      padding: 0px 20px;
      width: 48%;
      &:nth-of-type(2n+1) {
        margin-right: 2%;
      }
    }
  }
  .d-contract-edit-form-div {
    display: flex;
    // flex-wrap: wrap;
    @media screen and (max-width: 1024px) {
      .el-form-item{
        width: 280px;
      }
    }
    @media screen and (min-width: 1024px){
      .el-form-item{
        width: 280px;
        &:nth-of-type(2n+1) {
          margin-right: 2%;
        }
      }
    }
  }
</style>

