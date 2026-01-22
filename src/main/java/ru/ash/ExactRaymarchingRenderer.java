package ru.ash;

import ru.ash.matrix.Mat2;
import ru.ash.vectors.Vec2;
import ru.ash.vectors.Vec3;
import ru.ash.vectors.Vec4;

public class ExactRaymarchingRenderer {
    private final int width;
    private final int height;

    // Константы из шейдера
    private static final int MAX_STEPS = 50;      // 5e1
    private static final float TANH_SCALE = 70f;  // 7e1

    // Для r.xyy - возможно это uniform или что-то подобное
    // В оригинале это могла быть позиция мыши или другой параметр
    // Для начала используем (0.5, 0.5, 0.5)
    private final Vec3 r = new Vec3(0.5f, 0.5f, 0.5f);

    public ExactRaymarchingRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // ТОЧНАЯ нормализация координат как в шейдере
    private Vec3 normalizePixelCoords(int x, int y) {
        // FC.rgb*2.-r.xyy
        // FC - fragCoord (координаты пикселя в [0,1] или [0,width/height])
        // В GLSL: FC.rgb это vec3(uv, 0) обычно

        float u = (float)x / width;
        float v = (float)y / height;

        // FC.rgb * 2.0 - r.xyy
        Vec3 fc = new Vec3(u, v, 0);
        Vec3 result = fc.multiply(2.0f).subtract(new Vec3(r.x, r.y, r.y));

        // normalize(...)
        return result.normalize();
    }

    // ТОЧНЫЙ raymarching цикл как в шейдере
    private Vec4 rayMarch(Vec3 rayDir, float time) {
        float z = 0.0f; // начальная глубина
        Vec4 o = new Vec4(0, 0, 0, 0); // накопленный цвет

        // ТОЧНО как в шейдере: for(float i,z,d,s,c;i++<5e1;)
        for (int i = 0; i < MAX_STEPS; i++) {
            // vec3 p = z * normalize(FC.rgb*2.-r.xyy);
            // Но мы уже сделали normalize, так что:
            Vec3 p = rayDir.multiply(z);

            // p.z += 8.
            p.z += 8.0f;

            // p.xz *= mat2(cos(t/4.+vec4(0,33,11,0)))
            float angle = time / 4.0f;
            // vec4(0,33,11,0) - возможно это шум или фазы,
            // но в контексте mat2(cos(...)) это вероятно просто заполнитель
            // mat2(cos(angle), -sin(angle), sin(angle), cos(angle))
            Mat2 rotation = Mat2.rotation(angle);
            Vec2 xz = new Vec2(p.x, p.z);
            xz = rotation.multiply(xz);
            p.x = xz.x;
            p.z = xz.y;

            // z += d = max(length(cos(p/.2))/8., length(clamp(p,-3.,3.)-p))
            Vec3 pDiv = p.divide(0.2f);
            Vec3 cosP = pDiv.cos();
            float d1 = cosP.length() / 8.0f;

            Vec3 clamped = new Vec3(
                    Math.max(-3.0f, Math.min(3.0f, p.x)),
                    Math.max(-3.0f, Math.min(3.0f, p.y)),
                    Math.max(-3.0f, Math.min(3.0f, p.z))
            );
            float d2 = clamped.subtract(p).length();

            float d = Math.max(d1, d2);
            z += d;

            // o += (cos(dot(cos(p), sin(p/.6).yzx) + t + vec4(0,1,2,3)) + 1.1) / d / z

            // cos(p)
            Vec3 cosP2 = p.cos();

            // sin(p/.6).yzx
            Vec3 sinP = p.divide(0.6f).sin().yzx();

            // dot(cos(p), sin(p/.6).yzx)
            float dotVal = cosP2.dot(sinP);

            // cos(dot(...) + t + vec4(0,1,2,3)) + 1.1
            Vec4 phases = new Vec4(
                    dotVal + time,
                    dotVal + time + 1.0f,
                    dotVal + time + 2.0f,
                    dotVal + time + 3.0f
            );

            Vec4 colorAdd = phases.cos().add(1.1f);

            // / d / z
            if (d > 0.0001f && z > 0.0001f) {
                colorAdd = colorAdd.divide(d * z);
                o = o.add(colorAdd);
            }

            // Ранний выход если ушли далеко
            if (z > 100.0f) break;
        }

        return o;
    }

    // Основной метод рендеринга
    public byte[] renderFrame(float time) {
        byte[] pixels = new byte[width * height * 3];
        int pixelIndex = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 1. Точные нормализованные координаты
                Vec3 rayDir = normalizePixelCoords(x, y);

                // 2. Raymarching (точно как в шейдере)
                Vec4 color = rayMarch(rayDir, time);

                // 3. Постобработка: o = tanh(o/7e1)
                color = color.divide(TANH_SCALE).tanh();

                // 4. Преобразование в RGB (используем только xyz, w игнорируем)
                // GLSL обычно выводит в диапазоне [0,1], но у нас tanh дает [-1,1]
                float boost = 1.00f; // более яркие цвета Экспериментируйте
                float r = Math.max(0.0f, Math.min(1.0f, color.x * boost));
                float g = Math.max(0.0f, Math.min(1.0f, color.y * boost));
                float b = Math.max(0.0f, Math.min(1.0f, color.z * boost));

                // Clamp и преобразование
                r = Math.max(0.0f, Math.min(1.0f, r));
                g = Math.max(0.0f, Math.min(1.0f, g));
                b = Math.max(0.0f, Math.min(1.0f, b));

                pixels[pixelIndex++] = (byte)(r * 255);
                pixels[pixelIndex++] = (byte)(g * 255);
                pixels[pixelIndex++] = (byte)(b * 255);
            }

            // Прогресс
            if (y % 50 == 0) {
                System.out.print(".");
            }
        }

        return pixels;
    }

    // Метод для отладки - рендерим только центральный пиксель
    public void debugCenterPixel(float time) {
        int x = width / 2;
        int y = height / 2;

        Vec3 rayDir = normalizePixelCoords(x, y);
        System.out.println("Center pixel rayDir: " + rayDir);

        Vec4 color = rayMarch(rayDir, time);
        System.out.println("Raw accumulated color: " + color);

        color = color.divide(TANH_SCALE).tanh();
        System.out.println("After tanh/70: " + color);
    }
}
