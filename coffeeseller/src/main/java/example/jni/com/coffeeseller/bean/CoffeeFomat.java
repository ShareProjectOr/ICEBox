package example.jni.com.coffeeseller.bean;

import java.util.List;

import cof.ac.inter.ContainerConfig;
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
    private int temp;
    private List<ContainerConfig> containerConfigs;

  /*  public void cupAdd() {
        if (cup < coffee.restNum) {
            cup += 1;
        }
    }*/

    public void cupSub() {
        if (cup > 1) {
            cup -= 1;
        }
    }

  /*  public boolean isMax() {
        return cup == coffee.restNum;
    }*/

    public boolean isMin() {
        return cup == 1;
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

    public Coffee getCoffee() {
        return coffee;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public List<ContainerConfig> getContainerConfigs() {
        return containerConfigs;
    }

    public void setContainerConfigs(List<ContainerConfig> containerConfigs) {
        this.containerConfigs = containerConfigs;
    }
}
