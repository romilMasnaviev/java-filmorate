package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {

    private MpaDbStorage mpaDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup(){
        mpaDbStorage = new MpaDbStorage(jdbcTemplate);
    }

    @Test
    void getMpaTest(){
        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();
        assertEquals(mpa,mpaDbStorage.getMpa(1));
    }

    @Test
    void getAllMpa(){
        Mpa mpa1 =  Mpa.builder().id(1).name("G").build();
        Mpa mpa2 =  Mpa.builder().id(2).name("PG").build();
        Mpa mpa3 =  Mpa.builder().id(3).name("PG-13").build();
        Mpa mpa4 =  Mpa.builder().id(4).name("R").build();
        Mpa mpa5 =  Mpa.builder().id(5).name("NC-17").build();
        List<Mpa> mpas = new ArrayList<>();
        mpas.add(mpa1);
        mpas.add(mpa2);
        mpas.add(mpa3);
        mpas.add(mpa4);
        mpas.add(mpa5);
        assertEquals(mpas,mpaDbStorage.getAllMpa());
    }
}
