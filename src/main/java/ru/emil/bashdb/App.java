package ru.emil.bashdb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class App {

    private static class ShellStream implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        ShellStream(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    public static void main(String[] args) {
        try {

            // Read script
            Path path = Paths.get(Objects.requireNonNull(App.class.getClassLoader().getResource("testscript.sh")).toURI());
            StringBuilder script = new StringBuilder();
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> script.append(line).append("\n"));
            script.deleteCharAt(script.length() - 1);
            lines.close();

            Process process = Runtime.getRuntime().exec(script.toString());


            // ProcessBuilder processBuilder = new ProcessBuilder();
            // processBuilder.command(script.toString());
            // Process process = processBuilder.start();

            ShellStream streamGobbler =
                    new ShellStream(process.getInputStream(), System.out::println);


            OutputStream out = process.getOutputStream();

            if (!Objects.isNull(out)) {
                Scanner scanner = new Scanner(System.in);

                out.write(scanner.nextLine().getBytes());
                out.close();
            }


            Executors.newSingleThreadExecutor().submit(streamGobbler);

            int exitCode = process.waitFor();
            assert exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
