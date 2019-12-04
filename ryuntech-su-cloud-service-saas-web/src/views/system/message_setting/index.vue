<template>
  <div class="message-config-css">
    <h2>消息设置</h2>
    <div>
      <h4>应收计划逾期跟进提醒</h4>
      <div style="text-align:center;">
        <el-row>
          <el-col :span="4"><div style="border:1px solid #dddddd;background:#E0E0E0;padding:20px;">推送对象</div></el-col>
          <el-col :span="4"><div style="border:1px solid #dddddd;background:#E0E0E0;padding:20px;">推送方式</div></el-col>
          <el-col :span="4"><div style="border:1px solid #dddddd;background:#E0E0E0;padding:20px;">提醒时间</div></el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            <div style="border:1px solid #dddddd;background:#F0F0F0;padding:20px;">
              <el-checkbox v-model="planOverdueRemind.followPerson" @change="editOverdueConfig" true-label="1" false-label="0">跟进人</el-checkbox>
              <el-checkbox v-model="planOverdueRemind.departHead" @change="editOverdueConfig" true-label="1" false-label="0">部门负责人</el-checkbox>
            </div>
          </el-col>
          <el-col :span="4">
            <div style="border:1px solid #dddddd;background:#F0F0F0;padding:20px;">
              <el-checkbox v-model="planOverdueRemind.emailType" @change="editOverdueConfig" true-label="1" false-label="0">邮件</el-checkbox>
              <el-checkbox v-model="planOverdueRemind.wechatType" @change="editOverdueConfig" true-label="1" false-label="0">微信公众号</el-checkbox>
            </div>
          </el-col>
          <el-col :span="4">
            <div style="border:1px solid #dddddd;background:#F0F0F0;padding:17px;">
              间隔
              <el-select v-model="planOverdueRemind.intervalDay" placeholder="请选择" size="mini" style="width:80px;" @change="editOverdueConfig">
                <el-option v-for="(value,key) in overdueOptions" :key="key" :label="value" :value="value">
                </el-option>
              </el-select>
              天提醒
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div>
      <h4>应收计划到期提醒</h4>
      <div style="text-align:center;">
        <el-row>
          <el-col :span="4"><div style="border:1px solid #dddddd;background:#E0E0E0;padding:20px;">推送对象</div></el-col>
          <el-col :span="4"><div style="border:1px solid #dddddd;background:#E0E0E0;padding:20px;">推送方式</div></el-col>
          <el-col :span="4"><div style="border:1px solid #dddddd;background:#E0E0E0;padding:20px;">提醒时间</div></el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            <div style="border:1px solid #dddddd;background:#F0F0F0;padding:20px;">
              <el-checkbox v-model="planExpireRemind.followPerson" @change="editExpireConfig" true-label="1" false-label="0">跟进人</el-checkbox>
              <el-checkbox v-model="planExpireRemind.departHead" @change="editExpireConfig" true-label="1" false-label="0">部门负责人</el-checkbox>
            </div>
          </el-col>
          <el-col :span="4">
            <div style="border:1px solid #dddddd;background:#F0F0F0;padding:20px;">
              <el-checkbox v-model="planExpireRemind.emailType" @change="editExpireConfig" true-label="1" false-label="0">邮件</el-checkbox>
              <el-checkbox v-model="planExpireRemind.wechatType" @change="editExpireConfig" true-label="1" false-label="0">微信公众号</el-checkbox>
            </div>
          </el-col>
          <el-col :span="4">
            <div style="border:1px solid #dddddd;background:#F0F0F0;padding:17px;">
              间隔
              <el-select v-model="planExpireRemind.intervalDay" placeholder="请选择" size="mini" style="width:80px;" @change="editExpireConfig">
                <el-option v-for="(value,key) in expireOptions" :key="key" :label="value" :value="value">
                </el-option>
              </el-select>
              天提醒
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>

import { getOverdueConfigMessage, updateOverdueConfigMessage, getExpireConfigMessage, updateExpireConfigMessage } from '@/api/system/messageSetting'

export default {
  data() {
    return {
      overdueOptions: {
        one: 1,
        two: 2,
        three: 3,
        four: 4,
        five: 5,
        six: 6,
        seven: 7
      },
      expireOptions: {
        one: 1,
        two: 2,
        three: 3
      },
      planOverdueRemind: {
        followPerson: '0',
        departHead: '0',
        emailType: '0',
        wechatType: '0',
        intervalDay: 1,
      },
      planExpireRemind: {
        followPerson: '0',
        departHead: '0',
        emailType: '0',
        wechatType: '0',
        intervalDay: 1,
      },
    }
  },

  created() {
    this.fetchData()
  },

  methods: {
    fetchData() {
      // 查询逾期
      getOverdueConfigMessage().then(respose => {
        this.planOverdueRemind = respose.data
      })
      // 查询到期
      getExpireConfigMessage().then(respose => {
        this.planExpireRemind = respose.data
      })
    },
    editOverdueConfig() {
      updateOverdueConfigMessage(this.planOverdueRemind).then(response => {
        if(response.tcode === 200) {
          this._notify(response.msg, 'success')
        } else {
          this._notify(response.msg, 'error')
        }
      })
    },
    editExpireConfig() {
      updateExpireConfigMessage(this.planExpireRemind).then(response => {
        if(response.tcode === 200) {
          this._notify(response.msg, 'success')
        } else {
          this._notify(response.msg, 'error')
        }
      })
    }
  },
}
</script>

<style lang="scss">

.message-config-css {
  padding: 20px 20px 20px 70px;
}

</style>
