<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header></Header>
      <!--主内容-->
      <div class="main rl-margin-zero rl-padding-top-default">
        <div class="container">
          <div class="content rl-clear rl-bg-white">
            <div class="left rl-fl">
              <div class="head rl-tc rl-text-default">{{$t('HelpCenter.CustomerService')}}</div>
              <ul>
                <li
                  v-for="item in leftTabList"
                  v-if="item.show && item.id !== 2"
                  :key="item.id"
                  :class="activeTab === item.id ? 'active': ''"
                  @click="changeState(item.id)"
                >{{item.name}}</li>
              </ul>
            </div>
            <div class="right rl-fl" v-show="activeTab === 0">
              <div
                class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
              >{{$t('HelpCenter.T0')}}</div>
              <div class="content" v-show="$i18n.locale === 'zh'">
                <p class="text-indent">1、新用户首先要进行注册，打开网址，点击页面“立即注册”，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help1.png" alt />
                </div>
                <p class="text-indent">2、仔细阅读服务条款，点击“同意并继续”（注意：只有同意会员注册协议才可成为会员），如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help2.png" alt />
                </div>
                <p class="text-indent">3、验证手机号，点击“下一步”，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help3.jpg" alt />
                </div>
                <p class="text-indent">4、完善所有资料，带*为必填项，点击“提交”，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help4.png" alt />
                </div>
                <p class="text-indent">5、提交注册后，联系平台客服，提交公司五证资料（公司营业执照、税务登记证、组织机构代码证、一般纳税人资格证、开户许可证）；</p>
                <p
                  class="text-indent"
                >6、待公司资质审核通过后，输入用户名和密码，点击“登录”即可（公司资质审核未通过时，只可登录，无法进行购买和查看商品价格操作），如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help5.png" alt />
                </div>
                <p class="text-indent">7、在所有商品分类中查找所需要的产品或直接在搜索栏搜索所需要的产品，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help6.png" alt />
                </div>
                <p class="text-indent">8、根据价格、销量、人气等设置排列商品，方便快速找到自己想要的产品，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help7.png" alt />
                </div>
                <p class="text-indent">9、点击产品，进入产品详情页面浏览产品详情，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help8.png" alt />
                </div>
                <p class="text-indent">10、选择产品数量，可直接点击购买，也可先加入购物车待产品全部选好后一并结算，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help9.png" alt />
                </div>
                <p class="text-indent">11、点击购买，填写或选择收货信息、配送方式和发票信息等，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help10.png" alt />
                </div>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help11.png" alt />
                </div>
                <p class="text-indent">12、所有信息确认无误后，点击“确认无误，下订单”，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help12.png" alt />
                </div>
                <p class="text-indent">13、返回首页点击“会员中心”查看订单，如图：</p>
                <div class="h-img">
                  <img src="../assets/images/help/zh/help13.png" alt />
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'">
                <p
                  class="text-indent"
                >1. First you need to register an account, open a website, click [login] at the top right of the page to jump to the login page, and click [register] on the login page.</p>
                <div class="h-img">
                  <img src="../assets/images/help/en/p1.png" alt />
                </div>
                <p
                  class="text-indent"
                >2. Carefully read and agree the membership registration agreement (Note: You can become a member only after agreeing to the membership registration agreement).</p>
                <p class="text-indent">3. Verify email.</p>
                <p
                  class="text-indent"
                >4. Complete all required fields on the page according to the page prompt, and then click "submit".</p>
                <p
                  class="text-indent"
                >5. Submit registration information, and then wait for approval.</p>
                <p
                  class="text-indent"
                >6. After approval, you can enter user name and password on the [login page] to log in and browse more goods (Note: You cannot purchase goods or view goods prices without login).</p>
                <p
                  class="text-indent"
                >7. You can find the goods you need by category in [category of all goods] or the search bar directly.</p>
                <p class="text-indent">8. Click card of goods to view details.</p>
                <p
                  class="text-indent"
                >9. On the page of details, complete the quantity of goods you want to buy, and click "buy now" to submit the order or add goods to cart and check out with other goods.</p>
                <p
                  class="text-indent"
                >10. On the page of order confirmation, you need to complete the information such as shipping address, payment currency and payment method.</p>
                <p
                  class="text-indent"
                >11. Click "submit order" to complete the purchase after all information is confirmed.</p>
              </div>
            </div>
            <!-- 购买必读 -->
            <div class="right rl-fl" v-show="activeTab === 1">
              <div
                class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
              >{{$t('HelpCenter.T1')}}</div>
              <div class="content">
                <div class="content" v-show="$i18n.locale === 'zh'">
                  <p class="rl-text-bold">1. 如何订购商品？</p>
                  <p
                    class="text-indent"
                  >先平台注册会员，注册成功后浏览网站商品，点击购买所需要的商品或将需要的商品一一加入购物车后一并下单付款，另外，您也可以和我们网站的客服人员联系订购。</p>
                  <p class="rl-text-bold rl-margin-top-default">2. 为什么要注册会员？</p>
                  <p class="text-indent">只有注册用户才可以在网上进行订购，享受活动优惠价格。</p>
                  <p class="text-indent">只有注册用户才有可能获取我们赠送的礼品。</p>
                  <p class="text-indent">只有注册用户才可以登录"会员中心"，使用更多的会员购物功能，管理自己的资料。</p>
                  <p class="rl-text-bold rl-margin-top-default">3. 平台可以发送商品的图片和介绍给我吗？</p>
                  <p
                    class="text-indent"
                  >您关注我们的微信公众号，就会收到新品推广和活动推送消息。如果您目前还没有成为我们的会员，您可以直接在我们的网站上注册，非常方便。</p>
                  <p class="rl-text-bold rl-margin-top-default">4. 所有的产品都能够在网站上购买？</p>
                  <p
                    class="text-indent"
                  >目前网站上有展示的都是可以订购的，但是必须是仓库中有库存的产品我们才可以与您确认。部分热销产品您也可以通过我们的网站做一个预订，等到商品到货后，我们会立即通过电话或者电子邮件的方式通知您来订购。</p>
                  <p class="rl-text-bold rl-margin-top-default">5. 忘记了账户密码怎么办？</p>
                  <p
                    class="text-indent"
                  >为了保护您的利益，我们无法看到您的密码。当您忘记密码时，请登录注册页面，点击"忘记密码"，填写您的注册手机号，系统会将验证码发送到您的手机上，您输入正确的验证码之后可以重新设置密码。登录网站后您可以进入“会员中心—修改密码”，以确保您的账户安全。</p>
                  <p class="rl-text-bold rl-margin-top-default">6. 积分是怎么回事？有什么作用？</p>
                  <p class="text-indent">您通过订购商品可以获得相应积分，积分的高低反映您对我们的关注和支持程度。</p>
                  <p
                    class="text-indent"
                  >积分规则：1元=1积分，即下单商品总价格1元，则获得1个积分；积分按照您下单商品总价格的个位数取整计算，如68.2元=68积分。</p>
                  <p
                    class="text-indent"
                  >积分使用：获得的积分在下一次下单结算时可以抵扣部分现金；每月平台会推出2期积分换购活动，您也可以使用积分兑换活动产品；当月积分可以累加，每月最后一日24点超过50000以上的积分会自动清零。</p>
                </div>
                <div class="content" v-show="$i18n.locale === 'en'">
                  <p class="rl-text-bold">1. How do I order goods?</p>
                  <p
                    class="text-indent"
                  >First you need to become a member of the Company by registration, and then you can log in and browse the goods on the website, finally click to buy the goods you need or add them to cart one by one and place an order and make payment. In addition, you can also contact customer service staff of the website for ordering.</p>
                  <p
                    class="rl-text-bold rl-margin-top-default"
                  >2. Can system send me images and introductions of goods?</p>
                  <p
                    class="text-indent"
                  >You can directly browse the goods on the website, or follow our WeChat official account to get the latest goods and sales campaign.</p>
                  <p
                    class="rl-text-bold rl-margin-top-default"
                  >3. What should I do if I forgot my password?</p>
                  <p
                    class="text-indent"
                  >When you forget your password, please click "forget password" on the login page and reset the password by verifying your identity with the email information you completed during registration.</p>
                  <p class="rl-text-bold rl-margin-top-default">4. What is point? What does it do?</p>
                  <p
                    class="text-indent"
                  >You can get corresponding points for ordering goods on the website.</p>
                  <p
                    class="text-indent"
                  >Point rule: RMB 1 = 1 point, that is, if the total price of goods ordered is RMB 1, you can get 1 point; the point is rounded at single digit of the total price of goods ordered, for example, RMB 68.2 = 68 points.</p>
                  <p
                    class="text-indent"
                  >Point use: The points obtained can be deducted as some of the cash during next ordering and checkout; system will launch point redemption twice a month, you can also exchange points for goods; monthly points can be accumulated, and the points over 50,000 will be reset automatically after 24 o'clock on the last day of each month.</p>
                </div>
              </div>
            </div>
            <div class="right rl-fl" v-show="activeTab === 2">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T2')}}</div>
                <div class="content">
                  <p
                    class="text-indent"
                  >会员权益：网上进行订购，享受活动优惠价格；获取我们赠送的会员礼品；登录"会员中心"，使用更多的会员购物功能，管理自己的资料。</p>
                  <p
                    class="text-indent"
                  >我们的会员等级系统是动态的，按照会员积分的多少划分不同的等级，等级越高享受的商品折扣越大。针对会员的促销活动和优惠政策会运用到每一个会员活动中。如：“捆绑销售”、“积分换购”等等。</p>
                  <p class="text-indent">会员等级划分：</p>
                  <p
                    class="text-indent"
                  >铜牌会员（1≤积分≤9999）→银牌会员（10000≤积分≤49999）→金牌会员（50000≤积分≤99999）→钻石会员（积分≥100000）</p>
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T2')}}</div>
                <p class="text-indent rl-text-bold">1. Member equity</p>
                <p
                  class="text-indent"
                >You can enjoy preferential price of sale campaign or get member gifts as per member grade.</p>
                <p
                  class="text-indent"
                >This dynamic member grade system divides different grades as per member points. The higher the grade is, the greater the discount is. Sales campaign and preferential policies for members will be applied to every member event, such as "bundling sales" and "point redemption".</p>
                <p class="text-indent rl-text-bold">2. Classification of member grade</p>
                <p
                  class="text-indent"
                >Bronze (1 ≤ points ≤ 9,999) → Silver (10,000 ≤ points ≤ 49,999) → Gold (50,000 ≤ points ≤ 99,999) → Diamond (points ≥ 100,000)</p>
              </div>
            </div>
            <div class="right rl-fl" v-show="activeTab === 3">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T3')}}</div>
                <div class="content">
                  <div class="h-img">
                    <img src="../assets/images/help/zh/help14.png" alt />
                  </div>
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'"></div>
            </div>
            <div class="right rl-fl" v-show="activeTab === 4">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T4')}}</div>
                <div class="content">
                  <p
                    class="text-indent"
                  >1、物流：在BAT商城B2B平台下单满2000元以上（含2000元）的订单，平台中通包邮（PS：仅限中通快递配送），中通不到的地区或客户不愿使用中通快递的，请客户选择其它快递自行付费，具体付费下单页面会有显示，各快递费用请详见《配送费收取标准》。</p>
                  <p
                    class="text-indent"
                  >2、发货：平台默认在客户下单后两天正常发货，在下单页面，客户可以根据个人具体情况选择平台发货时间（仅限默认日期之后的时间），平台发货后客户可以到平台会员中心订单中点击“物流查询”，在线跟踪物流情况。</p>
                  <div class="h-img">
                    <img src="../assets/images/help/zh/help15.jpg" alt />
                  </div>
                  <div class="h-img">
                    <img src="../assets/images/help/zh/help16.png" alt />
                  </div>
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'"></div>
            </div>
            <div class="right rl-fl" v-show="activeTab === 5">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div class="content">
                  <div
                    class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                  >{{$t('HelpCenter.T5')}}</div>
                  <ul>
                    <li class="rl-tc">
                      <div>
                        <img src="../assets/images/help/zh/post-cost1.png" alt />
                      </div>
                      <p>跨越速运资费</p>
                    </li>
                    <li class="rl-tc">
                      <div>
                        <img src="../assets/images/help/zh/post-cost2.png" alt />
                      </div>
                      <p>中通速运资费</p>
                    </li>
                    <li class="rl-tc">
                      <div>
                        <img src="../assets/images/help/zh/post-cost3.png" alt />
                      </div>
                      <p>顺丰速运资费</p>
                    </li>
                    <li class="rl-tc">
                      <div>
                        <img src="../assets/images/help/zh/post-cost4.png" alt />
                      </div>
                      <p>申通速运资费</p>
                    </li>
                    <li class="rl-tc">
                      <div>
                        <img src="../assets/images/help/zh/post-cost5.png" alt />
                      </div>
                      <p>龙邦速运资费</p>
                    </li>
                    <li class="rl-tc">
                      <div>
                        <img src="../assets/images/help/zh/post-cost6.png" alt />
                      </div>
                      <p>EMS速运资费</p>
                    </li>
                    <li class="rl-tc">
                      <div>
                        <img src="../assets/images/help/zh/post-cost7.png" alt />
                      </div>
                      <p>邮政速运资费</p>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'"></div>
            </div>
            <!-- 支付方式 -->
            <div class="right rl-fl" v-show="activeTab === 6">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T6')}}</div>
                <div class="content">
                  
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T6')}}</div>
                <div class="content">
                  <p class="rl-text-bold">1. Payment Currency</p>
                  <p
                    class="text-indent"
                  >Overseas version of bat provides you with different payment methods according to the payment currency you choose. If you choose to check out in RMB, we offer two payment methods: Alipay and WeChat Pay. Or you can choose the offline bank transfer for payment.</p>
                </div>
              </div>
            </div>
            <div class="right rl-fl" v-show="activeTab === 7">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T7')}}</div>
                <div class="content">
                  <p
                    class="text-indent"
                  >订单完成，点击付款，付款页面中选择开具发票类型、抬头和发票内容，发票类型主要分为“普通发票”和“增值税发票”两种，普通发票分"个人"和"单位"两种。如下：</p>
                  <p class="text-indent">1、“普通发票”→个人→发票内容（明细、办公用品、电脑配件、耗材选其一）→确认发票信息；</p>
                  <p class="text-indent">2、“普通发票”→单位→填写单位名称→发票内容（明细、办公用品、电脑配件、耗材选其一）→确认发票信息；</p>
                  <p class="text-indent">3、“增值税发票”→填写单位名称、纳税人识别号、注册地址、注册电话、开户银行、银行账户→确认发票信息。</p>
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T7')}}</div>
                <div class="content">
                  <p
                    class="text-indent"
                  >If you need to issue an invoice, please offline contact commercial staff for handling.</p>
                </div>
              </div>
            </div>
            <!-- 客服中心 -->
            <div class="right rl-fl" v-show="activeTab === 10">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T10')}}</div>
                <div class="content">
                  <p class="text-indent">
                    <b>邮箱：</b>
                  </p>
                  <p class="text-indent">
                    <b>微信公众号：</b>
                  </p>
                  <div class="rl-margin-left-default" style="width: 135px">
                    <img style="width:100%" src="" alt />
                  </div>
                  <p class="text-indent">
                    <b>微博：</b>
                  </p>
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T10')}}</div>
                <div class="content">
                  <p class="text-indent">
                    <b>Email: </b>
                  </p>
                  <p class="text-indent">
                    <b>Facebook：</b>
                  </p>
                  <p class="text-indent">
                    <b>Instragram:</b>
                  </p>
                </div>
              </div>
            </div>
            <!-- 售后政策 -->
            <div class="right rl-fl" v-show="activeTab === 11">
              <div class="content" v-show="$i18n.locale === 'zh'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T11')}}</div>
                <div class="content">
                  <p class="rl-text-bold">一、客户服务政策：</p>
                  <p class="text-indent">1. 本政策适用于在中华人民共和国境内合法销售的【BAT商城】所有产品。（除台湾、香港、澳门地区）。</p>
                  <p class="text-indent">2. 保修的基本条件：【BAT商城】网站上的产品在保修期内出现了由产品自身原因导致的质量问题。</p>
                  <p class="rl-text-bold rl-margin-top-default">二、政策说明：</p>
                  <p class="text-indent">1. 本网站所售产品均实行三包政策，请顾客保存好有效凭证（发票，保修卡），以确保我们为您更好服务。</p>
                  <p class="text-indent">2. 保修卡及发票（或购买凭证）正本需要妥善保存，并在需要保修服务时一起出示。</p>
                  <p class="text-indent">3. 在保修期内发生零件或产品更换的，均不延长或重新计算产品的保修期。</p>
                  <p class="text-indent">4. 在保修服务中更换下来的零件或产品，均不返还用户。</p>
                  <p
                    class="text-indent"
                  >5. 商城网站图片及信息仅供参考。因拍摄灯光及不同显示器色差等问题可能造成商品图片与实物有少许色差，一切以实物为准，不属质量问题。</p>
                  <p class="rl-text-bold rl-margin-top-default">三、各类产品的保质期限：</p>
                  <table>
                    <tr>
                      <td>序号</td>
                      <td>品类</td>
                      <td>保质期限</td>
                      <td>序号</td>
                      <td>品类</td>
                      <td>保质期限</td>
                    </tr>
                    <tr>
                      <td>1</td>
                      <td>电源类</td>
                      <td>1年</td>
                      <td>6</td>
                      <td>车品类</td>
                      <td>1年</td>
                    </tr>
                    <tr>
                      <td>2</td>
                      <td>充电类</td>
                      <td>1年</td>
                      <td>7</td>
                      <td>生活类</td>
                      <td>1年</td>
                    </tr>
                    <tr>
                      <td>3</td>
                      <td>无线充电类</td>
                      <td>1年</td>
                      <td>8</td>
                      <td>支架类</td>
                      <td>1年</td>
                    </tr>
                    <tr>
                      <td>4</td>
                      <td>线材、音频类</td>
                      <td>1年</td>
                      <td>9</td>
                      <td>柔性膜</td>
                      <td>3个月</td>
                    </tr>
                    <tr>
                      <td>5</td>
                      <td>游戏类</td>
                      <td>1年</td>
                      <td>-</td>
                      <td></td>
                      <td></td>
                    </tr>
                  </table>
                  <p
                    class="rl-margin-left-lllg rl-margin-top-default rl-margin-bottom-default"
                  >对膜切机专用柔性防爆膜产品质保期分别定为：国内 3 个月，海外 4 个月。时间为产品从BAT仓库发货日期起开始计算，即产品发货日期。超过规定的质保期【BAT商城】不接受产品的售后服务。产品存储环境要求：温度 25±3℃，湿度 35%～75%。</p>
                  <p
                    class="text-indent"
                  >1. 客户发起柔性防爆膜质量问题并要求做售后时，请通过网站售后部门的联系方式与【BAT商城】售后人员联系，经【BAT商城】判定为产品质量问题后，退货需要满足如下条件：</p>
                  <p class="text-indent rl-margin-left-mid">a) 产品在质保期内，即产品从我司发出的日期起计算；</p>
                  <p class="text-indent rl-margin-left-mid">b) 小批量退货（2 盒以上）的要求整盒未拆封，且每盒耗材码不可有使用过；</p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >c) 退货时依BAT要求提供每盒耗材码给到BAT做集中处理，无耗材码或者耗材码被使用过的不接受退货，个别单片（50 片以下含 50 片）的除外。</p>
                  <p class="text-indent">
                    <span>&#10022;</span>不列入售后政策的产品类别：
                  </p>
                  <p class="text-indent rl-margin-left-mid">以下产品类别一经使用则不纳入售后政策服务范围，如下：</p>
                  <p class="text-indent rl-margin-left-mid">1. 手机保护类；（保护壳、保护膜等）；</p>
                  <p class="text-indent rl-margin-left-mid">2. 汽车香水、香薰、及其他香水类化学商品；</p>
                  <p class="text-indent rl-margin-left-mid">3. 腰包、臂带、汽车靠枕、靠垫；</p>
                  <p class="text-indent rl-margin-left-mid">4. 易耗类产品。</p>
                  <p class="rl-text-bold rl-margin-top-default">四、售后细则：</p>
                  <p
                    class="text-indent"
                  >1. 在本网站购买的商品，自购买日起(以发货登记为准) 7天内本产品出现自身原因导致的功能故障，经由我司售后部门/厂商特约维修中心检测确定，可选择退货退款、换货或维修服务。</p>
                  <p
                    class="text-indent"
                  >2. 在本网站购买的商品，自购买日起(以发货登记为准) 8日-15日内，本产品出现自身原因导致的功能故障，经由我司售后部门/厂商特约维修中心检测确定，可选择换货或者维修服务；换货时，销售商应当为消费者调换同型号同规格的产品。</p>
                  <p
                    class="text-indent"
                  >3. 在本网站购买的商品，自购买日起(以发货登记为准)12个月内，本产品出现自身原因导致的功能故障，经由我司售后部门/厂商特约维修中心检测确定，本公司承诺免费保修。</p>
                  <p
                    class="text-indent"
                  >4. 在三包有效期内，修理两次，仍不能正常使用的产品，凭制造商提供的维修记录和证明，我司为消费者免费调换同型号同规格的产品或者按 第6 条的规定退货。</p>
                  <p class="text-indent">5. 在三包有效期内，修理期限超过 30 日的，由我司免费为消费者调换同型号同规格产品。</p>
                  <p
                    class="text-indent"
                  >6. 在三包有效期内，符合换货条件的，销售商无同型号同规格产品，消费者不愿调换其他型号、规格产品而要求退货的，我司予以退货；有同型号同规格产品，消费者不愿调换而一定要求退货的，我司予以退货，但会对已使用过的商品按国家相关法规收取折旧费。折旧费计算自开具发票之日起退货之日止，其中应当扣除修理占用和待修的时间。</p>
                  <p class="rl-text-bold rl-margin-top-default">五、对于以下情况，不实行保修服务，包括：</p>
                  <p
                    class="text-indent"
                  >1. 未经授权的维修、误用、碰撞、疏忽、滥用、进液、事故、改动、不正确的使用非本产品配件，或撕毁、涂改条码标贴、防伪标记导致无法识别；</p>
                  <p class="text-indent">2. 不能出示保修凭证，或保修凭证与实物不符；</p>
                  <p class="text-indent">3. 产品的自然损耗，磨损，老化等现象；</p>
                  <p class="text-indent">4. 已超过保修期；</p>
                  <p class="text-indent">5. 雷击、用电系统造成的损坏；</p>
                  <p class="text-indent">6. 因不可抗力造成的损坏；</p>
                  <p class="text-indent">7. 非本平台授权正规渠道销售的产品；</p>
                  <p class="text-indent">8. 未按说明书使用、保管、维护而造成的损坏。</p>
                  <p class="rl-text-bold rl-margin-top-default">六、返厂规则：</p>
                  <p class="text-indent">1. 三包有效期自产品从我司仓库出货日起计算，三包有效期内消费者凭发票及三包凭证办理修理、换货、退货。</p>
                  <p
                    class="text-indent"
                  >2. 办理退换、维修前，应与我司售后人员联系，取得RMA号码后，按要求填写返品清单，否则将不予受理，由此造成的损失，自行承担。</p>
                  <p
                    class="text-indent"
                  >3. 若办理退货，请将已开具的商品发票、发货单随退货商品一并寄回。商品附件（包含发票、说明书、保修单、标签等）齐全，如有附加赠品，请将附加品一同退回，需保持收到时的原质原样，如不能齐全退回，将折算相应费用并通知到您，并与您确认处理意见。</p>
                  <p
                    class="text-indent"
                  >4. RMA清单内容填写清晰、详细、正确、完整；返回不良品总数量、包装件（箱）数、送修清单页数、填写日期和联系人应填写；装箱清清单须填写数量、产品的相关信息，以备后期查询。返回品数量以我司收到的实数为准。</p>
                  <p class="text-indent">5. 对于返回品，用户应承诺产品未接触任何有害物质，避免对售后人员造成伤害。</p>
                  <p class="text-indent rl-text-bold">注意事项：</p>
                  <p class="text-indent">1. 申报故障维修，但经我司售后检测并无故障的产品，将依据我司的实际检测情况做退回或收费处理。</p>
                  <p
                    class="text-indent"
                  >2. 返回品包装时须品类分隔开、整齐放置；不得将所有产品随意堆放在一起返厂。因返回品包装不良引起的运输过程中造成损坏或新增缺陷，由客户自行承担。</p>
                  <p class="text-indent">3. 适配器、数据线等带线材器件必须用捆扎线捆好。</p>
                  <p class="text-indent">4. 如果产品在非保修范畴内而客户提出需要维修，需与我司售后部门协商后处理，依据协商情况做退回或收费处理。</p>
                  <p class="rl-text-bold rl-margin-top-default">七、物流费用：</p>
                  <p
                    class="text-indent"
                  >按照谁发货谁付费的原则，拒绝接受到付。 客户寄回维修时寄回运费由客户承担，产品维修后寄出的物流费用由维修方承担。 返回品如通过物流发货，请选送货上门，如未选择送货上门，产生的费用由发货方承担。</p>
                  <p class="rl-text-bold rl-margin-top-default">八、海外售后规则：</p>
                  <p
                    class="text-indent"
                  >1. 海外客户应在本公司购买的商品仅遵循以上三包政策、质保期规则和上述其他规则。本公司售后政策不延伸至海外各地区，当与海外当地售后法规不符时，由各代理商自行承担相应责任和义务。</p>
                  <p
                    class="text-indent"
                  >2. 为提高售后服务效率，保障客户权益，海外产品经售后部判定不便退回的部分可采取提供产品不良的视频给品质确认，由售后部两个工作日内给出产品的售后处理方案。</p>
                  <p
                    class="text-indent"
                  >3. 不良视频采用验证排除法，视频内容必须包含：用合格产品适配手机确认 OK，客户再用反馈的不良品在同等条件下验证确认为产品不良。如反馈充电器不良，先用 OK 的充电器给手机充电，手机能正常充电。然后用反馈不良的充电器插在同一个位置，数据线和手机保持不变，在同等条件下无法给手机充电，表示客户反馈充电器异常现象属实，可正常做产品的售后服务。</p>
                  <p
                    class="text-indent"
                  >4. 客户提供的验证判定不合理的产品佐证经售后部门确认不接受无法做售后，但客户不认同售后处理意见的。销售协助退回，收到产品实物后确认非产品质量问题的由客户承担相对应的物流费用。</p>
                  <p class="text-indent">5. 针对客户发起的产品售后要求，在收到产品实物或产品佐证资料通过后 7 个工作日内处理完成。</p>
                  <p class="text-indent">6. 质保时间计算规则：自产品从BAT仓库发货时间起开始起计算，即为产品质保周期起始时间，产品类别参考表1-1.</p>
                  <p class="text-indent">7. 产品售后退回时，应与我司售后取得联系，按要求格式填写RMA单和返品明细清单。</p>
                </div>
              </div>
              <div class="content" v-show="$i18n.locale === 'en'">
                <div
                  class="rl-tc head rl-padding-top-default rl-padding-bottom-default rl-text-mid rl-text-bold"
                >{{$t('HelpCenter.T11')}}</div>
                <div class="content">
                  <p class="rl-text-bold">I. Customer service policy:</p>
                  <p
                    class="text-indent"
                  >1. This policy applies to all products of [bat] legally sold in Chinese mainland.</p>
                  <p
                    class="text-indent"
                  >2. The basic conditions of the warranty: products on the [bat] website have quality problems caused by the product itself during the warranty period.</p>
                  <p class="rl-text-bold rl-margin-top-default">II. Policy description:</p>
                  <p
                    class="text-indent"
                  >1. The products sold on this website are subject to "three guarantees" (repair, replacement and return). Please keep valid certificates (invoices, warranty cards) well to ensure that we serve you in time.</p>
                  <p
                    class="text-indent"
                  >2. The original warranty card and invoice (or proof of purchase) need to be kept properly and presented together when you need warranty service.</p>
                  <p
                    class="text-indent"
                  >3. If parts or products are replaced during the warranty period, the warranty period of the product will not be extended or recalculated.</p>
                  <p
                    class="text-indent"
                  >4. Parts or products replaced during the warranty service will not be returned to the user.</p>
                  <p
                    class="text-indent"
                  >5. The pictures and information on the mall website are for reference only. Due to problems such as shooting lights and color differences of different monitors, there may be a slight color difference between the product picture and the actual product. The actual product shall be prevail and a slight color difference is not a quality problem.</p>
                  <p
                    class="rl-text-bold rl-margin-top-default"
                  >III. Warranty period of various products:</p>
                  <table>
                    <tr>
                      <td>No</td>
                      <td>Category</td>
                      <td>Warranty period</td>
                      <td>No</td>
                      <td>Category</td>
                      <td>Warranty period</td>
                    </tr>
                    <tr>
                      <td>1</td>
                      <td>Power bank</td>
                      <td>1 year</td>
                      <td>6</td>
                      <td>In-car product</td>
                      <td>1 year</td>
                    </tr>
                    <tr>
                      <td>2</td>
                      <td>Charging product</td>
                      <td>1 year</td>
                      <td>7</td>
                      <td>Life product</td>
                      <td>1 year</td>
                    </tr>
                    <tr>
                      <td>3</td>
                      <td>Wireless charging product</td>
                      <td>1 year</td>
                      <td>8</td>
                      <td>Stand</td>
                      <td>1 year</td>
                    </tr>
                    <tr>
                      <td>4</td>
                      <td>Cable &amp; audio product</td>
                      <td>1 year</td>
                      <td>9</td>
                      <td>Flexible film</td>
                      <td>3 months</td>
                    </tr>
                    <tr>
                      <td>5</td>
                      <td>Gaming product</td>
                      <td>1 year</td>
                      <td>-</td>
                      <td></td>
                      <td></td>
                    </tr>
                  </table>
                  <p
                    class="rl-margin-left-lllg rl-margin-top-default rl-margin-bottom-default"
                  >The warranty period for flexible explosion-proof film dedicated to plotter is respectively: 3 months in China and 4 months in overseas. The time is calculated from the shipping date of the Bat warehouse, that is, the product delivery date. For those products that expire the specified warranty period, [bat] will not provide after-sales service. Product storage environment requirements: temperature of 25±3℃, humidity of 35% to 75%.</p>
                  <p
                    class="text-indent"
                  >1. When the customer raises the quality issue of the flexible explosion-proof film and asks for after-sales service, please contact our after-sales personnel beforehand. When the product is judged as quality problem, the return shall meet the following conditions:</p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >a) The product is within the warranty period, that is, calculated from the shipping date from our company;</p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >b) The requirement for small batch returns (more than 2 boxes) is that the entire box is not unpacked and that each box of consumable codes must not be used;</p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >c) According to Bat’s requirements, consumable code of each box should be provided to Bat for centralized processing. Products without consumables codes or consumables codes that have been used will not be accepted for return, except for individual single-piece (50 pieces or less).</p>
                  <p class="text-indent">
                    <span>&#10022;</span>Product categories not included in the after-sales policy:
                  </p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >The following product categories are not included in the scope of after-sales policy services once used:</p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >1. Phone protection products (protection case, protective film, etc.);</p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >2. Car perfume, aroma, and other perfume chemical products;</p>
                  <p
                    class="text-indent rl-margin-left-mid"
                  >3. Waist bags, armbands, car headrests, cushions;</p>
                  <p class="text-indent rl-margin-left-mid">4. Consumable products.</p>
                  <p class="rl-text-bold rl-margin-top-default">IV. After-sales rules:</p>
                  <p
                    class="text-indent"
                  >1. Within 7 days from the date of purchase from our website (subject to the delivery registration), customers may make return &amp; refund, replacement or repair in case of product malfunction caused by its own reasons once determined by our after-sales department/manufacturer's special maintenance center.</p>
                  <p
                    class="text-indent"
                  >2. Within 8~15 days from the date of purchase from our website (subject to the delivery registration), customers may make replacement or repair in case of product malfunction caused by its own reasons once determined by our after-sales department/manufacturer's special maintenance center. When replacing the goods, we shall exchange the product with the same model and specifications for you consumer.</p>
                  <p
                    class="text-indent"
                  >3. Within 1 year from the date of purchase from our website (subject to the delivery registration), customers may get free warranty in case of product malfunction caused by its own reasons once determined by our after-sales department/manufacturer's special maintenance center.</p>
                  <p
                    class="text-indent"
                  >4. Within the validity period of "three guarantees", if the goods still cannot be used normally after repairing for twice, they can be replaced with the goods of the same model and specifications for customers free of charge or returned according to the provisions of 6, with the maintenance records and certificates provided by the manufacturer.</p>
                  <p
                    class="text-indent"
                  >5. If goods are within the validity period of "three guarantees" but have been more than 30 days after repair period, they can be replaced with the goods of the same model and specifications for customers free of charge.</p>
                  <p
                    class="text-indent"
                  >6. If goods are within the validity period of "three guarantees" and meet replacement conditions, but customers are unwilling to replace them with goods of other model and specifications due to lack of goods of the same model and specifications, we will agree to the customer's return request; if customers are unwilling to replace them with goods of the same model and specifications and instead of return, we will agree to the customer's return request, but will charge depreciation cost for the goods used according to relevant national laws and regulations. The depreciation cost shall be charged from the date of invoicing to the date of return, excluding the time of repair and awaiting repair.</p>
                  <p
                    class="rl-text-bold rl-margin-top-default"
                  >V. There is no warranty service for the following situations:</p>
                  <p
                    class="text-indent"
                  >1. Unauthorized maintenance, misuse, collision, negligence, abuse, liquid infusion, accident, modification, incorrect use of non-product accessories, or tearing or altering barcode labels, anti-counterfeiting marks cause unrecognizable;</p>
                  <p
                    class="text-indent"
                  >2. No warranty certificate or the warranty certificate does not match the actual product;</p>
                  <p class="text-indent">3. Natural loss, wear and aging of products;</p>
                  <p class="text-indent">4. The warranty period has been exceeded;</p>
                  <p
                    class="text-indent"
                  >5. Damage caused by lightning strikes and electricity systems;</p>
                  <p class="text-indent">6. Damage caused by force majeure;</p>
                  <p
                    class="text-indent"
                  >7. Products not authorized by this platform to be sold through regular channels;</p>
                  <p
                    class="text-indent"
                  >8. Damage caused by failure to use, store and maintain according to the user manual.</p>
                  <p class="rl-text-bold rl-margin-top-default">VI. Return rules:</p>
                  <p
                    class="text-indent"
                  >1. The validity period of the “three guarantees” is calculated from the date of shipment from the warehouse of our company, during which customers could repair, exchange or return the goods via invoice and “three guarantees” certificate.</p>
                  <p
                    class="text-indent"
                  >2. Before handling the return and maintenance, please contact our after-sales personnel. After obtaining the RMA number, fill in the returned product list as required, otherwise it will not be accepted, and the resulting losses shall be borne by yourself.</p>
                  <p
                    class="text-indent"
                  >3. If you want to return the goods, please return the invoice issued and ship order with the returned goods. Commodity accessories (including invoices, manuals, warranty sheets, labels, etc.) with additional gifts(if any) shall be returned together and must be kept the same as they are received. If they cannot be fully returned, the corresponding fee will be converted and notified to you, and the processing opinion will be confirmed with you.</p>
                  <p
                    class="text-indent"
                  >4. The contents of the RMA list should be filled in clearly, detailedly, correctly, and completely; The total number of defective products returned, the number of packages (boxes), the number of pages for the repair list, the date of filling, and the contact person should be filled in; Packing list must be filled in the related information of quantity and product for later inquiry. The quantity of returned products is based on the real number received by our company.</p>
                  <p
                    class="text-indent"
                  >5.For returned products, the user should promise that the product has not been exposed to any harmful substances to avoid injury to after-sales personnel.</p>
                  <p class="text-indent rl-text-bold">Precautions:</p>
                  <p
                    class="text-indent"
                  >1. Products that have been declared for fault maintenance but have not been detected by our company after-sales department will be returned or charged according to our actual detection situation.</p>
                  <p
                    class="text-indent"
                  >2. When packaging the returned products, the categories must be separated and neatly placed; all products should not be piled together and returned to the factory. Damages or new defects caused during the transportation process due to poor packaging of returned products shall be borne by the customer.</p>
                  <p
                    class="text-indent"
                  >3. Adapters, data cables and other devices with cables must be bundled with binding wires.</p>
                  <p
                    class="text-indent"
                  >4. If the product is beyond the scope of warranty and the customer claims that it needs to be repaired, then it will be returned or charged according to the negotiation result between the customer and our after-sales department.</p>
                  <p class="rl-text-bold rl-margin-top-default">VII. Logistics costs:</p>
                  <p
                    class="text-indent"
                  >According to the principle of who pays what is shipped, refuse to accept freight collect.When the customer returns the goods to repair, the shipping cost is borne by the customer, and the logistics cost sent after the product is repaired is borne by the maintenance party.If the returned goods are shipped through logistics, please choose home delivery service. If you do not choose home delivery service, the cost will be borne by the shipper.</p>
                  <p class="rl-text-bold rl-margin-top-default">VIII. Overseas after-sales rules:</p>
                  <p
                    class="text-indent"
                  >1. Products purchased by overseas customers in our company only follow the above “three guarantees” policy, warranty period rules and other rules mentioned above. Our company's after-sales policy does not extend to overseas regions. When it is inconsistent with overseas after-sales regulations, each agent shall bear corresponding responsibilities and obligations.</p>
                  <p
                    class="text-indent"
                  >2. In order to improve the efficiency of after-sales services and guarantee the rights and interests of customers, if goods sold overseas cannot be returned after being determined by the Company, video of defective goods may be provided so that we will work out an after-sales solution to the goods within two working days.</p>
                  <p
                    class="text-indent"
                  >3. Defective goods video adopts exclusive method. That is customer should shoot the video that contains tests of qualified product and defective product that claims under the same conditions with the same phone. For example, if the customer claims the charger is bad, then the video content should include that customer use the qualified charger to charge the phone first, and the phone can be charged normally. Then keep other conditions be the same, charge the mobile phone with poor quality charger, if the mobile phone cannot be charged, indicating that the customer feedback that the charger is abnormal is true, and the product after-sales service can be done normally.</p>
                  <p
                    class="text-indent"
                  >4. According to the video provided by customers, if after-sales services cannot be provided upon confirmation by the our company, but customers do not agree with this, customers may contact sales department for assistance in return. After receiving the goods, if the goods have no quality problems upon confirmation by our company, customers shall bear the corresponding logistics cost.</p>
                  <p
                    class="text-indent"
                  >5. In response to the product after-sales request initiated by the customer, it will be processed and completed within 7 working days after receiving the actual product or product supporting documents.</p>
                  <p
                    class="text-indent"
                  >6. Calculation rules of warranty period: The warranty period is calculated from delivery time of goods from warehouse of Shenzhen Bat Excellent Technology Co., Ltd. Please refer to sheet 1-1 for warranty period of different product categories.</p>
                  <p
                    class="text-indent"
                  >7. When the product needs to be returned for after-sales service, customer should contact our after-sales personnel beforehand and fill in the RMA form and the detailed list of returned products in the required format.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--公共底部-->
      <Footer></Footer>
    </div>
  </div>
</template>

<script>
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
export default {
  name: "HelpCenter",
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      leftTabList: [],
      activeTab: Number(this.$route.query.index),
      help: true,
    };
  },
  mounted() {
    this.getLeftTab();
  },
  methods: {
    /**
     * 根据语言获取Tab标签
     */
    getLeftTab() {
      this.leftTabList = [
        { id: 0, name: this.$t("HelpCenter.T0"), show: true },
        { id: 1, name: this.$t("HelpCenter.T1"), show: true },
        { id: 2, name: this.$t("HelpCenter.T2"), show: true },
        { id: 3, name: this.$t("HelpCenter.T3"), show: true },
        { id: 4, name: this.$t("HelpCenter.T4"), show: true },
        { id: 5, name: this.$t("HelpCenter.T5"), show: true },
        { id: 6, name: this.$t("HelpCenter.T6"), show: true },
        { id: 7, name: this.$t("HelpCenter.T7"), show: true },
        { id: 7, name: this.$t("HelpCenter.T8"), show: true },
        { id: 9, name: this.$t("HelpCenter.T9"), show: true },
        { id: 10, name: this.$t("HelpCenter.T10"), show: true },
        { id: 11, name: this.$t("HelpCenter.T11"), show: true },
      ];
      if (this.$i18n.locale === "zh") {
        this.leftTabList[0].show = false;
        this.leftTabList[1].show = false;
        this.leftTabList[8].show = false;
        this.leftTabList[9].show = false;
        this.leftTabList[10].show = false;
        this.leftTabList[11].show = false;
      }
      if (this.$i18n.locale === "en") {
        this.leftTabList[0].show = false;
        this.leftTabList[1].show = false;
        this.leftTabList[3].show = false;
        this.leftTabList[4].show = false;
        this.leftTabList[5].show = false;
        this.leftTabList[8].show = false;
        this.leftTabList[9].show = false;
        this.leftTabList[10].show = false;
        this.leftTabList[11].show = false;
      }
    },
    /**
     * 标签切换
     * @param type
     */
    changeState(type) {
      this.activeTab = type;
    },
  },
  watch: {
    "$i18n.locale"() {
      this.getLeftTab();
    },
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../assets/less/variable.less");
.index {
  width: 100%;
}
.main {
  width: 1200px;
  .container {
    padding-left: 10px;
    padding-left: 10px;
    .content {
      .left {
        width: 250px;
        margin-top: 10px;
        .head {
          height: 50px;
          line-height: 50px;
          color: @blue;
          border: 1px solid @entryBd;
        }
        ul {
          li {
            margin-top: -1px;
            padding-left: 30px;
            height: 50px;
            line-height: 50px;
            font-size: 15px;
            border: 1px solid @entryBd;
            cursor: pointer;
            &.active,
            &:hover {
              color: @white;
              background-color: @blue;
            }
          }
        }
      }
      .right {
        padding-bottom: 30px;
        padding-left: 60px;
        padding-right: 20px;
        width: 830px;
        .head {
          margin-bottom: 20px;
          width: 100%;
          font-size: 18px;
          font-weight: normal;
          border-bottom: 1px solid @entryBd;
        }
        .content {
          p {
            font-size: 12px;
            line-height: 30px;
          }
          .h-img {
            margin-top: 10px;
            width: 684px;
            text-align: center;
            img {
              width: 80%;
            }
          }
          ul {
            li {
              display: inline-block;
              width: 220px;
              div {
                width: 220px;
              }
            }
          }
          table,
          table tr th,
          table tr td {
            border: 1px solid #000000;
          }
          table {
            margin-left: 30px;
            width: 500px;
            min-height: 25px;
            line-height: 25px;
            text-align: center;
            border-collapse: collapse;
          }
        }
      }
    }
  }
}
.text-indent {
  text-indent: 30px;
}
</style>
