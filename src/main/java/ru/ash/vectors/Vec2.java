package ru.ash.vectors;

public class Vec2 {
    public float x, y;

    // Конструкторы
    public Vec2() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(float value) {
        this.x = value;
        this.y = value;
    }

    // Конструктор из массива
    public Vec2(float[] values) {
        if (values.length >= 2) {
            this.x = values[0];
            this.y = values[1];
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
        }
    }

    // Копирующий конструктор
    public Vec2(Vec2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    // Арифметические операции

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 add(float value) {
        return new Vec2(x + value, y + value);
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 subtract(float value) {
        return new Vec2(x - value, y - value);
    }

    public Vec2 multiply(Vec2 other) {
        return new Vec2(x * other.x, y * other.y);
    }

    public Vec2 multiply(float scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    public Vec2 divide(Vec2 other) {
        return new Vec2(x / other.x, y / other.y);
    }

    public Vec2 divide(float scalar) {
        return new Vec2(x / scalar, y / scalar);
    }

    // Скалярное произведение
    public float dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    // Длина вектора
    public float length() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    // Нормализация
    public Vec2 normalize() {
        float len = length();
        if (len == 0.0f) return new Vec2(0, 0);
        return divide(len);
    }

    public Vec2 normalizeSelf() {
        float len = length();
        if (len != 0.0f) {
            x /= len;
            y /= len;
        }
        return this;
    }

    // Перпендикулярный вектор (90 градусов)
    public Vec2 perpendicular() {
        return new Vec2(-y, x);
    }

    // Угол вектора (в радианах)
    public float angle() {
        return (float)Math.atan2(y, x);
    }

    // Поэлементные математические функции
    public Vec2 sin() {
        return new Vec2((float)Math.sin(x), (float)Math.sin(y));
    }

    public Vec2 cos() {
        return new Vec2((float)Math.cos(x), (float)Math.cos(y));
    }

    public Vec2 abs() {
        return new Vec2(Math.abs(x), Math.abs(y));
    }

    // Ограничение значений
    public Vec2 clamp(float minVal, float maxVal) {
        return new Vec2(
                Math.max(minVal, Math.min(maxVal, x)),
                Math.max(minVal, Math.min(maxVal, y))
        );
    }

    public Vec2 clamp(Vec2 minVec, Vec2 maxVec) {
        return new Vec2(
                Math.max(minVec.x, Math.min(maxVec.x, x)),
                Math.max(minVec.y, Math.min(maxVec.y, y))
        );
    }

    // Получение максимальной/минимальной компоненты
    public float maxComponent() {
        return Math.max(x, y);
    }

    public float minComponent() {
        return Math.min(x, y);
    }

    // Проверка на ноль (с допуском)
    public boolean isZero(float epsilon) {
        return Math.abs(x) < epsilon && Math.abs(y) < epsilon;
    }

    // Поворот вектора на угол (в радианах)
    public Vec2 rotate(float angle) {
        float cosA = (float)Math.cos(angle);
        float sinA = (float)Math.sin(angle);
        return new Vec2(
                x * cosA - y * sinA,
                x * sinA + y * cosA
        );
    }

    // Линейная интерполяция
    public static Vec2 lerp(Vec2 a, Vec2 b, float t) {
        return new Vec2(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t
        );
    }

    // Расстояние между двумя векторами
    public static float distance(Vec2 a, Vec2 b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    // Статические методы

    public static Vec2 add(Vec2 a, Vec2 b) {
        return new Vec2(a.x + b.x, a.y + b.y);
    }

    public static Vec2 subtract(Vec2 a, Vec2 b) {
        return new Vec2(a.x - b.x, a.y - b.y);
    }

    public static Vec2 multiply(Vec2 a, float scalar) {
        return new Vec2(a.x * scalar, a.y * scalar);
    }

    public static float dot(Vec2 a, Vec2 b) {
        return a.x * b.x + a.y * b.y;
    }

    // Создание стандартных векторов
    public static Vec2 zero() {
        return new Vec2(0, 0);
    }

    public static Vec2 one() {
        return new Vec2(1, 1);
    }

    public static Vec2 unitX() {
        return new Vec2(1, 0);
    }

    public static Vec2 unitY() {
        return new Vec2(0, 1);
    }

    // Создание вектора из угла (в радианах)
    public static Vec2 fromAngle(float angle) {
        return new Vec2((float)Math.cos(angle), (float)Math.sin(angle));
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vec2 vec2 = (Vec2) obj;
        return Float.compare(vec2.x, x) == 0 &&
                Float.compare(vec2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        return result;
    }

    // Геттеры и сеттеры для совместимости с различными стилями

    public float getX() { return x; }
    public float getY() { return y; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Получение в виде массива
    public float[] toArray() {
        return new float[] {x, y};
    }
}
