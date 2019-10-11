<template>
  <div class="app-container">
    <el-card>
      <div>
        <span>数据维度&#12288; </span>
        <el-select v-model="search.paymentStatus" placeholder="全部">
          <!-- 合同状态(0已逾期,1已完成，2执行中)-->
          <el-option label="客户" value="0" />
          <el-option label="员工" value="1" />
        </el-select>

        <el-select v-model="search.paymentStatus" placeholder="全部">
          <!-- 合同状态(0已逾期,1已完成，2执行中)-->
          <el-option label="深圳睿云产业科技有限公司" value="0" />
          <el-option label="深圳橙势科技有限公司" value="1" />
        </el-select>
        <el-date-picker
          v-model="value"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        />

        <el-button style="margin-left: 10px;" type="success" icon="el-icon-search" @click="fetchData">查询</el-button>
        <el-button style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleSave">新建回款</el-button>
      </div>
      <br>
      <div>
        <div id="report" style="width: 100%;height: 400px;margin: 10px;" />
      </div>

    </el-card>
  </div>
</template>

<script>
import { getList, findById, del } from '@/api/collection'
import Pagination from '@/components/Pagination'
import echarts from 'echarts'
import Save from './save'
import { parseTime } from '@/utils/index'

export default {
  components: { Pagination, Save },
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
      form: null,
      activeNames: ['1', '2', '3', '4', '5'],
      charts: '',
      chart1: '',
      score: '100',
      source: [
        ['题型', '总分', '成绩'],
        ['单选题', 45, 12],
        ['多选题', 30, 15],
        ['判断题', 45, 8],
        ['填空题', 10, 2]
      ],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }]
      },
      value1: '',
      value: '',
      value2: ''
    }
  },
  created() {
    console.info('this.fetchData()')
    this.fetchData()
  },
  mounted() {
    this.$nextTick(function() {
      this.draw('report')
    })
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
      this.form = { id: null, createTime: parseTime(new Date()) }
      this.dialogVisible = true
    },
    handleEdit(id) {
      findById(id).then(response => {
        this.form = response.data
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
    },
    draw(id) {
      this.charts = echarts.init(document.getElementById(id))
      this.charts.setOption({
        legend: {},
        tooltip: {},
        dataset: {
          source: this.source // 连接数据
        },
        xAxis: { type: 'category' },
        yAxis: {
          // 这个地方如果需要在Y轴定义最大值就放开,如果需要根据数据自适应的话,就注释掉
          // type: "value",
          // max: this.score,
          // maxInterval: this.score * 0.2,
          // minInterval: 1,
          // splitNumber: 4
        },
        grid: { bottom: 30 },
        series: [
          {
            type: 'bar', // 表示这个是柱状图
            barCategoryGap: '40%',
            itemStyle: { color: '#999' }, // 定义颜色
            tooltip: {
              formatter: params => {
                // console.log(params)   打印这个params,按自己需要拼接字符串
                return ` ${params.value[0]} <br/>
                         ${params.seriesName}:${params.value[1]}`
              }
            }
          },
          {
            type: 'bar',
            barCategoryGap: '40%',
            itemStyle: { color: '#81cebe' },
            tooltip: {
              formatter: params => {
                return ` ${params.value[0]} <br/>
                         ${params.seriesName}:${params.value[2]}`
              }
            }
          }
        ]
      })
    }
  }
}
</script>
