<template>
  <div class="app-container">
    <el-card>
      <!-- <template>
        <div style="margin:10px;font-weight:bold;">
          <span style="font-size: 24px;">客户管理</span>
          <div>
            <a style="padding: 16px;font-weight:bold;" class="el-icon-download">导入</a>
            <a style="font-weight:bold;" class="el-icon-upload2">导出</a>
          </div>
        </div>
      </template> -->
      <div>
        <!-- <el-select v-model="search.paymentStatus" clearable placeholder="全部客户">
          <el-option label="销售一部" value="0" />
          <el-option label="销售二部" value="1" />
          <el-option label="销售三部" value="2" />
        </el-select> -->

        <el-input v-model="search.customerName" style="width: 200px;" clearable placeholder="请输入客户名称" />
        <el-button style="margin-left: 10px;" type="success" icon="el-icon-search" @click="fetchData">查询</el-button>
        <el-button style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleSave">新建客户</el-button>
        <!-- <el-button style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="$router.push('/customer/save')">新建客户</el-button> -->
      </div>
      <br>
      <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
        <el-table-column align="center" label="客户编号" width="220">
          <template slot-scope="scope">
            {{ scope.row.customerId }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="客户名称" width="150">
          <template slot-scope="scope">
            {{ scope.row.customerName }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="联系人" width="150">
          <template slot-scope="scope">
            {{ scope.row.contacts }}
          </template>
        </el-table-column>

        <el-table-column align="center" prop="contactsPhone" label="联系电话" width="200">
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.contactsPhone }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="staffName" label="负责员工" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.staffName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="department" label="部门" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.department }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button type="success" size="mini" icon="el-icon-search" @click="handleDetail(scope.row.customerId)">详情</el-button>
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row.customerId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDel(scope.row.customerId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- <save :son-data="form" @sonStatus="status" />
      <add :son-data="form" @sonStatus="status" /> -->

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
import { getList, findById, del } from '@/api/customer'
import Pagination from '@/components/Pagination'
// import Save from './save'
// import Add from './add'
import { parseTime } from '@/utils/index'

export default {
  // components: { Pagination, Save, Add },
  components: { Pagination },
  data() {
    return {
      list: null,
      search: {},
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
    console.info('this.fetchData()')
    this.fetchData()
  },
  methods: {
    _notify(message, type) {
      this.$message({
        message: message,
        type: type
      })
    },
    fetchData() {
      this.listLoading = true
      console.info('fetchData')
      getList(this.listQuery, this.search).then(response => {
        console.info(response)
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleSave() {
      this.$router.push({
        name: '新增客户',
        params: {
          title: '新增客户',
          // data: this.form
        }
      })
      // this.dialogVisible = true
    },
    handleEdit(id) {
      findById(id).then(response => {
        this.form = response.data
        this.$router.push({
          name: '编辑客户',
          params: {
            title: '编辑客户',
            data: this.form
          }
        })
      })
    },
    handleDetail(id) {
      findById(id).then(response => {
        this.form = response.data
        this.$router.push({
          name: '客户详情',
          params: {
            title: '客户详情',
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

    handleDel(id) {
      this.$confirm('你确定永久删除此账户？, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(id).then(response => {
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
