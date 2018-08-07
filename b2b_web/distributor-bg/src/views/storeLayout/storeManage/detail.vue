<template>
  <div class="store-detail">
    <header class="header">
      <h4 class="title">新增店铺</h4>
      <el-button
        class="mini-add-btn btn-right"
        icon="el-icon-back"
        @click="handleBack"
        >返回</el-button
      >
    </header>
    <div class="content">
      <el-form
        :model="detailData"
        :rules="rules"
        label-width="20%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="18">
            <el-form-item label="适用范围:" prop="distriData" :key="1">
              <el-button
                :disabled="disabled"
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="distributorShow = true"
                >添加分销商</el-button
              >
              <el-table
                :data="distriData"
                header-row-class-name="header-row"
                border
                row-key="id"
                class="tableCenter"
              >
                <el-table-column
                  label="分销商用户名"
                  align="center"
                  prop="distributorName"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  label="公司名"
                  align="center"
                  prop="companyName"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      :disabled="disabled"
                      class="mini-delete-btn"
                      @click="handleDeleteDistributor(scope.$index)"
                      >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          <!-- <el-form-item label="公众号名称" prop="appId" v-show="!sid">
            <el-select v-model="detailData.appName" placeholder="公众号名称"  @change="handleSelect">
              <el-option
              v-for="item in appidArr"
              :key="item.id"
              :label="item.platformName"
              :value="item.id">
              </el-option>
            </el-select>
            <div class="text" v-if="detailData.appName">{{detailData.appId}}</div>
            <el-button v-if="detailData.appName" class="mini-search-btn box-btn" @click="copyAppId">复制appID</el-button>
          </el-form-item> -->
          <el-form-item label="微信公众号" prop="appName" v-show="distriData.length>0">
            <!-- <el-input style="width:200px" v-model.trim="detailData.appName" :disabled="true"></el-input> -->
            <el-select v-model="detailData.appName" placeholder="微信公众号"  @change="handleSelect"  :disabled="disabled">
              <el-option
              v-for="item in appidArr"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              </el-option>
            </el-select>
            <!-- <div class="text" v-if="detailData.appName">{{detailData.appName}}</div> -->
          </el-form-item>
          <el-form-item label="门店名称" prop="shopName">
            <el-input v-model.trim="detailData.shopName"></el-input>
          </el-form-item>
          <el-form-item label="门店编码" prop="shopCode">
            <el-input v-model.trim="detailData.shopCode" :disabled="disabled"></el-input>
          </el-form-item>
          <el-form-item label="三方二维码规则" prop="extendParam"
              v-show="
                detailData.b2bId === 2529 ||
                (this.distriData &&
                  this.distriData.length > 0 &&
                  Number(this.distriData[0].distributorId) === 2529)
              "
            >
              <el-input
                type="textarea"
                v-model="detailData.extendParam"
                placeholder="请输入三方二维码规则"
              ></el-input>
          </el-form-item>
          <!-- v-if="distriData.length>0 && salesman.length > 0 && subConfigs.length > 0" -->
          <el-form-item label="业务员" prop="salemanName" v-if="subAccountFlag==1">
            <el-select v-model="detailData.salemanName" placeholder="请选择" filterable @change="changeSale">
              <el-option
              v-for="item in salesman"
              :key="item.id"
              :label="item.name"
              :value="item">
              </el-option>
            </el-select>
          </el-form-item>
           <el-form-item label="分账配置" prop="userConfigName" v-if="subAccountFlag==1">
            <el-select v-model="detailData.userConfigName" placeholder="请选择" @change="changeUserconfig">
              <el-option
              v-for="item in subConfigs"
              :key="item.id"
              :label="item.name"
              :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="detailData.remark"  placeholder="不超过200个字" maxlength="200"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="openFlag">
            <el-radio-group v-model="detailData.openFlag" @change="ChangeRadio">
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      </el-form>
      <div class="clearfix footbtn">
        <el-button class="mini-search-btn" @click="handleSubmit('ruleForm')"
          >保存提交</el-button
        >
        <el-button size="mini" @click="handleBack">返回</el-button>
      </div>
    </div>

    <!-- 添加分销商 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel"
      width="80%"
    >
      <select-distributor
        :distributorData="distriData"
        :rxShopSwitch="1"
        ref="selectDistributor"
        @disClose="disClose"
        @disSubmit="disSubmit"
      >
      </select-distributor>
    </el-dialog>
  </div>
</template>

<script>
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
export default {
  data() {
    // 添加分销商
    var validateDistriData = (rule, value, callback) => {
      if (this.distriData && this.distriData.length <= 0) {
        callback(new Error("请选择分销商"));
      } else {
        callback();
      }
    };
    return {
      sid: null,
      distriData: [],
      detailData: {
        distributorId: "",
        distributorName: "",
        distributorCompanyName: "",
        shopName: "",
        shopCode: "",
        extendParam: "",
        remark: "",
        openFlag: 1,
        appId: "",
        appName: "",
        wxPlatformId: "",
        salemanId: undefined, // 分账业务员id
        salemanName: undefined, // 分账业务员名称
        userConfigId: undefined, // 分销商分账配置id
        userConfigName: undefined, // 分销商分账配置名称
      },
      rules: {
        distriData: [
          {
            required: true,
            validator: validateDistriData,
            trigger: "change",
          },
        ],
        shopName: [
          {
            required: true,
            message: "请填写门店名称",
            trigger: "blur",
          },
        ],
        shopCode: [
          {
            required: true,
            message: "请填写门店编码",
            trigger: "blur",
          },
        ],
        appName: [
          {
            required: true,
            message: "请选择微信支付账户",
            trigger: "change",
          },
        ],
      },
      distributorShow: false,
      distributorId: 0,
      disabled: false,
      appidArr: [], // 微信公众号列表
      salesman: [], // 业务员列表
      subConfigs: [], // 分账配置列表
      hasRelative: false, // 是否关联三方二维码规则
      subAccountFlag:0, //是否开启分销
    };
  },
  components: { selectDistributor },
  mounted() {
    this.sid = this.$route.query.id;
    if (this.sid) {
      // 编辑
      this.initData();
    }
  },
  methods: {
    // 获取数据
    initData () {
      this.$http.shopDetail(this, {  
        id: this.sid
      }).then(res => {
        if (res.success) {
          this.detailData = res.data
          this.detailData.openFlag = Number(res.data.openFlag)
          this.distriData.push({
            distributorId: this.detailData.distributorId,
            distributorName: this.detailData.distributorName,
            companyName: this.detailData.distributorCompanyName
          })
           this.getSalesman()
          this.getSubConfigList()
          this.disabled = true
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 获取分账配置列表
    getSubConfigList() {
      this.$http.subConfigListByCondition(this, {id:  this.detailData.distributorId}).then(res => {
        if (res.success) {
          this.subConfigs = res.data ? res.data : []
        }
      }).catch(err => {
        console.log(err);
      })
    },
    // 获取分账业务员
    getSalesman() {
      this.$http.subSalesmanByCondition(this, {
        distributorId: this.detailData.distributorId,
        openFlag: 1
      }).then(res => {
        if (res.success) {
          this.salesman = res.data ? res.data : []
        }
      })
    },
    // 获取分销是否开启分账
    getSubAccountFlag(){
      this.$http.distributorDetail(this,{id:  this.detailData.distributorId}).then(res=>{
          console.log('是否开启分账：',res.data);
          this.subAccountFlag=res.data.extendData.subAccountFlag;
      })
    },
    
    // 选择分销商
    disSubmit(msg) {
      if (msg.length === 0) {
        this.$message({
          type: "warning",
          message: "请选择分销商！",
        });
      } else if (msg.length > 1) {
        this.$message({
          type: "warning",
          message: "只能选择一个分销商！",
        });
      } else {
        this.distriData = msg;
        this.detailData.distributorId = this.distriData[0].distributorId;
        this.detailData.distributorName = this.distriData[0].distributorName;
        this.detailData.distributorCompanyName = this.distriData[0].companyName;
        this.distributorShow = false;
        // 获取appid
        this.getAppid()
        // 获取分账业务员
        this.getSalesman()
        // 获取分账配置
        this.getSubConfigList()
        this.getSubAccountFlag();
      }
    },
    // 删除分销商
    handleDeleteDistributor(index) {
      this.distriData.splice(index, 1);
      this.appidArr = [];
      this.detailData.appId = null;
      this.detailData.appName = null;
      this.detailData.wxPlatformId = null
      this.detailData.appSecret = null
      this.detailData.platform = null
      this.detailData.type = null
    },
    handleSelect(val) {
      this.appidArr.forEach((item) => {
        if (item.id === val) {
          this.detailData.appName = item.name;
          this.detailData.appId = item.appId;
          this.detailData.appSecret = item.appSecret
          this.detailData.type = item.type
          this.detailData.wxPlatformId = item.id
          this.detailData.platform = item.platform
        }
      });
    },
    copyAppId() {
      if (!this.detailData.appId) {
        this.$message.error("请选择公众号名称");
      } else {
        let input = document.createElement("input");
        input.value = this.detailData.appId;
        document.body.appendChild(input);
        input.select();
        input.setSelectionRange(0, input.value.length);
        document.execCommand("Copy");
        document.body.removeChild(input);
        this.$message.success("复制成功");
      }
    },
    // 获取appid
    getAppid() {
      this.appidArr = [];
      this.$http.getWxPlatformList(this, { 
        page:1,
        size:1000,
        distributorId: this.detailData.distributorId,
      }).then((res) => {
        if (res.success) {
          if (res.data.list.length>0) {
            res.data.list.forEach((item) => {
              this.appidArr.push(item);
            });
          } else {
            this.detailData.appId = null;
            this.detailData.appName = null;
            this.detailData.wxPlatformId = null
            this.detailData.appSecret = null
            this.detailData.platform = null
            this.detailData.type = null
            this.$message.error("当前分销商没有绑定微信商户号，请重新选择！");
          }
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 更换状态
    ChangeRadio(val) {
      this.detailData.openFlag = val;
    },
    // 提交
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          if (this.sid) {
            // 编辑
            this.$http.editShop(this, this.detailData).then(res => {    
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "编辑成功",
                });
                this.$router.push({ name: "storeManage" });
              }
            });
          } else {
            // 新增
            this.$http.addShop(this, this.detailData).then(res => {  
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.$router.push({ name: "storeManage" });
              }
            });
          }
        }
      });
    },
    // 选择业务员
    changeSale(row) {
      this.detailData.salemanId = row.id
      this.detailData.salemanName = row.name
    },
    // 选择配置
    changeUserconfig(row) {
      this.detailData.userConfigId = row.id
      this.detailData.userConfigName = row.name
    },
    disClose() {
      this.distributorShow = false;
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    // 返回列表
    handleBack() {
      this.$router.push({ name: "storeManage" });
    },
    // 开启三方二维码规则
    changeRule() {},
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.store-detail {
  .content {
    padding-top: 30px;
    min-width: 1400px;
    .text {
      display: inline-block;
      margin: 0 20px;
    }
    .el-textarea {
      width: 70%;
    }
  }
  .footbtn {
    padding-bottom: 30px;
    text-align: center;
    .box-btn + .box-btn {
      margin-left: 10px;
    }
  }
}
</style>