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
          <h6 class="store-right-title">{{$t('UserCenter.StoreManage')}}</h6>
          <div class="top-box">
            <!-- 下载模板 -->
            <el-button class="mini-search-btn" type="info" @click="exportXLS" size="mini">{{$t('P.DownloadTemplate')}}</el-button>
            <!-- 批量上传-Y -->
            <el-upload class="upload-excel" :headers="importHeaders" :action="action" :auto-upload="true"
                :show-file-list="true" :before-upload="beforeUpload" :on-success="uploadSuccess" :on-error="uploadFail" :on-progress="onProgress">
                <el-button type="primary">
                  {{$t('P.BatchUpload')}}
                </el-button>
            </el-upload>
            <!-- 添加店铺 -->
            <el-button class="mini-search-btn"  type="info"  @click="addStore" size="mini" >{{$t('P.AddStore')}}</el-button>
          </div>
          <div class="search-box">
            <el-input
              size="mini"
              :placeholder="$t('UserCenter.StoreName')"
              type="text"
              clearable
              v-model.trim="pageInfo.shopName"
              @clear="handleClearName"
            ></el-input>
            <el-input
              class="ml-15"
              size="mini"
              :placeholder="$t('UserCenter.StoreCode')"
              type="text"
              clearable
              v-model.trim="pageInfo.shopCode"
              @clear="handleClearCode"
            ></el-input>
            <!--公众号--->
            <el-select
              class="app_select ml-15"
              :placeholder="$t('UserCenter.AppName')"
              size="mini"
              v-model="pageInfo.appName"
              clearable
              @change="handleSelectApp"
            >
              <el-option
                v-for="item in appArr"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
            <!--状态--->
            <el-select class="custom_select ml-15" :placeholder="$t('OrderSuccess.Status')"
              size="mini" v-model="pageInfo.openFlag" clearable  @change="handleSelect">
              <el-option label="开启" :value="1"></el-option>
              <el-option label="停用" :value="0"></el-option>
            </el-select>
            <!-- 搜索 -->
            <el-button  class="mini-search-btn ml-15" type="info" @click="search" size="mini">{{$t('P.Search')}}</el-button>
            <!-- 导出Excel -->
            <!-- @click="ExportDialogVisible = true" -->
            <div class="handle-wrap rl-tr"  @click="handleExport">
              <i class="iconfont icon-export"></i>
              {{$t('UserCenter.ExportExcel')}}
            </div>

          </div>
          <div class="table-box">
            <el-table
              :data="tableData"
              header-row-class-name="header-row"
              border
              style="text-align:center;"
              @select="select"
              @select-all="selectAll"
              @selection-change="handleSelectionChange"
              v-loading="loading"
            >
              <el-table-column align="center" type="selection" key="1"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.StoreName')"  prop="shopName" width="160"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.StoreCode')" prop="shopCode" :min-width="90"></el-table-column>
              <el-table-column align="center" :label="$t('OrderSuccess.Status')" prop="openFlag" width="60" show-overflow-tooltip>
                <template slot-scope="scope">
                  <div>{{getStatus(scope.row.openFlag)}}</div>
                </template>
              </el-table-column>
              <el-table-column align="center" v-if="subAccountFlag==1" :label="$t('UserCenter.Salesman')" prop="salemanName" :min-width="100"></el-table-column>
              <el-table-column align="center" v-if="subAccountFlag==1" :label="$t('UserCenter.SubAccountConfig')" prop="userConfigName" :min-width="100"></el-table-column>
              <!-- 公众号名称 -->
              <el-table-column align="center" :label="$t('UserCenter.AppName')" prop="appName" :min-width="100"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.Remark')" prop="remark" :min-width="100"></el-table-column>
              <el-table-column align="center" :label="$t('UserCenter.StoreQrcode')" width="140">
                <template slot-scope="scope" style="text-align: center; width: 140px; height: 140px">
                  <!-- <qriously v-if="scope.row.url" :value="scope.row.url" :size="300" :id="'qrcode' + scope.row.id" class="qrcode"/> -->
                  <el-image style="text-align: center; width: 120px;height: 120px;padding-right: 0px;" fit="contain"
                    :src="scope.row.qrUrl +'?x-oss-process=image/resize,h_120,l_120'" :preview-src-list="[scope.row.qrUrl]">
                  </el-image>
                </template>
              </el-table-column>
              <el-table-column align="center"  key="12" :label="$t('ShopCarts.Operation')" :width="250">
                <template slot-scope="scope">
                  <span class="log rl-tc cursor-pointer"  :class="scope.row.openFlag==='1'?'disabled':''" @click="onEdit(scope.row)">{{$t('ConfirmOrder.Edit')}}</span>
                  <span class="log rl-tc cursor-pointer" @click="onChangeStatus(scope.row)">{{getfStatus(scope.row.openFlag)}}</span>
                  <span class="log rl-tc cursor-pointer" :class="scope.row.openFlag==='1'?'disabled':''"  @click="onDownload(scope.row, scope.$index)">{{$t('UserCenter.Download')}}</span>
                  <span class="log rl-tc cursor-pointer" :class="scope.row.openFlag==='1'?'disabled':''" @click="onDelete(scope.row)">{{$t('ShopCarts.Delete')}}</span>
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
    <!----导出弹框----->
    <el-dialog
      title="导出表格"
      :visible.sync="ExportDialogVisible"
      width="40%"
      center>
        <div class="raido-group">
          <el-radio v-model="radioType" label="1">
            <i class="icon screen"></i>
            <div class="text">筛选后信息</div>
          </el-radio>
          <el-radio v-model="radioType" label="2">
            <i class="icon all"></i>
            <div class="text">全部信息</div>
          </el-radio>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleExport">确 定</el-button>
        <el-button @click="ExportDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
import { getToken } from '@/utils/auth'

// liu--
import { storeList,deleteStore,platformList,updateOpenFlagStore} from '@/apiService/api'
import axios from 'axios'
export default {
  components: {
    Header,
    Left
  },
  data () {
    return {
      loading: false,
      loading2: '',
      userState: 2,
      appId: null,
      platform:'',
      pageInfo: {
        appName: null,
        openFlag: null,
        shopCode: null,
        shopName: null,
        page: 1,
        size: 10
      },
      appArr: [],
      tableData: [],
      multipleSelect: [],
      totalCount: 0,
      ExportDialogVisible: false,
      radioType: '2',
      isSelect: false,
      importHeaders: {
        Accept: 'application/json',
        enctype: 'multipart/form-data',
        Platform: 'web',
        Version: '1.0.0',
        token: getToken()
      },
      // action: process.env.BASE_API + '/flexible/v1/web/user/shop/export'
      // action: 'http://172.16.12.7:8083/' + 'flexible/v1/web/user/shop/import'
      action:this.locationUrl_L + 'flexible/v1/web/user/shop/import',
      subAccountFlag: 0 // 是否开启分账
    }
  },
  mounted () {
    this.subAccountFlag = localStorage.getItem('subAccountFlag')
    this.getAppList()
    this._initData()
  },
  methods: {
    // 获取数据列表--Y
    _initData () {
      let userId=localStorage.getItem('userId');
      this.multipleSelect = []
      let params={
        appName:this.pageInfo.appName,  //公众号名称
        distributorId:userId,  //分销商id
        shopCode:this.pageInfo.shopCode,  //门店编码
        shopName:this.pageInfo.shopName, //门店名称
        openFlag:this.pageInfo.openFlag,  //状态 1、启用 0、禁用
        page:this.pageInfo.page,
        size:this.pageInfo.size,
      }
      storeList(params).then(res=>{
         if(res.success){
            this.tableData = res.data.list
            this.totalCount = res.data.total
         }
      })
      
    },
    // 获取公众号列表-y
    getAppList () {
      // type 平台类型：1 公众号 2 小程序,不传查询全部
      platformList({page:1,size:50,type:1 }).then(res => {
        if (res.success) {
          this.appArr = res.data
        }
      })
    },
    // 公众号下拉列表
    handleSelectApp (val) {
      if (val === '') {
        this.appId = null
        this.pageInfo.appName = null
      } else {
        this.appArr.forEach(item => {
          if (item.id === val) {
            this.pageInfo.appName = item.name
            this.appId = item.appId;
            this.platform=item.platform;
          }
        })
      }
      this._initData()
    },
    // 获取状态--Y
    getStatus (val) {
      if (Number(val) === 1) {
        return '开启'
      } else if (Number(val) === 0) {
        return '停用'
      }
    },
    // 获取相反状态-y
    getfStatus (val) {
      if (Number(val) === 0) {
        return '开启'
      } else if (Number(val) === 1) {
        return '停用'
      }
    },
     // 更改状态-y
    onChangeStatus (row) {
      let status = row.openFlag == 1 ? 0 : 1;
      updateOpenFlagStore({id:row.id,openFlag:status}).then(res => {
        if (res.success) {
          this.$message.success('更新成功！')
          this._initData()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 更换状态搜索-Y
    handleSelect (val) {
      this.pageInfo.openFlag = val === '' ? null : val
      this._initData()
    },
    // 清除门店名称-y
    handleClearName () {
      this.pageInfo.shopName = null
      this._initData()
    },
    // 清除门店编码-Y
    handleClearCode () {
      this.pageInfo.shopeCode = null
      this._initData()
    },
    // 搜索-y
    search () {
      this.pageInfo.page = 1
      this._initData()
    },
    
    // 当切换页面时的作用
    handleSelectionChange (val) {
      if (
        val.length === 0 &&
        this.multipleSelect.length !== 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.tableData.forEach((row2) => {
            if (row1.id === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    // 下载二维码--Y
    onDownload (row) {
      var userAgent = navigator.userAgent; // 取得浏览器的 userAgent 字符串
      var isOpera = userAgent.indexOf('Opera') > -1;
      let image = new Image();
      // 解决跨域 Canvas 污染问题
      image.setAttribute('crossOrigin', 'anonymous');
      image.src = row.qrUrl
      image.onload = function () {
        let url = row.qrUrl
        if (
          userAgent.indexOf('Trident') > -1 ||
          (userAgent.indexOf('compatible') > -1 &&
            userAgent.indexOf('MSIE') > -1 &&
            !isOpera)
        ) {
          // 判断是否 Edge/IE 浏览器
          var bstr = atob(url.split(',')[1]);
          var n = bstr.length;
          var u8arr = new Uint8Array(n);
          while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
          }
          // 创建blob对象
          var blob = new Blob([u8arr]);
          // 调用浏览器的方法，调起 IE 的下载流程
          window.navigator.msSaveOrOpenBlob(blob, row.pictureName + '.png');
        } else {
          let a = document.createElement('a'); // 生成一个 a 元素
          let event = new MouseEvent('click'); // 创建一个单击事件
          a.download = row.pictureName || 'photo'; // 设置图片名称
          a.href = url; // 将生成的 URL 设置为 a.href 属性
          a.dispatchEvent(event); // 触发 a 的单击事件
        }
      }
    },
    // 编辑--y
    onEdit (row) {
      this.$router.push({ name: 'StoreAdd', query: { id: row.id } })
    },
    // 删除
    onDelete (row) {
       if (row.id) {
        this.$confirm('此操作将删除已选数据，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          
          deleteStore({id: row.id}).then(res => {
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
    // 导出Excel--Y
    handleExport () {
      let downIds = [];
      // if (this.radioType === '1') {
      //   // 筛选后信息
      //   for (let i = 0; i < this.multipleSelect.length; i++) {
      //     this.multipleSelect[i].status = this.multipleSelect[i].openFlag === '1' ? '开启' : '停用'
      //     downIds.push(this.multipleSelect[i].id);
      //   }
      // } else {
      //   // 全部信息
      //   for (let i = 0; i < this.tableData.length; i++) {
      //     this.tableData[i].status = this.tableData[i].openFlag === '1' ? '开启' : '停用'
      //     downIds.push(this.tableData[i].id);
      //   }
      // }
      // console.log(downIds);
      // if (downIds.length === 0 && this.radioType === '1') {
      //   this.$message({
      //     type: 'error',
      //     message: '未勾选数据'
      //   })
      // } else {
        this.loading2 = this.$loading({
          lock: true,
          text: '文件导出中....',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
       
       let params={idList:downIds}
       let url=this.locationUrl_L + 'flexible/v1/web/user/shop/export';
       axios({
          method: 'post',
          url: url,
          data: params,
          responseType: 'arraybuffer',
          headers: {
            'Content-Type': 'application/json',
            token: getToken()
          }
        }).then((res) => {
          if (res) {
            const content = res.data;
            let blob = new Blob([content], {
              type: "application/ms-excel",
            });
            let url = window.URL.createObjectURL(blob)
            if ('download' in document.createElement('a')) {
              let link = document.createElement('a')
              link.style.display = 'none'
              link.href = url
              link.setAttribute('download', '店铺信息.xls')
              document.body.appendChild(link)
              link.click()
            } else {
              navigator.msSaveBlob(blob, '店铺信息.xls')
            }
            this.loading2.close()
          } else {
            this.$messag.error('导出失败！')
            this.loading2.close()
          }
        })

    },
    // 上传前校验文件类型--Y
    beforeUpload (file) {
      // 上传前配置
      let excelfileExtend = '.xls,.xlsx'; // 设置文件格式
      let fileExtend = file.name
        .substring(file.name.lastIndexOf('.'))
        .toLowerCase();
      if (excelfileExtend.indexOf(fileExtend) <= -1) {
        this.$message.error('只能上传.xls,.xlsx格式');
        return false;
      }
    },
    // 上传中-Y
    onProgress (event, file, fileList) {
      // 上传时的钩子
      this.loading2 = this.$loading({
        lock: true,
        text: '文件上传中....',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
    },
    // 批量上传失败-y
    uploadFail (err, file, fileList) {
      alert();
      // 上传错误
      this.$message.error({
        message: err.msg,
        duration: 3 * 1000,
        onClose: () => {}
      });
      this.loading2.close();
    },
    // 批量上传成功--Y
    uploadSuccess (response, file, fileList) {

      // 上传成功
      if (response.success) {
        this.$message.success({
          message: '上传成功',
          duration: 3 * 1000,
          onClose: () => {}
        });
        this.loading2.close()
        this._initData()
      } else {
        this.$message.error({
          message: response.errMessage,
          duration: 3 * 1000,
          onClose: () => {}
        });
        this.loading2.close()
      }
    },
    // 导出XLS文件模板--Y
    exportXLS () {
      let url=this.locationUrl_L+ 'flexible/v1/web/user/shop/tempDownLoad';
       axios({
          method: 'post',
          url: url,
          // data: params,
          responseType: 'arraybuffer',
          headers: {
            'Content-Type': 'application/json',
            token: getToken()
          }
        }).then((res) => {
          if (res) {
            const content = res.data;
            let blob = new Blob([content], {
              type: "application/ms-excel",
            });
            let url = window.URL.createObjectURL(blob);
            if ("download" in document.createElement("a")) {
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute("download", "店铺模板.xls");
              document.body.appendChild(link);
              link.click();
            } else {
              navigator.msSaveBlob(blob, "店铺模板.xls");
            }
          } else {
            this.$messag.error("导出失败！");
          }
        })
    },
    // 添加店铺-Y
    addStore () {
      this.$router.push({ name: 'StoreAdd' })
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
    // 单选时调用
    select (selection, row) {
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].id === row.id) {
          this.multipleSelect.splice(i, 1);
          d = true;
          break;
        }
      }
      if (!d) {
        this.multipleSelect.push(row);
        this.multipleSelect = this.setArr(this.multipleSelect);
      }
    },
    // 去重
    setArr (arr) {
      const obj = {};
      const temp = [];
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id); // 不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i]);
          obj[arr[i].id + type] = true; // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },
    // 全选时调用
    selectAll (selection) {
      this.isSelect = true;
      if (selection.length === 0) {
        this.tableData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].id === row.id) {
              this.multipleSelect.splice(i, 1);
              break;
            }
          }
        });
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
    },
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
          width:160px;
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
        /deep/ .el-table {
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
      margin:48px auto 65px;
      width:80%;
      text-align: center;
      .raido-group{
        .el-radio{
          display: inline-block;
          margin:0 30px;
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
