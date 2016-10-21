/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tintin.validator;

import com.tintin.model.HRCardRec;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author maxhsieh
 */
@Component
public class HRCardRecFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return HRCardRec.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //HRCardRec hrcardrec = (HRCardRec) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "idno", "NotEmpty.hrcardrecForm.idno");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "empno", "NotEmpty.hrcardrecForm.empno");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "readdt", "NotEmpty.hrcardrecForm.readdt");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cardtype", "NotEmpty.hrcardrecForm.cardtype");

    }

}
