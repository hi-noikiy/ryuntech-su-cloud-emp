<template>
  <div class="app-container">
    <el-card>
      <!-- <template>
        <div style="margin:10px;font-weight:bold;">
          <span style="font-size: 24px;">应收合同</span>
          <div>
            <a style="padding: 16px;font-weight:bold;" class="el-icon-download">导入</a>
            <a style="font-weight:bold;" class="el-icon-upload2">导出</a>
          </div>
        </div>
      </template> -->
      <div style="margin-bottom: 10px">
        <el-select v-model="search.belong_type" clearable size="small">
          <el-option label="全部合同" :value="0" />
          <el-option label="我负责的" :value="1" />
        </el-select>

        <el-select v-model="search.status" clearable size="small" placeholder="全部状态">
          <el-option v-for="(value,key) in contractStatusOptions" :label="value" :value="key" />
        </el-select>

        <el-input v-model="search.contractId" clearable size="small" style="width: 200px;" placeholder="请输入合同号查询" />
        <el-button size="mini" style="margin-left: 10px;" type="success" icon="el-icon-search" @click="fetchData">查询</el-button>
        <!-- <el-button size="mini" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleSave">新增合同</el-button> -->
        <el-button size="mini" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="$router.push('/contract/add')">新增合同</el-button>
      </div>

      <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
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

        <el-table-column align="center" prop="collectionAmount" label="合同状态" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.status && contractStatusOptions[scope.row.status] }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="collectionAmount" label="负责员工" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.staffName }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="false" align="center" label="部门" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.department }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="info" size="mini" icon="el-icon-search" @click="handleDetail(scope.row.contractId)">查看</el-button>
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row.contractId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDel(scope.row.contractId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- <save :son-data="form" :staff-map="staffMap" :customer-map="customerMap" @sonStatus="status" /> -->

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.limit"
        @pagination="fetchData"
      />
    </el-card>
  </div>
</template>

<script>
import { getList, findById, del } from '@/api/contract'
import { getUserMap } from '@/api/user'
import { getCustomerMap } from '@/api/customer'
import Save from './save'
import { parseTime } from '@/utils/index'
import { contractStatusOptions } from './contract'

export default {
  // components: { Save },
  data() {
    return {
      contractStatusOptions: contractStatusOptions,
      staffMap: [],
      customerMap: [],
      list: null,
      search: {
        belong_type: 0,
        status: undefined
      },
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      total: 0,
      dialogVisible: false,
      form: null
    }
  },
  created() {
    this.getUserMap()
    this.getCustomerMap()
    this.fetchData()
  },
  methods: {
    getUserMap() {
      getUserMap('').then(res => {
        this.staffMap = res.data
      }).catch(er => { console.log(er) })
    },
    getCustomerMap() {
      getCustomerMap('').then(res => {
        this.customerMap = res.data
      }).catch(er => { console.log(er) })
    },
    fetchData() {
      this.listLoading = true
      getList(this.listQuery, this.search).then(response => {
        console.info(response)
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleSave() {
      this.form = { contractId: null, createTime: parseTime(new Date()) }
      this.dialogVisible = true
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
    handleEdit(id) {
      findById(id).then(response => {
        this.form = response.data
        this.$router.push({
          name: '编辑合同',
          params: {
            title: '编辑客户',
            data: this.form
          }
        })
      })
    },

    // 子组件的状态Flag，子组件通过`this.$emit('sonStatus', val)`给父组件传值
    // 父组件通过`@sonStatus`的方法`status`监听到子组件传递的值
    status(data) {
      if (data) {
        this.fetchData()
      }
    },

    handleDel(contractId) {
      this.$confirm('你确定永久删除此账户？, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(contractId).then(response => {
          if (response.tcode === 200) {
            this._notify(response.msg, 'success')
          } else {
            this._notify(response.msg, 'error')
          }
          this.fetchData()
        })
      }).catch(() => {
        this._notify('已取消删除', 'info')
      })
    }
  }
}
</script>
