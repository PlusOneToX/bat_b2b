<template>
  <div class="factory-set">
    <header>
      <h4>工厂设置</h4>
    </header>

    <div class="total_body">
      <!------工厂推送设置------>
      <div class="total_body_header1">工厂推送设置</div>
      <el-form label-width="200px" label-position="right" ref="formData" class="total_body_block1">
        <el-form-item label="定制订单生产推送时效" style="margin-bottom: 0;">
          <template>
            <div class="turnover-isCon">
              <span class="turnover_span2">默认用户下单后</span>
              <!-- <input class="turnover_input" v-model="invalidDefault.invalid" type="text" size="mini"/> -->
              <el-select class="choose" v-model="invalidDefault.invalid" size="mini" clearable style="width:120px">
                <el-option v-for="item in invalids" :key="item.value" :label="item.label" :value="item.value"> </el-option>
              </el-select>
              <span class="good-bodyRules-spanOne">分下推至工厂进行生产</span>
            </div>
          </template>
        </el-form-item>
        <!-- 特殊时效设置 -->
        <el-form-item label="特殊时效设置" style="margin-bottom: 0;">
          <template>
            <div class="turnover-isCon">
              <el-button class="mini-search-btn" icon="el-icon-plus" @click="distributorShow1=true">添加设置</el-button>
            </div>
            <div class="turnover-isCon-s">
              <el-table :data="orderInvalid" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 70%" max-height="200">
                <el-table-column align="center" label="分销商渠道" prop="name">
                </el-table-column>
                <el-table-column align="center" label="生产时效" prop="invalid">
                  <template slot-scope="scope">
                    <el-select class="choose" v-model="scope.row.invalid" size="mini" clearable style="width:120px">
                      <el-option v-for="item in orderInvalids" :key="item.value" :label="item.label" :value="item.value"> </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="操作" width="250">
                  <template slot-scope="scope">
                    <el-button class="mini-delete-btn" @click="handleDeleteDis(scope.$index, scope.row.id, scope.row.isAdd, scope.row.orderSource)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-form-item>
        <!-- 工厂生产推送时效 -->
        <el-form-item label="工厂生产推送时效" style="margin-bottom: 0;">
          <template>
            <div class="turnover-isCon">
              <el-button class="mini-search-btn" icon="el-icon-plus" @click="showFactoryDialog">添加设置</el-button>
            </div>
            <div class="turnover-isCon-s">
              <el-table :data="delayPushes" border header-row-class-name="header-row" class="tableCenter goods-table" style="width: 70%" max-height="200">
                <el-table-column align="center" label="工厂" prop="disName">
                  <template slot-scope="scope">
                    <div class="name">{{getFactoryValue(scope.row.factory)}}</div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="推送时间" prop="invalid">
                  <template slot-scope="scope">
                    <div class="turnover-isCon">{{timeFormatter(scope.row.pushTime)}}</div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="操作" width="250">
                  <template slot-scope="scope">
                    <el-button class="mini-delete-btn" @click="handleDeleteFac(scope.$index, scope.row.id)">删除</el-button>
                    <el-button class="mini-search-btn" @click="handleSlectFac(scope.row.id)" v-if="scope.row.id">查看</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-form-item>
      </el-form>
      <div class="foot_btn">
        <el-button class="mini-search-btn" @click="handleSave()"> 保存 </el-button>
      </div>
    </div>
    <!-- 添加工厂生产推送时效dialog -->
    <el-dialog class="factoryDialog" width="60%" :modal-append-to-body="false" :visible="showAddFactorytime" :before-close="closeAddFactorytime">
      <div class="content">
        <div class="title" style="margin:0 0 20px 40px">添加工厂推送</div>
        <div class="con-list">
          <el-form label-width="150px" :model="formData2" :rules="rules" ref="formData2">
            <el-form-item label="选择工厂" class="form-item" prop="factoryId" placeholder="请先选择工厂">
              <el-select v-model="formData2.factoryId" @change="factoryChange">
                <el-option v-for="item in factoryList" :key="item.id" :value="item.id" :label="item.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="推送时间" class="form-item" prop="time" placeholder="请先选择推送时间">
              <el-time-picker
                v-model="formData2.time"
                format='HH:mm:ss'
                value-format="HH:mm:ss"
                placeholder="任意时间点">
              </el-time-picker>
            </el-form-item>
            <el-form-item label="适用范围" class="form-item" placeholder="适用范围">
              <el-radio-group v-model="formData2.distributorType">
                <div class="dis-item">
                  <el-radio :label="0">全部分销商可用</el-radio>
                </div>
                <div class="dis-item">
                  <el-radio :label="1">指定分销商可用</el-radio>
                  <div class="distributor-content" v-if="formData2.distributorType==1">
                    <el-button icon="el-icon-plus" class="mini-search-btn" @click="distributorShow=true">
                      添加分销商
                    </el-button>
                    <el-table class="goods-table" :data="formData2.distributorData" border header-row-class-name="header-row" max-height="600">
                      <el-table-column align="center" label="分销商用户名" width="150" prop="name"></el-table-column>
                      <el-table-column align="center" label="公司名" width="300" prop="companyName"></el-table-column>
                      <el-table-column  align="center" label="操作" width="80">
                        <template slot-scope="scope">
                          <el-button style="margin-top:0px;margin-bottom:0px;" class="mini-delete-btn" @click="handleDeleteDistributor(scope.$index, 1)">
                            删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
                <div class="dis-item">
                  <el-radio :label="2">指定分销商不可用</el-radio>
                  <div class="distributor-content" v-if="formData2.distributorType==2">
                    <el-button icon="el-icon-plus" class="mini-search-btn" @click="distributorShow=true">
                      添加分销商
                    </el-button>
                    <el-table class="goods-table" :data="formData2.distributorDataUn" border header-row-class-name="header-row" max-height="600">
                      <el-table-column align="center" label="分销商用户名" width="150" prop="name"></el-table-column>
                      <el-table-column align="center" label="公司名" width="300" prop="companyName"></el-table-column>
                      <el-table-column  align="center" label="操作" width="80">
                        <template slot-scope="scope">
                          <el-button style="margin-top:0px;margin-bottom:0px;" class="mini-delete-btn" @click="handleDeleteDistributor(scope.$index, 2)">
                            删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <el-button class="mini-search-btn" style="margin-left:47%;" @click="handleAddFactory">保存</el-button>
      <el-button size="mini" @click="closeAddFactorytime">返回</el-button>
    </el-dialog>
    <!-- 工厂生产推送时效/适用分为/指定分销商可见 添加分销商dialog -->
    <el-dialog :modal-append-to-body="false" :visible="distributorShow" :before-close="disCancel" width="80%" >
      <select-distributor :distributorData="formData2.distributorType==1?formData2.distributorData:formData2.distributorDataUn" ref="selectDistributor" @cancel="cancel" @submit="disSubmit"> </select-distributor>
    </el-dialog>
    <!-- 特殊时效设置 添加分销商dialog -->
    <el-dialog :modal-append-to-body="false" :visible="distributorShow1" :before-close="disCancel1" width="80%" >
      <select-distributor :distributorData="formData3.distributorType==1?formData3.distributorData:formData3.distributorDataUn" ref="selectDistributor1" @cancel="cancel1" @submit="disSubmit1"> </select-distributor>
    </el-dialog>
  </div>
</template>

<script>
import selectDistributor from '@/views/goods/components/selectDistributor'
import selectDistributor1 from '@/views/goods/components/selectDistributor'
import { monthDay, timeHours } from "@/utils/timeFormat"
export default {
  created() {
    this.getConfig()
  },
  data() {
    return {
      formData: {
        settingOrderList: []
      },
      invalidDefault: {}, // 定制订单生产推送时效
      orderInvalid: [],  // 特殊时效设置列表
      delayPushes: [], // 工厂生产推送时效列表
      disList: [,  // 分销商渠道列表
      distributors:[], // 所有分销商列表数据
      invalids: [
        {
          value: 14,
          label: '10分钟'
        },{
          value: 15,
          label: '20分钟'
        },{
          value: 16,
          label: '30分钟'
        },{
          value: 17,
          label: '1小时'
        },{
          value: 18,
          label: '2小时'
        }
      ],
      orderInvalids: [
        {
          value: 10,
          label: '10分钟'
        },{
          value: 20,
          label: '20分钟'
        },{
          value: 30,
          label: '30分钟'
        },{
          value: 60,
          label: '1小时'
        },{
          value: 120,
          label: '2小时'
        }
      ], // 
      formData2: {
        factoryId: '',
        factoryValue: '',
        time: '',
        distributorType: 0,
        distributorIds: [],
        distributorData: [], // 可用
        distributorDataUn: [] // 不可用
      },
      formData3: {
        factoryId: '',
        factoryValue: '',
        time: '',
        distributorType: 0,
        distributorIds: [],
        distributorData: [], // 可用
        distributorDataUn: [] // 不可用
      },
      rules: {
        factoryId: [{ required: true, message: '请选择工厂', trigger: 'change' }],
        time: [{ required: true, message: '请输入时间', trigger: 'blur' }]
      },
      factoryList: [
        {
          id: 1,
          name: '联辉王',
          value: 'LHW'
        },
        {
          id: 2,
          name: '云创',
          value: 'YC'
        },
        {
          id: 3,
          name: '麦客',
          value: 'MK'
        },
        {
          id: 4,
          name: "多鸿欧丽科",
          value: "DH_OLK",
        },

      ], // 工厂列表
      fId: '',
      distributorShow: false, // 工厂生产推送时效/适用分为/指定分销商可见 分销商
      distributorShow1: false, // 特殊时效设置 分销商
      isAdd: false,
      showAddFactorytime: false // 是否展示工厂生产推送时效展示
    }
  },
  methods : {
    getConfig() { //..配置详情
      this.showAddFactorytime = false
      this.$http.getFactorySetting(this).then(res => {  
        if(res.success) {
          if(res.data.orderInvalid !== undefined && res.data.orderInvalid !== null && res.data.orderInvalid.length >0){
            this.orderInvalid = res.data.orderInvalid
            let data = this.formData3.distributorType==1?this.formData3.distributorData:this.formData3.distributorDataUn
            this.orderInvalid.map((val)=>{
              if(data.filter(val1=>val1.id == val.orderSource).length==0){
                data.push({id:val.orderSource})  
              }
            })
          }
          if(res.data.delayPushes !== undefined && res.data.delayPushes !== null){
            this.delayPushes = res.data.delayPushes
          }
          this.formData = res.data
          this.invalidDefault = res.data.invalidDefault
        }
      })
    },
    handleSave() { //..保存操作
      if (this.orderInvalid.length > 0) {
        try{
          this.orderInvalid.forEach(item => {
            if (item.name === '' || item.orderSource === '') {
              this.$message.error('请选择分销商渠道')
              throw new Error()
            }
          })
        }catch (e) {
          return
        }
      }
      this.formData.orderInvalid = this.orderInvalid
      this.formData.invalidDefault = this.invalidDefault
      this.$http.editFactorySetting(this, this.formData).then(res => {  
        if(res.success) {
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 3 * 1000,
          })
          this.getConfig()
				}
      })
    },
    // 添加设置
    addParams(){
      this.orderInvalid.push({
        id: this.orderInvalid.length + 1,
        name:'',
        invalid: 0,
        orderSource: '',
        isAdd: true
      })
    },
    // 选择分销商渠道
    changeDis(index){
      for(let i = 0;i<this.disList.length;i++){
        if(this.disList[i].id === this.orderInvalid[index].orderSource){
          this.orderInvalid[index].name = this.disList[i].name
          break
        }
      }
    },
    // 选择工厂
    factoryChange (val) {
      this.factoryList.forEach(item => {
        if (val === item.id) {
          this.formData2.factoryValue = item.value
        }
      })
    },
    // 提交工厂推送
    handleAddFactory () {
      if (this.Validate()) {
        //工厂
        this.formData2.distributorIds = ''
        if (this.formData2.distributorType===1) {
          // 可用
          this.formData2.distributorData.forEach((item, index) => {
            if (index < this.formData2.distributorData.length -1) {
              this.formData2.distributorIds += item.id + ','
            } else {
              this.formData2.distributorIds += item.id
            }
          })
          this.formData2.distributorDataUn = []
        } else if (this.formData2.distributorType===2) {
          // 不可用
          this.formData2.distributorDataUn.forEach((item, index) => {
            if (index < this.formData2.distributorDataUn.length -1) {
              this.formData2.distributorIds += item.id + ','
            } else {
              this.formData2.distributorIds += item.id
            }
          })
          this.formData2.distributorData = []
        } else {
          this.formData2.distributorIds = ''
          this.formData2.distributorData = []
          this.formData2.distributorDataUn = []
        }
        let today = monthDay(new Date())
        if (this.fId) {
           // 修改
          let time = this.formData2.time.indexOf(' ')>-1 ? this.formData2.time :  today + this.formData2.time
          this.delayPushes.map((item, index) => {
            if (item.id === this.fId) {
              // this.delayPushes.splice(1, index)
              item.factory = this.formData2.factoryValue
              item.pushTime = time,
              item.useRange = this.formData2.distributorIds,
              item.type = this.formData2.distributorType
            }
          })
          this.showAddFactorytime = false
        } else {
          // 添加工厂
          this.delayPushes.push({
            factory: this.formData2.factoryValue,
            pushTime: today + this.formData2.time,
            useRange: this.formData2.distributorIds,
            type: this.formData2.distributorType
          })
           this.showAddFactorytime = false
        }
      }
    },
    // 根据工厂名获取id
    getFactoryIdByValue (value) {
      let fId = ''
      this.factoryList.forEach(item => {
        if (item.value === value) {
          fId = item.id
        }
      })
      return fId
    },
    // 验证工厂推送
    Validate () {
      if (this.formData2.factoryId === '' || this.formData2.factoryId === null || this.formData2.factoryId === undefined) {
        this.$message.warning('请选择工厂')
        return false
      } else if (this.formData2.time === '' || this.formData2.time === null || this.formData2.time === undefined) {
        this.$message.warning('请选择推送时间')
        return false
      } else {
        if (this.formData2.distributorType === 1 && this.formData2.distributorData.length<=0) {
          // 指定分销商可用
          this.$message.warning('请指定可用分销商')
          return false
        }
        if (this.formData2.distributorType === 2 && this.formData2.distributorDataUn.length<=0) {
          // 指定分销商可用
          this.$message.warning('请指定不可用分销商')
          return false
        }
        return true
      }
    },
    timeFormatter(createTime) {
      return timeHours(createTime);
    },
    getFactoryValue (value) {
      let fValue = ''
      this.factoryList.forEach(item => {
        if (item.value === value) {
          fValue = item.name
          return
        }
      })
      return fValue
    },
    // 查看工厂生产时效
    handleSlectFac (id) {
      this.showAddFactorytime = true
      this.fId = id
      this.delayPushes.forEach(item => {
        if (item.id === id) {
          this.formData2.factoryId = this.getFactoryIdByValue(item.factory)
          this.formData2.factoryValue = item.factory
          this.formData2.time = item.pushTime
          this.formData2.distributorType = item.type
          this.formData2.distributorIds = item.useRange
        }
      })
      if(this.formData2.distributorIds !== undefined && this.formData2.distributorIds !== null && this.formData2.distributorIds !== ''){
        if(this.distributors === undefined || this.distributors.length === 0){
          this.$http.getDistributorPoList(this, {page:1,size:10000}).then(res =>{  
            this.distributors = res.data.list
            this.initDistributor()
          })
        }else{
          this.initDistributor()
        }
      }
    },
    initDistributor(){
      let idArr = this.formData2.distributorIds.split(',')
      let disData = []
      idArr.forEach(item => {
        this.distributors.forEach(val => {
          if(val.id == Number(item)) {
            disData.push(val)
          }
        })
      })
      if (this.formData2.distributorType===1) {
        // 可用
        this.formData2.distributorData =this.setArr(disData)
        this.formData2.distributorDataUn = []
      } else if (this.formData2.distributorType===2) {
        // 不可用
        this.formData2.distributorDataUn =this.setArr(disData)
        this.formData2.distributorData = []
      }
    },
    // 删除工厂生产推送时效
    handleDeleteFac (index, id) {
      this.delayPushes.splice(index, 1)
    },
    // 删除特殊时效
    handleDeleteDis (index, id, val, orderSource) {
      this.orderInvalid.splice(index,1)
    },
    setArr(arr) { // 去重
      const obj = {}
      // eslint-disable-next-line prefer-const
      let temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id) // 不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i])
          obj[arr[i].id + type] = true // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp
    },
    // 删除分销商
    handleDeleteDistributor(index, type) {
      type===1?this.formData2.distributorData.splice(index, 1):this.formData2.distributorDataUn.splice(index, 1)
    },
    // 添加工厂时效弹框
    showFactoryDialog () {
      this.showAddFactorytime = true
      this.fId = ''
      this.formData2.factoryId = ''
      this.formData2.factoryValue = ''
      this.formData2.time = ''
      this.formData2.distributorType = 0
      this.formData2.distributorIds = ''
      this.formData2.distributorData = []
      this.formData2.distributorDataUn = []
    },
    // 关闭添加工厂生产推送时效弹框
    closeAddFactorytime () {
      this.showAddFactorytime = false
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel()
    },
    disCancel1() {
      this.$refs.selectDistributor1.handleCancel()
    },
    cancel(){
      this.distributorShow = false;
    },
    disSubmit(msg) {
      this.formData2.distributorType===1 ? this.formData2.distributorData = msg : this.formData2.distributorDataUn = msg
      this.distributorShow = false;
    },
    cancel1(){
      this.distributorShow1 = false;
    },
    disSubmit1(msg) {
      this.formData3.distributorType===1 ? this.formData3.distributorData = msg : this.formData3.distributorDataUn = msg
      //去除 新增重复
      msg.map(val=>{
        if(this.orderInvalid.filter(val1=>val1.orderSource==val.id).length==0){
          this.orderInvalid.push({
            id: this.orderInvalid.length + 1,
            name: val.name,
            invalid: 0,
            orderSource: val.id,
            isAdd: true
          })
        }
      })  
      this.orderInvalid.map((val,index)=>{
        if(!msg.filter(val1=>val1.id==val.orderSource).length!=0){
          this.handleDeleteDis(index,val.id,false,val.orderSource)
          }
      })
      this.distributorShow1 = false;
    },
  },
  components: { selectDistributor, selectDistributor1 }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .factory-set {
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