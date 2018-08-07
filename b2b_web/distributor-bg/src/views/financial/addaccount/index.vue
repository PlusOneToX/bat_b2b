<template>
    <div class="add-account">
        <page-header title="对账"></page-header>
        <el-row class="info">
            <el-col :span="24" class="title">基本信息</el-col>
            <div class="info-content">
                <el-col :xl="{span:5,offset:5}" :md="{span:7,offset:3}">
                    <el-form label-width="150px" size="mini">
                        <el-form-item label="分销商用户名：">{{distributorInfo.name}}</el-form-item>
                        <el-form-item label="公司名：">{{distributorInfo.companyName}}</el-form-item>
                        <el-form-item label="手机：">{{distributorInfo.mobile}}</el-form-item>
                    </el-form>
                </el-col>
                <el-col :xl="{span:5,offset:4}" :md="{span:7,offset:4}">
                    <el-form label-width="150px" size="mini">
                        <el-form-item label="姓名：">{{distributorInfo.registerName}}</el-form-item>
                        <el-form-item label="支付方式：">{{distributorInfo.tradeName}}</el-form-item>
                        <el-form-item label="电话：">{{distributorInfo.phone}}</el-form-item>
                    </el-form>
                </el-col>
            </div>
        </el-row>
        <el-row class="deliver">
            <el-col :span="24" class="title">发货对账</el-col>
            <el-row>
                <el-button size="mini">全选</el-button>
                <el-button size="mini">取消全选</el-button>
            </el-row>
            <el-table :data="deliverList" border header-row-class-name="header-row" :summary-method="getSummaries" show-summary>
                <el-table-column width="200" align="center" label="发货单号" prop="deliverGoodsId"></el-table-column>
                <el-table-column width="200" align="center" label="商品编号" prop="goodsNo"></el-table-column>
                <el-table-column width="200" align="center" label="商品名称" prop="goodsName"></el-table-column>
                <el-table-column width="200" align="center" label="存货编码" prop="itemCode"></el-table-column>
                <el-table-column width="200" align="center" label="订单号" prop="orderId"></el-table-column>
                <el-table-column width="200" align="center" label="下单时间" prop="createTime" :formatter="timeFormatter"></el-table-column>
                <el-table-column width="200" align="center" label="发货时间" prop="deliverTime" :formatter="timeFormatter"></el-table-column>
                <el-table-column width="200" align="center" label="支付方式" prop="orderPayment" :formatter="paymentFormatter"></el-table-column>
                <el-table-column width="200" align="center" label="付款状态" prop="orderStatus" :formatter="statusFormatter"></el-table-column>
                <el-table-column width="200" align="center" label="数量" prop="count"></el-table-column>
                <el-table-column width="200" align="center" label="货品等级单价" prop="saleAmount"></el-table-column>
                <el-table-column width="200" align="center" label="折扣率" prop="discountPercentage"></el-table-column>
                <el-table-column width="200" align="center" label="折扣金额" prop="discountAmount"></el-table-column>
                <el-table-column width="200" align="center" label="货品价格" prop="billAmount"></el-table-column>
            </el-table>
        </el-row>
        <el-row class="return">
            <el-col :span="24" class="title">退货对账</el-col>
            <el-row>
                <el-button size="mini">全选</el-button>
                <el-button size="mini">取消全选</el-button>
            </el-row>
            <el-table :data="returnList" border header-row-class-name="header-row" :summary-method="getSummaries" show-summary>
                <el-table-column width="200" align="center" label="退货单号" prop="return_order_id"></el-table-column>
                <el-table-column width="200" align="center" label="商品编号" prop="goodsNo"></el-table-column>
                <el-table-column width="200" align="center" label="商品名称" prop="goodsName"></el-table-column>
                <el-table-column width="200" align="center" label="存货编码" prop="itemCode"></el-table-column>
                <el-table-column width="200" align="center" label="订单号" prop="orderId"></el-table-column>
                <el-table-column width="200" align="center" label="退货时间" prop="returnTime" :formatter="timeFormatter"></el-table-column>
                <el-table-column width="200" align="center" label="数量" prop="count"></el-table-column>
                <el-table-column width="200" align="center" label="货品等级单价" prop="saleAmount"></el-table-column>
                <el-table-column width="200" align="center" label="折扣率" prop="discountPercentage"></el-table-column>
                <el-table-column width="200" align="center" label="折扣金额" prop="discountAmount"></el-table-column>
                <el-table-column width="200" align="center" label="货品价格" prop="billAmount"></el-table-column>
            </el-table>
        </el-row>
        <el-row>
            <el-col :span="24" class="title">操作信息</el-col>
            <el-col :span='24'>
                <el-form label-width="100px">
                    <el-form-item label="备注：">
                        <el-input type="textarea" :rows="5" v-model="remark"></el-input>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="2" :offset="9">
                <el-button type="primary" @click="add">发起对账</el-button>
            </el-col>
            <el-col :span="2" :offset="2">
                <el-button @click="cancel">取消</el-button>
            </el-col>
        </el-row>
        <!-- <el-row>
            <el-table :data="actionList">
                <el-table-column label="操作者"></el-table-column>
                <el-table-column label="操作时间"></el-table-column>
                <el-table-column label="对账状态"></el-table-column>
                <el-table-column label="操作内容"></el-table-column>
                <el-table-column label="备注"></el-table-column>
            </el-table>
        </el-row> -->
    </div>
</template>
<script>
import PageHeader from "@/components/PageHeader"
import { parseTime } from '@/utils/index'
export default {
    data() {
        return {
            distributorInfo: [],
            deliverList: [],
            returnList: [],
            pageInfo: {
                page: 1,
                count: 10000,
                distributorId: '',
                endTime: ''
            },
            remark: '',
            // actionList:[]
        }
    },
    activated() {
        this.pageInfo.distributorId = this.$route.query.id
        this.pageInfo.endTime = this.$route.query.endTime
        this.getDistributorInfo()
        this.getDeliverList()
        this.getReturnList()

    },
    components: {
        PageHeader,
    },
    methods: {
        timeFormatter(row, column, cellValue) { // 时间格式化
            return parseTime(cellValue)
        },
        paymentFormatter(r, c, v) {
            switch(v) {
                case 1:
                    return '支付宝'
                    break;
                case 2:
                    return '微信'
                    break;
                default:
                    return '其他付款方式'
            }
        },
        statusFormatter(r, c, v) {
            switch(v) {
                case 1:
                    return '未付款未发货'
                    break;
                case 2:
                    return '未付款已发货'
                    break;
                case 3:
                    return '已付款未发货'
                    break;
                case 4:
                    return '已付款已发货'
                    break;
                case 5:
                    return '已完成'
                    break;
                case 6:
                    return '退换货'
                    break;
                case 7:
                    return '无效'
                    break;
                default:
                    return '-'
            }
        },
        add() {
            let delivers = this.deliverList.map(v => {
                return { id: v.goodsId }
            })
            let returns = this.returnList.map(v => { return { id: v.goodsId } })
            this.$api.post(this, 'admin/u/p/bill', { distributorId: parseInt(this.$route.query.id), delivers: delivers, returns: returns, remark: this.remark }).then(res => {
                if(res.code == 0) {
                    this.$message.success({
                        message: '添加成功',
                        duration: 3 * 1000,
                        onClose: () => { }
                    })
                    this.$router.push({ name: 'success', query: { pathName: { name: '对账列表', link: 'account' } } })
                }
            })
        },
        cancel() {
            this.$router.push({ name: 'account' })
        },
        getSummaries(param) {
            const { columns, data } = param;
            const sums = [];
            columns.forEach((item, index) => {

                if(index === columns.length - 3) {
                    sums[index] = '总价'
                    return;
                } else if(index < columns.length - 3) {
                    sums[index] = ''
                    return;
                }
                else  {
                    const values = data.map(val => Number(val[item.property]))
                    if(!values.every(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr)
                            if(!isNaN(value)) {
                                return prev + curr
                            } else {
                                return prev
                            }
                        }, 0)
                    } else {
                        sums[index] = "N/A"
                    }
                }

            })
            return sums
        },
        getDistributorInfo() {
            this.$http.distributorDetail(this, { id: this.$route.query.id }).then(res => {
                this.$http.tradePoList(this, {page:1, size:1000, openFlag:1}).then(res => {      
                    let payType = result.settleAccounts.filter(v => {
                        return v.id == res.distributor.trade
                    })
                    res.distributor.tradeName = payType[0];
                })
                this.distributorInfo = res.distributor
            })
        },
        getDeliverList() {
            this.$api.get(this, 'admin/u/p/bill/distributor/deliver/list', this.pageInfo).then(res => {
                let order = {};
                if(res.delivers.length > 0) {
                    this.$api.get(this, 'admin/u/p/order', { id: res.delivers[0].orderId }).then(result => {
                        order = result.order
                        res.delivers.forEach(item => {
                            item.createTime = order.createTime;
                            item.deliverTime = order.distribution.createTime;
                            item.orderPayment = order.orderPayment;
                            item.orderStatus = order.orderStatus
                        })
                        this.deliverList = res.delivers;
                    })


                }

            })
        },
        getReturnList() {
            this.$api.get(this, 'admin/u/p/bill/distributor/return/list', this.pageInfo).then(res => {
                let order = {};
                if(res.returns.length > 0) {
                    this.$api.get(this, 'admin/u/p/order', { id: res.returns[0].orderId }).then(result => {
                        order = result.order
                        res.returns.forEach(item => {
                            item.createTime = order.createTime;
                            item.returnTime = order.distribution.updateTime;
                            item.orderPayment = order.orderPayment;
                            item.orderStatus = order.orderStatus
                        })
                        this.returnList = res.returns;
                    })


                }

            })
        }
    }
}

</script>
<style lang="scss" scoped>
.add-account {
    background-color: white;
    min-height: 100%;
    padding-bottom: 20px;
    .title {
        text-align: center;
        font-size: 18px;
        font-weight: 700;
        margin: 20px 0;
    }
    .el-table {
        margin: 20px 0;
    }
    .header-row {
        @include table-header-color;
    }
}

</style>
