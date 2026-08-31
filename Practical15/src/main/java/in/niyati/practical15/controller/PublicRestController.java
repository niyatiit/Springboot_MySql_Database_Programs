package in.niyati.practical15.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicRestController {

    // No authentication required - matches "/api/public/**" in SecurityConfig
    @GetMapping("/data")
    public String getPublicData() {
        return "This is public data - no login required.";
    }
}