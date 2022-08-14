package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa findMpaById(Integer mpaId) {
        Mpa mpa = mpaStorage.findMpaById(mpaId);

        validate(mpa);
        return mpa;
    }

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public void validate(Mpa mpa) {
        if (mpa == null) {
            throw new MpaNotFoundException("Рейтинг с таким id не найден!");
        }

        if (mpa.getId() < 0) {
            throw new MpaNotFoundException("id не может быть отрицательным!");
        }
    }
}
