package ru.ash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class App {

    public static void main(String[] args) {
        String dirName = "ppm";
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        int w = 16 * 60;  // 960
        int h = 9 * 60;   // 540
        int frames = 110;  // Количество кадров
        float timeStep = 0.1f; // Шаг времени между кадрами

        // Создаем рендерер
        ExactRaymarchingRenderer renderer = new ExactRaymarchingRenderer(w, h);

        System.out.println("Starting rendering of " + frames + " frames...");
        System.out.println("Resolution: " + w + "x" + h);
        System.out.println("Output directory: " + dirName);

        for (int frame = 0; frame < frames; frame++) {
            // Создаём имя файла с leading zeros: output_00.ppm, output_01.ppm, ...
            String outputPath = String.format("%s/output_%02d.ppm", dirName, frame);

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                // Записываем заголовок PPM
                String header = String.format("P6\n%d %d\n255\n", w, h);
                fos.write(header.getBytes());

                // Вычисляем время для этого кадра
                float time = frame * timeStep;

                // Рендерим кадр с помощью RaymarchingRenderer
                System.out.print("Rendering frame " + frame + "/" + frames +
                        " (time=" + String.format("%.1f", time) + ")... ");

                long startTime = System.currentTimeMillis();
                byte[] pixels = renderer.renderFrame(time);
                long renderTime = System.currentTimeMillis() - startTime;

                // Записываем пиксели в файл
                fos.write(pixels);

                System.out.println("done in " + renderTime + "ms");

            } catch (IOException e) {
                System.err.println("Error writing file " + outputPath + ": " + e.getMessage());
            }
        }

        System.out.println("\nSuccessfully generated " + frames + " files in directory '" + dirName + "'");

        // Дополнительная информация
        System.out.println("\nFiles created:");
        for (int frame = 0; frame < frames; frame++) {
            String filename = String.format("output_%02d.ppm", frame);
            File file = new File(dirName + "/" + filename);
            if (file.exists()) {
                System.out.println("  - " + filename + " (" +
                        String.format("%.1f", file.length() / 1024.0) + " KB)");
            }
        }
    }
}
