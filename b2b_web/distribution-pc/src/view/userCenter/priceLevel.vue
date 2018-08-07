<template>
  <div>
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
              <h6 >价格等级</h6>
              <span class="add-priceLevel" @click="addLevelPrice"><i class="el-icon-plus"></i>新增等级价</span>
            </div>
            <div class="query-detail" style="margin-top:20px">  
              <!-- 表格列表数据 --y-->
              <div class="table">
                <table>
                  <tr>
                    <th>价格等级名称</th>
                    <th>基础计算公式</th>
                    <th>是否有特殊公式</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                  <!-- <tr v-if="levelList.length === 0 || totalCount === 0">
                    <td class="empty" colspan="11">{{ $t("P.NoData") }}</td>
                  </tr> -->
                  <tr>
                    <td>默认等级价<span style="color:#999">（系统内置）</span></td>
                    <td>分销基础价</td>
                    <td>否</td>
                    <td>启用</td>
                    <td>-</td>
                  </tr>
                  <tr class="goods-list" v-for="(item, index) in levelList" :key="index">
                    <td>{{ item.name }}</td>
                    <td>分销基础价{{ item.arithmeticType==1?'*':'+' }}{{item.arithmeticNum}}</td>
                    <td>{{ item.specialFlag==1?'是':'否' }}</td>
                    <td>{{ item.openFlag==1?'启用':'停用' }}</td>
                    <td class="rl-clear">
                      <span class="tableBtn" @click="setLevelPrice(item.id)">设置</span> 
                      <span class="tableBtn" @click="deleteFun(item.id)">删除</span>
                    </td>
                  </tr>
                </table>
              </div>
            </div>
          </div>
         
        </div>
      </div>
    </div>
    

    

    <!--设置等级价--->
    <el-dialog class="alls" title="新增价格等级" :visible.sync="levelDialogVisible">
      <div class="levelDialog-box" >
          <div class="levelDialog-box-line">
              <span class="levelDialog-box-span">等级名称：</span>
              <el-input v-model="formInfo.name" placeholder="请输入名称" class="levelDialog-box-input"></el-input>
          </div>
          <div class="levelDialog-box-line">
              <span class="levelDialog-box-span">特殊公式：</span>
              <el-radio v-model="formInfo.specialFlag" label="0">不启用</el-radio>
              <el-radio v-model="formInfo.specialFlag" label="1">启用</el-radio>
          </div>
          <div class="levelDialog-box-line">
              <span class="levelDialog-box-span">状态：</span>
              <el-radio v-model="formInfo.openFlag" label="0">不启用</el-radio>
              <el-radio v-model="formInfo.openFlag" label="1">启用</el-radio>
          </div>
          <div class="levelDialog-box-line">
              <span class="levelDialog-box-span">通用公式：</span>
              <div>
                  <span>等级价格 = 成本价</span>
                  <el-select v-model="formInfo.arithmeticType" placeholder="请选择" class="levelDialog-box-select">
                    <el-option key="1" label="*" value="1"> </el-option>
                    <el-option key="2" label="+" value="2"> </el-option>
                  </el-select>
                  <el-input v-model="formInfo.arithmeticNum" placeholder="请输入数值" class="levelDialog-box-input2"></el-input>
              </div>
          </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addLevelPriceSaveFun">保存</el-button>
        <el-button @click="levelDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>


    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>

    
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
import {scaleprice,brandList,nobrandList,goodsListD,nogoodsList,scalepriceList,adjustScaleprice} from '@/apiService/api'

export default {
  components: { Header, Left, onceAgain, loading, countDown },
  data() {
    return {
      formInfo:{
				distributorId:'',
				name:'',
				openFlag:'1',  //状态, 1启用,0停用
				arithmeticType:'',  //1 乘 2 加 3 除 4 减
				arithmeticNum:'',  //参加运算的数值
				specialFlag:'1', //是否启用特殊公式, 1是,0否
			},
      userState: 2,
      showLoading:false,
      totalCount:0,
      distrbutionId:'',
      // 等级价设置
      levelList:[],
      levelDialogVisible:false,
      selectLevelVal:'',
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种   
      exchange:1, //汇率
      
    };
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  created() {
    this.exchange=Number( localStorage.getItem('exchange')); 
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
  
    this.scalepriceListFun();

  },
  methods: {
    // 获取等级价格
    scalepriceListFun(){
      let that=this;
      that.levelList=[];
      let id=localStorage.getItem('userId');
      scalepriceList({distributorId:id }).then(res=>{
        if(res.success){
          that.levelList=[...that.levelList,...res.data];
        }
      })
    },
    // 新增等级价--打开
    addLevelPrice(){
       this.levelDialogVisible=true;
       this.formInfo={
          distributorId:localStorage.getItem('userId'),
          name:'',
          openFlag:'1',  //状态, 1启用,0停用
          arithmeticType:'',  //1 乘 2 加 3 除 4 减
          arithmeticNum:'',  //参加运算的数值
          specialFlag:'1', //是否启用特殊公式, 1是,0否
        }
    },

    // 新增价格等级
    addLevelPriceSaveFun(){
        if(this.formInfo.name==''){
          this.$message('请输入等级名称');
          return
        }
        if(this.formInfo.arithmeticType==''){
          this.$message('请选择公式符号');
          return
        }
        if(this.formInfo.arithmeticNum==''){
          this.$message('请输入公式的数值');
          return
        }
        scaleprice('POST',this.formInfo).then(res=>{
            if(res.success){
              this.$message('新增公式成功！');
               this.levelDialogVisible=false;
              this.scalepriceListFun();
            }else{
              this.$message(res.errMessage);
             
            }
				})
    }, 

    // 设置
    setLevelPrice(id){
       this.$router.push({ name: "priceLevelDetail",query:{id:id} });
    },
   
   
    // 根据分销商id获取品牌可视
    brandListFun(){
       brandList({distributorId:localStorage.getItem('userId')}).then(res=>{
         if(res.success){
            this.brandListData=res.data;
            res.data.forEach(item=>{
              this.selectBrandList.push(item.id);
            })
          }
       })
    },

    // 品牌数据整合
    brandDataListFun(){
       this.brandDialogVisible=true;
       this.selectBrandList=[];
       nobrandList('GET',{distributorId:this.distrbutionId}).then(res=>{
          let list=res.data;
          let list2=this.brandListData;
          for(let i=0;i<list2.length;i++){
            let flag=true;
            for(let j=0;j<list.length;j++){
               if(list2[i].id==list[j]){
                  flag=false;
               }
            }
            if(flag){
               this.selectBrandList.push(list2[i].id);
            }
            
          }
          this.brandChangeFun();
       })
    },

    // 获取品牌不可视
    nobrandListFun(id){
       this.distrbutionId=id; 
       this.brandDataListFun()
    },
    
    // 品牌可视--全选
    brandAllChangeFun(val){
      let list=this.brandListData;
      let arr=[];
      for(let i=0;i<list.length;i++){
         arr.push(list[i].id) 
      }
      this.selectBrandList=val?arr:[];
      this.noSelectBrandList=val?[]:arr;
    },

    // 品牌可视--单选
    brandChangeFun(){
      let lengthVal=this.selectBrandList.length;
      let lengthVal2=this.brandListData.length;
      if(lengthVal==lengthVal2){
         this.allBrandChecked=true;
      }else{
         this.allBrandChecked=false;
      }
      let list1=this.brandListData;
      let list2=this.selectBrandList;
      for(let i=0;i<list1.length;i++){
        let flag=true;
        for(let j=0;j<list2.length;j++){
           if(list1[i].id==list2[j]){
              flag=false;
           }
        }
        if(flag){
          this.noSelectBrandList.push(list1[i].id);
        }
      }
    },

    // 设置品牌可视
    brandSaveFun(){
				let params={
					brandIds:this.noSelectBrandList,
					distributorId:this.distrbutionId
				}
				nobrandList('POST',params).then(res=>{
					if(res.success){
            this.$message('设置品牌成功');
            this.brandDialogVisible=false;
						this.getOrderList();
					}else{
            this.$message(res.errMessage);
					}
				})
    },

    // 获取商品
    async getGoodsList(){
       this.goodDialogVisible=true;
      let that=this;
      let params={
        distributorId:localStorage.getItem('userId'),
        page:this.goodPage,
        size:10,
      }
      let res=await goodsListD(params);
      let res2=await nogoodsList('GET',{distributorId:this.distrbutionId});
      if(res.success){
          that.goodTotalPage=res.data.total;
          that.goodList=res.data.list;
          if(res2.success){
            let list=res2.data;
            let flag2=true;
            that.goodList.forEach(item=>{
              if(list.length==0){
                this.$set(item,'isVisual',true);
                this.allIsVisual=true;
              }else{
                let flag=true;
                for(let i=0;i<list.length;i++){
                  if(item.id==list[i]){  
                    flag=false;
                    flag2=false;
                  }
                }
                this.$set(item,'isVisual',flag);
               
              }
            })
            this.allIsVisual=flag2;
          }

      }
    },

    // 获取下级不可视商品
    getNoGoodsDataList(id){
     
      this.distrbutionId=id;
      console.log('分销商id：',id);
      this.getGoodsList();
      
    },
    // 商品可视--全选
    goodAllChangeFun(val){
        this.allIsVisual=true;
        this.goodList.forEach(item=>{
          this.$set(item,'isVisual',val);
          if(!item.isVisual){
            this.allIsVisual=false;
          }
        })
    },

    // 商品可视--单选
    goodOneChangeFun(val,id){
        console.log(val);
        console.log(id);
        this.goodList.forEach(item=>{
          if(item.id==id){
              this.$set(item,'isVisual',val);
          }
        })
    },

    // 商品可视分页
    goodCurrentChange(val){
      this.goodPage = val;
      this.getGoodsList();
    },

    // 设置商品可视--保存
    goodSaveFun(){
        let checkList=[];
       
        for(let i=0;i<this.goodList.length;i++){
           if(!this.goodList[i].isVisual){
              checkList.push(this.goodList[i].id)
           }
        }
				let params={
					goodsIds:checkList,
					distributorId:this.distrbutionId
				}
				nogoodsList('POST',params).then(res=>{
					if(res.success){
            this.$message('设置商品可视成功');
            this.getGoodsList();
					}else{
            this.$message(res.errMessage);
					}
				})
    },
    

    // 设置等级价
    levelSetFun(id){
      this.levelDialogVisible=true;
      this.distrbutionId=id;
      this.selectLevelVal='';
    },
    // 调整价格价格
    bindPickerChange() { 
      let parames={
        applyStatus:2,
        id:this.distrbutionId,
        scalePriceId:this.selectLevelVal,
      }
   
      if(this.selectLevelVal===''){
        this.$message('请选择价格等级')
        return
      }
      adjustScaleprice(parames).then(res=>{
        if(res.success){
          this.$message('价格等级调整成功！');
          this.levelDialogVisible=false;
        }else{
          this.$message(res.errMessage);
        }
      })
    },

    // 删除等级价
    deleteFun(id){
        scaleprice('DELETE',{id:id}).then(res=>{
					if(res.success){
            this.$message('删除成功');
					  this.scalepriceListFun(); 
					}else{
            this.$message(res.errMessage);
					}
				})
    }

  
  },
  
};
</script>

<style  lang='less'>
@import url("../../assets/less/variable.less");
.tableBtn{
   color:#15BED6;
   margin-left: 10px;
   cursor: pointer;
}
.add-priceLevel{
  border:1px solid #15BED6;
  padding: 5px 10px;
  border-radius: 5px;
  color: #15BED6;
  cursor: pointer;
  i{margin-right: 5px;}
}
.levelDialog-box{
  
  border-top:1px solid #f3f3f3;
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
      width: 100px!important;
    }
  }
}
.goodDialog-div{
  padding-left: 5%;
  border-top: 1px solid #f3f3f3;
  padding-top: 15px;
  ul{
    border:1px solid #f3f3f3;
    width: 90%;
    .goodDialog-div-li1{
       background: #eef8fa;
       padding:10px 15px;
    }
    .goodDialog-div-li2{
      display: flex;
      align-items: center;
      padding: 10px 15px;
      .goodDialog-div-name{
        margin-left: 10px;
        width:95%;
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
      }
    }
  }
  
}
.goodDialog-div-pagination{
    margin-left: 30px;
    margin-top: 15px;
  }
.om-orderStatus-checkbox{
   display: flex;
   .om-orderStatus-all{
     margin-right: 15px;
   }
}
.brandDialog-box{
  height: 300px;
  padding: 30px;
   .all-file {
    border-bottom: 1px solid #d2d2d2;
    padding: 10px;
  }
  .file {
    padding: 10px;
  }
  .randDialog-box-checkBox{
    margin-top: 10px;
  }
}
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
/*弹框*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.5;
}
.pro-cover {
  width: 555px;
  height: 350px;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  z-index: 99;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../assets/images/shut.png") no-repeat center center;
  }
}
/*再来一单*/
.max-height300 {
  max-height: 300px;
}
.shop-table {
  margin-bottom: 20px;
  overflow-y: scroll;
  table {
    tr {
      td {
        height: 45px;
        text-align: center;
        border-bottom: 1px solid #ccc;
        .buy-sum {
          width: 92px;
          height: 22px;
          line-height: 22px;
          border: 1px solid #ebeff5;
          div {
            height: 22px;
            box-sizing: border-box;
            background-color: #fff;
            input {
              width: 62px;
            }
          }
          .buyac {
            width: 22px;
            font-size: 22px;
            color: #9b9b9b;
            cursor: pointer;
            text-align: center;
          }
          .buyb {
            width: 48px;
            line-height: 22px;
            color: #3a3a3a;
            border-left: 1px solid #ebeff5;
            border-right: 1px solid #ebeff5;
            input {
              width: 46px;
            }
          }
        }
        .songImg {
          width: 50px;
          img {
            margin-top: 5px;
            height: 50px;
          }
        }
      }
    }
  }
}
.export-price {
  border: 1px solid transparent;
  background-color: #eef8fa;
  .all-file {
    border-bottom: 1px solid #d2d2d2;
    padding-left: 10px;
    padding-right: 10px;
  }
  .file {
    padding-left: 10px;
    padding-right: 10px;
  }
}
</style>
<style lang='less'>
.el-dialog__wrapper {
  .el-dialog {
    width: 600px;
    .el-dialog__header {
      text-align: center;
    }
    .el-dialog__body {
      padding: 0;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
