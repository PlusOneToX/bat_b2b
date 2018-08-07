package com.bat.system.service.user.executor;

import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.user.dto.PermissionCreateCmd;
import com.bat.system.api.user.dto.PermissionUpdateCmd;
import com.bat.system.dao.user.PermissionMapper;
import com.bat.system.dao.user.dataobject.PermissionDO;
import com.bat.system.service.user.convertor.PermissionConvertor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.system.service.user.swaggerdto.ApiDocsDTO;
import com.bat.system.service.user.swaggerdto.ResourcesDTO;

import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class PermissionCmdExc {

    @Resource
    private PermissionMapper permissionMapper;

    public boolean createPermission(PermissionCreateCmd cmd) {
        try {
            permissionMapper.insert(PermissionConvertor.toPermissionDO(cmd));
        } catch (DuplicateKeyException e) {
            final String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Duplicate entry") && localizedMessage.contains("uk_url_path_method")) {
                throw SystemException
                    .buildException(com.bat.system.service.organization.executor.ErrorCode.B_PERMISSION_EXISTS);
            }
        }
        return true;
    }

    public boolean deletePermissionById(Integer id) {
        if (permissionMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_PERMISSION_ID_NOT_EXISTS);
        }
        return permissionMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updatePermission(PermissionUpdateCmd cmd) {
        if (permissionMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_PERMISSION_ID_NOT_EXISTS);
        }
        permissionMapper.updateByPrimaryKeySelective(PermissionConvertor.toPermissionDO(cmd));
        return true;
    }

    @SneakyThrows
    public void syncPermission(String gateWaySwaggerUrl) {
        List<PermissionDO> swaggerInfo = getSwaggerInfo(gateWaySwaggerUrl);
        swaggerInfo.forEach(permissionDO -> {
            PermissionDO permissionDO1 =
                permissionMapper.getByPathUrlAndMethod(permissionDO.getUrlPath(), permissionDO.getMethod());
            if (permissionDO1 == null) {
                permissionMapper.insert(permissionDO);
            } else {
                permissionDO.setId(permissionDO1.getId());
                permissionMapper.updateByPrimaryKeySelective(permissionDO);
            }
        });
    }

    static Map<String, String> map = new HashMap<>();

    static {
        map.put("order-service", "订单服务");
        map.put("flexible-custom-service", "柔性服务");
        map.put("warehouse-service", "仓库服务");
        map.put("system-service", "系统服务");
        map.put("goods-service", "商品服务");
        map.put("distributor-service", "客户服务");
        // map.put("thirdparty-service", "第三方服务");
        map.put("financial-service", "财务服务");
        map.put("promotion-service", "营销推广服务");
    }

    @SneakyThrows
    public static void main(String[] args) {
        // getSwaggerInfo(gateWaySwaggerUrl);
    }

    private static List<PermissionDO> getSwaggerInfo(String gateWaySwaggerUrl) throws IOException {
        List<PermissionDO> dos = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        if (gateWaySwaggerUrl.endsWith("/")) {
            gateWaySwaggerUrl = gateWaySwaggerUrl.substring(0, gateWaySwaggerUrl.length() - 1);
        }
        String url = gateWaySwaggerUrl + "/swagger-resources";
        final Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        String string = Objects.requireNonNull(response.body()).string();
        List<ResourcesDTO> resourcesDTOS = JSON.parseArray(string, ResourcesDTO.class);
        for (ResourcesDTO resourcesDTO : resourcesDTOS) {
            String url1 = gateWaySwaggerUrl + resourcesDTO.getUrl();
            final Request request1 = new Request.Builder().url(url1).build();
            final Call call1 = okHttpClient.newCall(request1);
            Response response1 = call1.execute();
            String string1 = Objects.requireNonNull(response1.body()).string();
            ApiDocsDTO apiDocsDTO = null;
            try {
                apiDocsDTO = JSON.parseObject(string1, ApiDocsDTO.class);
            } catch (Exception e) {
                continue;
            }
            Map<String, Map<String, ApiDocsDTO.Content>> paths = apiDocsDTO.getPaths();
            if (paths == null) {
                continue;
            }
            for (Map.Entry<String, Map<String, ApiDocsDTO.Content>> stringMapEntry : paths.entrySet()) {
                String path = stringMapEntry.getKey();
                if (!path.contains("/admin")) {
                    continue;
                }
                for (Map.Entry<String, ApiDocsDTO.Content> stringContentEntry : stringMapEntry.getValue().entrySet()) {
                    PermissionDO permissionDO = new PermissionDO();
                    permissionDO.setMethod(stringContentEntry.getKey());
                    permissionDO.setUrlPath(path);
                    // 如果swagger 服务有更改 info 优先取
                    String service = "默认服务";
                    if (!"微服务接口文档".equals(apiDocsDTO.getInfo().getTitle())) {
                        if (apiDocsDTO.getInfo().getTitle().contains("服务接口文档")) {
                            service = apiDocsDTO.getInfo().getTitle().substring(0,
                                apiDocsDTO.getInfo().getTitle().indexOf("服务接口文档")) + "模块";
                        } else {
                            service = apiDocsDTO.getInfo().getTitle();
                        }
                    } else if (map.get(resourcesDTO.getName()) != null) {
                        service = map.get(resourcesDTO.getName());
                    }
                    permissionDO.setService(service);
                    String module = stringContentEntry.getValue().getTags().get(0);
                    if (module.contains("后台接口")) {
                        // 如果是系统服务的商店配置模块 单独分开
                        if (module.startsWith("商店配置模块")) {
                            permissionDO.setService("商店配置模块");
                            module = module.substring(7, module.indexOf("后台接口")) + "管理";
                        } else if (module.startsWith("系统模块")) {
                            permissionDO.setService("系统模块");
                            module = module.substring(5, module.indexOf("后台接口")) + "管理";
                        } else {
                            module = module.substring(0, module.indexOf("后台接口")) + "管理";
                        }
                    }
                    permissionDO.setModule(module);
                    if (stringContentEntry.getKey().equals("get")) {
                        permissionDO.setPermissionName("查看");
                        if (path.contains("admin/")) {
                            String substring = path.substring(path.indexOf("admin/") + 6);
                            if (substring.startsWith("u/")) {
                                substring = substring.substring(substring.indexOf("u/") + 2);
                            }
                            if (substring.startsWith("p/")) {
                                substring = substring.substring(substring.indexOf("p/") + 2);
                            }
                            if (substring.startsWith("po/")) {
                                substring = substring.substring(substring.indexOf("po/") + 3);
                            }
                            if (substring.contains("/")) {
                                substring = substring.substring(0, substring.indexOf("/"));
                            }
                            // substring = substring.replace("/", "-");
                            permissionDO.setPermissionModule(substring + "-query");
                        }
                    } else {
                        permissionDO.setPermissionName("管理");
                        if (path.contains("admin/")) {
                            String substring = path.substring(path.indexOf("admin/") + 6);
                            if (substring.startsWith("u/")) {
                                substring = substring.substring(substring.indexOf("u/") + 2);
                            }
                            if (substring.startsWith("p/")) {
                                substring = substring.substring(substring.indexOf("p/") + 2);
                            }
                            if (substring.startsWith("po/")) {
                                substring = substring.substring(substring.indexOf("po/") + 3);
                            }
                            if (substring.contains("/")) {
                                substring = substring.substring(0, substring.indexOf("/"));
                            }
                            // substring = substring.replace("/", "-");
                            permissionDO.setPermissionModule(substring + "-manager");
                        }
                    }
                    dos.add(permissionDO);
                }
            }
        }
        return dos;
    }
}
