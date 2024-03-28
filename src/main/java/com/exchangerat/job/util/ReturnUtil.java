// package com.exchangerat.job.util;

// import java.io.IOException;
// import java.net.URISyntaxException;

// import org.springframework.stereotype.Component;

// import com.exchangerat.job.error.ResponseErrorCode;
// import com.exchangerat.job.error.ResponseErrorCodeEnum;
// import com.exchangerat.job.model.ReturnModel;

// @Component
// public class ReturnUtil  {
    
    
// 	public static ReturnModel getDataReturnError(ResponseErrorCode responseErrorCode)
// 			throws IOException, URISyntaxException {
 
// 		ReturnModel returnModel = new ReturnModel();
// 		String errorCode = responseErrorCode.getCode();
// 		returnModel.setStatus(0);
// 		// returnModel.setData();
// 		returnModel.setErrorCode(errorCode);
// 		returnModel.setErrorMsg(ResponseErrorCodeEnum.of(errorCode).getDesc());
 
// 		return returnModel;
// 	}



// }
