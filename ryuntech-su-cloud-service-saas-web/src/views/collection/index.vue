<template>
  <div class="app-container">
    <el-card>
      <div>
        <el-select v-model="search.paymentStatus" placeholder="全部账户">
          <el-option v-for="(value,key) in collectionTypeOptions" :key="key" :label="value" :value="key" />
        </el-select>
        <el-input v-model="search.customerName" style="width: 200px;" placeholder="请输入客户名称" />
        <el-button size="mini" style="margin-left: 10px;" type="success" icon="el-icon-search" @click="fetchData">查询</el-button>
        <el-button size="mini" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="$router.push('/collection/add')">添加回款</el-button>
      </div>
      <br>
      <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
        <el-table-column align="center" label="回款编号" width="220">
          <template slot-scope="scope">
            {{ scope.row.collectionId }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="客户名称" width="150">
          <template slot-scope="scope">
            {{ scope.row.customerName }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="回款金额" width="95">
          <template slot-scope="scope">
            {{ scope.row.amount }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="收款方式" width="150">
          <template slot-scope="scope">
            {{ scope.row.customerName }}
          </template>
        </el-table-column>

        <el-table-column align="center" prop="contractTime" label="回款时间" width="200">
          <template slot-scope="scope">
            <i class="el-icon-time" />
            <span>{{ scope.row.time }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="contractAmount" label="备注" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.remarks }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="balanceAmount" label="转账凭证" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.balanceAmount }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="collectionAmount" label="创建人" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.createBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="collectionAmount" label="状态" width="150">
          <template slot-scope="scope">
            <span v-if='scope.row.status===0'>已作废</span>
            <span v-if='scope.row.status===1'>已收款</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row.collectionId)">编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDel(scope.row.collectionId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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
import { getList, findById, del } from '@/api/collection'
import { parseTime } from '@/utils/index'
import { collectionTypeOptions } from './collection'

export default {
  data() {
    return {
      collectionTypeOptions: collectionTypeOptions,
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
    this.fetchData()
  },
  methods: {
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
      this.form = { id: null, createTime: parseTime(new Date()) }
      this.dialogVisible = true
    },
    handleEdit(id) {
      findById(id).then(response => {
        this.form = response.data
      })
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
