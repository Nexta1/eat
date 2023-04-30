package com.zhx.eat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhx.eat.Dto.SetmealDto;
import com.zhx.eat.common.CustomException;
import com.zhx.eat.entity.Setmeal;
import com.zhx.eat.entity.SetmealDish;
import com.zhx.eat.mapper.SetmealMapper;
import com.zhx.eat.service.CategoryService;
import com.zhx.eat.service.SetmealDishService;
import com.zhx.eat.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    CategoryService categoryService;

    @Override
//    public void add(SetmealDto setmealDto) {
//        Setmeal setmeal = new Setmeal();
//        BeanUtils.copyProperties(setmealDto, setmeal);
//        save(setmeal);
//        List<SetmealDish> setmealDish = setmealDto.getSetmealDishes();
//
//        setmealDish.stream().peek(item -> item.setSetmealId(setmeal.getId())).collect(Collectors.toList());
//        setmealDishService.saveBatch(setmealDish);
//
//    }
    public void add(SetmealDto setmealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        save(setmeal);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(item -> item.setSetmealId(setmeal.getId()));
        setmealDishService.saveBatch(setmealDishes);
    }

//    @Override
//    public Page<SetmealDto> getPage(Integer page, Integer pageSize, String name) {
//        Page<Setmeal> PageInfo = new Page<>(page, pageSize);
//        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
//        LambdaQueryWrapper<Setmeal> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        objectLambdaQueryWrapper.like(name != null, Setmeal::getName, name);
//        objectLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
//        page(PageInfo, objectLambdaQueryWrapper);
//        BeanUtils.copyProperties(PageInfo, setmealDtoPage, "records");
//        List<Setmeal> records = PageInfo.getRecords();
//        List<SetmealDto> collect = records.stream().map(item -> {
//            SetmealDto setmealDto = new SetmealDto();
//            BeanUtils.copyProperties(item, setmealDto);
//            String categoryName = categoryService.getById(item.getCategoryId()).getName();
//            setmealDto.setCategoryName(categoryName);
//            return setmealDto;
//        }).collect(Collectors.toList());
//        setmealDtoPage.setRecords(collect);
//
//        return setmealDtoPage;
//    }

    public Page<SetmealDto> getPage(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryChainWrapper<Setmeal> queryWrapper = lambdaQuery();
        queryWrapper.like(name != null, Setmeal::getName, name)
                .orderByDesc(Setmeal::getUpdateTime)
                .page(pageInfo);

        return (Page<SetmealDto>) pageInfo.convert(
                setmeal -> {
                    SetmealDto setmealDto = new SetmealDto();
                    BeanUtils.copyProperties(setmeal, setmealDto);
                    String categoryName = categoryService.getById(setmeal.getCategoryId()).getName();
                    setmealDto.setCategoryName(categoryName);
                    return setmealDto;
                }
        );
    }

    @Override
    public SetmealDto getSetmeal(Long ids) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = getById(ids);
        List<SetmealDish> list = setmealDishService.list(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, ids));
        String name = categoryService.getById(setmeal.getCategoryId()).getName();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(list);
        setmealDto.setCategoryName(name);
        return setmealDto;
    }
//    @Override
//    public SetmealDto getSetmeal(Long ids) {
//        SetmealDto setmealDto = new SetmealDto();
//        Setmeal setmeal = getById(ids);
//        List<SetmealDish> list = setmealDishService.list(new QueryWrapper<SetmealDish>()
//                .select("setmeal_dish.*")
//                .leftJoin("setmeal", "setmeal.id = setmeal_dish.setmeal_id")
//                .eq("setmeal.id", ids));
//        String name = categoryService.getById(setmeal.getCategoryId()).getName();
//        BeanUtils.copyProperties(setmeal, setmealDto);
//        setmealDto.setSetmealDishes(list);
//        setmealDto.setCategoryName(name);
//        return setmealDto;
//    }

    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishService.updateBatchById(setmealDishes);

    }

    @Override
    public void delete(List<Long> ids) {

        Long count = lambdaQuery().eq(Setmeal::getStatus, 1).in(Setmeal::getId, ids).count();
        if (count > 0) {
            throw new CustomException("有售卖的产品不能删除");
        }
        this.removeByIds(ids);

        setmealDishService.lambdaUpdate().in(SetmealDish::getSetmealId, ids).remove();
    }

}
