<template>
  <div>
    <div class="parent-node clearfix">
      <el-col :span="24">
        <el-checkbox
        :indeterminate="isChildindeterminate"
        @change="handleCheckedAllChild"
        v-model="checkall">
          {{parent.name}}
        </el-checkbox>
      </el-col>
    </div>
    <div class="child-node clearfix">
      <el-checkbox-group v-model="checkedChild" @change="handleChangeChild">
        <el-col
        :md="12" :xl="8"
        v-for="childnode in parent.children"
        :key="childnode.module">
          <el-checkbox :label="childnode.module">{{childnode.name}}</el-checkbox>
        </el-col>
      </el-checkbox-group>
    </div>
  </div>
</template>
<script>
export default {
  props: ['parent', 'checkedNode'],
  data() {
    return {
      isChildindeterminate: false,
      checkedChild: [],
      checkall: false,
    }
  },
  created() {
    if(this.checkedNode.length != 0) {
      this.checkedChild = this.checkedNode.filter(item => {
        return this.childNode.indexOf(item) >= 0
      })
    }

  },
  computed: {
    childNode() {
      let ary = [];
      this.parent.children.forEach(item => {
        ary.push(item.module)
      })
      return ary
    }
  },
  watch: {
    checkedChild(val) {
      // let count=val.length;
      // this.isChildindeterminate=(count==this.childNode.length||count==0)?false:true;
      // this.checkall=count==this.childNode.length;
      this.handleChangeChild(val)
      let obj = {
        name: this.parent.name,
        modules: this.checkedChild
      }
      this.$emit('checkedChange', obj);
    }
  },
  methods: {
    handleFilter(val) {
      this.childNode.forEach(item => {
        if(item === val) {
          return true
        }
      })
    },
    handleChangeChild(val) {
      let count = val.length;
      this.isChildindeterminate = (count == this.childNode.length || count == 0) ? false : true;
      this.checkall = count == this.childNode.length;

    },
    handleCheckedAllChild(val) {
      this.checkedChild = val ? this.childNode : [];
      this.isChildindeterminate = false;
      this.checkall = val;

    },
  }
}

</script>
<style lang="scss" scoped>
.child-node {
  padding-left: 25px;
}

</style>
