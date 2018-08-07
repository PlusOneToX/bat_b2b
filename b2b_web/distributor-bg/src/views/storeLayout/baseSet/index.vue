<template>
  <div class="base_index">
    <header>
      <h4>基本设置</h4>
    </header>
    <div class="total_body">
      <div class="total_body_header1">登录设置</div>
      <el-form label-width="200px" label-position="right" ref="promoteData" class="total_body_block">
        <el-form-item label="是否开启登录" class="total2_formItem">
          <el-radio-group v-model="formData.loginOpenFlag" class="turnover-isCon">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="提示内容" class="total2_formItem">
          <el-input
            type="textarea"
            :rows="6"
            placeholder="关闭登录时必填"
            v-model="formData.loginTips">
          </el-input>
        </el-form-item>
        <!-- <el-form-item label="提示内容(英文)" class="total2_formItem">
          <el-input
            placeholder="关闭登录时必填"
            v-model="formData.loginTipsEn">
        </el-input>
        </el-form-item> -->
      </el-form>
      <div class="total_body_header1">多级分销设置</div>
      <el-form label-width="200px" label-position="right" ref="formData" class="total_body_block">
        <el-form-item label="最高分销层级" class="total2_formItem">
          <el-input
            type="number"
            placeholder="最小填2"
            v-model="value">
          </el-input>
        </el-form-item>
      </el-form>
      <div class="total_body_header1">B2B小程序构建号</div>
      <el-form label-width="200px" label-position="right" ref="formData2" class="total_body_block">
        <el-form-item label="构建号" class="total2_formItem">
          <el-input
            placeholder="最小填1"
            v-model="buildNo">
          </el-input>
          <div class="explain">当小程序的构建号>=设置的构建号时，则会隐藏相关功能</div>
        </el-form-item>
      </el-form>

      <div class="total_body_header1">定时任务（款到发货+直发订单+国内区域+时间限制+未出库+自动关单）</div>
      <el-form label-width="200px" label-position="right" ref="cornJob" class="total_body_block">
        <el-form-item label="开启关闭" class="total2_formItem">
          <el-radio-group v-model="notDeliveryAutoCloseSwitch" class="turnover-isCon">
            <el-radio :label="1">开启</el-radio>
            <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="notDeliveryAutoCloseSwitch" label="时间限制天数" class="total2_formItem">
          <el-input
            placeholder="请输入"
            v-model="notDeliveryAutoCloseUnit"
            onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')">
          </el-input>
        </el-form-item>
      </el-form>

      <div class="foot_btn">
        <el-button class="mini-search-btn" @click="handleSave()"> 保存 </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import selectDistributor from '@/views/goods/components/selectDistributor'
import selectDistributor1 from '@/views/goods/components/selectDistributor'
export default {
  name: 'totalStationSet',
  created() {
    this.getConfig()
  },
  data() {
    return {
      formData: {
        loginOpenFlag: '', // 是否开启登录
        loginTips: '', // 提示内容
        loginTipsEn: '',
      },
      value: 2, // 最高分销层级
      buildNo: 1, // 构建号
      notDeliveryAutoCloseSwitch: 0, // 定时任务 - 开启关闭
      notDeliveryAutoCloseUnit: "",  // 定时任务 - 时间限制天数
    }
  },
  methods : {
    getConfig() { //..配置详情
      // 登录设置
      this.$http.getLoginSetting(this).then(res => {
        if (res.success) {
           this.formData = res.data
           this.formData.loginOpenFlag = res.data.loginOpenFlag === '1' ? 1 : 0
        }
      })
      // 多级分销设置、B2B小程序构建号
      let params = {key: ['distribution_layers', 'build_number', 'not_delivery_auto_close_switch', 'not_delivery_auto_close_unit']}
      this.$http.baseSetting(this, params).then(res => {
        if (res.success && res.data.length > 0) {
          res.data.forEach(item => {
            if (item.key === 'distribution_layers') {
              this.value = item.value
            }
            if (item.key === 'build_number') {
              this.buildNo = item.value
            }
            if (item.key === 'not_delivery_auto_close_switch') {
              this.notDeliveryAutoCloseSwitch = item.value === "true" ? 1 : 0
            }
            if (item.key === 'not_delivery_auto_close_unit') {
              this.notDeliveryAutoCloseUnit = item.value
            }
          })
        }
      })
    },
    handleSave() { //..保存操作
      // 验证
      if (this.formData.loginOpenFlag === 0 && this.formData.loginTips === '') {
        this.$message.error('请输入提示内容')
        return
      }
      if (this.value && Number(this.value) < 2 ) {
        this.$message.error('最高分销层级最小填2')
        return
      }
      if (this.notDeliveryAutoCloseSwitch !== 1) {
        this.notDeliveryAutoCloseUnit = "";
      }
      if (this.notDeliveryAutoCloseSwitch === 1 && this.notDeliveryAutoCloseUnit === "") {
        this.$message.error('请输入时间限制天数')
        return
      }
      this.$http.editLoginSetting(this, this.formData).then(res => {  
        if(res.success) {
          let info = [{
            key: 'distribution_layers',
            value: this.value
          }, {
            key: 'build_number',
            value: this.buildNo
          }, {
            key: 'not_delivery_auto_close_switch',
            value: this.notDeliveryAutoCloseSwitch === 1 ? "true" : "false"
          }, {
            key: 'not_delivery_auto_close_unit',
            value: this.notDeliveryAutoCloseUnit
          }]
          this.$http.editBaseSetting(this, info).then(res2 =>{
            if (res2.success) {
              this.$message({
                message: '修改成功',
                type: 'success',
                duration: 3 * 1000,
              })
              this.getConfig()
            }
          })
				}
      })
    }
  },
  components: { selectDistributor, selectDistributor1 },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .base_index {
    background-color: #fff;
    height: 80vh;
    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      h4 {
        display: inline-block;
        font-weight: 350;
        font-size: 16px;
        margin: 0 20px;
      }
    }
    .total_body {
      background-color: #fff;
      .total_body_header1 {
        line-height: 40px;
        font-size: 16px;
        color: $lakeBlue;
        font-weight: 700;
        padding: 20px 0 0 20px;
      }
      .total_body_block1 {
        margin: 0 auto;
        width: 97%;
        height: 30%;
        background-color: #f5f7fa;
        border-radius: 5px;
        border: 1px solid #dcdcdc;
        .turnover-isCon {
          margin-left: 20px;
          .turnover_span1 {
            margin-left: 10px;
            font-weight: 500;
            color: #606266;
            font-size: 14px;
          }
          .turnover_span2 {
            font-weight: 500;
            color: #606266;
            font-size: 14px;
          }
          .good-bodyRules-spanOne{
            font-weight: 500;
            color: #606266;
            font-size: 14px;
          }
          .turnover_input {
            font-weight:bold;
            color: #606266;
            width: 80px;
            font-size: 12px;
            text-align: center;
            -webkit-appearance: none;
            background-color: #fff;
            background-image: none;
            border-radius: 4px;
            border: 1px solid #dcdfe6;
            box-sizing: border-box;
            color: #606266;
            display: inline-block;
            font-size: inherit;
            height: 20px;
            line-height: 20px;
            outline: none;
            padding: 0 15px;
            transition: border-color .2s cubic-bezier(.645,.045,.355,1);
          }
          .turnover_input:hover {
            border-color: #c0c4cc;
          }
        }
        .turnover-isCon-s{
          margin:20px 0 30px 20px;
        }
        .total_frontDesk {
          display: inline;
        }
      }
      .total_body_block {
        margin: 0 auto;
        padding: 15px 0 0;
        width: 97%;
        height: 30%;
        border-radius: 5px;
        border: 1px solid #dcdcdc;
        background-color: #f5f7fa;
        .total2_formItem {
          margin-bottom: 15px;
           /deep/.el-textarea__inner{
            width:500px;
          }
          /deep/.el-input__inner{
            width:300px;
          }
        }
        .turnover-isCon{
          margin-left: 20px;
        }
      }
      .foot_btn {
        text-align: center;
        margin: 20px;
        padding-bottom: 20px;
      }
    }
  }
  .dis-item{
    margin:15px 0;
    .distributor-content{
      margin-left:20px;
    }
    .mini-search-btn{
      margin:15px 0;
    }
  }
  .explain{
    color:#999999;
  }
</style>
