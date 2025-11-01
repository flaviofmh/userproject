package com.user.project.User.Project.infrastructure.exception;

import com.user.project.User.Project.domain.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_GENERAL_ERROR_USER_FINAL
            = "An unexpected internal system error has occurred. Please try again and if the problem persists, contact " +
            "the system administrator.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        IssueType problemType = IssueType.ERR_SYSTEM;
        String detail = MSG_GENERAL_ERROR_USER_FINAL;

        ex.printStackTrace();

        var problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        IssueType problemType = IssueType.ERR_BUSINESS;
        String detail = ex.getMessage();

        var problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(jakarta.validation.ConstraintViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        IssueType problemType = IssueType.INVALID_PARAMETER;
        String detail = "One or more parameters are invalid.";

        List<Issue.Field> problemFields = ex.getConstraintViolations().stream()
                .map(cv -> Issue.Field.builder()
                        .name(cv.getPropertyPath().toString())
                        .userMessage(cv.getMessage())
                        .build())
                .collect(Collectors.toList());

        Issue problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        IssueType problemType = IssueType.INVALID_PARAMETER;
        String detail = "One or more parameters are invalid.";

        BindingResult bindingResult = ex.getBindingResult();

        List<Issue.Field> problemFields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> Issue.Field.builder()
                        .name(fieldError.getField())
                        .userMessage(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        Issue problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (body == null) {
            body = Issue.builder()
                    .timestamp(LocalDateTime.now())
                    .title("Error")
                    .status(statusCode.value())
                    .userMessage(MSG_GENERAL_ERROR_USER_FINAL)
                    .build();
        } else if (body instanceof String) {
            body = Issue.builder()
                    .timestamp(LocalDateTime.now())
                    .title((String) body)
                    .status(statusCode.value())
                    .userMessage(MSG_GENERAL_ERROR_USER_FINAL)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private Issue.Builder createProblemBuilder(HttpStatusCode status, IssueType problemType, String detail) {
        return Issue.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

}
