<template>
  <main class="time-set">
    <header>
      <h4>基本设置</h4>
    </header>
    <section v-loading="loading">
      <el-row class="time-set-syn">
        <el-col :offset="1">
          <h3>库存同步</h3>
        </el-col>
      </el-row>
      <div class="grey-box">
        <el-form label-width="120px">
          <el-form-item label="同步时间" class="grey-box-item">
            <el-input class="grey-box-input" v-model="repertoryTime" placeholder="不超过1000000" 
              type="number" step="1" min="0" max="1000000" size="mini" @keyup.native="proving" />
          </el-form-item>
          <el-form-item class="grey-box-item">
            <span class="place-holder">未填写默认10分钟从ERP同步一次商品库存。</span>
          </el-form-item>
          <el-form-item>
            <el-button class="mini-search-btn" @click="save1">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-row>
        <el-col :offset="1">
          <h3>在途交期</h3>
        </el-col>
      </el-row>
      <div class="grey-box">
        <el-form label-width="120px">
          <el-form-item label="交期时间" style="margin-bottom: 0;">
            <el-input v-model="enRouteTime" placeholder="不超过2000" type="number" step="1" min="0" max="2000" 
              size="mini" style="width:150px" @keyup.native="proving1" />
          </el-form-item>
          <el-form-item style="margin-bottom: 0;">
            <span class="place-holder">未填写默认交期在60天内的都属于在途库存</span>
          </el-form-item>
          <el-form-item>
            <el-button class="mini-search-btn" @click="save2">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </section>
  </main>
</template>

<script>
export default {
  name: 'timeSet',
  activated() {
    this.timeBreak()
  },
  data(){
    return {
      repertoryTime: '', // 库存同步时间
      enRouteTime: '', // 在途交期时间
      loading: false
    }
  },
  methods:{ 
    // ======== 操作 ========
    save1() { // 库存同步保存操作
      this.$http.editWarehouseSetting(this, { type: 1, value: this.repertoryTime }).then(res => {
        if (res.success) {
          this.$message.success({
            message: "保存成功",
            duration: 3 * 1000,
            onClose: () => { }
          });
          this.timeBreak()
        }
      })
    },
    
    save2() { // 在途交期保存操作
      this.$http.editWarehouseSetting(this, { type: 2, value: parseInt(this.enRouteTime) }).then(res => {  
        if (res.success) {
          this.$message.success({
            message: "保存成功",
            duration: 3 * 1000,
            onClose: () => { }
          });
          this.timeBreak()
        }
      })
    },

    // ======== 数据 ========
    timeBreak() { // 仓库设置列表数据
      this.loading = true;
      this.$http.warehouseSettingList(this).then(res => {
        if (res.success) {
          this.repertoryTime = res.data[0] ? res.data[0].value : 0;
          this.enRouteTime = res.data[1] ? res.data[1].value : 0
          this.loading = false
        } else {
          this.loading = false
        }
      })
    },
   
    // ======== 验证 ========
    proving(){ // 输入验证
			this.repertoryTime=this.repertoryTime.replace(/[^\.\d]/g,'');
			this.repertoryTime=this.repertoryTime.replace('.','');
			if(Number(this.repertoryTime) >1000000){
				this.repertoryTime = 1000000
			}
    },
    
    proving1(){ // 输入验证
			this.enRouteTime=this.enRouteTime.replace(/[^\.\d]/g,'');
			this.enRouteTime=this.enRouteTime.replace('.','');
			if(Number(this.enRouteTime) >2000){
				this.enRouteTime = 2000
			}
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  }
  input[type="number"]{
  -moz-appearance: textfield;
  }
  .time-set{
    background-color: white;
    height: 100%;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      h4{
        margin-left: 30px;
        font-weight: 350;
        font-size: 16px;
      }
    }
    .time-set-syn {
      margin-top: 20px;
    }
    .grey-box{
      padding: 10px;
      margin: 10px auto;
      width: 91%;
      background-color: #f5f7fa;
      border-radius: 5px;
      border: 1px solid #dcdcdc;
      .grey-box-item {
        margin-bottom: 0;
        .grey-box-input {
          width:150px;
          line-height: 20px !important;
        }
        .place-holder {
          color: #ccc;
          font-size: 12px;
        }
      }
    }
  }
</style>
