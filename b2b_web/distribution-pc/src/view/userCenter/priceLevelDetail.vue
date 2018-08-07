<template>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState" ref="header"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div  class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="order-list-wrap">
            <div class="user-right-title2">
              <h6 >价格等级详情</h6>
            </div>

            <div class="priceLevel-box">
               <div class="priceLevel-box-title">基本信息</div>
               <div class="priceLevel-box-content">
                   <div class="priceLevel-box-line">
                     <span>价格等级名称：</span>
                     <el-input v-model="formInfo.name" placeholder="请输入内容" class="priceLevel-box-input"></el-input>
                   </div>
                   <div class="priceLevel-box-line">
                     <span>特殊公式：</span>
                     <el-radio v-model="formInfo.specialFlag" label="0">不启用</el-radio>
                     <el-radio v-model="formInfo.specialFlag" label="1">启用</el-radio>
                   </div>
                   <div class="priceLevel-box-line">
                     <span>状态：</span>
                     <el-radio v-model="formInfo.openFlag" label="0">不启用</el-radio>
                     <el-radio v-model="formInfo.openFlag" label="1">启用</el-radio>
                   </div>
               </div>
            </div>

            <div class="priceLevel-box">
               <div class="priceLevel-box-title">通用公式</div>
               <div class="priceLevel-box-content">
                   <div class="priceLevel-box-line">
                     <div>等级价格=分销商基础价</div>
                     <el-select v-model="formInfo.arithmeticType" placeholder="请选择" class="priceLevel-box-select">
                        <el-option key="1" label="*" value="1"> </el-option>
                        <el-option key="2" label="+" value="2"> </el-option>
                      </el-select>
                      <el-input v-model="formInfo.arithmeticNum" placeholder="请输入数值" class="priceLevel-box-input2"></el-input>
                   </div>      
               </div>
            </div>

            <div class="priceLevel-box">
               <div class="priceLevel-box-title"><span>特殊公式</span><span class="priceLevel-box-labelBtn" @click="addSpecialsFun"><i class="el-icon-plus"></i>新增公式</span></div>
               <div class="priceLevel-box-content2" v-for="(item,index) in formInfo.scalePriceSpecials" :key="index">
                  <div class="priceLevel-box-label">
                    <span>等级公式{{index+1}}：</span>
                    <div>
                      <span @click="editSpecialsFun(item)">编辑</span>
                      <span @click="deleteScalepriceSpecialFun(item.id)">删除</span>
                    </div>
                    </div>  
                  <div class="priceLevel-box-labelBox">
                     <div class="priceLevel-box-line">
                        <span>等级价格 = 分销基础价</span>
                        <span v-if="item.arithmeticType==1">*</span>
                        <span v-if="item.arithmeticType==2">+</span>
                        <span>{{item.arithmeticNum}}</span>
                        <!-- <el-select v-model="item.arithmeticType" placeholder="请选择" class="priceLevel-box-select">
                          <el-option key="1" label="*" value="1"> </el-option>
                          <el-option key="2" label="+" value="2"> </el-option>
                        </el-select> -->
                        <!-- <el-input v-model="item.arithmeticNum" placeholder="请输入数值" class="priceLevel-box-input2"></el-input> -->
                     </div>
                  </div> 
                  <div class="priceLevel-box-labelBox2">
                    <div class="priceLevel-box-labelBoxTitle">选择的品牌</div>
                    <div class="priceLevel-box-labelBoxCheckBox">
                        <el-checkbox-group v-model="item.brandSelect"  >
                          <el-checkbox :label="items.id" v-for="items in brandListData" :key="items.id"  style="margin-top:10px" disabled>{{items.name}}</el-checkbox>
                        </el-checkbox-group> 
                    </div>
                  </div> 
               </div>
            </div>

            <div class="priceLevel-btn">
              <span class="priceLevel-btn-save" @click="scalepriceEdit">保存</span>
              <span class="priceLevel-btn-back" @click="toBack">返回</span>
            </div>


          </div> 
        </div>  

      </div>


        <!--设置等级价--->
        <el-dialog class="alls" :title="spacialsTitle" :visible.sync="levelDialogVisible">
          <div class="levelDialog-box" >
             
              <div class="levelDialog-box-line">
                  <span class="levelDialog-box-span">价格设置：</span>
                  <div>
                      <span>等级价格 = 成本价</span>
                      <el-select v-model="specialInfo.arithmeticType" placeholder="请选择" class="levelDialog-box-select">
                        <el-option key="1" label="*" value="1"> </el-option>
                        <el-option key="2" label="+" value="2"> </el-option>
                      </el-select>
                      <el-input v-model="specialInfo.arithmeticNum" placeholder="请输入数值" class="levelDialog-box-input2"></el-input>
                  </div>
              </div>
              <div class="levelDialog-box-line">选择品牌（必填）</div>
              <div class="levelDialog-box-line">
                  <div class="priceLevel-box-labelBoxCheckBox">
                        <el-checkbox-group v-model="brandSelect"  >
                          <el-checkbox :label="items.id" v-for="items in brandListData" :key="items.id"  style="margin-bottom:10px">{{items.name}}</el-checkbox>
                        </el-checkbox-group> 
                    </div>
              </div>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="SpecialsEditFun">保存</el-button>
            <el-button @click="levelDialogVisible = false">取消</el-button>
          </div>
        </el-dialog>
    </div>
   
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { formatDate, dateToNum, } from "@/assets/js/common.js";
import onceAgain from "@/components/onceAgain.vue";
import GD from "@/assets/js/globalData";
import loading from "@/components/loading.vue";
import countDown from "@/components/countDown.vue";
import {scaleprice,brandList,scalepriceSpecial} from '@/apiService/api'
export default {
  components: { Header, Left, onceAgain, loading, countDown },
  data() {
    return {
      userState:2,
       formInfo:{
          distributorId:'',
          name:'',
          openFlag:'1',  //状态, 1启用,0停用
          arithmeticType:'',  //1 乘 2 加 3 除 4 减
          arithmeticNum:'',  //参加运算的数值
          specialFlag:'1', //是否启用特殊公式, 1是,0否
          scalePriceSpecials:[],  //特殊价格
          id:'',
       },
       checkList:[],
       brandListData:[],  //品牌列表
       levelDialogVisible:false,
       brandSelect:[],
       specialInfo:{
          arithmeticNum: '',
          arithmeticType: '',
          brandCategorys:[],
          nextScalePriceId:'',  //分销商价格等级id
       },
       spacialsTitle:'新增特殊公式',
       spacialsMethods:'',  
      
      
    };
  },
 
  created() {
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getDataDetails();
    this.brandListFun();

  },
  methods: {
    // 获取价格详情
    getDataDetails(){
      let id=this.$route.query.id;
      let that=this;
      scaleprice('GET',{id:id}).then(res=>{
        if(res.success){
          let list=res.data;				  
          that.formInfo=res.data;
          that.formInfo.openFlag=that.formInfo.openFlag.toString();
          that.formInfo.specialFlag=that.formInfo.specialFlag.toString();	 
          this.formInfo.arithmeticType=this.formInfo.arithmeticType.toString();
          if(res.data.scalePriceSpecials.length==0){
              this.formInfo.scalePriceSpecials=[
                {
                  arithmeticType:'',
                  arithmeticNum:'',
                  brandCategorys:[],
                }
              ];
          }else{
              this.formInfo.scalePriceSpecials=res.data.scalePriceSpecials;  //特殊公式
              console.log('特殊公式:',res.data.scalePriceSpecials)
              this.formInfo.scalePriceSpecials.forEach(item=>{
                  item.arithmeticType=item.arithmeticType.toString();
                  let brandSelect=[];
                  if(item.brandCategorys&&item.brandCategorys.length>0){
                    item.brandCategorys.forEach(items=>{
                        brandSelect.push(items.brandId)
                    })
                  }
                  this.$set(item,"brandSelect",brandSelect)
                  
              })
          }	
          
        }else{
          this.$message(res.errMessage);	  
        }
      })
    },
      

    // 根据分销商id获取品牌可视
    brandListFun(){
       brandList({distributorId:localStorage.getItem('userId')}).then(res=>{
          this.brandListData=res.data;
          console.log('品牌列表：',this.brandListData)
       })
    },

    // 编辑价格
    scalepriceEdit(){
        this.formInfo.id=this.$route.query.id;
        let info=this.formInfo;
				if(info.name==''){
          this.$message('请输入等级名称');
					return;
				}
				if(info.arithmeticType!=''&&info.arithmeticNum==''){
          this.$message('请输入数值');
					return;
				}
       scaleprice('PUT',this.formInfo).then(res=>{
          if(res.success){
             this.$message('编辑成功');
             this.$router.push({name:'priceLevel'})
          }else{
             this.$message(res.errMessage);
          }
       })
    },

    // 返回
    toBack(){
      this.$router.push({name:'priceLevel'})
    },

    // 新增特殊公式
    addSpecialsFun(){
      this.levelDialogVisible=true;
      this.spacialsMethods='POST';
      this.spacialsTitle='新增特殊公式';
      this.brandSelect=[];
      this.specialInfo={
          arithmeticNum: '',
          arithmeticType: '',
          nextScalePriceId:'',
          brandCategorys:[],
       }
    },
    // 编辑打开弹框
    editSpecialsFun(item){
       this.levelDialogVisible=true;
       this.spacialsMethods='PUT';
       this.spacialsTitle='编辑特殊公式';
       this.brandSelect=[];
       this.specialInfo=item;
       this.specialInfo.arithmeticType=this.specialInfo.arithmeticType.toString();
       item.brandCategorys.forEach(items=>{
          this.brandSelect.push(items.brandId);
       })
       
    },

    // 特殊价格保存
    SpecialsEditFun(){
      let that=this;
      let info=this.specialInfo;
      if(info.arithmeticType==''){
        this.$message('运算符必填');
        return;
      }
      if(info.arithmeticType!=''&&info.arithmeticNum==''){
        this.$message('数值必填');
        return;
      }
      if(this.brandSelect.length==0){
        this.$message('必须选择至少一个品牌');
        return;
      }
      this.specialInfo.nextScalePriceId=this.$route.query.id;
      for(let i=0;i<this.brandSelect.length;i++){
         this.specialInfo.brandCategorys.push({brandId:this.brandSelect[i]})
      }
     
      scalepriceSpecial(this.spacialsMethods,that.specialInfo).then(res=>{
          if(res.success){
              this.$message((this.spacialsMethods=='POST')?'新增特殊公式成功！':'编辑特殊公式成功！');
              this.levelDialogVisible=false; 
              this.getDataDetails();
          }
      })
    },

    // 删除--特殊公式
			deleteScalepriceSpecialFun(id){
				this.$confirm('确定删除吗?', '删除提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          scalepriceSpecial('DELETE',{id:id}).then(res=>{
            if(res.success){
              this.$message('删除成功');
              this.getDataDetails();
            }else{
              this.$message(res.errMessage);
            }  
				  })
        })
				
			},
    

  
  },
  
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .user-right-title2 {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
   
    display: flex;
    align-items: center;
    justify-content: space-between;
    h6{
      font-size: 20px;
    }
  }
  .order-list-wrap {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .query {
    margin-bottom: 25px;
    .search {
      .item {
        input[type="text"] {
          padding-left: 10px;
          width: 140px;
          height: 38px;
          font-size: 12px;
          box-sizing: border-box;
          border: 1px solid @bdLighterColor;
          border-radius: 4px;
        }
        input[type="button"] {
          padding: 0 12px;
          height: 38px;
          line-height: 38px;
          box-sizing: border-box;
          border-radius: 4px;
        }
      }
      .select {
        /deep/ .el-input__inner {
          width: 180px;
        }
      }
    }
    .search-info {
      .items {
        .common-input {
          input {
            padding-left: 10px;
            width: 140px;
            height: 38px;
            font-size: 12px;
            box-sizing: border-box;
            border: 1px solid @bdLighterColor;
            border-radius: 4px;
          }
          &.addr-input {
            input {
              width: 180px;
            }
          }
        }
        &.date-items {
          line-height: 40px;
        }
      }
      /deep/ .el-date-editor.el-input,
      .el-date-editor.el-input__inner {
        width: 200px;
      }
    }
    /deep/ .el-input__inner {
      height: 38px;
      font-size: 12px;
      border: 1px solid @bdLighterColor;
      border-radius: 4px;
    }
  }
  button {
    padding: 0 16px;
    height: 40px;
    line-height: 40px;
    border: none;
  }
  .search-btn {
    background-color: @blue;
    &:hover,
    &:active,
    &:focus {
      color: @white;
      opacity: 0.6;
    }
  }
  .query-detail {
    .nav {
      margin-bottom: 15px;
      width: 100%;
      height: 40px;
      line-height: 40px;
      ul {
        overflow: hidden;
        background-color: @bdLightColor;
        border-radius: 4px;
        &.navEn {
          li {
            width: auto;
            padding: 0 12px;
            font-size: 13px;
          }
        }
        li {
          float: left;
          width: 93px;
          cursor: pointer;
          font-size: 14px;
          color: @lighterBlack;
          text-align: center;
          &:hover,
          &.current {
            color: @white;
            background-color: @blue;
          }
        }
      }
    }
    .export-btn {
      padding-top: 24px;
      color: @blue;
      line-height: 1;
      cursor: pointer;
      .iconfont {
        margin-right: 2px;
        color: @lighterGray;
      }
      &:hover {
        opacity: 0.6;
      }
    }
    .table {
      width: 100%;
      margin-bottom: 30px;
      table {
        width: 100%;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        tr {
          &.goods-list:hover {
            background-color: @lightGrayBg;
          }
          & + tr {
            border-top: 1px dashed @bdLighterColor;
          }
          th {
            height: 30px;
            line-height: 30px;
            text-align: center;
            background-color: @bdLightColor;
            font-size: 12px;
            color: @gray;
            font-weight: normal;
          }
          td {
            height: 50px;
            text-align: center;
            font-size: 12px;
            color: @lightBlack;
            &.empty {
              width: 100%;
              font-size: 16px;
              text-align: center;
            }
            .views {
              min-width: 65px;
              span {
                display: block;
              }
              .log {
                width: 100%;
                font-size: 12px;
                color: @blue;
                &:hover {
                  opacity: 0.6;
                }
              }
            }
            .countdown-box {
              width: 100%;
              margin-top: 12px;
              text-align: left;
              box-sizing: border-box;
            }
          }
        }
      }
    }
  }
  .apply {
    .after-sale {
      z-index: 11;
      width: 70px;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
    }
  }
}
.priceLevel-box{
    border:1px solid #f3f3f3;
    border-radius: 5px;
    margin-top: 20px;
    padding: 20px;
    .priceLevel-box-title{
      font-size: 16px;
      .priceLevel-box-labelBtn{
        cursor: pointer;
          background:  #409EFF;
          color: #fff;
          padding: 5px;
          border-radius: 5px;
          margin:5px 15px;
          font-size: 12px;
          i{
            margin-right: 5px;
          }
      }
    }
    .priceLevel-box-content{
        margin-top: 30px;
       .priceLevel-box-line{
         display: flex;
         align-items: center;
         margin-bottom: 20px;
         span{
           display: block;
           width: 100px;
           text-align: right;
           margin-right: 10px;
         }
         
       }
    }
    .priceLevel-box-input{
      width: 300px;
    }
    .priceLevel-box-input2{
      width: 100px;
    }
    .priceLevel-box-select{
      width: 100px;
      margin:0 15px;
    }
    .priceLevel-box-content2{
      margin-top: 20px;
      border:1px solid #f3f3f3;
      .priceLevel-box-label{
        background: #eef8fa ;
        padding:12px 15px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        div{
          color: #15BED6;
          span{
            margin-left: 15px;
            cursor: pointer;
          }
          
        }
      }
      .priceLevel-box-labelBox{
         padding: 15px;
         .priceLevel-box-line2{

         }
      }
      
      

    }
    .priceLevel-box-labelBox2{
       .priceLevel-box-labelBoxTitle{
         margin-left: 15px;
         color: #409EFF;
       }
       .priceLevel-box-labelBoxCheckBox{
         margin:15px;
       }
    }
    
}
.priceLevel-btn{
  margin: 20px;
  display: flex;
  align-items: center;
  justify-content:flex-end;
  
  span{
    display: block;
    padding: 10px 0;
    width: 80px;
    text-align: center;
    border-radius: 5px;
    cursor: pointer;
  }
  .priceLevel-btn-save{
      background: #15BED6;
      color: #fff;
      
  }
  .priceLevel-btn-back{
      margin-left: 25px;
      background: #f3f3f3;
  }
}
.levelDialog-box{
  // border-top:1px solid #f3f3f3;
  padding: 20px 0 20px 100px;
  .levelDialog-box-line{
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    .levelDialog-box-span{
      width: 100px;
    }
    .levelDialog-box-span2{
       color: #409EFF;
    }
    .levelDialog-box-select{
      width: 100px;
    }
    .levelDialog-box-input{
      width: 200px;
    }
    .levelDialog-box-input2{
      width: 100px;
    }
  }
}


</style>

