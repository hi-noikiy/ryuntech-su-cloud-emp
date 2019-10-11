<template>
  <el-dialog :title="dialogTitle" :before-close="handleClose" :visible.sync="dialogVisible" width="30%">
    <el-form ref="form" :rules="rules" :model="form" status-icon label-position="right" label-width="80px">
      <el-form-item v-if="form.contractId != null" label="客户编号" prop="contractId" label-width="120px">
        <el-input v-model="form.contractId" :disabled="true" />
      </el-form-item>
      <el-form-item label="客户名称" prop="contractName" label-width="120px">
        <el-input v-model="form.customerName" placeholder="请输入客户名称" />
      </el-form-item>

      <el-form-item label="联系人" prop="contacts" label-width="120px">
        <el-input v-model="form.contacts" placeholder="请输入联系人" />
      </el-form-item>

      <el-form-item label="联系电话" prop="contactsPhone" label-width="120px">
        <el-input v-model="form.contactsPhone" placeholder="请输入联系电话" />
      </el-form-item>

      <el-form-item label="省市区" prop="city" label-width="120px">
        <el-input v-model="form.city" placeholder="请输入省市区" />
      </el-form-item>

      <el-form-item label="详细地址" prop="address" label-width="120px">
        <el-input v-model="form.address" placeholder="请输入详细地址" />
      </el-form-item>

      <el-form-item label="跟进员工" prop="staffId" label-width="120px">
        <el-select v-model="form.staffId" placeholder="请选择跟进员工">
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
import { save, edit } from '@/api/customer'

export default {
  // 父组件向子组件传值，通过props获取。
  // 一旦父组件改变了`sonData`对应的值，子组件的`sonData`会立即改变，通过watch函数可以实时监听到值的变化
  // `props`不属于data，但是`props`中的参数可以像data中的参数一样直接使用
  props: ['sonData'],

  data() {
    return {
      dialogVisible: false,
      dialogTitle: 'Add',
      form: {
        contractId: '',
        customerName: '',
        contacts: '',
        contactsPhone: '',
        city: '',
        address: '',
        staffId: ''
      },
      rules: {
        customerName: [{ required: true, trigger: 'blur', message: '请输入客户名称' }],
        contacts: [{ required: true, trigger: 'blur', message: '请输入联系人' }],
        contactsPhone: [{ required: true, trigger: 'blur', message: '请输入联系电话' }],
        city: [{ required: true, trigger: 'blur', message: '请输入城市' }],
        address: [{ required: true, trigger: 'blur', message: '请输入详细地址' }],
        staffId: [{ required: true, trigger: 'blur', message: '请选择跟进员工' }]
      }
    }
  },
  watch: {
    'sonData': function(newVal, oldVal) {
      this.form = newVal
      this.dialogVisible = true
      if (newVal.contractId != null) {
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
      this.form.contractId = null
      this.form.contacts = null
      this.form.contractName = null
      this.form.contactsPhone = null
      this.form.city = null
      this.form.address = null
      this.form.staffId = null
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
    }

  }
}
</script>

<style lang="css">
  .line {
    text-align: center;
  }

</style>

