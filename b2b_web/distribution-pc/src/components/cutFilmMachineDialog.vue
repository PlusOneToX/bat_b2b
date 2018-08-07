<template>
    <div class="dialogwrap">
      <div class="cover" v-if="showOpen"></div>
      <div class="prote-cover cover-box rl-padding-bottom-lllg rl-relative" v-if="showOpen">
        <div class="rl-margin-bottom-default rl-padding-left-lllg rl-padding-top-xxxs rl-padding-bottom-xxxs rl-text-lg rl-text-bold rl-bg-gray-mm">
          {{$t('Register.cutFilmMachine')}}
          <div>{{$t('Register.cutFilmMachineDesc')}}</div>
        </div>
        <div class="address-infos rl-bg-white rl-padding-left-default rl-clear rl-margin-zero">
          <div class="pop-box rl-padding-top-lllg">
            <div class="box">
              <div class="box-word" v-if="$i18n.locale === 'zh'">
                
              </div>
              
            </div>

          </div>
        </div>
        <div class="box-foot rl-tc rl-margin-top-double">
          <span v-if="!select" @click="goBack" class="rl-bg-gray-mm cursor-pointer">{{$t('P.Cancel')}}</span>
          <span v-if="!select" @click="agreeGoon" class="rl-bg-blue-xs rl-text-white cursor-pointer rl-margin-left-default">{{$t('Register.AgreeAndContinue')}}</span>
          <span v-if="select" @click="agreeGoon" class="rl-bg-blue-xs rl-text-white cursor-pointer rl-margin-left-default">{{$t('Message.Confirm')}}</span>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
      name: 'cutFilmMachineDialog',
      props: {
        showOpen: {
          type: Boolean
        },
        select: {
          type: Boolean
        }
      },
      methods: {
        agreeGoon () {
          this.$emit('close')
        },
        goBack () {
          this.$router.go(-1)
        }
      }
    }
</script>

<style scoped lang='less'>
  .dialogwrap {
    .cover{
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: #000;
      z-index: 99;
      opacity: 0.5;
    }
    .prote-cover{
      width: 50%;
      height: 70%;
      border: 1px solid #ccc;
      border-radius: 5px;
      z-index: 99;
      background: #fefefe;
      box-shadow: 0 1px 10px 5px rgba(0,0,0,0.3);
      padding: 0;
    }
    .cover-box{
      box-sizing: border-box;
      position: fixed;
      top: 50%;
      left: 50%;
      bottom: 0;
      transform: translate3D(-50%, -50%, 0);
      z-index: 99;
      .address-info{
        width: 250px;
        .all{
          width: 100px;
          height: 30px;
          line-height: 30px;
          border-radius: 5px;
        }
        .right{
          height: 28px;
          line-height: 28px;
          border: 1px solid #ccc
        }
      }
      .address-infos{
        padding:0 30px;
        height: calc(100% - 180px);
        overflow: auto;
        -ms-overflow-style: none;
        scrollbar-width: none;
        &::-webkit-scrollbar{
          display: none;
        }
        .pop-box{
          padding-top:0;
          width: 100%;
          .box{
            width: 100%;
            height: 100%;
            overflow-y: auto;
            scrollbar-width: none;
            -ms-overflow-style: none;
            &::-webkit-scrollbar {
              display: none;
            }
            .box-word{
              h1{
                margin: 30px 0 10px;
                line-height:2;
              }
              p{
                margin-bottom: 14px;
                line-height: 1.8;
                text-indent: 28px;
                text-align: justify;
                &.rl-tr{
                  text-align: right;
                }
              }
            }
          }
        }
      }
      .box-foot{
         margin:30px 0 20px;
        span{
          display: inline-block;
          width: 180px;
          height: 40px;
          line-height: 40px;
          border-radius: 4px;
        }
      }
    }
  }
</style>
