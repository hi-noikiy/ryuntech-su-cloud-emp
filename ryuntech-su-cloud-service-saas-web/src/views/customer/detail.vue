<template>
  <div class="add-customer-center">
    <div>
      <h1>{{ customerName }}</h1>
      <span v-if="tabName === 'first'">
        <h4>负责员工：{{ correspondingStaff }}</h4>
      </span>
      <span v-else>
        <h4>跟进员工：{{ correspondingStaff }}</h4>
      </span>
    </div>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane :disabled="false" label="应收合同" name="first"></el-tab-pane>
      <el-tab-pane :disabled="false" label="风险预警" name="second"></el-tab-pane>
      <el-tab-pane :disabled="false" label="客户资料" name="third"></el-tab-pane>
    </el-tabs>

    <div v-if="tabName === 'first'">
      <div class="aaa">
        <h2>￥{{ feige(receivableBalance) }}</h2>
        <h4>应收余额</h4>
      </div>
      <div class="aaa">
        <h2>￥{{ feige(refundAmount) }}</h2>
        <h4>回款金额</h4>
      </div>
      <div class="aaa">
        <h2>￥{{ feige(totalSales) }}</h2>
        <h4>总销售额</h4>
      </div>
      <div class="aaa">
        <h2>{{ ercentage }}%</h2>
        <h4>回款率</h4>
      </div>
      
    </div>

    <el-table v-if="tabName === 'first'" v-loading="listLoading" :data="customerContractList" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column align="center" prop="contractId" label="合同编号" width="180">
        <template slot-scope="scope">
          {{ scope.row.contractId }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="contractName" label="合同名称" min-width="150">
        <template slot-scope="scope">
          {{ scope.row.contractName }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="contractTime" label="签订日期" width="160">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.contractTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="contractAmount" label="合同金额" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.contractAmount }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="balanceAmount" label="应收余额" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.balanceAmount }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="collectionAmount" label="回款金额" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.collectionAmount }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="status" label="合同状态" width="120">
        <template slot-scope="scope">
          <span v-if='scope.row.status==0'>已逾期</span>
          <span v-if='scope.row.status==1'>已完成</span>
          <span v-if='scope.row.status==2'>执行中</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="staffName" label="负责员工" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.staffName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="info" size="mini" icon="el-icon-edit" @click="handleDetail(scope.row.contractId)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="tabName === 'third'">
      <div>
        <h3>联系人</h3>
        <span>姓名：{{customerList.contacts}}</span>
        <br/>
        <span>手机号码：{{customerList.contactsPhone}}</span>
      </div>
      <div>
        <h3>地址</h3>
        <span>所属地区：{{customerList.cityId}}</span>
        <br/>
        <span>详细地址：{{customerList.address}}</span>
      </div>
    </div>

  </div>
</template>

<script>
import { findById, getContractListByCustomerId as getCustomerContractList } from '@/api/contract'

export default {
  data() {
    return {
      activeName: 'first',
      tabName: 'first',
      customerName: '',
      correspondingStaff: '',

      receivableBalance: 0,
      refundAmount: 0,
      totalSales: 0,
      ercentage: 0,

      showTitle: '',
      // customerList: {
      //   customerId: '',
      //   customerName: '',
      //   contacts: '',
      //   contactsPhone: '',
      //   cityId: '',
      //   address: '',
      //   staffId: '',
      //   provinceId: '',
      //   districtId: '',
      //   companyId: '',
      //   companyName: '',
      //   department: '',
      //   staffName: ''
      // },
      customerList: null,
      customerContractList: null,
      listLoading: false,
    }
  },
  created(){
    this.showTitle = this.$route.params.title
    if(typeof(this.$route.params.data) !== "undefined") {
      this.customerList = this.$route.params.data
      this.customerName = this.customerList.customerName
      this.correspondingStaff = this.customerList.staffName
      this.customerContractShow()
    }
  },
  methods: {

    customerContractShow() {
      getCustomerContractList(this.customerList.customerId).then(response => {
        this.customerContractList = response.data
        var a = this.customerContractList.map(row => row.contractAmount).reduce((acc, cur) => (parseFloat(cur) + parseFloat(acc)), 0)
        this.totalSales = this.customerContractList.map(row => row.contractAmount).reduce((acc, cur) => (parseFloat(cur) + parseFloat(acc)), 0)
        this.receivableBalance = this.customerContractList.map(row => row.balanceAmount).reduce((acc, cur) => (parseFloat(cur) + parseFloat(acc)), 0)
        this.refundAmount = this.customerContractList.map(row => row.collectionAmount).reduce((acc, cur) => (parseFloat(cur) + parseFloat(acc)), 0)
        this.ercentage = parseInt(this.refundAmount / this.totalSales * 100)
      })
    },

    handleDetail(id) {
      findById(id).then(response => {
        this.form = response.data
        this.$router.push({
          name: '合同详情',
          params: {
            title: '合同详情',
            data: this.form
          }
        })
      })
    },

    handleClick(tab, event) {
      this.tabName = tab.name
      console.log(this.activeName + '-------------------' + this.tabName)
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

<style lang="css">
  .add-customer-center {
    padding: 20px 20px 20px 70px;
  }
  .aaa {
    text-align: center;
    float: left;
    padding: 10px 30px;
  }
</style>

