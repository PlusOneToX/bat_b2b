<template>
  <main style="background-color:#fff !important;">
    <div class="warehouse-list-wrap">
      <div>
        <header>
          <div style="margin-left:30px;">收货人信息</div>
        </header>
      </div>
    </div>
    <div class="edit-deliverty" v-show="!successState" style="background-color:#fff;">
      <el-form :model="formData" label-width="180px" style="width:600px;padding-top:30px;">
        <el-form-item label="从已有收货地址中选择">
          <el-select v-model="formData.address" placeholder="请选择" style="width:120px" size="mini">
            <el-option v-for="address in addressesOfUser" :key = "address.id" :label="address.address" :value="address.address">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="收货人">
          <el-input v-model="formData.userName" />
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
      <div class="btns" style="text-align:center;padding: 40px;">
        <el-button size="mini" @click="onConfirm" type="primary">确认</el-button>
        <el-button size="mini" @click="onRevert">取消</el-button>
      </div>
    </div>
    <success v-if="successState" :expire="5" :goHome="onRevert" @alreadyGoHome="_ => {this.successState = false;}" >
    </success>
  </main>
</template>

<script>
import {parseTime} from '@/utils/index'
// import eventBus from '@/views/order/eventBus'
import success from '@/views/order/components/oprationSuccess'
import {confirmCreator, chooseBus} from '@/views/order/orderUtils'
import {getRegions} from '@/views/order/orderData'

let eventBus = {};

import {getdistributions,getDeliveryAddress,editOrder} from '@/views/order/orderData'

export default {
  name: 'editRedact',
  created(){
    // 以祖先组件name判断用哪个eventBus
    const parentName = this.$options.parent.$options.parent.$options.name;
    eventBus = chooseBus(parentName);
    
    this.formData = JSON.parse(JSON.stringify(this.chosenData.delivery));
    // 请求用户名下所有地址
    this.updateAddList();
    // 请求区域信息
    getRegions(this, this.regionParams).then(res => {
      this.RegionForChose.provinces = res.regions;
    })
    .catch(e => console.log(e))
  },
  components: {success} ,
  data(){
    return {
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
  computed: {},
  methods:{
    
    onRevert(){ // 点击事件 x 2
      // eventBus.$emit('switchPageState', 'orderDetail');
      this.$router.go(-1)
    },
    
    editOrderReq(){ // 发出编辑的请求
      return editOrder(this, {
        // 地区菜单数据，provinces触发citys触发districts
        id: this.chosenData.id,
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
        this.editOrderReq().then(res => {
          if(res.code == 0){
            this.successState = true;
          }
        }).catch(e => console.log(e))
      })
    },
    
    updateAddList(){ // 获取用户的所有地址
      getDeliveryAddress(this, {page: 1, count: 10000, userId: this.chosenData.distributorId}).then(res => {
        this.addressesOfUser = res.list
      }).catch(e => console.log(e))
    },

  },
  props: {
    chosenData: Object, // 此数据在该此组件内只读
  },
  watch: {
    chosenData(){
      this.formData = JSON.parse(JSON.stringify(this.chosenData.delivery));
    },
    'formData.provinceId': function(){
      getRegions(this, {parentId: this.formData.provinceId}).then(res => {
        // 更新city备选项和默认选项
        this.RegionForChose.citys = res.regions;
        this.formData.cityId = res.regions[0].id;
        // 清空district
        this.RegionForChose.districts = [];
        this.formData.districtId = null;
      }).catch(e => console.log(e))
    },
    'formData.cityId': function(){
      getRegions(this, {parentId: this.formData.cityId}).then(res => {
        // 更新district备选项和默认选项
        this.RegionForChose.districts = res.regions;
        this.formData.districtId = res.regions[0].id;
      }).catch(e => console.log(e))
    },

  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  * {
    box-sizing: border-box;
    padding: 0;
    margin: 0;
  }
  .warehouse-list-wrap {
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
    }
  }
  .edit-deliverty {
    background: #fff;
    height: 100%;
  }
</style>