package com.nns.punto_venta.modules.debug;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/debug")
public class DebugController {
    
    @GetMapping
    public ResponseEntity<String> debug (){
        return ResponseEntity.ok("Debug");
    }

}
