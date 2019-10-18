<!--Author liugg 2019年10月18日16:22:16-->
<template>
    <div class="app-container">
        <el-tabs v-model="activeName" @tab-click="gotoTab">
            <el-tab-pane label="员工帐号" name="one"></el-tab-pane>
            <el-tab-pane label="部门设置" name="two"></el-tab-pane>
            <el-tab-pane label="角色权限" name="three"></el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
  export default {
    name: 'employee_department_department',
    data() {
      return {
        activeName: 'two'
      }
    },
    methods: {
      gotoTab(tab, e) {
        if (tab.name == 'one') {
          this.$router.replace('/system/employee_department_employee')
        } else if (tab.name == 'two') {
          this.$router.replace('/system/employee_department_department')
        } else if (tab.name == 'three') {
          this.$router.replace('/system/employee_department_access')
        }
        return
      }
    }
  }
</script>

<style lang="scss">


</style>
