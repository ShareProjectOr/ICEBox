package example.jni.com.coffeeseller.bean;

import example.jni.com.coffeeseller.utils.CoffeeFomatInterface;

/**
 * Created by WH on 2018/3/22.
 * 购买时的Coffee规格
 */
    /*
    * coffeeId:选择的咖啡标识
    * cup:咖啡数量
    *   :加糖规格
    *   ：加奶规格
    *   :加水规格
    *   ...
    * */
public class CoffeeFomat {
    private Coffee coffee;
    private int coffeeId;
    private int cup = 1;
    private int surgerFomat = CoffeeFomatInterface.SUGER_NORMAL;
    private int milkFomat = CoffeeFomatInterface.MILK_NORMAL;

    public void cupAdd() {
        if (cup < coffee.restNum) {
            cup += 1;
        }
    }

    public void cupSub() {
        if (cup > 1) {
            cup -= 1;
        }
    }

    public boolean isMax() {
        return cup == coffee.restNum;
    }

    public boolean isMin() {
        return cup == 1;
    }

    public int getSurgerFomat() {
        return surgerFomat;
    }

    public void setSurgerFomat(int surgerFomat) {
        this.surgerFomat = surgerFomat;
    }

    public int getMilkFomat() {
        return milkFomat;
    }

    public void setMilkFomat(int milkFomat) {
        this.milkFomat = milkFomat;
    }

    public int getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(int coffeeId) {
        this.coffeeId = coffeeId;
    }

    public int getCup() {
        return cup;
    }

    public void setCup(int cup) {
        this.cup = cup;
    }

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
    }
}
