 <!--Author liugg 2019年10月18日16:22:16-->
<template>
  <div class="app-container">
    <div class="item-container">
      <div class="item">
        <img class="item-l" src="https://www.baidu.com/img/bd_logo1.png" alt="">
        <div class="item-r">
          <h3>企业信息</h3>
          <p>设置公司的基本信息</p>
        </div>
      </div>

      <div class="item" @click="gotoPage('/system/message_setting')">
        <img class="item-l" src="https://www.baidu.com/img/bd_logo1.png" alt="">
        <div class="item-r">
          <h3>消息设置</h3>
          <p>业务消息推送设置</p>
        </div>
      </div>

      <div class="item" @click="gotoPage('/system/e_d_employee')">
        <img class="item-l" src="https://www.baidu.com/img/bd_logo1.png" alt="">
        <div class="item-r">
          <h3>部门与员工</h3>
          <p>管理公司架构与员工帐号</p>
        </div>
      </div>

      <div class="item">
        <img class="item-l" src="https://www.baidu.com/img/bd_logo1.png" alt="">
        <div class="item-r">
          <h3>财务设置</h3>
          <p>设置财务相关信息</p>
        </div>
      </div>

    </div>

  </div>
</template>

<script>

export default {
  name: 'System',
  data() {
    return {

    }
  },
  methods: {

  }
}
</script>

 <style lang="scss" scoped>
   .item-container{
     margin-top: 100px;
     display: flex;
     flex-direction: row;
     justify-content: space-between;
     flex-wrap: wrap;
     .item{
       cursor: pointer;
       width: 310px;
       height: 100px;
       display: flex;
       flex-direction: row;
       align-items: center;
       margin-bottom: 20px;
       padding: 20px;
       img{
         width: 80px;
         height: 80px;
       }
       div.item-r {
         margin-left: 5px;
         h3{
           color: #409EFF;
         }
       }
     }
   }

 </style>
