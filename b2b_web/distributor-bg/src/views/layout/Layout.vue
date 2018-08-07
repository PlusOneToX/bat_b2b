<template>
  <!-- <div class="app-wrapper" :class="{hideSidebar:!sidebar.opened}">
    <sidebar class="sidebar-container"></sidebar>
    <div class="main-container">
      <navbar></navbar>
      <app-main></app-main>
    </div>
  </div> -->
  <el-container style="height: 100%; flex: none">
    <el-aside style="width: 200px">
      <sidebar></sidebar>
    </el-aside>
    <el-container>
      <el-header height="50">
        <Navbar></Navbar>
      </el-header>
      <tags-view v-if="needTagsView" />
      <el-main>
        <app-main></app-main>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { Navbar, Sidebar, AppMain, TagsView } from "@/views/layout/components";
import { mapState } from "vuex";

export default {
  name: "layout",
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView,
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar;
    },
    ...mapState({
      needTagsView: (state) => state.tagsView,
    })
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-header {
  padding: 0;
}
.el-main {
  background-color: $bg;
  padding: 0;
}
</style>
