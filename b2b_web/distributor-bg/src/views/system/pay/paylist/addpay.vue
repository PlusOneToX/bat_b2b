<template>
  <div>
    <div class="add">
      <div class="pay-list">
        <div>
          <header>
            <h4 v-if="!isEdit">添加收款条件</h4>
            <h4 v-else>查看收款条件</h4>
            <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancle">
              返回收款条件列表
            </el-button>
          </header>
        </div>
      </div>
      <div class="addfunction">
        <el-form label-width='10%' label-position="right" ref="formData" :model="formData" :rules="rules">
          <el-form-item label="收款条件ID:" prop="erpSettleAccountNo">
            <el-tooltip content="收款条件ID需同ERP中的收款条件ID一致，如果不一致无法同步信息。" placement="right">
              <el-input style="width: 40%;" v-model="formData.erpSettleAccountNo" maxlength="30"
                        placeholder="不超过30个字"></el-input>
            </el-tooltip>
          </el-form-item>
          <el-form-item label="支付方式名称:" prop="name">
            <el-input style="width: 40%;" v-model="formData.name" maxlength="20" placeholder="不超过20个字"></el-input>
          </el-form-item>
          <el-form-item label="支付方式英文名称:" prop="nameEn">
            <el-input style="width: 40%;" v-model="formData.nameEn" maxlength="50" placeholder="不超过50个字"></el-input>
          </el-form-item>
          <el-form-item label="结算时长:" prop="payWay">
            <el-radio-group v-model="formData.payWay" style="margin-top: 12px;">
              <div>
                <el-radio :label="1" v-model="formData.payWay">立即支付</el-radio>
              </div>
              <div style="margin-top: 15px;">
                <el-radio :label="2" v-model="formData.payWay">期间结算</el-radio>
              </div>
              <div v-show="formData.payWay === 2">
                <el-input v-model="formData.settlingTime" size="mini" class="couseInput" type="number" :min="0"
                          style="width: 120px; margin-left: 25px; margin-top: 10px;">
                  <template slot="append">天</template>
                </el-input>
              </div>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="状态:" prop="openFlag">
            <el-radio-group v-model="formData.openFlag">
              <el-radio :label="1" v-model="formData.openFlag">启用</el-radio>
              <el-radio :label="0" v-model="formData.openFlag">停用</el-radio>
            </el-radio-group>
          </el-form-item>

        </el-form>
        <el-button style="margin-left: 20%; margin-top:30px;" class="mini-search-btn" @click="handleSubmit()">
          保存
        </el-button>
        <el-button style="margin-top:30px;" size="mini" @click="handleCancle">
          返回
        </el-button>
      </div>
    </div>

  </div>
</template>

<script>
    export default {
        name: 'addpay',
        data() {
            return {
                formData: {
                    name: '',
                    nameEn: '',
                    payWay: 1,
                    settlingTime: 0,
                    erpSettleAccountNo: '',
                    openFlag: 1,
                },
                rules: {
                   erpSettleAccountNo: [{
                        required: true,
                        message: '请输入收款条件ID',
                        trigger: 'blur'
                    }],
                    name: [{
                        required: true,
                        message: '请输入收款条件名称',
                        trigger: 'blur'
                    }],
                    nameEn: [{
                        required: true,
                        message: '请输入收款条件英文名称',
                        trigger: 'blur'
                    }],
                },
                isEdit: false
            }
        },
        mounted() {
            this.getParams();
        },
        activated() {
            this.getParams();
        },
        methods: {
            getParams() {
                this.isEdit = false
                if (this.$route.params.id != undefined) {
                    this.$http.tradeDetail(this, {id: this.$route.params.id}).then(res => {
                        if (res.success) {
                            this.isEdit = true
                            this.formData = res.data
                        }
                    })
                } else {
                    this.formData.id = undefined
                    this.formData.name = ''
                    this.formData.nameEn = ''
                    this.formData.payWay = 1
                    this.formData.settlingTime = 0
                    this.formData.erpSettleAccountNo = ""
                    this.formData.openFlag = 1
                }
            },
            handleSubmit() {
              this.$refs['formData'].validate(valid => {
                if (valid) {
                  if (this.formData.id != undefined) {
                    this.$http.editTrade(this, this.formData).then(res => {   
                        if (res.success) {
                            this.$message({
                                message: '保存成功',
                                type: 'success',
                                duration: 3 * 1000,
                                onClose: () => {
                                }
                            })
                            this.loading = false
                            this.handleCancle()
                        }
                    })
                  } else {
                      this.$http.addTrade(this, this.formData).then(res => { 
                          if (res.success) {
                              this.$message({
                                  message: '保存成功',
                                  type: 'success',
                                  duration: 3 * 1000,
                                  onClose: () => {
                                  }
                              })
                              this.loading = false
                              this.handleCancle()
                          }
                      })
                  }
                }
              })
            },
            handleCancle() {
                this.$router.push({name: 'paylist'})
            },
            backpuls() {
                this.$router.push({name: 'paylist'})
            }
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss">
  * {
    box-sizing: border-box;
    padding: 0;
    margin: 0;
  }

  .main {
    background-color: #fff
  }

  .add {
    background-color: #Fff;
    min-height: 100%;
    padding-bottom: 30px;

    .pay-list {
      header {
        color: white;
        height: $lineHight;
        line-height: $lineHight;
        background-color: $lakeBlue;
      }
    }

    .addfunction {
      border-top: 0px;
      background-color: #fff;
      padding: 3%;

      .course {
        margin-bottom: 20px;
      }

      .place-holder {
        color: #bfbfbf;
        font-size: 12px;
      }
    }

    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    }

    .btn-home {
      float: right;
      padding: 5px;

      margin-top: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
</style>
