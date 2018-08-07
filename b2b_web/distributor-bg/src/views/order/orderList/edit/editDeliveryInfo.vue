<template>
  <main class="edit-deliverty" v-show="!successState">
    <header>
        <h4>编辑收货信息</h4>
        <el-button class="mini-add-btn btn-home" icon="el-icon-d-arrow-left" @click="onRevert">
          返回订单详情
        </el-button>
      </header>
    <el-form :model="formData" label-width="180px" style="width:600px;margin-top:50px">
      <el-form-item label="从已有收货地址中选择">
        <el-select v-model="formData.address" placeholder="请选择" style="width:120px" size="mini">
          <el-option v-for="address in addressesOfUser" :key="address.id" :label="address.address" :value="address.address">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="收货人">
        <el-input v-model="formData.userName"></el-input>
      </el-form-item>

      <el-form-item label="所在地区">
        <el-select v-model="formData.provinceId" placeholder="请选择" style="width:120px" size="mini">
          <el-option v-for="province in this.RegionForChose.provinces" :key="province.id" :label="province.regionName" :value="province.id">
          </el-option>
        </el-select>
        <el-select v-model="formData.cityId" placeholder="请选择" style="width:120px" size="mini">
          <el-option v-for="city in this.RegionForChose.citys" :key="city.id" :label="city.regionName" :value="city.id">
          </el-option>
        </el-select>
        <el-select v-model="formData.districtId" placeholder="请选择" style="width:120px" size="mini">
          <el-option v-for="district in this.RegionForChose.districts" :key="district.id" :label="district.regionName" :value="district.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="地址">
        <el-input v-model="formData.address" /> 
      </el-form-item>

      <el-form-item label="邮编">
        <el-input v-model="formData.zipCode" />
      </el-form-item>

      <el-form-item label="电话">
        <el-input v-model="formData.phone" />
      </el-form-item>

      <el-form-item label="手机">
        <el-input v-model="formData.mobile" />
      </el-form-item>
      
      <el-form-item label="最佳送货时间">
        <el-input v-model="formData.deliveryTime" />
      </el-form-item>
    </el-form>
    <div class="btns">
      <el-button class="mini-search-btn" @click="onConfirm" >确认</el-button>
    </div>
  </main>
  <!-- <success
  :expire="5"
  :goHome="onRevert"
  @alreadyGoHome="_ => {this.successState = false;}"
  v-if="successState">
  </success> -->
</template>

<script>
import success from '@/views/order/components/oprationSuccess'
import {confirmCreator} from '@/views/order/orderUtils'
import {getRegions} from '@/views/order/orderData'
import {getDeliveryAddress,editOrder} from '@/views/order/orderData'

export default {
  name: 'editDelivery',
  props: {
    chosenData: Object, // 此数据在该此组件内只读
  },
  components: {success} ,
  created(){
    const currOrderId = this.$route.query.orderId;
    if(this.orderId != currOrderId){
      this.$store.dispatch('updateOrderId', currOrderId)
      .then(_ => {
        this.formData = JSON.parse(JSON.stringify(this.orderDetail.delivery));
      })
    }else{
      this.formData = JSON.parse(JSON.stringify(this.orderDetail.delivery));
    }
    
    // 请求用户名下所有地址
    this.updateAddList();
    // 请求区域信息
    getRegions(this, this.regionParams)
    .then(res => {
      this.RegionForChose.provinces = res.regions;
    })
    .catch(e => console.log(e))
  },
  computed: {
    orderId() {
      return this.$store.getters.orderId
    },
    orderDetail() {
      return this.$store.getters.orderDetail
    },
  },
  data(){
    return {
      FirstReq: true, // 生命周期加载初始数据时，cityId不会被watcher清洗
      successState: false,
      formData: {},
      addressesOfUser: [],
      regionParams: { // 用来请求region数据
        parentId: 0,
        count: 300,
        page: 1,
      },
      RegionForChose: { // 备选region
        provinces: [],
        citys: [],
        districts: [],
      },
    }
  },
  methods:{
    onRevert(){ 
      this.$router.go(-1)
    },
    
    editOrderReq(){ // 发出编辑的请求
      return editOrder(this, { // 地区菜单数据，provinces触发citys触发districts
        id: this.orderId,
        delivery: {
          provinceId: this.formData.provinceId,
          cityId: this.formData.cityId,
          districtId: this.formData.districtId,
          userName: this.formData.userName,
          mobile: this.formData.mobile,
          zipCode: this.formData.zipCode,
          address: this.formData.address,
          phone: this.formData.phone,
          deliveryTime: this.formData.deliveryTime,
        }
      });
    },
    onConfirm(){
      confirmCreator(this)('提交更改', _ => {
        return this.editOrderReq().then(res => {
          if(res.code == 0){
            this.successState = true;
          }
          return res
        }).catch(
          e => console.log(e)
        )
      })
    },
    // 获取用户的所有地址
    updateAddList(){
      getDeliveryAddress(this, {
        page: 1, 
        count: 10000,
        userId: this.$route.query.distrId
      }).then(res => {this.addressesOfUser = res.list}).catch(
        e => console.log(e)
      )
    }
  },
  watch: {
    orderDetail() {
      this.formData = JSON.parse(JSON.stringify(this.orderDetail.delivery))
    },
    'formData.provinceId': function(){
      getRegions(this, {parentId: this.formData.provinceId})
      .then(res => {
        // 更新city备选项
        this.RegionForChose.citys = res.regions; // options of el-select
        // 更新cityId,生命周期加载初始数据时不会触发
        if(res.regions[0] && !this.FirstReq){
          this.formData.cityId = res.regions[0].id;
        }
        // FirstReq = false,意味着不再是第一次调用这个watcher
        if(this.FirstReq){
          this.FirstReq = false
        }
        // 因为city变了,清空options of districts
        this.RegionForChose.districts = [];
        this.formData.districtId = null;
      })
      .catch(e => console.log(e))
    },
    'formData.cityId': function(){
      // if(this.FirstReq) return 0;
      getRegions(this, {parentId: this.formData.cityId})
      .then(res => {
        // 更新district备选项和默认选项
        this.RegionForChose.districts = res.regions; // options of el-select
        if(res.regions[0]){
          this.formData.districtId = res.regions[0].id;
        }
      })
      .catch(e => console.log(e))
    },

  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
// @function calculate-width ($col-span) {
//   @return 100% / $col-span
// }  
.edit-deliverty{
  height: 100%;
  min-width: 1050px;
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  } 
  .btn-home{
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
    font-size: 12px;
  }
  table{
    width: 100%;
    border-collapse: collapse;
    tr{
      border-bottom: 1px solid $tableColor;
      th{
        padding: 20px 0;
        width:  calculate-width(4);
      }
      // th.radio{
      //   width: 60px;
      // }
      td{
        padding: 20px 0;
        text-align: center;
      }
    }
  }
  div.btns{
    text-align: center;
    margin-top: 30px;
  }
}
</style>
