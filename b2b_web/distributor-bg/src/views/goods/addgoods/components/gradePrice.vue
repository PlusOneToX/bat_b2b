<template>
  <div>
    <el-table :data="tableData" border header-row-class-name="header-row" style="text-align: center;">
      <el-table-column align="center" label="分销商等级" prop="name"></el-table-column>
      <el-table-column align="center" label="价格">
        <template slot-scope="scope">
          <i v-show="!(scope.row.value === undefined || scope.row.value === 0)" class="asmd">￥:&nbsp;</i>
          <i v-show="!(scope.row.value === undefined || scope.row.value === 0)">{{(scope.row.value === undefined || scope.row.value === 0)?'':scope.row.value|NumFormat}}</i>
          <!-- <el-input v-model.trim="scope.row.value" type="number" step="0.1" min="0"></el-input> -->
        </template>
      </el-table-column>
      <el-table-column align="center" label="留空默认">
        <!-- <p>{{chosenItem.salePrice|NumFormat}}</p> -->
        <template slot-scope="scope" >
          <i class="asmd">￥:&nbsp;</i>
          {{chosenItem.salePrice|NumFormat}}
          <!-- {{chosenItem.salePrice}} -->
        </template>
      </el-table-column>
    </el-table>
    <el-button class="mini-search-btn" @click.prevent="submit" style="margin:20px 0;margin-left: 48%;">确定</el-button>
  </div>
</template>
<script>
// 组件位置 》 商品管理/添加商品/扩展数据?click1="新增货品"&click2="分销商价"
export default {
  props: ['gprices', 'chosenItem'],
  data() {
    return {
      tableData: [],
      salePrice:''
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    verify1(row) {  // 验证只能输入数字（价格）
			row.value = row.value.replace(/\D/g,'')
		},
    getTableData() {
      // 分销商等级列表
      this.$http.getGradePoList(this, { page: 1, size: 10000, openFlag: 1 }).then(res => {  
        res.data.list.forEach(item => {
          item.value = undefined
          if(this.chosenItem.id != undefined){
            item.itemId = this.chosenItem.id  //留空默认价格
          }else{
            item.itemId = this.chosenItem.itemErpId
          }
        })
        this.tableData = res.data.list
        this.tableData.forEach((item, index) => {
          this.gprices.forEach((v, i) => {
            if (item.id === v.goodsItemGradeId) {
              item.value = v.price
            }
          })
        })
      })
    },
    submit() {
      this.$emit('submit', this.tableData)
    }
  },
  watch: {
    chosenItem(newVal) {
      console.log(newVal);
    },
    gprices(val) {
      this.tableData.forEach((item, index) => {
        item.value = undefined
        if(this.chosenItem.id != undefined){
          item.itemId = this.chosenItem.id  //留空默认价格
        }else{
          item.itemId = this.chosenItem.itemErpId
        }
        val.forEach((v, i) => {
          if(v.goodsItemGradeId == item.id) {
            item.value = v.price
          }
        })
      })
    }
  }
}

</script>
<style lang="scss" scoped>
.header-row {
  @include table-header-color;
}

</style>
