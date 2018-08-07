<template>
    <div class="parts">
        <el-button style="margin-left: 40px;" class="mini-search-btn" @click="add">添加配件</el-button>
        <div v-if="partGroup.length>0">
            <div class="part-content"  v-for='(item,index) in partGroup' :key='item.id'>
                <p>配件组{{index+1}}</p>
                <part-component :index="index" :partGroup="item" @postData="getData"></part-component>
            </div>
        </div>
        <div v-else class="no-parts">未添加配件，请添加配件</div>
    </div>
</template>
<script>
import partComponent from './partComponent'
export default {
    props:{partGroup:{
        type:Array,
        default:function(){
            return []
        }
    },
    partGroupGoods:{
        type:Array,
        default:function(){
            return []
        }
    }},
    data(){
        return {
            num:[1],
            partGroups:[],
        }
    },
    components:{
        partComponent,
    },
    // computed:{
    //     num(){

    //     }
    // },
    watch:{
        partGroup(val){
            
        },
        partGroupGoods(val){
            this.partGroup.forEach(item=>{
                let ary=[];
                val.forEach(v=>{
                    if(v.partGroupId==item.id)
                    {
                        ary.push(v.partGoodsId)
                    }
                    item.goodsIds=ary;
                })
            })
        }
    },
    methods:{
        add(){
            // this.num.push(this.num[this.num.length-1]+1)
            let obj= {
                partGroupName: '',
                minCount: '',
                maxCount: '',
                preferentialType: 1,
                discount: '',
                discountPrice: '',
                goodsIds: []
            }
            this.partGroup.push(obj)
        },
        getData(msg){
           this.partGroups[msg.index]=msg.data;
        }
    }
}

</script>
<style lang="scss" scoped>
.parts {
    padding:20px 0;
    .part-content {
        margin-top: 20px;
        margin-left: 40px;
        margin-right: 40px;
    }
}
.part-component {
    border: 1px solid #ccc;
    padding: 20px;
    margin-top: 10px;
}
.no-parts{
    border-radius: 5px;
    background-color: #f8f8f8;
    margin-top: 20px;
    margin-left: 40px;
    padding: 10px;
    font-size: 14px;
    margin-right: 40px;
}
</style>
