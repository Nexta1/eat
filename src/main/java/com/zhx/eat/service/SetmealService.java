package com.zhx.eat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhx.eat.Dto.SetmealDto;
import com.zhx.eat.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void add(SetmealDto setmealDto);

    Page<SetmealDto> getPage(Integer page, Integer pageSize, String name);

    SetmealDto getSetmeal(Long ids);

    void updateSetmeal(SetmealDto setmealDto);

    void delete(List<Long> ids);
}
