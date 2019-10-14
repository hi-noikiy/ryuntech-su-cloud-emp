<template>
    <div class="app-container">
        <div class="form-group">
            <h3>应收合同</h3>
            <div @click="toggerSelectContract" class="contract-select-btn">
                <span v-if="contractName">{{ contractName }}</span>
                <span v-else class="ph">
                    请选择回款的应收合同
                </span>
                <span class="right">●●●</span>
            </div>
        </div>

        <el-form ref="form" class="form-group add-collect-form" :model="form" label-position="right" label-width="120px">
            <h3>回款信息</h3>

            <el-form-item label="回款金额" prop="amount">
                <el-input v-model="form.amount" />
            </el-form-item>

            <el-form-item label="收款方式" prop="type">
                <el-select v-model="form.type" placeholder="请选择收款方式" style="width: 100%">
                    <el-option v-for="(value,key) in collectionTypeOptions" :key="key" :label="value" :value="key" />
                </el-select>
            </el-form-item>

            <el-form-item label="收款日期">
                <el-date-picker
                        v-model="form.time"
                        style="width: 100%"
                        type="datetime"
                        placeholder="选择收款日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        format="yyyy-MM-dd HH:mm:ss"
                />
            </el-form-item>

            <el-form-item label="备注">
                <el-input v-model="form.remarks" type="textarea"></el-input>
            </el-form-item>

            <el-form-item style="width: 100%" label="附件">
            </el-form-item>

        </el-form>

        <div class="operate-btn-group">
            <el-button type="primary">　提　交　</el-button>
            <el-button @click="$router.push('/collection/list')">　取　消　</el-button>
        </div>


        <el-dialog title="选择应收合同" class="contract-select-dialog" :visible.sync="showSelectContract" width="90%">
            <div style="margin-bottom: 10px">
                <el-select v-model="search.status" clearable size="mini" placeholder="全部状态">
                    <el-option v-for="(value,key) in contractStatusOptions" :label="value" :value="key" />
                </el-select>
                <el-input style="width: 200px" size="mini" v-model="search.keyword" placeholder="合同名称"></el-input>
                <el-button type="primary" size="mini" @click="getContractList">搜索</el-button>
            </div>

            <el-table v-loading="listLoading" @current-change="handleCurrentChange" :data="contractList" border fit highlight-current-row>
                <el-table-column label=" " width="40">
                    <template slot-scope="scope" align="center">
                        <span v-if="checkedContract && checkedContract.contractId == scope.row.contractId">√</span>
                    </template>
                </el-table-column>

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

                <el-table-column align="center" prop="contractAmount" label="合同金额" width="120">
                    <template slot-scope="scope">
                        <span>{{ scope.row.contractAmount }}</span>
                    </template>
                </el-table-column>

                <el-table-column align="center" prop="collectionAmount" label="合同状态" width="90">
                    <template slot-scope="scope">
                        <span>{{ scope.row.status && contractStatusOptions[scope.row.status] }}</span>
                    </template>
                </el-table-column>

                <el-table-column align="center" prop="collectionAmount" label="负责员工" width="90">
                    <template slot-scope="scope">
                        <span>{{ scope.row.staffName }}</span>
                    </template>
                </el-table-column>

                <el-table-column v-if="false" align="center" label="部门" width="150">
                    <template slot-scope="scope">
                        <span>{{ scope.row.department }}</span>
                    </template>
                </el-table-column>
            </el-table>

            <pagination
                    v-show="contractTotal>0"
                    :total="contractTotal"
                    :page.sync="listQuery.page"
                    :limit.sync="listQuery.limit"
                    @pagination="getContractList"
            />

            <div slot="footer" class="dialog-footer" style="margin-right: 30px">
                <el-button type="primary" @click="checkContract">　确 定　</el-button>
                <el-button @click="showSelectContract = false">　取 消　</el-button>
            </div>

        </el-dialog>
    </div>
</template>

<script>
  import { del } from '@/api/collection'
  import { getList as getContractList } from '@/api/contract'
  import { collectionTypeOptions } from './collection'
  import { contractStatusOptions } from '../contract/contract'

  import { parseTime } from '@/utils/index'

  export default {
    data() {
      return {
        contractName: '',
        showSelectContract: false,
        collectionTypeOptions: collectionTypeOptions,
        contractStatusOptions: contractStatusOptions,
        form: {
          contractId: undefined,
          amount: undefined,
          type: undefined,
          time: new Date(),
          remarks: undefined
        },
        listQuery: {
          page: 1,
          limit: 20
        },
        search: {
          status: undefined,
          keyword: undefined
        },
        listLoading: false,
        contractList: [],
        contractTotal: 0,
        checkedContract: undefined,
      }
    },
    methods: {
      handleCurrentChange(val) {
        this.checkedContract = val
      },
      getContractList() {
        this.listLoading = true
        getContractList(this.listQuery, this.search).then(response => {
          this.contractList = response.data.records
          this.contractTotal = response.data.total
          this.listLoading = false
        })
      },
      toggerSelectContract(){
        this.showSelectContract = !this.showSelectContract
        if (this.showSelectContract) {
          this.getContractList()
        }
      },
      checkContract() {
        if (!this.checkedContract) this._notify("请先选择合同！",'e')
        this.form.contractId = this.checkedContract.contractId
        this.contractName = this.checkedContract.contractId + ' : ' + this.checkedContract.contractName
        this.showSelectContract = false
      }
    }
  }
</script>

<style lang="scss" scoped>
    .form-group{
        box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
        padding: 20px;
        h3{
            font-size: 15px;
            margin: 0 0 8px 0;
            font-weight: 400;
        }
    }
    .contract-select-btn{
        width: 600px;
        border: 1px solid #eee;
        border-radius: 4px;
        padding: 5px 10px;
        display: flex;
        justify-content: space-between;
        cursor: pointer;
        font-size: 12px;
        span.ph{
            color: #333;
        }
    }
    .add-collect-form{
        margin-top: 25px;
    }
    .operate-btn-group{
        display: flex;
        justify-content: center;
        margin-top: 20px;
        .el-button + .el-button {
            margin-left: 30px;
        }
    }
</style>