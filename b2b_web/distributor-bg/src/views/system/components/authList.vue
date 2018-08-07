<template>
  <div class="auth-list">
    <div class="title">
      <el-checkbox :indeterminate="isAllindeterminate" @change="handleCheckedAll" v-model="checkall">
        {{title}}
      </el-checkbox>
    </div>
    <div class="module-content">
      <el-row class="clearfix">
        <!-- <div> -->
        <!-- 无子节点的权限module,关键在于v-if -->
        <el-checkbox-group v-model="checkedParent" @change="handleChangeParent">
          <el-col
          :md="12" :xl="8"
          v-for="parent in authData"
          v-if="parent.children.length ===0"
          :key="parent.module">
            <el-checkbox :label="parent.module">{{parent.name}}</el-checkbox>
          </el-col>
        </el-checkbox-group>
        <!-- </div> -->
        <!-- 有子节点的权限module,关键在于v-if -->
        <div
          v-for="parent in authData"
          :key="parent.id"
          v-if="parent.children.length != 0">
          <child-node
          :parent="parent"
          :checkedNode="checkedNode"
          @checkedChange="getChildnodeChange"
          ref="child"
          :key="parent.name" />
        </div>
      </el-row>
    </div>
  </div>
</template>
<script type="text/javascript">
import childNode from "./childNode"

export default {
  // authData: module的数组
  props: [
    'authData', // authMap[0-3].auth.actions: it's array made of structured permission module
    'checkedNode', // string[] 全局action\menu名组成的数组，可能是用来
    /** ----------------------------------------------- */
    'title',
    'name'
  ],
  data() {
    return {
      // 直接与视图交互的数据被分为了两类 它们的共同点是都处于authMap树状结构的末端
      checkedParent: [], // string[] 被选取的父节点(没有子节点) 直接与视图交互的数据
      checkedChild: [], // {module: string[], name: string} 被选取的子节点 直接与视图交互的数据
      /** ----------------------------------------------- */
      isAllindeterminate: false,
      count: 0,
      checkall: false,
    }
  },
  components: {
    childNode,
  },
  created() {
    if(this.checkedNode.length != 0) {
      // 筛选出在parentNode和checkedNode的交集
      this.checkedParent = this.parentNode.filter(item => {
        return this.checkedNode.indexOf(item) >= 0
      })
    }
  },
  mounted(){
    this.handleAllindeterminate()
  },
  computed: {
    // 将结构化的authData扁平化 return string[]
    parentNode() {
      let ary = [];
      // 这可以用filter和map实现
      this.authData.forEach(item => {
        if(item.children.length == 0) {
          ary.push(item.module)
        }
      })
      return ary
    },
    // number
    childNodeLength() {
      let length = 0;
      this.authData.forEach(item => {
        if(item.children.length > 0) {
          length += item.children.length
        }
      })
      return length;
    },
    // string[]
    checkedItems() {
      let ary = [];
      this.checkedChild.forEach(item => {
        item.modules.forEach(val => {
          ary.push(val)
        })
      })
      this.checkedParent.forEach(item => {
        ary.push(item)
      })
      return ary;
    }
  },
  methods: {
    handleChangeParent(val) {
      this.handleAllindeterminate()
    },

    handleAllindeterminate() {
      if(this.checkedParent.length == this.parentNode.length && this.count == this.childNodeLength) {
        this.isAllindeterminate = false;
        this.checkall = true;
      } else if(this.checkedParent.length > 0 || this.count > 0) {
        this.isAllindeterminate = true;
      } else {
        this.isAllindeterminate = false;
      }
    },
    getChildnodeChange(val) {
      this.count = 0;
      this.checkedChild.forEach((item, index) => {
        if(item.name == val.name) {
          this.checkedChild.splice(index, 1)
        }
      })
      this.checkedChild.push(val)
      this.checkedChild.forEach(item => {
        this.count += item.modules.length
      })
      this.handleAllindeterminate();
    },
    handleCheckedAll(val) {
      this.checkall = val;
      if(this.$refs.child != undefined){
        this.$refs.child.forEach(item => {
          item.handleCheckedAllChild(val);
        })
      }
      this.checkedParent = val ? this.parentNode : [];
      this.isAllindeterminate = false;
    },
  },
  watch: {
    // val: string[]
    // 触发getActionCheckedChange || getMenuCheckedChange
    checkedItems(val) {
      let obj = {
        title: this.name,
        modules: val
      }
      // 与父组件通信,只此一处
      this.$emit('checkedChange', obj) 
    },
  }
}

</script>
<style lang="scss" scoped>
.auth-list {
  .title {
    padding: 0 2%;
    border-bottom: 1px solid $bg;
  }
  .module-content {
    padding: 0 2%;
    height: 250px;
    overflow: auto;
  }
}

</style>
