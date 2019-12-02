<template>
  <div class="add-customer-center">
    <div style="border:1px solid #dddddd;">
      <div style="border:1px solid #cccccc;">
        <div style="display: flex;">
          <h1>{{ contractDetail.contractName }}</h1>
          <div style="margin: 28px;border:1px solid #dddddd;border-radius: 4px;background:#53FF53;">
            <span v-if='contractDetail.status==0'>已逾期</span>
            <span v-if='contractDetail.status==1'>已完成</span>
            <span v-if='contractDetail.status==2'>执行中</span>
          </div>
        </div>
        <h4>合同编号：{{ contractDetail.contractId }}</h4>
      </div>
    </div>

    <div>
      <el-button type="text" size="mini" style="margin-left: 14px;" icon="el-icon-edit">编辑</el-button>
      <el-button type="text" size="mini" style="margin-left: 14px;" icon="el-icon-edit">添加回款</el-button>
    </div>

    <div style="border:1px solid #dddddd;overflow: auto;">
      <div style="border:1px solid #dddddd;width:50%;float:left;">
        <div class="aa">
          <div class="bb">
            <div>
              <h4>客户名称</h4>
              <h2>{{ contractDetail.customerName }}</h2>
            </div>
            <div>
              <h4>合同金额</h4>
              <h2>￥{{ feige(contractDetail.contractAmount) }}</h2>
            </div>
            <div>
              <h4>待收金额</h4>
              <h2>￥{{ feige(contractDetail.balanceAmount) }}</h2>
            </div>
          </div>

          <div class="cc">
            <div>
              <span>联系人：</span><span class="cc-child">{{ contractDetail.contacts }}</span>
            </div>
            <div>
              <span>联系电话：</span><span class="cc-child">{{ contractDetail.contactsPhone }}</span>
            </div>
            <div>
              <span>签约时间：</span><span class="cc-child">{{ jiequ(contractDetail.contractTime) }}</span>
            </div>
            <div>
              <span>负责员工：</span><span class="cc-child">{{ contractDetail.staffName }}</span>
            </div>
            <div>
              <span>所属部门：</span><span class="cc-child">{{ contractDetail.department }}</span>
            </div>
          </div>
        </div>

        <div style="border:1px solid #dddddd;width: 100%;margin: 10px 0px;">
          <div style="display: flex;padding: 0px 10px;">
            <h3>合同回款计划(共{{ this.collectionPlans.length }}期)</h3>
            <!-- <el-button style="margin:0px 10px 0px 70%;" @click="addItem" type="text">修改回款计划</el-button> -->
            <el-button style="margin:0px 10px 0px 64%;" type="text" @click="editCollectionPlan">修改回款计划</el-button>
          </div>
          <div style="width:100%;">
            <el-table :data="collectionPlans" width="100%">
              <el-table-column align="center" prop="planTime" label="日期" width="120%">
                <template slot-scope="scope">
                  <span>{{ scope.row.planTime }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="planAmount" label="计划金额" width="140%">
                <template slot-scope="scope">
                  <span>￥{{ scope.row.planAmount }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="backAmount" label="已回款" width="140%">
                <template slot-scope="scope">
                  <span>￥{{ scope.row.backAmount }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="remakes" label="备注" width="140%">
                <template slot-scope="scope">
                  <span>{{ scope.row.remakes }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="status" label="状态" width="150%">
                <template slot-scope="scope">
                  <span v-if='scope.row.status==0'>已逾期</span>
                  <span v-if='scope.row.status==1'>已还款</span>
                  <span v-if='scope.row.status==2'>未开始</span>
                  <span v-if='scope.row.status==3'>回款中</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <div style="border:1px solid #dddddd;width: 100%;margin: 10px 0px;">
          <div style="display: flex;padding: 0px 10px;">
            <h3>合同收款记录</h3>
            <!-- <el-button style="margin:0px 10px 0px 70%;" @click="addItem" type="text">修改回款计划</el-button> -->
            <el-button style="margin:0px 10px 0px 72%;" type="text" @click="addCollection">快速回款</el-button>
          </div>
          <div style="width:100%;">
            <el-table :data="collectionRecords" width="100%" height="300">
              <el-table-column align="center" prop="time" label="日期" width="100%">
                <template slot-scope="scope">
                  <span>{{ jiequ(scope.row.time) }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="amount" label="回款金额" width="100%">
                <template slot-scope="scope">
                  <span>￥{{ scope.row.amount }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="type" label="回款方式" width="100%">
                <template slot-scope="scope">
                  <span>￥{{ scope.row.type }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="voucherType" label="回款凭证" width="100%">
                <template slot-scope="scope">
                  <!-- <span>{{ scope.row.voucherType }}</span> -->
                  <span><img src="../../assets/liucheng.jpg" class="avatar" style="width: 50%;height: 50%;margin: auto;"></span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="status" label="状态" width="100%">
                <template slot-scope="scope">
                  <span v-if='scope.row.status==0'>已作废</span>
                  <span v-if='scope.row.status==1'>已收款</span>
                  <span v-if='scope.row.status==2'>还款中</span>
                </template>
              </el-table-column>

              <el-table-column align="center" prop="createBy" label="创建人" width="100%">
                <template slot-scope="scope">
                  <span>{{ scope.row.createBy }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" label="操作" width="120%">
                <template slot-scope="scope">
                  <div v-if="scope.row.status === '0'">
                    <el-button type="text" size="mini" icon="el-icon-edit" @click="handleDel(scope.row.collectionId)">删除</el-button>
                  </div>
                  <div v-else>
                    <el-button type="text" size="mini" icon="el-icon-edit" @click="handleZuoFei(scope.row.collectionId)">作废</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <div style="border:1px solid #dddddd;width: 100%;margin: 10px 0px;">
          <div style="margin: 0px 20px;">
            <h3>合同附件</h3>
          </div>
          <div class="demo-image" style="display: flex;width:200px;height:200px;margin:0px 20px;">
            <div class="block" style="display: flex;">
              <img src="../../assets/liucheng.jpg" class="avatar">
              <img :src="contractDetail.attachmentCode" class="avatar">
            </div>
          </div>
        </div>
      </div>

      <div style="float:right;border:1px solid #dddddd;margin: 0px 20px;width:45%;">
        <div style="margin: 0px 20px;">
          <h3>跟进记录</h3>
        </div>
        <div style="margin: 0px 20px;border:1px solid #dddddd;height:700px;overflow-y: auto" v-if="followUpRecords !== null" >
          <div v-for="(item, index) in followUpRecords" :key="index">
            <span>{{ item.updatedAt }}</span>
            <span>{{ item.staffName }}</span>
            <span>({{ item.staffName }})</span>
            <br/>
            <span>添加跟进记录</span>
            <div style="border:1px solid #dddddd;padding:4px;">
              <span>销售回款预测：</span>
              <span v-if="item.estimateTime !== null">预计{{ item.estimateTime }}回款{{ item.estimateAmount }}元</span>
              <span v-else>未填写</span>
              <br/>
              <span>{{ item.content }}</span>
              <div style="border:1px solid #dddddd;display:flex;width: 100%;height: 50px;margin:4px;">
                <div v-if="item.attachmentCode !== null" class="block" style="width: 8%;height: 100%;margin-left:1%;">
                  <img src="../../assets/liucheng.jpg" class="avatar" style="width:100%;height:100%">
                </div>
                <div v-if="item.attachmentCode !== null" class="block" style="width: 8%;height: 100%;margin-left:1%;">
                  <img src="../../assets/liucheng.jpg" class="avatar" style="width:100%;height:100%;">
                </div>
              </div>
              <div v-if="item.followupRecordComments !== null" style="background:#E0E0E0;border:1px solid #dddddd;width:98%;margin:4px;">
                <div v-for="(subItem, index) in item.followupRecordComments" :key="index" style="margin:3px;">
                  <span>{{ subItem.staffName }}：{{ subItem.commentContent }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else style="margin: 0px 20px;border:1px solid #dddddd;text-align:center;">暂无跟进记录</div>
      </div>
    </div>
  </div>
</template>

<script>
import { getListByContractId, del, zuofei } from '@/api/collection'

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
      customerList: null,
      customerContractList: null,
      listLoading: false,

      contractDetail: null,
      collectionPlans: null,
      collectionRecords: null,
      followUpRecords: null,
      followupRecordComments: null
    }
  },
  created(){
    if(typeof(this.$route.params.data) !== "undefined") {
      this.contractDetail = this.$route.params.data
      this.collectionPlans = this.contractDetail.receivableCollectionPlanDTOs
      this.followUpRecords = this.contractDetail.followUpRecords
      this.showCollections()
    }
  },
  methods: {

    showCollections() {
      getListByContractId(this.contractDetail.contractId).then(response => {
        this.collectionRecords = response.data
      })
    },

    editCollectionPlan() {
      this.$router.push({
        name: '编辑合同',
        params: {
          title: '编辑合同',
          data: this.contractDetail
        }
      })
    },

    addCollection() {
      this.$router.push({
        name: '添加回款',
        params: {
          title: '添加回款',
          data: this.contractDetail
        }
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
          this.showCollections()
        })
      }).catch(() => {
        this._notify('作废已取消', 'info')
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
          this.showCollections()
        })
      }).catch(() => {
        this._notify('已取消删除', 'info')
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
    },
    jiequ(value) {
      if(!value) {
        return ''
      }
      var arr = value.split(' ')
      return arr[0]
    }
  }
}
</script>

<style lang="css">
  .add-customer-center {
    padding: 20px 120px 20px 70px;
  }
  .aa {
    border:1px solid #dddddd;
    width: 100%;
  }
  .bb {
    display: flex;
  }
  .cc {
    text-align: left;
    border: 1px solid #dddddd;
    width: 100%;
    /* margin: 2%; */
    /* height: 600px; */
    /* padding: 10px; */
  }
  .bb div {
    border: 1px solid #dddddd;
    width: 30%;
    text-align: center;
    margin: 0px 3% 1% 0px;
  }
  /* .cc span {
    border: 1px solid #dddddd;
    text-align: center;
    margin: 2%;
    padding: 2%;
  }
  span.c-clild {
    border: 1px solid #dddddd;
    text-align: center;
    margin: 2% 4% 2% 0px;
    padding: 2%;
  } */
  .cc div {
    /* border: 1px solid #dddddd; */
    display: inline-table;
    text-align: center;
    margin: 1% 2% 1% 1%;
    width: 30%;
  }
  span.cc-child {
    margin: 0px 14%;
  }
</style>

