/*
 * Copyright (c) 2023 VMware, Inc. or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vmware.tanzu.demos.log4shelldemo;

import org.semver4j.Semver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

enum Log4ShellPatchStatus {
    PATCHED, UNPATCHED, UNKNOWN
}

@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    ModelAndView index() {
        return new ModelAndView("index", Map.of("status", getLog4ShellPatchStatus().toString()));
    }

    private Log4ShellPatchStatus getLog4ShellPatchStatus() {
        final Log4ShellPatchStatus status;
        final var sbVersionStr = SpringBootVersion.getVersion();
        if (sbVersionStr == null) {
            status = Log4ShellPatchStatus.UNKNOWN;
        } else {
            final var sbVersion = new Semver(sbVersionStr);
            if (sbVersion.getMajor() <= 2 && sbVersion.getMinor() <= 6 && sbVersion.getPatch() <= 1) {
                status = Log4ShellPatchStatus.UNPATCHED;
            } else {
                status = Log4ShellPatchStatus.PATCHED;
            }
        }
        logger.info("Got Log4Shell patch status: {}", status);
        return status;
    }
}
