package org.alvin.security.controller;

import org.alvin.security.service.MethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//@RequestMapping("")
@RestController
public class HelloController {

         @Autowired
        MethodService methodService;

        @GetMapping("/hello/{name}")
        public String hello(@PathVariable("name") String name){
            return "hello , " + name + " !";
        }

        @GetMapping("/user/hello")
        public String userHello(){
            return "你好，普通用户！";
        }

        @GetMapping("/admin/hello")
        public String adminHello(){
            return "你好，管理员！";
        }

        @GetMapping("/db/hello")
        public String dbaHello(){
            return "你好，数据库管理员！";
        }

       @GetMapping("/method/admin")
       public String methodAdmin(){
           return methodService.admin();
       }

       @GetMapping("/method/user")
         public String methodUser(){
              return methodService.user();
         }

         @GetMapping("/method/hello")
            public String methodHello(){
                return methodService.hello();
            }


}
