package com.zhx.eat.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhx.eat.Dto.SetmealDto;
import com.zhx.eat.common.R;
import com.zhx.eat.entity.Setmeal;
import com.zhx.eat.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto) {
        log.info("{}", setmealDto);
        setmealService.add(setmealDto);
        return R.success("新城套餐成功");
    }

    //    /**
//     * 分页查询
//     * @param page
//     * @param pageSize
//     * @param name
//     * @return
//     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> getPage(Integer page, Integer pageSize, String name) {
        Page<SetmealDto> pageInfo = setmealService.getPage(page, pageSize, name);
        return R.success(pageInfo);
    }

    //
//    /**
//     * 根据id查询套餐
//     * @param ids
//     * @return
//     */
    @GetMapping("/{ids}")
    public R getSetmeal(@PathVariable Long ids) {
        SetmealDto setmealDto = setmealService.getSetmeal(ids);
        return R.success(setmealDto);
    }

    //
//    /**
//     * 更新套餐信息
//     * @param setmealDto
//     * @return
//     */
    @PutMapping
    public R updateSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateSetmeal(setmealDto);
        return R.success("更新套餐成功!");
    }

    //
//    /**
//     * 修改销售状态
//     * @param status
//     * @param ids
//     * @return
//     */
    @PostMapping("/status/{status}")
    public R changeStatus(@PathVariable int status, String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        setmealService.lambdaUpdate().in(Setmeal::getId, idList).set(Setmeal::getStatus, status).update();
//        for (String id : idList) {
//            Setmeal setmeal = new Setmeal();
//            setmeal.setId(Long.parseLong(id));
//            setmeal.setStatus(status);
//
//            setmealService.updateById(setmeal);
//        }
        return R.success("更新状态成功");
    }

    @DeleteMapping
    public R delete(@RequestParam List<Long> ids) {
        setmealService.delete(ids);
        return R.success("删除套餐成功！");
    }
//    @GetMapping("/list")
//    public R getList(Long categoryId,Integer status){
//        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Setmeal::getStatus,status).eq(Setmeal::getCategoryId,categoryId);
//        List<Setmeal> list = setmealService.list(wrapper);
//        return R.success(list);
//    }
//
//    @GetMapping("/dish/{id}")
//    public R getSetMeal(@PathVariable Long id){
//        Setmeal setmeal = setmealService.getById(id);
//        return R.success(setmeal);
//    }

}

