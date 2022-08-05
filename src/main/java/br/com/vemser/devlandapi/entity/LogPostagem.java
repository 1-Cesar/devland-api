package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;


@Document(collection = "log_postagem")
@Getter
@Setter
public class LogPostagem {
    @Id
    private Integer idPostagem;

    @Enumerated(EnumType.STRING)
    private TipoPostagem tipoPostagem;

    private LocalDateTime data;

}
