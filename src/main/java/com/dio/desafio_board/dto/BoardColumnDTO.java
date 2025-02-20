package com.dio.desafio_board.dto;
import com.dio.desafio_board.persistence.entity.BoardColumnKindEnum;
public record BoardColumnDTO(Long id,
                             String name,
                             BoardColumnKindEnum kind,
                             int cardsAmount) {
}