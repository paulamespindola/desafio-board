package com.dio.desafio_board.dto;

import com.dio.desafio_board.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}