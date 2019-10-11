<template>
  <div class="dashboard-container">
    <el-card>
      <div>
        <div>
          <p>
            <strong style="font-size: x-large;float: left;">数据简报</strong>
          </p>
        </div>

        <div>
          <el-date-picker
            v-model="value"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']"
          />
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>

        </div>
      </div>
      <el-row>

        <div style="display: block;">
          <el-col :span="8">
            <div class="grid-content bg-purple" style="border:solid 1px #000;margin: 20px;">
              <p>应收未回款</p>
              <p><strong style="font-size: x-large;">728,558.00</strong></p>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light" style="border:solid 1px #000;margin: 20px">
              <p>本月回款</p>
              <p><strong style="font-size: x-large;">6,000.00</strong></p>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple" style="border:solid 1px #000;margin: 20px">
              <p>本月销售额</p>
              <p><strong style="font-size: x-large;">78,888.00</strong></p>
            </div>
          </el-col>
        </div>

      </el-row>
    </el-card>

    <el-row>
      <el-col :span="12">
        <el-card style="margin: 10px;">
          <div>
            <div>
              <p>
                <strong style="font-size: x-large;float: left;">新增销售</strong>
              </p>
            </div>

            <div>
              <el-date-picker
                v-model="value"
                type="daterange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
              />
              <el-select v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>

          <div>
            <div id="myChart" style="width: 100%;height: 400px;margin: 10px;" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="margin: 10px;">
          <div>
            <div>
              <p>
                <strong style="font-size: x-large;float: left;">回款对比</strong>
              </p>
            </div>

            <div>
              <el-date-picker
                v-model="value"
                type="daterange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
              />
              <el-select v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </div>

          <div>
            <div id="report" style="width: 100%;height: 400px;margin: 10px;" />
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
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
      options: [{
        value: '选项1',
        label: '深圳睿云产业科技有限公司'
      }, {
        value: '选项2',
        label: '深圳橙势科技有限公司'
      }],
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
  mounted() {
    this.$nextTick(function() {
      this.draw('report')
      this.drawLine()
      // this.draw("report2");
    })
    /* let that = this;
        that.initData();
        window.addEventListener("resize", () => {
            this.resize(); //监听屏幕大小，来刷新画布
        });*/
  },
  methods: {
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
    },
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      const myChart = echarts.init(document.getElementById('myChart'))
      // 绘制图表
      myChart.setOption({
        title: {
          text: '',
          subtext: ''
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['金额/万元']
        },
        toolbox: {
          show: true,
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            dataView: { readOnly: false },
            magicType: { type: ['line', 'bar'] },
            restore: {},
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['2019-02-25', '2019-03-04', '2019-03-18', '2019-03-26', '2019-04-16', '2019-04-26', '2019-05-04']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}'
          }
        },
        series: [
          {
            name: '金额/万元',
            type: 'line',
            data: [11, 11, 15, 13, 12, 13, 10],
            markPoint: {
              data: [
                { type: 'max', name: '最大值' },
                { type: 'min', name: '最小值' }
              ]
            }
            /* markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        }*/
          }
        ]
      })
    }
  }
}
</script>

<style lang="scss">
  .dashboard-container {
    text-align: center;
    padding: 20px;
  }

  .el-collapse-item__header {
    display: block !important;
  }
</style>
