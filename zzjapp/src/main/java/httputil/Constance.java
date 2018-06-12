package httputil;

public interface Constance {
   // String HOST_URL = "http://10.21.161.45:8080";
   // String HOST_URL = "http://202.98.157.25:80";
    //  String HOST_URL = "http://10.21.161.45:8080";
    //  String HOST_URL = "http://192.168.4.218:8080";
    //String HOST_URL = "http://10.21.161.45:8080";
    // String HOST_URL = "http://192.168.4.158:8080";
   String HOST_URL = "http://119.23.71.185:8080";
    String UPLOAD_APP_EXCEPTION_URL = HOST_URL + "/Upload/GetLog";
    String GET_APP_VERSION_URL = HOST_URL + "/Upload/GetAppURL";
    String DOWN_APP_URL = HOST_URL + "/Upload/download";
    String LOGIN_URL = HOST_URL + "/User/Login";
    String GET_USER_LIST_URL = HOST_URL + "/User/Search";
    String REPAIR_PASSEORD_URL = HOST_URL + "/User/SavePWD";

    String EDIT_MANAGER_URL = HOST_URL + "/User/Save";
    String CREATE_SYS_MANAGER_URL = HOST_URL + "/User/AddAdmin";
    String CREATE_AGENT_URL = HOST_URL + "/User/AddAgent";
    String CREATE_MACHINE_MANAGER = HOST_URL + "/User/AddOP";

    String GET_MACHINE_LIST_URL = HOST_URL + "/Machine/Search";
    String BIND_MACHINE_FOR_AGENT_URL = HOST_URL + "/Machine/BindAgent";
    String BIND_MACHINE_FOR_MACHINE_MANAGER_URL = HOST_URL + "/Machine/BindManager";


    String ADDRESS_EXCEL_NAME = "provence_code_excel.xls";

    String GET_TRADE_LIST_URL = HOST_URL + "/Trade/Search";
    String GET_TRADE_ITEM_CONTENT_URL = HOST_URL + "/Trade/Get";
    String TRADE_REFUND_URL = HOST_URL + "/Trade/tradeBack";
    String GET_ACCOUNT_LOG_LIST_URL = HOST_URL + "/AccountLog/Search";
    String ADD_MACHINE_URL = HOST_URL + "/Machine/AddMachine";
    String MACHINE_SHUNTDOWN_URL = HOST_URL + "/Machine/Shutdown";

    String GET_NEED_DEAL_EXCEPTION = HOST_URL + "/Exception/NeedDeal";
    String CHANGE_USER_ACTIVATEDTYPE = HOST_URL + "/User/ToggleActivatedType";
    String CHANGE_MACHINE_ACTIVATEDTYPE = HOST_URL + "/Machine/ToggleActivated";
    String EDIT_MACHINE_CONTENT_URL = HOST_URL + "/Machine/Save";
    String GET_SINGLE_MACHINE_URL = HOST_URL + "/Machine/Get";
    String GET_MACHINE_EXCEPTION_URL = HOST_URL + "/Exception/Search";

    String GET_MACHINE_TRADE_TOTAL = HOST_URL + "/Trade/GetTotal";
    String GET_MACHINE_TRAND_TREND = HOST_URL + "/Trade/GetLine";
    String GET_EXCEPTION_TOTAL = HOST_URL + "/Exception/GetTotal";
    String GET_EXCEPTION_TREND = HOST_URL + "/Exception/GetLine";
    String GET_DIVIDER_LIST_URL = HOST_URL + "/Divide/Search";
    String GET_DIVIDER_URL = HOST_URL + "/Divide/Get";
    String HAVE_SEND_URL = HOST_URL + "/Divide/Success";
    String TAG_HAVE_CHECK_URL = HOST_URL + "/Divide/Review";
    String EDIT_DIVIDER_URL = HOST_URL + "/Divide/Save";
}
