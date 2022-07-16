package br.com.vemser.devlandapi.enums;

import java.util.Arrays;

public enum TipoUsuario {
    DEV(1), EMPRESA(2);

    private int tipo;

    TipoUsuario(int tipo) {
        this.tipo = tipo;
    }

    public Integer getTipo() {
        return this.tipo;
    }

    public static TipoUsuario ofTipo(Integer tipo) {
        return Arrays.stream(TipoUsuario.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}
