package exercise;

import io.javalin.Javalin;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public final class App {

    private static final List<Map<String, String>> USERS = Data.getUsers();

    public static Javalin getApp() {

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        app.get("/users", ctx -> {
            var pageNumber = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
            var perPage = ctx.queryParamAsClass("per", Integer.class).getOrDefault(5);
            var pageCount = USERS.size() / perPage;
            JSONArray array = new JSONArray();
            for (int i = 0; i < pageCount; i++) {
                for (int j = 0; j < perPage; j++) {
                    if (i == pageNumber - 1) {
                        array.put(USERS.get(i * perPage + j));
                    }
                }
            }
            ctx.result(String.valueOf(array));
        });

        return app;

    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }
}
