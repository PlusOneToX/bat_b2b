<template>
  <section class="procedure">
  <div class="title">审批流程</div>

    <div v-for="item in flows" :key="item.id">
      <div class="information">
        <div class="user">
          {{item.checkUserName}}
        </div>
        <div class="check-status">
          {{formatStatus(item.checkStatus)}}
        </div>
				<div class="check-time" v-if="item.checkStatus == 2 || item.checkStatus == 3 || item.checkStatus == 5">
					{{timeFormatter(item.checkTime)}}
				</div>
				<div class="check-remark">
					{{item.remark}}
				</div>
      </div>
      <div class="icon" v-if="flows.indexOf(item) != flows.length - 1">
        <i class="el-icon-arrow-right"></i>
      </div>
    </div>

    <div class="clear-float"></div>
  </section>
</template>

<script>
/*
 * @Author: lijiemin
 * @Date: 2018-05-06 17:15:42
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2018-05-22 14:18:45
 */
import { parseTime } from '@/utils/index'
export default {
  name: '',
  data() {
    return {
      flow: []
    }
  },
  props: ['flows'],
  computed: {

  },
  methods: {
    timeFormatter(cellValue) {
      return parseTime(cellValue)
    },
    formatStatus(type) {
      switch (type) {
        case 0 :
          return '未审批'
          // eslint-disable-next-line no-unreachable
          break
        case 1:
          return '审批中'
          // eslint-disable-next-line no-unreachable
          break
        case 2:
          return '审批通过'
          // eslint-disable-next-line no-unreachable
          break
        case 3:
          return '审批拒绝'
          // eslint-disable-next-line no-unreachable
          break
        case 5:
          return '发起人'
          // eslint-disable-next-line no-unreachable
          break
        default:
          return '-'
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
	@import '../../scss/checkProcedure.scss';
</style>
