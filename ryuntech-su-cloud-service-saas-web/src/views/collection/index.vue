<template>
  <div class="app-container">
    <el-card>
      <!-- <template>
        <div style="margin:10px;font-weight:bold;">
          <span style="font-size: 24px;">回款管理</span>
          <div>
            <a style="padding: 16px;font-weight:bold;" class="el-icon-download">导入</a>
            <a style="font-weight:bold;" class="el-icon-upload2">导出</a>
          </div>
        </div>
      </template> -->
      <div>
        <el-select v-model="search.type" clearable placeholder="全部方式">
          <el-option v-for="(value,key) in collectionTypeOptions" :key="key" :label="value" :value="key" />
        </el-select>
        <el-date-picker
          v-model="backCollectionTime"
          type="daterange"
          start-placeholder="请选择开始日期"
          end-placeholder="请选择结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          format="yyyy-MM-dd"
          :default-time="['00:00:00', '23:59:59']"
        />
        <el-input v-model="search.customerName"  clearable style="width: 200px;" placeholder="请输入客户名称" />
        <el-button size="mini" style="margin-left: 10px;" type="success" icon="el-icon-search" @click="fetchData">查询</el-button>
        <el-button size="mini" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="$router.push('/collection/add')">添加回款</el-button>
      </div>
      <br>
      <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
        <el-table-column align="center" label="回款编号" width="180">
          <template slot-scope="scope">
            {{ scope.row.collectionId }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="客户名称" width="150">
          <template slot-scope="scope">
            {{ scope.row.customerName }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="回款金额" width="120">
          <template slot-scope="scope">
            {{ scope.row.amount }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="收款方式" width="140">
          <template slot-scope="scope">
            {{ scope.row.type }}
          </template>
        </el-table-column>

        <el-table-column align="center" prop="contractTime" label="回款时间" width="160">
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

        <el-table-column align="center" prop="balanceAmount" label="转账凭证" width="160">
          <template slot-scope="scope">
            <span>{{ scope.row.balanceAmount }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="collectionAmount" label="创建人" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.createBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="状态" width="150">
          <template slot-scope="scope">
            <span v-if='scope.row.status==0'>已作废</span>
            <span v-if='scope.row.status==1'>已收款</span>
            <span v-if='scope.row.status==2'>回款中</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <div v-if="scope.row.status === '0'">
              <el-button type="primary" size="mini" icon="el-icon-edit" disabled @click="handleZuoFei(scope.row.collectionId)">已作废</el-button>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row.collectionId)">编辑</el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDel(scope.row.collectionId)">删除</el-button>
            </div>
            <div v-else>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleZuoFei(scope.row.collectionId)">作废</el-button>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row.collectionId)">编辑</el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" disabled @click="handleDel(scope.row.collectionId)">删除</el-button>
            </div>
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
import { getList, findById, del, zuofei } from '@/api/collection'
import { parseTime } from '@/utils/index'
import { collectionTypeOptions } from './collection'

export default {
  data() {
    return {
      collectionTypeOptions: collectionTypeOptions,
      list: null,
      backCollectionTime: null,
      search: {
        type: '',
        customerName: '',
        startTime: '',
        endTime: ''

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
      form: null,
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)

            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      if(this.backCollectionTime !== null ) {
        let backTime = (this.backCollectionTime + '').split(',')
        this.search.startTime = backTime[0]
        this.search.endTime = backTime[1]
      } else {
        this.search.startTime = ''
        this.search.endTime = ''
      }
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
      this.$confirm('你确定永久删除此回款信息？, 是否继续?', '提示', {
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
    },

    handleZuoFei(id) {
      this.$confirm('你确定此回款信息作废？, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        zuofei(id).then(response => {
          if (response.tcode === 200) {
            this._notify(response.msg, 'success')
          } else {
            this._notify(response.msg, 'error')
          }
          this.fetchData()
        })
      }).catch(() => {
        this._notify('作废已取消', 'info')
      })
    }
  }
}
</script>
