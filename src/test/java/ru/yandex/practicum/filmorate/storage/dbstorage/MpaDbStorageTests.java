package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTests {
    private final MpaStorage mpaStorage;

    @Test
    public void shouldFindMpaById() {
        int gMpaId = 1;
        int pgMpaId = 2;
        int pg13MpaId = 3;
        int rMpaId = 4;
        int nc17MpaId = 5;

        String mpa01Name = mpaStorage.findMpaById(gMpaId).getName();
        String mpa02Name = mpaStorage.findMpaById(pgMpaId).getName();
        String mpa03Name = mpaStorage.findMpaById(pg13MpaId).getName();
        String mpa04Name = mpaStorage.findMpaById(rMpaId).getName();
        String mpa05Name = mpaStorage.findMpaById(nc17MpaId).getName();

        assertThat(mpa01Name).isEqualTo("G");
        assertThat(mpa02Name).isEqualTo("PG");
        assertThat(mpa03Name).isEqualTo("PG-13");
        assertThat(mpa04Name).isEqualTo("R");
        assertThat(mpa05Name).isEqualTo("NC-17");
    }

    @Test
    public void shouldFindAllMpa() {
        int mpaCount = 5;

        assertThat(mpaStorage.findAll().size()).isEqualTo(mpaCount);
    }
}
