package br.com.vemser.devlandapi.enums;

import java.util.Arrays;

public enum TipoClassificacao {
    RESIDENCIAL(1), COMERCIAL(2);

    private int tipo;

    TipoClassificacao(int tipo) {
        this.tipo = tipo;
    }

    public Integer getTipo() {
        return this.tipo;
    }

    public static TipoClassificacao ofTipo(Integer tipo) {
        return Arrays.stream(TipoClassificacao.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}
