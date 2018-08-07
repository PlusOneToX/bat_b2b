<template>
  <el-container>
    <el-main>
      <div class="box-has-info">
        <div class="half-left">
          <el-table  height="300px" :data="specificationData" ref="table" @current-change="handleSelectionSpeChange" highlight-current-row header-row-class-name="header-row" style="text-align:center;">
            <el-table-column align="center" label="规格" prop="name" width="60">
              <template slot-scope="scope">
                <el-radio @change="changeSpeId(scope.row)" style="padding-left:10px" v-model="selectSpeId" :label="scope.row.id">{{""}}</el-radio>
              </template>
            </el-table-column>
            <el-table-column label="" prop="name" align="left">
              <template slot-scope="scope">
                <div style="color: #333;font-size: 14px;font-weight: 500;">
                  {{scope.row.name + (scope.row.remark !== undefined && scope.row.remark !==""?"["+scope.row.remark+"]":"")}}
                </div>
              </template>
            </el-table-column>
            <!-- <el-table-column label="" prop="remark" align="left"></el-table-column> -->
            <!-- <el-table-column label="显示方式" prop="showType" :formatter="formatShowType"></el-table-column> -->
            <!-- <el-table-column label="规格值" prop="values" :formatter="formatValue"></el-table-column> -->
          </el-table>
          <!-- <div class="clearfix">
            <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
          </div> -->
          <el-table height="300px" :data="colorData" ref="table" @current-change="handleSelectionColChange" highlight-current-row header-row-class-name="header-row" style="text-align:center;">
            <el-table-column align="center" label="颜色" prop="name" width="60">
              <template slot-scope="scope">
                <el-radio @change="changeColId(scope.row)" style="padding-left:10px" v-model="selectColId" :label="scope.row.id">{{""}}</el-radio>
              </template>
            </el-table-column>
            <el-table-column  label="" prop="name" align="left">
              <template slot-scope="scope">
                <div style="color: #333;font-size: 14px;font-weight: 500;">
                  {{scope.row.name + (scope.row.remark !== undefined && scope.row.remark !==""?"["+scope.row.remark+"]":"")}}
                </div>
              </template>
            </el-table-column>
            <!-- <el-table-column label="" prop="remark" align="left"></el-table-column> -->
            <!-- <el-table-column label="显示方式" prop="showType" :formatter="formatShowType"></el-table-column> -->
            <!-- <el-table-column label="规格值" prop="values" :formatter="formatValue"></el-table-column> -->
          </el-table>
          <!-- <div class="clearfix">
            <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
          </div> -->
        </div>
        <div class="half-right">
          <div class="speValue">
            <div class="speColValue">
              规格值
            </div>
            <div v-if="selectSpeId !== undefined && selectSpeId !== ''">
              <div style="color: #333;font-size: 14px;font-weight: 500;padding: 10px; padding-left:20px;">
                {{selectSpeData.name + (selectSpeData.remark !== undefined && selectSpeData.remark !==""?"["+selectSpeData.remark+"]":"")}}
              </div>
              <div class="speTag">
                <el-tag
                  :key="tag.id"
                  style="max-height: 600px;position: relative;"
                  size="medium"
                  type='info'
                  v-for="tag in selectSpeData.values"
                  :disable-transitions="false">
                  {{tag.name}}
                </el-tag>
              </div>
            </div>
            <div v-else style="text-align: center;vertical-align: middle; line-height: 240px;color: #929292;font-size: 14px;font-weight: 500;">
              暂无数据，请先选择规格
            </div>
          </div>
          <div class="speValue">
            <div class="speColValue">
              颜色值
            </div>
            <div v-if="selectColId !== undefined && selectColId !== ''">
              <div style="color: #333;font-size: 14px;font-weight: 500;padding: 10px; padding-left:20px;">
                {{selectColData.name + (selectColData.remark !== undefined && selectColData.remark !==""?"["+selectColData.remark+"]":"")}}
              </div>
              <div class="speTag">
                <el-tag
                  :key="tag.id"
                  size="medium"
                  type='info'
                  v-for="tag in selectColData.values"
                  :disable-transitions="false">
                  {{tag.name}}
                </el-tag>
              </div>
            </div>
            <div v-else style="text-align: center; vertical-align: middle; line-height: 240px;color: #929292;font-size: 14px;font-weight: 500;">
              暂无数据，请先选择颜色
            </div>
          </div>
        </div>
      </div>
    </el-main>
    <el-footer>
      <div class="foot-btn" style="text-align: center;">
          <el-button class="mini-search-btn" @click="submit">确定</el-button>
          <el-button size="mini" @click="cancel">取消</el-button>
      </div>
    </el-footer>
  </el-container>
  
</template>
<script>
import page from "@/components/pagination"
export default {
  props:['colorId','specificationId'],
  data() {
    return {
      specificationData: [{
        values:[]
      }],
      colorData: [{
        values:[]
      }],
      selectSpeData:{},
      selectColData:{
        name:'',
        remark:'',
        values:[]
      },
      pageInfo: {
        page: 1,
        size: 10000,
        openFlag:1,
        attributeType:1
      },
      total: 0,
      multipleSelectionSpe: [],
      multipleSelectionCol: [],
      selectSpeId:'',
      selectColId:''
    }
  },
  components: {
    page
  },
  created() {
    this.selectSpeId = this.specificationId
    this.selectColId = this.colorId
    this.getTableData()
  },
  watch: {
    specificationId(val){
      this.selectSpeId = val
    },
    colorId(val){
      this.selectColId = val
    }

  },
  methods: {
    changeSpeId(row){//切换规格值
      for(let i=0;i<this.specificationData.length;i++){
        if(this.selectSpeId === this.specificationData[i].id){
          this.selectSpeData = this.specificationData[i]
          break
        }
      }
    },
    changeColId(row){//切换颜色值
      for(let i=0;i<this.colorData.length;i++){
        if(this.selectColId === this.colorData[i].id){
          this.selectColData = this.colorData[i]
          break
        }
      }
    },
    getTableData() {
      this.pageInfo.attributeType = 1
      this.$http.getAttributePoList(this, this.pageInfo).then(res => {  
        if(res.success) {
          this.specificationData = res.data.list;
          if(this.selectSpeId !== undefined && this.selectSpeId !== ''){
            for(let i=0;i<this.specificationData.length;i++){
              if(this.selectSpeId === this.specificationData[i].id){
                this.selectSpeData = this.specificationData[i]
                this.$http.getAllvalueList(this, {id: this.specificationData[i].id}).then(res2 => {
                  if (res.success) {
                    this.multipleSelectionSpe.values = res2.data
                    this.specificationData.values = res2.data
                    this.selectSpeData = {
                      name: this.specificationData[i].name,
                      remark: this.specificationData[i].description,
                      values: res2.data
                    }
                  }
                })
                break
              }
            }
            
          }
        }
        this.pageInfo.attributeType = 2
        this.$http.getAttributePoList(this, this.pageInfo).then(res => {    
          if(res.success) {
            this.colorData = res.data.list;
            if(this.selectColId !== undefined && this.selectColId !== ''){
              for(let i=0;i<this.colorData.length;i++){
                if(this.selectColId === this.colorData[i].id){
                  this.selectColData = this.colorData[i]
                  this.$http.getAllvalueList(this, {id: this.colorData[i].id}).then(res2 => {
                    if (res2.success) {
                      this.multipleSelectionCol.values = res2.data
                      this.selectColData = {
                        name: this.colorData[i].name,
                        remark: this.colorData[i].description,
                        values: res2.data
                      }
                    }
                  })
                  break
                }
              }
            }
          }
        })
      })
    },
    // formatShowType(row, col, val) {
    //   switch(val) {
    //     case 1:
    //       return '文字';
    //       break;
    //     case 2:
    //       return '图片';
    //       break;

    //   }
    // },
    formatValue(row, col, val) {
      let ary = [];
      row.values.forEach(item => {
        ary.push(item.name)
      })
      return ary.join(',')
    },
    sizeChange(size) {
      this.pageInfo.size = size;

    },
    currentChange(page) {
      this.pageInfo.page = page;

    },
    handleSelectionSpeChange(val) {
       this.multipleSelectionSpe = val;
       this.$http.getAllvalueList(this, {id: val.id}).then(res => {
        if (res.success) {
          this.multipleSelectionSpe.values = res.data
          this.specificationData.values = res.data
          this.selectSpeData = {
            name: val.name,
            remark: val.description,
            values: res.data
          }
        }
      })
    },
    handleSelectionColChange(val) {
      this.multipleSelectionCol = val;
      this.$http.getAllvalueList(this, {id: val.id}).then(res => {
        if (res.success) {
           this.multipleSelectionCol.values = res.data
          this.selectColData = {
            name: val.name,
            remark: val.description,
            values: res.data
          }
        }
      })
    },

    submit() {
      this.$emit('submit', this.multipleSelectionSpe,this.multipleSelectionCol)
    },
    cancel() {
      this.selectSpeId = this.specificationId
      this.selectColId = this.colorId
      this.$emit('cancel')
    }
  }
}

</script>
<style lang="scss" scoped>
.header-row {
  @include table-header-color;
}
.foot-btn {
  margin-bottom: 20px;
}

.box-has-info{
		overflow: hidden;
		.half-left{
      border-left: 1px solid #ebeef5;
      border-right: 1px solid #ebeef5;
      // border-bottom: 1px solid #ebeef5;
			width: 50%;
			float: left;
		}
		.half-right{
      width: 50%;
      padding-left: 10px;
			float: left;
    }
    
}
span.el-radio__label{
  padding-left: 10px !important;
}
.el-tag{
  margin-bottom: 10px;
  margin-right: 10px;
}
.speTag{
  overflow-x:hidden;
  overflow:auto;
  // position: relative;
  max-height: 230px;
  padding-left:20px;
}
.speValue{
  height: 300px;
  border-left: 1px solid #ebeef5;
  border-right: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}
.speColValue{
  line-height: 23px;
  font-size: 14px;
  color: #333;
  border-bottom: 1px solid #ebeef5;
  font-weight: 500;
  background-color: #e7fafb;
  padding-bottom: 5px;
  padding-top: 5px;
  padding-left: 15px;
}
</style>
