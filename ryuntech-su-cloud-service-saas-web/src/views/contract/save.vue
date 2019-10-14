<template>
  <el-dialog :title="dialogTitle" :before-close="handleClose" :visible.sync="dialogVisible" width="70%">
    <el-form ref="form" class="d-contract-edit-form" :rules="rules" :model="form" status-icon label-position="right" label-width="120px">

      <el-form-item v-if="form.contractId != null" label="合同编号" prop="contractId">
        <el-input v-model="form.contractId" :disabled="true" />
      </el-form-item>

      <el-form-item label="合同名称" prop="contractName">
        <el-input v-model="form.contractName" placeholder="请输入合同名称" />
      </el-form-item>

      <el-form-item label="合同编码" prop="contractCode">
        <el-input v-model="form.contractCode" placeholder="未设置系统自动生成" />
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

      <el-form-item label="签约客户" prop="customerName">
        <el-select v-model="form.customerId" filterable style="width: 100%" clearable size="small" placeholder="请选择或搜索客户">
          <el-option v-for="cus in customerMap" :label="cus.customer_name" :value="cus.customer_id" />
        </el-select>
      </el-form-item>

      <el-form-item label="负责员工" prop="staffName">
        <el-select v-model="form.staffId" filterable style="width: 100%" clearable size="small" placeholder="请选择或搜索员工">
          <el-option v-for="item in staffMap" :label="item.username" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-form-item style="width: 100%" label="合同附件">
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

      <el-form-item style="width: 100%" label="回款计划">
        <el-button type="text">添加回款计划</el-button>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer" style="margin-right: 30px">
      <el-button @click="handleClose">　退 出　</el-button>
      <el-button type="primary" @click="onSubmit('form')">　提 交　</el-button>
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
  props: ['sonData', 'staffMap', 'customerMap'],

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
        customerId: '',
        staffId: '',
        url: ''
      },
      rules: {
        contractId: [{ required: false, trigger: 'blur', message: '请输入合同编号' }],
        contractName: [{ required: true, trigger: 'blur', message: '请输入合同名' }],
        contractCode: [{ required: false, trigger: 'blur', message: '请输入合同编码' }],
        contractAmount: [{ required: true, trigger: 'blur', message: '请输入合同金额' }],
        contractTime: [{ required: true, trigger: 'blur', message: '请输入签订日期' }],
        contacts: [{ required: true, trigger: 'blur', message: '请输入联系人' }],
        contactsPhone: [{ required: true, trigger: 'blur', message: '请输入联系电话' }],
        customerId: [{ required: true, trigger: 'blur', message: '请选择客户' }],
        staffId: [{ required: true, trigger: 'blur', message: '请选择负责员工' }]
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
      this.form.customerId = null
      this.form.staffId = null
      this.form.url = null
      this.form.createTime = parseTime(new Date(), '')
    },
    handleClose() {
      this.clearForm()
      this.$refs.form.clearValidate()
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

<style lang="scss">
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

  .d-contract-edit-form {
    display: flex;
    flex-wrap: wrap;
    @media screen and (max-width: 1024px) {
      .el-form-item{
        width: 100%;
      }
    }
    @media screen and (min-width: 1024px){
      .el-form-item{
        width: 48%;
        &:nth-of-type(2n+1) {
          margin-right: 2%;
        }
      }
    }
  }
</style>

