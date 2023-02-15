package com.zhx.eat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhx.eat.common.R;
import com.zhx.eat.entity.Employee;
import com.zhx.eat.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        // 1. 查询数据库中是否有该用户
//        Employee user = employeeService.getOne(new QueryWrapper<Employee>().eq("username", employee.getUsername()));
//        if (user == null) {
//            return R.error("登录失败");
//        }
//            md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null) {
            return R.error("登陆失败");
        }

        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败");
        }

        if (emp.getStatus() == 0) {
            return R.error("账号被禁用");
        }
        // 4. 返回用户信息，并保存到session中
        request.getSession().setAttribute("employee", emp);
        // 将数据写入到threadLocal中
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
