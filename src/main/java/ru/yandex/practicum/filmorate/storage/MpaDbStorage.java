package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component("mpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findMpaById(Integer mpaId) {
        String sqlQuery = "SELECT rating_id, rating_name FROM ratings WHERE rating_id = ?";

        Mpa mpa = jdbcTemplate.query(sqlQuery, new MpaMapper(), mpaId).stream()
                .findAny()
                .orElse(null);

        return mpa;
    }

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "SELECT rating_id, rating_name FROM ratings";
        List<Mpa> allMpa = jdbcTemplate.query(sqlQuery, new MpaMapper());

        return allMpa;
    }
}
