package com.kevin30313.alkewallet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomeController {
	@GetMapping("/")
    public String home() {
        return "¡Bienvenido a AlkeWallet API! El acceso está libre.";

    }
}
