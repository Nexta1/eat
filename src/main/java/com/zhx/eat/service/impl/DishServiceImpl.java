package com.zhx.eat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhx.eat.Dto.DishDto;
import com.zhx.eat.entity.Dish;
import com.zhx.eat.entity.DishFlavor;
import com.zhx.eat.mapper.DishMapper;
import com.zhx.eat.service.CategoryService;
import com.zhx.eat.service.DishFlavorService;
import com.zhx.eat.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    @Override
    public void addDish(DishDto dishDto) {
        // 首先封装菜品信息并保存
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        save(dish);

        List<DishFlavor> flavors = dishDto.getFlavors();
        // 封装flavors信息并且批量保存
        flavors.stream().peek((item) -> item.setDishId(dish.getId())).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public Page<DishDto> pageSearch(int page, int pageSize, String name) {
        // 创建分页
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        // 返回页面的分页
        Page<DishDto> dishDtoPage = new Page<>();
        QueryWrapper<Dish> employeeQueryWrapper = new QueryWrapper<>();

        employeeQueryWrapper.orderByDesc("create_time");
        if (name != null) {
            employeeQueryWrapper.like("name", name);
        }
        // 查出信息
        page(pageInfo, employeeQueryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage);

        // 根据id查询出菜品名
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> recordsDto = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            String categoryName = categoryService.getById(item.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(recordsDto);
        return dishDtoPage;
    }

    @Override
    public DishDto getDishById(Long id) {
        // 根据id查询菜品信息
        DishDto dishDto = new DishDto();
        Dish dish = getById(id);
        // 根据id查询口味信息
        List<DishFlavor> dishFlavors = dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id", id));

        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavors);
        // 返回数据
        return dishDto;
    }

    @Override
    public void updateDish(DishDto dishDto) {
        // 首先封装菜品信息并保存
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        updateById(dish);

        List<DishFlavor> flavors = dishDto.getFlavors();
        // 封装flavors信息并且批量保存
        flavors = flavors.stream().peek((item) -> item.setDishId(dish.getId())).collect(Collectors.toList());
        dishFlavorService.updateBatchById(flavors);
    }

    public void deleteDish(String ids) {
        String[] list = ids.split(",");
        for (String id : list) {
            // 删除菜品
            removeById(Long.parseLong(id));
            // 删除菜品对应口味信息
            dishFlavorService.remove(new QueryWrapper<DishFlavor>().eq("dish_id", id));
        }
    }
}
