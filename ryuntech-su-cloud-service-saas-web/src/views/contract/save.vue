<template>
  <el-dialog :title="dialogTitle" :before-close="handleClose" :visible.sync="dialogVisible" width="40%">
    <el-form ref="form" :rules="rules" :model="form" status-icon label-position="right" label-width="150px">
      <el-form-item v-if="form.contractId != null" label="合同编号" prop="contractId" label-width="120px">
        <el-input v-model="form.contractId" :disabled="true" />
      </el-form-item>
      <div>
        <div style="display:inline;float: left;">
          <el-form-item label="合同名称" prop="contractName" label-width="120px">
            <el-input v-model="form.contractName" placeholder="请输入合同名称" />
          </el-form-item>
        </div>
        <div style="display:inline;float: left;">
          <el-form-item label="合同编码" prop="contractCode" label-width="120px">
            <el-input v-model="form.contractCode" placeholder="未设置系统自动生成" />
          </el-form-item>
        </div>
      </div>

      <div>
        <div style="display:inline;float: left;">
          <el-form-item label="合同金额" prop="contractAmount" label-width="120px">
            <el-input v-model="form.contractAmount" placeholder="请输入合同金额" />
          </el-form-item>
        </div>
        <div style="display:inline;float: left;">
          <el-form-item label="合同签订日期" prop="contractTime" label-width="120px">
            <el-date-picker
              v-model="form.contractTime"
              type="datetime"
              placeholder="选择签订日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </div>
      </div>
      <div>
        <div style="display:inline;float: left;">
          <el-form-item label="联系人" prop="contacts" label-width="120px">
            <el-input v-model="form.contacts" placeholder="请输入联系人" />
          </el-form-item>
        </div>
        <div style="display:inline;float: left;">
          <el-form-item label="联系电话" prop="contactsPhone" label-width="120px">
            <el-input v-model="form.contactsPhone" placeholder="请输入联系电话" />
          </el-form-item>
        </div>
      </div>

      <div>
        <div style="display:inline;float: left;">
          <el-form-item label="签约客户" prop="customerName" label-width="120px">
            <el-input v-model="form.customerName" placeholder="请选择签约客户" />
          </el-form-item>
        </div>
        <div style="display:inline;float: left;">
          <el-form-item label="负责员工" prop="staffName" label-width="120px">
            <el-input v-model="form.staffName" placeholder="请输入或选择员工" />
          </el-form-item>
        </div>
      </div>

      <div style="display:inline-block;">
        <el-form-item label="合同附件" prop="url" label-width="120px">
          <el-upload
            class="avatar-uploader"
            :action="localUpload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imgURL" :src="imgURL" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
            <div v-if="!imgURL" class="el-upload__text">点我上传合同附件</div>
          </el-upload>
        </el-form-item>
      </div>

      <!--<el-form-item label="合同附件" prop="url" label-width="120px">
        <el-input v-model="form.url" placeholder="请选择合同附件" />
      </el-form-item>-->

      <div style="display:inline-block;width: 100%">
        <div style="display:inline;float: left;width: 50%">
          <strong>回款计划</strong>
        </div>
        <div style="display:inline;float: right;width: 50%">
          <a style="display:inline;float: right;color: #20a0ff;" href="#">添加回款计划</a>
        </div>
        <!--<el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column
            prop="date"
            label="日期"
            width="180">
          </el-table-column>
          <el-table-column
            prop="name"
            label="金额"
            width="180">
          </el-table-column>
          <el-table-column
            prop="address"
            label="备注">
          </el-table-column>
        </el-table>-->
      </div>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        退出
      </el-button>
      <el-button type="primary" @click="onSubmit('form')">
        提交
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { save, edit, upload } from '@/api/contract'
import { parseTime } from '@/utils/index'

export default {
  // 父组件向子组件传值，通过props获取。
  // 一旦父组件改变了`sonData`对应的值，子组件的`sonData`会立即改变，通过watch函数可以实时监听到值的变化
  // `props`不属于data，但是`props`中的参数可以像data中的参数一样直接使用
  props: ['sonData'],

  data() {
    return {
      dialogVisible: false,
      dialogTitle: '合同详细信息',
      localUpload: upload,
      imgURL: '',
      form: {
        contractId: '',
        contractName: '',
        contractCode: '',
        contractAmount: '',
        contractTime: '',
        contacts: '',
        contactsPhone: '',
        staffId: '',
        url: ''
      },
      rules: {
        contractId: [{ required: true, trigger: 'blur', message: '请输入合同编号' }],
        contractName: [{ required: true, trigger: 'blur', message: '请输入合同名' }],
        contractCode: [{ required: true, trigger: 'blur', message: '请输入合同编码' }],
        contractAmount: [{ required: true, trigger: 'blur', message: '请输入合同金额' }],
        contractTime: [{ required: true, trigger: 'blur', message: '请输入签订日期' }],
        contacts: [{ required: true, trigger: 'blur', message: '请输入联系人' }],
        contactsPhone: [{ required: true, trigger: 'blur', message: '请输入联系电话' }],
        staffId: [{ required: true, trigger: 'blur', message: '请输入负责员工' }],
        url: [{ required: true, trigger: 'blur', message: '请上传合同附件' }]
      }
    }
  },
  watch: {
    'sonData': function(newVal, oldVal) {
      this.form = newVal
      this.imgURL = this.form.url
      this.dialogVisible = true
      if (newVal.id != null) {
        this.dialogTitle = '合同详细信息'
      } else {
        this.dialogTitle = '合同详细信息'
      }
    }
  },
  methods: {
    _notify(message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    clearForm() {
      this.form.contractId = null
      this.form.contractName = null
      this.form.contractCode = null
      this.form.contractAmount = null
      this.form.contractTime = null
      this.form.contacts = null
      this.form.contactsPhone = null
      this.form.staffId = null
      this.form.url = null
      this.form.createTime = parseTime(new Date(), '')
    },
    handleClose() {
      this.clearForm()
      this.dialogVisible = false
    },
    onSubmit(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          if (this.form.contractId === null) {
            save(this.form).then(response => {
              if (response.tcode === 200) {
                this._notify(response.msg, 'success')
                this.clearForm()
                this.$emit('sonStatus', true)
                this.dialogVisible = false
              } else {
                this._notify(response.msg, 'error')
              }
            })
          } else {
            edit(this.form).then(response => {
              if (response.tcode === 200) {
                this._notify(response.msg, 'success')
                this.clearForm()
                this.$emit('sonStatus', true)
                this.dialogVisible = false
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
    },
    // 文件上传成功的钩子函数
    handleAvatarSuccess(res, file, fileList) {
      if (res.tcode == 200) {
        this._notify('图片上传成功', 'success')
        this.form.url = res.data.url
        this.imgURL = res.data.url
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

<style lang="css">
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
</style>

