<template>
  <div class="approve-list">
    <header>
      <h4>审批配置</h4>
    </header>

    <div v-loading="loading">
      <!-- 商品管理 -->
      <div class="approve-subject">
        <h4 class="approve-subject-h4" style="margin: 14px 0 5px 10px;">商品管理</h4>
        <hr class="approve-subject-hr" />
        <el-container>
          <el-aside width="310px">
            <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">上下架: </h4>
            <el-radio-group v-model="approveCommodity">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
            </el-radio-group>
            <h4 class="approve-subject-h4" v-show="approveCommodity === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
          </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approveCommodity === 1">
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of users" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletes(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
                <!-- <i class="icon"></i> -->
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewOne()">添加用户</el-button>
            </span>
          </el-main>
        </el-container>

        <el-container>
          <el-aside width="320px">
            <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">等级变更: </h4>
            <el-radio-group v-model="approveChangeGade">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
            </el-radio-group>
            <h4 class="approve-subject-h4" v-show="approveChangeGade === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
          </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approveChangeGade === 1">
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userTen" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesTen(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
                <!-- <i class="icon"></i> -->
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewTen()">添加用户</el-button>
            </span>
          </el-main>
        </el-container>
      </div>

      <!-- 分销商管理 -->
      <div class="approve-subject">
        <div>
          <h4 class="approve-subject-h4" style="margin: 14px 0 5px 14px;">分销商管理</h4>
          <hr class="approve-subject-hr" />
          <el-container>
            <el-aside width="320px">
              <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">信息编辑: </h4>
              <el-radio-group v-model="approveDistributor">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
              <h4 class="approve-subject-h4" v-show="approveDistributor === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approveDistributor === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userOne" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesOne(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewTwo()">添加用户</el-button>
            </span>
          </el-main>
        </el-container>
        </div>
      </div>

      <!-- 仓库管理 -->
      <div class="approve-subject" v-show="false">
        <div>
          <h4 class="approve-subject-h4" style="margin: 14px 0 5px 14px;">仓库管理</h4>
          <hr class="approve-subject-hr" />
          <!-- 库存调整 -->
          <el-container>
            <el-aside width="320px">
              <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">库存调整: </h4>
              <el-radio-group v-model="approveWarehouse">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
              <h4 class="approve-subject-h4" v-show="approveWarehouse === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approveWarehouse === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userTwo" :key="item.id" :value="item.name">
                <el-button class="mini-approve-btn">{{item.name}}</el-button>
                <i @click="deletesTwo(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewThree()">添加用户</el-button>
            </span>
          </el-main>
        </el-container>

          <!-- 库存预留 -->
          <el-container>
            <el-aside width="320px">
              <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">库存预留: </h4>
              <el-radio-group v-model="approveWarehouseReserved">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
              <h4 class="approve-subject-h4" v-show="approveWarehouseReserved === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approveWarehouseReserved === 1">
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userThree" :key="item.id" :value="item.name">
                <el-button class="mini-approve-btn">{{item.name}}</el-button>
                <i @click="deletesThres(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewFour">添加用户</el-button>
            </span>
          </el-main>
        </el-container>
        </div>
      </div>

      <!-- 订单管理 -->
      <div class="approve-subject" v-show="false">
        <div>
          <h4 class="approve-subject-h4" style="margin: 14px 0 5px 14px;">订单管理</h4>
          <hr class="approve-subject-hr" />
          <!-- 订单价格 -->
          <el-container>
            <el-aside width="320px">
            <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">订单价格: </h4>
            <el-radio-group v-model="approveOrderPrice">
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
            </el-radio-group>
            <h4 class="approve-subject-h4" v-show="approveOrderPrice === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approveOrderPrice === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userFour" :key="item.id" :value="item.name">
                <el-button class="mini-approve-btn">{{item.name}}</el-button>
                <i @click="deletesFour(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewFive">添加用户</el-button>
            </span>
          </el-main>
        </el-container>

          <!-- 对账折扣 -->
          <el-container>
            <el-aside width="320px">
            <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">对账折扣: </h4>
            <el-radio-group v-model="approvePpromotion">
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
            </el-radio-group>
            <h4 class="approve-subject-h4" v-show="approvePpromotion === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approvePpromotion === 1">
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userFive" :key="item.id" :value="item.name">
                <el-button class="mini-approve-btn">{{item.name}}</el-button>
                <i @click="deletesFive(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewSix">添加用户</el-button>
            </span>
          </el-main>
        </el-container>
        </div>
      </div>

      <!-- 代金券 -->
      <div class="approve-subject">
        <div>
          <h4 class="approve-subject-h4" style="margin: 14px 0 5px 14px;">代金券</h4>
          <hr class="approve-subject-hr" />
          <el-container>
            <el-aside width="320px">
              <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">创建审批: </h4>
              <el-radio-group v-model="approveVoucher">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
              <h4 class="approve-subject-h4" v-show="approveVoucher === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
            <span class="approve-subject-span" v-show="approveVoucher === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userSixteen" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesSixteen(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewSixteen()">添加用户</el-button>
            </span>
          </el-main>
        </el-container>
        </div>
      </div>

      <!-- 活动审批 -->
      <div class="approve-subject">
        <div>
          <h4 class="approve-subject-h4" style="margin: 14px 0 5px 14px;">营销推广</h4>
          <hr class="approve-subject-hr" />
          <el-container>
            <el-aside width="320px">
          <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">促销活动: </h4>
          <el-radio-group v-model="approvePromotion" >
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
          <h4 class="approve-subject-h4" v-show="approvePromotion === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
          <span class="approve-subject-span" v-show="approvePromotion === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userSix" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesSix(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewSeven()">添加用户</el-button>
          </span>
          </el-main>
        </el-container>
         <el-container>
            <el-aside width="320px">
          <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">优惠券: </h4>
          <el-radio-group v-model="approveCoupon" >
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
          <h4 class="approve-subject-h4" v-show="approveCoupon === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
          <span class="approve-subject-span" v-show="approveCoupon === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userFourteen" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesFourteen(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewFourteen()">添加用户</el-button>
          </span>
          </el-main>
        </el-container>
         <el-container>
            <el-aside width="320px">
          <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">拼团秒杀: </h4>
          <el-radio-group v-model="approveGroupBy" >
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
          <h4 class="approve-subject-h4" v-show="approveGroupBy === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
          <span class="approve-subject-span" v-show="approveGroupBy === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userFifteen" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesFifteen(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewFifteen()">添加用户</el-button>
          </span>
          </el-main>
        </el-container>
        <!-- <el-container>
            <el-aside width="350px">
          <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">商品等级折扣: </h4>
          <el-radio-group v-model="approvePolicy" >
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
          <h4 class="approve-subject-h4" v-show="approvePolicy === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
          <span class="approve-subject-span" v-show="approvePolicy === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userEleven" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesEleven(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewEleven()">添加用户</el-button>
          </span>
          </el-main>
        </el-container> -->
        <!-- <el-container>
            <el-aside width="350px">
          <h4 class="approve-subject-h4" style="margin: 10px 18px 18px;">提货增长返利: </h4>
          <el-radio-group v-model="approvePickUpRate" >
              <el-radio :label="1">开启</el-radio>
              <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
          <h4 class="approve-subject-h4" v-show="approvePickUpRate === 1" style="margin: 10px 0px 0px 18px;float: right;">流程配置:  </h4>
            </el-aside>
          <el-main>
          <span class="approve-subject-span" v-show="approvePickUpRate === 1"> 
              <div style="display: inline-block;padding-top:5px;padding-bottom:10px;" v-for="(item, index) of userTwelve" :key="item.id" :value="item.realName">
                <el-button class="mini-approve-btn">{{item.realName}}</el-button>
                <i @click="deletesTwelve(index)" class="el-icon-error close approve-subject-i"></i>
                <img class="icon" :src="approve_arrow_img"/>
              </div>
              <el-button class="mini-search-btn" style="margin-top:5px;margin-bottom:10px;" @click="addNewTwelve()">添加用户</el-button>
          </span>
          </el-main>
        </el-container> -->
        </div>
      </div>

      <el-row style="margin: 20px;padding-bottom:20px;">
        <el-col :span="5" :offset="10">
          <el-button class="mini-search-btn" @click="handleSubmit">保存</el-button>
          <el-button size="mini" @click="handleCancel">取消</el-button>
        </el-col>
      </el-row>
    </div>


    <!-- ====引用组件==== -->
    <!-- 商品管理 -->
    <el-dialog :modal-append-to-body="false" :visible="OneShow" width="80%" :before-close="OneCloseDialog">
      <addCommodity ref="selectOneUsers" :selectUsers="users" @cancel="OneCloseDialog" @submit="getUserData" @submitBack="handleBack"></addCommodity>
    </el-dialog>

    <!-- 分销商管理 -->
    <el-dialog :modal-append-to-body="false" :visible="TwoShow" width="70%" :before-close="TwoCloseDialog">
      <addDistributor ref="selectTwoUsers" :selectUsers="userOne" @cancel="TwoCloseDialog" @submitOne="UserDataOne" @submitBack="handleBack"></addDistributor>
    </el-dialog>

    <!-- 仓库管理 库存调整 -->
    <el-dialog :modal-append-to-body="false" :visible="ThreeShow" width="70%" :before-close="ThreeCloseDialog">
      <addWarehouseOne ref="selectThreeUsers" @cancel="ThreeCloseDialog" @submitThree="UserDataTwo"></addWarehouseOne>
    </el-dialog>

    <!-- 仓库管理 库存预留 -->
    <el-dialog :modal-append-to-body="false" :visible="FourShow" width="70%" :before-close="FourCloseDialog">
      <addWarehouseTwo ref="selectFourUsers" @cancel="FourCloseDialog" @submitFour="UserDataThree"></addWarehouseTwo>
    </el-dialog>

    <!-- 订单管理用户，订单价格 -->
    <el-dialog :modal-append-to-body="false" :visible="FiveShow" width="70%" :before-close="FiveCloseDialog">
      <addOrderOne ref="selectFiveUsers" @cancel="FiveCloseDialog" @submitFive="UserDataFour"></addOrderOne>
    </el-dialog>

    <!-- 订单管理用户，对账折扣 -->
    <el-dialog :modal-append-to-body="false" :visible="SixShow" width="70%" :before-close="SixCloseDialog">
      <addOrderTwo ref="selectSixUsers" @cancel="SixCloseDialog" @submitSix="UserDataFive"></addOrderTwo>
    </el-dialog>

    <!-- 营销推广，促销 -->
    <el-dialog :modal-append-to-body="false" :visible="sevenShow" width="70%" :before-close="SevenCloseDialog">
      <addPromotionOne ref="selectSevenUsers" :selectUsers="userSix" @cancel="SevenCloseDialog" @submitSeven="UserDataSix" @submitBack="handleBack"></addPromotionOne>
    </el-dialog>
    <!-- 商品，等级变更 -->
    <el-dialog :modal-append-to-body="false" :visible="tenShow" width="70%" :before-close="tenCloseDialog">
      <addChangeGade ref="selectTenUsers" :selectUsers="userTen" @cancel="tenCloseDialog" @submitTen="UserDataTen" @submitBack="handleBack"></addChangeGade>
    </el-dialog>
    <!-- 营销推广，商品等级折扣 -->
    <el-dialog :modal-append-to-body="false" :visible="elevenShow" width="70%" :before-close="elevenCloseDialog">
      <addPolicy ref="selectElevenUsers" :selectUsers="userEleven" @cancel="elevenCloseDialog" @submitEleven="UserDataEleven" @submitBack="handleBack"></addPolicy>
    </el-dialog>
    <!-- 营销推广，提货增长返利 -->
    <el-dialog :modal-append-to-body="false" :visible="twelveShow" width="70%" :before-close="twelveCloseDialog">
      <addPickUpRate ref="selectTwelveUsers" :selectUsers="userTwelve" @cancel="twelveCloseDialog" @submitTwelve="UserDataTwelve" @submitBack="handleBack"></addPickUpRate>
    </el-dialog>
    <!-- 营销推广，优惠券 -->
    <el-dialog :modal-append-to-body="false" :visible="fourteenShow" width="70%" :before-close="FourteenCloseDialog">
      <addPromotionOne ref="selectFourteenUsers" :selectUsers="userFourteen" @cancel="FourteenCloseDialog" @submitSeven="UserDataFourteen" @submitBack="handleBack"></addPromotionOne>
    </el-dialog>
    <!-- 营销推广，拼团秒杀 -->
    <el-dialog :modal-append-to-body="false" :visible="fifteenShow" width="70%" :before-close="FifteenCloseDialog">
      <addPromotionOne ref="selectFifteenUsers" :selectUsers="userFifteen" @cancel="FifteenCloseDialog" @submitSeven="UserDataFifteen" @submitBack="handleBack"></addPromotionOne>
    </el-dialog>
    <!-- 代金券 -->
    <el-dialog :modal-append-to-body="false" :visible="sixteenShow" width="70%" :before-close="sixteenCloseDialog">
      <addPromotionOne ref="selectSixteenUsers" :selectUsers="userSixteen" @cancel="sixteenCloseDialog" @submitSeven="UserDataSixteen" @submitBack="handleBack"></addPromotionOne>
    </el-dialog>
    </div>
</template>

<script>
// 引用组件
import Vue from 'vue'
import addCommodity from "@/views/system/approve/addCommodity" // 商品管理用户
import addDistributor from "@/views/system/approve/addDistributor" // 分销商管理用户
import addWarehouseOne from "@/views/system/approve/addWarehouseOne" // 仓库管理用户,库存调整
import addWarehouseTwo from "@/views/system/approve/addWarehouseTwo" // 仓库管理用户，库存预留
import addOrderOne from "@/views/system/approve/addOrderOne" // 订单管理用户，订单价格
import addOrderTwo from "@/views/system/approve/addOrderTwo" // 订单管理用户, 对账折扣
import addPromotionOne from "@/views/system/approve/addPromotionOne" // 订单管理用户, 对账折扣
import addChangeGade from "@/views/system/approve/addChangeGade" // 订单管理用户, 对账折扣
import addPolicy from "@/views/system/approve/addPolicy" // 订单管理用户, 对账折扣
import addPickUpRate from "@/views/system/approve/addPickUpRate" // 订单管理用户, 对账折扣
import { checkApprove } from "@/views/system/systemData" // 订单管理用户, 对账折扣
import { getCheckDetail,getUserIfos } from '@/views/system/systemData'
import approve_arrow_img from '@/assets/images/approve-arrow-right.png'
import checkCouponVue from '../../marketingPromotion/checkPromotion/checkCoupon.vue'

export default {
  name: 'approve',
  data() {
    return {
      loading: false,
      approve_arrow_img,
      disableIsShow: false,
      approveCommodity: 0, // 商品管理审批是否开启，默认关闭
      approveChangeGade: 0, // 商品，等级变更，默认关闭
      approveDistributor: 0, // 分销商管理审批，默认关闭
      approveWarehouse: 0, // 仓库管理,库存调整审批，默认关闭
      approveWarehouseReserved: 0, // 仓库管理,库存预留审批，默认关闭
      approveOrderPrice: 0, // 订单管理，订单价格审批，默认关闭
      approvePpromotion: 0, // 订单管理，对账折扣，默认关闭
      approvePromotion: 0, // 营销推广，促销，默认关闭
      approvePolicy: 0, // 营销推广，商品分类折扣，默认关闭
      approvePickUpRate: 0, // 营销推广，提货增长返利，默认关闭
      approveCoupon: 0, // 营销推广，优惠券，默认关闭
      approveGroupBy: 0, // 营销推广，拼团秒杀，默认关闭
      approveVoucher: 0, // 代金券，默认关闭

      mDapproveCommodity: 0, // 商品管理审批是否开启，默认关闭
      mDapproveChangeGade: 0, // 商品，等级变更，默认关闭
      mDapproveDistributor: 0, // 分销商管理审批，默认关闭
      mDapproveWarehouse: 0, // 仓库管理,库存调整审批，默认关闭
      mDapproveWarehouseReserved: 0, // 仓库管理,库存预留审批，默认关闭
      mDapproveOrderPrice: 0, // 订单管理，订单价格审批，默认关闭
      mDapprovePpromotion: 0, // 订单管理，对账折扣，默认关闭
      mDapprovePromotion: 0, // 营销推广，促销，默认关闭
      mDapprovePolicy: 0, // 营销推广，商品分类折扣，默认关闭
      mDapprovePickUpRate: 0, // 营销推广，提货增长返利，默认关闭
      mDapproveCoupon: 0, // 营销推广，优惠券，默认关闭
      mDapproveGroupBy: 0, // 营销推广，拼团秒杀，默认关闭
      mDapproveVoucher: 0, // 代金券，默认关闭

      OneShow: false, // 商品管理dialog
      TwoShow: false, // 分销商管理dialog
      ThreeShow: false, // 仓库管理,库存调整dialog
      FourShow: false, // 仓库管理,库存预留dialog
      FiveShow: false,  // 订单管理用户，订单价格dialog
      SixShow: false, // 订单管理用户，对账折扣dialog
      sevenShow: false, // 营销推广，促销dialog
      tenShow: false, // 商品，等级变更dialog
      elevenShow: false, // 营销推广，商品分类折扣dialog
      twelveShow: false, // 营销推广，提货增长返利dialog
      fourteenShow: false, // // 营销推广，优惠券dialog
      fifteenShow: false, // // 营销推广，拼团秒杀dialog
      sixteenShow: false, // // 代金券dialog
      
      mDusers: [], // 商品管理选中的用户名
      mDuserOne: [], // 分销商管理选中的用户名
      mDuserTwo: [], // 仓库管理,库存调整选中的用户名
      mDuserThree: [],  // 仓库管理,库存预留选中的用户名
      mDuserFour: [], // 订单管理用户，订单价格选中的用户名
      mDuserFive: [], // 订单管理用户，对账折扣选中的用户名
      mDuserSix: [], // 营销推广，促销选中的用户名
      mDuserTen: [], // 商品，等级变更选中的用户名
      mDuserEleven: [], // 营销推广，商品分类折扣选中的用户名
      mDuserTwelve: [], // 营销推广，提货增长返利选中的用户名
      mDuserFourteen: [], // 营销推广，优惠券
      mDuserFifteen: [], // 营销推广，拼团秒杀
      mDuserSixteen: [], // 代金券

      users: [], // 商品管理选中的用户名
      userOne: [], // 分销商管理选中的用户名
      userTwo: [], // 仓库管理,库存调整选中的用户名
      userThree: [],  // 仓库管理,库存预留选中的用户名
      userFour: [], // 订单管理用户，订单价格选中的用户名
      userFive: [], // 订单管理用户，对账折扣选中的用户名
      userSix: [], // 营销推广，促销选中的用户名
      userTen: [], // 商品，等级变更选中的用户名
      userEleven: [], // 营销推广，商品分类折扣选中的用户名
      userTwelve: [], // 营销推广，提货增长返利选中的用户名
      userFourteen: [], // 营销推广，优惠券
      userFifteen: [], // 营销推广，拼团秒杀
      userSixteen: [], // 代金券
    }
  },
  components: {
    addCommodity, // 商品管理用户dialog
    addDistributor, // 分销商管理用户dialog
    addWarehouseOne, // 仓库管理,用户库存调整dialog
    addWarehouseTwo,  // 仓库管理,用户库存预留dialog
    addOrderOne,  // 订单管理用户，订单价格dialog
    addOrderTwo, // 订单管理用户，对账折扣dialog
    addPromotionOne, // 营销推广，促销dialog
    addChangeGade, // 商品，等级变更dialog
    addPolicy, // 营销推广，商品分类折扣dialog
    addPickUpRate, // 营销推广，提货增长返利dialog
  },
  created() {
    this.getCheckDetail();
  },
  activated() {
    this.getCheckDetail();
  },
  methods: {
    handleCancel(){
      this.users = this.mDusers.concat();
      this.userOne = this.mDuserOne.concat();
      this.userTwo = this.mDuserTwo.concat();
      this.userThree = this.mDuserThree.concat();
      this.userFour = this.mDuserFour.concat();
      this.userFive = this.mDuserFive.concat();
      this.userSix = this.mDuserSix.concat();
      this.userTen = this.mDuserTen.concat();
      this.userEleven = this.mDuserEleven.concat();
      this.userTwelve = this.mDuserTwelve.concat();
      this.userFourteen = this.mDuserFourteen.concat();
      this.userFifteen = this.mDuserFifteen.concat();
      this.userSixteen = this.mDuserSixteen.concat();
      
      this.approveCommodity = this.mDapproveCommodity
      this.approveChangeGade = this.mDapproveChangeGade
      this.approveDistributor = this.mDapproveDistributor
      this.approveWarehouse = this.mDapproveWarehouse
      this.approveWarehouseReserved = this.mDapproveWarehouseReserved
      this.approveOrderPrice = this.mDapproveOrderPrice
      this.approvePpromotion = this.mDapprovePpromotion
      this.approvePromotion = this.mDapprovePromotion
      this.approvePolicy = this.mDapprovePolicy
      this.approvePickUpRate = this.mDapprovePickUpRate
      this.approveCoupon = this.mDapproveCoupon
      this.approveGroupBy = this.mDapproveGroupBy
      this.approveVoucher = this.mDapproveVoucher
    },
    getCheckDetail() {
      this.loading = true
      this.users = []
      this.userOne = []
      this.userTwo = []
      this.userThree = []
      this.userFour = []
      this.userFive = []
      this.userSix = []
      this.userTen = []
      this.userEleven = []
      this.userTwelve = []
      this.userFourteen = []
      this.userFifteen = []
      this.userSixteen = []
      // ext: 1 商品管理上下架审批, 2 分销商编辑审批, 3 仓库库存调整审批,4 仓库库存预留审批, 5 订单价格审批,6 订单对账折扣审批,7 促销活动新增审批 8 促销活动编辑审批
      // 13 促销活动  14 优惠券  15 拼团秒杀 16 代金券
      this.$http.checkDetail(this).then(res => {
        res.data.forEach(item =>{
            let userIds=''
            if(item.ext === 1){ // ext: 1 商品管理上下架审批
              this.approveCommodity = item.openFlag
              this.mDapproveCommodity = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.users.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {
                res.data.forEach(item => {
                  for(let j = 0;j<this.users.length;j++){
                    if(item.id === this.users[j].id){
                      this.users[j].realName = item.realName
                    }
                  }
                })
                this.mDusers = this.users.concat();
              })
            }else if(item.ext === 2){ // ext: 2 分销商编辑审批
              this.approveDistributor = item.openFlag
              this.mDapproveDistributor = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userOne.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {
                res.data.forEach(item => {
                  for(let j = 0;j<this.userOne.length;j++){
                    if(item.id === this.userOne[j].id){
                      this.userOne[j].realName = item.realName
                    }
                  }
                })
                this.mDuserOne = this.userOne.concat();
              })
            }else if(item.ext === 3){ // ext: 3 仓库库存调整审批
              this.approveWarehouse = item.openFlag
              this.mDapproveWarehouse = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userTwo.push(obj)
              }
              // getUserIfos(this,{ids:userIds}).then(data =>{
              this.$http.getUserIds(this, { ids: userIds }).then(res => {
                res.data.forEach(item => {
                  for(let j = 0;j<this.userTwo.length;j++){
                    if(item.id === this.userTwo[j].id){
                      this.userTwo[j].realName = item.realName
                    }
                  }
                })
                this.mDuserTwo = this.userTwo.concat();
              })
            }else if(item.ext === 4){ // ext: 4 仓库库存预留审批
              this.approveWarehouseReserved = item.openFlag
              this.mDapproveWarehouseReserved = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userThree.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userThree.length;j++){
                    if(item.id === this.userThree[j].id){
                      this.userThree[j].realName = item.realName
                    }
                  }
                })
                this.mDuserThree = this.userThree.concat();
              })
            }else if(item.ext === 5){ // ext: 5 订单价格审批
              this.approveOrderPrice = item.openFlag
              this.mDapproveOrderPrice = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userFour.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userFour.length;j++){
                    if(item.id === this.userFour[j].id){
                      this.userFour[j].realName = item.realName
                    }
                  }
                })
                this.mDuserFour = this.userFour.concat();
              })
            }else if(item.ext === 6){ // ext: 6 订单对账折扣审批
              this.approvePpromotion = item.openFlag
              this.mDapprovePpromotion = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userFive.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userFive.length;j++){
                    if(item.id === this.userFive[j].id){
                      this.userFive[j].realName = item.realName
                    }
                  }
                })
                this.mDuserFive = this.userFive.concat();
              })
            }else if(item.ext === 13){ // ext: 13 促销活动新增审批
              this.approvePromotion = item.openFlag
              this.mDapprovePromotion = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userSix.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userSix.length;j++){
                    if(item.id === this.userSix[j].id){
                      this.userSix[j].realName = item.realName
                    }
                  }
                })
                this.mDuserSix = this.userSix.concat();
              })
            }else if(item.ext === 10){ // ext: 10 商品等级变动
              this.approveChangeGade = item.openFlag
              this.mDapproveChangeGade = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userTen.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userTen.length;j++){
                    if(item.id === this.userTen[j].id){
                      this.userTen[j].realName = item.realName
                    }
                  }
                })
                this.mDuserTen = this.userTen.concat();
              })
            }else if(item.ext === 11){ // ext: 11
              this.approvePolicy = item.openFlag
              this.mDapprovePolicy = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userEleven.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {   
                res.data.forEach(item => {
                  for(let j = 0;j<this.userEleven.length;j++){
                    if(item.id === this.userEleven[j].id){
                      this.userEleven[j].realName = item.realName
                    }
                  }
                })
                this.mDuserEleven = this.userEleven.concat();
              })
            }else if(item.ext === 12){ // ext: 12
              this.approvePickUpRate = item.openFlag
              this.mDapprovePickUpRate = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userTwelve.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {   
                res.data.forEach(item => {
                  for(let j = 0;j<this.userTwelve.length;j++){
                    if(item.id === this.userTwelve[j].id){
                      this.userTwelve[j].realName = item.realName
                    }
                  }
                })
                this.mDuserTwelve = this.userTwelve.concat();
              })
            }else if(item.ext === 14){ // ext: 14 优惠券审批
              this.approveCoupon = item.openFlag
              this.mDapproveCoupon = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userFourteen.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userFourteen.length;j++){
                    if(item.id === this.userFourteen[j].id){
                      this.userFourteen[j].realName = item.realName
                    }
                  }
                })
                this.mDuserFourteen = this.userFourteen.concat();
              })
            }else if(item.ext === 15){ // ext: 15 拼团秒杀审批
              this.approveGroupBy = item.openFlag
              this.mDapproveGroupBy = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userFifteen.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userFifteen.length;j++){
                    if(item.id === this.userFifteen[j].id){
                      this.userFifteen[j].realName = item.realName
                    }
                  }
                })
                this.mDuserFifteen = this.userFifteen.concat();
              })
            }else if(item.ext === 16){ // ext: 16 代金券
              this.approveVoucher = item.openFlag
              this.mDapproveVoucher = item.openFlag
              for(let i=0;i<item.checkConfigs.length;i++){
                let obj = {
                  id: item.checkConfigs[i].checkUser,
                  realName: '',
                  disabled: false,
                }
                obj.disabled = item.checkConfigs[i].openFlag === 1?true:false
                if(i === 0){
                  userIds = item.checkConfigs[i].checkUser
                }else{
                  userIds = userIds+','+item.checkConfigs[i].checkUser
                }
                this.userSixteen.push(obj)
              }
              this.$http.getUserIds(this, { ids: userIds }).then(res => {  
                res.data.forEach(item => {
                  for(let j = 0;j<this.userSixteen.length;j++){
                    if(item.id === this.userSixteen[j].id){
                      this.userSixteen[j].realName = item.realName
                    }
                  }
                })
                this.mDuserSixteen = this.userSixteen.concat();
              })
            }
          })
          res.success ? this.loading = false : this.looking = false
        })
    },
    getUserIfos(userIds){

    },
    // ===== 商品管理 =====
    addNewOne() { // 商品管理添加用户
      this.OneShow = true;
    },
    
    addNewTen(userTen) { // 商品管理登记变更添加用户
      this.tenShow = true;
    },
    
    tenCloseDialog() { // 用户添加关闭dialog的X
      this.$refs.selectTenUsers.handleBack()
      this.tenShow = false;
    },
    
    OneCloseDialog() { // 商品管理用户添加关闭dialog的X
      this.$refs.selectOneUsers.handleBack()
      this.OneShow = false;
    },
    
    deletes(index) { // 商品管理上下架删除操作
      this.users.splice(index, 1);
    },
    
    getUserData(val) { // dialog组件传上来的选中的值
      this.OneShow = false;
      this.setArr(this.users.concat(val))
      this.users.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.users = val
    },
  
    // ===== 分销商管理 =====
    addNewTwo() { // 添加用户
      this.TwoShow = true;
    },
    
    TwoCloseDialog() { // 添加关闭dialog的X
      this.$refs.selectTwoUsers.handleBack()
      this.TwoShow = false;
    },
    
    UserDataOne(val) { // dialog组件传上来的选中值
      this.TwoShow = false;
      this.setArr(this.userOne.concat(val))
      this.userOne.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userOne = val
    },
    
    deletesOne(index) { // 删除按钮
      this.userOne.splice(index, 1);
    },

    // ===== 仓库管理，库存调整 =====
    addNewThree() { // 添加用户
      this.ThreeShow = true;
    },
    
    ThreeCloseDialog() { // 添加关闭dialog的X
      this.$refs.selectThreeUsers.handleBack()
      this.ThreeShow = false;
    },
   
    UserDataTwo(val) {  // dialog组件传上来的选中值
      this.ThreeShow = false;
      this.setArr(this.userTwo.concat(val))
      this.userTwo.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userTwo = val
    },
    
    deletesTwo(index) { // 删除按钮
      this.userTwo.splice(index, 1);
    },

    // ===== 仓库管理，库存预留 =====
    addNewFour() { // 添加用户
      this.FourShow = true;
    },
    
    FourCloseDialog() { // 添加关闭dialog的X
      this.$refs.selectFourUsers.handleBack()
      this.FourShow = false;
    },
    
    UserDataThree(val) { // dialog组件传上来的选中值
      this.FourShow = false;
      this.setArr(this.userThree.concat(val))
      this.userThree.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userThree = val
    },
    
    deletesThres(index) { // 删除操作
      this.userThree.splice(index, 1);
    },

    // ===== 订单管理，订单价格 =====

    addNewFive() { // 添加用户
      this.FiveShow = true;
    },
    
    FiveCloseDialog() { // 添加关闭dialog的X
      this.$refs.selectFiveUsers.handleBack()
      this.FiveShow = false;
    },
    
    UserDataFour(val) { // dialog组件传上来的选中值
      this.FiveShow = false;
      this.setArr(this.userFour.concat(val))
      this.userFour.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userFour = val
    },
    
    deletesFour(index) { // 删除按钮
      this.userFour.splice(index, 1);
    },

    //  ===== 订单管理，对账折扣 =====
    addNewSix() { // 添加用户
      this.SixShow = true;
    },
    
    SixCloseDialog() { // 添加关闭dialog的X
      this.$refs.selectSixUsers.handleBack()
      this.SixShow = false;
    },
    
    UserDataFive(val) { // dialog组件传上来的选中值
      this.SixShow = false;
      this.setArr(this.userFive.concat(val))
      this.userFive.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userFive = val
    },
    
    deletesFive(index) { // 删除按钮
      this.userFive.splice(index, 1);
    },

    //  ===== 营销推广，促销活动 =====
    addNewSeven() { // 商品管理添加用户
      this.sevenShow = true;
    },
    
    SevenCloseDialog() { // 用户添加关闭dialog的X
      this.$refs.selectSevenUsers.handleBack()
      this.sevenShow = false;
    },
    
    addNewEleven() { // 添加用户
      this.elevenShow = true;
    },
    
    elevenCloseDialog() { // 用户添加关闭dialog的X
      this.$refs.selectElevenUsers.handleBack()
      this.elevenShow = false;
    },
    
    addNewTwelve() { // 添加用户
      this.twelveShow = true;
    },
    
    twelveCloseDialog() { // 用户添加关闭dialog的X
      this.$refs.selectTwelveUsers.handleBack()
      this.twelveShow = false;
    },

     //  ===== 营销推广，优惠券 =====
    addNewFourteen() { // 添加用户
      this.fourteenShow = true;
    },
    
    FourteenCloseDialog() { // 用户添加关闭dialog的X
      this.$refs.selectFourteenUsers.handleBack()
      this.fourteenShow = false;
    },
    
     //  ===== 营销推广，拼团秒杀 =====
    addNewFifteen() { // 添加用户
      this.fifteenShow = true;
    },
    
    FifteenCloseDialog() { // 用户添加关闭dialog的X
      this.$refs.selectFifteenUsers.handleBack()
      this.fifteenShow = false;
    },

    //  ===== 代金券 =====
    addNewSixteen() { // 添加用户
      this.sixteenShow = true;
    },
    
    sixteenCloseDialog() { // 用户添加关闭dialog的X
      this.$refs.selectSixteenUsers.handleBack()
      this.sixteenShow = false;
    },

    deletesSix(index) { // 营销推广促销活动删除操作
      this.userSix.splice(index, 1);
    },
    
    deletesTen(index) { // 商品管理登记变更删除操作
      this.userTen.splice(index, 1);
    },
    
    deletesEleven(index) { // 删除按钮
      this.userEleven.splice(index, 1);
    },
    
    deletesTwelve(index) { // 删除按钮
      this.userTwelve.splice(index, 1);
    },

    deletesFourteen(index) { // 删除按钮
      this.userFourteen.splice(index, 1);
    },

    deletesFifteen(index) { // 删除按钮
      this.userFifteen.splice(index, 1);
    },

    deletesSixteen(index) { // 删除按钮
      this.userSixteen.splice(index, 1);
    },
    
    UserDataSix(val) { // dialog组件传上来的选中的值
      this.sevenShow = false;
      this.setArr(this.userSix.concat(val))
      this.userSix.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userSix = val
    },
    
    UserDataTen(val) { // dialog组件传上来的选中的值
      this.tenShow = false;
      this.setArr(this.userTen.concat(val))
      this.userTen.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userTen = val
    },
    
    UserDataEleven(val) { // dialog组件传上来的选中的值
      this.elevenShow = false;
      this.setArr(this.userEleven.concat(val))
      this.userEleven.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userEleven = val
    },
    
    UserDataTwelve(val) { // dialog组件传上来的选中的值
      this.twelveShow = false;
      this.setArr(this.userTwelve.concat(val))
      this.userTwelve.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userTwelve = val
    },

    UserDataFourteen(val) { // dialog组件传上来的选中的值
      this.fourteenShow = false;
      this.setArr(this.userFourteen.concat(val))
      this.userFourteen.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userFourteen = val
    },
    UserDataFifteen(val) { // dialog组件传上来的选中的值
      this.fifteenShow = false;
      this.setArr(this.userFifteen.concat(val))
      this.userFifteen.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userFifteen = val
    },
    UserDataSixteen(val) { // dialog组件传上来的选中的值
      this.sixteenShow = false;
      this.setArr(this.userSixteen.concat(val))
      this.userSixteen.forEach(item => {
        if(val.id == item.id) {
          item.splice(0,0,item.id)
        }
      })
      this.userSixteen = val
    },
    
    handleBack() { // dialog组件关闭
      this.OneShow = false
      this.TwoShow = false
      this.ThreeShow = false
      this.FourShow = false
      this.FiveShow = false
      this.SixShow = false
      this.sevenShow = false
      this.tenShow = false
      this.elevenShow = false
      this.twelveShow = false
      this.fourteenShow = false
      this.fifteenShow = false
      this.sixteenShow = false
    },
    handleSubmit(){
      return this.$confirm(`确认要保存更改吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(this.sendRequests).then(allRes => {
        for(const res of allRes){
          if (res && res.code === 0) {
            // this.$message({
            //   type: 'success',
            //   message: `操作成功`
            // })
          }
        }
      })
      .catch(e => console.log(e))
    },
    
    sendRequests() { // 保存按钮
      if(this.approveCommodity === 1 && this.users.length === 0){
        this.$message.error('商品管理开启了审批，审批人不能为空！');
        return
      }
      if(this.approveDistributor === 1 && this.userOne.length === 0){
        this.$message.error('分销商管理开启了审批，审批人不能为空！');
        return
      }
      if(this.approvePromotion === 1 && this.userSix.length === 0){
        this.$message.error('营销推广开启了审批，审批人不能为空！');
        return
      }
      if(this.approveChangeGade === 1 && this.userTen.length === 0){
        this.$message.error('等级变更开启了审批，审批人不能为空！');
        return
      }
      // if(this.approvePolicy === 1 && this.userEleven.length === 0){
      //   this.$message.error('商品分类折扣开启了审批，审批人不能为空！');
      //   return
      // }
      // if(this.approvePickUpRate === 1 && this.userTwelve.length === 0){
      //   this.$message.error('提货增长返利开启了审批，审批人不能为空！');
      //   return
      // }
      // if(this.approvePickUpRate === 1 && this.userTwelve.length === 0){
      //   this.$message.error('提货增长返利开启了审批，审批人不能为空！');
      //   return
      // }
      // if(this.approvePickUpRate === 1 && this.userTwelve.length === 0){
      //   this.$message.error('提货增长返利开启了审批，审批人不能为空！');
      //   return
      // }
      
      const goodsCheckU = this.users.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
       const distrCheckU = this.userOne.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
       const wareAdjCheckU = this.userTwo.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
       const wareReseCheckU = this.userThree.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
       const orderPriceCheckU = this.userFour.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
       const orderDiscCheckU = this.userFive.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
       const promotionCheckU = this.userSix.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
        const changeGadeCheckU = this.userTen.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
        const policyCheckU = this.userEleven.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
        const pickUpRateCheckU = this.userTwelve.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
        const couponCheckU = this.userFourteen.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
        const groupByCheckU = this.userFifteen.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
        const voucherCheckU = this.userSixteen.map(item => item.id).reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + (cur === undefined?'':cur)
        }, '')
      return Promise.all([
          checkApprove(this, {ext:1,checkUsers:goodsCheckU,openFlag:this.approveCommodity}),
          checkApprove(this, {ext:2,checkUsers:distrCheckU,openFlag:this.approveDistributor}),
          checkApprove(this, {ext:3,checkUsers:wareAdjCheckU,openFlag:this.approveWarehouse}),
          checkApprove(this, {ext:4,checkUsers:wareReseCheckU,openFlag:this.approveWarehouseReserved}),
          checkApprove(this, {ext:5,checkUsers:orderPriceCheckU,openFlag:this.approveOrderPrice}),
          checkApprove(this, {ext:6,checkUsers:orderDiscCheckU,openFlag:this.approvePpromotion}),
          checkApprove(this, {ext:13,checkUsers:promotionCheckU,openFlag:this.approvePromotion}),
          checkApprove(this, {ext:8,checkUsers:promotionCheckU,openFlag:this.approvePromotion}),
          // checkApprove(this, {ext:10,checkUsers:changeGadeCheckU,openFlag:this.approveChangeGade}),
          // checkApprove(this, {ext:11,checkUsers:policyCheckU,openFlag:this.approvePolicy}),
          // checkApprove(this, {ext:12,checkUsers:pickUpRateCheckU,openFlag:this.approvePickUpRate})
          checkApprove(this, {ext:14,checkUsers:couponCheckU,openFlag:this.approveCoupon}),
          checkApprove(this, {ext:15,checkUsers:groupByCheckU,openFlag:this.approveGroupBy}),
          checkApprove(this, {ext:16,checkUsers:voucherCheckU,openFlag:this.approveVoucher})
          .then(res =>{
            if(res.success) {
              this.$message({
                  message: '保存成功',
                  type: 'success',
                  duration: 3 * 1000,
                  onClose: () => { }
              })
              this.mDusers = this.mDusers.concat();
              this.mDuserOne = this.mDuserOne.concat();
              this.mDuserTwo = this.mDuserTwo.concat();
              this.mDuserThree = this.mDuserThree.concat();
              this.mDuserFour = this.mDuserFour.concat();
              this.mDuserFive = this.mDuserFive.concat();
              this.mDuserSix = this.mDuserSix.concat();
              this.mDuserTen = this.mDuserTen.concat();
              this.mDuserEleven = this.mDuserEleven.concat();
              this.mDuserTwelve = this.mDuserTwelve.concat();
              this.mDuserFourteen = this.mDuserFourteen.concat();
              this.mDuserFifteen = this.mDuserFifteen.concat();
              this.mDuserSixteen = this.mDuserSixteen.concat();
              
              this.mDapproveCommodity = this.approveCommodity
              this.mDapproveChangeGade = this.approveChangeGade
              this.mDapproveDistributor = this.approveDistributor
              this.mDapproveWarehouse = this.approveWarehouse
              this.mDapproveWarehouseReserved = this.approveWarehouseReserved
              this.mDapproveOrderPrice = this.approveOrderPrice
              this.mDapprovePpromotion = this.approvePpromotion
              this.mDapprovePromotion = this.approvePromotion
              this.mDapprovePolicy = this.approvePolicy
              this.mDapprovePickUpRate = this.approvePickUpRate
              this.mDapproveCoupon = this.approveCoupon
              this.mDapproveGroupBy = this.approveGroupBy
              this.mDapproveVoucher = this.approveVoucher
            }
          })
      ])
    },
    
    setArr(arr){ // 去重
        let obj ={};  
        let temp=[];  
        for( let i = 0; i < arr.length; i++ ) {  
        let type= Object.prototype.toString.call(arr[i].id);//不加类型 分不清 1 '1'    
        if( !obj[ arr[i].id +type] ) {  
          temp.push( arr[i] );  
          obj[ arr[i].id+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
        }
      } 
      return temp; 
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.main[data-v-6beed8bc] {
  background-color: #fff;
}
.el-icon-error {
  color: #999;
}
.approve-list {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .approve-subject {
    margin: 50px auto;
    width: 90%;
    height: 30%;
    background-color: #f5f7fa;
    border-radius: 5px;
    border: 1px solid #dcdcdc;
    .approve-subject-h4 {
      font-size: 14px;
    }
    .approve-subject-hr {
      border: 0;
      background-color: #dcdcdc;
      height: 1px;
    }
    .approve-subject-span {
      font-size: 14px;
      .approve-subject-i {
        position: relative;
        top: -5px;
        left: -40px;
      }
      .close {
        float:right;
      }
    }
    .box-has-info{
      overflow: hidden;
      .half-left{
        width: 30%;
        box-sizing: border-box;
        float: left;
      }
      .half-right{
        width: 70%;
        box-sizing: border-box;
        float: left;
      }
    }
    .el-main {
      padding-left: 10px;
      padding-right: 0px;
      padding-top: 0px;
      padding-bottom: 0px;
    }
    .icon{
      position: relative;
      width: 20px;
      height: 20px;
      top: 5px;
      margin-right: 10px;
    }
    // .icon::before{
    //   content: url("/src/assets/images/approve-arrow-right.png");
    // }
  }
  
}
</style>
