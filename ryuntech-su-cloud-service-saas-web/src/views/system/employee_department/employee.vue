 <!--Author liugg 2019年10月18日16:22:16-->
<template>
  <div class="app-container">
      <bannerHeader active="one"/>
      <div class="operate-banner">
          <el-select style="width: 120px" v-model="search.department_id" placeholder="全部部门" clearable size="mini">
              <el-option label="部门01" :value="0" />
              <el-option label="部门02" :value="1" />
          </el-select>
          <el-select style="margin-left: 15px;width: 120px" v-model="search.status" placeholder="全部状态" clearable size="mini">
              <el-option label="正常" :value="0" />
              <el-option label="禁用" :value="1" />
          </el-select>
          <el-input v-model="search.keyword" clearable size="mini" style="margin-left: 15px;width: 350px;" placeholder="请输入员工姓名/帐号/手机/角色">
          <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>

          <el-button @click="$router.push('/system/add_employee')" type="primary" size="mini" style="float: right;margin-right: 20px">新建员工</el-button>
      </div>

      <el-table style="margin-top: 20px" v-loading="listLoading" :data="employeeList" fit highlight-current-row>
          <el-table-column align="center" label="名字" width="100">
              <template slot-scope="scope">
                  {{ scope.row.name }}
              </template>
          </el-table-column>
          <el-table-column align="center" label="角色" min-width="120">
              <template slot-scope="scope">
                  {{ scope.row.roleName }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="部门" min-width="100">
              <template slot-scope="scope">
                  {{ scope.row.customerName }}
              </template>
          </el-table-column>

          <el-table-column align="center" label="手机号" width="160">
              <template slot-scope="scope">
                  <i class="el-icon-time" />
                  <span>{{ scope.row.phone }}</span>
              </template>
          </el-table-column>

          <el-table-column align="center" label="数据权限" width="150">
              <template slot-scope="scope">
                  <span>{{ scope.row.contractAmount }}</span>
              </template>
          </el-table-column>

          <el-table-column align="center" label="状态" width="150">
              <template slot-scope="scope">
                  <span>{{ scope.row.status }}</span>
              </template>
          </el-table-column>

          <el-table-column align="center" label="操作" width="200">
              <template slot-scope="scope">
                  <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row.contractId)">编辑</el-button>
                  <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDel(scope.row.contractId)">禁用</el-button>
                  <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDel(scope.row.contractId)">恢复</el-button>
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

  </div>
</template>

<script>
import bannerHeader from './bannerHeader.vue'
export default {
  name: 'EmployeeDepartmentEmployee',
  components: { bannerHeader },
  data() {
    return {
      search: {
        department_id: undefined,
        status: undefined,
        keyword: undefined
      },
      listQuery: {
        page: 1,
        limit: 20
      },
      employeeList: [],
      total: 0,
      listLoading: false,
    }
  },
  methods: {
    fetchData() {

    }

  }
}
</script>

 <style lang="scss">

 </style>
