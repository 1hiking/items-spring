package org.hik.items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class ItemsApplication {

    private static final Logger log = LoggerFactory.getLogger(ItemsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ItemsApplication.class, args);
    }

    @GetMapping("/")
    public String redirectToItems() {
        log.info("Redirecting to /item/");
        return "redirect:/item/";
    }

}
