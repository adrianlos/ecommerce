package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public final class UserRegistrationRequestValidator implements Validator {

    private final UserService userService;

    private final AddressValidator addressValidator;

    private final UrlValidator urlValidator = new UrlValidator();

    private final EmailValidator emailValidator = new EmailValidator(false, true, DomainValidator.getInstance());

    private final PasswordValidator passwordValidator = new PasswordValidator(
            new LengthRule(8, 32),
            new CharacterCharacteristicsRule(
                    3,
                new CharacterRule(EnglishCharacterData.LowerCase, 5),
                new CharacterRule(EnglishCharacterData.UpperCase, 5),
                new CharacterRule(EnglishCharacterData.Digit),
                new CharacterRule(EnglishCharacterData.Special)));

    @Override
    public boolean supports(Class<?> aClass) {
        return Objects.equals(aClass, UserRegistrationRequest.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        val request = (UserRegistrationRequest) o;

        if (StringUtils.isEmpty(request.getLogin())) {
            errors.rejectValue("login", "EMPTY");
        } else if(request.getLogin().length() >= 255) {
            errors.rejectValue("login", "TOO_LONG");
        } else if(!isEmail(request.getLogin())) {
            errors.rejectValue("login", "NOT_AN_EMAIL");
        } else if(userService.get(request.getLogin()).isPresent()) {
            errors.rejectValue("login", "ALREADY_USED");
        }

        if (StringUtils.isEmpty(request.getPassword())) {
            errors.rejectValue("password", "EMPTY");
        } else if(request.getPassword().length() >= 256) {
            errors.rejectValue("password", "TOO_LONG");
        } else if(!isValidPassword(request.getPassword())) {
            errors.rejectValue("password", "INVALID");
        }

        if (request.getRole() == null) {
            errors.rejectValue("role", "EMPTY");
        }

        if (request.getContactPreference() == null) {
            errors.rejectValue("contact_preference", "EMPTY");
        }

        errors.setNestedPath("address");
        addressValidator.validate(request.getAddress(), errors);
        errors.setNestedPath("");

        if (StringUtils.isEmpty(request.getAvatarUrl())) {
            errors.rejectValue("avatarUrl", "EMPTY");
        } else if(request.getAvatarUrl().length() >= 256) {
            errors.rejectValue("avatarUrl", "TOO_LONG");
        } else if(!urlValidator.isValid(request.getAvatarUrl())) {
            errors.rejectValue("avatarUrl", "INVALID");
        }
    }

    private boolean isEmail(String login) {
        return emailValidator.isValid(login);
    }

    private boolean isValidPassword(String password) {
        return passwordValidator.validate(new PasswordData(password)).isValid();
    }
}
