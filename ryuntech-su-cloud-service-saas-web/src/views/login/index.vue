<template>
  <div class="login-container">
    <div class="login-weaper  animated bounceInDown">
      <el-card class="login-left">
        <div class="login-time">
          {{ date }}
        </div>
        <div style="text-align: center">
          <h1>睿应收</h1>
          <h3>睿应收</h3>
          <!--<h3>By <a href="https://github.com/TyCoding/cloud-template">TyCoding</a></h3>-->
        </div>
      </el-card>
      <el-card class="login-right">
        <el-form
          ref="loginForm"
          :model="loginForm"
          :rules="loginRules"
          status-icon
          class="login-form"
          auto-complete="on"
          label-position="left"
        >

          <div class="title-container">
            <h3 class="title">登陆</h3>
          </div>

          <el-form-item prop="username">
            <span class="svg-container">
              <svg-icon icon-class="user" />
            </span>
            <el-input
              ref="username"
              v-model="loginForm.username"
              prefix-icon="el-icon-user"
              placeholder="Username"
              name="username"
              type="text"
              tabindex="1"
              auto-complete="on"
            />
          </el-form-item>

          <el-form-item prop="password">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.password"
              :type="passwordType"
              placeholder="Password"
              name="password"
              tabindex="2"
              auto-complete="on"
              @keyup.enter.native="handleLogin"
            />
            <span class="show-pwd" @click="showPwd">
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>

          <el-button
            :loading="loading"
            type="primary"
            style="width:100%;margin-bottom:30px;"
            @click.native.prevent="handleLogin"
          >Login
          </el-button>
        </el-form>

        <!--弹出窗口：选择公司-->
        <el-dialog title="请选择公司" :visible.sync="editRolesDialogVisible" width="30%" padding="1px" margin="0px" align="center" @close="closeDialog">
          <div style="margin: -20px 0px 0px 0px;">
            <!--<a v-for="(item,index) in companyList" :key="index">{{item}}</a>-->
            <ul id="companycss">
              <li v-for="(item,index) in companyList" @click="handleLink(item)">
                <span v-if='item.company'>{{ item.company }}</span>
              </li>
            </ul>
          </div>
        </el-dialog>
      </el-card>
    </div>
  </div>
</template>

<script>
import { asyncRouterMap } from '@/router'
// import { filterAsyncRouter } from '@/store/modules/permission'
import { parseTime, companyListByUserName } from '@/utils/index'
import { setToken } from '@/utils/auth'
import { logout } from '@/api/user'

export default {
  name: 'Login',
  data() {
    return {
      companyList: null,
      userDetail: null,
      date: parseTime(new Date().getTime(), ''),
      loginForm: {
        username: 'ryuntech',
        password: '12345678'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur' }],
        password: [{ required: true, trigger: 'blur' }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      editRolesDialogVisible:false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  mounted() {
    const _this = this
    this.timer = setInterval(() => {
      _this.date = parseTime(new Date().getTime(), '')
    }, 1000)
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.editRolesDialogVisible = true
            // this.$router.push({ path: this.redirect || '/' })

            companyListByUserName(this.loginForm.username).then(response => {
              this.companyList = response.data
            }).catch(() => {
              this.$message({
                type: 'error',
                message: '用户所在公司查询失败！'
              });
            })

          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    closeDialog() {
      this.loading = false
      this.editRolesDialogVisible = false
      this.$store.dispatch('user/logout')
    },
    // 选择公司事件
    handleLink(data) {

      this.$store.dispatch('user/getInfo').then(responses => {
        this.userDetail = responses
        // 如果选中公司和查询到的该用户信息中的公司一样，将该公司对应的权限添加到路由表中
        if (data.company == responses.companyName) {
          this.$router.addRoutes(responses.perms)
        }
        this.$router.push({ path: this.redirect || '/' })
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '用户所在公司相应权限信息查询失败！'
        });
      })

    }
  }
}
</script>
<style lang="scss">
  .el-input__inner {
    padding-left: 35px !important;
  }
</style>
<style lang="scss" scoped>
  $bg: #2d3a4b;
  $dark_gray: #889aa4;
  $light_gray: #eee;
  @-webkit-keyframes animate-cloud {
    0% {
      background-position: 600px 100%
    }
    to {
      background-position: 0 100%
    }
  }

  #companycss {
    margin: 0px 36px 0px 0px;
  }

  #companycss li {
    cursor: pointer;
    border: 1px solid black;
    border-left: none;
    border-right: none;
    margin: 0px;
    padding: 6px;
    list-style-type:none;
  }

  #companycss li:hover {
    background-color: #F0F0F0;
  }

  .el-card {
    border: none !important;
  }

  .login-container {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    position: relative;
    width: 100%;
    height: 100%;
    margin: 0 auto;
    background: url(../../assets/bg_cloud.jpg) 0 bottom repeat-x #049ec4;
    -webkit-animation: animate-cloud 20s linear infinite;
    animation: animate-cloud 20s linear infinite;

    .login-weaper {
      margin: 0 auto;
      width: 1000px;
      -webkit-box-shadow: -4px 5px 10px rgba(0, 0, 0, .4);
      box-shadow: -4px 5px 10px rgba(0, 0, 0, .4);
    }

    .bounceInDown {
      -webkit-animation-name: bounceInDown;
      animation-name: bounceInDown;
    }

    .animated {
      -webkit-animation-duration: 1s;
      animation-duration: 1s;
      -webkit-animation-fill-mode: both;
      animation-fill-mode: both;
    }

    .login-time {
      position: absolute;
      left: 25px;
      top: 25px;
      width: 100%;
      color: #fff;
      font-weight: 400;
      opacity: .9;
      font-size: 20px;
      overflow: hidden;
    }

    .login-left {
      border-top-left-radius: 5px;
      border-bottom-left-radius: 5px;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      justify-content: center;
      -webkit-box-orient: vertical;
      -webkit-box-direction: normal;
      -ms-flex-direction: column;
      flex-direction: column;
      background-color: #409eff;
      color: #fff;
      float: left;
      width: 50%;
      position: relative;
      min-height: 500px;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
    }

    .login-right {
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      border-left: none;
      border-top-right-radius: 5px;
      border-bottom-right-radius: 5px;
      color: #fff;
      background-color: #fff;
      width: 50%;
      float: left;
      min-height: 500px;
    }

    .login-form {
      position: relative;
      width: 520px;
      max-width: 100%;
      margin: 0 auto;
      overflow: hidden;
      padding: 0 56px;
    }

    .svg-container {
      position: absolute;
      top: -5px;
      font-size: 14px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
      padding: 6px 5px 6px 15px;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }

    .el-input {
      position: initial !important;
    }

    .title-container {
      position: relative;

      .title {
        font-size: 26px;
        color: #606266;
        margin: 50px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }
    }

    .el-icon-user::before {
      content: "\e6e3";
    }

    .show-pwd {
      position: absolute;
      right: 33px;
      top: 0px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }
  }
</style>
