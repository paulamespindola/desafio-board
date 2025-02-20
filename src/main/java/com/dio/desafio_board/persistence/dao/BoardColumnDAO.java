package com.dio.desafio_board.persistence.dao;

import com.dio.desafio_board.persistence.entity.BoardColumnEntity;
import com.dio.desafio_board.persistence.entity.BoardColumnKindEnum;
import com.dio.desafio_board.persistence.entity.CardEntity;
import com.dio.desafio_board.dto.BoardColumnDTO;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS_COLUMNS (name, \"order\", kind, board_id) VALUES (?, ?, ?, ?) RETURNING id;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getName());
            statement.setInt(i++, entity.getOrder());
            statement.setString(i++, entity.getKind().name());
            statement.setLong(i, entity.getBoard().getId());
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity.setId(resultSet.getLong("id"));
            }
            return entity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(final Long boardId) throws SQLException {
        List<BoardColumnEntity> entities = new ArrayList<>();
        var sql = "SELECT id, name, \"order\", kind FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY \"order\";";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(findByName(resultSet.getString("kind")));
                entities.add(entity);
            }
            return entities;
        }
    }

    // 📝 Buscar colunas com detalhes (quantidade de cards)
    public List<BoardColumnDTO> findByBoardIdWithDetails(final Long boardId) throws SQLException {
        List<BoardColumnDTO> dtos = new ArrayList<>();
        var sql = """
                  SELECT bc.id,
                         bc.name,
                         bc.kind,
                         (SELECT COUNT(c.id)
                            FROM CARDS c
                           WHERE c.board_column_id = bc.id) AS cards_amount
                    FROM BOARDS_COLUMNS bc
                   WHERE board_id = ?
                   ORDER BY "order";
                  """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var dto = new BoardColumnDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        findByName(resultSet.getString("kind")),
                        resultSet.getInt("cards_amount")
                );
                dtos.add(dto);
            }
            return dtos;
        }
    }

    public Optional<BoardColumnEntity> findById(final Long boardColumnId) throws SQLException {
        var sql = """
                  SELECT bc.name,
                         bc.kind,
                         c.id AS card_id,
                         c.title,
                         c.description
                    FROM BOARDS_COLUMNS bc
               LEFT JOIN CARDS c
                      ON c.board_column_id = bc.id
                   WHERE bc.id = ?;
                  """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardColumnId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setName(resultSet.getString("name"));
                entity.setKind(findByName(resultSet.getString("kind")));
                do {
                    if (resultSet.getString("title") == null) break;
                    var card = new CardEntity();
                    card.setId(resultSet.getLong("card_id"));
                    card.setTitle(resultSet.getString("title"));
                    card.setDescription(resultSet.getString("description"));
                    entity.getCards().add(card);
                } while (resultSet.next());
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

    private BoardColumnKindEnum findByName(String name) {
        return BoardColumnKindEnum.findByName(name);
    }
}
