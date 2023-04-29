package com.carara.controller;

import com.carara.service.IpTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class IpTrackerController {
    private final IpTrackerService ipTrackerService;

    public IpTrackerController(IpTrackerService ipTrackerService) {
        this.ipTrackerService = ipTrackerService;
    }

    @PostMapping("/request-handled")
    public ResponseEntity<Void> requestHandled(@RequestBody String ipAddress) {
        ipTrackerService.requestHandled(ipAddress);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top100")
    public ResponseEntity<List<Map.Entry<String, Integer>>> top100() {
        List<Map.Entry<String, Integer>> top100 = ipTrackerService.top100();
        return ResponseEntity.ok(top100);
    }

    @GetMapping("/clear")
    public ResponseEntity<Void> clear() {
        ipTrackerService.clear();
        return ResponseEntity.ok().build();
    }
}
