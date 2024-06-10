package com.example.simpletodo.settings.commonVal

import com.example.simpletodo.settings.exception.ErrorResponse

class ApiResponse<T>(val isSuccess: Boolean, val payload: T?, val error: ErrorResponse?) {

    constructor(isSuccess: Boolean, payload: T?) : this(isSuccess, payload, null)
    constructor(isSuccess: Boolean, error: ErrorResponse) : this(isSuccess, null, error)
}