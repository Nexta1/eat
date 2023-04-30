package com.zhx.eat.Dto;


import com.zhx.eat.entity.Setmeal;
import com.zhx.eat.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
