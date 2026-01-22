package ru.ash;

import ru.ash.matrix.Mat2;
import ru.ash.vectors.Vec2;
import ru.ash.vectors.Vec3;
import ru.ash.vectors.Vec4;


class RaymarchingRenderer {
    private final int width;
    private final int height;

    // Константы из шейдера
    private static final int MAX_STEPS = 50;
    private static final float TANH_SCALE = 0.09f;
    private static final float MAX_DISTANCE = 100f;
    private static final float MIN_DISTANCE = 0.001f;
    private static final boolean WRITE_LOGS = Boolean.FALSE;

    public RaymarchingRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public byte[] renderFrame(float time) {
        byte[] pixels = new byte[width * height * 3];
        int pixelIndex = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 1. Нормализованные координаты пикселя
                Vec3 pixelCoord = normalizePixelCoords(x, y);

                // 2. Raymarching цикл
                Vec4 color = rayMarch(pixelCoord, time);

                // 3. Постобработка (аналог: tanh(o/7e1))
                color = tanh(color.divide(TANH_SCALE));

                // 4. Конвертация в RGB байты
                byte[] rgb = color.toRGBBytesMapped();
                pixels[pixelIndex++] = rgb[0]; // R
                pixels[pixelIndex++] = rgb[1]; // G
                pixels[pixelIndex++] = rgb[2]; // B
            }
        }

        return pixels;
    }


    Vec3 normalizePixelCoords(int x, int y) {
        // Аналог: FC.rgb*2.-.xyy
        // Преобразуем экранные координаты в [-1, 1]
        float u = (2f * x / width) - 1f;
        float v = (2f * y / height) - 1f;
        return new Vec3(u, v, 0).normalize();
    }

    float sceneSDF(Vec3 p) {
        // Аналог: max(length(cos(p/.2))/8., length(clamp(p,-3.,3.)-p))

        // 1. Первая часть: length(cos(p/.2))/8.
        Vec3 p1 = p.divide(0.2f);
        Vec3 cosP1 = new Vec3(
                (float)Math.cos(p1.x),
                (float)Math.cos(p1.y),
                (float)Math.cos(p1.z)
        );
        float d1 = cosP1.length() / 8f;

        // 2. Вторая часть: clamp(p,-3.,3.) - p
        Vec3 clamped = new Vec3(
                Math.max(-3f, Math.min(3f, p.x)),
                Math.max(-3f, Math.min(3f, p.y)),
                Math.max(-3f, Math.min(3f, p.z))
        );
        float d2 = clamped.subtract(p).length();

        return Math.max(d1, d2);
    }

    Vec4 rayMarch(Vec3 rayDir, float time) {
        float z = 0; // начальная глубина
        Vec4 accumulatedColor = new Vec4(0, 0, 0, 0);

        if (WRITE_LOGS) {
            System.out.println("=== Raymarching Debug ===");
            System.out.println("Ray direction: " + rayDir);
            System.out.println("Time: " + time);
        }

        for (int i = 0; i < MAX_STEPS; i++) {
            // 1. Позиция камеры
            Vec3 p = rayDir.multiply(z);
            p.z += 8f; // смещение камеры
            if (WRITE_LOGS) {
                System.out.println("Step " + i + ": z=" + z + ", p=" + p);
            }

            // 2. Вращение
            Mat2 rotation = createRotationMatrix(time);
            Vec2 xz = new Vec2(p.x, p.z);
            xz = rotation.multiply(xz);
            p.x = xz.x;
            p.z = xz.y;

            // 3. Расстояние до сцены
            float distance = sceneSDF(p);
            z += distance;
            if (WRITE_LOGS) {
                System.out.println("  SDF distance: " + distance);
            }


            // 4. Накопление цвета
            if (distance < 0.001f || z > 100f) break;

            Vec4 colorContribution = calculateColor(p, time, distance, z);
            if (WRITE_LOGS) {
                System.out.println("  Color contribution: " + colorContribution);
            }

            accumulatedColor = accumulatedColor.add(colorContribution);

        }
        if (WRITE_LOGS) {
            System.out.println("Final accumulated color: " + accumulatedColor);
        }
        return accumulatedColor;
    }

    Vec4 calculateColor(Vec3 p, float time, float d, float z) {
        // cos(dot(cos(p), sin(p/.6).yzx) + t + vec4(0,1,2,3)) + 1.1

        // 1. cos(p)
        Vec3 cosP = new Vec3(
                (float)Math.cos(p.x),
                (float)Math.cos(p.y),
                (float)Math.cos(p.z)
        );

        // 2. sin(p/.6).yzx (перестановка компонентов)
        Vec3 pDiv = p.divide(0.6f);
        Vec3 sinP = new Vec3(
                (float)Math.sin(pDiv.y), // y из sin(p/.6)
                (float)Math.sin(pDiv.z), // z из sin(p/.6)
                (float)Math.sin(pDiv.x)  // x из sin(p/.6)
        );

        // 3. dot product
        float dot = cosP.dot(sinP);

        // 4. Создание vec4 с разными фазами
        Vec4 phases = new Vec4(
                dot + time,
                dot + time + 1f,
                dot + time + 2f,
                dot + time + 3f
        );

        // 5. Косинус и смещение
        Vec4 color = new Vec4(
                (float)Math.cos(phases.x),
                (float)Math.cos(phases.y),
                (float)Math.cos(phases.z),
                (float)Math.cos(phases.w)
        ).add(1.1f);

        // 6. Деление на расстояние и глубину
        return color.divide(d * z);
    }

    // Функция tanh (гиперболический тангенс)
    Vec4 tanh(Vec4 v) {
        return new Vec4(
                (float)Math.tanh(v.x),
                (float)Math.tanh(v.y),
                (float)Math.tanh(v.z),
                (float)Math.tanh(v.w)
        );
    }

    // Создание матрицы вращения
    Mat2 createRotationMatrix(float time) {
        float angle = time / 4f;
        float cosA = (float)Math.cos(angle);
        float sinA = (float)Math.sin(angle);
        return new Mat2(cosA, -sinA, sinA, cosA);
    }
}
