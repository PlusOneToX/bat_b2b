<template>
  <div class="user-edit" >
		<el-form :model="formData" status-icon :rules="rules" label-width='20%' label-position="right" ref="formData" v-loading="loading">
			<el-row >
				<el-col :span="15">
					<el-col :span="24">
						<div class="tip-left">
							<p><code>分账设置</code></p>
						</div>
                        <el-form-item label="分账时效" prop="subAccountAdminConfigCmd.agingType">
                            <el-radio-group v-model="formData.subAccountAdminConfigCmd.agingType" :disabled="exaShow">
                                <el-radio :label="1">实时</el-radio>
                                <el-radio :label="2">延时</el-radio>
                            </el-radio-group>
                            <div class="input-time" v-if="formData.subAccountAdminConfigCmd.agingType===2">
                                <el-input onkeyup="value=value.replace(/[^\d]/g,'')"  v-model="formData.subAccountAdminConfigCmd.delayTime" size="mini" placeholder="0"></el-input>
                                <span>小时</span>
                            </div>
                        </el-form-item>
                        <el-form-item label="分账等级">
                            <el-button class="mini-search-btn" @click="handlerAdd" :disabled="exaShow">添加等级</el-button>
                            <el-button class="mini-search-btn" @click="handleClear" :disabled="exaShow">重置</el-button>
                            <div class="place_holder">(保存后，就只能增加等级不允许删除，如需要删除请重置，重置后所有分账配置需要重新设置)</div>
                        </el-form-item>
                       
                        <!----addItem-->
                        <el-form-item v-if="accountArr.length>0" >
                        <!-- <div v-if="accountArr.length>0">   -->
                            <!-- <el-table class="tableCenter" :data="accountArr" header-row-class-name="header-row" border>
                                <el-table-column align="center" label="等级" prop="id" :min-width="60"></el-table-column>
                                <el-table-column align="center" label="等级名称" prop="name" :min-width="120"></el-table-column>
                            </el-table> -->
                            <table class="productData">
                                  <tr>
                                    <th>等级</th>
                                    <th>等级名称</th>
                                </tr>
                                <tr v-for="(item, index) in accountArr" :key="index" :vlaue="index + 1">
                                    <td>
                                        <el-form-item >
                                            <span>{{index+1}}</span>
                                        </el-form-item>
                                    </td>
                                    <td>
                                        <el-form-item >
                                            <el-input type="text" v-model="item.name" size="mini" width="120" :disabled="item.disabled"></el-input>
                                        </el-form-item>
                                    </td>
                                </tr>
                            </table>
                            <span>等级数字越小，等级越低，等级1是直接和店铺关联的</span>
                        </el-form-item>
                        <!-- </div>   -->
                     </el-col>
                </el-col>
            </el-row>
        </el-form>
	</div>
</template>

<script>
import eventBus from '@/views/order/eventBus'
export default {
    props: ['basicMessage', 'exaShow', 'checkMsg', 'node'],
    data() {
        return{
            loading: false,
            accountArr: [], // 分账等级
            addItem: false,
            input: '',
            formData: {
                subAccountAdminConfigCmd: {
                    agingType: 1,
                    delayTime: 0,
                    levelNameList: []
                }
            },
            rules: {
                subAccountAdminConfigCmd: {
                    agingType: [
                        { required: true, message: '分账时效', trigger: 'change' }
                    ]
                }
            }
        }
    },
    methods: {
        // ======== 操作 ========
        handleSubmit(formData) { // 必填验证
            this.$refs['formData'].validate(valid => {
                // eslint-disable-next-line no-empty
                if (valid) {
                    if(this.accountArr !== undefined){
                        if(this.accountArr.length === 0){
                            this.$message({
                            message: '请添加分账等级',
                            type: 'error',
                            duration: 3 * 1000,
                            onClose: () => {}
                            })
                            valid = false
                        } else {
                            this.accountArr.forEach(item => {
                                console.log('item==', item);
                                if (item.name === '' || item.name === null || item.name === undefined) {
                                    this.$message({
                                    message: '请填写等级名称',
                                    type: 'error',
                                    duration: 3 * 1000,
                                    onClose: () => {}
                                    })
                                    valid = false
                                }
                            })
                        }
                        if (valid) {
                            this.formData.subAccountAdminConfigCmd.levelNameList = []
                            this.accountArr.forEach(elem => {
                                this.formData.subAccountAdminConfigCmd.levelNameList.push(elem.name)
                            })
                        }
                    }
                } else {
                    this.$message({
                        message: '请先将店铺分账资料补充完整',
                        type: 'error',
                        duration: 3 * 1000,
                        onClose: () => {}
                    })
                }
                eventBus.$emit('subSurely', { valid: valid })
            })
        },
        handlerAdd() { // 添加分账等级
            this.addItem = true
            var addManObject = {}
            this.accountArr.push(addManObject)
        },
        handleClear() { // 重置
            this.addItem = false
            this.accountArr = []
            this.formData.subAccountAdminConfigCmd.levelNameList = []
        }
    },
    watch: {
        basicMessage(val) {
            if (val.subAccountAdminConfigCmd) {
                this.formData.subAccountAdminConfigCmd = val.subAccountAdminConfigCmd
                if (this.formData.subAccountAdminConfigCmd.levelNameList.length > 0) {
                    this.addItem = true
                    this.accountArr = []
                    this.formData.subAccountAdminConfigCmd.levelNameList.forEach((item, index) => {
                        this.accountArr.push({
                            name: item,
                            disabled: true
                        })
                    })
                }
            }
        }
    }
}
</script>

<style rel="stylesheet/scss" lang="scss">
 @import '../../scss/permissions.scss' ;
 .tip-left{
     margin-bottom:40px
 }
 .input-time{
    display: inline-block;
     margin-left:20px;
     .el-input{
        display: inline-block;
        width:100px;
        margin-right: 10px;
        .el-input__suffix{
            display: none;
        }
     }
 }
 .productData{
     width:50%
 }
</style>