package com.exchangerat.job.error;


 
public enum ResponseErrorCodeEnum {
    /** 请求参数错误 E001 */
    DATE_NOT_MATCH("E001", "請求參數錯誤"),
   
     
    // 成功
    SUCCESS("0000", "成功");
   
    /** 状态代码 */
    private final String code;
    /** 状态代码含义描述 */
    private final String desc;
   
    private ResponseErrorCodeEnum(String code, String desc) {
      this.code = code;
      this.desc = desc;
    }
   
    public String getCode() {
      return code;
    }
   
    public String getDesc() {
      return desc;
    }
   
    public static ResponseErrorCodeEnum of(Integer code) {
      ResponseErrorCodeEnum status[] = ResponseErrorCodeEnum.values();
      for (ResponseErrorCodeEnum statu : status) {
        if (statu.code.equals(code)) {
          return statu;
        }
      }
      return null;
    }
  }
