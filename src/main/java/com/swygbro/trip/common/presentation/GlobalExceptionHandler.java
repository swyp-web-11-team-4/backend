package com.swygbro.trip.common.presentation;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swygbro.trip.common.domain.dto.ApiResponse;
import com.swygbro.trip.common.domain.dto.ErrorResponse;
import com.swygbro.trip.common.domain.dto.ErrorResponse.FieldError;
import com.swygbro.trip.common.domain.exception.BusinessException;
import com.swygbro.trip.common.domain.exception.CommonErrorCode;
import com.swygbro.trip.common.domain.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외 처리 핸들러입니다.
 *
 * <p>애플리케이션에서 발생하는 모든 예외를 일관된 형식으로 처리하여 클라이언트에 응답합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * BusinessException을 처리합니다.
   *
   * @param e BusinessException
   * @param request HttpServletRequest
   * @return 에러 응답
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(
      BusinessException e, HttpServletRequest request) {
    log.warn(
        "BusinessException occurred: code={}, message={}",
        e.getErrorCode().getCode(),
        e.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.of(e.getErrorCode().getCode(), e.getMessage(), request.getRequestURI());

    ApiResponse<ErrorResponse> response =
        ApiResponse.error(e.getErrorCode().getHttpStatus(), errorResponse);

    return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
  }

  /**
   * BindException을 처리합니다.
   *
   * <p>바인딩 또는 검증 실패 시 발생합니다. MethodArgumentNotValidException과 BindException을 모두 처리합니다.
   *
   * @param e BindException
   * @param request HttpServletRequest
   * @return 에러 응답
   */
  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<ApiResponse<ErrorResponse>> handleBindException(
      BindException e, HttpServletRequest request) {
    log.warn("Validation or binding failed: {}", e.getMessage());

    List<ErrorResponse.FieldError> fieldErrors = extractFieldErrors(e.getBindingResult());
    ErrorResponse errorResponse = createErrorResponse(request.getRequestURI(), fieldErrors);

    ApiResponse<ErrorResponse> response = ApiResponse.error(HttpStatus.BAD_REQUEST, errorResponse);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * HttpMessageNotReadableException을 처리합니다.
   *
   * <p>JSON 파싱 실패 또는 타입 변환 실패 시 발생합니다.
   *
   * @param e HttpMessageNotReadableException
   * @param request HttpServletRequest
   * @return 에러 응답
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e, HttpServletRequest request) {
    log.warn("HTTP message not readable: {}", e.getMessage());

    ErrorResponse errorResponse =
        createErrorResponse(CommonErrorCode.INVALID_TYPE, request.getRequestURI());

    ApiResponse<ErrorResponse> response = ApiResponse.error(HttpStatus.BAD_REQUEST, errorResponse);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * MissingServletRequestParameterException을 처리합니다.
   *
   * <p>필수 요청 파라미터가 누락되었을 때 발생합니다.
   *
   * @param e MissingServletRequestParameterException
   * @param request HttpServletRequest
   * @return 에러 응답
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiResponse<ErrorResponse>> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e, HttpServletRequest request) {
    log.warn("Missing request parameter: {}", e.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.of(
            CommonErrorCode.MISSING_PARAMETER.getCode(),
            String.format("필수 파라미터 '%s'가 누락되었습니다.", e.getParameterName()),
            request.getRequestURI());

    ApiResponse<ErrorResponse> response = ApiResponse.error(HttpStatus.BAD_REQUEST, errorResponse);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * 처리되지 않은 모든 예외를 처리합니다.
   *
   * @param e Exception
   * @param request HttpServletRequest
   * @return 에러 응답
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<ErrorResponse>> handleException(
      Exception e, HttpServletRequest request) {
    log.error("Unexpected exception occurred", e);

    ErrorResponse errorResponse =
        createErrorResponse(CommonErrorCode.INTERNAL_ERROR, request.getRequestURI());

    ApiResponse<ErrorResponse> response =
        ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  /**
   * BindingResult에서 FieldError 목록을 추출합니다.
   *
   * @param bindingResult BindingResult
   * @return FieldError 목록
   */
  private List<ErrorResponse.FieldError> extractFieldErrors(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream()
        .map(
            error ->
                ErrorResponse.FieldError.of(
                    error.getField(), error.getRejectedValue(), error.getDefaultMessage()))
        .toList();
  }

  /**
   * ErrorCode를 기반으로 ErrorResponse를 생성합니다.
   *
   * @param errorCode ErrorCode
   * @param path 요청 경로
   * @return ErrorResponse
   */
  private ErrorResponse createErrorResponse(ErrorCode errorCode, String path) {
    return ErrorResponse.of(errorCode.getCode(), errorCode.getDefaultMessage(), path);
  }

  /**
   * ErrorCode와 FieldError 목록을 기반으로 ErrorResponse를 생성합니다.
   *
   * @param path 요청 경로
   * @param fieldErrors FieldError 목록
   * @return ErrorResponse
   */
  private ErrorResponse createErrorResponse(String path, List<FieldError> fieldErrors) {
    return ErrorResponse.of(
        CommonErrorCode.INVALID_INPUT.getCode(),
        CommonErrorCode.INVALID_INPUT.getDefaultMessage(),
        path,
        fieldErrors);
  }
}
