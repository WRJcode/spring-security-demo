### Spring Security演示demo

#### 1. 项目介绍

本项目是一个简单的Spring Security演示demo，主要演示了Spring Security的基本使用方法。

#### 2.引入依赖

```xml
<!-- spring security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

#### 3.配置Spring Security

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll() // 都可以访问
                .antMatchers("/users/**").hasRole("ADMIN") // 需要相应的角色才能访问
                .and()
                .formLogin() // 基于 Form 表单登录验证
                .loginPage("/login").failureUrl("/login-error"); // 自定义登录界面
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // 认证信息存储于内存中
                .withUser("admin").password("admin").roles("ADMIN");
    }
}
```

#### 4.编写登录界面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="col-md-4 col-md-offset-4">
        <form action="/login" method="post">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名">
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码">
            </div>
            <button type="submit" class="btn btn-default">登录</button>
        </form>
    </div>
</div>
</body>
</html>
```

#### 5.编写登录失败页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录失败</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="col-md-4 col-md-offset-4">
        <div class="alert alert-danger" role="alert">用户名或密码错误，登录失败！</div>
    </div>
</div>
</body>
</html>
```

#### 6.编写首页

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="col-md-4 col-md-offset-4">
        <div class="alert alert-success" role="alert">登录成功！</div>
    </div>
</div>
</body>
</html>
```

#### 7.编写用户管理页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="col-md-4 col-md-offset-4">
        <div class="alert alert-success" role="alert">用户管理页面！</div>
    </div>
</div>
</body>
</html>
```

#### 8.编写启动类

```java
@SpringBootApplication
public class SpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }
}
```

#### 9.SpringSecurity原理


