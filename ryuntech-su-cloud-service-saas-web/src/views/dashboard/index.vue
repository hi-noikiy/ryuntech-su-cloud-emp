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
          <!-- <el-date-picker
            v-model="value2"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
          /> -->
          <!-- <el-select v-model="indexDTO.companyName" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select> -->
          <!-- <el-date-picker
            v-model="value2"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            :picker-options="pickerOptions">
          </el-date-picker> -->
          <!-- <el-select :value="valueTitle" :clearable="clearable" @clear="clearHandle"> -->
          <el-select :value="valueTitle">
            <el-option :value="valueTitle" :label="valueTitle">
              <el-tree id="tree-option"
                ref="selectTree"
                :accordion="accordion"
                :data="departmentData"
                :props="defaultProps"
                :node-key="defaultProps.value"
                :default-expanded-keys="defaultExpandedKey"
                @node-click="handleNodeClick">
              </el-tree>
            </el-option>
          </el-select>

          <el-date-picker
            v-model="dataBriefingParam.sameMonth"
            type="month"
            value-format="yyyy-MM"
            format="yyyy-MM"
            placeholder="选择月"
            :clearable="false"
            @change="monthlyCheck">
          </el-date-picker>
        </div>
      </div>
      <el-row>

        <div style="display: block;">
          <el-col :span="6">
            <div class="grid-content bg-purple" style="border:solid 1px #000;margin: 20px;">
              <p>应收未回款</p>
              <p><strong style="font-size: x-large;">{{ feige(dataBriefing.noRepayment) }}</strong></p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple-light" style="border:solid 1px #000;margin: 20px">
              <p>本月回款</p>
              <p><strong style="font-size: x-large;">{{ feige(dataBriefing.backMoney) }}</strong></p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple" style="border:solid 1px #000;margin: 20px">
              <p>本月销售额</p>
              <p><strong style="font-size: x-large;">{{ feige(dataBriefing.salesVolume) }}</strong></p>
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
              <el-date-picker style="width:240px;"
                v-model="startEndTime"
                type="daterange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
                :default-value="new Date()"
              />
              <!-- <el-select v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select> -->
              <el-select :value="valueTitle">
                <el-option :value="valueTitle" :label="valueTitle">
                  <el-tree id="tree-option"
                    ref="selectTree"
                    :accordion="accordion"
                    :data="departmentData"
                    :props="defaultProps"
                    :node-key="defaultProps.value"
                    :default-expanded-keys="defaultExpandedKey"
                    @node-click="aa">
                  </el-tree>
                </el-option>
              </el-select>
              <el-select v-model="dayOrMonthOption.dayOrMonth" style="width:100px;">
                <el-option label="按天" :value="0" />
                <el-option label="按月" :value="1" />
              </el-select>
              <!-- <el-select v-model="dayOrMonth">
                <el-option
                  v-for="item in dayOrMonthOption"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select> -->
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
              <!-- <el-select v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select> -->
              <el-select :value="valueTitle">
                <el-option :value="valueTitle" :label="valueTitle">
                  <el-tree id="tree-option"
                    ref="selectTree"
                    :accordion="accordion"
                    :data="departmentData"
                    :props="defaultProps"
                    :node-key="defaultProps.value"
                    :default-expanded-keys="defaultExpandedKey"
                    @node-click="bb">
                  </el-tree>
                </el-option>
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
import moment from 'moment'

import { reportdata, departrelation, queryDataBriefing, diguiDepartmentIds } from '@/api/dashboard'

export default {
  name: 'el-tree-select',
  data() {
    return {
      // 数据简报
      departmentIdValue: null,
      // departmentNameValue: null,
      // clearable: true,
      accordion: false,
      valueId: '',    // 初始值
      valueTitle: '',
      defaultExpandedKey:[],

      dataBriefing: {
        noRepayment: '0', //  应收未回款
        backMoney: '0', // 本月回款
        salesVolume: '0', // 销售额
      },
      dataBriefingParam: {
        departmentNames: [],  //部门名称
        sameMonth: new Date(),  // 年月
      },
      activeNames: ['1', '2', '3', '4', '5'],
      indexDTO: {
        companyName: '',
        startDate: '',
        endDate: ''
      },
      departmentData: [{
        departmentId: '',
        departmentName: '',
        sonDepartment: []
      }],
      defaultProps: {
        value: 'departmentId',
        label: 'departmentName',
        children: 'sonDepartment'
      },
      // 新增销售
      startEndTime: [],
      dayOrMonthOption: {
        startTime: '',
        endTime: '',
        departmentNames: [], 
        dayOrMonth: 0,
      },

      charts: '',
      chart1: '',
      score: '100',
      source: [
        ['回款对比', '实际回款', '计划回款'],
        ['2019-02-25', 45, 12],
        ['2019-03-04', 30, 15],
        ['2019-03-18', 45, 8],
        ['2019-03-26', 10, 2]
      ],
      options: [{
        value: '选项1',
        label: '深圳睿云产业科技有限公司'
      }, {
        value: '选项2',
        label: '深圳橙势科技有限公司'
      }],
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
      value1: '',
      // 开始结束时间
      value2: '',
      value: ''

    }
  },
  mounted() {
    this.$nextTick(function() {
      this.draw('report')
      this.drawLine()
      // this.draw("report2");
    })
    this.initData()
    /* let that = this;
        that.initData();
        window.addEventListener("resize", () => {
            this.resize(); //监听屏幕大小，来刷新画布
        });*/

  },
  methods: {

    initData() {
      // 开始调用
      // console.info(this.value2)
      // reportdata(this.indexDTO).then(response => {
      //   console.info('response:' + response)
      // })

      // 查询部门层级关系后
      departrelation().then(response => {
        // 数据简报
        this.departmentData = [response.data]
        this.departmentIdValue = this.departmentData[0].departmentId
        this.departmentNameValue = this.departmentData[0].departmentName
        console.log(this.departmentIdValue + '-------------------' + this.departmentNameValue)
        this.valueId = this.departmentIdValue
        this.valueTitle = this.departmentNameValue
        this.initHandle()
        console.log(this.sameMonth + 'ffffffffffffffffffffffffffffffffffffffff')

        this.dataBriefingParam.departmentNames = diguiDepartmentIds(this.departmentData, this.dataBriefingParam.departmentNames)
        this.dataBriefingParam.sameMonth = moment(this.dataBriefingParam.sameMonth).format('YYYY-MM')
        queryDataBriefing(this.dataBriefingParam).then(response => {
          this.dataBriefing = response.data
        })

        // 新增销售
        // this.startEndTime = 
      })
    },

    monthlyCheck() {
      queryDataBriefing(this.dataBriefingParam).then(response => {
        this.dataBriefing = response.data
      })
    },

    // 初始化值
    initHandle(){
      // console.log('******************************************' + this.valueId)
      // if(this.valueId){
      //   this.valueTitle = this.$refs.selectTree.getNode(this.valueId).data[this.defaultProps.label]     // 初始化显示
      //   this.$refs.selectTree.setCurrentKey(this.valueId)       // 设置默认选中
      //   this.defaultExpandedKey = [this.valueId]      // 设置默认展开
      // } 
      // this.$nextTick(()=>{
      //   let scrollWrap = document.querySelectorAll('.el-scrollbar .el-select-dropdown__wrap')[0]
      //   let scrollBar = document.querySelectorAll('.el-scrollbar .el-scrollbar__bar')
      //   scrollWrap.style.cssText = 'margin: 0px; max-height: none; overflow: hidden;'
      //   scrollBar.forEach(ele => ele.style.width = 0)
      // })
      console.log(this.departmentIdValue + '-----\\\\\-----' + this.valueTitle)
    },

    // 切换选项
    handleNodeClick(node){
      this.dataBriefingParam.departmentNames = []
      this.dataBriefingParam.departmentNames = diguiDepartmentIds([node], this.dataBriefingParam.departmentNames)
      this.valueTitle = node[this.defaultProps.label]
      this.valueId = node[this.defaultProps.value]
      this.$emit('getValue',this.valueId)
      this.defaultExpandedKey = []

      queryDataBriefing(this.dataBriefingParam).then(response => {
        this.dataBriefing = response.data
      })
    },

    // 清除选中
    // clearHandle(){
    //   this.valueTitle = ''
    //   this.valueId = null
    //   this.defaultExpandedKey = []
    //   this.clearSelected()
    //   this.$emit('getValue',null)
    // },
    // /* 清空选中样式 */
    // clearSelected(){
    //   let allNode = document.querySelectorAll('#tree-option .el-tree-node')
    //   allNode.forEach((element)=>element.classList.remove('is-current'))
    // },

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
          text: '金额/万元',
          subtext: '',
          textStyle: {
            fontSize: 12,
          },
          left: '5%',
          top: '4%'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['金额/万元']
        },
        toolbox: {
          show: false,
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
            // name: '金额/万元',
            type: 'line',
            data: [11, 41, 22, 74, 55, 86, 237],
            markPoint: {
              data: [
                // { type: 'max', name: '最大值' },
                // { type: 'min', name: '最小值' }
              ]
            },
            label : {
              show: true
              }
            },
            /* markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        }*/
        ]
      })
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
  },
  watch: {
    value(){
      this.valueId = this.departmentIdValue
      this.initHandle()
    }
  }
};
</script>

<style lang="scss">
  .dashboard-container {
    text-align: center;
    padding: 20px;
  }

  .el-collapse-item__header {
    display: block !important;
  }

  .el-scrollbar .el-scrollbar__view .el-select-dropdown__item{
    height: auto;
    max-height: 274px;
    // padding: 0;
    overflow: hidden;
    overflow-y: auto;
  }
  .el-select-dropdown__item.selected{
    font-weight: normal;
  }
  ul li >>>.el-tree .el-tree-node__content{
    height:auto;
    padding: 0 20px;
  }
  .el-tree-node__label{
    font-weight: normal;
  }
  .el-tree >>>.is-current .el-tree-node__label{
    color: #409EFF;
    font-weight: 700;
  }
  .el-tree >>>.is-current .el-tree-node__children .el-tree-node__label{
    color:#606266;
    font-weight: normal;
  }
</style>
