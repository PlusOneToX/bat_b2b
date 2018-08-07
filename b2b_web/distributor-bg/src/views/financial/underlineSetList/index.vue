<template>
	<div class="category-list">
		<header>
			<h4>添加线下账户设置</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="addpuls">
				返回线下账户设置列表
			</el-button>
		</header>
		<el-form :model="formData" :rules="rules" status-icon label-width='20%' label-position="right" ref="formData" v-loading="loading">
			<el-row>
				<el-col :span="15">
					<el-col :span="24" style="margin-top: 40px;">
						<el-form-item label="开户行:" prop="bankName" >
							<el-input v-model="formData.bankName" maxlength="100"/>
						</el-form-item>
						<el-form-item label="开户人姓名:" prop="name">
							<el-input v-model="formData.name" maxlength="100"/>
						</el-form-item>
						<el-form-item label="账号:" prop="cardNo">
							<el-input v-model="formData.cardNo" maxlength="30" @keyup.native="proving"/>
						</el-form-item>
					</el-col>
				</el-col>
				<el-col :span="2" :offset="8">
					<el-button class="mini-search-btn box-btn" @click="editAccount('formData')">保存</el-button>
				</el-col>
			</el-row>
		</el-form>
		
	</div>
</template>

<script>
export default {
  name: "underlineSetList",
  activated() {
    if (this.$route.query.id) {
      this.bankAccountData()
      this.compile = true;
    }else if(!this.$route.query.id){
      this.formData.bankName = "";
      this.formData.name = "";
      this.formData.cardNo = "";
      this.compile = false;
    }
  },
  data() {
		return {
			loading: false,
			formData: {
				bankName: "",
				name: "",
				cardNo: "",
				compile: false,
			},
			rules: {
				bankName: [{ required: true, message: '请输入开户行', trigger: 'blur' }],
				name: [{ required: true, message: '请输入开户人姓名', trigger: 'blur' }],
				cardNo: [{ required: true, message: '请输入账号', trigger: 'blur' }],
			}
		}
  },
  methods: {
    // ======== 操作 ========
    addpuls() { // 返回操作
			this.$router.push({ name: "underlineSet" });
    },

    editAccount(formData) { // 保存操作
			if(this.compile == true) {
				this.$refs['formData'].validate(valid => {
					if(valid) {
						this.amend()
					} else {
						return false
					}
				})
			}else if(this.compile == false) {
				this.$refs['formData'].validate(valid => {
					if(valid) {
						this.addAmend()
					} else {
						return false
					}
				})
			}
			
    },

    amend() {
			if (this.compile == true) { // 保存操作  编辑
			this.$api.put(this, "admin/u/p/bankAccount", {
				id: this.$route.query.id,
				cardNo: this.formData.cardNo, // 卡号
				name: this.formData.name, // 收款账号名
				bankName: this.formData.bankName // 收款银行
				}).then(res => {
					if (res.code == 0) {
						this.$message({
							message: "信息更新成功",
							type: "success",
							duration: 3 * 1000,
						});
						this.$router.push({ name: "underlineSet" });
					}
				});
			}
    },
    
		addAmend() { // 添加
			this.$api.post(this, "admin/u/p/bankAccount", { 
				cardNo: this.formData.cardNo, // 卡号
				name: this.formData.name, // 收款账号名
				bankName: this.formData.bankName // 收款银行
      }).then(res => {
        if (res.code == 0) {
            this.$message({
            message: "添加成功",
            type: "success",
            duration: 3 * 1000,
          });
          this.$router.push({ name: "underlineSet" });
        }
      });
		},
    
    // ======== 验证 ========
    proving() {
			this.formData.cardNo = this.formData.cardNo.replace(/[^\.\d]/g,'');
			this.formData.cardNo = this.formData.cardNo.replace('.','');
		},
		// ======== 数据 ========
		bankAccountData() { // 详情
			this.loading = true;
			this.$api.get(this,'admin/u/p/bankAccount/id',{id: this.$route.query.id}).then(res => {
				this.formData = res.bankAccount
				res.code == 0 ? this.loading = false : this.loading = false
			})
		}
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.category-list {
  background: white;
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
    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
	  }
  }
	.box-btn {
		margin-bottom: 20px;
	}
}
</style>
