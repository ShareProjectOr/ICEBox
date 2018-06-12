package otherutis;

/**
 * @author 刘建呈
 *
 */
public enum MachineTypeEnum {
	
	ZeroZero("00","没有错误"),ZeroOne("01","客户端串口打开失败"),ZeroTwo("02","榨汁机应答超时"),
	TwoOne("21","排杆上失败"),TwoTwo("22",""),TwoThree("23","关门失败(取杯)"),
	TwoFour("24","出杯处有杯子没取走"),TwoFive("25","排杆下失败"),TwoEight("28","移动到C处失败"),
	TwoNine("29","落杯失败"),TwoA("2a","移动到D处失败"),TwoB("2b","开门失败"),
	TwoC("2c","注杯失败,无橙"),TwoD("2d","注杯失败,落橙超时(卡橙)"),TwoE("2e","落盖失败"),
	TwoF("2f","注杯失败,用橙超限"),ThreeZero("30","转盘微动开关信号错误(同时检测到有效信号)"),ThreeOne("31","落料准备超时"),
	FE("fe","开关门异常"),FF("ff","榨汁电机复位超时(20s)"),ONETHOUNDS("1000","一次通信失败"),
	THOUNDONE("1001","多次通信失败且超时"),THOUNDTWO("1002","榨汁超时"),THOUNDTHREE("1003","榨汁请求被拒绝"),
	;
	private String key;
	private String value;

	private MachineTypeEnum(String value, String key) {
		this.value = value;
		this.key = key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public static String getValueByKey(String key) {
		for (MachineTypeEnum e : values()) {
			if (e.getKey().equals(key)) {
				return e.getValue();
			}
		}
		return null;
	}

	public static String getKeyByValue(String value) {
		for (MachineTypeEnum e : values()) {
			if (e.getValue().equals(value)) {
				return e.getKey();
			}
		}
		return null;
	}
}
