<template>
	<div class="check-detail">
		<header>
		  <h4>商品上下架审批单</h4>
			<el-button class="btn-home" icon="el-icon-d-arrow-left" @click="onCancel">
				返回商品审批列表
			</el-button>
		</header>
		
		<div class="content" v-loading="loading">
			<div>
				<div class="box-has-info">
					<div class="half-width">
						<el-form ref="recordDetail">
							<el-form-item label="审批单号："> {{checkData.id}} </el-form-item>
							<el-form-item label="审批类型："> {{formatType(checkData.subExt1)}} </el-form-item>
							
						</el-form>
					</div>
					<div class="half-width">
						<el-form ref="recordDetail">
							<el-form-item label="发起时间："> {{timeFormat(0,0,checkData.createTime)}} </el-form-item>
							<el-form-item label="审批状态："> {{formatStatus(0,0,checkData.status)}} </el-form-item>
						</el-form>
					</div>
				</div>
				<el-table :data="goodsData" border header-row-class-name="header-row" style="text-align:center;">
					<el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
					<el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
					<el-table-column align="center" label="商品分类" prop="categoryName"></el-table-column>
					<el-table-column align="center" label="商品品牌" prop="brandName"></el-table-column>
					<el-table-column align="center" label="操作" :min-width="80">
						<template slot-scope="scope">
							<el-button type="primary" size="mini" @click="previewCheck(scope.row)">查看</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<div style="margin-right: 20px;margin-bottom: 30px;" >
          		<check-procedure :flows="checkData.flows" v-if="checkData.flows.length>0"></check-procedure>
			</div>
			<el-form>
				<el-form-item label="审批备注：" v-if="checkStatus">
					<el-input type="textarea" v-model="remark" style="width: 700px;" :rows="3"></el-input>
				</el-form-item>
			</el-form>
			<div v-if="checkStatus" style="margin-left: 45%; margin-top:30px;margin-bottom:30px">
				<el-button class="mini-search-btn" type="success" @click="submit(2)" size="mini">同意</el-button>
				<el-button  @click="submit(3)" size="mini">拒绝</el-button>
			</div>
			<el-table :data="flows" border header-row-class-name="header-row" class="flows-table" style="text-align:center;">
				<el-table-column align="center" label="审批人" prop="checkUserName"></el-table-column>
				<el-table-column align="center" label="审批时间" prop="checkTime" :formatter="timeFormat"></el-table-column>
				<el-table-column align="center" label="审批状态" prop="checkStatus" :formatter="formatStatus"></el-table-column>
				<el-table-column align="center" label="备注" prop="remark"></el-table-column>
			</el-table>
		</div>
	</div>
</template>
<script>
import PageHeader from "@/components/PageHeader"
import { timeFormat } from "@/utils/timeFormat"
import { getIdsGoods } from "@/views/goods/goodslist/data/goodslistManage.js" // ids获取商品
import checkProcedure from "@/components/checkProcedure"
import store from '@/store'
export default {
	data() {
    return {
      checkData: {
        flows: []
      },
      goodsData: [],
      flows: [],
      remark: '',
      checkStatus:true,
      loading: false,
    }
	},
	components: { PageHeader, checkProcedure },
	computed: {
		// checkedData(){
		// 	return this.$route.params.status
		// },
		/** 是否超级管理员 */
		isSuperAdmin() {
		return this.$store.getters.userinfo.adminType === 1
		},
		// 引入Vuex用户权限
		hasPermis() {
			return this.$store.getters.hasPermis
		},
		// 由什么申请页面进来
		checkTypeNum() {
			return this.$route.params.checkType
		}
	},
	created() {
    if(this.$route.params.id) {
      this.loading = true
      this.getDetail()
    }
	},
	methods: {
    getDetail () { //..审批详情数据
      this.$api.get(this, 'admin/u/p/goods/check/detail', { id: this.$route.params.id }).then(res => {
        if(res.code == 0) {
          res.goods.check.flows.forEach(item =>{
            item.checkUserName = ''
          })
          this.checkData = res.goods.check
          let ids = res.goods.goodsIds.join(',')
          let userIds = [];
          this.checkStatus = false
          if(this.checkData.status === 1){ //..是否轮到自己审批
            for(let i = 1;i<res.goods.check.flows.length;i++){
              if(res.goods.check.flows[i].checkStatus === 1 && res.goods.check.flows[i].checkUser === store.getters.userinfo.id){
                this.checkStatus = true
              }
            }
          }
          res.goods.check.flows.forEach(item => {
            userIds.push(item.checkUser);
          })
          this.$api.get(this, 'admin/u/po/admin/ids', { ids: userIds.join(',') }).then(result => {
            if(result.code == 0) {
              res.goods.check.flows.forEach(item => {
                result.admins.forEach(val => {
                  if(val.id == item.checkUser) {
                    item.checkUserName = val.name;
                  }
                })
              })
              this.flows = res.goods.check.flows
            }
          })
          
          getIdsGoods(this, { ids: ids }).then(result => { //..ids获取商品
            this.goodsData = result.goods;
          })
        }
        res.code == 0 ? this.loading = false : this.loading = false
      })
    },
		previewCheck(row){ //..查看操作
			this.$router.push({name:'addgoods',query:{id:row.id, saleStatus: row.saleStatus}})
		},
		
		onCancel() { //..返回操作
			this.$router.go(-1)
		},
			
    timeFormat(row, col, val) { // 时间戳
      return timeFormat(val)
    },
			
    formatType(type){ //..申请类型
      switch(type) {
        case 1:
          return '下架申请';
          break;
        case 3:
          return '上架申请';
          break;
        default:
          return '-'
      }
    },
		
    formatStatus(row, col, type) { //..审核状态
      switch(type) {
        case 0:
          return "待审批";
          break;
        case 1:
          return '审批中';
          break;
        case 2:
          return '审批通过';
          break;
        case 3:
          return '审批拒绝';
          break;
        case 5:
          return '发起审批';
          break;
        default:
          return '-'
      }
    },
			
    submit(type) { //..同意 || 拒绝，按钮
      this.$confirm('确定审批通过/审批拒绝?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$api.put(this, 'admin/u/p/goods/check', {
        id: this.$route.params.id,
        checkStatus: type,
        remark: this.remark
        }).then(res => {
          if(res.code == 0) {
            this.$message({
              message: '提交成功',
              type: 'success',
              duration: 3 * 1000,
              onClose: () => {
                this.$router.go(-1)
              }
            })
          }else if(res.code != 0) {
            this.getDetail()
          }
        })
      })
    },
	}
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.check-detail {
	background-color: white;
	min-height: 100%;
	.header-row {
		@include table-header-color;
	}
	.content {
		padding: 20px;
	}
	.mark {
		margin-top: 20px;
		div:first-child {
			text-align: center;
			font-weight: 700;
			font-size: 18px;
			margin-bottom: 20px;
		}
	}
	.flows-table {
		margin-top: 20px;
	}
	.check-status{
		margin:10px 0;
	}
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
	}
	h4 {
		display: inline-block;
		font-weight: 350;
		font-size: 16px;
		margin: 0 20px;
	}
	.btn-home{
	float: right;
	padding: 5px;
	margin-top: 8px;
	margin-right: 8px;
	margin-left: 0;
	font-size: 12px;
  }
  .box-has-info{
	overflow: hidden;
	.half-width{
		width: 30%;
		box-sizing: border-box;
		float: left;
	}
  }
}

</style>
