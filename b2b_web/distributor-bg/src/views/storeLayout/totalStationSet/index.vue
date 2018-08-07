<template>
  <div class="total_index">
    <header>
      <h4>购物设置</h4>
    </header>
    <div class="total_body">
      <div class="total_body_header1">页面显示设置</div>
      <el-form label-width="200px" label-position="right" ref="formData" class="total_body_block1">
        <el-form-item label="前台库存显示方式" style="margin-bottom: 0;">
          <el-radio-group v-model="formData.stockShowFlag" class="turnover-isCon">
            <el-radio :label="0">显示实际库存</el-radio>
            <el-radio :label="1">显示模糊库存
              <template>
                <span class="turnover_span1">当库存数量小于等于</span>
                <input class="turnover_input" v-model="formData.stockShowNumber" type="text" size="mini"/>
                <span class="good-bodyRules-spanOne">时为库存紧张，其余情况为库存充足，0为无货。</span>
              </template>
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <div class="total_body_header1">营销推广设置</div>
      <el-form label-width="200px" label-position="right" ref="promoteData" class="total_body_block2">
        <el-form-item label="定制商品订单是否参与活动" class="total2_formItem">
          <el-radio-group v-model="formData.customizedAttendEventFlag" class="turnover-isCon">
            <el-radio :label="1">参与</el-radio>
            <el-radio :label="0">不参与</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="MTO订单是否参与活动" class="total2_formItem">
          <el-radio-group v-model="formData.mtoAttendEventFlag" class="turnover-isCon">
            <el-radio :label="1">参与</el-radio>
            <el-radio :label="0">不参与</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="直运订单是否参与活动" class="total2_formItem">
          <el-radio-group v-model="formData.directTransportationEventFlag" class="turnover-isCon">
            <el-radio :label="1">参与</el-radio>
            <el-radio :label="0">不参与</el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>

      <div class="total_body_header1">新品时间设置</div>
      <el-form label-width="200px" label-position="right" ref="formData" class="total_body_block1">
        <el-form-item label="新品时间范围设置" style="margin-bottom: 0;">
          <template>
            <div class="turnover-isCon">
              <span class="turnover_span2">商品首次上架时间起</span>
              <input class="turnover_input" v-model="formData.newproductTime" type="text" size="mini"/>
              <span class="good-bodyRules-spanOne">天内，属于新品，显示在购物页面【新品】栏目里。</span>
            </div>
          </template>
        </el-form-item>
      </el-form>

      <!------订单结算提示------>
      <div class="total_body_header1">订单结算提示</div>
      <el-form label-width="200px" label-position="right" ref="formData" class="total_body_block1">
        <el-form-item label="订单结算提示" class="total2_formItem">
          <el-checkbox v-model="formData.stiffUseHint">直发分销商</el-checkbox>
          <el-checkbox v-model="formData.noStiffUseHint">非直发分销商</el-checkbox>
        </el-form-item>
        <el-form-item label="提示内容" class="total2_formItem">
          <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="formData.hint">
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
        stockShowFlag: '', //..前台库存显示方式
        stockShowNumber: '', //..库存数量
        onWayAttendEventFlag: 0, //..在途订单是否参与活动
        customizedAttendEventFlag: 0, //..定制商品订单是否参与活动
        mtoAttendEventFlag: 0, //..MTO订单是否参与活动
        directTransportationEventFlag: 0, //..直运订单是否参与活动
        stiffUseHint: false, // 直发分销商
        noStiffUseHint: false, // 非直发分销商
        hint: '', // 提示内容

      }
    }
  },
  methods : {
    getConfig() { //..配置详情
      this.$http.getShopSetting(this).then(res => {  
        if(res.success) {
          this.formData = res.data
          this.formData.stiffUseHint = res.data.stiffUseHint===1?true:false
          this.formData.noStiffUseHint = res.data.noStiffUseHint===1?true:false
        }
      })
    },
    handleSave() { //..保存操作
      // 订单结算提示
      this.formData.stiffUseHint = this.formData.stiffUseHint ? 1 : 0
      this.formData.noStiffUseHint = this.formData.noStiffUseHint ? 1 : 0
      this.$http.editShopSetting(this, this.formData).then(res => {  
        if(res.success) {
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 3 * 1000,
          })
          this.getConfig()
				}
      })
    }
  },
  components: { selectDistributor, selectDistributor1 }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .total_index {
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
      .total_body_block2 {
        margin: 0 auto;
        width: 97%;
        height: 30%;
        border-radius: 5px;
        border: 1px solid #dcdcdc;
        background-color: #f5f7fa;
        .total2_formItem {
          margin-bottom: 0;
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
</style>
