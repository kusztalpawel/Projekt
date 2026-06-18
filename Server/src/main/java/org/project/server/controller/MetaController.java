package org.project.server.controller;

import org.project.server.model.AchievementMetric;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/meta")
public class MetaController {

    @GetMapping("/achievement-metrics")
    public List<String> getAchievementMetrics() {
        return Arrays.stream(AchievementMetric.values())
                .map(Enum::name)
                .toList();
    }
}