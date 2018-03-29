package example.jni.com.coffeeseller.bean;

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
    private int coffeeId;
    private int cup = 0;

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

    public void cupAdd() {
        cup += 1;
    }

    public void cupSub() {
        if (cup > 0) {
            cup -= 1;
        }
    }
}
