<template>
  <div class="part-component">
    <el-form label-width="150px" :model="formData" :rules="rules" ref="formData">
      <el-form-item label="配件组名称" prop="partGroupName">
        <el-input class="part-input" v-model="formData.partGroupName"></el-input>
      </el-form-item>

      <el-form-item label="最小购买量">
        <el-input class="part-input" v-model="formData.minCount"></el-input>
      </el-form-item>

      <el-form-item label="最大购买量">
        <el-input class="part-input" v-model="formData.maxCount"></el-input>
      </el-form-item>

      <el-form-item label="配件优惠">
        <el-radio-group v-model="formData.preferentialType">
          <el-radio :label="1">优惠某个折扣</el-radio>
          <el-radio :label="2">优惠一定金额</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="优惠额度">
        <el-input class="part-input" v-model="discount"></el-input>
        <div class="hint-msg">无优惠额度可不填，优惠9折输入0.9，优惠100元输入100</div>
      </el-form-item>

      <el-form-item label="配件商品">
        <el-button class="mini-search-btn" @click="goodsShow=true">添加配件商品</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :modal-append-to-body="false" :visible="goodsShow" width="80%" :before-close="closeDialog" >
      <select-goods ref="selectGoods" @submit="submit" @cancel="goodsShow=false"></select-goods>
    </el-dialog>
    
    <el-table :data="goods" border header-row-class-name="header-row">
      <el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
      <el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
      <el-table-column align="center" lable="操作">
        <template slot-scope="scope">
          <el-button class="mini-delete-btn" @click="handleDelete(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import selectGoods from "@/views/goods/components/selectGoods";
export default {
  props: ["index", "partGroup"],
  data() {
    return {
      formData: {
        partGroupName: "",
        minCount: "",
        maxCount: "",
        preferentialType: 1,
        discount: "",
        discountPrice: "",
        goodsIds: ""
      },
      rules: {
        partGroupName: [
          {
            required: true,
            message: "请输入配件组名称",
            trigger: "blur"
          }
        ]
      },
      discount: "",
      goodsShow: false,
      goods: []
    };
  },
  components: {
    selectGoods
  },
  created() {
    this.formData = this.partGroup;
    if (this.partGroup.discount) {
      this.discount = this.partGroup.discount;
    } else {
      this.discount = this.partGroup.discountPrice;
    }
    if (this.partGroup.goodsIds.length > 0) {
      let ids = this.partGroup.goodsIds.join(",");
      this.$api.get(this, "admin/u/po/goods/ids", { ids: ids }).then(res => {
        this.goods = res.goods;
      });
    }
  },
  watch: {
    discount(val) {
      if (this.formData.preferentialType == 1) {
        this.formData.discount = val;
      } else if (this.formData.preferentialType == 2) {
        this.formData.discountPrice = val;
      }
    },
    goods: {
      handler() {
        let ary = [];
        this.goods.forEach(item => {
          ary.push(item.id);
        });
        this.formData.goodsIds = ary.join(",");
      },
      deep: true
    },
    formData: {
      handler() {
        let obj = {
          index: this.index,
          data: this.formData
        };
        this.$emit("postData", obj);
      },
      deep: true
    }
  },
  methods: {
    closeDialog() {
      this.goodsShow = false;
    },
    submit(val) {
      this.goods = val;
      this.goodsShow = false;
    },
    handleDelete(index) {
      this.goods.splice(index, 1);
    },
    handleSubmit() {
      this.$refs["formData"].validate(valid => {
        if (valid) {
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.part-component {
  border: 1px solid #ccc;
  padding: 20px;
  margin-top: 10px;
  .part-input {
    width: 30%;
  }
  .hint-msg {
    font-size: 12px;
    color: #ccc;
  }
}
</style>
