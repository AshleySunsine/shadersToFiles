package ru.ash.vectors;

public class Vec4 {
    public float x, y, z, w;

    // Конструкторы
    public Vec4() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4(float value) {
        this.x = value;
        this.y = value;
        this.z = value;
        this.w = value;
    }

    public Vec4(Vec3 v, float w) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public Vec4(float x, Vec3 v) {
        this.x = x;
        this.y = v.x;
        this.z = v.y;
        this.w = v.z;
    }

    // Копирующий конструктор
    public Vec4(Vec4 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }

    // Арифметические операции

    public Vec4 add(Vec4 other) {
        return new Vec4(x + other.x, y + other.y, z + other.z, w + other.w);
    }

    public Vec4 add(float value) {
        return new Vec4(x + value, y + value, z + value, w + value);
    }

    public Vec4 subtract(Vec4 other) {
        return new Vec4(x - other.x, y - other.y, z - other.z, w - other.w);
    }

    public Vec4 subtract(float value) {
        return new Vec4(x - value, y - value, z - value, w - value);
    }

    public Vec4 multiply(Vec4 other) {
        return new Vec4(x * other.x, y * other.y, z * other.z, w * other.w);
    }

    public Vec4 multiply(float scalar) {
        return new Vec4(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public Vec4 divide(Vec4 other) {
        return new Vec4(x / other.x, y / other.y, z / other.z, w / other.w);
    }

    public Vec4 divide(float scalar) {
        return new Vec4(x / scalar, y / scalar, z / scalar, w / scalar);
    }

    // Скалярное произведение (для совместимости, хотя для Vec4 это менее распространено)
    public float dot(Vec4 other) {
        return x * other.x + y * other.y + z * other.z + w * other.w;
    }

    // Длина вектора
    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    // Нормализация
    public Vec4 normalize() {
        float len = length();
        if (len == 0.0f) return new Vec4(0, 0, 0, 0);
        return divide(len);
    }

    public Vec4 normalizeSelf() {
        float len = length();
        if (len != 0.0f) {
            x /= len;
            y /= len;
            z /= len;
            w /= len;
        }
        return this;
    }

    // Поэлементные математические функции
    public Vec4 sin() {
        return new Vec4(
                (float)Math.sin(x),
                (float)Math.sin(y),
                (float)Math.sin(z),
                (float)Math.sin(w)
        );
    }

    public Vec4 cos() {
        return new Vec4(
                (float)Math.cos(x),
                (float)Math.cos(y),
                (float)Math.cos(z),
                (float)Math.cos(w)
        );
    }

    // Гиперболический тангенс (очень важно для нашего шейдера!)
    public Vec4 tanh() {
        return new Vec4(
                (float)Math.tanh(x),
                (float)Math.tanh(y),
                (float)Math.tanh(z),
                (float)Math.tanh(w)
        );
    }

    public Vec4 abs() {
        return new Vec4(
                Math.abs(x),
                Math.abs(y),
                Math.abs(z),
                Math.abs(w)
        );
    }

    // Ограничение значений
    public Vec4 clamp(float minVal, float maxVal) {
        return new Vec4(
                Math.max(minVal, Math.min(maxVal, x)),
                Math.max(minVal, Math.min(maxVal, y)),
                Math.max(minVal, Math.min(maxVal, z)),
                Math.max(minVal, Math.min(maxVal, w))
        );
    }

    // Получение первых трех компонент как Vec3
    public Vec3 xyz() {
        return new Vec3(x, y, z);
    }

    public Vec3 yzw() {
        return new Vec3(y, z, w);
    }

    // Получение компонент в другом порядке (для GLSL совместимости)
    public Vec4 yzwx() {
        return new Vec4(y, z, w, x);
    }

    public Vec4 zwxy() {
        return new Vec4(z, w, x, y);
    }

    public Vec4 wxyz() {
        return new Vec4(w, x, y, z);
    }

    // Получение максимальной/минимальной компоненты
    public float maxComponent() {
        return Math.max(Math.max(x, y), Math.max(z, w));
    }

    public float minComponent() {
        return Math.min(Math.min(x, y), Math.min(z, w));
    }

    // Проверка на ноль (с допуском)
    public boolean isZero(float epsilon) {
        return Math.abs(x) < epsilon &&
                Math.abs(y) < epsilon &&
                Math.abs(z) < epsilon &&
                Math.abs(w) < epsilon;
    }

    // Линейная интерполяция
    public static Vec4 lerp(Vec4 a, Vec4 b, float t) {
        return new Vec4(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t,
                a.z + (b.z - a.z) * t,
                a.w + (b.w - a.w) * t
        );
    }

    // Статические методы

    public static Vec4 add(Vec4 a, Vec4 b) {
        return new Vec4(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
    }

    public static Vec4 subtract(Vec4 a, Vec4 b) {
        return new Vec4(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
    }

    public static Vec4 multiply(Vec4 a, float scalar) {
        return new Vec4(a.x * scalar, a.y * scalar, a.z * scalar, a.w * scalar);
    }

    public static float dot(Vec4 a, Vec4 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }

    // Создание стандартных векторов
    public static Vec4 zero() {
        return new Vec4(0, 0, 0, 0);
    }

    public static Vec4 one() {
        return new Vec4(1, 1, 1, 1);
    }

    // Для цветов (RGBA)
    public static Vec4 color(float r, float g, float b, float a) {
        return new Vec4(r, g, b, a);
    }

    public static Vec4 color(Vec3 rgb, float a) {
        return new Vec4(rgb.x, rgb.y, rgb.z, a);
    }

    // Конвертация в массив байтов для PPM (игнорируем w-компонент)
    public byte[] toRGBBytes() {
        // Преобразуем float [0,1] в байты [0,255]
        // Ограничиваем значения и отбрасываем w-компонент
        int r = (int)(Math.max(0, Math.min(1, x)) * 255);
        int g = (int)(Math.max(0, Math.min(1, y)) * 255);
        int b = (int)(Math.max(0, Math.min(1, z)) * 255);

        return new byte[] {
                (byte)(r & 0xFF),
                (byte)(g & 0xFF),
                (byte)(b & 0xFF)
        };
    }


    public byte[] toRGBBytesMapped() {
        // Для шейдера: значения могут быть за пределами [0,1] после tanh
        // Tanh дает значения в диапазоне [-1, 1], преобразуем в [0, 1]
        float r = (x + 1.0f) * 0.5f;
        float g = (y + 1.0f) * 0.5f;
        float b = (z + 1.0f) * 0.5f;

        // Clamp на всякий случай
        r = Math.max(0, Math.min(1, r));
        g = Math.max(0, Math.min(1, g));
        b = Math.max(0, Math.min(1, b));

        return new byte[] {
                (byte)((int)(r * 255) & 0xFF),
                (byte)((int)(g * 255) & 0xFF),
                (byte)((int)(b * 255) & 0xFF)
        };
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %.3f, %.3f)", x, y, z, w);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vec4 vec4 = (Vec4) obj;
        return Float.compare(vec4.x, x) == 0 &&
                Float.compare(vec4.y, y) == 0 &&
                Float.compare(vec4.z, z) == 0 &&
                Float.compare(vec4.w, w) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        result = 31 * result + Float.floatToIntBits(w);
        return result;
    }
}
