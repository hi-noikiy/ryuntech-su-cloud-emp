package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ryuntech.common.constant.Global;
import com.ryuntech.common.constant.RedisConstant;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.model.CurrentUser;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.dto.PermUriDTO;
import com.ryuntech.saas.api.helper.CustomErrorHandler;
import com.ryuntech.saas.api.mapper.CompanyMapper;
import com.ryuntech.saas.api.mapper.EmployeeMapper;
import com.ryuntech.saas.api.mapper.SysPermMapper;
import com.ryuntech.saas.api.model.Company;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.ICompanyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
@Service
public class CompanyServiceImpl extends BaseServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Autowired
    private SysPermMapper sysPermMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Value("${spring.auth.client_id}")
    String clientId;

    @Value("${spring.auth.client_secret}")
    String clientSecret;

    @Value("${spring.auth.grant_type}")
    String grantType;

    @Override
    public Company selectByCompany(Company company) {
        if (StringUtils.isNotBlank(company.getName())) {
            queryWrapper.eq("name", company.getName());
        }
        if (StringUtils.isNotBlank(company.getName())) {
            queryWrapper.eq("company_id", company.getCompanyId());
        }
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Result choose(String companyId, String sysUserId) throws Exception {
        // 用户身份判断
        if (!jedisUtil.exists(RedisConstant.PRE_LOGIN_USER_SUCCESS + sysUserId)) {
            return new Result(CommonEnums.OPERATE_ERROR, "暂无权限，请返回登录");
        }

        if (!jedisUtil.sismember(RedisConstant.PRE_LOGIN_USER_SUCCESS + sysUserId, companyId)) {
            return new Result(CommonEnums.OPERATE_ERROR, "暂无权限，请返回登录");
        }

        ResponseEntity<Map> response = getAccessToken(companyId, sysUserId);
        int expires = (int) response.getBody().get("expires_in");
        String token = (String) response.getBody().get("access_token");
        String refreshToken = (String) response.getBody().get("refresh_token");

        if (jedisUtil.exists(RedisConstant.PRE_CURRENT_USER + token)) {
            if (expires < Global.ACCESS_TOKEN_EXPIRE / 2) {
                // 重新生成token及获取有效时间
                response = refreshAccessToken(refreshToken);
                expires = (int) response.getBody().get("expires_in");
                token = (String) response.getBody().get("access_token");
            }
        }

        // 加载当前用户的权限key及uri
        List<PermUriDTO> permUriList = sysPermMapper.getPermUri(companyId, sysUserId);
        List<String> uriList = permUriList.stream().map(p -> p.getUri()).collect(Collectors.toList());
        Set<String> uris = new HashSet<>(uriList);

        List<String> keyList = permUriList.stream().map(p -> p.getResKey()).distinct().collect(Collectors.toList());
        Set<String> keys = new HashSet<>(keyList);

        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", companyId);
        queryWrapper.eq("sys_user_id", sysUserId);
        Employee employee = employeeMapper.selectOne(queryWrapper);

        CurrentUser currentUser = new CurrentUser(sysUserId, companyId, employee.getEmployeeId(), employee.getName(), employee.getDataType(), employee.getDataDepartmentId(), uris, keys);
        jedisUtil.setex(RedisConstant.PRE_CURRENT_USER + token, expires, new Gson().toJson(currentUser));
        return new Result(token);
    }

    // auth获取token
    private ResponseEntity<Map> getAccessToken(String companyId, String uuid) throws Exception {
        ServiceInstance serviceInstance = loadBalancerClient.choose("ryuntech-su-cloud-gateway");
        URI uri = serviceInstance.getUri();
        String authUri = uri + "/api/auth/oauth/token";
        //authUri = authUri.replace("http", "https");
        authUri = "https://wx.ryuntech.com:9999/api/auth/oauth/token";

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization", httpBasic);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("username", companyId);
        body.add("password", uuid);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

        restTemplate.setErrorHandler(new CustomErrorHandler());
        ResponseEntity<Map> response = restTemplate.exchange(authUri, HttpMethod.POST, httpEntity, Map.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            LinkedHashMap hashMap = (LinkedHashMap) response.getBody();
            Object errorMsg = hashMap.get("error_description");
            throw new Exception(String.valueOf(errorMsg));
        }
        return response;
    }

    // auth刷新token
    private ResponseEntity<Map> refreshAccessToken(String refreshToken) throws Exception {
        ServiceInstance serviceInstance = loadBalancerClient.choose("ryuntech-su-cloud-gateway");
        URI uri = serviceInstance.getUri();
        String authUri = uri + "/api/auth/oauth/token";
        //authUri = authUri.replace("http", "https");
        authUri = "https://wx.ryuntech.com:9999/api/auth/oauth/token";

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization", httpBasic);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

        restTemplate.setErrorHandler(new CustomErrorHandler());
        ResponseEntity<Map> response = restTemplate.exchange(authUri, HttpMethod.POST, httpEntity, Map.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            LinkedHashMap hashMap = (LinkedHashMap) response.getBody();
            Object errorMsg = hashMap.get("error_description");
            throw new Exception(String.valueOf(errorMsg));
        }
        return response;
    }

    private String getHttpBasic(String client, String secret) {
        String s = client + ":" + secret;
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic " + new String(encode);
    }

}
