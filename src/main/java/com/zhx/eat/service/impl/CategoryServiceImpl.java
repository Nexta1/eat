package com.zhx.eat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhx.eat.common.CustomException;
import com.zhx.eat.entity.Category;
import com.zhx.eat.entity.Dish;
import com.zhx.eat.entity.Setmeal;
import com.zhx.eat.mapper.CategoryMapper;
import com.zhx.eat.service.CategoryService;
import com.zhx.eat.service.DishService;
import com.zhx.eat.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @Override
    public boolean removeById(Serializable categoryId) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//      1.  添加查条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, categoryId);
        Long count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("分类有菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
//      1.  添加查条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, categoryId);
        Long count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("关联套餐，不能删除");
        }
        return super.removeById(categoryId);

    }
}
