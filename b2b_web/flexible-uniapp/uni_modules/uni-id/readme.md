**文档已移至[uni-id 文档](https://uniapp.dcloud.net.cn/uniCloud/uni-id)**

> 一般 uni-id 升级大版本时为不兼容更新，从低版本迁移到高版本请参考：[uni-id 迁移指南](https://uniapp.dcloud.net.cn/uniCloud/uni-id?id=migration)

## 重要升级说明

**uni-id 3.x 版本，搭配的 uniCloud admin 版本需大于 1.2.10。**

### 缓存角色权限

自`uni-id 3.0.0`起，支持在 token 内缓存用户的角色权限，默认开启此功能，各登录接口的 needPermission 参数不再生效。如需关闭请在 config 内配置`"removePermissionAndRoleFromToken": true`。

为什么要缓存角色权限？要知道云数据库是按照读写次数来收取费用的，并且读写数据库会拖慢接口响应速度。未配置`"removePermissionAndRoleFromToken": true`的情况下，可以在调用 checkToken 接口时不查询数据库获取用户角色权限。

详细 checkToken 流程如下：

![](https://vkceyugu.cdn.bspapp.com/VKCEYUGU-dc-site/ed45d350-5a4d-11eb-b997-9918a5dda011.png)

可以看出，旧版 token（removePermissionAndRoleFromToken 为 true 时生成的）在 checkToken 时如需返回权限需要进行两次数据库查询。新版 token 不需要查库即可返回权限信息。

**注意**

-   由于角色权限缓存在 token 内，可能会存在权限已经更新但是用户 token 未过期之前依然是旧版角色权限的情况。可以调短一些 token 过期时间来减少这种情况的影响。
-   admin 角色 token 内不包含 permission，如需自行判断用户是否有某个权限，要注意 admin 角色需要额外判断一下，写法如下
    ```js
    const { role, permission } = await uniID.checkToken(event.uniIdToken);
    if (role.includes("admin") || permission.includes("your permission id")) {
        // 当前角色拥有'your permission id'对应的权限
    }
    ```
