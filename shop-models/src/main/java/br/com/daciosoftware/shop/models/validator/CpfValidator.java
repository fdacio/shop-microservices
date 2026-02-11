package br.com.daciosoftware.shop.models.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CpfValidator implements ConstraintValidator<CPF, String> {

    private static final Pattern ONLY_DIGITS = Pattern.compile("\\d{11}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotNull / @NotBlank cuidar disso
        }

        String cpf = value.replaceAll("\\D", "");

        if (!ONLY_DIGITS.matcher(cpf).matches() || hasAllEqualDigits(cpf)) {
            return false;
        }

        return isValidCpf(cpf);
    }

    private boolean isValidCpf(String cpf) {
        int digito1 = calcularDigito(cpf, 10);
        int digito2 = calcularDigito(cpf, 11);

        return cpf.endsWith("" + digito1 + digito2);
    }

    private int calcularDigito(String cpf, int pesoInicial) {
        int soma = 0;

        for (int i = 0; i < pesoInicial - 1; i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * (pesoInicial - i);
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    private boolean hasAllEqualDigits(String cpf) {
        char first = cpf.charAt(0);
        for (char c : cpf.toCharArray()) {
            if (c != first) {
                return false;
            }
        }
        return true;
    }
}
