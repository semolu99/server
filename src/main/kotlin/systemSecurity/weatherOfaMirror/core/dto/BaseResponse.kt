package systemSecurity.weatherOfaMirror.core.dto

import systemSecurity.weatherOfaMirror.core.status.ResultCode

data class BaseResponse<T> (
    val resultCode: String = ResultCode.SUCCESS.name,
    val data: T? = null,
    val message: String = ResultCode.SUCCESS.msg,
)