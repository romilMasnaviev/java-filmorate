package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    //test name parameter
    private static final String filmWithoutName = "{\"id\":1,\"name\":\"\",\"description\":\"1\",\"releaseDate\":\"2000-01-01\",\"duration\":100}";


    //test description parameter
    private static final String film100CharsDescription = "{\"id\":1," +
            "\"name\":1\"\"," +
            "\"description\":\"kjdnjfdnjdanjnjfdnjdfnjndfjndjfnjdfnjdfjdfakldjsnfkld" +
            "asjnflkdjnalkfdnjskksfljnelkjnfsalkjlfnealknfki" +
            "\"," +
            "\"releaseDate\":\"2000-01-01\"" +
            ",\"duration\":100,\"likes\":[]}";

    private static final String film199CharsDescription = "{\"id\":1," +
            "\"name\":1\"\"," +
            "\"description\":\"kjdnjfdnjdanjnjfdnjdfnjndfjndjfnjdfnjdfjdfakldjsnfklda" +
            "sjnflkdjnalkfdnjskndsafkjnsakfndksanfdksajnfdsakjnfsalkjnfsakjnfkjsanfdskajfndaskjfndakfjnsakj" +
            "sakfjnsdaknfaskljnfdlkjnaskjansfkdasjnfsalkjfnaslkd" +
            "\"," +
            "\"releaseDate\":\"2000-01-01\"" +
            ",\"duration\":100,\"likes\":[]}";

    private static final String film200CharsDescription = "{\"id\":1," +
            "\"name\":1\"\"," +
            "\"description\":\"kjdnjfdnjdanjnjfdnjdfnjndfjndjfnjdfnjdfjdfakldjsnfklda" +
            "sjnflkdjnalkfdnjskndsafkjnsakfndksanfdksajnfdsakjnfsalkjnfsakjnfkjsanfds" +
            "kajfndaskjfndakfjnsakjsakfjnsdaknfaskljnfdlkjnasfkjansfkdasjnfsalkjfnasl" +
            "kd" +
            "\"," +
            "\"releaseDate\":\"2000-01-01\"" +
            ",\"duration\":100,\"likes\":[]}";

    private static final String film201CharsDescription = "{\"id\":1," +
            "\"name\":1\"\"," +
            "\"description\":\"kjdnjfdnjdanjnjfdnjdfnjndfjndjfnjdfnjdfjdfakldjsnfklda" +
            "sjnflkdjnalkfdnjskndsafkjnsakfndksadnfdksajnfdsakjnfsalkjnfsakjnfkjsanfd" +
            "skajfndaskjfndakfjnsakjsakfjnsdaknfaskljnfdlkjnasfkjansfkdasjnfsalkjfnas" +
            "lkd" +
            "\"," +
            "\"releaseDate\":\"2000-01-01\"" +
            ",\"duration\":100}";

    private static final String film300CharsDescription = "{\"id\":1," +
            "\"name\":1\"\"," +
            "\"description\":\"kjdnjfdnjdanjnjfdnjdfnjndfjndjfnjdfnjdfjdfakldjsnfkldasjnf" +
            "lkdjnalkfdnjskksfljnelkjnfsalkjlfnealknfkiskjnfgslkjgnsjkgnkljngdsnrgskjsdnj" +
            "ksdgrnkjnjkgrsnjkgrnjgrsnjnjsrgnjrdnsjnjsrgnjksdlrgjnngjdsrlnjsdgrlnjnjkrgsn" +
            "jsrgnjlnsdrgnjdgrsdrsglnjnjdslrgnjlkjnsrgjnksdrjndnjrgsnjdrgsnjgdrsjnkrgjlnl" +
            "jnrsgnjlkrhdhj" +
            "\"," +
            "\"releaseDate\":\"2000-01-01\"" +
            ",\"duration\":100}";


    //test date parameter
    private static final String filmDateEarlier1895_12_28 = "{\"id\":1,\"name\":1\"\",\"description\":\"1\",\"releaseDate\":\"1894-01-01\",\"duration\":100}";
    private static final String filmDate1895_12_27 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"1895-12-27\",\"duration\":100}";
    private static final String filmDate1895_12_28 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"1895-12-28\",\"duration\":100,\"likes\":[]}";
    private static final String filmDate2000_12_28 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"2000-12-28\",\"duration\":100,\"likes\":[]}";


    //test duration parameter
    private static final String filmDurationIsMinus100 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"2000-12-28\",\"duration\":-100}";
    private static final String filmDurationIsMinus1 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"2000-12-28\",\"duration\":-1}";
    private static final String filmDurationIs0 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"2000-12-28\",\"duration\":0}";
    private static final String filmDurationIs1 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"2000-12-28\",\"duration\":1,\"likes\":[]}";
    private static final String filmDurationIs100 = "{\"id\":1,\"name\":1\"\",\"description\":1\"1\",\"releaseDate\":\"2000-12-28\",\"duration\":100,\"likes\":[]}";


    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private static FilmController filmController;

    @BeforeEach
    public void setup() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
    }

    @Test
    public void addFilm_EmptyName_GetValidationException() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(filmWithoutName, Film.class)));
    }

    @Test
    public void addFilm_100CharsDescription_GetValidationException() {
        Film expectedFilm = gson.fromJson(film100CharsDescription, Film.class);
        Film film = filmController.addFilm(gson.fromJson(film100CharsDescription, Film.class));
        assertEquals(expectedFilm, film);
    }

    @Test
    public void addFilm_199CharsDescription_GetValidationException() {
        Film expectedFilm = gson.fromJson(film199CharsDescription, Film.class);
        Film film = filmController.addFilm(gson.fromJson(film199CharsDescription, Film.class));
        assertEquals(expectedFilm, film);
    }

    @Test
    public void addFilm_200CharsDescription_GetAddedFilm() {
        Film expectedFilm = gson.fromJson(film200CharsDescription, Film.class);
        Film film = filmController.addFilm(gson.fromJson(film200CharsDescription, Film.class));
        System.out.println(gson.toJson(film));
        assertEquals(expectedFilm, film);
    }

    @Test
    public void addFilm_201CharsDescription_GetAddedFilm() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(film201CharsDescription, Film.class)));
    }

    @Test
    public void addFilm_300CharsDescription_GetAddedFilm() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(film300CharsDescription, Film.class)));
    }

    @Test
    public void addFilm_ReleaseDateEarlier1895_12_28_GetValidationException() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(filmDateEarlier1895_12_28, Film.class)));
    }

    @Test
    public void addFilm_ReleaseDate1895_12_27_GetValidationException() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(filmDate1895_12_27, Film.class)));
    }

    @Test
    public void addFilm_ReleaseDate1895_12_28_GetAddedFilm() {
        Film expectedFilm = gson.fromJson(filmDate1895_12_28, Film.class);
        Film film = filmController.addFilm(gson.fromJson(filmDate1895_12_28, Film.class));
        assertEquals(expectedFilm, film);
    }

    @Test
    public void addFilm_ReleaseDate2000_12_28_GetAddedFilm() {
        Film expectedFilm = gson.fromJson(filmDate2000_12_28, Film.class);
        Film film = filmController.addFilm(gson.fromJson(filmDate2000_12_28, Film.class));
        assertEquals(expectedFilm, film);
    }

    @Test
    public void addFilm_DurationIsMinus100_GetValidationException() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(filmDurationIsMinus100, Film.class)));
    }

    @Test
    public void addFilm_DurationIsMinus1_GetValidationException() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(filmDurationIsMinus1, Film.class)));
    }

    @Test
    public void addFilm_DurationIs0_GetValidationException() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(gson.fromJson(filmDurationIs0, Film.class)));
    }

    @Test
    public void addFilm_DurationIs1_GetValidationException() {
        Film expectedFilm = gson.fromJson(filmDurationIs1, Film.class);
        Film film = filmController.addFilm(gson.fromJson(filmDurationIs1, Film.class));
        assertEquals(expectedFilm, film);
    }

    @Test
    public void addFilm_filmDurationIs100_GetValidationException() {
        Film expectedFilm = gson.fromJson(filmDurationIs100, Film.class);
        Film film = filmController.addFilm(gson.fromJson(filmDurationIs100, Film.class));
        assertEquals(expectedFilm, film);
    }

}