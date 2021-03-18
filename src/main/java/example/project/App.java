package example.project;

import io.jooby.AccessLogHandler;
import io.jooby.Jooby;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class App extends Jooby {

    public App() {
        decorator(new AccessLogHandler());

        get("/non-blocking", ctx ->
                Mono.fromCallable(() -> "data/test.json")
                        .map(this::readJsonAsString)
        );
        onStarted(this::started);
    }

    public static void main(final String[] args) { //NOSONAR
        runApp(args, App::new);
    }

    private void started() {
        log.info("\n\n::::::::::::: Example Project has been started :::::::::::::\n");
    }

    @SneakyThrows
    public String readJsonAsString(String resourcePath) {
        String result = null;
        InputStream is = ClassLoader.getSystemResourceAsStream(resourcePath);
        if (is != null) {
            result = IOUtils.toString(is, StandardCharsets.UTF_8);
        } else {
            URL resourceUrl = this.getClass().getClassLoader().getResource(resourcePath);
            if (resourceUrl != null) {
                File file = new File(resourceUrl.getFile());
                result = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            }
        }
        return result;
    }
}
