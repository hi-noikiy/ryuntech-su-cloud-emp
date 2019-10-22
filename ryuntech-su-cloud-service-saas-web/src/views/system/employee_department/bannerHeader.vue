<!--Author liugg 2019年10月18日16:22:16-->
<template>
  <div class="b-container">
    <div class="b-item" @click="gotoTab('one')" :class="{active: activeName == 'one'}">员工帐号</div>
    <div class="b-item" @click="gotoTab('two')" :class="{active: activeName == 'two'}">部门设置</div>
    <div class="b-item" @click="gotoTab('three')" :class="{active: activeName == 'three'}">角色权限</div>
    <div class="r-item"></div>
  </div>
</template>

<script>

export default {
  name: 'BannerHeader',
  props: { active: { type: String, default: 'one' }},
  data() {
    return {
      activeName: this.active
    }
  },
  methods: {
    gotoTab(name) {
      console.log(name)
      if (name === 'one') {
        this.$router.push('/system/e_d_employee')
      } else if (name === 'two') {
        this.$router.push('/system/e_d_department')
      } else if (name === 'three') {
        this.$router.push('/system/e_d_access')
      }
    }
  }
}
</script>

<style lang="scss">
  .b-container{
    font-size: 16px;
    height: 30px;
    margin-bottom: 15px;
    min-width: 600px;
    display: flex;
    div{
      border-bottom: 2px solid #d3dce6;
      &.b-item{
        width: 120px;
        cursor: pointer;
        text-align: center;
        &.active{
          color: #409EFF;
          border-bottom-color: #409EFF;
        }
      }
      &.r-item{
        flex: 1;
      }
    }
  }
</style>
