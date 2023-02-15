package com.zhx.eat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhx.eat.entity.Employee;
import com.zhx.eat.mapper.EmployeeMapper;
import com.zhx.eat.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
