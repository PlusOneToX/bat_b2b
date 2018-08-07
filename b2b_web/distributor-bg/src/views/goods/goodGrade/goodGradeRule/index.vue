<template>
<div class="good-grade-rule-edit">
	<div class="good-grade-rule">
		<header >
		  <h4>商品等级规则设置</h4>
		</header>
	</div>

  <div v-loading="loading2">
    <el-form label-width='100px' label-position="right" :model="turnoverRule" ref="turnoverRule" class="good-body-rules">
      <el-form-item label="周转规则设置">
        <div class="turnover-content bg-purple-dark">
          <!-- <el-form label-width='180px' label-position="right" :model="turnoverRule" ref="turnoverRule" size="mini" class="turnover-con-one">
            <el-form-item label="是否自动计算商品等级">
              <el-radio-group :disabled="!isEdit" v-model="turnoverRule.autoCalculation" class="turnover-isCon">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="2">否</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item :disabled="!isEdit" label="是否开启人工确认" style="margin-bottom: 0px;">
              <el-radio-group :disabled="!isEdit" v-model="turnoverRule.artificialConfirm" class="turnover-isCon">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="2">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form> -->

          

          <div class="turnover-con-two">
            <div class="turnover-Conone">
              <span class="turnover-Contwo">存销比设置</span>
            </div>
            <div class="turnover-Conthree">
              <span class="turnover-Confour">存销比</span><span class="turnover-Confive">=</span>
            </div>
            <div class="turnover-Consix">
              <span class="turnover-Conseven">X</span><input :disabled="!isEdit" class="turnover-Coneight" v-model="turnoverRule.coefficient" size="mini"/>
            </div>
            <div class= "container">
              <div class="container-top">
                <span class="container-one">(即时库存+预计入库量-锁定数量)</span>
              </div>
              <div class="container-center"></div>
              <div class="container-bottom">
                <span class="container-bottomOne">(最近 <input :disabled="!isEdit" v-model="turnoverRule.latestShipmentsDay" class="container-bottomTwo" size="mini"/> 天合计发货量+锁定数量)</span>
              </div>
            </div>
          </div>
          
          <el-form label-width='180px' label-position="right" :model="turnoverRule" ref="turnoverRule" class="turnover-shelves" size="mini">
            <el-form-item label="是否包含下架商品">
              <el-radio-group :disabled="!isEdit" v-model="turnoverRule.includeLowerGoods" class="turnover-shelvesOne">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="2">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="新品设置">
              <el-radio-group :disabled="!isEdit" v-model="turnoverRule.newGoodsRule" class="turnover-shelvesOne">
                <el-radio :label="1">第一单出库后</el-radio>
                <el-radio :label="2">商品上架后
                  <template>
                    <input :disabled="!isEdit" class="good-bodyRules-input" v-model="turnoverRule.numberOfDays" size="mini"/><span class="good-bodyRules-spanOne"> 天内属于新品</span>
                  </template>
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          
        </div>
      </el-form-item>
    </el-form>

    <el-form label-width='100px' label-position="right"  ref="grades" class="good-bodyRules">
      <el-form-item  v-show="isEdit" label="商品等级设置" style="margin-bottom: 0px;">
        <button  class="mini-search-btn" @click.prevent="addGradeRule">添加规则</button>
      </el-form-item>
      <el-form-item :label="label" style="margin-top: 0px;">
        <div v-for="(grade, index) in gradeRules" :key="grade.id" class="good-grade-content bg-purple-dark">
          <el-form label-width='80px' label-position="right"  ref="grade" class="good-bodyRules-content" size="mini">
            <el-form-item label="规则名称" class="good-bodyRules-level">
              <el-input :disabled="!isEdit" placeholder="请输入规则名称" class="category-input" v-model="grade.name"></el-input>
              <el-button v-show="isEdit" class="mini-delete-btn rule-delete" @click="deleteRule(index)">删除规则</el-button>
            </el-form-item>
            <el-form-item label="选择类目" class="good-bodyRules-level">
              <el-input :disabled="!isEdit" @focus="categoryFocus(index)" @blur="categoryBlur(index)"  readonly placeholder="请选择" class="category-input" v-model="grade.categoryName"></el-input>
              <transition name="el-zoom-in-top">
                <div v-clickoutside="handleClose" class="category-box" v-if="grade.categoryShow" >
                  <el-tree @check-change="categoryChange" :data='categorylist' check-strictly show-checkbox ref="tree" node-key="id" :default-checked-keys="grade.categoryIds" :default-expanded-keys="categoryExpanded[index]"></el-tree>
                </div>
              </transition>
            </el-form-item>
            <el-form-item label="设置等级" class="good-bodyRules-level">
              <button v-show="isEdit" size="mini" class="mini-search-btn good-level-btn" @click.prevent="addGrade(index)">添加商品等级</button>
              <el-table ref="ruleSettings" :data="grade.ruleSettings" border header-row-class-name="header-row" class="goods-level-table tableCenter" max-height="260">
                <el-table-column label="等级名称" align="center" :width="220">
                  <template slot-scope="scope">
                    <el-select :disabled="scope.row.id !== undefined" v-model="scope.row.goodGradeId" placeholder="请选择等级">
                      <el-option
                        v-for="item in goodGrades"
                        :key="item.id"
                        :label="item.gradeName"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </template> 
                </el-table-column>
                
                <el-table-column align="center" label="商品等级规则" :width="450" >
                  <template slot-scope="scope">
                    <div class="tableContainer">
                      <div class="tableLeft">
                        <el-select :disabled="scope.row.id !== undefined" v-model="scope.row.compareType" placeholder="请选择">
                          <el-option label="大于" :value="1" class="tableLeft-span" ></el-option>
                          <el-option label="小于" :value="2" class="tableLeft-span"></el-option>
                          <el-option label="介于" :value="3" class="tableLeft-span"></el-option>
                        </el-select>
                      </div>

                      <div class="tableCenter"></div>

                      <div class="tableRight">
                        <span v-if="scope.row.compareType === 2" class="tableRight-span">存销比
                          <el-select :disabled="scope.row.id !== undefined" v-model="scope.row.lessType" style="width: 60px;">
                            <el-option label="<" :value="1" class="tableRight-span"></el-option>
                            <el-option label="≤" :value="2" class="tableRight-span"></el-option>
                          </el-select>
                          <el-input class="tableRight-input" :disabled="scope.row.id !== undefined" v-model="scope.row.lessThan" type="number" @input="scope.row.lessThan=/^\d+\.?\d{0,2}$/.test(scope.row.lessThan)||scope.row.lessThan == '' ? scope.row.lessThan:Number(scope.row.lessThan.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0" />
                        </span>
                        <span v-if="scope.row.compareType === 1" class="tableRight-span">
                          <el-input class="tableRight-input" :disabled="scope.row.id !== undefined" v-model="scope.row.greaterThan" @input="scope.row.greaterThan=/^\d+\.?\d{0,2}$/.test(scope.row.greaterThan)||scope.row.greaterThan == '' ? scope.row.greaterThan:Number(scope.row.greaterThan.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0" />
                          <el-select :disabled="scope.row.id !== undefined" v-model="scope.row.greaterType" placeholder="请选择" style="width: 60px;">
                            <el-option label="<" :value="1" class="tableRight-span"></el-option>
                            <el-option label="≤" :value="2" class="tableRight-span"></el-option>
                          </el-select>
                          存销比
                        </span>
                        <span v-if="scope.row.compareType === 3" class="tableRight-span">
                          <el-input class="tableRight-input" :disabled="scope.row.id !== undefined" v-model="scope.row.greaterThan" @input="scope.row.greaterThan=/^\d+\.?\d{0,2}$/.test(scope.row.greaterThan)||scope.row.greaterThan == '' ? scope.row.greaterThan:Number(scope.row.greaterThan.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0"  />
                          <el-select :disabled="scope.row.id !== undefined" v-model="scope.row.greaterType" placeholder="请选择" style="width: 60px;">
                            <el-option label="<" :value="1" class="tableRight-span"></el-option>
                            <el-option label="≤" :value="2" class="tableRight-span"></el-option>
                          </el-select>
                          存销比
                          <el-select :disabled="scope.row.id !== undefined" v-model="scope.row.lessType" placeholder="请选择" style="width: 60px;">
                            <el-option label="<" :value="1" class="tableRight-span"></el-option>
                            <el-option label="≤" :value="2" class="tableRight-span"></el-option>
                          </el-select>
                          <el-input class="tableRight-input" :disabled="scope.row.id !== undefined" v-model="scope.row.lessThan" @input="scope.row.lessThan=/^\d+\.?\d{0,2}$/.test(scope.row.lessThan)||scope.row.lessThan == '' ? scope.row.lessThan:Number(scope.row.lessThan.toString().match(/^\d+(?:\.\d{0,1})?/))" min="0" />
                        </span>
                      </div>
                    </div>
                  </template> 
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button :disabled="!isEdit" class="mini-delete-btn" @click="deleteGrade(index,scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>

            </el-form-item>
          </el-form>
        </div>
      </el-form-item>
    </el-form>
    <span style="margin-left: 45%; margin-top:20px;" >
        <el-button v-if="isEdit" class="mini-search-btn" :loading="loading" type="success" @click.prevent="handleSubmit()"> 保存 </el-button>
        <el-button v-else class="mini-search-btn" :loading="loading" type="success" @click.prevent="edit()"> 编辑 </el-button>
        <el-button v-if="isEdit" class="mini-search-btn" :loading="loading" type="success" @click.prevent="cancel()"> 取消 </el-button>
		</span>
  </div>
</div>
</template>
<script type="text/javascript">
import { getGradeListData, getRuleDetail, addGoodGrade, turnoverRule, getProductlineList, getGradeBaseList } from '@/views/goods/goodGrade/goodGradeData.js'
export default {
  name: 'goodGradeRule',
  created() {
    getProductlineList(this).then(res => {
      this.categorylist = res
      this.gradeRules.forEach(item => {
        if (item.categoryIds !== undefined && (item.categoryName === undefined || item.categoryName === '')) {
          item.categoryIds = item.categoryIds instanceof Array ? item.categoryIds : item.categoryIds.split(',')
          const ary = []
          item.categoryIds.forEach(item => {
            for (let i = 0; i < this.categorylist.length; i++) {
              if (Number(item) === this.categorylist[i].id) {
                ary.push(this.categorylist[i].label)
                break
              }
            }
          })
          item.categoryName = ary.join(',')
        }
      })
    })
    getGradeBaseList(this, { status: 1 }).then(res => {
      this.goodGrades = res.goodGrades
    })
    this.getParams()
  },
  data() {
    return {
      deleteGrades: [],
      isEdit: false,
      index: 0,
      label: '商品等级设置',
      deleteRules: [],
      loading: false,
      loading2: false,
      categorylist: [],
      categoryExpanded: [],
      goodGrades: [],
      turnoverRule: {
        autoCalculation: 2, // 是否自动计算商品等级 1 是,2 否
        artificialConfirm: 1, // 是否开启人工确认 1 是,2 否
        includeLowerGoods: 2, // 是否包含下架商品 1 是,2 否
        coefficient: '', // 系数
        latestShipmentsDay: '', // 最近?天发货量
        newGoodsRule: 2,
        numberOfDays: ''
      },
      gradeRules: [{
        name: '',
        categoryIds: [],
        categoryName: '',
        focus: false,
        categoryShow: false,
        ruleSettings: []
      }]
    }
  },
  methods: {
    edit() {
      this.isEdit = true
    },
    cancel() {
      this.isEdit = false
      this.getParams()
    },
    handleClose() {
      if (this.gradeRules[this.index].categoryShow && !this.gradeRules[this.index].focus) {
        this.gradeRules[this.index].categoryShow = false
      }
    },
    categoryChange() {
      const ary1 = []
      const ary2 = []
      this.$refs.tree[0].getCheckedNodes().forEach(item => {
        ary1.push(item.id)
        ary2.push(item.label)
      })
      this.gradeRules[this.index].categoryName = ary2.join(',')
      this.gradeRules[this.index].categoryIds = ary1
    },
    categoryFocus(index) {
      this.gradeRules[index].focus = true
      this.gradeRules[index].categoryShow = true
      this.index = index
    },
    categoryBlur(index) {
      this.gradeRules[index].focus = false
    },
    getParams() { // 详情数据
      this.loading2 = true
      this.categoryExpanded = []
      getRuleDetail(this).then(res => { // 货品等级设置详情【周转规则设置】
        res.code === 0 ? this.loading2 = false : this.loading2 = false
        if (res.turnoverRule !== undefined) {
          this.turnoverRule = res.turnoverRule
        } else {
          this.turnoverRule = {
            autoCalculation: 1,
            artificialConfirm: 1,
            includeLowerGoods: 2,
            coefficient: '',
            latestShipmentsDay: '',
            newGoodsRule: 2,
            numberOfDays: ''
          }
        }
      })
      getGradeListData(this).then(res => { // 货品等级设置详情【商品等级设置】
        if (res.gradeRules !== undefined && res.gradeRules.length > 0) {
          res.gradeRules.forEach(element => {
            // [operationType] 操作类型 1.添加 2.删除 3.修改
            element.operationType = 3
            element.focus = false
            element.categoryShow = false
            element.categoryName = ''
            element.ruleSettings.forEach(item => {
              item.operationType = 3
            })
            if (element.categoryIds !== undefined) {
              element.categoryIds = element.categoryIds instanceof Array ? element.categoryIds : element.categoryIds.split(',')
              const ary = []
              element.categoryIds.forEach(item => {
                for (let i = 0; i < this.categorylist.length; i++) {
                  if (Number(item) === this.categorylist[i].id) {
                    ary.push(this.categorylist[i].label)
                    break
                  }
                }
              })
              element.categoryName = ary.join(',')
            }
          })
          this.gradeRules = res.gradeRules
        } else {
          this.gradeRules = [{
            name: '',
            categoryIds: [],
            categoryName: '',
            focus: false,
            categoryShow: false,
            operationType: 1,
            ruleSettings: []
          }]
        }
      })
    },

    addGrade(index) { // 增加等级
      const obj = {
        operationType: 1,
        compareType: 1,
        gradeName: '',
        greaterType: 2,
        lessType: 1,
        greaterThan: '',
        lessThan: ''
      }
      this.gradeRules[index].ruleSettings.push(obj)
      setTimeout(() => { // 滚动到最后一行
        this.$refs.ruleSettings[index].bodyWrapper.scrollTop = this.$refs.ruleSettings[index].bodyWrapper.scrollHeight
      }, 50)
    },
    addGradeRule() {
      const obj = {
        name: '',
        categoryIds: [],
        categoryName: '',
        operationType: 1,
        focus: false,
        categoryShow: false,
        ruleSettings: []
      }
      this.gradeRules.push(obj)
    },
    deleteRule(index) { // 删除规则
      if (this.gradeRules[index].id !== undefined && this.gradeRules[index].id !== '') {
        this.gradeRules[index].operationType = 2
        this.deleteRules.push(this.gradeRules[index])
      }
      this.gradeRules.splice(index, 1)
    },
    deleteGrade(ruleIndex, gradeIndex) { // 删除等级
      if (this.gradeRules[ruleIndex].ruleSettings[gradeIndex].id !== undefined) {
        this.gradeRules[ruleIndex].ruleSettings[gradeIndex].operationType = 2 // 1.添加 2.删除 3.修改
        this.gradeRules[ruleIndex].ruleSettings[gradeIndex].ruleId = this.gradeRules[ruleIndex].id
        this.deleteGrades.push(this.gradeRules[ruleIndex].ruleSettings[gradeIndex])// 保存删除项
      }
      this.gradeRules[ruleIndex].ruleSettings.splice(gradeIndex, 1)
      // this.$confirm('删除该等级后，无法再对该等级的商品做商品分类折扣活动。确定要删除', '提示', {
      // 	confirmButtonText: '确定',
      // 	cancelButtonText: '取消',
      // 	type: 'warning',
      // 	center: true
      // }).then(() => {
      // })
    },

    handleSubmit() { // 保存操作
      this.loading = true
      if (this.turnoverRule.coefficient === undefined || this.turnoverRule.coefficient === 0 || this.turnoverRule.coefficient === '' || this.turnoverRule.coefficient === '0') {
        this.$message.error('系数不能为空或零！')
        this.loading = false
        return
      }
      if (this.turnoverRule.latestShipmentsDay === undefined || this.turnoverRule.latestShipmentsDay === 0 || this.turnoverRule.latestShipmentsDay === '' || this.turnoverRule.latestShipmentsDay === '0') {
        this.$message.error('最近多少天发货量中的天数不能为空或零！')
        this.loading = false
        return
      }
      if (this.turnoverRule.numberOfDays === undefined || this.turnoverRule.numberOfDays === 0 || this.turnoverRule.numberOfDays === '') {
        this.$message.error('新品设置的天数不能为空或零！')
        this.loading = false
        return
      }
      let isError = false
      this.gradeRules.forEach(item => {
        if (item.ruleSettings == null || item.ruleSettings.length === 0) {
          this.$message.error('"' + item.name + '"' + '规则至少要有一个等级设置')
          this.loading = false
          isError = true
          return
        }
      })
      if (isError) {
        return
      }
      let data = []
      data = data.concat(this.gradeRules)
      data = JSON.parse(JSON.stringify(data))
      data.forEach(item => {
        this.deleteGrades.forEach(grade => {
          if (item.id === grade.ruleId) {
            item.ruleSettings.push(grade)
          }
        })
      })
      data = data.concat(this.deleteRules)
      data = JSON.parse(JSON.stringify(data))
      const addRules = []
      const deleteRules = []
      const changeRules = []
      for (let i = 0; i < data.length; i++) {
        const item = data[i]
        if (item.categoryIds !== undefined) {
          item.categoryIds = item.categoryIds instanceof Array ? item.categoryIds.join(',') : item.categoryIds
        }
        if (item.operationType !== 2) {
          if (item.name === undefined || item.name.trim() === '') {
            this.$message.error('规则名称不能为空！')
            this.loading = false
            return
          }
          if (item.categoryIds === undefined) {
            this.$message.error('选择类目不能为空！')
            this.loading = false
            return
          }
          for (let i = 0; i < item.ruleSettings.length; i++) { // 判断内容是否填写完整
            if (item.ruleSettings[i].goodGradeId === undefined) {
              this.$message.error('选择等级不能为空！')
              this.loading = false
              return
            }
            if (item.ruleSettings[i].compareType === 1 && (item.ruleSettings[i].greaterThan === undefined || item.ruleSettings[i].greaterThan === '')) {
              this.$message.error("\'" + item.name + "\'" + '等级存销比大于或大于等于的值不能为空！')
              this.loading = false
              return
            } else if (item.ruleSettings[i].compareType === 2 && (item.ruleSettings[i].lessThan === undefined || item.ruleSettings[i].lessThan === '')) {
              this.$message.error("\'" + item.name + "\'" + '等级存销比小于或小于等于的值不能为空！')
              this.loading = false
              return
            } else if (item.ruleSettings[i].compareType === 3) {
              if (item.ruleSettings[i].greaterThan === undefined || item.ruleSettings[i].greaterThan === '') {
                this.$message.error("\'" + item.name + "\'" + '等级存销比大于或大于等于的值不能为空！')
                this.loading = false
                return
              }
              if (item.ruleSettings[i].lessThan === undefined || item.ruleSettings[i].lessThan === '') {
                this.$message.error("\'" + item.name + "\'" + '等级存销比小于或小于等于的值不能为空！')
                this.loading = false
                return
              }
            }
          }
        }
        if (item.operationType === 1) { // 添加
          addRules.push(item)
        } else if (item.operationType === 2) { // 删除
          deleteRules.push(item)
        } else { // 修改
          changeRules.push(item)
        }
      }

      return Promise.all([
        turnoverRule(this, this.turnoverRule), // 周转规则设置
        addGoodGrade(this, { addGoodGradeRules: addRules, changeGoodGradeRules: changeRules, deleteGoodGradeRules: deleteRules }).then(res => { // 商品等级设置
          this.loading = false
          if (res.code === 0) {
            this.isEdit = false
            this.getParams()
            this.$message({
              message: '保存成功',
              type: 'success',
              duration: 3 * 1000,
              onClose: () => {
                this.deleteGrades = []
                this.deleteRules = []
              }
            })
          }
        })
      ])
    }
  },
  directives: { // ..事件绑定
    clickoutside: {
      bind: function(el, binding, vnode) {
        function documentHandler(e) {
          if (el.contains(e.target)) { // ..这里判断点击的元素是否是本身，是本身，则返回
            return false
          }
          // if(binding.expression){ //..判断指令中是否绑定了函数
          if (binding.value) {
            binding.value(e) // ..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
          }
        }
        el._vueClickOutside_ = documentHandler // ..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
        document.addEventListener('click', documentHandler)
      },
      unbind: function(el, binding) {
        document.removeEventListener('click', el._vueClickOutside_)
        delete el._vueClickOutside_
      }
    }
  }

}

</script>
<style rel="stylesheet/scss" lang="scss" >
.good-grade-rule-edit {
	  background-color: white;
	  padding-bottom: 30px;
	.good-grade-rule {
		background-color: white;
		padding-bottom: 20px;
		header {
			color: white;
			height: $lineHight;
			line-height: $lineHight;
			background-color: $lakeBlue;
      margin-bottom: 20px;
      h4 {
        display: inline-block;
        font-weight: 350;
        font-size: 16px;
        margin: 0 20px;
      } 
		}
  }
  .el-table .cell div {
      padding-right: 0px !important;
      overflow: visible;
      text-overflow: ellipsis;
  }
  .good-body-rules {
    margin-top:1%;
    margin-left: 3%;
    margin-right: 3%;
    .turnover-content {
      border-radius: 5px;
      width: 70%;
      min-height: 36px;
      .turnover-con-one {
        margin-top: 15px;
        margin-bottom: 0%;
        .turnover-isCon {
          margin-left: 20px;
        }
      }
      .turnover-con-two {
        margin-left: 3px;
        .turnover-Conone {
          width:180px;
          position: absolute;
          margin-top: 15px;
          .turnover-Contwo {
            color: #606266;
            float: right;
            margin-right:15px;
          }
        }
        .turnover-Conthree {
          position: absolute;
          margin-left:197px;
          text-align: center;
          margin-top: 15px;
          .turnover-Confour {
            color: #606266;
            font-weight:bold;
          }
          .turnover-Confive {
            margin-left: 5px;
            color: #606266;
            font-weight:bold;
          }
        }
        .turnover-Consix {
          position: absolute;
          margin-left:505px;
          text-align: center;
          margin-top: 15px;
          .turnover-Conseven {
            color: #606266;
            font-weight:bold;
          }
          .turnover-Coneight {
            font-weight:bold;
            margin-left:10px;
            color: #606266;
            width: 30px;
            font-size: 12px;
            text-align: center;
          }
        }
        .container{
          margin-left: 265px;
          position: relative;
          width: 230px;
          height: 64px;
          .container-top{
            width:100%;
            text-align: center;
            font-size: 14px;
            height:36px;
            .container-one {
              color: #606266;
              font-weight:bold;
            }
          }
          .container-center{
            width: 100%; 
            position: absolute;
            background: #606266;
            height:1px;
          }
          .container-bottom{
            position: absolute;
            text-align: center;
            font-size: 14px;
            width: 100%;
            top: 32px;
            bottom: 0;
            left: 0;
            .container-bottomOne {
              font-weight:bold;
              color: #606266;
              margin-bottom: 5px;
              text-align: center;
            }
            .container-bottomTwo {
              font-weight:bold;
              color: #606266;
              width: 35px;
              font-size: 12px;
              text-align: center;
            }
          }
        }
      }
      .turnover-shelves {
        margin-top: 10px;
        .turnover-shelvesOne {
          margin-left: 20px;
        }
      }
    }
  }
  .good-bodyRules-input {
      margin-left:20px;
      font-weight:bold;
      color: #606266;
      width: 30px;
      font-size: 12px;
      text-align: center;
    }
  .good-bodyRules {
    margin-top:1%;
    margin-left: 3%;
    margin-right: 4%;
    .good-grade-content{
      margin-top: 10px;
      border-radius: 5px;
      width: 95%;
      min-height: 36px;
      .good-bodyRules-content {
        margin-top: 15px;
        margin-bottom: 0%;
        .good-bodyRules-group {
          margin-left: 20px;
          .good-bodyRules-span {
            font-weight:bold;
            color: #606266;
          }
          .good-bodyRules-spanOne {
            font-weight:bold;
            color: #606266;
          }
        }
        .good-bodyRules-level {
          margin-bottom: 0px;
          .good-level-btn {
            margin-left: 10px;
            margin-bottom:15px
          }
          .goods-level-table {
            width: 90%;
            margin-left: 10px;
            margin-bottom:15px
          }
          .tableContainer{
            text-align: center;
            width: 410px;
            height: 100%;
            overflow:hidden;
            display: flex;
            justify-content: flex-start;
            align-items: center;
            white-space:nowrap;
            .tableLeft{
              float:left;
              width: 20%;
              margin-right: 10px;
              position: relative;
              .tableLeft-span {
                font-weight:bold;
                color: #606266;
                width: 80px;  
              }
            }
            .tableCenter{
              bottom: 0;
              width: 1px;
              height: 100%;
              background-color: #ebeef5;
              margin-left: 90px;
              position: absolute;
            }
            .tableRight{
              margin-left: 10px;
              .tableRight-span {
                font-weight:bold;color: #606266;
              }
              .tableRight-input {
                width: 65px;
                font-weight:bold;
                color: #606266;
                text-align: center;
              }
            }
          }
        }
      }
    }
  }
  .bg-purple-dark { // 公用边框样式
    background: #f9fafc;
    border: 1px solid #ccc;
  }
	.el-input__inner{
		font-weight:bold;
		color: #606266;
	}
  .operation{
    margin-bottom: 10px;
    margin-left: 3%;
    margin-top: 10px;
    .btn-add{
      color: #fff;
      background-color: #21b8cb;
      border-radius: 6px;
    }
  }
  .category-input {
    width: 300px;
    margin-left: 10px;
    margin-bottom:10px
  }
  .category-box {
    border: 1px solid #ccc;
    width: 300px;
    margin-left: 10px;
    padding: 10px;
    overflow: auto;
    max-height: 200px;
    background-color: white;
    position: absolute;
    left: 0;
    top: 45px;
    z-index: 99;
    border-radius: 10px;
  }
  .rule-delete{
	   float: right;
     margin-right: 15px;
   }
  .operate{
    position: relative;
    margin-top:20px;
  }
}
</style>
