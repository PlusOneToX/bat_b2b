<template>
  <div class="warehouse-list-wrap">
    <div>
      <header>
        <span>查看详情</span>
      </header>
    
      <div class="box-has-border" style="margin:0;">
        <div class="form">
          <el-form label-width="180px" :model="formData">
            <el-form-item label="操作备注" >
              <el-input type="textarea" :rows="5" v-model="mytext"></el-input>
            </el-form-item>
          </el-form>
        </div>

        <div class="formSelect">
            <el-form label-width="180px" :model="formData">
                 <el-form-item label="配送方式" prop="isTaxpayers" >
                    <el-select v-model="batch" placeholder="请选择" size="small">
                      <el-option v-for="item in batchs" :key="item.id" :label="item.label" :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
            </el-form>
        </div>

        <div class="formInput">
            <el-form label-width="180px" :rules="rules" :model="formData" ref="formData">
              <el-form-item label="包裹单号" prop="odd">
                <el-input size="mini" v-model.number="formData.odd"></el-input>
              </el-form-item>
            </el-form>
        </div>

        <div class="formButton">
           <el-button type="primary" size="small" @click="sure('formData')">确认</el-button>
           <el-button type="primary" size="small" @click="cancelled()">取消</el-button>
        </div>

      </div>

    </div>


  </div>

</template>

<script>

import {getOrderDetail} from '@/views/order/orderData'
import procedure from '@/components/Procedure/index'
import { parseTime } from "@/utils/index";
// ======引用组件======
import eventBus from '@/views/order/eventBus'


export default {
  name: 'editSure',
  mounted(){

  },
  data(){
    return {
      mytext: "",
      batchs: [{
          value: '1',
          label: '顺丰快递'
        },{
          value: '2',
          label: '申通快递'
        },{
          value: '3',
          label: '圆通快递'
        },{
          value: '4',
          label: '中通快递'
        },{
          value: '5',
          label: '百世快递'
        },{
          value: '6',
          label: '韵达快递'
        },{
          value: '7',
          label: '天天快递'
        },{
          value: '8',
          label: '中国邮政'
        },{
          value: '9',
          label: 'EMS'
        },{
          value: '10',
          label: '宅急送'
        },{
          value: '11',
          label: '德邦物流'
        },{
          value: '12',
          label: '全峰快递'
        },{
          value: '13',
          label: '优速快递'
        },{
          value: '14',
          label: '汇通快递'
        },{
          value: '15',
          label: '速尔快递'
        }],
      batch: [],
      formData: {
        odd: '',
      },
      rules: {
          odd: [
            { 
              required: true, 
              message: '请输入包裹单号', 
              trigger: 'blur' 
            },
          ],
      }
    }
  },
  props: {
    
  },
  computed: {
    
  },
  methods:{
    // 确认按钮
    sure(data) {
        let query = this.$route.query
        // eventBus.$emit('redact','editsure')
        // this.$route.query.id
        this.$api .put(this, "admin/u/afterservice", {
          id : query.id,
          operateId : 1 ,
          courierNo: this.formData.odd,
          courierType : this.batch
        }).then(res => {
          if (res.code == 0) {
            this.$message({ message: "成功操作"});
            // this.$router.go(-1)
            this.$router.push({name: 'orderGoodslist'})
          }else {
            this.$message({meaage: '操作失败', type: 'wrong'})
            this.$router.go(-1)
          }
        });
        
    },
    // 取消按钮
    cancelled() {
      this.$router.go(-1)
    }
  },
  watch: {
    entryIndex(){
      this.updateMainData();
    },
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .warehouse-list-wrap{
    background-color: #fff;
    height: 100%;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    .box-has-border{
        overflow: hidden;
        margin-top: 15px;
        background-color: #fff;
        
        .form {
          width: 50%;
          padding: 20px 0 0 0;
        }
        .formSelect{
            width: 50%;
            padding: 20px 0 0 0; 
        }
        .formInput{
            min-width: 350px;
            -moz-min-width:350px;
            -webkit-min-width: 350px;
            width: 25%;
            -moz-width: 25%;
            -webkit-width: 25%;
        }
        .formButton{
          text-align: center;
        }
      }
  }
</style>

