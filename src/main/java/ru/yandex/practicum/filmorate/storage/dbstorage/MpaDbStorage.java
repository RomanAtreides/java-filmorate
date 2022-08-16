package ru.yandex.practicum.filmorate.storage.dbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findMpaById(Integer mpaId) {
        String sqlQuery = "SELECT rating_id, rating_name FROM ratings WHERE rating_id = ?";

        return jdbcTemplate.query(sqlQuery, new MpaMapper(), mpaId).stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "SELECT rating_id, rating_name FROM ratings";
        return jdbcTemplate.query(sqlQuery, new MpaMapper());
    }
}
