<template>
    <div class="app-container">
        <div class="form-group">
            <h3>应收合同</h3>
            <div @click="toggerSelectContract" class="contract-select-btn">
                <span v-if="contractName">{{ contractName }}</span>
                <span v-else class="ph">
                    请选择回款的应收合同
                </span>
                <span class="right">●●●</span>
            </div>
            <template>
                <div style="margin: 10px 0px -10px 40px;" v-if="contractName">
                    <span>应收未收金额:</span>
                    <span style="color:red;font-size:13px;margin:10px;">{{ feige(uncollectedAmount) }}元</span>
                    <span style="margin-left:220px;">合同金额:</span>
                    <span style="font-size:13px;">{{ feige(totalContractAmount) }}元</span>
                </div>
            </template>
        </div>

        <el-form ref="form" class="form-group add-collect-form" :rules="rules" :model="form" label-position="right" label-width="120px">
            <h3>回款信息</h3>

            <el-form-item label="回款金额" prop="amount">
                <el-input v-model="form.amount" />
            </el-form-item>

            <el-form-item label="收款方式" prop="type">
                <el-select v-model="form.type" placeholder="请选择收款方式" style="width: 100%">
                    <el-option v-for="(value,key) in collectionTypeOptions" :key="key" :label="value" :value="value" />
                </el-select>
            </el-form-item>

            <el-form-item label="收款日期" prop="time">
                <el-date-picker
                        v-model="form.time"
                        style="width: 100%"
                        type="datetime"
                        placeholder="选择收款日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        format="yyyy-MM-dd HH:mm:ss"
                />
            </el-form-item>

            <el-form-item label="备注" prop="remarks">
                <el-input v-model="form.remarks" type="textarea"></el-input>
            </el-form-item>

            <el-form-item style="width: 100%" label="附件" prop="url">
                <el-upload
                        class="avatar-uploader"
                        :action="localUpload"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload"
                >
                    <img v-if="form.url" :src="form.url" class="avatar">
                    <template v-else>
                        <i class="el-icon-plus avatar-uploader-icon" />
                        <div class="el-upload__text">点我上传合同附件</div>
                    </template>
                </el-upload>
            </el-form-item>

        </el-form>

        <div class="operate-btn-group">
            <el-button type="primary" @click="submitAddCollection('form')">　提　交　</el-button>
            <el-button @click="$router.push('/collection/list')">　取　消　</el-button>
        </div>


        <el-dialog title="选择应收合同" class="contract-select-dialog" :visible.sync="showSelectContract" width="90%">
            <div style="margin-bottom: 10px">
                <el-select v-model="search.status" clearable size="mini" placeholder="全部状态">
                    <el-option v-for="(value,key) in contractStatusOptions" :label="value" :value="key" />
                </el-select>
                <el-input style="width: 200px" size="mini" v-model="search.keyword" placeholder="合同名称"></el-input>
                <el-button type="primary" size="mini" @click="getContractList">搜索</el-button>
            </div>

            <el-table v-loading="listLoading" @current-change="handleCurrentChange" :data="contractList" border fit highlight-current-row>
                <el-table-column label=" " width="40">
                    <template slot-scope="scope" align="center">
                        <span v-if="checkedContract && checkedContract.contractId == scope.row.contractId">√</span>
                    </template>
                </el-table-column>

                <el-table-column align="center" label="合同编号" width="180">
                    <template slot-scope="scope">
                        {{ scope.row.contractId }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="合同名称" min-width="150">
                    <template slot-scope="scope">
                        {{ scope.row.contractName }}
                    </template>
                </el-table-column>

                <!-- <el-table-column align="center" label="客户编号" min-width="100" v-if="false">
                    <template slot-scope="scope">
                        {{ scope.row.customerId }}
                    </template>
                </el-table-column> -->

                <el-table-column align="center" label="客户名称" min-width="100">
                    <template slot-scope="scope">
                        {{ scope.row.customerName }}
                    </template>
                </el-table-column>

                <el-table-column align="center" prop="contractTime" label="签订日期" width="160">
                    <template slot-scope="scope">
                        <i class="el-icon-time" />
                        <span>{{ scope.row.contractTime }}</span>
                    </template>
                </el-table-column>

                <el-table-column align="center" prop="contractAmount" label="合同金额" width="120">
                    <template slot-scope="scope">
                        <span>{{ scope.row.contractAmount }}</span>
                    </template>
                </el-table-column>

                <el-table-column align="center" prop="collectionAmount" label="合同状态" width="90">
                    <template slot-scope="scope">
                        <span>{{ scope.row.status && contractStatusOptions[scope.row.status] }}</span>
                    </template>
                </el-table-column>

                <el-table-column align="center" prop="collectionAmount" label="负责员工" width="90">
                    <template slot-scope="scope">
                        <span>{{ scope.row.staffName }}</span>
                    </template>
                </el-table-column>

                <el-table-column v-if="false" align="center" label="部门" width="150">
                    <template slot-scope="scope">
                        <span>{{ scope.row.department }}</span>
                    </template>
                </el-table-column>
            </el-table>

            <pagination
                    v-show="contractTotal>0"
                    :total="contractTotal"
                    :page.sync="listQuery.page"
                    :limit.sync="listQuery.limit"
                    @pagination="getContractList"
            />

            <div slot="footer" class="dialog-footer" style="margin-right: 30px">
                <el-button type="primary" @click="checkContract">　确 定　</el-button>
                <el-button @click="showSelectContract = false">　取 消　</el-button>
            </div>

        </el-dialog>
    </div>
</template>

<script>
  import { del, save } from '@/api/collection'
  import { getList as getContractList, upload as uploadUrl } from '@/api/contract'
  import { collectionTypeOptions } from './collection'
  import { contractStatusOptions } from '../contract/contract'
  import { parseTime } from '@/utils/index'

  export default {
    data() {
      return {
        contractName: '',
        showSelectContract: false,
        collectionTypeOptions: collectionTypeOptions,
        contractStatusOptions: contractStatusOptions,
        localUpload: uploadUrl,
        uncollectedAmount: '',
        totalContractAmount: '',
        form: {
          collectionId: '',
          customerId: '',
          customerName: '',
          contractId: '',
          amount: '',
          type: '',
          time: '',
          createTime: parseTime(new Date(), ''),
          remarks: '',
          url: ''
        },
        rules: {
            amount: [{ required: true, trigger: 'blur', message: '请输入回款金额' },
                       { pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请正确输入金额', trigger: 'blur' }],
            type: [{ required: true, trigger: 'blur', message: '请输入收款方式' }],
            time: [{ required: true, trigger: 'blur', message: '请输入回款日期' }],
            remarks: [{ required: true, trigger: 'blur', message: '请输入备注信息' }],
            url: [{ required: true, trigger: 'blur', message: '请上传合同附件' }]
        },
        listQuery: {
          page: 1,
          limit: 20
        },
        search: {
          status: undefined,
          keyword: undefined
        },
        listLoading: false,
        contractList: [],
        contractTotal: 0,
        checkedContract: undefined,
      }
    },
    methods: {
      handleCurrentChange(val) {
        this.checkedContract = val
      },
      getContractList() {
        this.listLoading = true
        getContractList(this.listQuery, this.search).then(response => {
          this.contractList = response.data.records
          this.contractTotal = response.data.total
          this.listLoading = false
        })
      },
      toggerSelectContract(){
        this.showSelectContract = !this.showSelectContract
        if (this.showSelectContract) {
          this.getContractList()
        }
      },
      checkContract() {
        if (!this.checkedContract) this._notify("请先选择合同！",'e')
        this.form.contractId = this.checkedContract.contractId
        this.form.customerId = this.checkedContract.customerId
        this.form.customerName = this.checkedContract.customerName
        this.contractName = this.checkedContract.contractId + ' : ' + this.checkedContract.contractName
        this.uncollectedAmount = this.checkedContract.balanceAmount
        this.totalContractAmount = this.checkedContract.contractAmount
        this.showSelectContract = false
      },
      // 文件上传成功的钩子函数
      handleAvatarSuccess(res, file, fileList) {
        if (res.tcode == 200) {
          this._notify('图片上传成功', 'success')
          this.form.url = res.data.url
        }
      },
      // 文件上传前的前的钩子函数
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg'
        const isGIF = file.type === 'image/gif'
        const isPNG = file.type === 'image/png'
        const isBMP = file.type === 'image/bmp'
        const isLt2M = file.size / 1024 / 1024 < 2
        if (!isJPG && !isGIF && !isPNG && !isBMP) { this.$message.error('上传图片必须是JPG/GIF/PNG/BMP 格式!') }
        if (!isLt2M) { this.$message.error('上传图片大小不能超过 2MB!') }
        return (isJPG || isBMP || isGIF || isPNG) && isLt2M
      },
      submitAddCollection(form) {
        if (!this.form.contractId) { return this.$message.error('请选择应收合同！') }
        if(parseFloat(this.form.amount) > parseFloat(this.uncollectedAmount)) {
            return this.$message.error('回款金额应不大于应收未收金额！')
        }
        // if (!this.form.amount || !/\d[\d\.]+/.test(this.form.amount)) { return this.$message.error('请输入回款金额！') }
        // if (!this.form.type) { return this.$message.error('请选择收款方式！') }
        // if (!this.form.time) { return this.$message.error('请选择收款时间！') }
        this.$refs[form].validate((valid) => {
            if (valid) {
                save(this.form).then( res => {
                  console.log(res)
                  this.$message.success('添加成功')
                  this.$router.push('/collection/list')
                }).catch( er => console.log(er) )
            } else {
        this.$message('请完善回款信息 !')
        return false
        }
        })
      },
      //分割金额，加','
      feige(value) {
          if(!value) return '0.00'
        value = parseFloat(value).toFixed(2)
        var intPart = Math.trunc(value)// 获取整数部分
        var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,') // 将整数部分逢三一断
        var floatPart = '.00' // 预定义小数部分
        var value2Array = value.split('.')
        // =2表示数据有小数位
        if(value2Array.length === 2) {
            floatPart = value2Array[1].toString() // 拿到小数部分
            if(floatPart.length === 1) { // 补0,实际上用不着
            return intPartFormat + '.' + floatPart + '0'
            } else {
            return intPartFormat + '.' + floatPart
            }
        } else {
            return intPartFormat + floatPart
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
    .form-group{
        box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
        padding: 20px;
        h3{
            font-size: 15px;
            margin: 0 0 8px 0;
            font-weight: 400;
        }
    }
    .contract-select-btn{
        width: 600px;
        border: 1px solid #eee;
        border-radius: 4px;
        padding: 5px 10px;
        display: flex;
        justify-content: space-between;
        cursor: pointer;
        font-size: 12px;
        span.ph{
            color: #333;
        }
    }
    .add-collect-form{
        margin-top: 25px;
    }
    .operate-btn-group{
        display: flex;
        justify-content: center;
        margin-top: 20px;
        .el-button + .el-button {
            margin-left: 30px;
        }
    }
</style>

<style lang="scss">
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
</style>