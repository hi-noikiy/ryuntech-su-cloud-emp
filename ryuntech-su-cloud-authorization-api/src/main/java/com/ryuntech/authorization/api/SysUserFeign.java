package com.ryuntech.authorization.api;

import com.ryuntech.common.model.CurrentUser;
import com.ryuntech.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "ryuntech-su-cloud-authorization")
public interface SysUserFeign {
    @PostMapping("/sys/getSysUserId")
    Result getSysUserId(@RequestParam("token") String token);
}
