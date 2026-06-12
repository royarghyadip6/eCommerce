package com.appconfiguration.config.controller;

import com.appconfiguration.config.BuildInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BuildInfoController {

 /*   @Value("${build.id:default-id}")
    private String buildId;
    @Value("${build.name:default-name}")
    private String buildName;
    @Value("${build.version:default-version}")
    private String buildVersion;*/

    private final BuildInfo buildInfo;

    @GetMapping("/build-info")
    public String getBuildInfo() {
//        return "Build ID: " + buildInfo.getId() + ",Build Owner: " + buildInfo.getOwner() + ", Build Name: " + buildInfo.getName() + ", Build Version: " + buildInfo.getVersion();
        return buildInfo.toString();
    }
}
