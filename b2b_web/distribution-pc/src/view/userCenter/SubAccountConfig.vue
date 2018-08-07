<template>
  <div class="store-manage">
    <!--公共头部-->
    <Header :userState="userState"></Header>
    <!--主内容-->
    <div class="store-main rl-clear rl-margin-zero">
      <!--公共左边-->
      <Left></Left>
      <div class="store-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
        <div class="content">
          <h6 class="store-right-title">{{$t('UserCenter.SubAccountConfig')}}</h6>
          <div class="top-box">
            <!-- 新增 -->
            <el-button class="mini-search-btn"  type="info"  @click="handleAdd" size="mini" >{{$t('UserCenter.Add')}}</el-button>
          </div>
          <div class="table-box">
            <el-table
              :data="tableData"
              header-row-class-name="header-row"
              border
              style="text-align:center;"
              v-loading="loading">
              <el-table-column align="center" :label="$t('UserCenter.SubAccountConfigName')"  prop="name" width="160"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.PercentType')" prop="amountType" :formatter="formatType" :min-width="100"></el-table-column>
              <el-table-column align="center" v-for="(item, i) in levelRatioList" :key="i" :label="item.name" width="160">
                 <template slot-scope="scope">
                   <div>{{scope.row.levelRatioList[i]?(scope.row.levelRatioList[i].ratio+'%'):'--'}}</div>
                 </template>
               </el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.Store')" :min-width="100">
                  <template slot-scope="scope">
                    <span class="log rl-tc cursor-pointer" @click="handleSelect(scope.row.id)">点击查看</span>
                </template>
              </el-table-column>
              <el-table-column align="center"  key="12" :label="$t('ShopCarts.Operation')" :min-width="120">
                <template slot-scope="scope">
                  <span class="log rl-tc cursor-pointer"  :class="scope.row.openFlag==='1'?'disabled':''" @click="editDialog(scope.row.id)">{{$t('ConfirmOrder.Edit')}}</span>
                  <span class="log rl-tc cursor-pointer" @click="handleDelete(scope.row.id)">{{$t('ShopCarts.Delete')}}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <div class="rl-tr rl-margin-top-default" v-if="totalCount > 0">
          <el-pagination background :current-page="pageInfo.page" :page-sizes="[10, 20, 30]" :page-size="pageInfo.size"
            @size-change="onSizeChange" @current-change="onCurrentChange"
            layout=" prev, pager, next , total , sizes , jumper"
            :total="totalCount"
            class="page"
          ></el-pagination>
        </div>
      </div>
    </div>
     <!----新增、编辑弹框----->
    <el-dialog :title="title" :visible.sync="DialogVisible" width="40%" center>
       <div class="add-info rl-padding-top-default">
         <el-form ref="formData" :model="formData"></el-form>
            <div class="items rl-clear">
                <span class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}">
                    <em class="red">*</em>配置名称:
                </span>
                <div class="search-input rl-fl">
                    <input type="text" v-model="formData.name" placeholder="请输入配置名称" />
                </div>
            </div>
            <div class="items rl-clear">
                <span  class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}"><em class="red">*</em>分账金额:</span>
                <el-radio-group  v-model="formData.amountType" class="adress-el-radio">
                    <el-radio :label="1" value="1">按实付金额</el-radio>
                    <el-radio :label="2" value="2">按利润金额</el-radio>
                </el-radio-group>
            </div>
            <div class="items rl-clear">
                <span class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}">
                    分账比例:
                </span>
                <div class="text rl-fl">
                    贵司微信账户的最大分账为实付金额的{{ratio}}%，超出比例会分账失败，请谨慎设置
                </div>
            </div>
        </div>

        <el-table :data="formData.levelRatioList" header-row-class-name="header-row" border style="text-align:center;" v-loading="loading">
          <el-table-column align="center" label="等级"  prop="levelName" width="160"></el-table-column>
          <el-table-column align="center" label="分账比例" prop="ratio" :min-width="100">
            <template slot-scope="scope">
              <el-input class="input-per" v-model="scope.row.ratio" onkeyup="this.value=this.value.replace('.','$#$').replace(/\./g,'').replace('$#$','.')"></el-input>%
            </template>
          </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="handleSave">保 存</el-button>
            <el-button @click="DialogVisible = false">取 消</el-button>
        </span>
    </el-dialog>
    <!----点击查看弹框----->
    <el-dialog
      title="关联店铺列表"
      :visible.sync="StoreDialogVisible"
      width="30%"
      center>
       <el-table
        :data="shopData"
        header-row-class-name="header-row"
        border
        style="text-align:center;"
        v-loading="loading">
        <el-table-column align="center" :label="$t('UserCenter.StoreName')"  prop="shopName" width="160"></el-table-column>
        <el-table-column align="center" :label="$t('UserCenter.StoreCode')" prop="shopCode" :min-width="100"></el-table-column>
        <el-table-column align="center" :label="$t('OrderSuccess.Status')" prop="openFlag" width="100" show-overflow-tooltip>
            <template slot-scope="scope">
                <div>{{getStatus(scope.row.openFlag)}}</div>
            </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
import { 
  subAccountUserConfig,
  subLevelListByCondition,
  subAccountUserConfigAdd,
  subAccountUserConfigUpdate,
  subAccountUserConfigDetail,
  subAccountUserConfigDel,
  shoplistByCondition,
  orderSubAccountRatio} from '@/apiService/api'
export default {
  components: {
    Header,
    Left
  },
  data () {
    return {
      action:this.locationUrl_L + 'flexible/v1/web/user/shop/import',
      loading: false,
      loading2: '',
      title: '',
      sId: undefined,
      userState: 2,
      appId: null,
      platform:'',
      pageInfo: {
        page: 1,
        size: 10
      },
      formData: {
        name: '',
        amountType: 1,
        levelRatioList: []
      },
      levelRatioList: [],
      tableData: [],
      shopData: [],
      totalCount: 0,
      ratio: 0,
      DialogVisible: false,
      StoreDialogVisible: false
    }
  },
  mounted () {
    this.getlevel()
    this._initData()
  },
  methods: {
    // 获取数据列表--Y
    _initData () {
      subAccountUserConfig(this.pageInfo).then(res=>{
         if(res.success){
            this.tableData = res.data.list;
            this.tableData.forEach(item=>{
              
            })
            this.totalCount = res.data.total
         }
      })
    },
    // 保存
    handleSave () {   
      this.$refs['formData'].validate(valid => {
        if(valid) {
          // try {
            // this.formData.levelRatioList.forEach(item => {
            //   if (parseInt(item.ratio) > parseInt(this.ratio)) {
            //     this.$message.error('分账比例不得超出最大分账比例，请谨慎设置！')
            //     throw new Error()
            //   }
            // })
            if (this.sId) {
              // 编辑
              subAccountUserConfigUpdate(this.formData).then(res => {
                if (res.success) {
                    this.$message.success('修改成功')
                    this.DialogVisible = false
                    this._initData()
                } else {
                  this.$message.error(res.errMessage)
                }
              })
            } else {
              // 新增
              subAccountUserConfigAdd(this.formData).then(res => {
                if (res.success) {
                    this.$message.success('新增成功')
                    this.DialogVisible = false
                    this._initData()
                } else {
                  this.$message.error(res.errMessage)
                }
              })
            }
          // } catch (error) {
          //   console.log();
          // }
        }
      })
    },
    // 编辑
    editDialog(id) {
      this.title= '编辑分账配置'
      this.sId = id
       // 查询数据详情
      subAccountUserConfigDetail({id: id}).then(res => {
        if (res.success) {
          if (res.data && res.data.levelRatioList.length > 0) {
            res.data.levelRatioList.forEach(item => {
              item.ratio = (item.ratio * 100).toFixed(2)
            })
          }
          this.formData = res.data
        }
      })
      if (!this.ratio) {
        this.getRaio()
      }
      this.DialogVisible = true
    },
    // 查看
    handleSelect (id) {
      // 查看关联店铺列表
      shoplistByCondition({userConfigId: id}).then(res => {
        if (res.success) {
          this.shopData = res.data
          this.StoreDialogVisible = true
        } else {
          this.$message.error(res.errMessage)
        }
      })
    },
    // 删除
    handleDelete (id) {
       if (id) {
        this.$confirm('此操作将删除数据，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          subAccountUserConfigDel({id: id}).then(res => {
            if (res.success) {
              this.$message({
                type: 'success',
                message: '删除成功'
              })
              this._initData()
            } else {
              this.$message({
                type: 'error',
                message: '删除失败'
              })
            }
          })
        }).catch((_) => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
      }
    },
    // 添加分账配置
    handleAdd () {
      this.title = '新增分账配置'
      this.sId = undefined
      this.formData.name = ''
      this.formData.amountType = 1
      this.formData.levelRatioList.forEach(item => {
        item.id = undefined
        item.ratio = ''
      })
      if (!this.ratio) {
        this.getRaio()
      }
      this.DialogVisible = true
    },
    // 获取分账比例
    getRaio() {
      orderSubAccountRatio().then(res => {
        if (res.success) {
          this.ratio = res.data * 100
        }
      })
    },
    // 获取分账等级
    getlevel() {
      subLevelListByCondition().then(res => {
        if (res.success) {
          if (res.data && res.data.length>0) {
            this.levelRatioList = []
            res.data.forEach(item => {
              this.levelRatioList.push({
                id: item.id,
                name: item.levelName
              })
              item.levelId = item.id
              item.ratio = ''
              item.id = undefined
            })
          }
          this.formData.levelRatioList = res.data
        }
      }).catch(err => {
        console.log(err)
      })
    },
    onSizeChange (val) {
      // 分页方法
      this.pageInfo.size = val
      this._initData()
    },
    onCurrentChange (val) {
      // 分页方法
      this.pageInfo.page = val
      this._initData()
    },
     // 获取状态
    getStatus (val) {
      if (Number(val) === 1) {
        return '启用'
      } else if (Number(val) === 2) {
        return '禁用'
      } else {
        return '--'
      }
    },
    formatType(row, col, val) {
       switch (val) {
        case 1:
          return '实付金额'
        case 2:
          return '利润金额'
      }
    }
  }
}
</script>

<style scoped="scoped" lang='less'>
  @import url("../../assets/less/variable.less");
  .store-manage {
    width: 100%;
  }
  .store-main {
    width: 1200px;
    .store-right{
      width: 1030px;
      .content{
        padding: 24px 40px 0;
        border: 2px solid @bdLightColor;
        border-radius: 8px;
      }
      .store-right-title {
        padding-bottom: 10px;
        border-bottom: 1px solid @bdLightColor;
        font-size: 20px;
      }
      .top-box{
        margin-top:25px;
        .upload-excel{
          display: inline-block;
          .el-button--primary{
            background-color: #15BED6 !important;
            border: 1px solid #15BED6;
            padding: 7px 15px;
            font-size: 12px;
            border-color: #15BED6;
          }
          /deep/.el-upload-list{
            display:none;
          }
        }
      }
      .search-box{
        margin-top:20px;
        .el-input{
          display: inline-block;
          width:80%;
          /deep/.el-input__inner{
            height: 38px;
            line-height: 38px;
          }
        }
        .el-select{
          display: inline-block;
          width:120px;
          /deep/.el-input__inner{
            height: 38px;
            line-height: 38px;
          }
        }
        .el-button{
          height: 38px;
          vertical-align: middle;
        }
        .ml-15{
          margin-left:15px;
        }
        .handle-wrap {
          position: absolute;
          right: 0;
          bottom: 15px;
          font-size: 14px;
          color: @blue;
          line-height: 1;
          cursor: pointer;
          &:hover{
            opacity: 0.6;
          }
          .mini-export{
            color: @blue;
          }
          .iconfont {
            margin-right: 2px;
            color: @lighterGray;
          }
        }
      }
      .table-box {
        width: 100%;
        margin-top: 20px;
        margin-bottom: 40px;
        /deep/.el-table {
          width: 100%;
          word-wrap: break-word;
          word-break: break-all;
          border-collapse: collapse;
          tr {
            &:hover {
              background-color: @lightGrayBg;
            }
            th {
              padding: 0;
              height: 32px;
              line-height: 32px;
              text-align: center;
              background-color: @bdLightColor;
              font-size: 12px;
              color: @gray;
              font-weight: normal;
            }
            td {
              height: 53px;
              text-align: center;
              font-size: 12px;
              color: @lightBlack;
              border: none;
              border-top: 1px dashed @bdLighterColor;
              .log {
                margin: 0 10px;
                color: @blue;
                &.disabled{
                  pointer-events: none;
                  color: #3333;
                }
                &:hover {
                  opacity: 0.6;
                }
              }
              .cell{
                canvas{
                  width:40px;
                  height:40px;
                  vertical-align: middle;
                }
              }
            }
          }
        }
      }
    }
  }
  /deep/.el-dialog__wrapper{
    .el-dialog__header{
      padding-top:30px;
    }
    .el-dialog__body{
      margin:0 auto;
      width:80%;
      text-align: center;
       .add-info {
        .items {
          margin-bottom: 15px;
          height: 30px;
          line-height: 30px;
          .item-left {
            margin-right: 15px;
            display: block;
            width: 100px;
            height: 30px;
            line-height: 30px;
            font-size: 14px;
            &.en {
              width: 115px;
            }
          }
          .text{
              display: inline-block;
              width: calc(100% - 120px);
              font-size:12px;
              text-align: left;
          }
          .red {
            color: @red;
          }
          .search-input {
            width: calc(100% - 120px);
            height: 28px;
            line-height: 28px;
            border: 1px solid @bdLightColor;
            input {
              padding-left: 10px;
              width: 100%;
              border: 0;
              box-sizing: border-box;
            }
            &.large {
              width: 420px;
              height: 66px;
              border: none;
              textarea {
                padding: 5px 10px;
                width: 100%;
                height: 100%;
                border: 1px solid @bdLightColor;
                box-sizing: border-box;
              }
            }
          }
          .item {
            input.trade-input {
              padding-left: 10px;
              width: 115px;
              height: 30px;
              line-height: 30px;
              box-sizing: border-box;
              font-size: 12px;
              color: @lighterBlack;
              background: url("../../assets/images/selectUl.png") no-repeat
                100px center;
              box-sizing: border-box;
              overflow: hidden;
              text-overflow: ellipsis;
              border: 1px solid @bdLightColor;
            }
            .selectUl {
              position: absolute;
              top: 30px;
              left: 0;
              z-index: 11;
              width: 85px;
              box-sizing: border-box;
              max-height: 220px;
              overflow-y: auto;
              border: 1px solid @bdLightColor;
              li {
                padding-left: 10px;
                line-height: 30px;
                cursor: pointer;
                box-sizing: border-box;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
              li:hover {
                background: @blue;
                color: @white;
              }
            }
          }
          .adress-el-radio{
              height: 40px;
              line-height: 40px;
              float:left;
          }
        }
        .button {
          margin-left: 130px;
          span {
            display: block;
            padding: 0 20px;
            min-width: 60px;
            height: 30px;
            line-height: 30px;
            text-align: center;
            color: @white;
            border-radius: 4px;
            box-sizing: border-box;
          }
        }
    }
        .raido-group{
            .el-radio{
            display: inline-block;
            margin:0 30px;
            line-height: 30px;
            text-align: center;
            .el-radio__input{
                display: none;
            }
            .el-radio__label{
                padding: 0;
                .icon{
                display: inline-block;
                width:64px;
                height:64px;
                background-size: 64px 64px;
                    background-repeat: no-repeat;
                &.screen{
                    background-image: url('../../assets/images/store/icon_screen@2x.png');
                }
                &.all{
                    background-image: url('../../assets/images/store/icon_all@2x.png');
                }
                }
                .text{
                margin-top:10px;
                color:#555555
                }
            }
            &.is-checked{
                .el-radio__label{
                .screen{
                    background-image: url('../../assets/images/store/icon_screen_pre@2x.png');
                }
                .all{
                    background-image: url('../../assets/images/store/icon_all_pre@2x.png');
                }
                }
                .text{
                color:#0DB8CF
                }
            }
            }
        }
        .el-table{
            .el-table__body{
                .input-per{
                    display: inline-block;
                    width:80%;
                    margin-right:10px;
                    .el-input__inner{
                      text-align: center;
                    }
                }
            }
          
        }
    }
    .el-dialog__footer{
      padding-bottom:40px;
      .el-button{
        margin:0 30px;
        &.el-button--primary{
          background-color: #0DB8CF;
          border-color: #0DB8CF;
        }
      }
    }
  }
</style>
