<template>
    <div class="add-account">
        <page-header title="对账"></page-header>
        <el-row class="info">
            <el-col :span="24" class="title">基本信息</el-col>
            <div class="info-content">
                <el-col :xl="{span:5,offset:5}" :md="{span:7,offset:3}">
                    <el-form label-width="150px" size="mini">
                        <el-form-item label="对账单号：">{{distributorInfo.id}}</el-form-item>
                        <el-form-item label="分销商用户名：">{{distributorInfo.name}}</el-form-item>
                        <el-form-item label="公司名：">{{distributorInfo.companyName}}</el-form-item>
                        <el-form-item label="手机：">{{distributorInfo.mobile}}</el-form-item>
                         <el-form-item label="电话：">{{distributorInfo.phone}}</el-form-item>
                    </el-form>
                </el-col>
                <el-col :xl="{span:5,offset:4}" :md="{span:7,offset:4}">
                    <el-form label-width="150px" size="mini">
                        <el-form-item label="对账时间：">{{timeFormatter(0,0,distributorInfo.startTime)}}</el-form-item>
                        <el-form-item label="对账状态：">{{ payStatusFormatter(distributorInfo.status)}}</el-form-item>
                        <el-form-item label="姓名：">{{distributorInfo.registerName}}</el-form-item>
                        <el-form-item label="支付方式：">{{paymentFormatter(0,0,distributorInfo.payType)}}</el-form-item>

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
            <el-table :data="deliverList" border header-row-class-name="header-row">
                <el-table-column width="200" align="center" label="发货单号" prop="deliverOrderId"></el-table-column>
                <el-table-column width="200" align="center" label="商品编号" prop="goodsNo"></el-table-column>
                <el-table-column width="200" align="center" label="商品名称" prop="goodsName"></el-table-column>
                <el-table-column width="200" align="center" label="存货编码" prop="itemCode"></el-table-column>
                <el-table-column width="200" align="center" label="订单号" prop="orderId"></el-table-column>
                <el-table-column width="200" align="center" label="下单时间" prop="orderTime" :formatter="timeFormatter"></el-table-column>
                <el-table-column width="200" align="center" label="发货时间" prop="deliverTime" :formatter="timeFormatter"></el-table-column>
                <!-- <el-table-column width="200" align="center" label="支付方式" prop="orderPayment" :formatter="paymentFormatter"></el-table-column> -->
                <!-- <el-table-column width="200" align="center" label="付款状态" prop="orderStatus" :formatter="statusFormatter"></el-table-column> -->
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
            <el-table :data="returnList" border header-row-class-name="header-row">
                <el-table-column width="200" align="center" label="退货单号" prop="returnOrderId"></el-table-column>
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
                <el-button type="primary" @click="handleOrder" v-if="distributorInfo.status==5">取消对账</el-button>
            </el-col>
            <el-col :span="2" :offset="2">
                <el-button @click="cancel">返回</el-button>
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
        this.getDistributorInfo()
        // this.getDeliverList()
        // this.getReturnList()

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
        payStatusFormatter(v) {
            switch(v) {
                case 1:
                    return '对账中';
                    break;
                case 2:
                    return '已拒绝';
                    break;
                case 3:
                    return '折扣申请';
                    break;
                case 4:
                    return '已取消';
                    break;
                case 5:
                    return '已完成';
                    break;
                default:
                    return '-'
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
        handleOrder() {
            this.$api.put(this,'admin/u/p/bill/status',{id:this.$route.query.id,status:4,remark:this.remark}).then(res=>{
                if(res.code==0)
                {
                    this.$message.success({
                        message: '取消成功',
                        duration: 3 * 1000,
                        onClose: () => {
                            
                        }
                    })
                    this.$router.push({name:'success',query:{pathName:{name:'对账列表',link:'account'}}})
                }
            })
        },
        cancel() {
            this.$router.push({ name: 'account' })
        },
        getDistributorInfo() {
            this.$api.get(this, 'admin/u/p/bill', { id: this.$route.query.id }).then(res => {
                this.$api.get(this, 'admin/u/p/distributor/cooperating', { id: res.order.distributorId }).then(result => {
                // this.$http.distributorDetail(this, { id: res.order.distributorId }).then(result => {
                    if (result.success) {
                        let data = result.distributor;
                        // let data = result.data.list;
                        res.order.name = data.name;
                        res.order.companyName = data.companyName
                        res.order.registerName = data.registerName;
                        res.order.mobile = data.mobile;
                        res.order.phone = data.phone;
                        this.distributorInfo = res.order
                        this.deliverList=res.delivers
                        this.returns=res.returns;
                    }
                })
            })
        },
        // getDeliverList() {
        //     this.$api.get(this, 'admin/u/p/bill/distributor/deliver/list', this.pageInfo).then(res => {
        //         let order = {};
        //         if(res.delivers.length > 0) {
        //             this.$api.get(this, 'admin/u/p/order', { id: res.delivers[0].orderId }).then(result => {
        //                 order = result.order
        //                 res.delivers.forEach(item => {
        //                     item.createTime = order.createTime;
        //                     item.deliverTime = order.distribution.createTime;
        //                     item.orderPayment = order.orderPayment;
        //                     item.orderStatus = order.orderStatus
        //                 })
        //                 this.deliverList = res.delivers;
        //             })


        //         }

        //     })
        // },
        // getReturnList() {
        //     this.$api.get(this, 'admin/u/p/bill/distributor/return/list', this.pageInfo).then(res => {
        //         let order = {};
        //         if(res.returns.length > 0) {
        //             this.$api.get(this, 'admin/u/p/order', { id: res.returns[0].orderId }).then(result => {
        //                 order = result.order
        //                 res.returns.forEach(item => {
        //                     item.createTime = order.createTime;
        //                     item.returnTime = order.distribution.updateTime;
        //                     item.orderPayment = order.orderPayment;
        //                     item.orderStatus = order.orderStatus
        //                 })
        //                 this.returnList = res.returns;
        //             })


        //         }

        //     })
        // }
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
