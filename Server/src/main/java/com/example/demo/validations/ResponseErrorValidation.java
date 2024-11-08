package com.example.demo.validations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidation { // для обработки ошибок валидации

    // Преобразует ошибки валидации из `BindingResult` в `ResponseEntity`, чтобы отправить их клиенту
    public ResponseEntity<Object> mapValidationService(BindingResult result) {
        if (result.hasErrors()) { // Проверяет, есть ли ошибки
            Map<String, String> errorMap = new HashMap<>();

            if(!CollectionUtils.isEmpty(result.getAllErrors())){
                for(ObjectError error : result.getAllErrors()){
                    errorMap.put(error.getCode(), error.getDefaultMessage()); // добавляет в мапу все ошибки
                }
            }

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage()); // добавляет в мапу все ошибки
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST); // возвращает мапу и 400 ошибку
        }
        return null;
    }

}
