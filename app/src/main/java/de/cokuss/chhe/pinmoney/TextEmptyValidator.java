package de.cokuss.chhe.pinmoney;

import InputValidator.validators.Validator;

class TextEmptyValidator implements Validator {

    @Override
    public boolean validate(String input) {
        return !(input == null || input.isEmpty());
    }

    @Override
    public String getValidationMessage() {
        return "Bitte Tragen Sie hier etwas ein!";
    }

}

