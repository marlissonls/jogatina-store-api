package br.com.jogatinastore.model.user.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    @Named("cleanCpf")
    public String cleanCpf(String cpf) {
        return cpf != null ? cpf.replaceAll("\\D", "") : null;
    }

    @Named("formatCpf")
    public String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}