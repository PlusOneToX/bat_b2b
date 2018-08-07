<template>
  <div class="register" v-if="showOpen">
    <div class="intro-content">
      <!-- <div class="title">{{$t('Register.RegAgreement')}}</div> -->
      <div class="title">{{title}}</div>
      <div class="address-infos rl-bg-white rl-padding-left-default rl-clear rl-margin-zero">
        <div class="pop-box rl-padding-top-lllg">
          <div class="box">
            <div v-html="contentData"></div>
          </div>
        </div>
      </div>
      <div class="btn-wrap">
        <span v-if="!select" @click="goBack" class="gray-btn">{{$t('P.Cancel')}}</span>
        <span v-if="!select" @click="agreeGoon" class="blue-btn">{{$t('Register.AgreeAndContinue')}}</span>
        <span v-if="select" @click="agreeGoon" class="blue-btn">{{$t('Message.Confirm')}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {agreementSignList,agreementSignId} from  '@/apiService/api'
export default {
  name: 'RegisterDialog',
   data () {
    return {
      agreementId:'',
      contentData:'',
      title:''
    }
   },
  props: {
    showOpen: {
      type: Boolean
    },
    select: {
      type: Boolean
    }
  },
  mounted(){
      let tenantNo=localStorage.getItem('tenantNo');
      console.log('tenantNo',tenantNo);
      if(tenantNo&&tenantNo!=null&&tenantNo!=undefined&&tenantNo!=''){
         this.getData();
      }
     
  },
  methods: {
    agreeGoon () {
      this.$emit('close');
    },
    goBack () {
      this.$router.go(-1);
    },
    getData(){
        let agreementArea='';
        if(this.$i18n.locale === 'zh'){
             agreementArea=0;
        }else{
             agreementArea=1;
        }
        
        agreementSignList({page:1,size:100,agreementArea :agreementArea,type:2}).then(res=>{
          this.agreementId=res.data.list[0].id;
          agreementSignId({id:res.data.list[0].id}).then(res2=>{
              console.log('协议数据',res2.data);
              this.contentData=res2.data.content;
              this.title=res2.data.name;
          })
           
        })
    },

    // 根据品牌id查询协议
  }
};
</script>

<style scoped lang='less'>
@import url("../assets/less/variable.less");
.register {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 99;
  .intro-content {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 50%;
    height: 70%;
    border-radius: 8px;
    transform: translate3D(-50%, -50%, 0);
    z-index: 99;
    background-color: @white;
    .title {
      padding-left: 28px;
      height: 60px;
      line-height: 60px;
      font-size: 22px;
      font-weight: bold;
      color: #000000;
      background-color: @entryBd;
      border-radius: 8px 8px 0 0;
    }
    .address-infos {
      padding:30px 30px 0;
      height: calc(100% - 160px);
      box-sizing: border-box;
      overflow: auto;
      -ms-overflow-style: none;
      scrollbar-width: none;
      &::-webkit-scrollbar{
        display: none;
      }
      .pop-box {
         padding-top:0;
        width: 100%;
        .box {
          width: 100%;
          height: 100%;
          overflow-y: auto;
          scrollbar-width: none;
          -ms-overflow-style: none;
          &::-webkit-scrollbar {
            display: none;
          }
          .box-word {
            h1 {
              margin: 30px 0 10px;
              line-height: 2;
            }
            p {
              margin-bottom: 14px;
              line-height: 1.8;
              text-indent: 28px;
              text-align: justify;
              &.rl-tr {
                text-align: right;
              }
            }
          }
        }
      }
    }
    .btn-wrap {
      margin-top: 30px;
      font-size: 16px;
      text-align: center;
      span {
        display: inline-block;
        width: 180px;
        height: 40px;
        line-height: 40px;
        border-radius: 4px;
        cursor: pointer;
        & + span {
          margin-left: 15px;
        }
        &.gray-btn {
          color: @lightBlack;
          background-color: @entryBd;
        }
        &.blue-btn {
          color: @white;
          background-color: @blue;
        }
        &:hover {
          opacity: 0.8;
        }
      }
    }
  }
}
</style>
