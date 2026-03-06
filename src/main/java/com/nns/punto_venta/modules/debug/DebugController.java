package com.nns.punto_venta.modules.debug;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/debug")
public class DebugController {

    private final BCryptPasswordEncoder encoder;

    public DebugController(BCryptPasswordEncoder encoder){
        this.encoder=encoder;
    }
    
    @GetMapping("/debug")
    public ResponseEntity<String> debug (){
        return ResponseEntity.ok("Debug");
    }


    
    @PostMapping("/password-encode")
    public String postMethodName(@RequestBody String password) {
    
        
        return encoder.encode(password);
    }
}
