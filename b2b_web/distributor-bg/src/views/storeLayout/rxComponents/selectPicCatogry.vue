<template>
  <div class="check-pic">
    <div class="h-search">
      <el-select
        size="mini"
        v-model="pageInfo.type"
        placeholder="图库类型"
        style="width: 120px"
        clearable
      >
        <el-option label="普通素材" :value="1"></el-option>
        <el-option label="IP素材" :value="2"></el-option>
        <el-option label="模板" :value="3"></el-option>
        <el-option label="贴图" :value="4"></el-option>
      </el-select>
      <div class="search-right">
        <el-input v-model.trim="content" clearable placeholder="请输入分类图片编号/名称" size="mini" class="box-search" ></el-input>
				<button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
      </div>
    </div>
    <el-table
      ref="multipleSelect"
      :data="tableData"
      tooltip-effect="dark"
      row-key="id"
      @select="select"
      @select-all="selectAll"
      style="text-align: center"
      border
      header-row-class-name="header-row"
      @selection-change="handleSelectionChange"
      v-loading="loading"
    >
      <el-table-column
        align="center"
        type="selection"
        with="100"
      ></el-table-column>
      <el-table-column
        align="center"
        label="图片编号"
        prop="id"
      ></el-table-column>
      <el-table-column
        align="center"
        label="图片分类名称"
        prop="name"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="分类图片">
        <template slot-scope="scope" style="text-align: center; width: 60px; height: 60px;">
          <el-image v-if="scope.row.image" style="text-align: center;width: 60px; height: 60px;padding-right: 0px;"
            :src="scope.row.image" fit="contain" :preview-src-list="[scope.row.image]">
          </el-image>
          <div v-else style="text-align: center;line-height: 60px;padding-right: 0px;">-</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="图片类型" prop="type" :formatter="formatPicType"> </el-table-column>
    </el-table>
    <page
      :total="total"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    ></page>
    <el-button
      style="margin-left: 46%; margin-top: 10px"
      class="mini-search-btn"
      @click="handleSubmit"
      >确定</el-button
    >
    <el-button size="mini" style="margin-top: 10px" @click="handleCancel"
      >返回</el-button
    >
  </div>
</template>
<script>
import page from "@/components/pagination";
export default {
  props: ["picCategoryData"],
  data() {
    return {
      pageInfo: {
        page: 1,
        size: 10,
        type: '', // 图片类型
        status: 1,
        content: ''
      },
      content: '',
      tableData: [],
      total: 0,
      multipleSelect: [],
      isSelect: false,
      selected: [],
      loading: false
    };
  },
  components: { page },
  created() {
    this.multipleSelect = [];
    this.multipleSelect = this.multipleSelect.concat(this.picCategoryData);
    this.getTableData();
  },
  activated() {
    this.getTableData();
  },
  methods: {
    // changeContent(val){
		// 	if(val === undefined || val === '' || val === null){
		// 		this.getTableData()
		// 	}
		// },
		onSearch(){ // 搜索操作
      this.pageInfo.content = this.content;
			this.pageInfo.page = 1;
			this.getTableData()
		},
    selectRow() {
      //分销商请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.multipleSelect.clearSelection();
      this.multipleSelect.forEach((row1) => {
        this.tableData.forEach((row2) => {
          if (row1.pictureId === row2.pictureId) {
            this.$refs.multipleSelect.toggleRowSelection(row2);
          }
        });
      });
    },
    getTableData() {
      this.pageInfo.categoryId = this.categoryId?this.categoryId:this.pageInfo.categoryId;
      this.loading = true;
      // 所有最终分类的图片分类
      // pictureList(this, this.pageInfo).then((res) => {
      this.$http.pictureCategoryPoList(this, this.pageInfo).then(res => {
        this.isSelect = false;
        this.selected = [];

        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading = false;
          if (this.multipleSelect && this.multipleSelect.length > 0) {
            this.multipleSelect.forEach((row1) => {
              //重新获取数据时，判断哪些选中了
              this.tableData.forEach((row2) => {
                if (row1.id === row2.id) {
                  this.$refs.multipleSelect.toggleRowSelection(row2);
                  this.selected.push(row2);
                }
              });
            });
          }
        }
      });
    },
    handleSubmit() {
      this.$emit("picSubmit", this.multipleSelect);
    },
    handleCancel() {
      this.multipleSelect = [];
      this.multipleSelect = this.multipleSelect.concat(this.picCategoryData);
      this.$emit("picClose");
      this.selectRow();
    },
    // 上移
    handleUp(index, row) {
      // if(this.goods.length > 1 && index !== 0) {
      //     this.goods = swapItem(this.goods, index, index - 1)　　
      // }
    },
    // 下移
    handleDown(index, row) {
      // if(this.goods.length > 1 && index !== (this.goods.length - 1)) {
      //     this.goods = swapItem(this.goods, index, index + 1)　　
      // }
    },
    select(selection, row) {
      //单选时调用
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].pictureId === row.pictureId) {
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
    selectAll(selection) {
      //全选时调用
      this.isSelect = true;
      if (selection.length === 0) {
        this.tableData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].pictureId === row.pictureId) {
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
    handleSelectionChange(val) {
      //当切换页面时的作用
      if (
        val.length === 0 &&
        this.multipleSelect.length != 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.tableData.forEach((row2) => {
            if (row1.pictureId === row2.pictureId) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    //去重
    setArr(arr) {
      let obj = {};
      let temp = [];
      for (let i = 0; i < arr.length; i++) {
        let type = Object.prototype.toString.call(arr[i].pictureId); //不加类型 分不清 1 '1'
        if (!obj[arr[i].pictureId + type]) {
          temp.push(arr[i]);
          obj[arr[i].pictureId + type] = true; //这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },
    // 分页页数
    sizeChange(size) {
      this.pageInfo.count = size;
    },
    // 分页总数
    currentChange(page) {
      this.pageInfo.page = page;
    },
    formatPicType(row, col, val) {
       switch(val) {
          case 1:
          return "普通素材";
          break;
          case 2:
          return "IP素材";
          break;
          case 3:
          return "模板";
          break;
          case 4:
          return "贴图";
          break;
        }
    }
  },
  watch: {
    pageInfo: {
      handler() {
        this.getTableData();
      },
      deep: true,
    },
    picCategoryData: {
      //请求的分销商数据变化时
      handler() {
        this.multipleSelect = [];
        this.multipleSelect = this.multipleSelect.concat(this.picCategoryData);
        this.selectRow();
      },
      deep: true
    }
  },
};
</script>
<style lang="scss" scoped>
.check-pic {
  background-color: white;
  min-height: 100%;
  .header-row {
    @include table-header-color;
  }
  .foot-btn {
    margin-top: 50px;
    /* margin-bottom:20px; */
  }
  .h-search{
    margin-bottom: 20px;
    .search-right {
			float: right;
			overflow: hidden;
			.box-search{
				width: 215px;
			}
			.box-btn {
				float: right;
				margin-left:5px;
				margin-right:10px;
			}
		}
  }
}
</style>
