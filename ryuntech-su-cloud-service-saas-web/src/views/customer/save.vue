<template>
  <el-dialog :title="dialogTitle" :before-close="handleClose" :visible.sync="dialogVisible" width="30%">
    <el-form ref="form" :rules="rules" :model="form" status-icon label-position="right" label-width="80px">
      <el-form-item v-if="form.contractId != null" label="客户编号" prop="contractId" label-width="120px">
        <el-input v-model="form.contractId" :disabled="true" />
      </el-form-item>
      <el-form-item label="客户名称" prop="contractName" label-width="120px">
        <el-input v-model="form.contractName" placeholder="请输入客户名称" />
      </el-form-item>

      <el-form-item label="联系人" prop="contacts" label-width="120px">
        <el-input v-model="form.contacts" placeholder="请输入联系人" />
      </el-form-item>

      <el-form-item label="联系电话" prop="contactsPhone" label-width="120px">
        <el-input v-model="form.contactsPhone" placeholder="请输入联系电话" />
      </el-form-item>

      <el-form-item label="省市区" prop="contactsPhone" label-width="120px">
        <el-input v-model="form.contactsPhone" placeholder="请输入省市区" />
      </el-form-item>

      <el-form-item label="详细地址" prop="contactsPhone" label-width="120px">
        <el-input v-model="form.contactsPhone" placeholder="请输入详细地址" />
      </el-form-item>

      <el-form-item label="跟进员工" prop="contactsPhone" label-width="120px">
        <el-select v-model="form.staffId" placeholder="请输入跟进员工">
          <i slot="prefix" class="el-input__icon el-icon-goods" />
          <el-option label="橙势科技" value="0" />
          <el-option label="睿云科技" value="1" />
        </el-select>
      </el-form-item>

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
import { save, edit, upload } from '@/api/user'
import { parseTime } from '@/utils/index'

export default {
  // 父组件向子组件传值，通过props获取。
  // 一旦父组件改变了`sonData`对应的值，子组件的`sonData`会立即改变，通过watch函数可以实时监听到值的变化
  // `props`不属于data，但是`props`中的参数可以像data中的参数一样直接使用
  props: ['sonData'],

  data() {
    return {
      dialogVisible: false,
      dialogTitle: 'Add',
      localUpload: upload,
      imgURL: '',
      form: {
        id: '',
        username: '',
        password: '',
        phone: '',
        avatar: '',
        createTime: ''
      },
      rules: {
        username: [{ required: true, trigger: 'blur', message: '请输入登录账户' }],
        password: [{ required: true, trigger: 'blur', message: '请输入登录密码' }],
        phone: [{ required: true, trigger: 'blur', message: '请输入联系电话' }],
        createTime: [{ required: true, trigger: 'blur', message: '请选择创建时间' }],
        avatar: [{ required: true, trigger: 'blur', message: '请上传个性头像' }]
      }
    }
  },
  watch: {
    'sonData': function(newVal, oldVal) {
      this.form = newVal
      this.imgURL = this.form.avatar
      this.dialogVisible = true
      if (newVal.id != null) {
        this.dialogTitle = '编辑客户'
      } else {
        this.dialogTitle = '新增客户'
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
      this.form.id = null
      this.form.username = null
      this.form.password = null
      this.form.phone = null
      this.form.avatar = null
      this.imgURL = null
      this.form.createTime = parseTime(new Date(), '')
    },
    handleClose() {
      this.clearForm()
      this.dialogVisible = false
    },
    onSubmit(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          if (this.form.id === null) {
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
      if (res.code == 200) {
        this._notify('图片上传成功', 'success')
        this.form.avatar = res.data.url
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

